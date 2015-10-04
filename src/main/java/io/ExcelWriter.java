package io;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import data.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import data.Value;
import util.Util;

/**
 * A simple Excel file writer. It writes Collections of Entries as rows in an
 * excel file.
 *
 * @author Eric
 */
public class ExcelWriter {

    private final FileOutputStream fileOut;
    private final Workbook wb;
    private final Map<String, CellStyle> csMap;
    private int maxColNum;
    private String msg;

    /**
     * Create a simple Excel file writer.
     *
     * @param filename output file name.
     */
    public ExcelWriter(String filename) {
        msg = "";
        if (filename.endsWith(".xlsx")) {
            wb = new XSSFWorkbook();
        } else if (filename.endsWith(".xls")) {
            wb = new HSSFWorkbook();
        } else {
            wb = null;
            msg = String.format("Error: file %s does not end in .xls or .xlsx", filename);
        }
        if (wb != null) {
            FileOutputStream temp;
            try {
                temp = new FileOutputStream(filename);
            } catch (FileNotFoundException e) {
                msg = String.format("Error: cannot open file %s", filename);
                temp = null;
            }
            fileOut = temp;

            wb.createSheet();
            CreationHelper helper = wb.getCreationHelper();
            DataFormat df = helper.createDataFormat();
            csMap = new HashMap<>();
            CellStyle cs = wb.createCellStyle();
            cs.setDataFormat(df.getFormat("0.00"));
            csMap.put(new ValueDouble(1).getCellStyleKey(), cs);
            cs = wb.createCellStyle();
            cs.setDataFormat(df.getFormat("mm/dd/yyyy"));
            csMap.put(new ValueDate(new Date()).getCellStyleKey(), cs);
            cs = wb.createCellStyle();
            cs.setDataFormat(df.getFormat("text"));
            csMap.put(new ValueStr("").getCellStyleKey(), cs);
        } else {
            fileOut = null;
            csMap = null;
        }

        maxColNum = 0;
    }

    /**
     * Append all the given entries at the end of the excel sheet
     *
     * @param entries list of entries to be written
     */
    public void writeAll(List<List<Value>> entries) {
        if (wb != null) {
            Sheet s = wb.getSheetAt(0);
            for (int i = 0; i < entries.size(); i++) {
                Row r = s.createRow(i);
                List<Value> valList = entries.get(i);
                for (int j = 0; j < valList.size(); j++) {
                    Cell c = r.createCell(j);
                    Util.writeToCell(c, valList.get(j), csMap);
                }
                maxColNum = Math.max(maxColNum, valList.size());
            }
        }
    }

    /**
     * Writes the constructed excel workbook to file.
     */
    public void close() {
        if (wb != null && fileOut != null) {
            Sheet s = wb.getSheetAt(0);
            for (int i = 0; i < maxColNum; i++) {
                s.autoSizeColumn(i);
            }
            try {
                wb.write(fileOut);
                fileOut.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error while writing to file.");
            }
        }
    }

    /**
     * Returns the error message.
     *
     * @return the error message.
     */
    public String getErrorMsg() {
        return msg;
    }
}
