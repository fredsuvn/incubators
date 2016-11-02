package com.cogician.quicker.binary;

import com.cogician.quicker.OutOfBoundsException;

/**
 * <p>
 * An interface to random write primitive value at specified index and bit offset.
 * </p>
 * <p>
 * The index can be index of byte, of int, or of custom length, decided by implementation; the bit offset is the offset
 * from first bit of the value specified by the index in bits, start from 0. The bit offset can not greater than length
 * of the value specified by index.
 * </p>
 * <p>
 * Written bits is always in big-endian.
 * </p>
 * <p>
 * There are two sets of methods, indexes of one being int type, the others being long type. The only different between
 * is that indexes of long type have wider value range.
 * </p>
 * 
 * @author Fred Suvn
 * @version 0.0.0, 2015-09-06 16:47:32
 * @version 0.0.0, 2016-03-14T14:34:21+08:00
 * @since 0.0.0
 */
public interface RandomWrite {

    /**
     * <p>
     * Puts specified bit value at given index, the lowest bit order being only effective, others ignored.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param value
     *            specified bit value
     * @throws OutOfBoundsException
     *             if out of bounds
     * @since 0.0.0
     */
    public void putBit(int index, int value) throws OutOfBoundsException;

    /**
     * <p>
     * Puts specified bit value at given bit offset of given index, the lowest bit order being only effective, others
     * ignored.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param offset
     *            given bit offset in bounds
     * @param value
     *            specified bit value
     * @throws OutOfBoundsException
     *             if out of bounds
     * @since 0.0.0
     */
    public void putBit(int index, int offset, int value) throws OutOfBoundsException;

    /**
     * <p>
     * Puts specified boolean value as bit at given index, true being 1, false being 0.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param value
     *            specified boolean value
     * @throws OutOfBoundsException
     *             if out of bounds
     * @since 0.0.0
     */
    default void putBit(int index, boolean value) throws OutOfBoundsException {
        putBit(index, value ? 1 : 0);
    }

    /**
     * <p>
     * Puts specified boolean value as bit at given bit offset of given index, true being 1, false being 0.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param offset
     *            given bit offset in bounds
     * @param value
     *            specified boolean value
     * @throws OutOfBoundsException
     *             if out of bounds
     * @since 0.0.0
     */
    default void putBit(int index, int offset, boolean value) throws OutOfBoundsException {
        putBit(index, offset, value ? 1 : 0);
    }

    /**
     * <p>
     * Puts specified byte value at given index. The byte value is lowest 1 byte of given value, high orders ignored.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param value
     *            given value
     * @throws OutOfBoundsException
     *             if out of bounds
     * @since 0.0.0
     */
    public void putByte(int index, int value) throws OutOfBoundsException;

    /**
     * <p>
     * Puts specified byte value at given bit offset of given index. The byte value is lowest 1 byte of given value,
     * high orders ignored.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param offset
     *            given bit offset in bounds
     * @param value
     *            given value
     * @throws OutOfBoundsException
     *             if out of bounds
     * @since 0.0.0
     */
    public void putByte(int index, int offset, int value) throws OutOfBoundsException;

    /**
     * <p>
     * Puts specified short value at given index. The short value is lowest 2 bytes of given value, high orders ignored.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param value
     *            given value
     * @throws OutOfBoundsException
     *             if out of bounds
     * @since 0.0.0
     */
    public void putShort(int index, int value) throws OutOfBoundsException;

    /**
     * <p>
     * Puts specified short value at given bit offset of given index. The short value is lowest 2 bytes of given value,
     * high orders ignored.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param offset
     *            given bit offset in bounds
     * @param value
     *            given value
     * @throws OutOfBoundsException
     *             if out of bounds
     * @since 0.0.0
     */
    public void putShort(int index, int offset, int value) throws OutOfBoundsException;

    /**
     * <p>
     * Puts specified char value at given index. The char value is lowest 2 bytes of given value, high orders ignored.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param value
     *            given value
     * @throws OutOfBoundsException
     *             if out of bounds
     * @since 0.0.0
     */
    public void putChar(int index, int value) throws OutOfBoundsException;

    /**
     * <p>
     * Puts specified char value at given bit offset of given index. The char value is lowest 2 bytes of given value,
     * high orders ignored.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param offset
     *            given bit offset in bounds
     * @param value
     *            given value
     * @throws OutOfBoundsException
     *             if out of bounds
     * @since 0.0.0
     */
    public void putChar(int index, int offset, int value) throws OutOfBoundsException;

