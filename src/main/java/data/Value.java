package data;

import org.apache.poi.ss.usermodel.Cell;
import util.Util;

import javax.swing.*;

/**
 * A generic class that represents the different possible Excel value types.
 * <p/>
 * Currently it supports String ({@link ValueStr}), Numbers ({@link ValueDouble}), Date
 * ({@link ValueDate}), and array ({@link ValueArr}).
 *
 * @author Eric
 */
public abstract class Value {

    /**
     * Returns a string representation of the attribute value.
     */
    public abstract String toString();

    /**
     * Returns true if this Value is equal to the given Value object
     *
     * @param obj the Value instance to compare to.
     * @return true if the two AttValues are equal.
     */
    public abstract boolean equals(Object obj);

    /**
     * Sets the given Excel cell content to this Value.
     *
     * @param c the Excel cell to set the value of.
     */
    public abstract void writeToCell(Cell c);

    /**
     * Returns a String describing the value type. Can be used to find the
     * CellStyle needed to format an Excel Cell containing this attribute.
     *
     * @return A string describing the value type.
     */
    public abstract String getCellStyleKey();

    /**
     * Returns a numeric representation of this Value.
     *
     * @return a numeric representation of this value.
     */
    public abstract Number getNumericValue();

    /**
     * Format the given double value according to the column index.
     *
     * @param val the double value.
     * @param col the column index
     * @return the Value.
     */
    public static Value format(double val, int col) {
        switch (col) {
            case 0:
                return new ValueDate(val);
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
                if (Util.equalTol(val, -2, 0.1)) {
                    return new ValueStr("X");
                } else if (Util.equalTol(val, -1, 0.1)) {
                    return new ValueStr("B");
                } else if (Util.equalTol(val, 1, 0.1)) {
                    return new ValueStr("A");
                } else if (Util.equalTol(val, 2, 0.1)) {
                    return new ValueStr("T");
                } else {
                    return new ValueStr("O");
                }
            case 15:
                return new ValueStr(Util.equalTol(val, 1, 0.1) ? "PV" : "");
            case 16:
                if (Util.equalTol(val, 0, 0.1)) {
                    return new ValueStr("DY");
                } else if (Util.equalTol(val, 1, 0.1)) {
                    return new ValueStr("LY");
                } else if (Util.equalTol(val, 2, 0.1)) {
                    return new ValueStr("DG");
                } else {
                    return new ValueStr("LG");
                }
            case 17:
                if (Util.equalTol(val, 0, 0.1)) {
                    return new ValueStr("HH");
                } else if (Util.equalTol(val, 1, 0.1)) {
                    return new ValueStr("LL");
                } else if (Util.equalTol(val, 2, 0.1)) {
                    return new ValueStr("GS");
                } else {
                    return new ValueStr("BU");
                }
            case 22:
                if (Util.equalTol(val, 0, 0.1)) {
                    return new ValueStr("B");
                } else if (Util.equalTol(val, 1, 0.1)) {
                    return new ValueStr("S");
                } else if (Util.equalTol(val, 2, 0.1)) {
                    return new ValueStr("V");
                } else {
                    return new ValueStr("L");
                }
            default:
                return new ValueDouble(val);
        }
    }
}
