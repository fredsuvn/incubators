package com.cogician.quicker.util;

/**
 * <p>
 * Static quick utility class provides static methods for bits and bytes.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2015-05-18 09:58:21
 * @since 0.0.0
 */
public class BitsQuicker {

    /**
     * <p>
     * Empty constructor for reflection framework.
     * </p>
     */
    public BitsQuicker() {

    }

    /**
     * <p>
     * Reverses bits of specified byte value.
     * </p>
     *
     * @param v
     *            specified byte value to be reversed
     * @return reversed value of specified byte value
     * @since 0.0.0
     */
    public static byte reverse(final byte v) {
        int i = Integer.reverse(v);
        return (byte)(i >>> 24);
    }

    /**
     * <p>
     * Reverses bits of specified short value.
     * </p>
     *
     * @param v
     *            specified short value to be reversed
     * @return reversed value of specified short value
     * @since 0.0.0
     */
    public static short reverse(final short v) {
        int i = Integer.reverse(v);
        return (short)(i >>> 16);
    }

    /**
     * <p>
     * Reverses bits of specified char value.
     * </p>
     *
     * @param v
     *            specified char value to be reversed
     * @return reversed value of specified char value
     * @since 0.0.0
     */
    public static char reverse(final char v) {
        int i = Integer.reverse(v);
        return (char)(i >>> 16);
    }

    /**
     * <p>
     * Reverses bits of specified int value.
     * </p>
     *
     * @param v
     *            specified int value to be reversed
     * @return reversed value of specified int value
     * @since 0.0.0
     */
    public static int reverse(final int v) {
        return Integer.reverse(v);
    }

    /**
     * <p>
     * Reverses bits of specified float value.
     * </p>
     *
     * @param v
     *            specified float value to be reversed
     * @return reversed value of specified float value
     * @since 0.0.0
     */
    public static float reverse(final float v) {
        return Float.intBitsToFloat(reverse(Float.floatToRawIntBits(v)));
    }

    /**
     * <p>
     * Reverses bits of specified long value.
     * </p>
     *
     * @param v
     *            specified long value to be reversed
     * @return reversed value of specified long value
     * @since 0.0.0
     */
    public static long reverse(final long v) {
        return Long.reverse(v);
    }

    /**
     * <p>
     * Reverses bits of specified double value.
     * </p>
     *
     * @param v
     *            specified double value to be reversed
     * @return reversed value of specified double value
     * @since 0.0.0
     */
    public static double reverse(final double v) {
        return Double.longBitsToDouble(reverse(Double.doubleToRawLongBits(v)));
    }

    /**
     * <p>
     * Reverses bytes of specified short value.
     * </p>
     *
     * @param v
     *            specified short value to be reversed
     * @return reversed value of specified short value
     * @since 0.0.0
     */
    public static short reverseBytes(final short v) {
        return Short.reverseBytes(v);
    }

    /**
     * <p>
     * Reverses bytes of specified char value.
     * </p>
     *
     * @param v
     *            specified char value to be reversed
     * @return reversed value of specified char value
     * @since 0.0.0
     */
    public static char reverseBytes(final char v) {
        return Character.reverseBytes(v);
    }

    /**
     * <p>
     * Reverses bytes of specified int value.
     * </p>
     *
     * @param v
     *            specified int value to be reversed
     * @return reversed value of specified int value
     * @since 0.0.0
     */
    public static int reverseBytes(final int v) {
        return Integer.reverseBytes(v);
    }

    /**
     * <p>
     * Reverses bytes of specified float value.
     * </p>
     *
     * @param v
     *            specified float value to be reversed
     * @return reversed value of specified float value
     * @since 0.0.0
     */
    public static float reverseBytes(final float v) {
        return Float.intBitsToFloat(reverseBytes(Float.floatToRawIntBits(v)));
    }

    /**
     * <p>
     * Reverses bytes of specified long value.
     * </p>
     *
     * @param v
     *            specified long value to be reversed
     * @return reversed value of specified long value
     * @since 0.0.0
     */
    public static long reverseBytes(final long v) {
        return Long.reverseBytes(v);
    }

    /**
     * <p>
     * Reverses bytes of specified double value.
     * </p>
     *
     * @param v
     *            specified double value to be reversed
     * @return reversed value of specified double value
     * @since 0.0.0
     */
    public static double reverseBytes(final double v) {
        return Double.longBitsToDouble(reverseBytes(Double.doubleToRawLongBits(v)));
    }

    /**
     * <p>
     * Gets part of bits of an specified int value, from specified start bit position inclusive, through specified bits
     * number. The gotten bits will be sign-extended to int value, for example:
     *
     * <pre>
     * int i = 0x00FF0000;
     * System.out.println(getSignedBitsOfInt(i, 8, 8));
     * </pre>
     *
     * Above code will print value: 0xFFFFFFFF.
     * </p>
     *
     * @param value
     *            specified int value
     * @param startBit
     *            start bit position inclusive, [0, 31]
     * @param bitsNum
     *            specified bits number, [1, 32 - startBit]
     * @return sign-extended part of bits of an specified int value
     * @since 0.0.0
     */
    public static int getSignedBitsOfInt(int value, final int startBit, final int bitsNum) {
        // don't consider if (startBit == 0 && bitsNum == 32)
        value <<= startBit;
        value >>= (32 - bitsNum);
        return value;
    }

    /**
     * <p>
     * Gets part of bits of an specified int value, from specified start bit position inclusive, through specified bits
     * number. The gotten bits will be zero-extended to int value, for example:
     *
     * <pre>
     * int i = 0x00FF0000;
     * System.out.println(getSignedBitsOfInt(i, 8, 8));
     * </pre>
     *
     * Above code will print value: 0x000000FF.
     * </p>
     *
     * @param value
     *            specified int value
     * @param startBit
     *            start bit position inclusive, [0, 31]
     * @param bitsNum
     *            specified bits number, [1, 32 - startBit]
     * @return zero-extended part of bits of an specified int value
     * @since 0.0.0
     */
    public static int getUnsignedBitsOfInt(int value, final int startBit, final int bitsNum) {
        // don't consider if (startBit == 0 && bitsNum == 32)
        value <<= startBit;
        value >>>= (32 - bitsNum);
        return value;
    }

    /**
     * <p>
     * Gets part of bits of an specified long value, from specified start bit position inclusive, through specified bits
     * number. The gotten bits will be sign-extended to long value, for example:
     *
     * <pre>
     * long l = 0x00FF000000000000L;
     * System.out.println(getSignedBitsOfLong(i, 8, 8));
     * </pre>
     *
     * Above code will print value: 0xFFFFFFFFFFFFFFFFL.
     * </p>
     *
     * @param value
     *            specified long value
     * @param startBit
     *            start bit position inclusive, [0, 63]
     * @param bitsNum
     *            specified bits number, [1, 64 - startBit]
     * @return sign-extended part of bits of an specified long value
     * @since 0.0.0
     */
    public static long getSignedBitsOfLong(long value, final int startBit, final int bitsNum) {
        // don't consider if (startBit == 0 && bitsNum == 64)
        value <<= startBit;
        value >>= (64 - bitsNum);
        return value;
    }