    /**
     * <p>
     * Puts specified 3-bytes value at given index. The 3-bytes value is lowest 3 bytes of given value, high orders
     * ignored.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param value
     *            given value
     * @throws OutOfBoundsException
     *             if out of bounds
     * @since 0.0.0
     */
    public void putMedium(int index, int value) throws OutOfBoundsException;

    /**
     * <p>
     * Puts specified 3-bytes value at given bit offset of given index. The 3-bytes value is lowest 2 bytes of given
     * value, high orders ignored.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param offset
     *            given bit offset in bounds
     * @param value
     *            given value
     * @throws OutOfBoundsException
     *             if out of bounds
     * @since 0.0.0
     */
    public void putMedium(int index, int offset, int value) throws OutOfBoundsException;

    /**
     * <p>
     * Puts given int value at given index.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param value
     *            given value
     * @throws OutOfBoundsException
     *             if out of bounds
     * @since 0.0.0
     */
    public void putInt(int index, int value) throws OutOfBoundsException;

    /**
     * <p>
     * Puts given int value at given bit offset of given index.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param offset
     *            given bit offset in bounds
     * @param value
     *            given value
     * @throws OutOfBoundsException
     *             if out of bounds
     * @since 0.0.0
     */
    public void putInt(int index, int offset, int value) throws OutOfBoundsException;

    /**
     * <p>
     * Puts given float value at given index.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param value
     *            given value
     * @throws OutOfBoundsException
     *             if out of bounds
     * @since 0.0.0
     */
    default void putFloat(int index, float value) throws OutOfBoundsException {
        putInt(index, Float.floatToRawIntBits(value));
    }

    /**
     * <p>
     * Puts given float value at given bit offset of given index.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param offset
     *            given bit offset in bounds
     * @param value
     *            given value
     * @throws OutOfBoundsException
     *             if out of bounds
     * @since 0.0.0
     */
    default void putFloat(int index, int offset, float value) throws OutOfBoundsException {
        putInt(index, offset, Float.floatToRawIntBits(value));
    }

    /**
     * <p>
     * Puts given long value at given index.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param value
     *            given value
     * @throws OutOfBoundsException
     *             if out of bounds
     * @since 0.0.0
     */
    public void putLong(int index, long value) throws OutOfBoundsException;

    /**
     * <p>
     * Puts given long value at given bit offset of given index.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param offset
     *            given bit offset in bounds
     * @param value
     *            given value
     * @throws OutOfBoundsException
     *             if out of bounds
     * @since 0.0.0
     */
    public void putLong(int index, int offset, long value) throws OutOfBoundsException;

    /**
     * <p>
     * Puts given double value at given index.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param value
     *            given value
     * @throws OutOfBoundsException
     *             if out of bounds
     * @since 0.0.0
     */
    default void putDouble(int index, double value) {
        putLong(index, Double.doubleToRawLongBits(value));
    }

    /**
     * <p>
     * Puts given double value at given bit offset of given index.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param offset
     *            given bit offset in bounds
     * @param value
     *            given value
     * @throws OutOfBoundsException
     *             if out of bounds
     * @since 0.0.0
     */
    default void putDouble(int index, int offset, double value) throws OutOfBoundsException {
        putLong(index, offset, Double.doubleToRawLongBits(value));
    }

    /**
     * <p>
     * Puts given bits of specified number at given index. The bits value is lowest bits of given value, high orders
     * ignored.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param value
     *            given value
     * @param bitsNum
     *            specified bits number, [1, 32]
     * @throws OutOfBoundsException
     *             if out of bounds
     * @since 0.0.0
     */
    public void putBits(int index, int value, int bitsNum) throws OutOfBoundsException;

    /**
     * <p>
     * Puts given bits of specified number at given bit offset of given index. The bits value is lowest bits of given
     * value, high orders ignored.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param offset
     *            given bit offset in bounds
     * @param value
     *            given value
     * @param bitsNum
     *            specified bits number, [1, 32]
     * @throws OutOfBoundsException
     *             if out of bounds
     * @since 0.0.0
     */
    public void putBits(int index, int offset, int value, int bitsNum) throws OutOfBoundsException;

    /**
     * <p>
     * Puts given bits of specified number at given index. The bits value is lowest bits of given value, high orders
     * ignored.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param value
     *            given value
     * @param bitsNum
     *            specified bits number, [1, 64]
     * @throws OutOfBoundsException
     *             if out of bounds
     * @since 0.0.0
     */
    public void putLongBits(int index, long value, int bitsNum) throws OutOfBoundsException;

