package com.cogician.quicker.binary;

import com.cogician.quicker.OutOfBoundsException;

/**
 * <p>
 * Component interface of {@linkplain Bits}, provides methods to random write
 * bits into a continuous binary space, if the written value represents a
 * numeric value, it will be reordered according to the current byte order when
 * written, the byte order is controlled by implementation.
 * </p>
 * <p>
 * Binary space is in bounds of {@linkplain Integer#MAX_VALUE} bytes, access in
 * bits. It can be in memory, external accessor, or other writable object.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2015-03-05 14:21:14
 * @since 0.0.0
 * @see Bits
 */
interface BitsPutter extends BytesPutter {

    /**
     * <p>
     * Writes 1 bit at specified bit index, if specified value is true, put 1,
     * else put 0.
     * </p>
     * 
     * @param bitIndex
     *            specified bit index, in bounds
     * @param value
     *            specified value to be written
     * @throws OutOfBoundsException
     *             if written byte out of bounds
     * @since 0.0.0
     */
    public void putBoolean(long bitIndex, boolean value);

    /**
     * <p>
     * Writes lowest byte of specified value at specified bit index, high 24
     * order bits ignored.
     * </p>
     * 
     * @param bitIndex
     *            specified bit index, in bounds
     * @param value
     *            specified value to be written
     * @throws OutOfBoundsException
     *             if written byte out of bounds
     * @since 0.0.0
     */
    public void putByte(long bitIndex, int value);

    /**
     * <p>
     * Writes low 2 bytes of specified value at specified bit index according to
     * the current byte order, high 16 order bits ignored.
     * </p>
     * 
     * @param bitIndex
     *            specified bit index, in bounds
     * @param value
     *            specified value to be written
     * @throws OutOfBoundsException
     *             if written bytes out of bounds
     * @since 0.0.0
     */
    public void putShort(long bitIndex, int value);

    /**
     * <p>
     * Writes low 2 bytes of specified value at specified bit index according to
     * the current byte order, high 16 order bits ignored.
     * </p>
     * 
     * @param bitIndex
     *            specified bit index, in bounds
     * @param value
     *            specified value to be written
     * @throws OutOfBoundsException
     *             if written bytes out of bounds
     * @since 0.0.0
     */
    public void putChar(long bitIndex, int value);

    /**
     * <p>
     * Writes low 3 bytes of specified value at specified bit index according to
     * the current byte order, high 8 bit order bits ignored.
     * </p>
     * 
     * @param bitIndex
     *            specified bit index, in bounds
     * @param value
     *            specified value to be written
     * @throws OutOfBoundsException
     *             if written bytes out of bounds
     * @since 0.0.0
     */
    public void putMedium(int bitIndex, int value);

    /**
     * <p>
     * Writes specified int value at specified bit index according to the
     * current byte order.
     * </p>
     * 
     * @param bitIndex
     *            specified bit index, in bounds
     * @param value
     *            specified value to be written
     * @throws OutOfBoundsException
     *             if written bytes out of bounds
     * @since 0.0.0
     */
    public void putInt(long bitIndex, int value);

    /**
     * <p>
     * Writes specified long value at specified bit index according to the
     * current byte order.
     * </p>
     * 
     * @param bitIndex
     *            specified bit index, in bounds
     * @param value
     *            specified value to be written
     * @throws OutOfBoundsException
     *             if written bytes out of bounds
     * @since 0.0.0
     */
    public void putLong(long bitIndex, long value);

    /**
     * <p>
     * Writes bytes of specified number at specified bit index according in the
     * current byte order. Written bytes in the low order, for example:
     * 
     * <pre>
     * int i = 0x01020304;
     * putNumeric(0, i, 3);
     * </pre>
     * If current byte order is big-endian, actual written bytes above is
     * "0x02, 0x03, 0x04".
     * </p>
     * 
     * @param bitIndex
     *            specified bit index, in bounds
     * @param value
     *            specified value to be written
     * @param bytesNum
     *            specified number of bytes to be read, [1, 4]
     * @throws IllegalArgumentException
     *             if specified number out of bounds
     * @throws OutOfBoundsException
     *             if written bytes out of bounds
     * @since 0.0.0
     */
    public void putNumeric(long bitIndex, int value, int bytesNum);

    /**
     * <p>
     * Writes bytes of specified number at specified bit index according in the
     * current byte order. Written bytes in the low order, for example:
     * 
     * <pre>
     * long l = 0x0102030405060708L;
     * putNumeric(0, l, 3);
     * </pre>
     * If current byte order is big-endian, actual written bytes above is
     * "0x06, 0x07, 0x08".
     * </p>
     * 
     * @param bitIndex
     *            specified bit index, in bounds
     * @param value
     *            specified value to be written
     * @param bytesNum
     *            specified number of bytes to be read, [1, 8]
     * @throws IllegalArgumentException
     *             if specified number out of bounds
     * @throws OutOfBoundsException
     *             if written bytes out of bounds
     * @since 0.0.0
     */
    public void putLongNumeric(long bitIndex, long value, int bytesNum);

