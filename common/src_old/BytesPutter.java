package com.cogician.quicker.binary;

import com.cogician.quicker.OutOfBoundsException;

/**
 * <p>
 * Component interface of {@linkplain Block}, provides methods to random write
 * bytes into a continuous binary space, if the written value represents a
 * numeric value, it will be reordered according to the current byte order when
 * written, the byte order is controlled by implementation.
 * </p>
 * <p>
 * Binary space is in bounds of {@linkplain Integer#MAX_VALUE} bytes, access in
 * bytes. It can be in memory, external accessor, or other writable object.
 * </p>
 * 
 * @author Fred Suvn
 * @version 0.0.0, 2015-11-16 13:59:00
 * @since 0.0.0
 * @see Block
 */
public interface BytesPutter {

    /**
     * <p>
     * Writes 1 byte at specified byte index, if specified value is true, put 1,
     * else put 0.
     * </p>
     * 
     * @param byteIndex
     *            specified byte index, in bounds
     * @param value
     *            specified value to be written
     * @throws OutOfBoundsException
     *             if written byte out of bounds
     * @since 0.0.0
     */
    public void putBoolean(int byteIndex, boolean value);

    /**
     * <p>
     * Writes lowest byte of specified value at specified byte index, high 24
     * order bits ignored.
     * </p>
     * 
     * @param byteIndex
     *            specified byte index, in bounds
     * @param value
     *            specified value to be written
     * @throws OutOfBoundsException
     *             if written byte out of bounds
     * @since 0.0.0
     */
    public void putByte(int byteIndex, int value);

    /**
     * <p>
     * Writes low 2 bytes of specified value at specified byte index according
     * to the current byte order, high 16 order bits ignored.
     * </p>
     * 
     * @param byteIndex
     *            specified byte index, in bounds
     * @param value
     *            specified value to be written
     * @throws OutOfBoundsException
     *             if written bytes out of bounds
     * @since 0.0.0
     */
    public void putShort(int byteIndex, int value);

    /**
     * <p>
     * Writes low 2 bytes of specified value at specified byte index according
     * to the current byte order, high 16 order bits ignored.
     * </p>
     * 
     * @param byteIndex
     *            specified byte index, in bounds
     * @param value
     *            specified value to be written
     * @throws OutOfBoundsException
     *             if written bytes out of bounds
     * @since 0.0.0
     */
    public void putChar(int byteIndex, int value);

    /**
     * <p>
     * Writes low 3 bytes of specified value at specified byte index according
     * to the current byte order, high 8 bit order bits ignored.
     * </p>
     * 
     * @param byteIndex
     *            specified byte index, in bounds
     * @param value
     *            specified value to be written
     * @throws OutOfBoundsException
     *             if written bytes out of bounds
     * @since 0.0.0
     */
    public void putMedium(int byteIndex, int value);

    /**
     * <p>
     * Writes specified int value at specified byte index according to the
     * current byte order.
     * </p>
     * 
     * @param byteIndex
     *            specified byte index, in bounds
     * @param value
     *            specified value to be written
     * @throws OutOfBoundsException
     *             if written bytes out of bounds
     * @since 0.0.0
     */
    public void putInt(int byteIndex, int value);

    /**
     * <p>
     * Writes specified long value at specified byte index according to the
     * current byte order.
     * </p>
     * 
     * @param byteIndex
     *            specified byte index, in bounds
     * @param value
     *            specified value to be written
     * @throws OutOfBoundsException
     *             if written bytes out of bounds
     * @since 0.0.0
     */
    public void putLong(int byteIndex, long value);

    /**
     * <p>
     * Writes bytes of specified number at specified byte index according in the
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
     * @param byteIndex
     *            specified byte index, in bounds
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
    public void putNumeric(int byteIndex, int value, int bytesNum);

    /**
     * <p>
     * Writes bytes of specified number at specified byte index according in the
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
     * @param byteIndex
     *            specified byte index, in bounds
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
    public void putLongNumeric(int byteIndex, long value, int bytesNum);

    /**
     * <p>
     * Writes specified float value at specified byte index according to the
     * current byte order.
     * </p>
     * 
     * @param byteIndex
     *            specified byte index, in bounds
     * @param value
     *            specified value to be written
     * @throws OutOfBoundsException
     *             if written bytes out of bounds
     * @since 0.0.0
     */
    public void putFloat(int byteIndex, float value);

    /**
     * <p>
     * Writes specified double value at specified byte index according to the
     * current byte order.
     * </p>
     * 
     * @param byteIndex
     *            specified byte index, in bounds
     * @param value
     *            specified value to be written
     * @throws OutOfBoundsException
     *             if written bytes out of bounds
     * @since 0.0.0
     */
    public void putDouble(int byteIndex, double value);

    /**
     * <p>
     * Writes bytes of specified number at specified byte index. Written bytes
     * in big-endian order (left-to-right), for example:
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
     * @param byteIndex
     *            specified byte index, in bounds
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
    public void putBytes(int byteIndex, int value, int bytesNum);

    /**
     * <p>
     * Writes bytes of specified number at specified byte index. Written bytes
     * in big-endian order (left-to-right), for example:
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
     * @param byteIndex
     *            specified byte index, in bounds
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
    public void putLongBytes(int byteIndex, long value, int bytesNum);
}
