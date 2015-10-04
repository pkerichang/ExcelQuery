package data;

import java.util.Date;

import org.apache.poi.ss.format.CellDateFormatter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;

/**
 * A subclass of {@link Value} that represents a date.
 */
public class ValueDate extends Value {
    private final Date d;
    private final CellDateFormatter formatter;

    /**
     * Create a new ValueDate instance.
     *
     * @param d the date this instance represents.
     */
    public ValueDate(Date d) {
        this.d = d;
        formatter = new CellDateFormatter("mm/dd/yyyy");
    }

    /**
     * Create a new ValueDate instance from Excel double value.
     *
     * @param val the excel double value representing the date.
     */
    public ValueDate(double val) {
        this(DateUtil.getJavaDate(val));
    }

    /**
     * Returns the {@link Date} value associated with this instance.
     *
     * @return the {@link Date} value.
     */
    public Date getValue() {
        return d;
    }

    @Override
    public String toString() {
        return formatter.format(d);
    }

    @Override
    public boolean equals(Object v) {
        if (!(v instanceof ValueDate))
            return false;
        ValueDate dv = (ValueDate) v;
        return formatter.format(d).equals(formatter.format(dv.d));
    }

    @Override
    public void writeToCell(Cell c) {
        c.setCellValue(d);
    }

    @Override
    public String getCellStyleKey() {
        return "date";
    }

    @Override
    public Number getNumericValue() {
        return d.getTime();
    }

    @Override
    public int hashCode() {
        return d.hashCode();
    }
}