    /**
     * <p>
     * Gets part of bits of an specified long value, from specified start bit position inclusive, through specified bits
     * number. The gotten bits will be zero-extended to long value, for example:
     *
     * <pre>
     * long l = 0x00FF000000000000L;
     * System.out.println(getUnsignedBitsOfLong(i, 8, 8));
     * </pre>
     *
     * Above code will print value: 0x00000000000000FFL.
     * </p>
     *
     * @param value
     *            specified long value
     * @param startBit
     *            start bit position inclusive, [0, 63]
     * @param bitsNum
     *            specified bits number, [1, 64 - startBit]
     * @return zero-extended part of bits of an specified long value
     * @since 0.0.0
     */
    public static long getUnsignedBitsOfLong(long value, final int startBit, final int bitsNum) {
        // don't consider if (startBit == 0 && bitsNum == 64)
        value <<= startBit;
        value >>>= (64 - bitsNum);
        return value;
    }

    /**
     * <p>
     * Fill bit 0 into specified int value, from specified start bit position inclusive, through specified bits number.
     * </p>
     * 
     * @param value
     *            specified int value
     * @param startBit
     *            start bit position inclusive, [0, 31]
     * @param bitsNum
     *            specified bits number, [1, 32 - startBit]
     * @return value after filling
     * @since 0.0.0
     */
    public static int fill0Bits(int value, int startBit, int bitsNum) {
        int i = 0xFFFFFFFF;
        i <<= startBit;
        i >>>= (32 - bitsNum);
        i <<= (32 - startBit - bitsNum);
        i = ~i;
        value &= i;
        return value;
    }

    /**
     * <p>
     * Fill bit 0 into specified long value, from specified start bit position inclusive, through specified bits number.
     * </p>
     * 
     * @param value
     *            specified long value
     * @param startBit
     *            start bit position inclusive, [0, 63]
     * @param bitsNum
     *            specified bits number, [1, 64 - startBit]
     * @return value after filling
     * @since 0.0.0
     */
    public static long fill0Bits(long value, int startBit, int bitsNum) {
        long l = 0xFFFFFFFFFFFFFFFFL;
        l <<= startBit;
        l >>>= (64 - bitsNum);
        l <<= (64 - startBit - bitsNum);
        l = ~l;
        value &= l;
        return value;
    }

    /**
     * <p>
     * Fill bit 1 into specified int value, from specified start bit position inclusive, through specified bits number.
     * </p>
     * 
     * @param value
     *            specified int value
     * @param startBit
     *            start bit position inclusive, [0, 31]
     * @param bitsNum
     *            specified bits number, [1, 32 - startBit]
     * @return value after filling
     * @since 0.0.0
     */
    public static int fill1Bits(int value, int startBit, int bitsNum) {
        int i = 0xFFFFFFFF;
        i <<= startBit;
        i >>>= (32 - bitsNum);
        i <<= (32 - startBit - bitsNum);
        value |= i;
        return value;
    }

    /**
     * <p>
     * Fill bit 1 into specified long value, from specified start bit position inclusive, through specified bits number.
     * </p>
     * 
     * @param value
     *            specified long value
     * @param startBit
     *            start bit position inclusive, [0, 63]
     * @param bitsNum
     *            specified bits number, [1, 64 - startBit]
     * @return value after filling
     * @since 0.0.0
     */
    public static long fill1Bits(long value, int startBit, int bitsNum) {
        long l = 0xFFFFFFFFFFFFFFFFL;
        l <<= startBit;
        l >>>= (64 - bitsNum);
        l <<= (64 - startBit - bitsNum);
        value |= l;
        return value;
    }

    /**
     * <p>
     * Fill specified bit into specified int value, from specified start bit position inclusive, through specified bits
     * number.
     * </p>
     * 
     * @param value
     *            specified int value
     * @param startBit
     *            start bit position inclusive, [0, 31]
     * @param bitsNum
     *            specified bits number, [1, 32 - startBit]
     * @param isZero
     *            whether specified bit is 0, true is 0, false is 1
     * @return value after filling
     * @since 0.0.0
     */
    public static int fillBits(int value, int startBit, int bitsNum, boolean isZero) {
        return isZero ? fill0Bits(value, startBit, bitsNum) : fill1Bits(value, startBit, bitsNum);
    }

    /**
     * <p>
     * Fill specified bit into specified long value, from specified start bit position inclusive, through specified bits
     * number.
     * </p>
     * 
     * @param value
     *            specified long value
     * @param startBit
     *            start bit position inclusive, [0, 63]
     * @param bitsNum
     *            specified bits number, [1, 64 - startBit]
     * @param isZero
     *            whether specified bit is 0, true is 0, false is 1
     * @return value after filling
     * @since 0.0.0
     */
    public static long fillBits(long value, int startBit, int bitsNum, boolean isZero) {
        return isZero ? fill0Bits(value, startBit, bitsNum) : fill1Bits(value, startBit, bitsNum);
    }

    /**
     * <p>
     * Copies bits of specified number, from specified start bit position inclusive of specified source value, to
     * specified bit position inclusive of specified destination value. The destination value after copying will be
     * returned. For example:
     *
     * <pre>
     * int i1 = 0xFF000000;
     * int i2 = 0x000000FF;
     * int i = bitsCopy(i1, 0, i2, 0, 8);
     * System.out.println(i);
     * </pre>
     *
     * Above code will print value: 0xFF0000FF.
     * </p>
     *
     * @param sourceValue
     *            specified source value
     * @param sourceStartBit
     *            specified source start bit position inclusive, [0, 31]
     * @param destValue
     *            specified destination value
     * @param destStartBit
     *            specified destination start bit position inclusive, [0, 31]
     * @param bitsNum
     *            specified bits number, greater than 1, and sourceStartBit/destStartBit + bitsNum <= 32
     * @return destination value after copying
     * @since 0.0.0
     */
    public static int bitsCopy(int sourceValue, final int sourceStartBit, int destValue, final int destStartBit,
            final int bitsNum) {
        sourceValue <<= sourceStartBit;
        sourceValue >>>= (32 - bitsNum);
        sourceValue <<= (32 - destStartBit - bitsNum);
        int i = destValue;
        i <<= destStartBit;
        i >>>= (32 - bitsNum);
        i <<= (32 - destStartBit - bitsNum);
        i = ~i;
        destValue &= i;
        return sourceValue | destValue;
    }

    /**
     * <p>
     * Copies bits of specified number, from specified start bit position inclusive of specified source value, to
     * specified bit position inclusive of specified destination value. The destination value after copying will be
     * returned. For example:
     *
     * <pre>
     * long l1 = 0xFF00000000000000L;
     * long l2 = 0x00000000000000FFL;
     * long l = bitsCopy(l1, 0, l2, 0, 8);
     * System.out.println(l);
     * </pre>
     *
     * Above code will print value: 0xFF000000000000FFL.
     * </p>
     *
     * @param sourceValue
     *            specified source value
     * @param sourceStartBit
     *            specified source start bit position inclusive, [0, 63]
     * @param destValue
     *            specified destination value
     * @param destStartBit
     *            specified destination start bit position inclusive, [0, 63]
     * @param bitsNum
     *            specified bits number, greater than 1, and sourceStartBit/destStartBit + bitsNum <= 64
     * @return destination value after copying
     * @since 0.0.0
     */
    public static long bitsCopy(long sourceValue, final int sourceStartBit, long destValue, final int destStartBit,
            final int length) {
        sourceValue <<= sourceStartBit;
        sourceValue >>>= (64 - length);
        sourceValue <<= (64 - destStartBit - length);
        long l = destValue;
        l <<= destStartBit;
        l >>>= (64 - length);
        l <<= (64 - destStartBit - length);
        l = ~l;
        destValue &= l;
        return sourceValue | destValue;
    }

