package com.cogician.quicker.binary;

/**
 * <p>
 * Utility for converting bytes to numlti-bytes-value, always in big-endian order.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-08-21T22:33:53+08:00
 * @since 0.0.0, 2016-08-21T22:33:53+08:00
 */
public class BytesUtil {

    /**
     * <p>
     * To unsigned byte.
     * </p>
     * 
     * @param v
     *            input value
     * @return unsigned byte
     * @since 0.0.0
     */
    public static final int toUnsignedByte(byte v) {
        return v & 0x000000ff;
    }

    /**
     * <p>
     * To unsigned short.
     * </p>
     * 
     * @param v
     *            input value
     * @return unsigned short
     * @since 0.0.0
     */
    public static final int toUnsignedShort(short v) {
        return v & 0x0000ffff;
    }

    /**
     * <p>
     * To unsigned medium(consists of low-3-bytes).
     * </p>
     * 
     * @param v
     *            input value
     * @return unsigned medium
     * @since 0.0.0
     */
    public static final int toUnsignedMedium(int v) {
        return v & 0x00ffffff;
    }

    /**
     * <p>
     * To unsigned int.
     * </p>
     * 
     * @param v
     *            input value
     * @return unsigned int
     * @since 0.0.0
     */
    public static final long toUnsignedInt(int v) {
        return v & 0x00000000ffffffff;
    }

    /**
     * <p>
     * To unsigned bytes(consists of low-specified-bytes).
     * </p>
     * 
     * @param v
     *            input value
     * @param bytesNum
     *            specified number of bytes, [1, 8]
     * @return unsigned bytes
     * @throws IllegalArgumentException
     *             if specified number of bytes is not in [1, 8]
     * @since 0.0.0
     */
    public static final long toUnsignedBytes(long v, int bytesNum) throws IllegalArgumentException {
        int bits = 8 * (8 - bytesNum);
        return (v << bits) >> bits;
    }

    /**
     * <p>
     * Composes given bytes into a short.
     * </p>
     * 
     * @param b1
     *            first byte
     * @param b2
     *            second byte
     * @return a short
     * @since 0.0.0
     */
    public static final short toShort(byte b1, byte b2) {
        return (short)((b1 << 8) | toUnsignedByte(b2));
    }

    /**
     * <p>
     * Composes given bytes into a char.
     * </p>
     * 
     * @param b1
     *            first byte
     * @param b2
     *            second byte
     * @return a char
     * @since 0.0.0
     */
    public static final char toChar(byte b1, byte b2) {
        return (char)((b1 << 8) | toUnsignedByte(b2));
    }

    /**
     * <p>
     * Composes given bytes into a medium (consists of low-3-bytes).
     * </p>
     * 
     * @param b1
     *            first byte
     * @param b2
     *            second byte
     * @param b3
     *            third byte
     * @return a medium
     * @since 0.0.0
     */
    public static final int toMedium(byte b1, byte b2, byte b3) {
        return (b1 << 16) | (toUnsignedByte(b2) << 8) | toUnsignedByte(b3);
    }

    /**
     * <p>
     * Composes given bytes into an int.
     * </p>
     * 
     * @param b1
     *            first byte
     * @param b2
     *            second byte
     * @param b3
     *            third byte
     * @param b4
     *            fourth byte
     * @return an int
     * @since 0.0.0
     */
    public static final int toInt(byte b1, byte b2, byte b3, byte b4) {
        return (b1 << 24) | (toUnsignedByte(b2) << 16) | (toUnsignedByte(b3) << 8) | toUnsignedByte(b4);
    }

    /**
     * <p>
     * Composes given bytes into a float.
     * </p>
     * 
     * @param b1
     *            first byte
     * @param b2
     *            second byte
     * @param b3
     *            third byte
     * @param b4
     *            fourth byte
     * @return a float
     * @since 0.0.0
     */
    public static final float toFloat(byte b1, byte b2, byte b3, byte b4) {
        return Float.intBitsToFloat(toInt(b1, b2, b3, b4));
    }

    /**
     * <p>
     * Composes given bytes into a long.
     * </p>
     * 
     * @param b1
     *            first byte
     * @param b2
     *            second byte
     * @param b3
     *            third byte
     * @param b4
     *            fourth byte
     * @param b5
     *            fifth byte
     * @param b6
     *            sixth byte
     * @param b7
     *            seventh byte
     * @param b8
     *            eighth byte
     * @return a long
     * @since 0.0.0
     */
    public static final long toLong(byte b1, byte b2, byte b3, byte b4, byte b5, byte b6, byte b7, byte b8) {
        long l1 = toInt(b1, b2, b3, b4);
        long l2 = toUnsignedInt(toInt(b5, b6, b7, b8));
        return (l1 << 32) | l2;
    }

    /**
     * <p>
     * Composes given bytes into a double.
     * </p>
     * 
     * @param b1
     *            first byte
     * @param b2
     *            second byte
     * @param b3
     *            third byte
     * @param b4
     *            fourth byte
     * @param b5
     *            fifth byte
     * @param b6
     *            sixth byte
     * @param b7
     *            seventh byte
     * @param b8
     *            eighth byte
     * @return a double
     * @since 0.0.0
     */
    public static final double toDouble(byte b1, byte b2, byte b3, byte b4, byte b5, byte b6, byte b7, byte b8) {
        return Double.longBitsToDouble(toLong(b1, b2, b3, b4, b5, b6, b7, b8));
    }
}
