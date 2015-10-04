package io;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import data.Vector;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

/**
 * A simple Excel file reader. It takes the first sheet in the given workbook,
 * reads all the data, and returns the result as list of columns.
 * <p>
 * The data must be stored in consecutive rows. All cells must have either date
 * or number data.
 * </p>
 *
 * @author Eric
 */
public class ExcelReader {

    private final int numField;
    private Sheet sheet;
    private String msg;

    /**
     * Create a simple Excel file reader.
     *
     * @param filename name of the excel workbook
     * @param numField number of cells per row to read
     */
    public ExcelReader(String filename, int numField) {
        this.numField = numField;
        sheet = null;
        msg = "";
        try {
            InputStream inp = new FileInputStream(filename);
            Workbook wb = WorkbookFactory.create(inp);
            sheet = wb.getSheetAt(0);
        } catch (IOException | InvalidFormatException e) {
            msg = e.getMessage();
        }
    }

    /**
     * Returns a 2D double array representing the excel data.
     * <p>
     * In the case of an error, null is returned. In this case, call
     * {@link #getErrMsg()} to see the error message.
     * </p>
     *
     * @return the excel file data.
     */
    public List<Vector> getData() {
        if (sheet == null) {
            return null;
        }

        int size = sheet.getLastRowNum() + 1;
        List<List<Double>> cols = new ArrayList<>(numField);
        for (int i = 0; i < numField; i++) {
            cols.add(new ArrayList<Double>(size));
        }
        for (int i = 0; i < size; i++) {
            Row r = sheet.getRow(i);
            if (r == null) {
                msg = String.format("Error: cannot read row %d", i + 1);
                return null;
            }
            for (int j = 0; j < numField; j++) {
                Cell c = r.getCell(j);
                if (c == null
                        || (c.getCellType() != Cell.CELL_TYPE_NUMERIC && c.getCellType() != Cell.CELL_TYPE_FORMULA)) {
                    msg = String.format("Error: cannot read row %d cell %d", i, j);
                    if (j == 0) {
                        return parse(cols);
                    }
                    return null;
                }

                try {
                    cols.get(j).add(c.getNumericCellValue());
                } catch (Exception ex) {
                    msg = String.format("Error: cannot read row %d cell %d\nMessage: %s", i, j, ex.getMessage());
                    return null;
                }
            }
        }
        return parse(cols);
    }

    private List<Vector> parse(List<List<Double>> cols) {
        List<Vector> ans = new ArrayList<>(cols.size());
        for (List<Double> col : cols) {
            ans.add(new Vector(col));
        }
        return ans;
    }

    /**
     * Returns the error message during parsing. If no errors occur, an empty
     * string is returned.
     *
     * @return the error message.
     */
    public String getErrMsg() {
        return msg;
    }
}