    /**
     * <p>
     * Gets alignment length of specified bits number, based on specified bit wide. The alignment length is least
     * multiple of specified bit wide which exactly contains the whole specified bits number. For example:
     * 
     * <pre>
     * long bitsNum = 9L;
     * System.out.println(getAlignmentLength(bitsNum, 8));
     * </pre>
     * 
     * Above code will print value: 2. Because at least 2 * 8 bits can contain the 9 bits.
     * </p>
     *
     * @param bitsNum
     *            specified bits number, [1L, {@link Integer#MAX_VALUE} * {@link Integer#MAX_VALUE}]
     * @param wide
     *            specified bit wide, positive
     * @return alignment length of specified bits number, based on specified bit wide
     * @since 0.0.0
     */
    public static int getAlignmentLength(final long bitsNum, final int wide) {
        return bitsNum % wide == 0 ? (int)(bitsNum / wide) : (int)(bitsNum / wide) + 1;
    }
    
    public static float requireFloat(float v){
        if (Float.isNaN(v)){
            return v;
        } else {
            return v;
        }
    }

    /**
     * <p>
     * Makes short value by given bytes, in parameter given order (big-endian), b1 is highest-order byte, b0 is lowest.
     * </p>
     * 
     * @param b1
     *            highest-order byte
     * @param b0
     *            lowest-order byte
     * @return short value made by given bytes
     * @since 0.0.0
     */
    public static short makeShort(byte b1, byte b0) {
        return (short)(((b1 & 0xFF) << 8) | ((b0 & 0xFF) << 0));
    }

    /**
     * <p>
     * Makes char value by given bytes, in parameter given order (big-endian), b1 is highest-order byte, b0 is lowest.
     * </p>
     * 
     * @param b1
     *            highest-order byte
     * @param b0
     *            lowest-order byte
     * @return char value made by given bytes
     * @since 0.0.0
     */
    public static char makeChar(byte b1, byte b0) {
        return (char)(((b1 & 0xFF) << 8) | ((b0 & 0xFF) << 0));
    }

    /**
     * <p>
     * Makes int value by given bytes, in parameter given order (big-endian), b3 is highest-order byte, b0 is lowest.
     * </p>
     * 
     * @param b3
     *            highest-order byte
     * @param b2
     *            2-order byte
     * @param b1
     *            1-order byte
     * @param b0
     *            lowest-order byte
     * @return int value made by given bytes
     * @since 0.0.0
     */
    public static int makeInt(byte b3, byte b2, byte b1, byte b0) {
        return ((b3 & 0xFF) << 24) | ((b2 & 0xFF) << 16) | ((b1 & 0xFF) << 8) | ((b0 & 0xFF) << 0);
    }

    /**
     * <p>
     * Makes float value by given bytes, in parameter given order (big-endian), b3 is highest-order byte, b0 is lowest.
     * </p>
     * 
     * @param b3
     *            highest-order byte
     * @param b2
     *            2-order byte
     * @param b1
     *            1-order byte
     * @param b0
     *            lowest-order byte
     * @return float value made by given bytes
     * @since 0.0.0
     */
    public static float makeFloat(byte b3, byte b2, byte b1, byte b0) {
        return Float.intBitsToFloat(makeInt(b3, b2, b1, b0));
    }

    /**
     * <p>
     * Makes long value by given bytes, in parameter given order (big-endian), b7 is highest-order byte, b0 is lowest.
     * </p>
     * 
     * @param b7
     *            highest-order byte
     * @param b6
     *            6-order byte
     * @param b5
     *            5-order byte
     * @param b4
     *            4-order byte
     * @param b3
     *            3-order byte
     * @param b2
     *            2-order byte
     * @param b1
     *            1-order byte
     * @param b0
     *            lowest-order byte
     * @return long value made by given bytes
     * @since 0.0.0
     */
    public static long makeLong(byte b7, byte b6, byte b5, byte b4, byte b3, byte b2, byte b1, byte b0) {
        return ((b7 & 0xFFL) << 56) | ((b6 & 0xFFL) << 48) | ((b5 & 0xFFL) << 40) | ((b4 & 0xFFL) << 32)
                | ((b3 & 0xFFL) << 24) | ((b2 & 0xFFL) << 16) | ((b1 & 0xFFL) << 8) | ((b0 & 0xFFL) << 0);
    }

    /**
     * <p>
     * Makes double value by given bytes, in parameter given order (big-endian), b7 is highest-order byte, b0 is lowest.
     * </p>
     * 
     * @param b7
     *            highest-order byte
     * @param b6
     *            6-order byte
     * @param b5
     *            5-order byte
     * @param b4
     *            4-order byte
     * @param b3
     *            3-order byte
     * @param b2
     *            2-order byte
     * @param b1
     *            1-order byte
     * @param b0
     *            lowest-order byte
     * @return double value made by given bytes
     * @since 0.0.0
     */
    public static double makeDouble(byte b7, byte b6, byte b5, byte b4, byte b3, byte b2, byte b1, byte b0) {
        return Double.longBitsToDouble(makeLong(b7, b6, b5, b4, b3, b2, b1, b0));
    }

    /**
     * <p>
     * Makes int value by given shorts, in parameter given order (big-endian), s1 is highest-order short, s0 is lowest.
     * </p>
     * 
     * @param s1
     *            highest-order short
     * @param s0
     *            lowest-order short
     * @return int value made by given shorts
     * @since 0.0.0
     */
    public static int makeInt(short s1, short s0) {
        return ((s1 & 0xFFFF) << 16) | ((s0 & 0xFFFF) << 0);
    }

    /**
     * <p>
     * Makes float value by given shorts, in parameter given order (big-endian), s1 is highest-order short, s0 is
     * lowest.
     * </p>
     * 
     * @param s1
     *            highest-order short
     * @param s0
     *            lowest-order short
     * @return float value made by given shorts
     * @since 0.0.0
     */
    public static float makeFloat(short s1, short s0) {
        return Float.intBitsToFloat(makeInt(s1, s0));
    }

    /**
     * <p>
     * Makes long value by given shorts, in parameter given order (big-endian), s3 is highest-order short, s0 is lowest.
     * </p>
     * 
     * @param s3
     *            highest-order short
     * @param s2
     *            2-order short
     * @param s1
     *            1-order short
     * @param s0
     *            lowest-order short
     * @return long value made by given shorts
     * @since 0.0.0
     */
    public static long makeLong(short s3, short s2, short s1, short s0) {
        return ((s3 & 0xFFFFL) << 48) | ((s2 & 0xFFFFL) << 32) | ((s1 & 0xFFFFL) << 16) | ((s0 & 0xFFFFL) << 0);
    }

