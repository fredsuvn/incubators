package com.cogician.quicker.binary;

import com.cogician.quicker.OutOfBoundsException;
import com.cogician.quicker.bigarray.BigArray;

/**
 * <p>
 * Component interface of {@linkplain Bits}, provides methods to random read
 * bits from a continuous binary space. The read bits will be composed into a
 * primitive value, if the primitive value represent a number, it will be
 * reordered according to the current byte order, the byte order is controlled
 * by implementation.
 * </p>
 * <p>
 * Binary space is in bounds of {@linkplain Integer#MAX_VALUE} bytes, access in
 * bits. It can be in memory, external accessor, or other readable object.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2015-03-05 14:16:33
 * @since 0.0.0
 * @see Bits
 */
interface BitsGetter extends BytesGetter {

    /**
     * <p>
     * Reads 1 bit at specified bit index, if the bit is 1, return true, else
     * return false.
     * </p>
     * 
     * @param bitIndex
     *            specified bit index, in bounds
     * @return true if bit at specified bit index is 1, false else
     * @throws OutOfBoundsException
     *             if read byte out of bounds
     * @since 0.0.0
     */
    public boolean getBoolean(long bitIndex);

    /**
     * <p>
     * Reads 1 byte at specified bit index.
     * </p>
     * 
     * @param bitIndex
     *            specified bit index, in bounds
     * @return byte value at specified bit index
     * @throws OutOfBoundsException
     *             if read byte out of bounds
     * @since 0.0.0
     */
    public byte getByte(long bitIndex);

    /**
     * <p>
     * Reads 1 byte at specified bit index, zero-extending it to an int value
     * according to the current byte order. The int value returned.
     * </p>
     * 
     * @param bitIndex
     *            specified bit index, in bounds
     * @return int value zero-extended by read byte at specified bit index
     * @throws OutOfBoundsException
     *             if read byte out of bounds
     * @since 0.0.0
     */
    public int getUnsignedByte(long bitIndex);

    /**
     * <p>
     * Reads 2 bytes from specified bit index inclusive, composing them into a
     * short value according to the current byte order. The short value
     * returned.
     * </p>
     * 
     * @param bitIndex
     *            specified bit index, in bounds
     * @return short value composed of read bytes from specified bit index
     * @throws OutOfBoundsException
     *             if read bytes out of bounds
     * @since 0.0.0
     */
    public short getShort(long bitIndex);

    /**
     * <p>
     * Reads 2 bytes from specified bit index inclusive, composing them into a
     * short value according to the current byte order, and then zero-extends
     * the short value to an int value. The int value returned.
     * </p>
     * 
     * @param bitIndex
     *            specified bit index, in bounds
     * @return int value zero-extended by read short from specified bit index
     * @throws OutOfBoundsException
     *             if read bytes out of bounds
     * @since 0.0.0
     */
    public int getUnsignedShort(long bitIndex);

    /**
     * <p>
     * Reads 2 bytes from specified bit index inclusive, composing them into a
     * char value according to the current byte order. The char value returned.
     * </p>
     * 
     * @param bitIndex
     *            specified bit index, in bounds
     * @return char value composed of read bytes from specified bit index
     * @throws OutOfBoundsException
     *             if read bytes out of bounds
     * @since 0.0.0
     */
    public char getChar(long bitIndex);

    /**
     * <p>
     * Reads 3 bytes from specified bit index inclusive, composing and
     * sign-extending them into an int value according to the current byte
     * order. The int value returned.
     * </p>
     * 
     * @param bitIndex
     *            specified bit index, in bounds
     * @return int value composed and sign-extended of read bytes from specified
     *         bit index
     * @throws OutOfBoundsException
     *             if read bytes out of bounds
     * @since 0.0.0
     */
    public int getMedium(long bitIndex);
    
