package util;

import data.Op;
import data.Value;
import data.Vector;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.commons.math3.stat.descriptive.rank.Percentile;

import java.util.List;
import java.util.Map;


/**
 * This class contains miscellaneous utility methods.
 */
public class Util {

    private static Percentile p = new Percentile();

    /**
     * Compute percentiles from given array, then quantize.
     *
     * @param arr the array to quantize
     * @return the quantized array.
     */
    public static Vector categorize(Vector arr) {
        double[] values = arr.getDataCopy();
        p.setData(values);
        final double p2 = p.evaluate(80);
        final double p1 = p.evaluate(60);
        final double n1 = p.evaluate(40);
        final double n2 = p.evaluate(20);
        // System.out.println(String.format("%f, %f, %f, %f", n2, n1, p1, p2));
        return arr.apply(new Op() {
            @Override
            public double apply(double val) {
                if (val < n2) {
                    return -2;
                } else if (val < n1) {
                    return -1;
                } else if (val < p1) {
                    return 0;
                } else if (val < p2) {
                    return 1;
                }
                return 2;
            }
        });
    }

    /**
     * Calculate the moving average from the integral vector.
     *
     * @param integral the integral vector.
     * @param n        number of samples to average.
     * @return the moving average.
     */
    public static Vector movingAverage(Vector integral, int n) {
        Vector ans = integral.sub(integral.shift(-n));
        ans.scalei(1.0 / n);
        return ans;
    }

    /**
     * Converts the given {@link Value} to string.  Returns "*" if {@code v} is null.
     *
     * @param v the {@link Value} to convert.
     * @return a string representing the given {@link Value}.
     */
    public static String toString(Value v) {
        return (v == null) ? "*" : v.toString();
    }

    /**
     * Writes the given value to the given Excel cell.
     *
     * @param c     the Excel cell.
     * @param v     the value to write.  If null, write "*".
     * @param csMap the map from data type to Excel cell style.
     */
    public static void writeToCell(Cell c, Value v, Map<String, CellStyle> csMap) {
        if (v == null) {
            c.setCellValue("*");
        } else {
            v.writeToCell(c);
            c.setCellStyle(csMap.get(v.getCellStyleKey()));
        }
    }

    /**
     * Compares two lists, and return true if and only if each element is identical.
     *
     * @param a        the first list.
     * @param b        the second list.
     * @param wildNull if true, null object matches to anything.
     * @return true if and only if two lists are identical.
     */
    public static boolean listEquals(List<?> a, List<?> b, boolean wildNull) {
        if (a.size() != b.size())
            return false;
        for (int i = 0; i < a.size(); i++) {
            Object a1 = a.get(i);
            Object b1 = b.get(i);
            if (!(wildNull && (a1 == null || b1 == null) ||
                    a1 != null && a1.equals(b1))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns true if the given two floating point numbers are within the specified tolerance.
     *
     * @param a   the first number.
     * @param b   the second number.
     * @param tol the tolerance
     * @return true if the two numbers are within the specifed tolerance.
     */
    public static boolean equalTol(double a, double b, double tol) {
        return Math.abs(a - b) < tol;
    }

}