    /**
     * <p>
     * Makes double value by given shorts, in parameter given order (big-endian), s3 is highest-order short, s0 is
     * lowest.
     * </p>
     * 
     * @param s3
     *            highest-order short
     * @param s2
     *            2-order short
     * @param s1
     *            1-order short
     * @param s0
     *            lowest-order short
     * @return double value made by given shorts
     * @since 0.0.0
     */
    public static double makeDouble(short s3, short s2, short s1, short s0) {
        return Double.longBitsToDouble(makeLong(s3, s2, s1, s0));
    }

    /**
     * <p>
     * Makes int value by given chars, in parameter given order (big-endian), c1 is highest-order char, c0 is lowest.
     * </p>
     * 
     * @param c1
     *            highest-order char
     * @param c0
     *            lowest-order char
     * @return int value made by given chars
     * @since 0.0.0
     */
    public static int makeInt(char c1, char c0) {
        return ((c1 & 0xFFFF) << 16) | ((c0 & 0xFFFF) << 0);
    }

    /**
     * <p>
     * Makes float value by given chars, in parameter given order (big-endian), c1 is highest-order char, c0 is lowest.
     * </p>
     * 
     * @param c1
     *            highest-order char
     * @param c0
     *            lowest-order char
     * @return float value made by given chars
     * @since 0.0.0
     */
    public static float makeFloat(char c1, char c0) {
        return Float.intBitsToFloat(makeInt(c1, c0));
    }

    /**
     * <p>
     * Makes long value by given chars, in parameter given order (big-endian), c3 is highest-order char, c0 is lowest.
     * </p>
     * 
     * @param c1
     *            highest-order char
     * @param c2
     *            2-order char
     * @param c1
     *            1-order char
     * @param c0
     *            lowest-order char
     * @return long value made by given chars
     * @since 0.0.0
     */
    public static long makeLong(char c3, char c2, char c1, char c0) {
        return ((c3 & 0xFFFFL) << 48) | ((c2 & 0xFFFFL) << 32) | ((c1 & 0xFFFFL) << 16) | ((c0 & 0xFFFFL) << 0);
    }

    /**
     * <p>
     * Makes double value by given chars, in parameter given order (big-endian), c3 is highest-order char, c0 is lowest.
     * </p>
     * 
     * @param c1
     *            highest-order char
     * @param c2
     *            2-order char
     * @param c1
     *            1-order char
     * @param c0
     *            lowest-order char
     * @return double value made by given chars
     * @since 0.0.0
     */
    public static double makeDouble(char c3, char c2, char c1, char c0) {
        return Double.longBitsToDouble(makeLong(c3, c2, c1, c0));
    }

    /**
     * <p>
     * Makes float value by given int value's binary bits.
     * </p>
     * 
     * @param i
     *            given int value
     * @return float value made by given int
     * @since 0.0.0
     */
    public static float makeFloat(int i) {
        return Float.intBitsToFloat(i);
    }

    /**
     * <p>
     * Makes long value by given ints, in parameter given order (big-endian), i1 is highest-order int, i0 is lowest.
     * </p>
     * 
     * @param i1
     *            highest-order int
     * @param i0
     *            lowest-order int
     * @return long value made by given ints
     * @since 0.0.0
     */
    public static long makeLong(int i1, int i0) {
        return ((i1 & 0xFFFFFFFFL) << 32) | ((i0 & 0xFFFFFFFFL) << 0);
    }

    /**
     * <p>
     * Makes double value by given ints, in parameter given order (big-endian), i1 is highest-order int, i0 is lowest.
     * </p>
     * 
     * @param i1
     *            highest-order int
     * @param i0
     *            lowest-order int
     * @return double value made by given ints
     * @since 0.0.0
     */
    public static double makeDouble(int i1, int i0) {
        return Double.longBitsToDouble(makeLong(i1, i0));
    }

    /**
     * <p>
     * Makes int value by given float value's binary bits.
     * </p>
     * 
     * @param f
     *            given float value
     * @return int value made by given float
     * @since 0.0.0
     */
    public static int makeInt(float f) {
        return Float.floatToRawIntBits(f);
    }

    /**
     * <p>
     * Makes long value by given floats, in parameter given order (big-endian), f1 is highest-order float, f0 is lowest.
     * </p>
     * 
     * @param f1
     *            highest-order float
     * @param f0
     *            lowest-order float
     * @return long value made by given floats
     * @since 0.0.0
     */
    public static long makeLong(float f1, float f0) {
        int i1 = Float.floatToRawIntBits(f1);
        int i0 = Float.floatToRawIntBits(f0);
        return ((i1 & 0xFFFFFFFFL) << 32) | ((i0 & 0xFFFFFFFFL) << 0);
    }

    /**
     * <p>
     * Makes double value by given floats, in parameter given order (big-endian), f1 is highest-order float, f0 is
     * lowest.
     * </p>
     * 
     * @param f1
     *            highest-order float
     * @param f0
     *            lowest-order float
     * @return double value made by given floats
     * @since 0.0.0
     */
    public static double makeDouble(float f1, float f0) {
        int i1 = Float.floatToRawIntBits(f1);
        int i0 = Float.floatToRawIntBits(f0);
        return Double.longBitsToDouble(makeLong(i1, i0));
    }

    /**
     * <p>
     * Makes double value by given long value's binary bits.
     * </p>
     * 
     * @param l
     *            given long value
     * @return double value made by given long
     * @since 0.0.0
     */
    public static double makeDouble(long l) {
        return Double.longBitsToDouble(l);
    }

    /**
     * <p>
     * Makes long value by given double value's binary bits.
     * </p>
     * 
     * @param l
     *            given double value
     * @return long value made by given double
     * @since 0.0.0
     */
    public static long makeLong(double d) {
        return Double.doubleToRawLongBits(d);
    }

    /**
     * <p>
     * Gets byte from specified short value at specified order in byte, the order form highest to lowest is 1 to 0.
     * </p>
     * <p>
     * Methods getByte are compositive of methods getByte7 to getByte0.
     * </p>
     * 
     * @param v
     *            specified short value
     * @param order
     *            order in byte, [1, 0]
     * @return byte at specified order in byte
     * @since 0.0.0
     */
    public static byte getByte(short v, int order) {
        return (byte)(v >> (8 * order));
    }

    /**
     * <p>
     * Gets byte from specified char value at specified order in byte, the order form highest to lowest is 1 to 0.
     * </p>
     * <p>
     * Methods getByte are compositive of methods getByte7 to getByte0.
     * </p>
     * 
     * @param v
     *            specified char value
     * @param order
     *            order in byte, [1, 0]
     * @return byte at specified order in byte
     * @since 0.0.0
     */
    public static byte getByte(char v, int order) {
        return (byte)(v >> (8 * order));
    }

    /**
     * <p>
     * Gets byte from specified int value at specified order in byte, the order form highest to lowest is 3 to 0.
     * </p>
     * <p>
     * Methods getByte are compositive of methods getByte7 to getByte0.
     * </p>
     * 
     * @param v
     *            specified int value
     * @param order
     *            order in byte, [3, 0]
     * @return byte at specified order in byte
     * @since 0.0.0
     */
    public static byte getByte(int v, int order) {
        return (byte)(v >> (8 * order));
    }

