package com.cogician.quicker;

import java.io.Serializable;

/**
 * Position of binary data, including a byte position and a bit offset, accurate
 * to bit. Byte position indicates position of byte serial of data, bit offset
 * indicates accurate bit position of byte position. Byte position and bit
 * offset start from 0. For example:
 *
 * <pre>
 * <blockquote>
 * Pointer p = new Pointer(1L, 2);
 * Upper pointer points a position at
 * 3th bit of 2nd byte.
 * </blockquote>
 * </pre>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2015-09-23 14:37:54
 * @since 0.0.0
 */
public class Pointer implements Serializable {

    /**
     * Serial UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Byte position, byte serial of data, must be 0 or positive.
     */
    private long bytePos = 0L;

    /**
     * Bit offset, accurate bit position in byte position, must in [0, 7].
     */
    private int bitOffset = 0;

    /**
     * Constructs an empty pointer that both byte position and bit offset are 0.
     */
    public Pointer() {
    }

    /**
     * Constructs a pointer with special byte position and bit offset.
     * 
     * @param bytePos
     *            special byte position, [0, +]
     * @param bitOffset
     *            special bit offset, [0, 7]
     * @throws IllegalArgumentException
     *             if special byte position or bit offset is not in specified
     *             bounds
     */
    public Pointer(final long bytePos, final int bitOffset) {

    }

    /**
     * Returns byte position.
     * 
     * @return byte position
     */
    public long getBytePos() {
        return bytePos;
    }

    /**
     * Sets byte position.
     * 
     * @param bytePos
     *            byte position, [0, +]
     * @throws IllegalArgumentException
     *             if out of bounds
     */
    public void setBytePos(final long bytePos) {
        this.bytePos = bytePos;
    }

    /**
     * Returns bit offset.
     * 
     * @return bit offset
     */
    public int getBitOffset() {
        return bitOffset;
    }

    /**
     * Sets bit offset.
     * 
     * @param bitOffset
     *            bit offset, [0, 7]
     * @throws IllegalArgumentException
     *             if out of bounds
     */
    public void setBitOffset(final int bitOffset) {
        this.bitOffset = bitOffset;
    }
}
