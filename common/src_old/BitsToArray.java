package com.cogician.quicker.binary;

import com.cogician.quicker.OutOfBoundsException;

/**
 * <p>
 * This interface provides methods that put bits into specified primitive array,
 * the bits provided from instance.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2015-05-20 09:16:41
 * @since 0.0.0
 */
public interface BitsToArray {

    /**
     * <p>
     * Puts bits into specified boolean array from specified start index, each
     * element of array occupied 1 bit. It will stop putting until reaches the
     * end of array, and the actual number of bit put will be returned.
     * </p>
     * 
     * @param dest
     *            specified boolean array, not null
     * @param startIndex
     *            specified start index in bounds
     * @return actual actual number of bit put
     * @throws NullPointerException
     *             if dest is null
     * @throws OutOfBoundsException
     *             if startIndex out of bounds
     * @since 0.0.0
     */
    public int toBooleanArray(boolean[] dest, int startIndex);

    /**
     * <p>
     * Puts bits into specified byte array from specified bit offset of
     * specified start index, each element of array occupied 8 bits. It will
     * stop putting until reaches the end of array, and the actual number of bit
     * put will be returned.
     * </p>
     * 
     * @param dest
     *            specified byte array, not null
     * @param startIndex
     *            specified start index in bounds
     * @param bitOffset
     *            specified bit offset of startIndex, [0, 7]
     * @return actual actual number of bit put
     * @throws NullPointerException
     *             if dest is null
     * @throws OutOfBoundsException
     *             if startIndex and/or bitOffset out of bounds
     * @since 0.0.0
     */
    public long toByteArray(byte[] dest, int startIndex, int bitOffset);

    /**
     * <p>
     * Puts bits into specified short array from specified bit offset of
     * specified start index, each element of array occupied 16 bits. It will
     * stop putting until reaches the end of array, and the actual number of bit
     * put will be returned.
     * </p>
     * 
     * @param dest
     *            specified short array, not null
     * @param startIndex
     *            specified start index in bounds
     * @param bitOffset
     *            specified bit offset of startIndex, [0, 15]
     * @return actual actual number of bit put
     * @throws NullPointerException
     *             if dest is null
     * @throws OutOfBoundsException
     *             if startIndex and/or bitOffset out of bounds
     * @since 0.0.0
     */
    public long toShortArray(short[] dest, int startIndex, int bitOffset);

    /**
     * <p>
     * Puts bits into specified char array from specified bit offset of
     * specified start index, each element of array occupied 16 bits. It will
     * stop putting until reaches the end of array, and the actual number of bit
     * put will be returned.
     * </p>
     * 
     * @param dest
     *            specified char array, not null
     * @param startIndex
     *            specified start index in bounds
     * @param bitOffset
     *            specified bit offset of startIndex, [0, 15]
     * @return actual actual number of bit put
     * @throws NullPointerException
     *             if dest is null
     * @throws OutOfBoundsException
     *             if startIndex and/or bitOffset out of bounds
     * @since 0.0.0
     */
    public long toCharArray(char[] dest, int startIndex, int bitOffset);

    /**
     * <p>
     * Puts bits into specified int array from specified bit offset of specified
     * start index, each element of array occupied 32 bits. It will stop putting
     * until reaches the end of array, and the actual number of bit put will be
     * returned.
     * </p>
     * 
     * @param dest
     *            specified int array, not null
     * @param startIndex
     *            specified start index in bounds
     * @param bitOffset
     *            specified bit offset of startIndex, [0, 31]
     * @return actual actual number of bit put
     * @throws NullPointerException
     *             if dest is null
     * @throws OutOfBoundsException
     *             if startIndex and/or bitOffset out of bounds
     * @since 0.0.0
     */
    public long toIntArray(int[] dest, int startIndex, int bitOffset);

    /**
     * <p>
     * Puts bits into specified float array from specified bit offset of
     * specified start index, each element of array occupied 32 bits. It will
     * stop putting until reaches the end of array, and the actual number of bit
     * put will be returned.
     * </p>
     * 
     * @param dest
     *            specified float array, not null
     * @param startIndex
     *            specified start index in bounds
     * @param bitOffset
     *            specified bit offset of startIndex, [0, 31]
     * @return actual actual number of bit put
     * @throws NullPointerException
     *             if dest is null
     * @throws OutOfBoundsException
     *             if startIndex and/or bitOffset out of bounds
     * @since 0.0.0
     */
    public long toFloatArray(float[] dest, int startIndex, int bitOffset);

