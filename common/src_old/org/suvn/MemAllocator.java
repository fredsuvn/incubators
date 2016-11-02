package com.cogician.quicker;

import com.cogician.quicker.struct.ArrayPos;

/**
 * A class to allocate or reallocate memory as {@linkplain Bitsss}.
 *
 * @author Fred Suvn
 * @version 0.0.0, 2015-07-03 11:21:38
 */
public class MemAllocator {
    /**
     * Allocates a data block of special length in bytes.
     *
     * @param length
     *            special length in bytes, 0 or positive
     * @return a data block of special length in bytes, not null
     * @throws IllegalArgumentException
     *             if length in bytes is negative
     */
    public static Bitsss allocate(final int length) {
        return new Bitsss(length);
    }

    /**
     * Allocates a data block of special length in bits.
     *
     * @param length
     *            special length in bits, 0 or positive
     * @return a data block of special length in bits, not null
     * @throws IllegalArgumentException
     *             if length in bits is negative
     */
    public static Bitsss allocate(final long lengthInBits) {
        return new Bitsss(lengthInBits);
    }

    /**
     * Allocates a data block of special data length.
     *
     * @param length
     *            special data length, not null
     * @return a data block of special data length, not null
     * @throws IllegalArgumentException
     *             if data length is null
     */
    public static Bitsss allocate(final DataNumber length) {
        return new Bitsss(length);
    }

    /**
     * Allocates a data block of special length in bytes and special type of bit
     * wide, the type of bit wide is a class of primitive type including byte,
     * short, char, int, float, long, double, returned data block's data are
     * stored in array of the primitive type. For example:
     *
     * <pre>
     * To allocate a block based on a byte array:
     * DataBlock block = MemAllocator.allocate(1024, byte.class);
     * </pre>
     *
     * @param length
     *            special length in bytes, 0 or positive
     * @param wildType
     *            type of bit wide, one of byte.class, short.class, char.class,
     *            int.class, float.class, long.class and double.class
     * @return a data block of special length in bytes and special type of bit
     *         wide, not null
     * @throws NullPointerException
     *             if type of bit wide is null
     * @throws IllegalArgumentException
     *             if length in bytes is negative or type of bit wide is not one
     *             of special type
     */
    public static Bitsss allocate(final int length, final Class<?> wildType) {
        return new Bitsss(length, wildType);
    }

    /**
     * Allocates a data block of special length in bits and special type of bit
     * wide, the type of bit wide is a class of primitive type including byte,
     * short, char, int, float, long, double, returned data block's data are
     * stored in array of the primitive type. For example:
     *
     * <pre>
     * To allocate a block based on a byte array:
     * DataBlock block = MemAllocator.allocate(1024 * 8L, byte.class);
     * </pre>
     *
     * @param length
     *            special length in bits, 0 or positive
     * @param wildType
     *            type of bit wide, one of byte.class, short.class, char.class,
     *            int.class, float.class, long.class and double.class
     * @return a data block of special length in bits and special type of bit
     *         wide, not null
     * @throws NullPointerException
     *             if type of bit wide is null
     * @throws IllegalArgumentException
     *             if length in bits is negative or type of bit wide is not one
     *             of special type
     */
    public static Bitsss allocate(final long lengthInBits, final Class<?> wildType) {
        return new Bitsss(lengthInBits, wildType);
    }

    /**
     * Allocates a data block of special data length and special type of bit
     * wide, the type of bit wide is a class of primitive type including byte,
     * short, char, int, float, long, double, returned data block's data are
     * stored in array of the primitive type. For example:
     *
     * <pre>
     * To allocate a block based on a byte array:
     * DataBlock block = MemAllocator.allocate(1024, byte.class);
     * </pre>
     *
     * @param length
     *            special data length, not null
     * @param wildType
     *            type of bit wide, one of byte.class, short.class, char.class,
     *            int.class, float.class, long.class and double.class
     * @return a data block of special data length and special type of bit wide,
     *         not null
     * @throws NullPointerException
     *             if special data length is null or type of bit wide is null
     */
    public static Bitsss allocate(final DataNumber length, final Class<?> wildType) {
        return new Bitsss(length, wildType);
    }

    /**
     * Wraps a primitive array as a data block and return. The data are one
     * between primitive array and returned data block.
     *
     * @param arrayObject
     *            primitive array object, not null
     * @return a data block from wrapped primitive array, not null
     * @throws NullPointerException
     *             if primitive array object is null
     * @throws IllegalArgumentException
     *             if given object is not a primitive array object
     */
    public static Bitsss wrap(final Object arrayObject) {
        return new Bitsss(arrayObject);
    }

    /**
     * Wraps a primitive array from start index and offset to end index and
     * offset as a data block and return. The data are one between primitive
     * array and returned data block.
     *
     * @param arrayObject
     *            primitive array object, not null
     * @param startIndex
     *            start index inclusive, 0 or positive
     * @param startOffset
     *            bit offset of start index, [0, element wide - 1]
     * @param endIndex
     *            end index inclusive, 0 or positive
     * @param endOffset
     *            bit offset of end index, [0, element wide - 1]
     * @return a data block from wrapped primitive array, not null
     * @throws NullPointerException
     *             if primitive array object is null
     * @throws IllegalArgumentException
     *             if given object is not a primitive array object, or index and
     *             offset are illegal
     * @throws OutOfBoundsException
     *             if special range out of bounds of array
     */
    public static Bitsss wrap(final Object arrayObject, final int startIndex,
            final int startOffset, final int endIndex, final int endOffset) {
        return new Bitsss(arrayObject, startIndex, startOffset, endIndex,
                endOffset);
    }

    /**
     * Wraps a primitive array from start array position to end array position
     * as a data block and return. The data are one between primitive array and
     * returned data block.
     *
     * @param arrayObject
     *            primitive array object, not null
     * @param startPos
     *            start array position in bounds, not null
     * @param endPos
     *            end array position in bounds, not null
     * @return a data block from wrapped primitive array, not null
     * @throws NullPointerException
     *             if primitive array object is null
     * @throws IllegalArgumentException
     *             if given object is not a primitive array object, or positions
     *             are illegal
     * @throws OutOfBoundsException
     *             if special range out of bounds of array
     */
    public static Bitsss wrap(final Object arrayObject, final ArrayPos startPos,
            final ArrayPos endPos) {
        return new Bitsss(arrayObject, startPos, endPos);
    }
}