    /**
     * <p>
     * Writes bits of specified number at specified bit index. Written bits in
     * the low order, for example:
     * 
     * <pre>
     * int i = 0x01020304;
     * putNumericBit(0, i, 24);
     * </pre>
     * Actual written bits above is "0x02, 0x03, 0x04".
     * </p>
     * 
     * @param bitIndex
     *            specified bit index, in bounds
     * @param value
     *            specified value to be written
     * @param bitsNum
     *            specified number of bits to be read, [1, 32]
     * @throws IllegalArgumentException
     *             if specified number out of bounds
     * @throws OutOfBoundsException
     *             if written bytes out of bounds
     * @since 0.0.0
     */
    public void putNumericBits(long bitIndex, int value, int bitsNum);

    /**
     * <p>
     * Writes bits of specified number at specified bit index. Written bits in
     * the low order, for example:
     * 
     * <pre>
     * long l = 0x0102030405060708L;
     * putNumericBit(0, l, 24);
     * </pre>
     * Actual written bytes above is "0x06, 0x07, 0x08".
     * </p>
     * 
     * @param bitIndex
     *            specified bit index, in bounds
     * @param value
     *            specified value to be written
     * @param bitsNum
     *            specified number of bits to be read, [1, 64]
     * @throws IllegalArgumentException
     *             if specified number out of bounds
     * @throws OutOfBoundsException
     *             if written bytes out of bounds
     * @since 0.0.0
     */
    public void putLongNumericBits(long bitIndex, long value, int bitsNum);

    /**
     * <p>
     * Writes specified float value at specified bit index according to the
     * current byte order.
     * </p>
     * 
     * @param bitIndex
     *            specified bit index, in bounds
     * @param value
     *            specified value to be written
     * @throws OutOfBoundsException
     *             if written bytes out of bounds
     * @since 0.0.0
     */
    public void putFloat(long bitIndex, float value);

    /**
     * <p>
     * Writes specified double value at specified bit index according to the
     * current byte order.
     * </p>
     * 
     * @param bitIndex
     *            specified bit index, in bounds
     * @param value
     *            specified value to be written
     * @throws OutOfBoundsException
     *             if written bytes out of bounds
     * @since 0.0.0
     */
    public void putDouble(long bitIndex, double value);

    /**
     * <p>
     * Writes bytes of specified number at specified bit index. Written bytes in
     * big-endian order (left-to-right), for example:
     * 
     * <pre>
     * int i = 0x01020304;
     * putBytes(0, i, 3);
     * </pre>
     * Actual written bytes above is "0x01, 0x02, 0x03".
     * </p>
     * <p>
     * This method ignores the current byte order.
     * </p>
     * 
     * @param bitIndex
     *            specified bit index, in bounds
     * @param value
     *            specified value to be written
     * @param bytesNum
     *            specified number of bytes to be read, [1, 4]
     * @throws IllegalArgumentException
     *             if specified number out of bounds
     * @throws OutOfBoundsException
     *             if written bytes out of bounds
     * @since 0.0.0
     */
    public void putBytes(long bitIndex, int value, int bytesNum);

    /**
     * <p>
     * Writes bytes of specified number at specified bit index. Written bytes in
     * big-endian order (left-to-right), for example:
     * 
     * <pre>
     * long l = 0x0102030405060708L;
     * putBytes(0, l, 3);
     * </pre>
     * Actual written bytes above is "0x01, 0x02, 0x03".
     * </p>
     * <p>
     * This method ignores the current byte order.
     * </p>
     * 
     * @param bitIndex
     *            specified bit index, in bounds
     * @param value
     *            specified value to be written
     * @param bytesNum
     *            specified number of bytes to be read, [1, 8]
     * @throws IllegalArgumentException
     *             if specified number out of bounds
     * @throws OutOfBoundsException
     *             if written bytes out of bounds
     * @since 0.0.0
     */
    public void putLongBytes(long bitIndex, long value, int bytesNum);

    /**
     * <p>
     * Writes bits of specified number at specified bit index. Written bits
     * always from high to low order (left-to-right), for example:
     * 
     * <pre>
     * int i = 0x01020304;
     * putBits(0, i, 24);
     * </pre>
     * Actual written bits above is "0x01, 0x02, 0x03".
     * </p>
     * <p>
     * There is no problem about the byte order in this method.
     * </p>
     * 
     * @param bitIndex
     *            specified bit index, in bounds
     * @param value
     *            specified value to be written
     * @param bitsNum
     *            specified number of bits to be read, [1, 32]
     * @throws IllegalArgumentException
     *             if specified number out of bounds
     * @throws OutOfBoundsException
     *             if written bytes out of bounds
     * @since 0.0.0
     */
    public void putBits(long bitIndex, int value, int bitsNum);

    /**
     * <p>
     * Writes bits of specified number at specified bit index. Written bits
     * always from high to low order (left-to-right), for example:
     * 
     * <pre>
     * long l = 0x0102030405060708L;
     * putBits(0, l, 24);
     * </pre>
     * Actual written bytes above is "0x01, 0x02, 0x03".
     * </p>
     * <p>
     * There is no problem about the byte order in this method.
     * </p>
     * 
     * @param bitIndex
     *            specified bit index, in bounds
     * @param value
     *            specified value to be written
     * @param bitsNum
     *            specified number of bits to be read, [1, 64]
     * @throws IllegalArgumentException
     *             if specified number out of bounds
     * @throws OutOfBoundsException
     *             if written bytes out of bounds
     * @since 0.0.0
     */
    public void putLongBits(long bitIndex, long value, int bitsNum);
}