    /**
     * <p>
     * Puts bits into specified long array from specified bit offset of
     * specified start index, each element of array occupied 64 bits. It will
     * stop putting until reaches the end of array, and the actual number of bit
     * put will be returned.
     * </p>
     * 
     * @param dest
     *            specified long array, not null
     * @param startIndex
     *            specified start index in bounds
     * @param bitOffset
     *            specified bit offset of startIndex, [0, 63]
     * @return actual actual number of bit put
     * @throws NullPointerException
     *             if dest is null
     * @throws OutOfBoundsException
     *             if startIndex and/or bitOffset out of bounds
     * @since 0.0.0
     */
    public long toLongArray(long[] dest, int startIndex, int bitOffset);

    /**
     * <p>
     * Puts bits into specified double array from specified bit offset of
     * specified start index, each element of array occupied 64 bits. It will
     * stop putting until reaches the end of array, and the actual number of bit
     * put will be returned.
     * </p>
     * 
     * @param dest
     *            specified double array, not null
     * @param startIndex
     *            specified start index in bounds
     * @param bitOffset
     *            specified bit offset of startIndex, [0, 63]
     * @return actual actual number of bit put
     * @throws NullPointerException
     *             if dest is null
     * @throws OutOfBoundsException
     *             if startIndex and/or bitOffset out of bounds
     * @since 0.0.0
     */
    public long toDoubleArray(double[] dest, int startIndex, int bitOffset);

    /**
     * <p>
     * Converts into a boolean array, each element occupied 1 bit.
     * </p>
     *
     * @return converted boolean array, not null
     * @since 0.0.0
     */
    public boolean[] toBooleanArray();

    /**
     * <p>
     * Converts bits into a new byte array then returns the array, each element
     * of array occupied 8 bits. If size of instance is not multiple of 8-bits,
     * the last bits of array unfilled will be filled 0.
     * </p>
     *
     * @return converted byte array, not null
     * @since 0.0.0
     */
    public byte[] toByteArray();

    /**
     * <p>
     * Converts bits into a new short array then returns the array, each element
     * of array occupied 16 bits. If size of instance is not multiple of
     * 16-bits, the last bits of array unfilled will be filled 0.
     * </p>
     *
     * @return converted short array, not null
     * @since 0.0.0
     */
    public short[] toShortArray();

    /**
     * <p>
     * Converts bits into a new char array then returns the array, each element
     * of array occupied 16 bits. If size of instance is not multiple of
     * 16-bits, the last bits of array unfilled will be filled 0.
     * </p>
     *
     * @return converted char array, not null
     * @since 0.0.0
     */
    public char[] toCharArray();

    /**
     * <p>
     * Converts bits into a new int array then returns the array, each element
     * of array occupied 32 bits. If size of instance is not multiple of
     * 32-bits, the last bits of array unfilled will be filled 0.
     * </p>
     *
     * @return converted int array, not null
     * @since 0.0.0
     */
    public int[] toIntArray();

    /**
     * <p>
     * Converts bits into a new float array then returns the array, each element
     * of array occupied 32 bits. If size of instance is not multiple of
     * 32-bits, the last bits of array unfilled will be filled 0.
     * </p>
     *
     * @return converted float array, not null
     * @since 0.0.0
     */
    public float[] toFloatArray();

    /**
     * <p>
     * Converts bits into a new long array then returns the array, each element
     * of array occupied 64 bits. If size of instance is not multiple of
     * 64-bits, the last bits of array unfilled will be filled 0.
     * </p>
     *
     * @return converted long array, not null
     * @since 0.0.0
     */
    public long[] toLongArray();

    /**
     * <p>
     * Converts bits into a new double array then returns the array, each
     * element of array occupied 64 bits. If size of instance is not multiple of
     * 64-bits, the last bits of array unfilled will be filled 0.
     * </p>
     *
     * @return converted double array, not null
     * @since 0.0.0
     */
    public double[] toDoubleArray();
}
