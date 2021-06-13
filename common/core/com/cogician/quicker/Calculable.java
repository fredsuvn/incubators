package com.cogician.quicker;

/**
 * This interface represents an object is calculable including four operations:
 * addition, subtraction, multiplication, and division. Each operation is a
 * separate interface which can be used alone. The interface
 * {@link FourFundamentalOperations} include all four operations.
 *
 * @author Fred Suvn
 * @version 0.0.0, 2015-03-04 09:42:50
 * @see FourFundamentalOperations
 * @since 0.0.0
 */
public interface Calculable {
    /**
     * This interface represents an object can be added.
     *
     * @author Fred Suvn
     * @version 0.0.0, 2015-03-04 09:55:21
     * @see Calculable
     */
    public static interface Addable<T> {
        /**
         * Adds special instance on this.
         *
         * @param t
         *            special instance
         * @return result after adding
         */
        public T add(T t);
    }

    /**
     * This interface represents an object can be subtracted.
     *
     * @author Fred Suvn
     * @version 0.0.0, 2015-03-04 10:21:41
     * @see Calculable
     */
    public static interface Subtractable<T> {
        /**
         * Subtracts special instance from this.
         *
         * @param t
         *            special instance
         * @return result after subtracting
         */
        public T subtract(T t);
    }

    /**
     * This interface represents an object can be multiplied.
     *
     * @author Fred Suvn
     * @version 0.0.0, 2015-03-04 14:38:02
     * @see Calculable
     */
    public static interface Multiplyable<T> {
        /**
         * Multiplies special instance.
         *
         * @param t
         *            special instance
         * @return result after multiplying
         */
        public T multiply(T t);
    }

    /**
     * This interface represents an object can be divided.
     *
     * @author Fred Suvn
     * @version 0.0.0, 2015-03-04 14:40:05
     * @see Calculable
     */
    public static interface Divideable<T> {
        /**
         * This divides by special instance.
         *
         * @param t
         *            special instance
         * @return result after dividing
         */
        public T divide(T t);
    }

    /**
     * This interface represents an object can be calculated with four
     * fundamental operation: addition, subtraction, multiplication, and
     * division.
     *
     * @author Fred Suvn
     * @version 0.0.0, 2015-03-04 14:45:24
     */
    public static interface FourFundamentalOperations<T> extends Addable<T>,
            Subtractable<T>, Multiplyable<T>, Divideable<T> {

    }
}
