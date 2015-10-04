package data;

import org.apache.poi.ss.usermodel.Cell;
import util.Util;

/**
 * A subclass of {@link Value} that represents a floating point number.
 */
public class ValueDouble extends Value {

    private static final double TOL = 1e-2;

    private final Double val;

    /**
     * Create a new ValueDouble instance.
     *
     * @param d the number this instance represents.
     */
    public ValueDouble(double d) {
        val = d;
    }

    /**
     * Returns the value of this instance.
     *
     * @return the floating point number.
     */
    public double getValue() {
        return val;
    }

    @Override
    public String toString() {
        return String.format("%.2f", val);
    }

    @Override
    public boolean equals(Object v) {
        if (v == null || !(v instanceof ValueDouble))
            return false;
        ValueDouble d = (ValueDouble) v;
        return Util.equalTol(val, d.val, TOL);
    }

    @Override
    public void writeToCell(Cell c) {
        c.setCellValue(val);
    }

    @Override
    public String getCellStyleKey() {
        return "number";
    }

    @Override
    public Number getNumericValue() {
        return val;
    }

    public int hashCode() {
        return val.hashCode();
    }

}