    /**
     * <p>
     * Reads 3 bytes from specified bit index inclusive, composing and
     * zero-extending them into an int value according to the current byte
     * order. The int value returned.
     * </p>
     * 
     * @param bitIndex
     *            specified bit index, in bounds
     * @return int value composed and zero-extended of read bytes from specified
     *         bit index
     * @throws OutOfBoundsException
     *             if read bytes out of bounds
     * @since 0.0.0
     */
    public int getUnsignedMedium(long bitIndex);
    
    

    /**
     * <p>
     * Reads 4 bytes from specified bit index inclusive, composing them into an
     * int value according to the current byte order. The int value returned.
     * </p>
     * 
     * @param bitIndex
     *            specified bit index, in bounds
     * @return int value composed of read bytes from specified bit index
     * @throws OutOfBoundsException
     *             if read bytes out of bounds
     * @since 0.0.0
     */
    public int getInt(long bitIndex);

    /**
     * <p>
     * Reads 4 bytes from specified bit index inclusive, composing them into an
     * int value according to the current byte order, and then zero-extends the
     * int value to a long value. The long value returned.
     * </p>
     * 
     * @param bitIndex
     *            specified bit index, in bounds
     * @return long value zero-extended by read int from specified bit index
     * @throws OutOfBoundsException
     *             if read bytes out of bounds
     * @since 0.0.0
     */
    public long getUnsignedInt(long bitIndex);

    /**
     * <p>
     * Reads 8 bytes from specified bit index inclusive, composing them into a
     * long value according to the current byte order. The long value returned.
     * </p>
     * 
     * @param bitIndex
     *            specified bit index, in bounds
     * @return long value composed of read bytes from specified bit index
     * @throws OutOfBoundsException
     *             if read bytes out of bounds
     * @since 0.0.0
     */
    public long getLong(long bitIndex);

    /**
     * <p>
     * Reads bytes of specified number from specified bit index inclusive,
     * composing them into an numeric value according to the current byte order,
     * if specified number is less than 4, the numeric value will be
     * sign-extended to an int value. The int value returned.
     * </p>
     * 
     * @param bitIndex
     *            specified bit index, in bounds
     * @param bytesNum
     *            specified number of bytes to be read, [1, 4]
     * @return int value sign-extended by read numeric from specified bit index
     * @throws IllegalArgumentException
     *             if specified number out of bounds
     * @throws OutOfBoundsException
     *             if read bytes out of bounds
     * @since 0.0.0
     */
    public int getNumeric(long bitIndex, int bytesNum);

    /**
     * <p>
     * Reads bytes of specified number from specified bit index inclusive,
     * composing them into an numeric value according to the current byte order,
     * if specified number is less than 4, the numeric value will be
     * zero-extended to an int value. The int value returned.
     * </p>
     * 
     * @param bitIndex
     *            specified bit index, in bounds
     * @param bytesNum
     *            specified number of bytes to be read, [1, 4]
     * @return int value zero-extended by read numeric from specified bit index
     * @throws IllegalArgumentException
     *             if specified number out of bounds
     * @throws OutOfBoundsException
     *             if read bytes out of bounds
     * @since 0.0.0
     */
    public int getUnsignedNumeric(long bitIndex, int bytesNum);

    /**
     * <p>
     * Reads bytes of specified number from specified bit index inclusive,
     * composing them into an numeric value according to the current byte order,
     * if specified number is less than 8, the numeric value will be
     * sign-extended to a long value. The long value returned.
     * </p>
     * 
     * @param bitIndex
     *            specified bit index, in bounds
     * @param bytesNum
     *            specified number of bytes to be read, [1, 8]
     * @return long value sign-extended by read numeric from specified bit index
     * @throws IllegalArgumentException
     *             if specified number out of bounds
     * @throws OutOfBoundsException
     *             if read bytes out of bounds
     * @since 0.0.0
     */
    public long getLongNumeric(long bitIndex, int bytesNum);

