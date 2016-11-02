package com.cogician.quicker.binary;

import java.io.Serializable;

import com.cogician.quicker.Bitsss;
import com.cogician.quicker.annotation.Base;

/**
 * <p>
 * A {@linkplain Base} interface to random access and manipulate data from a
 * contiguous of space, commonly is an array of primitive type, accurate to
 * bits. The max length supported is {@linkplain Integer#MAX_VALUE} bytes. The
 * accessor can not be empty and the length of an accssor must >= 1 bit.
 * </p>
 * <p>
 * This interface extends from {@linkplain BitsGetter}, {@linkplain BitsPutter} and
 * {@linkplain BitsToArray}, which are used to read, write and put data into
 * a primitive array.
 * </p>
 * <p>
 * This interface should not be used directly as a common public API, but only
 * called innerly by higher type like {@linkplain Bitsss} instead, actually, this
 * interface is only used to implement {@linkplain Bitsss} which as a public API.
 * </p>
 * <p>
 * <b>Note</b> the parameters of methods of this interface will not be checked.
 * Methods will throw exceptions or return wrong results if given parameters
 * don't meet the requirements. The details of exceptions and wrong results
 * depend on implementations.
 * </p>
 * <p>
 * This interface is serializable and cloneable.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2015-03-27 14:37:55
 * @since 0.0.0
 * @see BitsGetter
 * @see BitsPutter
 * @see BitsToArray
 */
