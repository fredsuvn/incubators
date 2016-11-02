package com.cogician.quicker.binary;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;

import com.cogician.quicker.OutOfBoundsException;

/**
 * <p>
 * This interface provides methods that put bits into specified buffer, the bits
 * provided from instance.
 * </p>
 * 
 * @author Fred Suvn
 * @version 0.0.0, 2015-12-09 14:28:19
 * @since 0.0.0
 */
public interface BitsToBuffer {

    /**
     * <p>
     * Puts bits into specified byte buffer from specified bit offset of
     * specified start index, each element of buffer occupied 8 bits. It will
     * stop putting until reaches the end of buffer, and the actual number of
     * bit put will be returned.
     * </p>
     * 
     * @param dest
     *            specified byte buffer, not null
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
    public long toByteBuffer(ByteBuffer dest, int startIndex, int bitOffset);

    /**
     * <p>
     * Puts bits into specified short buffer from specified bit offset of
     * specified start index, each element of buffer occupied 16 bits. It will
     * stop putting until reaches the end of buffer, and the actual number of
     * bit put will be returned.
     * </p>
     * 
     * @param dest
     *            specified short buffer, not null
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
    public long toShortBuffer(ShortBuffer dest, int startIndex, int bitOffset);

    /**
     * <p>
     * Puts bits into specified char buffer from specified bit offset of
     * specified start index, each element of buffer occupied 16 bits. It will
     * stop putting until reaches the end of buffer, and the actual number of
     * bit put will be returned.
     * </p>
     * 
     * @param dest
     *            specified char buffer, not null
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
    public long toCharBuffer(CharBuffer dest, int startIndex, int bitOffset);

    /**
     * <p>
     * Puts bits into specified int buffer from specified bit offset of
     * specified start index, each element of buffer occupied 32 bits. It will
     * stop putting until reaches the end of buffer, and the actual number of
     * bit put will be returned.
     * </p>
     * 
     * @param dest
     *            specified int buffer, not null
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
    public long toIntBuffer(IntBuffer dest, int startIndex, int bitOffset);

    /**
     * <p>
     * Puts bits into specified float buffer from specified bit offset of
     * specified start index, each element of buffer occupied 32 bits. It will
     * stop putting until reaches the end of buffer, and the actual number of
     * bit put will be returned.
     * </p>
     * 
     * @param dest
     *            specified float buffer, not null
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
    public long toFloatBuffer(FloatBuffer dest, int startIndex, int bitOffset);

    /**
     * <p>
     * Puts bits into specified long buffer from specified bit offset of
     * specified start index, each element of buffer occupied 64 bits. It will
     * stop putting until reaches the end of buffer, and the actual number of
     * bit put will be returned.
     * </p>
     * 
     * @param dest
     *            specified long buffer, not null
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
    public long toLongBuffer(LongBuffer dest, int startIndex, int bitOffset);

    /**
     * <p>
     * Puts bits into specified double buffer from specified bit offset of
     * specified start index, each element of buffer occupied 64 bits. It will
     * stop putting until reaches the end of buffer, and the actual number of
     * bit put will be returned.
     * </p>
     * 
     * @param dest
     *            specified double buffer, not null
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
    public long toDoubleBuffer(DoubleBuffer dest, int startIndex, int bitOffset);

    /**
     * <p>
     * Converts bits into a new byte buffer then returns the buffer, each
     * element of buffer occupied 8 bits. If size of instance is not multiple of
     * 8-bits, the last bits of buffer unfilled will be filled 0.
     * </p>
     *
     * @return converted byte buffer, not null
     * @since 0.0.0
     */
    public ByteBuffer toByteBuffer();

    /**
     * <p>
     * Converts bits into a new short buffer then returns the buffer, each
     * element of buffer occupied 16 bits. If size of instance is not multiple
     * of 16-bits, the last bits of buffer unfilled will be filled 0.
     * </p>
     *
     * @return converted short buffer, not null
     * @since 0.0.0
     */
    public ShortBuffer toShortBuffer();

    /**
     * <p>
     * Converts bits into a new char buffer then returns the buffer, each
     * element of buffer occupied 16 bits. If size of instance is not multiple
     * of 16-bits, the last bits of buffer unfilled will be filled 0.
     * </p>
     *
     * @return converted char buffer, not null
     * @since 0.0.0
     */
    public CharBuffer toCharBuffer();

    /**
     * <p>
     * Converts bits into a new int buffer then returns the buffer, each element
     * of buffer occupied 32 bits. If size of instance is not multiple of
     * 32-bits, the last bits of buffer unfilled will be filled 0.
     * </p>
     *
     * @return converted int buffer, not null
     * @since 0.0.0
     */
    public IntBuffer toIntBuffer();

    /**
     * <p>
     * Converts bits into a new float buffer then returns the buffer, each
     * element of buffer occupied 32 bits. If size of instance is not multiple
     * of 32-bits, the last bits of buffer unfilled will be filled 0.
     * </p>
     *
     * @return converted float buffer, not null
     * @since 0.0.0
     */
    public FloatBuffer toFloatBuffer();

    /**
     * <p>
     * Converts bits into a new long buffer then returns the buffer, each
     * element of buffer occupied 64 bits. If size of instance is not multiple
     * of 64-bits, the last bits of buffer unfilled will be filled 0.
     * </p>
     *
     * @return converted long buffer, not null
     * @since 0.0.0
     */
    public LongBuffer toLongBuffer();

    /**
     * <p>
     * Converts bits into a new double buffer then returns the buffer, each
     * element of buffer occupied 64 bits. If size of instance is not multiple
     * of 64-bits, the last bits of buffer unfilled will be filled 0.
     * </p>
     *
     * @return converted double buffer, not null
     * @since 0.0.0
     */
    public DoubleBuffer toDoubleBuffer();
}