    /**
     * <p>
     * Reads bytes of specified number from specified bit index inclusive,
     * composing them into an numeric value according to the current byte order,
     * if specified number is less than 8, the numeric value will be
     * zero-extended to a long value. The long value returned.
     * </p>
     * 
     * @param bitIndex
     *            specified bit index, in bounds
     * @param bytesNum
     *            specified number of bytes to be read, [1, 8]
     * @return long value zero-extended by read numeric from specified bit index
     * @throws IllegalArgumentException
     *             if specified number out of bounds
     * @throws OutOfBoundsException
     *             if read bytes out of bounds
     * @since 0.0.0
     */
    public long getUnsignedLongNumeric(long bitIndex, int bytesNum);

    /**
     * <p>
     * Reads bits of specified number from specified bit index inclusive, if
     * specified number is less than 32, the numeric value will be sign-extended
     * to an int value. The int value returned.
     * </p>
     * <p>
     * There is no problem about the byte order in this method, it always
     * composing from high to low order (left to right), then extending in
     * front.
     * </p>
     * 
     * @param bitIndex
     *            specified bit index, in bounds
     * @param bitsNum
     *            specified number of bits to be read, [1, 32]
     * @return int value sign-extended by read numeric from specified bit index
     * @throws IllegalArgumentException
     *             if specified number out of bounds
     * @throws OutOfBoundsException
     *             if read bytes out of bounds
     * @since 0.0.0
     */
    public int getNumericBits(long bitIndex, int bitsNum);

    /**
     * <p>
     * Reads bits of specified number from specified bit index inclusive, if
     * specified number is less than 32, the numeric value will be zero-extended
     * to an int value. The int value returned.
     * </p>
     * <p>
     * There is no problem about the byte order in this method, it always
     * composing from high to low order (left to right), then extending in
     * front.
     * </p>
     * 
     * @param bitIndex
     *            specified bit index, in bounds
     * @param bitsNum
     *            specified number of bits to be read, [1, 32]
     * @return int value zero-extended by read numeric from specified bit index
     * @throws IllegalArgumentException
     *             if specified number out of bounds
     * @throws OutOfBoundsException
     *             if read bytes out of bounds
     * @since 0.0.0
     */
    public int getUnsignedNumericBits(long bitIndex, int bitsNum);

    /**
     * <p>
     * Reads bits of specified number from specified bit index inclusive, if
     * specified number is less than 64, the numeric value will be sign-extended
     * to an long value. The long value returned.
     * </p>
     * <p>
     * There is no problem about the byte order in this method, it always
     * composing from high to low order (left to right), then extending in
     * front.
     * </p>
     * 
     * @param bitIndex
     *            specified bit index, in bounds
     * @param bitsNum
     *            specified number of bits to be read, [1, 64]
     * @return long value sign-extended by read numeric from specified bit index
     * @throws IllegalArgumentException
     *             if specified number out of bounds
     * @throws OutOfBoundsException
     *             if read bytes out of bounds
     * @since 0.0.0
     */
    public long getLongNumericBits(long bitIndex, int bitsNum);

    /**
     * <p>
     * Reads bits of specified number from specified bit index inclusive, if
     * specified number is less than 64, the numeric value will be zero-extended
     * to an long value. The long value returned.
     * </p>
     * <p>
     * There is no problem about the byte order in this method, it always
     * composing from high to low order (left to right), then extending in
     * front.
     * </p>
     * 
     * @param bitIndex
     *            specified bit index, in bounds
     * @param bitsNum
     *            specified number of bits to be read, [1, 64]
     * @return long value zero-extended by read numeric from specified bit index
     * @throws IllegalArgumentException
     *             if specified number out of bounds
     * @throws OutOfBoundsException
     *             if read bytes out of bounds
     * @since 0.0.0
     */
    public long getUnsignedLongNumericBits(long bitIndex, int bitsNum);

    /**
     * <p>
     * Reads 4 bytes from specified bit index inclusive, composing them into a
     * float value according to the current byte order. The float value
     * returned.
     * </p>
     * <p>
     * Note floating-point value's byte order is not necessarily equal to
     * numeric value's at same time.
     * </p>
     * 
     * @param bitIndex
     *            specified bit index, in bounds
     * @return float value composed of read bytes from specified bit index
     * @throws OutOfBoundsException
     *             if read bytes out of bounds
     * @since 0.0.0
     */
    public float getFloat(long bitIndex);