    /**
     * <p>
     * Gets byte from specified float value at specified order in byte, the order form highest to lowest is 3 to 0.
     * </p>
     * <p>
     * Methods getByte are compositive of methods getByte7 to getByte0.
     * </p>
     * 
     * @param v
     *            specified float value
     * @param order
     *            order in byte, [3, 0]
     * @return byte at specified order in byte
     * @since 0.0.0
     */
    public static byte getByte(float v, int order) {
        return (byte)(Float.floatToRawIntBits(v) >> (8 * order));
    }

    /**
     * <p>
     * Gets byte from specified long value at specified order in byte, the order form highest to lowest is 7 to 0.
     * </p>
     * <p>
     * Methods getByte are compositive of methods getByte7 to getByte0.
     * </p>
     * 
     * @param v
     *            specified long value
     * @param order
     *            order in byte, [7, 0]
     * @return byte at specified order in byte
     * @since 0.0.0
     */
    public static byte getByte(long v, int order) {
        return (byte)(v >> (8 * order));
    }

    /**
     * <p>
     * Gets byte from specified double value at specified order in byte, the order form highest to lowest is 7 to 0.
     * </p>
     * <p>
     * Methods getByte are compositive of methods getByte7 to getByte0.
     * </p>
     * 
     * @param v
     *            specified double value
     * @param order
     *            order in byte, [7, 0]
     * @return byte at specified order in byte
     * @since 0.0.0
     */
    public static byte getByte(double v, int order) {
        return (byte)(Double.doubleToRawLongBits(v) >> (8 * order));
    }

    /**
     * <p>
     * Gets 0-order-byte from specified short value. The order in byte from high to low (left to right) is 1 to 0.
     * </p>
     * 
     * @param v
     *            specified short value
     * @return 0-order-byte from specified value
     * @since 0.0.0
     */
    public static byte getByte0(short v) {
        return (byte)(v >> 0);
    }

    /**
     * <p>
     * Gets 0-order-byte from specified char value. The order in byte from high to low (left to right) is 1 to 0.
     * </p>
     * 
     * @param v
     *            specified char value
     * @return 0-order-byte from specified value
     * @since 0.0.0
     */
    public static byte getByte0(char v) {
        return (byte)(v >> 0);
    }

    /**
     * <p>
     * Gets 0-order-byte from specified int value. The order in byte from high to low (left to right) is 3 to 0.
     * </p>
     * 
     * @param v
     *            specified int value
     * @return 0-order-byte from specified value
     * @since 0.0.0
     */
    public static byte getByte0(int v) {
        return (byte)(v >> 0);
    }

    /**
     * <p>
     * Gets 0-order-byte from specified float value. The order in byte from high to low (left to right) is 3 to 0.
     * </p>
     * 
     * @param v
     *            specified float value
     * @return 0-order-byte from specified value
     * @since 0.0.0
     */
    public static byte getByte0(float v) {
        return (byte)(Float.floatToRawIntBits(v) >> 0);
    }

    /**
     * <p>
     * Gets 0-order-byte from specified long value. The order in byte from high to low (left to right) is 7 to 0.
     * </p>
     * 
     * @param v
     *            specified long value
     * @return 0-order-byte from specified value
     * @since 0.0.0
     */
    public static byte getByte0(long v) {
        return (byte)(v >> 0);
    }

    /**
     * <p>
     * Gets 0-order-byte from specified double value. The order in byte from high to low (left to right) is 7 to 0.
     * </p>
     * 
     * @param v
     *            specified double value
     * @return 0-order-byte from specified value
     * @since 0.0.0
     */
    public static byte getByte0(double v) {
        return (byte)(Double.doubleToRawLongBits(v) >> 0);
    }

    /**
     * <p>
     * Gets 1-order-byte from specified short value. The order in byte from high to low (left to right) is 1 to 0.
     * </p>
     * 
     * @param v
     *            specified short value
     * @return 1-order-byte from specified value
     * @since 0.0.0
     */
    public static byte getByte1(short v) {
        return (byte)(v >> 8);
    }

    /**
     * <p>
     * Gets 1-order-byte from specified char value. The order in byte from high to low (left to right) is 1 to 0.
     * </p>
     * 
     * @param v
     *            specified char value
     * @return 1-order-byte from specified value
     * @since 0.0.0
     */
    public static byte getByte1(char v) {
        return (byte)(v >> 8);
    }

    /**
     * <p>
     * Gets 1-order-byte from specified int value. The order in byte from high to low (left to right) is 3 to 0.
     * </p>
     * 
     * @param v
     *            specified int value
     * @return 1-order-byte from specified value
     * @since 0.0.0
     */
    public static byte getByte1(int v) {
        return (byte)(v >> 8);
    }

    /**
     * <p>
     * Gets 1-order-byte from specified float value. The order in byte from high to low (left to right) is 3 to 0.
     * </p>
     * 
     * @param v
     *            specified float value
     * @return 1-order-byte from specified value
     * @since 0.0.0
     */
    public static byte getByte1(float v) {
        return (byte)(Float.floatToRawIntBits(v) >> 8);
    }

    /**
     * <p>
     * Gets 1-order-byte from specified long value. The order in byte from high to low (left to right) is 7 to 0.
     * </p>
     * 
     * @param v
     *            specified long value
     * @return 1-order-byte from specified value
     * @since 0.0.0
     */
    public static byte getByte1(long v) {
        return (byte)(v >> 8);
    }

    /**
     * <p>
     * Gets 1-order-byte from specified double value. The order in byte from high to low (left to right) is 7 to 0.
     * </p>
     * 
     * @param v
     *            specified double value
     * @return 1-order-byte from specified value
     * @since 0.0.0
     */
    public static byte getByte1(double v) {
        return (byte)(Double.doubleToRawLongBits(v) >> 8);
    }

    /**
     * <p>
     * Gets 2-order-byte from specified int value. The order in byte from high to low (left to right) is 3 to 0.
     * </p>
     * 
     * @param v
     *            specified int value
     * @return 2-order-byte from specified value
     * @since 0.0.0
     */
    public static byte getByte2(int v) {
        return (byte)(v >> 16);
    }

    /**
     * <p>
     * Gets 2-order-byte from specified float value. The order in byte from high to low (left to right) is 3 to 0.
     * </p>
     * 
     * @param v
     *            specified float value
     * @return 2-order-byte from specified value
     * @since 0.0.0
     */
    public static byte getByte2(float v) {
        return (byte)(Float.floatToRawIntBits(v) >> 16);
    }

    /**
     * <p>
     * Gets 2-order-byte from specified long value. The order in byte from high to low (left to right) is 7 to 0.
     * </p>
     * 
     * @param v
     *            specified long value
     * @return 2-order-byte from specified value
     * @since 0.0.0
     */
    public static byte getByte2(long v) {
        return (byte)(v >> 16);
    }

    /**
     * <p>
     * Gets 2-order-byte from specified double value. The order in byte from high to low (left to right) is 7 to 0.
     * </p>
     * 
     * @param v
     *            specified double value
     * @return 2-order-byte from specified value
     * @since 0.0.0
     */
    public static byte getByte2(double v) {
        return (byte)(Double.doubleToRawLongBits(v) >> 16);
    }

