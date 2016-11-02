package com.cogician.quicker.binary;

/**
 * <p>
 * An interface to random read primitive value at specified index and bit offset.
 * </p>
 * <p>
 * The index can be index of byte, of int, or of custom length, decided by implementation; the bit offset is the offset
 * from first bit of the value specified by the index in bits, start from 0. The bit offset can not greater than length
 * of the value specified by index.
 * </p>
 * <p>
 * If returned primitive value's effective bits is less than length of returned type, the returned value will signed or
 * unsigned extend in the head. Composing of read bits is always in big-endian.
 * </p>
 * <p>
 * There are two sets of methods, indexes of one being int type, the others being long type. The only different between
 * is that indexes of long type have wider value range.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2015-09-06 15:43:11
 * @version 0.0.0, 2016-03-09T17:26:50+08:00
 * @since 0.0.0
 */
public interface RandomRead {

    /**
     * <p>
     * Gets value at given index as boolean, zero being false, non-zero being true.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @return value at given index as boolean
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    public boolean getBoolean(int index) throws IndexOutOfBoundsException;

    /**
     * <p>
     * Gets next bit value at given index as boolean, 1 being true, 0 being false.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @return next bit value at given index as boolean
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    default boolean getBitAsBoolean(int index) throws IndexOutOfBoundsException {
        return getBit(index) == 1;
    }

    /**
     * <p>
     * Gets next bit value at given index and given bit offset of the index as boolean, 1 being true, 0 being false.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param offset
     *            given bit offset in bounds
     * @return next bit value at given index and given bit offset of the index as boolean
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     *             @throws IllegalArgumentException if bit offset is negative or out of bounds
     * @since 0.0.0
     */
    default boolean getBitAsBoolean(int index, int offset) throws IndexOutOfBoundsException {
        return getBit(index) == 1;
    }