    /**
     * <p>
     * Reads 8 bytes from specified bit index inclusive, composing them into a
     * double value according to the current byte order. The double value
     * returned.
     * </p>
     * <p>
     * Note floating-point value's byte order is not necessarily equal to
     * numeric value's at same time.
     * </p>
     * 
     * @param bitIndex
     *            specified bit index, in bounds
     * @return double value composed of read bytes from specified bit index
     * @throws OutOfBoundsException
     *             if read bytes out of bounds
     * @since 0.0.0
     */
    public double getDouble(long bitIndex);

    /**
     * <p>
     * Reads at most 4 bytes from specified bit index inclusive, composing them
     * into an int value with that first byte into the highest byte order,
     * second byte into next byte order and so on--like big-endian byte order.
     * If the specified number of read bytes is less than 4, the rest orders
     * will be filled 0. The int value returned.
     * </p>
     * <p>
     * This method ignores the current byte order.
     * </p>
     * 
     * @param bitIndex
     *            specified bit index, in bounds
     * @param bytesNum
     *            specified number of bytes to be read, [1, 4]
     * @return int value composed of read bytes from specified bit index
     * @throws IllegalArgumentException
     *             if specified number out of bounds
     * @throws OutOfBoundsException
     *             if read bytes out of bounds
     * @since 0.0.0
     */
    public int getBytes(long bitIndex, int bytesNum);

    /**
     * <p>
     * Reads at most 8 bytes from specified bit index inclusive, composing them
     * into a long value with that first byte into the highest byte order,
     * second byte into next byte order and so on--like big-endian byte order.
     * If the specified number of read bytes is less than 8, the rest orders
     * will be filled 0. The long value returned.
     * </p>
     * <p>
     * This method ignores the current byte order.
     * </p>
     * 
     * @param bitIndex
     *            specified bit index, in bounds
     * @param bytesNum
     *            specified number of bytes to be read, [1, 8]
     * @return long value composed of read bytes from specified bit index
     * @throws IllegalArgumentException
     *             if specified number out of bounds
     * @throws OutOfBoundsException
     *             if read bytes out of bounds
     * @since 0.0.0
     */
    public long getLongBytes(long bitIndex, int bytesNum);

    /**
     * <p>
     * Reads at most 32 bits from specified bit index inclusive, composing them
     * into an int value with that first bit into the highest bit order, second
     * bit into next bit order and so on. If the specified number of read bits
     * is less than 32, the rest orders will be filled 0. The int value
     * returned.
     * </p>
     * <p>
     * There is no problem about the byte order in this method.
     * </p>
     * 
     * @param bitIndex
     *            specified bit index, in bounds
     * @param bitsNum
     *            specified number of bits to be read, [1, 32]
     * @return int value composed of read bits from specified bit index
     * @throws IllegalArgumentException
     *             if specified number out of bounds
     * @throws OutOfBoundsException
     *             if read bytes out of bounds
     * @since 0.0.0
     */
    public int getBits(long bitIndex, int bitsNum);

    /**
     * <p>
     * Reads at most 64 bits from specified bit index inclusive, composing them
     * into an long value with that first bit into the highest bit order, second
     * bit into next bit order and so on. If the specified number of read bits
     * is less than 64, the rest orders will be filled 0. The long value
     * returned.
     * </p>
     * <p>
     * There is no problem about the byte order in this method.
     * </p>
     * 
     * @param bitIndex
     *            specified bit index, in bounds
     * @param bitsNum
     *            specified number of bits to be read, [1, 64]
     * @return long value composed of read bits from specified bit index
     * @throws IllegalArgumentException
     *             if specified number out of bounds
     * @throws OutOfBoundsException
     *             if read bytes out of bounds
     * @since 0.0.0
     */
    public long getLongBits(long bitIndex, int bitsNum);
}
