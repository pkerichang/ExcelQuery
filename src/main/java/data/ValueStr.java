package data;

import org.apache.poi.ss.usermodel.Cell;

/**
 * A subclass of {@link Value} that represents a string.
 */
public class ValueStr extends Value {
    private final String s;

    /**
     * Create a new ValueStr instance.
     *
     * @param str the String value of this instance.
     */
    public ValueStr(String str) {
        s = str;
    }

    @Override
    public String toString() {
        return s;
    }

    @Override
    public boolean equals(Object v) {
        return v != null && s.equals(v.toString());
    }

    @Override
    public void writeToCell(Cell c) {
        c.setCellValue(s);
    }

    @Override
    public String getCellStyleKey() {
        return "string";
    }

    @Override
    public Number getNumericValue() {
        return s.hashCode();
    }

    @Override
    public int hashCode() {
        return s.hashCode();
    }

}
