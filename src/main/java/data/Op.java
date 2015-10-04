package data;

/**
 * An operation on a floating point number.
 */
public abstract class Op {

    /**
     * Default constructor
     */
    public Op() {

    }

    /**
     * Applies this operation on the given value.
     *
     * @param val the value
     * @return the result.
     */
    public abstract double apply(double val);
}