    /**
     * <p>
     * Puts given bits of specified number at given bit offset of given index. The bits value is lowest bits of given
     * value, high orders ignored.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param offset
     *            given bit offset in bounds
     * @param value
     *            given value
     * @param bitsNum
     *            specified bits number, [1, 64]
     * @throws OutOfBoundsException
     *             if out of bounds
     * @since 0.0.0
     */
    public void putLongBits(int index, int offset, long value, int bitsNum) throws OutOfBoundsException;

    // --------------------------------separator of int and long of index

    /**
     * <p>
     * Puts specified bit value at given index, the lowest bit order being only effective, others ignored.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param value
     *            specified bit value
     * @throws OutOfBoundsException
     *             if out of bounds
     * @since 0.0.0
     */
    public void putBit(long index, int value) throws OutOfBoundsException;

    /**
     * <p>
     * Puts specified bit value at given bit offset of given index, the lowest bit order being only effective, others
     * ignored.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param offset
     *            given bit offset in bounds
     * @param value
     *            specified bit value
     * @throws OutOfBoundsException
     *             if out of bounds
     * @since 0.0.0
     */
    public void putBit(long index, long offset, int value) throws OutOfBoundsException;

    /**
     * <p>
     * Puts specified boolean value as bit at given index, true being 1, false being 0.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param value
     *            specified boolean value
     * @throws OutOfBoundsException
     *             if out of bounds
     * @since 0.0.0
     */
    default void putBit(long index, boolean value) throws OutOfBoundsException {
        putBit(index, value ? 1 : 0);
    }

    /**
     * <p>
     * Puts specified boolean value as bit at given bit offset of given index, true being 1, false being 0.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param offset
     *            given bit offset in bounds
     * @param value
     *            specified boolean value
     * @throws OutOfBoundsException
     *             if out of bounds
     * @since 0.0.0
     */
    default void putBit(long index, long offset, boolean value) throws OutOfBoundsException {
        putBit(index, offset, value ? 1 : 0);
    }

    /**
     * <p>
     * Puts specified byte value at given index. The byte value is lowest 1 byte of given value, high orders ignored.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param value
     *            given value
     * @throws OutOfBoundsException
     *             if out of bounds
     * @since 0.0.0
     */
    public void putByte(long index, int value) throws OutOfBoundsException;

    /**
     * <p>
     * Puts specified byte value at given bit offset of given index. The byte value is lowest 1 byte of given value,
     * high orders ignored.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param offset
     *            given bit offset in bounds
     * @param value
     *            given value
     * @throws OutOfBoundsException
     *             if out of bounds
     * @since 0.0.0
     */
    public void putByte(long index, long offset, int value) throws OutOfBoundsException;

    /**
     * <p>
     * Puts specified short value at given index. The short value is lowest 2 bytes of given value, high orders ignored.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param value
     *            given value
     * @throws OutOfBoundsException
     *             if out of bounds
     * @since 0.0.0
     */
    public void putShort(long index, int value) throws OutOfBoundsException;

    /**
     * <p>
     * Puts specified short value at given bit offset of given index. The short value is lowest 2 bytes of given value,
     * high orders ignored.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param offset
     *            given bit offset in bounds
     * @param value
     *            given value
     * @throws OutOfBoundsException
     *             if out of bounds
     * @since 0.0.0
     */
    public void putShort(long index, long offset, int value) throws OutOfBoundsException;

    /**
     * <p>
     * Puts specified char value at given index. The char value is lowest 2 bytes of given value, high orders ignored.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param value
     *            given value
     * @throws OutOfBoundsException
     *             if out of bounds
     * @since 0.0.0
     */
    public void putChar(long index, int value) throws OutOfBoundsException;

    /**
     * <p>
     * Puts specified char value at given bit offset of given index. The char value is lowest 2 bytes of given value,
     * high orders ignored.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param offset
     *            given bit offset in bounds
     * @param value
     *            given value
     * @throws OutOfBoundsException
     *             if out of bounds
     * @since 0.0.0
     */
    public void putChar(long index, long offset, int value) throws OutOfBoundsException;

    /**
     * <p>
     * Puts specified 3-bytes value at given index. The 3-bytes value is lowest 3 bytes of given value, high orders
     * ignored.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param value
     *            given value
     * @throws OutOfBoundsException
     *             if out of bounds
     * @since 0.0.0
     */
    public void putMedium(long index, int value) throws OutOfBoundsException;