    /**
     * <p>
     * Gets 3-order-byte from specified int value. The order in byte from high to low (left to right) is 3 to 0.
     * </p>
     * 
     * @param v
     *            specified int value
     * @return 3-order-byte from specified value
     * @since 0.0.0
     */
    public static byte getByte3(int v) {
        return (byte)(v >> 24);
    }

    /**
     * <p>
     * Gets 3-order-byte from specified float value. The order in byte from high to low (left to right) is 3 to 0.
     * </p>
     * 
     * @param v
     *            specified float value
     * @return 3-order-byte from specified value
     * @since 0.0.0
     */
    public static byte getByte3(float v) {
        return (byte)(Float.floatToRawIntBits(v) >> 24);
    }

    /**
     * <p>
     * Gets 3-order-byte from specified long value. The order in byte from high to low (left to right) is 7 to 0.
     * </p>
     * 
     * @param v
     *            specified long value
     * @return 3-order-byte from specified value
     * @since 0.0.0
     */
    public static byte getByte3(long v) {
        return (byte)(v >> 24);
    }

    /**
     * <p>
     * Gets 3-order-byte from specified double value. The order in byte from high to low (left to right) is 7 to 0.
     * </p>
     * 
     * @param v
     *            specified double value
     * @return 3-order-byte from specified value
     * @since 0.0.0
     */
    public static byte getByte3(double v) {
        return (byte)(Double.doubleToRawLongBits(v) >> 24);
    }

    /**
     * <p>
     * Gets 4-order-byte from specified long value. The order in byte from high to low (left to right) is 7 to 0.
     * </p>
     * 
     * @param v
     *            specified long value
     * @return 4-order-byte from specified value
     * @since 0.0.0
     */
    public static byte getByte4(long v) {
        return (byte)(v >> 32);
    }

    /**
     * <p>
     * Gets 4-order-byte from specified double value. The order in byte from high to low (left to right) is 7 to 0.
     * </p>
     * 
     * @param v
     *            specified double value
     * @return 4-order-byte from specified value
     * @since 0.0.0
     */
    public static byte getByte4(double v) {
        return (byte)(Double.doubleToRawLongBits(v) >> 32);
    }

    /**
     * <p>
     * Gets 5-order-byte from specified long value. The order in byte from high to low (left to right) is 7 to 0.
     * </p>
     * 
     * @param v
     *            specified long value
     * @return 5-order-byte from specified value
     * @since 0.0.0
     */
    public static byte getByte5(long v) {
        return (byte)(v >> 40);
    }

    /**
     * <p>
     * Gets 5-order-byte from specified double value. The order in byte from high to low (left to right) is 7 to 0.
     * </p>
     * 
     * @param v
     *            specified double value
     * @return 5-order-byte from specified value
     * @since 0.0.0
     */
    public static byte getByte5(double v) {
        return (byte)(Double.doubleToRawLongBits(v) >> 40);
    }

    /**
     * <p>
     * Gets 6-order-byte from specified long value. The order in byte from high to low (left to right) is 7 to 0.
     * </p>
     * 
     * @param v
     *            specified long value
     * @return 6-order-byte from specified value
     * @since 0.0.0
     */
    public static byte getByte6(long v) {
        return (byte)(v >> 48);
    }

    /**
     * <p>
     * Gets 6-order-byte from specified double value. The order in byte from high to low (left to right) is 7 to 0.
     * </p>
     * 
     * @param v
     *            specified double value
     * @return 6-order-byte from specified value
     * @since 0.0.0
     */
    public static byte getByte6(double v) {
        return (byte)(Double.doubleToRawLongBits(v) >> 48);
    }

    /**
     * <p>
     * Gets 7-order-byte from specified long value. The order in byte from high to low (left to right) is 7 to 0.
     * </p>
     * 
     * @param v
     *            specified long value
     * @return 7-order-byte from specified value
     * @since 0.0.0
     */
    public static byte getByte7(long v) {
        return (byte)(v >> 56);
    }

    /**
     * <p>
     * Gets 7-order-byte from specified double value. The order in byte from high to low (left to right) is 7 to 0.
     * </p>
     * 
     * @param v
     *            specified double value
     * @return 7-order-byte from specified value
     * @since 0.0.0
     */
    public static byte getByte7(double v) {
        return (byte)(Double.doubleToRawLongBits(v) >> 56);
    }

    /**
     * <p>
     * Gets short from specified int value at specified order in short, the order form highest to lowest is 1 to 0.
     * </p>
     * <p>
     * Methods getShort are compositive of methods getShort3 to getShort0.
     * </p>
     * 
     * @param v
     *            specified int value
     * @param order
     *            order in short, [1, 0]
     * @return short at specified order in short
     * @since 0.0.0
     */
    public static short getShort(int v, int order) {
        return (short)(v >> (16 * order));
    }

    /**
     * <p>
     * Gets short from specified float value at specified order in short, the order form highest to lowest is 1 to 0.
     * </p>
     * <p>
     * Methods getShort are compositive of methods getShort3 to getShort0.
     * </p>
     * 
     * @param v
     *            specified float value
     * @param order
     *            order in short, [1, 0]
     * @return short at specified order in short
     * @since 0.0.0
     */
    public static short getShort(float v, int order) {
        return (short)(Float.floatToRawIntBits(v) >> (16 * order));
    }

    /**
     * <p>
     * Gets short from specified long value at specified order in short, the order form highest to lowest is 3 to 0.
     * </p>
     * <p>
     * Methods getShort are compositive of methods getShort3 to getShort0.
     * </p>
     * 
     * @param v
     *            specified long value
     * @param order
     *            order in short, [3, 0]
     * @return short at specified order in short
     * @since 0.0.0
     */
    public static short getShort(long v, int order) {
        return (short)(v >> (16 * order));
    }

    /**
     * <p>
     * Gets short from specified double value at specified order in short, the order form highest to lowest is 3 to 0.
     * </p>
     * <p>
     * Methods getShort are compositive of methods getShort3 to getShort0.
     * </p>
     * 
     * @param v
     *            specified double value
     * @param order
     *            order in short, [3, 0]
     * @return short at specified order in short
     * @since 0.0.0
     */
    public static short getShort(double v, int order) {
        return (short)(Double.doubleToRawLongBits(v) >> (16 * order));
    }

    /**
     * <p>
     * Gets 0-order-short from specified int value. The order in short from high to low (left to right) is 1 to 0.
     * </p>
     * 
     * @param v
     *            specified int value
     * @return 0-order-short from specified value
     * @since 0.0.0
     */
    public static short getShort0(int v) {
        return (short)(v >> 0);
    }

    /**
     * <p>
     * Gets 0-order-short from specified float value. The order in short from high to low (left to right) is 1 to 0.
     * </p>
     * 
     * @param v
     *            specified float value
     * @return 0-order-short from specified value
     * @since 0.0.0
     */
    public static short getShort0(float v) {
        return (short)(Float.floatToRawIntBits(v) >> 0);
    }