@Base
public interface Bits extends BitsGetter, BitsPutter, BitsToArray,
        Serializable, Cloneable {
    /**
     * <p>
     * Returns whether this accessor is empty. An empty accessor's length is 0.
     * </p>
     *
     * @return whether this accessor is empty
     * @since 0.0.0
     */
    public boolean isEmpty();

    /**
     * <p>
     * Returns bit length of space represented by this interface.
     * </p>
     *
     * @return bit length of space represented by this interface
     * @since 0.0.0
     */
    public long lengthInBits();

    /**
     * <p>
     * Returns byte length of space represented by this interface. If the actual
     * length is not a multiple of byte (8 bits), it will return the length
     * rounded down to byte.
     * </p>
     *
     * @return byte length of space represented by this interface
     * @since 0.0.0
     */
    public int lengthInBytes();

    /**
     * <p>
     * Returns number of remainder bits from first bit of specified position
     * inclusive in bytes.
     * </p>
     *
     * @param posInBytes
     *            specified position inclusive in bytes, 0 or length in bytes -
     *            1 if length in bytes greater than 1
     * @return number of remainder bits from first bit of specified position in
     *         bytes
     * @since 0.0.0
     */
    public long remainderLength(int posInBytes);

    /**
     * <p>
     * Returns number of remainder bits from specified position inclusive in
     * bits.
     * </p>
     *
     * @param posInBits
     *            specified position inclusive in bits, [0L, length in bits -
     *            1L]
     * @return number of remainder bits from specified position in bits
     * @since 0.0.0
     */
    public long remainderLength(long posInBits);

    /**
     * <p>
     * Returns part of this accessor from specified start position inclusive in
     * bytes to end.
     * </p>
     *
     * @param startInBytes
     *            specified start position inclusive in bytes, 0 or length in
     *            bytes - 1 if length in bytes greater than 1
     * @return part of this accessor from specified start position inclusive in
     *         bytes to end
     * @since 0.0.0
     */
    public Bits partOf(int startInBytes);

    /**
     * <p>
     * Returns part of this accessor from specified start position inclusive to
     * end position exclusive, in bytes.
     * </p>
     *
     * @param startInclusive
     *            specified start position inclusive in bytes, 0 or length in
     *            bytes - 1 if length in bytes greater than 1
     * @param endExclusive
     *            specified end position exclusive in bytes, (startInclusive,
     *            length in bytes]
     * @return part of this accessor from specified start position inclusive to
     *         end position exclusive, in bytes
     * @since 0.0.0
     */
    public Bits partOf(int startInclusive, int endExclusive);

    /**
     * <p>
     * Returns part of this accessor from specified start position inclusive in
     * bits to end.
     * </p>
     *
     * @param startInBits
     *            specified start position inclusive in bits, [0L, length in
     *            bits - 1L]
     * @return part of this accessor from specified start position inclusive in
     *         bits to end
     * @since 0.0.0
     */
    public Bits partOf(long startInBits);

    /**
     * <p>
     * Returns part of this accessor from specified start position inclusive to
     * end position exclusive, in bits.
     * </p>
     *
     * @param startInclusive
     *            specified start position inclusive in bits, [0L, length in
     *            bits - 1L]
     * @param endExclusive
     *            specified end position exclusive in bits, (startInclusive,
     *            length in bits]
     * @return part of this accessor from specified start position inclusive to
     *         end position exclusive, in bits
     * @since 0.0.0
     */
    public Bits partOf(long startInclusive, long endExclusive);

    /**
     * <p>
     * Shadow copy this space, return a new one, but the data is shared with
     * this instance.
     * </p>
     *
     * @return a new instance but the data is shared with this instance
     */
    public Bits shadowClone();

    /**
     * <p>
     * Deep copy this space, return a new one.
     * </p>
     *
     * @return a new instance deep copied by this space
     */
    public Bits deepClone();

    /**
     * <p>
     * Deep copy this space, return a new one.
     * </p>
     * <p>
     * This method is same as {@linkplain #deepClone()}.
     * </p>
     *
     * @return a new instance deep copied by this space
     */
    public Bits clone();

    /**
     * <p>
     * Set all bits of this accessor to specified value, true is 1, false is 0.
     * </p>
     *
     * @param value
     *            specified value, true is 1, false is 0
     */
    public void setBits(boolean value);

    /**
     * <p>
     * Copies data of this accessor into specified dest accessor, the copied
     * length is shorter one of two accessor. This length in bits is the actual
     * number of bits copied, will be returned. Copy in head-to-tail
     * (left-to-right) order.
     * </p>
     * <p>
     * The parameters of this method will not be checked. It will throw
     * exceptions or return wrong results if given parameters don't meet the
     * requirements. The details of exceptions and wrong results depend on
     * implementations.
     * </p>
     *
     * @param dest
     *            specified dest accessor, not null
     * @return actual number of bits copied
     */
    public long copyTo(Bits dest);

    /**
     * <p>
     * And operation between this accessor and target accessor, the result
     * stored in this instance, like:
     *
     * <pre>
     * accessor = accessor &amp; target;
     * </pre>
     *
     * The different between this and primitive type is: this method operate
     * from head to tail (left-to-right), if two accessor's length is not equal,
     * it will stop operate when reach to the end of short one. The actual
     * number of bits operated -- mostly is length in bits of short one --
     * returned.
     * </p>
     * <p>
     * The parameters of this method will not be checked. It will throw
     * exceptions or return wrong results if given parameters don't meet the
     * requirements. The details of exceptions and wrong results depend on
     * implementations.
     * </p>
     *
     * @param target
     *            target accessor, not null
     * @return actual number of bits operated
     */
    public long and(Bits target);

    /**
     * <p>
     * Or operation between this accessor and target accessor, the result stored
     * in this instance, like:
     *
     * <pre>
     * accessor = accessor | target;
     * </pre>
     *
     * The different between this and primitive type is: this method operate
     * from head to tail (left-to-right), if two accessor's length is not equal,
     * it will stop operate when reach to the end of short one. The actual
     * number of bits operated -- mostly is length in bits of short one --
     * returned.
     * </p>
     * <p>
     * The parameters of this method will not be checked. It will throw
     * exceptions or return wrong results if given parameters don't meet the
     * requirements. The details of exceptions and wrong results depend on
     * implementations.
     * </p>
     *
     * @param target
     *            target accessor, not null
     * @return actual number of bits operated
     */
    public long or(Bits target);

    /**
     * <p>
     * Xor operation between this accessor and target accessor, the result
     * stored in this instance, like:
     *
     * <pre>
     * accessor = accessor &circ; target;
     * </pre>
     *
     * The different between this and primitive type is: this method operate
     * from head to tail (left-to-right), if two accessor's length is not equal,
     * it will stop operate when reach to the end of short one. The actual
     * number of bits operated -- mostly is length in bits of short one --
     * returned.
     * </p>
     * <p>
     * The parameters of this method will not be checked. It will throw
     * exceptions or return wrong results if given parameters don't meet the
     * requirements. The details of exceptions and wrong results depend on
     * implementations.
     * </p>
     *
     * @param target
     *            target accessor, not null
     * @return actual number of bits operated
     */
    public long xor(Bits target);

    /**
     * <p>
     * Not operation for this accessor like:
     *
     * <pre>
     * accessor = ^accessor;
     * </pre>
     *
     * </p>
     */
    public void not();

    /**
     * <p>
     * Reverses this accessor in bits.
     * </p>
     */
    public void reverse();

    /**
     * <p>
     * Reverses this accessor in bytes. If the length of this accessor is not
     * multiple of bytes, action of this method is unexpected.
     * </p>
     */
    public void reverseInBytes();

    /**
     * <p>
     * Logical shift left bits of specified number like:
     *
     * <pre>
     * accessor = accessor &lt;&lt; bits;
     * </pre>
     *
     * </p>
     * <p>
     * The parameters of this method will not be checked. It will throw
     * exceptions or return wrong results if given parameters don't meet the
     * requirements. The details of exceptions and wrong results depend on
     * implementations.
     * </p>
     * <p>
     * This method is same with {@linkplain #arithmeticLeft(long)}.
     * </p>
     *
     * @param bits
     *            specified number of shifted bits, [0L, length in bits]
     * @see #arithmeticLeft(long)
     */
    public void logicalLeft(long bits);

    /**
     * <p>
     * Logical shift right bits of specified number like:
     *
     * <pre>
     * accessor = accessor &gt;&gt;&gt; bits;
     * </pre>
     *
     * </p>
     * <p>
     * The parameters of this method will not be checked. It will throw
     * exceptions or return wrong results if given parameters don't meet the
     * requirements. The details of exceptions and wrong results depend on
     * implementations.
     * </p>
     *
     * @param bits
     *            specified number of shifted bits, [0L, length in bits]
     */
    public void logicalRight(long bits);

    /**
     * <p>
     * Arithmetic shift left bits of specified number like:
     *
     * <pre>
     * accessor = accessor &lt;&lt; bits;
     * </pre>
     *
     * </p>
     * <p>
     * The parameters of this method will not be checked. It will throw
     * exceptions or return wrong results if given parameters don't meet the
     * requirements. The details of exceptions and wrong results depend on
     * implementations.
     * </p>
     * <p>
     * This method is same with {@linkplain #logicalLeft(long)}.
     * </p>
     *
     * @param bits
     *            specified number of shifted bits, [0L, length in bits]
     * @see #logicalLeft(long)
     */
    public void arithmeticLeft(long bits);

    /**
     * <p>
     * Arithmetic shift right bits of specified number like:
     *
     * <pre>
     * accessor = accessor &gt;&gt; bits;
     * </pre>
     *
     * </p>
     * <p>
     * The parameters of this method will not be checked. It will throw
     * exceptions or return wrong results if given parameters don't meet the
     * requirements. The details of exceptions and wrong results depend on
     * implementations.
     * </p>
     *
     * @param bits
     *            specified number of shifted bits, [0L, length in bits]
     */
    public void arithmeticRight(long bits);

    /**
     * <p>
     * Rotates left bits of specified number like:
     *
     * <pre>
     * ROL AL, bits
     * </pre>
     *
     * </p>
     * <p>
     * If specified number of bits is negative, rotate right:
     *
     * <pre>
     * rotateLeft(-bits) == rotateRight(bits)
     * </pre>
     *
     * </p>
     * <p>
     * The parameters of this method will not be checked. It will throw
     * exceptions or return wrong results if given parameters don't meet the
     * requirements. The details of exceptions and wrong results depend on
     * implementations.
     * </p>
     *
     * @param bits
     *            specified number of bits, [0L, length in bits]
     * @see #rotateRight(long)
     */
    public void rotateLeft(long bits);

    /**
     * <p>
     * Rotates right bits of specified number like:
     *
     * <pre>
     * ROL AL, bits
     * </pre>
     *
     * </p>
     * <p>
     * If specified number of bits is negative, rotate left:
     *
     * <pre>
     * rotateRight(-bits) == rotateLeft(bits)
     * </pre>
     *
     * </p>
     * <p>
     * The parameters of this method will not be checked. It will throw
     * exceptions or return wrong results if given parameters don't meet the
     * requirements. The details of exceptions and wrong results depend on
     * implementations.
     * </p>
     *
     * @param bits
     *            specified number of bits, [0L, length in bits]
     * @see #rotateLeft(long)
     */
    public void rotateRight(long bits);

    /**
     * <p>
     * Returns source data object of this accessor. The source data object is
     * base and underline object which directly read, written and manipulated by
     * this accessor. Commonly it is an array of primitive, or a native object
     * represents a block of memory, or even itself.
     * </p>
     *
     * @return source data object of this accessor, not null
     */
    public Object getSourceData();
}