    /**
     * <p>
     * Puts specified 3-bytes value at given bit offset of given index. The 3-bytes value is lowest 2 bytes of given
     * value, high orders ignored.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param offset
     *            given bit offset in bounds
     * @param value
     *            given value
     * @throws OutOfBoundsException
     *             if out of bounds
     * @since 0.0.0
     */
    public void putMedium(long index, long offset, int value) throws OutOfBoundsException;

    /**
     * <p>
     * Puts given int value at given index.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param value
     *            given value
     * @throws OutOfBoundsException
     *             if out of bounds
     * @since 0.0.0
     */
    public void putInt(long index, int value) throws OutOfBoundsException;

    /**
     * <p>
     * Puts given int value at given bit offset of given index.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param offset
     *            given bit offset in bounds
     * @param value
     *            given value
     * @throws OutOfBoundsException
     *             if out of bounds
     * @since 0.0.0
     */
    public void putInt(long index, long offset, int value) throws OutOfBoundsException;

    /**
     * <p>
     * Puts given float value at given index.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param value
     *            given value
     * @throws OutOfBoundsException
     *             if out of bounds
     * @since 0.0.0
     */
    default void putFloat(long index, float value) throws OutOfBoundsException {
        putInt(index, Float.floatToRawIntBits(value));
    }

    /**
     * <p>
     * Puts given float value at given bit offset of given index.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param offset
     *            given bit offset in bounds
     * @param value
     *            given value
     * @throws OutOfBoundsException
     *             if out of bounds
     * @since 0.0.0
     */
    default void putFloat(long index, long offset, float value) throws OutOfBoundsException {
        putInt(index, offset, Float.floatToRawIntBits(value));
    }

    /**
     * <p>
     * Puts given long value at given index.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param value
     *            given value
     * @throws OutOfBoundsException
     *             if out of bounds
     * @since 0.0.0
     */
    public void putLong(long index, long value) throws OutOfBoundsException;

    /**
     * <p>
     * Puts given long value at given bit offset of given index.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param offset
     *            given bit offset in bounds
     * @param value
     *            given value
     * @throws OutOfBoundsException
     *             if out of bounds
     * @since 0.0.0
     */
    public void putLong(long index, long offset, long value) throws OutOfBoundsException;

    /**
     * <p>
     * Puts given double value at given index.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param value
     *            given value
     * @throws OutOfBoundsException
     *             if out of bounds
     * @since 0.0.0
     */
    default void putDouble(long index, double value) {
        putLong(index, Double.doubleToRawLongBits(value));
    }

    /**
     * <p>
     * Puts given double value at given bit offset of given index.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param offset
     *            given bit offset in bounds
     * @param value
     *            given value
     * @throws OutOfBoundsException
     *             if out of bounds
     * @since 0.0.0
     */
    default void putDouble(long index, long offset, double value) throws OutOfBoundsException {
        putLong(index, offset, Double.doubleToRawLongBits(value));
    }

    /**
     * <p>
     * Puts given bits of specified number at given index. The bits value is lowest bits of given value, high orders
     * ignored.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param value
     *            given value
     * @param bitsNum
     *            specified bits number, [1, 32]
     * @throws OutOfBoundsException
     *             if out of bounds
     * @since 0.0.0
     */
    public void putBits(long index, int value, int bitsNum) throws OutOfBoundsException;

    /**
     * <p>
     * Puts given bits of specified number at given bit offset of given index. The bits value is lowest bits of given
     * value, high orders ignored.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param offset
     *            given bit offset in bounds
     * @param value
     *            given value
     * @param bitsNum
     *            specified bits number, [1, 32]
     * @throws OutOfBoundsException
     *             if out of bounds
     * @since 0.0.0
     */
    public void putBits(long index, long offset, int value, int bitsNum) throws OutOfBoundsException;

    /**
     * <p>
     * Puts given bits of specified number at given index. The bits value is lowest bits of given value, high orders
     * ignored.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param value
     *            given value
     * @param bitsNum
     *            specified bits number, [1, 64]
     * @throws OutOfBoundsException
     *             if out of bounds
     * @since 0.0.0
     */
    public void putLongBits(long index, long value, int bitsNum) throws OutOfBoundsException;

    /**
     * <p>
     * Puts given bits of specified number at given bit offset of given index. The bits value is lowest bits of given
     * value, high orders ignored.
     * </p>
     * 
     * @param index
     *            given index in bounds
     * @param offset
     *            given bit offset in bounds
     * @param value
     *            given value
     * @param bitsNum
     *            specified bits number, [1, 64]
     * @throws OutOfBoundsException
     *             if out of bounds
     * @since 0.0.0
     */
    public void putLongBits(long index, long offset, long value, int bitsNum) throws OutOfBoundsException;

}