    /**
     * <p>
     * Gets 0-order-short from specified long value. The order in short from high to low (left to right) is 3 to 0.
     * </p>
     * 
     * @param v
     *            specified long value
     * @return 0-order-short from specified value
     * @since 0.0.0
     */
    public static short getShort0(long v) {
        return (short)(v >> 0);
    }

    /**
     * <p>
     * Gets 0-order-short from specified double value. The order in short from high to low (left to right) is 3 to 0.
     * </p>
     * 
     * @param v
     *            specified double value
     * @return 0-order-short from specified value
     * @since 0.0.0
     */
    public static short getShort0(double v) {
        return (short)(Double.doubleToRawLongBits(v) >> 0);
    }

    /**
     * <p>
     * Gets 1-order-short from specified int value. The order in short from high to low (left to right) is 1 to 0.
     * </p>
     * 
     * @param v
     *            specified int value
     * @return 1-order-short from specified value
     * @since 0.0.0
     */
    public static short getShort1(int v) {
        return (short)(v >> 16);
    }

    /**
     * <p>
     * Gets 1-order-short from specified float value. The order in short from high to low (left to right) is 1 to 0.
     * </p>
     * 
     * @param v
     *            specified float value
     * @return 1-order-short from specified value
     * @since 0.0.0
     */
    public static short getShort1(float v) {
        return (short)(Float.floatToRawIntBits(v) >> 16);
    }

    /**
     * <p>
     * Gets 1-order-short from specified long value. The order in short from high to low (left to right) is 3 to 0.
     * </p>
     * 
     * @param v
     *            specified long value
     * @return 1-order-short from specified value
     * @since 0.0.0
     */
    public static short getShort1(long v) {
        return (short)(v >> 16);
    }

    /**
     * <p>
     * Gets 1-order-short from specified double value. The order in short from high to low (left to right) is 3 to 0.
     * </p>
     * 
     * @param v
     *            specified double value
     * @return 1-order-short from specified value
     * @since 0.0.0
     */
    public static short getShort1(double v) {
        return (short)(Double.doubleToRawLongBits(v) >> 16);
    }

    /**
     * <p>
     * Gets 2-order-short from specified long value. The order in short from high to low (left to right) is 3 to 0.
     * </p>
     * 
     * @param v
     *            specified long value
     * @return 2-order-short from specified value
     * @since 0.0.0
     */
    public static short getShort2(long v) {
        return (short)(v >> 32);
    }

    /**
     * <p>
     * Gets 2-order-short from specified double value. The order in short from high to low (left to right) is 3 to 0.
     * </p>
     * 
     * @param v
     *            specified double value
     * @return 2-order-short from specified value
     * @since 0.0.0
     */
    public static short getShort2(double v) {
        return (short)(Double.doubleToRawLongBits(v) >> 32);
    }

    /**
     * <p>
     * Gets 3-order-short from specified long value. The order in short from high to low (left to right) is 3 to 0.
     * </p>
     * 
     * @param v
     *            specified long value
     * @return 3-order-short from specified value
     * @since 0.0.0
     */
    public static short getShort3(long v) {
        return (short)(v >> 48);
    }

    /**
     * <p>
     * Gets 3-order-short from specified double value. The order in short from high to low (left to right) is 3 to 0.
     * </p>
     * 
     * @param v
     *            specified double value
     * @return 3-order-short from specified value
     * @since 0.0.0
     */
    public static short getShort3(double v) {
        return (short)(Double.doubleToRawLongBits(v) >> 48);
    }

    /**
     * <p>
     * Gets char from specified int value at specified order in char, the order form highest to lowest is 1 to 0.
     * </p>
     * <p>
     * Methods getChar are compositive of methods getChar3 to getChar0.
     * </p>
     * 
     * @param v
     *            specified int value
     * @param order
     *            order in char, [1, 0]
     * @return char at specified order in char
     * @since 0.0.0
     */
    public static char getChar(int v, int order) {
        return (char)(v >> (16 * order));
    }

    /**
     * <p>
     * Gets char from specified float value at specified order in char, the order form highest to lowest is 1 to 0.
     * </p>
     * <p>
     * Methods getChar are compositive of methods getChar3 to getChar0.
     * </p>
     * 
     * @param v
     *            specified float value
     * @param order
     *            order in char, [1, 0]
     * @return char at specified order in char
     * @since 0.0.0
     */
    public static char getChar(float v, int order) {
        return (char)(Float.floatToRawIntBits(v) >> (16 * order));
    }

    /**
     * <p>
     * Gets char from specified long value at specified order in char, the order form highest to lowest is 3 to 0.
     * </p>
     * <p>
     * Methods getChar are compositive of methods getChar3 to getChar0.
     * </p>
     * 
     * @param v
     *            specified long value
     * @param order
     *            order in char, [3, 0]
     * @return char at specified order in char
     * @since 0.0.0
     */
    public static char getChar(long v, int order) {
        return (char)(v >> (16 * order));
    }

    /**
     * <p>
     * Gets char from specified double value at specified order in char, the order form highest to lowest is 3 to 0.
     * </p>
     * <p>
     * Methods getChar are compositive of methods getChar3 to getChar0.
     * </p>
     * 
     * @param v
     *            specified double value
     * @param order
     *            order in char, [3, 0]
     * @return char at specified order in char
     * @since 0.0.0
     */
    public static char getChar(double v, int order) {
        return (char)(Double.doubleToRawLongBits(v) >> (16 * order));
    }

    /**
     * <p>
     * Gets 0-order-char from specified int value. The order in char from high to low (left to right) is 1 to 0.
     * </p>
     * 
     * @param v
     *            specified int value
     * @return 0-order-char from specified value
     * @since 0.0.0
     */
    public static char getChar0(int v) {
        return (char)(v >> 0);
    }

    /**
     * <p>
     * Gets 0-order-char from specified int value. The order in char from high to low (left to right) is 1 to 0.
     * </p>
     * 
     * @param v
     *            specified int value
     * @return 0-order-char from specified value
     * @since 0.0.0
     */
    public static char getChar0(float v) {
        return (char)(Float.floatToRawIntBits(v) >> 0);
    }

    /**
     * <p>
     * Gets 0-order-char from specified long value. The order in char from high to low (left to right) is 1 to 0.
     * </p>
     * 
     * @param v
     *            specified long value
     * @return 0-order-char from specified value
     * @since 0.0.0
     */
    public static char getChar0(long v) {
        return (char)(v >> 0);
    }

    /**
     * <p>
     * Gets 0-order-char from specified double value. The order in char from high to low (left to right) is 1 to 0.
     * </p>
     * 
     * @param v
     *            specified double value
     * @return 0-order-char from specified value
     * @since 0.0.0
     */
    public static char getChar0(double v) {
        return (char)(Double.doubleToRawLongBits(v) >> 0);
    }

    /**
     * <p>
     * Gets 1-order-char from specified int value. The order in char from high to low (left to right) is 1 to 0.
     * </p>
     * 
     * @param v
     *            specified int value
     * @return 1-order-char from specified value
     * @since 0.0.0
     */
    public static char getChar1(int v) {
        return (char)(v >> 16);
    }

