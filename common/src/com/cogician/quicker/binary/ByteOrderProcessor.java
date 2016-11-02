package com.cogician.quicker.binary;

import java.nio.ByteOrder;

/**
 * <p>
 * This processor reorders input value by its byte order.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-08-21T21:36:37+08:00
 * @since 0.0.0, 2016-08-21T21:36:37+08:00
 */
public interface ByteOrderProcessor {

    /**
     * <p>
     * Big-endian processor.
     * </p>
     * 
     * @since 0.0.0
     */
    public static final ByteOrderProcessor BIG_ENDIAN = new BigEndianProcessor();

    /**
     * <p>
     * Little-endian processor.
     * </p>
     * 
     * @since 0.0.0
     */
    public static final ByteOrderProcessor LITTLE_ENDIAN = new LittleEndianProcessor();

    /**
     * <p>
     * Returns byte order of this processor.
     * </p>
     * 
     * @return byte order of this processor
     * @since 0.0.0
     */
    public ByteOrder getByteOrder();

    /**
     * <p>
     * Reorders input value.
     * </p>
     * 
     * @param v
     *            input value
     * @return reordered value
     * @since 0.0.0
     */
    public short doShort(short v);

    /**
     * <p>
     * Reorders input value.
     * </p>
     * 
     * @param v
     *            input value
     * @return reordered value
     * @since 0.0.0
     */
    public char doChar(char v);

    /**
     * <p>
     * Reorders input low-3-bytes value. The high byte will be ignored and intact to return.
     * </p>
     * 
     * @param v
     *            input value
     * @return reordered value
     * @since 0.0.0
     */
    public int doMedium(int v);

    /**
     * <p>
     * Reorders input value.
     * </p>
     * 
     * @param v
     *            input value
     * @return reordered value
     * @since 0.0.0
     */
    public int doInt(int v);

    /**
     * <p>
     * Reorders input value.
     * </p>
     * 
     * @param v
     *            input value
     * @return reordered value
     * @since 0.0.0
     */
    public float doFloat(float v);

    /**
     * <p>
     * Reorders input value.
     * </p>
     * 
     * @param v
     *            input value
     * @return reordered value
     * @since 0.0.0
     */
    public long doLong(long v);

    /**
     * <p>
     * Reorders input value.
     * </p>
     * 
     * @param v
     *            input value
     * @return reordered value
     * @since 0.0.0
     */
    public double doDouble(double v);

    /**
     * <p>
     * Reorders input specified number of low-bytes value. The rest high byte will be ignored and intact to return.
     * </p>
     * 
     * @param v
     *            input value
     * @return reordered value
     * @since 0.0.0
     */
    public long doBytes(long v, int bytesNum);

    /**
     * <p>
     * {@linkplain ByteOrderProcessor} of big-endian.
     * </p>
     *
     * @author Fred Suvn
     * @version 0.0.0, 2016-08-21T21:59:31+08:00
     * @since 0.0.0, 2016-08-21T21:59:31+08:00
     */
    public static class BigEndianProcessor implements ByteOrderProcessor {

        @Override
        public ByteOrder getByteOrder() {
            return ByteOrder.BIG_ENDIAN;
        }

        @Override
        public short doShort(short v) {
            return v;
        }

        @Override
        public char doChar(char v) {
            return v;
        }

        @Override
        public int doMedium(int v) {
            return v;
        }

        @Override
        public int doInt(int v) {
            return v;
        }

        @Override
        public float doFloat(float v) {
            return v;
        }

        @Override
        public long doLong(long v) {
            return v;
        }

        @Override
        public double doDouble(double v) {
            return v;
        }

        @Override
        public long doBytes(long v, int bytesNum) {
            return v;
        }
    }

    /**
     * <p>
     * {@linkplain ByteOrderProcessor} of little-endian.
     * </p>
     *
     * @author Fred Suvn
     * @version 0.0.0, 2016-08-21T21:59:31+08:00
     * @since 0.0.0, 2016-08-21T21:59:31+08:00
     */
    public static class LittleEndianProcessor implements ByteOrderProcessor {

        private LittleEndianProcessor() {

        }

        @Override
        public ByteOrder getByteOrder() {
            return ByteOrder.BIG_ENDIAN;
        }

        @Override
        public short doShort(short v) {
            return Short.reverseBytes(v);
        }

        @Override
        public char doChar(char v) {
            return Character.reverseBytes(v);
        }

        @Override
        public int doMedium(int v) {
            return Integer.reverseBytes(v) >> 8;
        }

        @Override
        public int doInt(int v) {
            return Integer.reverseBytes(v);
        }

        @Override
        public float doFloat(float v) {
            return Float.intBitsToFloat(doInt(Float.floatToRawIntBits(v)));
        }

        @Override
        public long doLong(long v) {
            return Long.reverseBytes(v);
        }

        @Override
        public double doDouble(double v) {
            return Double.longBitsToDouble(doLong(Double.doubleToRawLongBits(v)));
        }

        @Override
        public long doBytes(long v, int bytesNum) {
            return doLong(v) >> (8 * (8 - bytesNum));
        }
    }
}
