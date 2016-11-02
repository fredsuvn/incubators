package com.cogician.quicker.binary;

/**
 * <p>
 * Base binary access, used to implement underline read/writ operation. All read/write operation uses big-endian.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-08-29T16:31:06+08:00
 * @since 0.0.0, 2016-08-29T16:31:06+08:00
 */
public interface BaseReadWrite {

    /**
     * <p>
     * Gets byte at specified index.
     * </p>
     * 
     * @param index
     *            specified index
     * @return byte at specified index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @throws BinaryException
     *             if any problem occurs when _getting
     * @since 0.0.0
     */
    public byte _getByte(long index) throws IndexOutOfBoundsException, BinaryException;

    /**
     * <p>
     * Gets 2-bytes short at specified index in big-endian.
     * </p>
     * 
     * @param index
     *            specified index
     * @return short at specified index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds or it leads to read out of bounds
     * @throws BinaryException
     *             if any problem occurs when _getting
     * @since 0.0.0
     */
    public short _getShort(long index) throws IndexOutOfBoundsException, BinaryException;

    /**
     * <p>
     * Gets 2-bytes char at specified index in big-endian.
     * </p>
     * 
     * @param index
     *            specified index
     * @return char at specified index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds or it leads to read out of bounds
     * @throws BinaryException
     *             if any problem occurs when _getting
     * @since 0.0.0
     */
    public char _getChar(long index) throws IndexOutOfBoundsException, BinaryException;

    /**
     * <p>
     * Gets 3-bytes medium at specified index in big-endian.
     * </p>
     * 
     * @param index
     *            specified index
     * @return medium at specified index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds or it leads to read out of bounds
     * @throws BinaryException
     *             if any problem occurs when _getting
     * @since 0.0.0
     */
    public int _getMedium(long index) throws IndexOutOfBoundsException, BinaryException;

    /**
     * <p>
     * Gets 4-bytes int at specified index in big-endian.
     * </p>
     * 
     * @param index
     *            specified index
     * @return int at specified index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds or it leads to read out of bounds
     * @throws BinaryException
     *             if any problem occurs when _getting
     * @since 0.0.0
     */
    public int _getInt(long index) throws IndexOutOfBoundsException, BinaryException;

    /**
     * <p>
     * Gets 8-bytes long at specified index in big-endian.
     * </p>
     * 
     * @param index
     *            specified index
     * @return long at specified index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds or it leads to read out of bounds
     * @throws BinaryException
     *             if any problem occurs when _getting
     * @since 0.0.0
     */
    public long _getLong(long index) throws IndexOutOfBoundsException, BinaryException;

    /**
     * <p>
     * Gets 4-bytes float at specified index in big-endian.
     * </p>
     * 
     * @param index
     *            specified index
     * @return float at specified index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds or it leads to read out of bounds
     * @throws BinaryException
     *             if any problem occurs when _getting
     * @since 0.0.0
     */
    default float _getFloat(long index) throws IndexOutOfBoundsException, BinaryException {
        return Float.intBitsToFloat(_getInt(index));
    }

    /**
     * <p>
     * Gets 8-bytes double at specified index in big-endian.
     * </p>
     * 
     * @param index
     *            specified index
     * @return double at specified index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds or it leads to read out of bounds
     * @throws BinaryException
     *             if any problem occurs when _getting
     * @since 0.0.0
     */
    default double _getDouble(long index) throws IndexOutOfBoundsException, BinaryException {
        return Double.longBitsToDouble(_getLong(index));
    }

    /**
     * <p>
     * Writes low-byte of given value at specified index in big-endian.
     * </p>
     * 
     * @param index
     *            specified index
     * @param value
     *            given value
     * @throws IndexOutOfBoundsException
     *             if specified index out of bounds or it leads to write out of bounds
     * @throws BinaryException
     *             if any problem occurs when putting
     * @since 0.0.0
     */
    public void _setByte(long index, int value) throws IndexOutOfBoundsException, BinaryException;

    /**
     * <p>
     * Writes low-2-bytes of given value at specified index in big-endian.
     * </p>
     * 
     * @param index
     *            specified index
     * @param value
     *            given value
     * @throws IndexOutOfBoundsException
     *             if specified index out of bounds or it leads to write out of bounds
     * @throws BinaryException
     *             if any problem occurs when putting
     * @since 0.0.0
     */
    public void _setShort(long index, int value) throws IndexOutOfBoundsException, BinaryException;

    /**
     * <p>
     * Writes low-2-bytes of given value at specified index in big-endian.
     * </p>
     * 
     * @param index
     *            specified index
     * @param value
     *            given value
     * @throws IndexOutOfBoundsException
     *             if specified index out of bounds or it leads to write out of bounds
     * @throws BinaryException
     *             if any problem occurs when putting
     * @since 0.0.0
     */
    public void _setChar(long index, int value) throws IndexOutOfBoundsException, BinaryException;

    /**
     * <p>
     * Writes low-3-bytes of given value at specified index in big-endian.
     * </p>
     * 
     * @param index
     *            specified index
     * @param value
     *            given value
     * @throws IndexOutOfBoundsException
     *             if specified index out of bounds or it leads to write out of bounds
     * @throws BinaryException
     *             if any problem occurs when putting
     * @since 0.0.0
     */
    public void _setMedium(long index, int value) throws IndexOutOfBoundsException, BinaryException;

    /**
     * <p>
     * Writes 4-bytes of given value at specified index in big-endian.
     * </p>
     * 
     * @param index
     *            specified index
     * @param value
     *            given value
     * @throws IndexOutOfBoundsException
     *             if specified index out of bounds or it leads to write out of bounds
     * @throws BinaryException
     *             if any problem occurs when putting
     * @since 0.0.0
     */
    public void _setInt(long index, int value) throws IndexOutOfBoundsException, BinaryException;

    /**
     * <p>
     * Writes 8-bytes of given value at specified index in big-endian.
     * </p>
     * 
     * @param index
     *            specified index
     * @param value
     *            given value
     * @throws IndexOutOfBoundsException
     *             if specified index out of bounds or it leads to write out of bounds
     * @throws BinaryException
     *             if any problem occurs when putting
     * @since 0.0.0
     */
    public void _setLong(long index, long value) throws IndexOutOfBoundsException, BinaryException;

    /**
     * <p>
     * Writes 4-bytes of given value at specified index in big-endian.
     * </p>
     * 
     * @param index
     *            specified index
     * @param value
     *            given value
     * @throws IndexOutOfBoundsException
     *             if specified index out of bounds or it leads to write out of bounds
     * @throws BinaryException
     *             if any problem occurs when putting
     * @since 0.0.0
     */
    default void _setFloat(long index, float value) throws IndexOutOfBoundsException, BinaryException {
        _setInt(index, Float.floatToRawIntBits(value));
    }

    /**
     * <p>
     * Writes 8-bytes of given value at specified index in big-endian.
     * </p>
     * 
     * @param index
     *            specified index
     * @param value
     *            given value
     * @throws IndexOutOfBoundsException
     *             if specified index out of bounds or it leads to write out of bounds
     * @throws BinaryException
     *             if any problem occurs when putting
     * @since 0.0.0
     */
    default void _setDouble(long index, double value) throws IndexOutOfBoundsException, BinaryException {
        _setLong(index, Double.doubleToRawLongBits(value));
    }
}
