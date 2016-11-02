package com.cogician.quicker.binary;

import com.cogician.quicker.OutOfBoundsException;

/**
 * <p>
 * Component interface of {@linkplain Block}, provides methods to random read
 * bytes from a continuous binary space. The read bytes will be composed into a
 * primitive value, if the primitive value represent a number, it will be
 * reordered according to the current byte order, the byte order is controlled
 * by implementation.
 * </p>
 * <p>
 * Binary space is in bounds of {@linkplain Integer#MAX_VALUE} bytes, access in
 * bytes. It can be in memory, external accessor, or other readable object.
 * </p>
 * 
 * @author Fred Suvn
 * @version 0.0.0, 2015-11-16 13:58:44
 * @since 0.0.0
 * @see Block
 */
interface BytesGetter {

    /**
     * <p>
     * Reads 1 byte at specified byte index, if the byte is nonzero, return
     * true, else return false.
     * </p>
     * 
     * @param byteIndex
     *            specified byte index, in bounds
     * @return true if byte at specified byte index is nonzero, false else
     * @throws OutOfBoundsException
     *             if read byte out of bounds
     * @since 0.0.0
     */
    public boolean getBoolean(int byteIndex);

    /**
     * <p>
     * Reads 1 byte at specified byte index.
     * </p>
     * 
     * @param byteIndex
     *            specified byte index, in bounds
     * @return byte value at specified byte index
     * @throws OutOfBoundsException
     *             if read byte out of bounds
     * @since 0.0.0
     */
    public byte getByte(int byteIndex);

    /**
     * <p>
     * Reads 1 byte at specified byte index, zero-extending it to an int value
     * according to the current byte order. The int value returned.
     * </p>
     * 
     * @param byteIndex
     *            specified byte index, in bounds
     * @return int value zero-extended by read byte at specified byte index
     * @throws OutOfBoundsException
     *             if read byte out of bounds
     * @since 0.0.0
     */
    public int getUnsignedByte(int byteIndex);

    /**
     * <p>
     * Reads 2 bytes from specified byte index inclusive, composing them into a
     * short value according to the current byte order. The short value
     * returned.
     * </p>
     * 
     * @param byteIndex
     *            specified byte index, in bounds
     * @return short value composed of read bytes from specified byte index
     * @throws OutOfBoundsException
     *             if read bytes out of bounds
     * @since 0.0.0
     */
    public short getShort(int byteIndex);

    /**
     * <p>
     * Reads 2 bytes from specified byte index inclusive, composing them into a
     * short value according to the current byte order, and then zero-extends
     * the short value to an int value. The int value returned.
     * </p>
     * 
     * @param byteIndex
     *            specified byte index, in bounds
     * @return int value zero-extended by read short from specified byte index
     * @throws OutOfBoundsException
     *             if read bytes out of bounds
     * @since 0.0.0
     */
    public int getUnsignedShort(int byteIndex);

    /**
     * <p>
     * Reads 2 bytes from specified byte index inclusive, composing them into a
     * char value according to the current byte order. The char value returned.
     * </p>
     * 
     * @param byteIndex
     *            specified byte index, in bounds
     * @return char value composed of read bytes from specified byte index
     * @throws OutOfBoundsException
     *             if read bytes out of bounds
     * @since 0.0.0
     */
    public char getChar(int byteIndex);

    /**
     * <p>
     * Reads 3 bytes from specified byte index inclusive, composing and
     * sign-extending them into an int value according to the current byte
     * order. The int value returned.
     * </p>
     * 
     * @param byteIndex
     *            specified byte index, in bounds
     * @return int value composed and sign-extended of read bytes from specified
     *         byte index
     * @throws OutOfBoundsException
     *             if read bytes out of bounds
     * @since 0.0.0
     */
    public int getMedium(int byteIndex);

    /**
     * <p>
     * Reads 3 bytes from specified byte index inclusive, composing and
     * zero-extending them into an int value according to the current byte
     * order. The int value returned.
     * </p>
     * 
     * @param byteIndex
     *            specified byte index, in bounds
     * @return int value composed and zero-extended of read bytes from specified
     *         byte index
     * @throws OutOfBoundsException
     *             if read bytes out of bounds
     * @since 0.0.0
     */
    public int getUnsignedMedium(int byteIndex);

    /**
     * <p>
     * Reads 4 bytes from specified byte index inclusive, composing them into an
     * int value according to the current byte order. The int value returned.
     * </p>
     * 
     * @param byteIndex
     *            specified byte index, in bounds
     * @return int value composed of read bytes from specified byte index
     * @throws OutOfBoundsException
     *             if read bytes out of bounds
     * @since 0.0.0
     */
    public int getInt(int byteIndex);

    /**
     * <p>
     * Reads 4 bytes from specified byte index inclusive, composing them into an
     * int value according to the current byte order, and then zero-extends the
     * int value to a long value. The long value returned.
     * </p>
     * 
     * @param byteIndex
     *            specified byte index, in bounds
     * @return long value zero-extended by read int from specified byte index
     * @throws OutOfBoundsException
     *             if read bytes out of bounds
     * @since 0.0.0
     */
    public long getUnsignedInt(int byteIndex);

    /**
     * <p>
     * Reads 8 bytes from specified byte index inclusive, composing them into a
     * long value according to the current byte order. The long value returned.
     * </p>
     * 
     * @param byteIndex
     *            specified byte index, in bounds
     * @return long value composed of read bytes from specified byte index
     * @throws OutOfBoundsException
     *             if read bytes out of bounds
     * @since 0.0.0
     */
    public long getLong(int byteIndex);

