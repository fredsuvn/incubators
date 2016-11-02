package com.cogician.quicker.binary;

/**
 * <p>
 * </p>
 * 
 * @author Fred Suvn
 * @version 0.0.0, 2015-12-11 15:51:38
 * @since 0.0.0
 */
public interface Numeric<T> {

    /**
     * <p>
     * And operation like:
     *
     * <pre>
     * this = this &amp; target;
     * </pre>
     *
     * As a number, if two instances' length is not equal, this method will
     * imaginarily sign-extend the shorter one first (real lengths of two
     * instances are not changed). The actual number of bits operated will be
     * returned.
     * </p>
     *
     * @param target
     *            target instance, not null
     * @return actual number of bits operated
     * @throws NullPointerException
     *             if target is null
     */
    public long and(T target);

    /**
     * <p>
     * Or operation like:
     *
     * <pre>
     * this = this | target;
     * </pre>
     *
     * As a number, if two instances' length is not equal, this method will
     * imaginarily sign-extend the shorter one first (real lengths of two
     * instances are not changed). The actual number of bits operated will be
     * returned.
     * </p>
     *
     * @param target
     *            target instance, not null
     * @return actual number of bits operated
     * @throws NullPointerException
     *             if target is null
     */
    public long or(T target);

    /**
     * <p>
     * Xor operation like:
     *
     * <pre>
     * this = this ^ target;
     * </pre>
     *
     * As a number, if two instances' length is not equal, this method will
     * imaginarily sign-extend the shorter one first (real lengths of two
     * instances are not changed). The actual number of bits operated will be
     * returned.
     * </p>
     *
     * @param target
     *            target instance, not null
     * @return actual number of bits operated
     * @throws NullPointerException
     *             if target is null
     */
    public long xor(T target);

    /**
     * <p>
     * Not operation like
     *
     * <pre>
     * this = ~this;
     * </pre>
     *
     * </p>
     */
    public void not();

    /**
     * <p>
     * Reverses bits of this instance.
     * </p>
     */
    public void reverse();

    /**
     * <p>
     * Reverses bytes of this instance. If length of this instance is not
     * multiple of byte, do nothing.
     * </p>
     */
    public void reverseBytes();

    /**
     * <p>
     * Logical shift left bits of specified number like:
     *
     * <pre>
     * this = this &lt;&lt; bits;
     * </pre>
     *
     * Shifted bits should be in [1, bits length - 1], otherwise do nothing.
     * </p>
     * <p>
     * This method is same with {@linkplain #arithmeticLeft(long)}.
     * </p>
     *
     * @param bits
     *            specified number of shifted bits
     * @see #arithmeticLeft(long)
     */
    public void logicalLeft(long bits);

    /**
     * <p>
     * Logical shift right bits of specified number like:
     *
     * <pre>
     * this = this &gt;&gt;&gt; bits;
     * </pre>
     *
     * Shifted bits should be in [1, bits length - 1], otherwise do nothing.
     * </p>
     * <p>
     * The parameters of this method will not be checked. It will throw
     * exceptions or return wrong results if given parameters don't meet the
     * requirements. The details of exceptions and wrong results depend on
     * implementations.
     * </p>
     *
     * @param bits
     *            specified number of shifted bits, [0L, bits length]
     */
    public void logicalRight(long bits);

    /**
     * <p>
     * Arithmetic shift left bits of specified number like:
     *
     * <pre>
     * accessor = accessor &lt;&lt; bits;
     * </pre>
     *
     * </p>
     * <p>
     * The parameters of this method will not be checked. It will throw
     * exceptions or return wrong results if given parameters don't meet the
     * requirements. The details of exceptions and wrong results depend on
     * implementations.
     * </p>
     * <p>
     * This method is same with {@linkplain #logicalLeft(long)}.
     * </p>
     *
     * @param bits
     *            specified number of shifted bits, [0L, bits length]
     * @see #logicalLeft(long)
     */
    public void arithmeticLeft(long bits);

    /**
     * <p>
     * Arithmetic shift right bits of specified number like:
     *
     * <pre>
     * accessor = accessor &gt;&gt; bits;
     * </pre>
     *
     * </p>
     * <p>
     * The parameters of this method will not be checked. It will throw
     * exceptions or return wrong results if given parameters don't meet the
     * requirements. The details of exceptions and wrong results depend on
     * implementations.
     * </p>
     *
     * @param bits
     *            specified number of shifted bits, [0L, length in bits]
     */
    public void arithmeticRight(long bits);

    /**
     * <p>
     * Rotates left bits of specified number like:
     *
     * <pre>
     * ROL AL, bits
     * </pre>
     *
     * </p>
     * <p>
     * If specified number of bits is negative, rotate right:
     *
     * <pre>
     * rotateLeft(-bits) == rotateRight(bits)
     * </pre>
     *
     * </p>
     * <p>
     * The parameters of this method will not be checked. It will throw
     * exceptions or return wrong results if given parameters don't meet the
     * requirements. The details of exceptions and wrong results depend on
     * implementations.
     * </p>
     *
     * @param bits
     *            specified number of bits, [0L, length in bits]
     * @see #rotateRight(long)
     */
    public void rotateLeft(long bits);

    /**
     * <p>
     * Rotates right bits of specified number like:
     *
     * <pre>
     * ROL AL, bits
     * </pre>
     *
     * </p>
     * <p>
     * If specified number of bits is negative, rotate left:
     *
     * <pre>
     * rotateRight(-bits) == rotateLeft(bits)
     * </pre>
     *
     * </p>
     * <p>
     * The parameters of this method will not be checked. It will throw
     * exceptions or return wrong results if given parameters don't meet the
     * requirements. The details of exceptions and wrong results depend on
     * implementations.
     * </p>
     *
     * @param bits
     *            specified number of bits, [0L, length in bits]
     * @see #rotateLeft(long)
     */
    public void rotateRight(long bits);
}