    /**
     * <p>
     * Gets next bit value at given index, 1 or 0.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @return next bit value at given index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    public byte getBit(int index) throws IndexOutOfBoundsException;

    /**
     * <p>
     * Gets next bit value at given bit offset of given index, 1 or 0.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param offset
     *            given bit offset in bounds
     * @return next bit value at given bit offset of given index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    public byte getBit(int index, int offset) throws IndexOutOfBoundsException;

    /**
     * <p>
     * Gets next byte value at given index.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @return next byte value at given index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    public byte getByte(int index) throws IndexOutOfBoundsException;

    /**
     * <p>
     * Gets next byte value at given bit offset of given index.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param offset
     *            given bit offset in bounds
     * @return next byte value at given bit offset of given index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    public byte getByte(int index, int offset) throws IndexOutOfBoundsException;

    /**
     * <p>
     * Gets next unsigned byte value at given index.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @return next unsigned byte value at given index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    default int getUnsignedByte(int index) throws IndexOutOfBoundsException {
        return 0x000000ff & getByte(index);
    }

    /**
     * <p>
     * Gets next unsigned byte value at given bit offset of given index.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param offset
     *            given bit offset in bounds
     * @return next unsigned byte value at given bit offset of given index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    default int getUnsignedByte(int index, int offset) throws IndexOutOfBoundsException {
        return 0x000000ff & getByte(index, offset);
    }

    /**
     * <p>
     * Gets next short value at given index.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @return next short value at given index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    public short getShort(int index) throws IndexOutOfBoundsException;

    /**
     * <p>
     * Gets next short value at given bit offset of given index.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param offset
     *            given bit offset in bounds
     * @return next short value at given bit offset of given index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    public short getShort(int index, int offset) throws IndexOutOfBoundsException;

    /**
     * <p>
     * Gets next unsigned short value at given index.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @return next unsigned short value at given index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    default int getUnsignedShort(int index) throws IndexOutOfBoundsException {
        return 0x0000ffff & getShort(index);
    }

    /**
     * <p>
     * Gets next unsigned short value at given bit offset of given index.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param offset
     *            given bit offset in bounds
     * @return next unsigned short value at given bit offset of given index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    default int getUnsignedShort(int index, int offset) throws IndexOutOfBoundsException {
        return 0x0000ffff & getShort(index, offset);
    }

    /**
     * <p>
     * Gets next char value at given index.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @return next char value at given index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    public char getChar(int index) throws IndexOutOfBoundsException;

    /**
     * <p>
     * Gets next char value at given bit offset of given index.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param offset
     *            given bit offset in bounds
     * @return next char value at given bit offset of given index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    public char getChar(int index, int offset) throws IndexOutOfBoundsException;

    /**
     * <p>
     * Gets next signed 3-bytes-medium value at given index.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @return next signed 3-bytes-medium value at given index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    public int getMedium(int index) throws IndexOutOfBoundsException;

    /**
     * <p>
     * Gets next signed 3-bytes-medium value at given bit offset of given index.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param offset
     *            given bit offset in bounds
     * @return next signed 3-bytes-medium value at given bit offset of given index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    public int getMedium(int index, int offset) throws IndexOutOfBoundsException;

    /**
     * <p>
     * Gets next unsigned 3-bytes-medium value at given index.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @return next unsigned 3-bytes-medium value at given index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    default int getUnsignedMedium(int index) throws IndexOutOfBoundsException {
        return 0x00ffffff & getMedium(index);
    }

    /**
     * <p>
     * Gets next unsigned 3-bytes-medium value at given bit offset of given index.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param offset
     *            given bit offset in bounds
     * @return next unsigned 3-bytes-medium value at given bit offset of given index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    default int getUnsignedMedium(int index, int offset) throws IndexOutOfBoundsException {
        return 0x00ffffff & getMedium(index, offset);
    }

    /**
     * <p>
     * Gets next int value at given index.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @return next int value at given index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    public int getInt(int index) throws IndexOutOfBoundsException;

    /**
     * <p>
     * Gets next int value at given bit offset of given index.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param offset
     *            given bit offset in bounds
     * @return next int value at given bit offset of given index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    public int getInt(int index, int offset) throws IndexOutOfBoundsException;

    /**
     * <p>
     * Gets next unsigned int value at given index.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @return next unsigned int value at given index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    default long getUnsignedInt(int index) throws IndexOutOfBoundsException {
        return 0x00000000ffffffffL & getInt(index);
    }

    /**
     * <p>
     * Gets next unsigned int value at given bit offset of given index.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param offset
     *            given bit offset in bounds
     * @return next unsigned int value at given bit offset of given index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    default long getUnsignedInt(int index, int offset) throws IndexOutOfBoundsException {
        return 0x00000000ffffffffL & getInt(index, offset);
    }

    /**
     * <p>
     * Gets next float value at given index.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @return next float value at given index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    default float getFloat(int index) throws IndexOutOfBoundsException {
        return Float.intBitsToFloat(getInt(index));
    }

    /**
     * <p>
     * Gets next float value at given bit offset of given index.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param offset
     *            given bit offset in bounds
     * @return next float value at given bit offset of given index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    default float getFloat(int index, int offset) throws IndexOutOfBoundsException {
        return Float.intBitsToFloat(getInt(index, offset));
    }

    /**
     * <p>
     * Gets next long value at given index.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @return next long value at given index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    public long getLong(int index) throws IndexOutOfBoundsException;

    /**
     * <p>
     * Gets next long value at given bit offset of given index.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param offset
     *            given bit offset in bounds
     * @return next long value at given bit offset of given index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    public long getLong(int index, int offset) throws IndexOutOfBoundsException;

    /**
     * <p>
     * Gets next double value at given index.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @return next double value at given index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    default double getDouble(int index) throws IndexOutOfBoundsException {
        return Double.longBitsToDouble(getLong(index));
    }

    /**
     * <p>
     * Gets next double value at given bit offset of given index.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param offset
     *            given bit offset in bounds
     * @return next double value at given bit offset of given index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    default double getDouble(int index, int offset) throws IndexOutOfBoundsException {
        return Double.longBitsToDouble(getLong(index, offset));
    }

    /**
     * <p>
     * Gets next bits of specified number at given index, signed-extending as int.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param bitsNum
     *            specified bits number, [1, 32]
     * @return next bits of specified number at given index, signed-extending as int
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    public int getBits(int index, int bitsNum) throws IndexOutOfBoundsException;

    /**
     * <p>
     * Gets next bits of specified number at given bit offset of given index, signed-extending as int.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param offset
     *            given bit offset in bounds
     * @param bitsNum
     *            specified bits number, [1, 32]
     * @return next bits of specified number at given bit offset of given index, signed-extending as int
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    public int getBits(int index, int offset, int bitsNum) throws IndexOutOfBoundsException;

    /**
     * <p>
     * Gets next bits of specified number at given index, unsigned-extending as int.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param bitsNum
     *            specified bits number, [1, 32]
     * @return next bits of specified number at given index, unsigned-extending as int
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    default int getUnsignedBits(int index, int bitsNum) throws IndexOutOfBoundsException {
        int off = 32 - bitsNum;
        return (getBits(index, bitsNum) << off) >>> off;
    }

    /**
     * <p>
     * Gets next bits of specified number at given bit offset of given index, unsigned-extending as int.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param offset
     *            given bit offset in bounds
     * @param bitsNum
     *            specified bits number, [1, 32]
     * @return next bits of specified number at given bit offset of given index, unsigned-extending as int
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    default int getUnsignedBits(int index, int offset, int bitsNum) throws IndexOutOfBoundsException {
        int off = 32 - bitsNum;
        return (getBits(index, offset, bitsNum) << off) >>> off;
    }

    /**
     * <p>
     * Gets next bits of specified number at given index, signed-extending as long.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param bitsNum
     *            specified bits number, [1, 64]
     * @return next bits of specified number at given index, signed-extending as long
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    public long getLongBits(int index, int bitsNum) throws IndexOutOfBoundsException;

    /**
     * <p>
     * Gets next bits of specified number at given bit offset of given index, signed-extending as long.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param offset
     *            given bit offset in bounds
     * @param bitsNum
     *            specified bits number, [1, 64]
     * @return next bits of specified number at given bit offset of given index, signed-extending as long
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    public long getLongBits(int index, int offset, int bitsNum) throws IndexOutOfBoundsException;

    /**
     * <p>
     * Gets next bits of specified number at given index, unsigned-extending as long.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param bitsNum
     *            specified bits number, [1, 64]
     * @return next bits of specified number at given index, unsigned-extending as long
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    default long getUnsignedLongBits(int index, int bitsNum) throws IndexOutOfBoundsException {
        int off = 64 - bitsNum;
        return (getLongBits(index, bitsNum) << off) >>> off;
    }

    /**
     * <p>
     * Gets next bits of specified number at given bit offset of given index, unsigned-extending as long.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param offset
     *            given bit offset in bounds
     * @param bitsNum
     *            specified bits number, [1, 64]
     * @return next bits of specified number at given bit offset of given index, unsigned-extending as long
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    default long getUnsignedLongBits(int index, int offset, int bitsNum) throws IndexOutOfBoundsException {
        int off = 64 - bitsNum;
        return (getLongBits(index, offset, bitsNum) << off) >>> off;
    }

    // --------------------------------separator of int and long of index

    /**
     * <p>
     * Gets value at given index as boolean, zero being false, non-zero being true.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @return value at given index as boolean
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    public boolean getBoolean(long index) throws IndexOutOfBoundsException;

    /**
     * <p>
     * Gets next bit value at given index as boolean, 1 being true, 0 being false.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @return next bit value at given index as boolean
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    default boolean getBitAsBoolean(long index) throws IndexOutOfBoundsException {
        return getBit(index) == 1;
    }

    /**
     * <p>
     * Gets next bit value at given index and given bit offset of the index as boolean, 1 being true, 0 being false.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param offset
     *            given bit offset in bounds
     * @return next bit value at given index and given bit offset of the index as boolean
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    default boolean getBitAsBoolean(long index, long offset) throws IndexOutOfBoundsException {
        return getBit(index) == 1;
    }

    /**
     * <p>
     * Gets next bit value at given index, 1 or 0.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @return next bit value at given index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    public byte getBit(long index) throws IndexOutOfBoundsException;

    /**
     * <p>
     * Gets next bit value at given bit offset of given index, 1 or 0.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param offset
     *            given bit offset in bounds
     * @return next bit value at given bit offset of given index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    public byte getBit(long index, long offset) throws IndexOutOfBoundsException;

    /**
     * <p>
     * Gets next byte value at given index.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @return next byte value at given index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    public byte getByte(long index) throws IndexOutOfBoundsException;

    /**
     * <p>
     * Gets next byte value at given bit offset of given index.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param offset
     *            given bit offset in bounds
     * @return next byte value at given bit offset of given index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    public byte getByte(long index, long offset) throws IndexOutOfBoundsException;

    /**
     * <p>
     * Gets next unsigned byte value at given index.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @return next unsigned byte value at given index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    default int getUnsignedByte(long index) throws IndexOutOfBoundsException {
        return 0x000000ff & getByte(index);
    }

    /**
     * <p>
     * Gets next unsigned byte value at given bit offset of given index.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param offset
     *            given bit offset in bounds
     * @return next unsigned byte value at given bit offset of given index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    default int getUnsignedByte(long index, long offset) throws IndexOutOfBoundsException {
        return 0x000000ff & getByte(index, offset);
    }

    /**
     * <p>
     * Gets next short value at given index.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @return next short value at given index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    public short getShort(long index) throws IndexOutOfBoundsException;

    /**
     * <p>
     * Gets next short value at given bit offset of given index.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param offset
     *            given bit offset in bounds
     * @return next short value at given bit offset of given index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    public short getShort(long index, long offset) throws IndexOutOfBoundsException;

    /**
     * <p>
     * Gets next unsigned short value at given index.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @return next unsigned short value at given index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    default int getUnsignedShort(long index) throws IndexOutOfBoundsException {
        return 0x0000ffff & getShort(index);
    }

    /**
     * <p>
     * Gets next unsigned short value at given bit offset of given index.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param offset
     *            given bit offset in bounds
     * @return next unsigned short value at given bit offset of given index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    default int getUnsignedShort(long index, long offset) throws IndexOutOfBoundsException {
        return 0x0000ffff & getShort(index, offset);
    }

    /**
     * <p>
     * Gets next char value at given index.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @return next char value at given index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    public char getChar(long index) throws IndexOutOfBoundsException;

    /**
     * <p>
     * Gets next char value at given bit offset of given index.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param offset
     *            given bit offset in bounds
     * @return next char value at given bit offset of given index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    public char getChar(long index, long offset) throws IndexOutOfBoundsException;

    /**
     * <p>
     * Gets next signed 3-bytes-medium value at given index.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @return next signed 3-bytes-medium value at given index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    public int getMedium(long index) throws IndexOutOfBoundsException;

    /**
     * <p>
     * Gets next signed 3-bytes-medium value at given bit offset of given index.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param offset
     *            given bit offset in bounds
     * @return next signed 3-bytes-medium value at given bit offset of given index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    public int getMedium(long index, long offset) throws IndexOutOfBoundsException;

    /**
     * <p>
     * Gets next unsigned 3-bytes-medium value at given index.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @return next unsigned 3-bytes-medium value at given index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    default int getUnsignedMedium(long index) throws IndexOutOfBoundsException {
        return 0x00ffffff & getMedium(index);
    }

    /**
     * <p>
     * Gets next unsigned 3-bytes-medium value at given bit offset of given index.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param offset
     *            given bit offset in bounds
     * @return next unsigned 3-bytes-medium value at given bit offset of given index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    default int getUnsignedMedium(long index, long offset) throws IndexOutOfBoundsException {
        return 0x00ffffff & getMedium(index, offset);
    }

    /**
     * <p>
     * Gets next int value at given index.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @return next int value at given index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    public int getInt(long index) throws IndexOutOfBoundsException;

    /**
     * <p>
     * Gets next int value at given bit offset of given index.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param offset
     *            given bit offset in bounds
     * @return next int value at given bit offset of given index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    public int getInt(long index, long offset) throws IndexOutOfBoundsException;

    /**
     * <p>
     * Gets next unsigned int value at given index.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @return next unsigned int value at given index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    default long getUnsignedInt(long index) throws IndexOutOfBoundsException {
        return 0x00000000ffffffffL & getInt(index);
    }

    /**
     * <p>
     * Gets next unsigned int value at given bit offset of given index.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param offset
     *            given bit offset in bounds
     * @return next unsigned int value at given bit offset of given index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    default long getUnsignedInt(long index, long offset) throws IndexOutOfBoundsException {
        return 0x00000000ffffffffL & getInt(index, offset);
    }

    /**
     * <p>
     * Gets next float value at given index.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @return next float value at given index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    default float getFloat(long index) throws IndexOutOfBoundsException {
        return Float.intBitsToFloat(getInt(index));
    }

    /**
     * <p>
     * Gets next float value at given bit offset of given index.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param offset
     *            given bit offset in bounds
     * @return next float value at given bit offset of given index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    default float getFloat(long index, long offset) throws IndexOutOfBoundsException {
        return Float.intBitsToFloat(getInt(index, offset));
    }

    /**
     * <p>
     * Gets next long value at given index.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @return next long value at given index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    public long getLong(long index) throws IndexOutOfBoundsException;

    /**
     * <p>
     * Gets next long value at given bit offset of given index.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param offset
     *            given bit offset in bounds
     * @return next long value at given bit offset of given index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    public long getLong(long index, long offset) throws IndexOutOfBoundsException;

    /**
     * <p>
     * Gets next double value at given index.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @return next double value at given index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    default double getDouble(long index) throws IndexOutOfBoundsException {
        return Double.longBitsToDouble(getLong(index));
    }

    /**
     * <p>
     * Gets next double value at given bit offset of given index.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param offset
     *            given bit offset in bounds
     * @return next double value at given bit offset of given index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    default double getDouble(long index, long offset) throws IndexOutOfBoundsException {
        return Double.longBitsToDouble(getLong(index, offset));
    }

    /**
     * <p>
     * Gets next bits of specified number at given index, signed-extending as int.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param bitsNum
     *            specified bits number, [1, 32]
     * @return next bits of specified number at given index, signed-extending as int
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    public int getBits(long index, int bitsNum) throws IndexOutOfBoundsException;

    /**
     * <p>
     * Gets next bits of specified number at given bit offset of given index, signed-extending as int.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param offset
     *            given bit offset in bounds
     * @param bitsNum
     *            specified bits number, [1, 32]
     * @return next bits of specified number at given bit offset of given index, signed-extending as int
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    public int getBits(long index, long offset, int bitsNum) throws IndexOutOfBoundsException;

    /**
     * <p>
     * Gets next bits of specified number at given index, unsigned-extending as int.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param bitsNum
     *            specified bits number, [1, 32]
     * @return next bits of specified number at given index, unsigned-extending as int
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    default int getUnsignedBits(long index, int bitsNum) throws IndexOutOfBoundsException {
        int off = 32 - bitsNum;
        return (getBits(index, bitsNum) << off) >>> off;
    }

    /**
     * <p>
     * Gets next bits of specified number at given bit offset of given index, unsigned-extending as int.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param offset
     *            given bit offset in bounds
     * @param bitsNum
     *            specified bits number, [1, 32]
     * @return next bits of specified number at given bit offset of given index, unsigned-extending as int
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    default int getUnsignedBits(long index, long offset, int bitsNum) throws IndexOutOfBoundsException {
        int off = 32 - bitsNum;
        return (getBits(index, offset, bitsNum) << off) >>> off;
    }

    /**
     * <p>
     * Gets next bits of specified number at given index, signed-extending as long.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param bitsNum
     *            specified bits number, [1, 64]
     * @return next bits of specified number at given index, signed-extending as long
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    public long getLongBits(long index, int bitsNum) throws IndexOutOfBoundsException;

    /**
     * <p>
     * Gets next bits of specified number at given bit offset of given index, signed-extending as long.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param offset
     *            given bit offset in bounds
     * @param bitsNum
     *            specified bits number, [1, 64]
     * @return next bits of specified number at given bit offset of given index, signed-extending as long
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    public long getLongBits(long index, long offset, int bitsNum) throws IndexOutOfBoundsException;

    /**
     * <p>
     * Gets next bits of specified number at given index, unsigned-extending as long.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param bitsNum
     *            specified bits number, [1, 64]
     * @return next bits of specified number at given index, unsigned-extending as long
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    default long getUnsignedLongBits(long index, int bitsNum) throws IndexOutOfBoundsException {
        int off = 64 - bitsNum;
        return (getLongBits(index, bitsNum) << off) >>> off;
    }

    /**
     * <p>
     * Gets next bits of specified number at given bit offset of given index, unsigned-extending as long.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param offset
     *            given bit offset in bounds
     * @param bitsNum
     *            specified bits number, [1, 64]
     * @return next bits of specified number at given bit offset of given index, unsigned-extending as long
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    default long getUnsignedLongBits(long index, long offset, int bitsNum) throws IndexOutOfBoundsException {
        int off = 64 - bitsNum;
        return (getLongBits(index, offset, bitsNum) << off) >>> off;
    }
}