    /**
     * <p>
     * Gets 1-order-char from specified float value. The order in char from high to low (left to right) is 1 to 0.
     * </p>
     * 
     * @param v
     *            specified float value
     * @return 1-order-char from specified value
     * @since 0.0.0
     */
    public static char getChar1(float v) {
        return (char)(Float.floatToRawIntBits(v) >> 16);
    }

    /**
     * <p>
     * Gets 1-order-char from specified long value. The order in char from high to low (left to right) is 1 to 0.
     * </p>
     * 
     * @param v
     *            specified long value
     * @return 1-order-char from specified value
     * @since 0.0.0
     */
    public static char getChar1(long v) {
        return (char)(v >> 16);
    }

    /**
     * <p>
     * Gets 1-order-char from specified double value. The order in char from high to low (left to right) is 1 to 0.
     * </p>
     * 
     * @param v
     *            specified double value
     * @return 1-order-char from specified value
     * @since 0.0.0
     */
    public static char getChar1(double v) {
        return (char)(Double.doubleToRawLongBits(v) >> 16);
    }

    /**
     * <p>
     * Gets 2-order-char from specified long value. The order in char from high to low (left to right) is 1 to 0.
     * </p>
     * 
     * @param v
     *            specified long value
     * @return 2-order-char from specified value
     * @since 0.0.0
     */
    public static char getChar2(long v) {
        return (char)(v >> 32);
    }

    /**
     * <p>
     * Gets 2-order-char from specified double value. The order in char from high to low (left to right) is 1 to 0.
     * </p>
     * 
     * @param v
     *            specified double value
     * @return 2-order-char from specified value
     * @since 0.0.0
     */
    public static char getChar2(double v) {
        return (char)(Double.doubleToRawLongBits(v) >> 32);
    }

    /**
     * <p>
     * Gets 3-order-char from specified long value. The order in char from high to low (left to right) is 1 to 0.
     * </p>
     * 
     * @param v
     *            specified long value
     * @return 3-order-char from specified value
     * @since 0.0.0
     */
    public static char getChar3(long v) {
        return (char)(v >> 48);
    }

    /**
     * <p>
     * Gets 3-order-char from specified double value. The order in char from high to low (left to right) is 1 to 0.
     * </p>
     * 
     * @param v
     *            specified double value
     * @return 3-order-char from specified value
     * @since 0.0.0
     */
    public static char getChar3(double v) {
        return (char)(Double.doubleToRawLongBits(v) >> 48);
    }

    /**
     * <p>
     * Gets int from specified long value at specified order in int, the order form highest to lowest is 1 to 0.
     * </p>
     * <p>
     * Methods getInt are compositive of methods getInt1 to getInt0.
     * </p>
     * 
     * @param v
     *            specified long value
     * @param order
     *            order in int, [1, 0]
     * @return int at specified order in int
     * @since 0.0.0
     */
    public static int getInt(long v, int order) {
        return (int)(v >> (32 * order));
    }

    /**
     * <p>
     * Gets int from specified double value at specified order in int, the order form highest to lowest is 1 to 0.
     * </p>
     * <p>
     * Methods getInt are compositive of methods getInt1 to getInt0.
     * </p>
     * 
     * @param v
     *            specified double value
     * @param order
     *            order in int, [1, 0]
     * @return int at specified order in int
     * @since 0.0.0
     */
    public static int getInt(double v, int order) {
        return (int)(Double.doubleToRawLongBits(v) >> (32 * order));
    }

    /**
     * <p>
     * Gets 0-order-int from specified long value. The order in int from high to low (left to right) is 1 to 0.
     * </p>
     * 
     * @param v
     *            specified long value
     * @return 0-order-int from specified value
     * @since 0.0.0
     */
    public static int getInt0(long v) {
        return (int)(v >> 0);
    }

    /**
     * <p>
     * Gets 0-order-int from specified double value. The order in int from high to low (left to right) is 1 to 0.
     * </p>
     * 
     * @param v
     *            specified double value
     * @return 0-order-int from specified value
     * @since 0.0.0
     */
    public static int getInt0(double v) {
        return (int)(Double.doubleToRawLongBits(v) >> 0);
    }

    /**
     * <p>
     * Gets 1-order-int from specified long value. The order in int from high to low (left to right) is 1 to 0.
     * </p>
     * 
     * @param v
     *            specified long value
     * @return 1-order-int from specified value
     * @since 0.0.0
     */
    public static int getInt1(long v) {
        return (int)(v >> 32);
    }

    /**
     * <p>
     * Gets 1-order-int from specified double value. The order in int from high to low (left to right) is 1 to 0.
     * </p>
     * 
     * @param v
     *            specified double value
     * @return 1-order-int from specified value
     * @since 0.0.0
     */
    public static int getInt1(double v) {
        return (int)(Double.doubleToRawLongBits(v) >> 32);
    }

    /**
     * <p>
     * Gets float from specified long value at specified order in float, the order form highest to lowest is 1 to 0.
     * </p>
     * <p>
     * Methods getFloat are compositive of methods getFloat1 to getFloat0.
     * </p>
     * 
     * @param v
     *            specified long value
     * @param order
     *            order in float, [1, 0]
     * @return float at specified order in float
     * @since 0.0.0
     */
    public static float getFloat(long v, int order) {
        return Float.intBitsToFloat(getInt(v, order));
    }

    /**
     * <p>
     * Gets float from specified double value at specified order in float, the order form highest to lowest is 1 to 0.
     * </p>
     * <p>
     * Methods getFloat are compositive of methods getFloat1 to getFloat0.
     * </p>
     * 
     * @param v
     *            specified double value
     * @param order
     *            order in float, [1, 0]
     * @return float at specified order in float
     * @since 0.0.0
     */
    public static float getFloat(double v, int order) {
        return Float.intBitsToFloat(getInt(v, order));
    }

    /**
     * <p>
     * Gets 0-order-float from specified long value. The order in float from high to low (left to right) is 1 to 0.
     * </p>
     * 
     * @param v
     *            specified long value
     * @return 0-order-float from specified value
     * @since 0.0.0
     */
    public static float getFloat0(long v) {
        return Float.intBitsToFloat(getInt0(v));
    }

    /**
     * <p>
     * Gets 0-order-float from specified double value. The order in float from high to low (left to right) is 1 to 0.
     * </p>
     * 
     * @param v
     *            specified double value
     * @return 0-order-float from specified value
     * @since 0.0.0
     */
    public static float getFloat0(double v) {
        return Float.intBitsToFloat(getInt0(v));
    }

    /**
     * <p>
     * Gets 1-order-float from specified long value. The order in float from high to low (left to right) is 1 to 0.
     * </p>
     * 
     * @param v
     *            specified long value
     * @return 1-order-float from specified value
     * @since 0.0.0
     */
    public static float getFloat1(long v) {
        return Float.intBitsToFloat(getInt1(v));
    }

    /**
     * <p>
     * Gets 1-order-float from specified double value. The order in float from high to low (left to right) is 1 to 0.
     * </p>
     * 
     * @param v
     *            specified double value
     * @return 1-order-float from specified value
     * @since 0.0.0
     */
    public static float getFloat1(double v) {
        return Float.intBitsToFloat(getInt1(v));
    }
}