    /**
     * <p>
     * Reads bytes of specified number from specified byte index inclusive,
     * composing them into an numeric value according to the current byte order,
     * if specified number is less than 4, the numeric value will be
     * sign-extended to an int value. The int value returned.
     * </p>
     * 
     * @param byteIndex
     *            specified byte index, in bounds
     * @param bytesNum
     *            specified number of bytes to be read, [1, 4]
     * @return int value sign-extended by read numeric from specified byte index
     * @throws IllegalArgumentException
     *             if specified number out of bounds
     * @throws OutOfBoundsException
     *             if read bytes out of bounds
     * @since 0.0.0
     */
    public int getNumeric(int byteIndex, int bytesNum);

    /**
     * <p>
     * Reads bytes of specified number from specified byte index inclusive,
     * composing them into an numeric value according to the current byte order,
     * if specified number is less than 4, the numeric value will be
     * zero-extended to an int value. The int value returned.
     * </p>
     * 
     * @param byteIndex
     *            specified byte index, in bounds
     * @param bytesNum
     *            specified number of bytes to be read, [1, 4]
     * @return int value zero-extended by read numeric from specified byte index
     * @throws IllegalArgumentException
     *             if specified number out of bounds
     * @throws OutOfBoundsException
     *             if read bytes out of bounds
     * @since 0.0.0
     */
    public int getUnsignedNumeric(int byteIndex, int bytesNum);

    /**
     * <p>
     * Reads bytes of specified number from specified byte index inclusive,
     * composing them into an numeric value according to the current byte order,
     * if specified number is less than 8, the numeric value will be
     * sign-extended to a long value. The long value returned.
     * </p>
     * 
     * @param byteIndex
     *            specified byte index, in bounds
     * @param bytesNum
     *            specified number of bytes to be read, [1, 8]
     * @return long value sign-extended by read numeric from specified byte
     *         index
     * @throws IllegalArgumentException
     *             if specified number out of bounds
     * @throws OutOfBoundsException
     *             if read bytes out of bounds
     * @since 0.0.0
     */
    public Long getLongNumeric(int byteIndex, int bytesNum);

    /**
     * <p>
     * Reads bytes of specified number from specified byte index inclusive,
     * composing them into an numeric value according to the current byte order,
     * if specified number is less than 8, the numeric value will be
     * zero-extended to a long value. The long value returned.
     * </p>
     * 
     * @param byteIndex
     *            specified byte index, in bounds
     * @param bytesNum
     *            specified number of bytes to be read, [1, 8]
     * @return long value zero-extended by read numeric from specified byte
     *         index
     * @throws IllegalArgumentException
     *             if specified number out of bounds
     * @throws OutOfBoundsException
     *             if read bytes out of bounds
     * @since 0.0.0
     */
    public Long getUnsignedLongNumeric(int byteIndex, int bytesNum);

    /**
     * <p>
     * Reads 4 bytes from specified byte index inclusive, composing them into a
     * float value according to the current byte order. The float value
     * returned.
     * </p>
     * <p>
     * Note floating-point value's byte order is not necessarily equal to
     * numeric value's at same time.
     * </p>
     * 
     * @param byteIndex
     *            specified byte index, in bounds
     * @return float value composed of read bytes from specified byte index
     * @throws OutOfBoundsException
     *             if read bytes out of bounds
     * @since 0.0.0
     */
    public float getFloat(int byteIndex);

    /**
     * <p>
     * Reads 8 bytes from specified byte index inclusive, composing them into a
     * double value according to the current byte order. The double value
     * returned.
     * </p>
     * <p>
     * Note floating-point value's byte order is not necessarily equal to
     * numeric value's at same time.
     * </p>
     * 
     * @param byteIndex
     *            specified byte index, in bounds
     * @return double value composed of read bytes from specified byte index
     * @throws OutOfBoundsException
     *             if read bytes out of bounds
     * @since 0.0.0
     */
    public double getDouble(int byteIndex);

    /**
     * <p>
     * Reads at most 4 bytes from specified byte index inclusive, composing them
     * into an int value with that first byte into the highest byte order,
     * second byte into next byte order and so on--like big-endian byte order.
     * If the specified number of read bytes is less than 4, the rest orders
     * will be filled 0. The int value returned.
     * </p>
     * <p>
     * This method ignores the current byte order.
     * </p>
     * 
     * @param byteIndex
     *            specified byte index, in bounds
     * @param bytesNum
     *            specified number of bytes to be read, [1, 4]
     * @return int value composed of read bytes from specified byte index
     * @throws IllegalArgumentException
     *             if specified number out of bounds
     * @throws OutOfBoundsException
     *             if read bytes out of bounds
     * @since 0.0.0
     */
    public int getBytes(int byteIndex, int bytesNum);

    /**
     * <p>
     * Reads at most 8 bytes from specified byte index inclusive, composing them
     * into a long value with that first byte into the highest byte order,
     * second byte into next byte order and so on--like big-endian byte order.
     * If the specified number of read bytes is less than 8, the rest orders
     * will be filled 0. The long value returned.
     * </p>
     * <p>
     * This method ignores the current byte order.
     * </p>
     * 
     * @param byteIndex
     *            specified byte index, in bounds
     * @param bytesNum
     *            specified number of bytes to be read, [1, 8]
     * @return long value composed of read bytes from specified byte index
     * @throws IllegalArgumentException
     *             if specified number out of bounds
     * @throws OutOfBoundsException
     *             if read bytes out of bounds
     * @since 0.0.0
     */
    public long getLongBytes(int byteIndex, int bytesNum);
}
