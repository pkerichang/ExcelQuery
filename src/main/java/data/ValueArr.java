package data;

import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import util.Util;

/**
 * A subclass of {@link Value} that represents an array of {@link Value}s.
 */
public class ValueArr extends Value {

    private final List<Value> arr;

    /**
     * Construct a new ValueArr instance.
     *
     * @param arr the list of {@link Value}s this instance represents.
     */
    public ValueArr(List<Value> arr) {
        this.arr = arr;
    }

    @Override
    public String toString() {
        if (arr == null)
            return "";
        StringBuilder b = new StringBuilder();
        for (Value v : arr) {
            b.append(Util.toString(v));
        }
        return b.toString();
    }

    @Override
    public boolean equals(Object v) {
        if (v == null || !(v instanceof ValueArr)) {
            return false;
        }

        ValueArr a = (ValueArr) v;
        return (arr == null) && (a.arr == null) || ((a.arr != null) && Util.listEquals(arr, a.arr, true));
    }

    @Override
    public void writeToCell(Cell c) {
        c.setCellValue(toString());
    }

    @Override
    public String getCellStyleKey() {
        return "string";
    }

    @Override
    public Number getNumericValue() {
        return arr.hashCode();
    }

    @Override
    public int hashCode() {
        return arr.hashCode();
    }
}
