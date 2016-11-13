package com.cogician.quicker.struct;

import javax.annotation.concurrent.Immutable;

/**
 * <p>
 * An implementation of {@linkplain Number}, which can be converted to any numeric primitive type.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-11-08T14:41:20+08:00
 * @since 0.0.0, 2016-11-08T14:41:20+08:00
 */
@Immutable
public class QuickNumber extends Number {

    private static final long serialVersionUID = 1L;

    private final long bits;

    /**
     * <p>
     * Constructs with specified int value.
     * </p>
     * 
     * @param v
     *            specified int value
     * @since 0.0.0
     */
    public QuickNumber(int v) {
        this.bits = v;
    }

    /**
     * <p>
     * Constructs with specified long value.
     * </p>
     * 
     * @param v
     *            specified long value
     * @since 0.0.0
     */
    public QuickNumber(long v) {
        this.bits = v;
    }

    /**
     * <p>
     * Constructs with specified float value.
     * </p>
     * 
     * @param v
     *            specified float value
     * @since 0.0.0
     */
    public QuickNumber(float v) {
        this.bits = Float.floatToRawIntBits(v);
    }

    /**
     * <p>
     * Constructs with specified double value.
     * </p>
     * 
     * @param v
     *            specified double value
     * @since 0.0.0
     */
    public QuickNumber(double v) {
        this.bits = Double.doubleToRawLongBits(v);
    }

    @Override
    public int intValue() {
        return (int)bits;
    }

    @Override
    public long longValue() {
        return bits;
    }

    @Override
    public float floatValue() {
        return Float.intBitsToFloat(intValue());
    }

    @Override
    public double doubleValue() {
        return Double.longBitsToDouble(longValue());
    }

}
