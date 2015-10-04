package data;

import java.util.List;

/**
 * A 1-D double Vector with concept of view.
 */
public class Vector {
    private double[] data;
    private int offset;

    /**
     * Initialize the Vector.
     *
     * @param data the underlying data.
     */
    public Vector(double[] data) {
        this(data, 0);
    }

    /**
     * Create a new Vector instance.
     *
     * @param data   the underlying data.
     * @param offset the offset.
     */
    public Vector(double[] data, int offset) {
        this.data = data;
        this.offset = offset;
    }

    /**
     * Intialize the Vector
     *
     * @param data the underlying data.
     */
    public Vector(List<Double> data) {
        int size = data.size();
        offset = 0;
        this.data = new double[size];
        for (int i = 0; i < size; i++) {
            this.data[i + offset] = data.get(i);
        }
    }

    /**
     * Returns the ith data
     *
     * @param i the index
     * @return the ith data.
     */
    public double get(int i) {
        try {
            return data[i + offset];
        } catch (ArrayIndexOutOfBoundsException e) {
            return Double.NaN;
        }
    }

    /**
     * Get a copy of this data.
     *
     * @return a copy of this vector as double array.
     */
    public double[] getDataCopy() {
        double[] ans = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            try {
                ans[i] = data[i + offset];
            } catch (ArrayIndexOutOfBoundsException e) {
                ans[i] = Double.NaN;
            }
        }
        return ans;
    }

    /**
     * Returns the difference vector.
     *
     * @param v vector to subtract.
     * @return the difference vector.
     */
    public Vector sub(Vector v) {
        double[] ans = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            try {
                ans[i] = data[i + offset] - v.data[i + v.offset];
            } catch (ArrayIndexOutOfBoundsException e) {
                ans[i] = Double.NaN;
            }
        }
        return new Vector(ans);
    }

    /**
     * Returns a vector that is 1 if vlist[0] is true, else 2 if
     * vlist[1] is true, and so on.
     *
     * @param vlist the vector list.
     * @return the result vector.
     */
    public static Vector cond(Vector... vlist) {
        int n = vlist.length;
        int n2 = vlist[0].data.length;
        double[] ans = new double[n2];
        for (int i = 0; i < n2; i++) {
            ans[i] = 0;
            for (int j = 0; j < n; j++) {
                if (vlist[j].get(i) == 1) {
                    ans[i] = j + 1;
                    break;
                }
            }
        }
        return new Vector(ans);
    }

    /**
     * Returns the product vector.
     *
     * @param v vector to subtract.
     * @return the product vector.
     */
    public Vector mult(Vector v) {
        double[] ans = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            try {
                ans[i] = data[i + offset] * v.data[i + v.offset];
            } catch (ArrayIndexOutOfBoundsException e) {
                ans[i] = Double.NaN;
            }
        }
        return new Vector(ans);
    }

    /**
     * Returns the difference vector.
     *
     * @param v vector to subtract.
     * @return the difference vector.
     */
    public Vector subi(Vector v) {
        for (int i = 0; i < data.length; i++) {
            try {
                data[i + offset] = data[i + offset] - v.data[i + v.offset];
            } catch (ArrayIndexOutOfBoundsException e) {
                if (0 <= i + offset && i + offset < data.length) {
                    data[i + offset] = Double.NaN;
                }
            }
        }
        return this;
    }

    /**
     * Returns a shifted vesion of this vector.
     * <p/>
     * WARNING: as of this implementation, chaining this call this not work as expected.
     *
     * @param offset positive to shift left, negative to shift right.
     * @return a shifted vector.
     */
    public Vector shift(int offset) {
        return new Vector(data, offset);
    }

    /**
     * Returns the max of this vector and b.
     *
     * @param b the second Vector
     * @return the max.
     */
    public Vector max(Vector b) {
        int n = this.data.length;
        double[] data = new double[n];
        for (int i = 0; i < n; i++) {
            try {
                data[i] = Math.max(this.data[i + this.offset], b.data[i + b.offset]);
            } catch (ArrayIndexOutOfBoundsException e) {
                data[i] = Double.NaN;
            }
        }
        return new Vector(data);
    }

    /**
     * Returns the min of this vector and b.
     *
     * @param b the second Vector
     * @return the min.
     */
    public Vector min(Vector b) {
        int n = this.data.length;
        double[] data = new double[n];
        for (int i = 0; i < n; i++) {
            try {
                data[i] = Math.min(this.data[i + this.offset], b.data[i + b.offset]);
            } catch (ArrayIndexOutOfBoundsException e) {
                data[i] = Double.NaN;
            }
        }
        return new Vector(data);
    }

    /**
     * Returns the (this vector > b)
     *
     * @param b the second Vector
     * @return the new vector
     */
    public Vector gt(Vector b) {
        int n = this.data.length;
        double[] data = new double[n];
        for (int i = 0; i < n; i++) {
            try {
                double a1 = this.data[i + this.offset];
                double a2 = b.data[i + b.offset];
                data[i] = (a1 > a2) ? 1 : 0;
            } catch (ArrayIndexOutOfBoundsException e) {
                data[i] = 0;
            }
        }
        return new Vector(data);
    }

    /**
     * Returns the (this vector > b)
     *
     * @param thres the second Vector
     * @return the new vector
     */
    public Vector gt(double thres) {
        int n = this.data.length;
        double[] data = new double[n];
        for (int i = 0; i < n; i++) {
            try {
                double a1 = this.data[i + this.offset];
                data[i] = (a1 > thres) ? 1 : 0;
            } catch (ArrayIndexOutOfBoundsException e) {
                data[i] = Double.NaN;
            }
        }
        return new Vector(data);
    }

    /**
     * Returns the inverse of this vector.
     *
     * @return the inverse of this vector.
     */
    public Vector not() {
        int n = this.data.length;
        double[] data = new double[n];
        for (int i = 0; i < n; i++) {
            try {
                data[i] = (this.data[i + this.offset] == 0) ? 1 : 0;
            } catch (ArrayIndexOutOfBoundsException e) {
                data[i] = Double.NaN;
            }
        }
        return new Vector(data);
    }

    /**
     * Returns the size of this vector.
     * @return size of this vector.
     */
    public int size() {
        return data.length;
    }

    /**
     * Returns the integral of this vector.
     *
     * @return the inverse of this vector.
     */
    public Vector integral() {
        int n = this.data.length;
        double[] data = new double[n];
        data[0] = get(0);
        for (int i = 1; i < n; i++) {
            try {
                data[i] = this.data[i + this.offset] + data[i - 1];
            } catch (ArrayIndexOutOfBoundsException e) {
                data[i] = Double.NaN;
            }
        }
        return new Vector(data);
    }

    /**
     * Returns the sum of the two vectors.
     *
     * @param b the vector to add
     * @return the sum.
     */
    public Vector add(Vector b) {
        return scaleAdd(1.0, b);
    }

    /**
     * scale this vector, then add in place.
     *
     * @param scale the scale.
     * @param b     the vector to add.
     * @return this vector.
     */
    public Vector scaleAdd(double scale, Vector b) {
        double[] ans = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            try {
                ans[i] = data[i + offset] * scale + b.data[i + b.offset];
            } catch (ArrayIndexOutOfBoundsException ignored) {
            }
        }
        return new Vector(ans);
    }

    /**
     * scale this vector, then add in place.
     *
     * @param scale the scale.
     * @param b     the vector to add.
     * @return this vector.
     */
    public Vector scaleAddi(double scale, Vector b) {
        for (int i = 0; i < data.length; i++) {
            try {
                data[i + offset] = data[i + offset] * scale + b.data[i + b.offset];
            } catch (ArrayIndexOutOfBoundsException e) {
                if (0 <= i + offset && i + offset < data.length) {
                    data[i + offset] = Double.NaN;
                }
            }
        }
        return this;
    }

    /**
     * Applies the given operation to all elements of this vector.
     *
     * @param op the operation to apply.
     * @return the new vector
     */
    public Vector apply(Op op) {
        double[] ans = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            try {
                ans[i] = op.apply(data[i + offset]);
            } catch (ArrayIndexOutOfBoundsException e) {
                ans[i] = Double.NaN;
            }
        }
        return new Vector(ans, 0);
    }

    /**
     * Applies the given operation in-place
     *
     * @param op the operation to apply.
     * @return this vector
     */
    public Vector applyi(Op op) {
        for (int i = 0; i < data.length; i++) {
            try {
                data[i + offset] = op.apply(data[i + offset]);
            } catch (ArrayIndexOutOfBoundsException ignored) {
            }
        }
        return this;
    }


    /**
     * Scales this vector by the given amount.
     *
     * @param scale the scale factor.
     * @return this vector
     */
    public Vector scalei(double scale) {
        for (int i = 0; i < data.length; i++) {
            try {
                data[i + offset] = scale * data[i + offset];
            } catch (ArrayIndexOutOfBoundsException ignored) {
            }
        }
        return this;
    }


}
