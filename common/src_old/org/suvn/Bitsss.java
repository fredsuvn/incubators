package com.cogician.quicker;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.ObjLongConsumer;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import com.cogician.quicker.binary.Bits;
import com.cogician.quicker.binary.data.BaseAccessorFactory;
import com.cogician.quicker.binary.data.impl.primitivearray.PrimitiveArrayBaseAccessorFactoryImpl;
import com.cogician.quicker.struct.ArrayPos;

/**
 * This block represents data block, like a byte array, but this data block's
 * length may be not multiple of byte, may be 7 bits or 33 bits.
 * <p>
 * The data block commonly is used to read, write or manipulate data. It
 * accurate to bit and can read write special length in bits without multiple of
 * byte:
 *
 * <pre>
 * DataBlock data = new DataBlock(1023L);
 * data.writeInt(0L, 0xffeeccaa);
 * System.out.println(data.readBitsAsInt(8L, 4)); // print 0x0000000e!
 * </pre>
 *
 * Or, it can do bit operations:
 *
 * <pre>
 * DataBlock data1 = new DataBlock(1023L);
 * DataBlock data2 = new DataBlock(1023L);
 * data1.writeInt(0L, 0xffeecc00);
 * data2.writeInt(0L, 0x00000011);
 * data1.or(data2);
 * System.out.println(data1.readInt(0L)); // print 0xffeecc11!
 * </pre>
 *
 * Convenient for each and stream operations:
 *
 * <pre>
 * DataBlock data = new DataBlock(1023L);;
 * data.forEachByte((b, s) -&gt; {System.out.println(b);}) // print each byte of block!
 * data.forEach(6L, (block, s) -&gt; {
 *     System.out.println(block.toBinaryString());
 * }); // print binary string of each 6 bits of block!
 * </pre>
 *
 * And more.
 *
 * @author Fred Suvn
 * @version 0.0.0, 2015-06-03 14:51:52
 * @since 0.0.0
 */
public class Bitsss implements Serializable, Cloneable, BitsReader, BitsWriter,
        IndexedSubable<Bitsss> {

    /**
     * Serial ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Factory for create base accessor.
     */
    private static final BaseAccessorFactory factory = PrimitiveArrayBaseAccessorFactoryImpl
            .singleInstance();

    /**
     * Max length of {@linkplain Bitsss} in bits.
     */
    private static final long MAX_LENGTH_IN_BITS = Integer.MAX_VALUE * 8L;

    /**
     * Max length of {@linkplain Bitsss} in bytes.
     */
    private static final int MAX_LENGTH_IN_BYTES = Integer.MAX_VALUE;

    /**
     * Data source, commonly is a primitive array.
     */
    private final Object source;

    /**
     * Base accessor tp access data source, never null.
     */
    private final Bits base;

    /**
     * <p>
     * Constructs an instance of specified length in <b>bytes</b>. If the
     * underline data stored in array of primitive type, it will use long[]
     * default.
     * </p>
     *
     * @param lengthInBytes
     *            specified length in <b>bytes</b>, [0, Integer.MAX_VALUE]
     * @throws IllegalArgumentException
     *             if specified length is negative
     */
    public Bitsss(final int lengthInBytes) {
        this(lengthInBytes * 8L);
    }

    /**
     * <p>
     * Constructs an instance of specified length in <b>bits</b>, If the
     * underline data stored in array of primitive type, it will use long[]
     * default.
     * </p>
     *
     * @param lengthInBits
     *            specified length in <b>bits</b>, [0L,
     *            {@linkplain Integer#MAX_VALUE} * 8L]
     * @throws IllegalArgumentException
     *             if specified length is negative
     * @throws OutOfBoundsException
     *             if specified length out of bounds
     */
    public Bitsss(final long lengthInBits) {
        if (lengthInBits < 0) {
            throw new IllegalArgumentException(
                    "Length should be less than or equal to Integer.MAX_VALUE!");
        }
        if (lengthInBits > MAX_LENGTH_IN_BITS) {
            throw new OutOfBoundsException(lengthInBits);
        }
        FileChannel.
        base = factory.create(lengthInBits, byte.class);
    }

    /**
     * Checks whether specified class is one of byte.class, short.class,
     * char.class, int.class, float.class, long.class and double.class.
     *
     * @param primitiveType
     *            specified class, should be one of byte.class, short.class,
     *            char.class, int.class, float.class, long.class and
     *            double.class
     * @return whether specified class is one of byte.class, short.class,
     *         char.class, int.class, float.class, long.class and double.class
     */
    private boolean isInPrimitiveClass(final Class<?> primitiveType) {
        return primitiveType.getName().equals(byte.class.getName())
                || primitiveType.getName().equals(short.class.getName())
                || primitiveType.getName().equals(char.class.getName())
                || primitiveType.getName().equals(int.class.getName())
                || primitiveType.getName().equals(float.class.getName())
                || primitiveType.getName().equals(long.class.getName())
                || primitiveType.getName().equals(double.class.getName());
    }

    /**
     * Constructs with special length in byte and redundant bits more than
     * length of multiple of bytes, total length of block should be less than or
     * equal to {@linkplain Integer#MAX_VALUE} bytes. This constructor should be
     * specified primitive type which as base storage of primitive array.
     *
     * @param lengthInBytes
     *            special length in byte, positive
     * @param redundantBits
     *            redundant bits more than length of multiple of bytes, [0, 7]
     * @param primitiveType
     *            primitive type, should be in byte.class, short.class,
     *            char.class, int.class, float.class, long.class and
     *            double.class
     * @throws NullPointerException
     *             thrown if primitive type is null
     * @throws IllegalArgumentException
     *             thrown if given parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    public Bitsss(final int lengthInBytes, final Class<?> primitiveType) {
        if (null == primitiveType) {
            throw new NullPointerException("Primitive type cannot be null.");
        }
        if (lengthInBytes < 0 || redundantBits < 0 || redundantBits > 7
                || !isInPrimitiveClass(primitiveType)) {
            throw new IllegalArgumentException(
                    "Length in byte should be positive, redundant bits should be in [0, 7], "
                            + "primitive class should be one of byte.class, char.class, int.class, "
                            + "float.class, long.class, double.class!");
        }
        if (lengthInBytes * 8L + redundantBits > MAX_LENGTH_IN_BITS) {
            throw new OutOfBoundsException(lengthInBytes * 8L + redundantBits);
        }
        base = factory.create(lengthInBytes, redundantBits, primitiveType);
    }

    /**
     * Constructs with special length in bits, total length of block should be
     * less than or equal to {@linkplain Integer#MAX_VALUE} bytes. This
     * constructor should be specified primitive type which as base storage of
     * primitive array.
     *
     * @param length
     *            special length in bits, positive and less than or equal to
     *            {@linkplain Integer#MAX_VALUE} bytes
     * @param primitiveType
     *            primitive type, should be in byte.class, short.class,
     *            char.class, int.class, float.class, long.class and
     *            double.class
     * @throws NullPointerException
     *             thrown if primitive type is null
     * @throws IllegalArgumentException
     *             thrown if given parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    Bitsss(final long length, final Class<?> primitiveType) {
        if (null == primitiveType) {
            throw new NullPointerException("Primitive type cannot be null!");
        }
        if (length < 0 || !isInPrimitiveClass(primitiveType)) {
            throw new IllegalArgumentException(
                    "Length should be less than or equal to Integer.MAX_VALUE, "
                            + "primitive class should be one of byte.class, char.class, int.class, "
                            + "float.class, long.class, double.class!");
        }
        if (length > MAX_LENGTH_IN_BITS) {
            throw new OutOfBoundsException(length);
        }
        base = factory.create(length, byte.class);
    }

    /**
     * Constructs with special length in bits, total length of block should be
     * less than or equal to {@linkplain Integer#MAX_VALUE} bytes. This
     * constructor should be specified primitive type which as base storage of
     * primitive array.
     *
     * @param length
     *            special length in bits, not null, positive and less than or
     *            equal to {@linkplain Integer#MAX_VALUE} bytes
     * @param primitiveType
     *            primitive type, should be in byte.class, short.class,
     *            char.class, int.class, float.class, long.class and
     *            double.class
     * @throws NullPointerException
     *             thrown if length or primitive type is null
     * @throws IllegalArgumentException
     *             thrown if given parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    Bitsss(final DataNumber length, final Class<?> primitiveType) {
        if (null == length || null == primitiveType) {
            throw new NullPointerException(
                    "Length or primitive type cannot be null!");
        }
        if (!isInPrimitiveClass(primitiveType)) {
            throw new IllegalArgumentException(
                    "Primitive class should be one of byte.class, char.class, int.class, "
                            + "float.class, long.class, double.class!");
        }
        if (!length.isInInteger()) {
            throw new OutOfBoundsException(length);
        }
        base = factory.create(length.sumBitsAsLong(), byte.class);
    }

    /**
     * Returns whether given object is a primitive array.
     *
     * @param arrayObject
     *            given object
     * @return whether given object is a primitive array
     */
    private boolean isPrimitiveArray(final Object arrayObject) {
        return arrayObject.getClass().getComponentType().isPrimitive();
    }

    /**
     * Constructs with special primitive array, total length of block should be
     * less than or equal to {@linkplain Integer#MAX_VALUE} bytes. The data of
     * created data block are stored base on given primitive array, array's data
     * are shared with this block.
     *
     * @param arrayObject
     *            special primitive array, should be one of byte[], short[],
     *            char[], int[], float[], long[] or double[]
     * @throws NullPointerException
     *             thrown if array object is null
     * @throws IllegalArgumentException
     *             thrown if given parameter is not primitive array object
     */
    Bitsss(final Object arrayObject) {
        if (!isPrimitiveArray(Objects.requireNonNull(arrayObject,
                "Array Object cannot be null!"))) {
            throw new IllegalArgumentException(
                    "Array Object should be an array object!");
        }
        base = factory.create(arrayObject);
    }

    /**
     * Constructs with special primitive array from start position with special
     * index and bit offset of the index, to end position with special index and
     * bit offset of the index, total length of block should be less than or
     * equal to {@linkplain Integer#MAX_VALUE} bytes. End position should be
     * greater than or equal to start position. The data of created data block
     * are stored base on given primitive array, array's data are shared with
     * this block.
     *
     * @param arrayObject
     *            special primitive array, should be one of byte[], short[],
     *            char[], int[], float[], long[] or double[]
     * @param startIndex
     *            start index in bounds
     * @param startOffset
     *            bit offset of start index, [0, element wide - 1]
     * @param endIndex
     *            end index in bounds
     * @param endOffset
     *            bit offset of end index, [0, element wide - 1]
     * @throws NullPointerException
     *             thrown if array object is null
     * @throws IllegalArgumentException
     *             thrown if given parameter is not primitive array object or
     *             positions are illegal
     */
    Bitsss(final Object arrayObject, final int startIndex, final int startOffset,
            final int endIndex, final int endOffset) {
        if (!isPrimitiveArray(Objects.requireNonNull(arrayObject,
                "Array Object cannot be null!"))) {
            throw new IllegalArgumentException(
                    "Array Object should be an array object!");
        }
        if (startIndex < 0 || endIndex < 0 || startOffset < 0 || endOffset < 0
                || startOffset > 7 || endOffset > 7 || startIndex > endIndex
                || ((startIndex == endIndex) && (startOffset > endOffset))) {
            throw new IllegalArgumentException(
                    "Indexes and bit offsets are illegal!");
        }
        if (startIndex * 8L + startOffset > endIndex * 8L + endOffset) {
            throw new OutOfBoundsException(
                    "End position should be greater than or equal to start position!");
        }
        base = factory.create(arrayObject, startIndex, startOffset, endIndex,
                endOffset);
    }

    /**
     * Constructs with special primitive array from start position with special
     * index and bit offset of the index, to end position with special index and
     * bit offset of the index, total length of block should be less than or
     * equal to {@linkplain Integer#MAX_VALUE} bytes. End position should be
     * greater than or equal to start position. The data of created data block
     * are stored base on given primitive array, array's data are shared with
     * this block.
     *
     * @param arrayObject
     *            special primitive array, should be one of byte[], short[],
     *            char[], int[], float[], long[] or double[]
     * @param startPos
     *            start array position in bounds
     * @param endPos
     *            end array position in bounds
     * @throws NullPointerException
     *             thrown if array object or array poses is null
     * @throws IllegalArgumentException
     *             thrown if given parameter is not primitive array object or
     *             positions are illegal
     */
    Bitsss(final Object arrayObject, final ArrayPos startPos,
            final ArrayPos endPos) {
        if (null == arrayObject || null == startPos || null == endPos) {
            throw new NullPointerException(
                    "Array Object and array positions cannot be null!");
        }
        if (!isPrimitiveArray(arrayObject)) {
            throw new IllegalArgumentException(
                    "Array Object should be an array object!");
        }
        if (startPos.index < 0
                || endPos.index < 0
                || startPos.offset < 0
                || endPos.offset < 0
                || startPos.offset > 7
                || endPos.offset > 7
                || startPos.index > endPos.offset
                || ((startPos.index == endPos.index) && (startPos.offset > endPos.offset))) {
            throw new IllegalArgumentException(
                    "Indexes and bit offsets are illegal!");
        }
        if (startPos.index * 8L + startPos.offset > endPos.index * 8L
                + endPos.offset) {
            throw new OutOfBoundsException(
                    "End position should be greater than or equal to start position!");
        }
        base = factory.create(arrayObject, startPos.index, startPos.offset,
                endPos.index, endPos.offset);
    }

    /**
     * Constructs with special base accessor.
     *
     * @param base
     *            special base accessor, not null
     * @throws NullPointerException
     *             thrown if special base accessor is null
     */
    private Bitsss(final Bits base) {
        this.base = base;
    }

    /**
     * Shadow copy this instance.
     *
     * @return shadow copy of this instance
     */
    public Bitsss shadowClone() {
        return new Bitsss(base);
    }

    /**
     * Deep copy this instance.
     *
     * @return Deep copy of this instance
     */
    public Bitsss deepClone() {
        return new Bitsss(base.deepClone());
    }

    /**
     * Deep copy, same as {@linkplain #deepClone()}.
     *
     * @return Deep copy of this instance
     */
    @Override
    public Bitsss clone() {
        return deepClone();
    }

    /**
     * Returns whether this data block is empty.
     *
     * @return whether this data block is empty
     */
    public boolean isEmpty() {
        return base.isEmpty();
    }

    /**
     * Returns bit length of this data block.
     *
     * @return bit length of this data block
     */
    public long lengthInBits() {
        return base.lengthInBits();
    }

    /**
     * Returns byte length of this data block. If the actual length is not a
     * multiple of byte (8 bits), it will return length rounded down to byte.
     *
     * @return byte length of this data block
     */
    public int lengthInBytes() {
        return base.lengthInBytes();
    }

    /**
     * Checks whether given position or length is legal.
     *
     * @param bytePos
     *            position or length in bytes, positive
     * @param bitOffset
     *            detail bit offset of byte position or length, [0, 7]
     * @throws IllegalArgumentException
     *             thrown if Illegal
     */
    private void checkPosOrLengthLegality(final int bytePos, final int bitOffset) {
        if (bytePos < 0 || bitOffset < 0 || bitOffset > 7) {
            throw new IllegalArgumentException("Position illegal!");
        }
    }

    /**
     * Checks whether given position or length is legal.
     *
     * @param pos
     *            position or length in bits, positive
     * @throws IllegalArgumentException
     *             thrown if Illegal
     */
    private void checkPosOrLengthLegality(final long pos) {
        if (pos < 0) {
            throw new IllegalArgumentException("Position illegal!");
        }
    }

    /**
     * Checks whether given position or length is not null.
     *
     * @param pos
     *            position or length in bits, not null
     * @throws NullPointerException
     *             thrown if Illegal
     */
    private void checkPosOrLengthLegality(final DataNumber pos) {
        if (null == pos) {
            throw new NullPointerException("Position is null!");
        }
    }

    /**
     * Checks whether given position in bounds of block.
     *
     * @param bytePos
     *            position in bytes, default positive
     * @param posOffset
     *            detail bit offset of byte position, default in [0, 7]
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    private void checkPosInBounds(final int bytePos, final int posOffset) {
        checkPosInBounds(bytePos * 8L + posOffset);
    }

    /**
     * Checks whether given position in bounds of block.
     *
     * @param pos
     *            position in bits, default positive
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    private void checkPosInBounds(final long pos) {
        if (pos >= lengthInBits()) {
            throw new OutOfBoundsException(pos);
        }
    }

    /**
     * Checks whether given position in bounds of block.
     *
     * @param pos
     *            position in bits, default not null
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    private void checkPosInBounds(final DataNumber pos) {
        checkPosInBounds(pos.sumBitsAsLong());
    }

    /**
     * Returns remainder bits number from special position inclusive to end of
     * this data block inclusive in bits.
     * <p>
     * The special position is indicated by a byte position (specified by
     * {@code bytePos}) and a bit offset of the byte position (specified by
     * {@code bitOffset}), byte position should be positive and in bounds of
     * block, bit offset should be in 0 inclusive to 8 exclusive.
     *
     * @param bytePos
     *            special byte position in bounds
     * @param posOffset
     *            bit offset of special byte position, [0, 7]
     * @return distance between special position and end of block in bits
     * @throws IllegalArgumentException
     *             thrown if given parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    public long remainderLength(final int bytePos, final int posOffset) {
        checkPosOrLengthLegality(bytePos, posOffset);
        checkPosInBounds(bytePos, posOffset);
        return base.remainderLength(bytePos, posOffset);
    }

    /**
     * Returns remainder bits number from special position inclusive to end of
     * space represented by this interface inclusive in bits.
     * <p>
     * The special position is in bits and (specified by {@code pos}) should be
     * positive and in bounds of space.
     *
     * @param pos
     *            special position in bits in bounds
     * @return distance between special position and end of space represented by
     *         this interface in bits
     * @throws IllegalArgumentException
     *             thrown if given parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    public long remainderLength(final long pos) {
        checkPosOrLengthLegality(pos);
        checkPosInBounds(pos);
        return base.remainderLength(pos);
    }

    /**
     * Returns remainder bits number from special position inclusive to end of
     * space represented by this interface inclusive in bits.
     * <p>
     * The special position is in bits and (specified by {@code pos}) should be
     * in bounds of space.
     *
     * @param pos
     *            special position in bits in bounds, not null
     * @return distance between special position and end of space represented by
     *         this interface in bits
     * @throws NullPointerException
     *             thrown if special position is null
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    public long remainderLength(final DataNumber pos) {
        checkPosOrLengthLegality(pos);
        checkPosInBounds(pos);
        return base.remainderLength(pos.sumBitsAsLong());
    }

    /**
     * Check whether special distance with special length in bits start from
     * special position is in bounds of block.
     *
     * @param startPos
     *            start position in bits in bounds, default legal
     * @param length
     *            length in bits in bounds, default legal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    private void checkBoundsInBounds(final long startPos, final long length) {
        if (startPos + length > lengthInBits()) {
            throw new OutOfBoundsException(startPos + length);
        }
    }

    /**
     * Returns part of block from start position to end.
     *
     * @param startBytePos
     *            start position in bytes in bounds
     * @param startOffset
     *            detail bit offset of start position, [0, 7]
     * @return part of block from start position to end
     * @throws IllegalArgumentException
     *             thrown if given parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    public Bitsss partOf(final int startBytePos, final int startOffset) {
        return partOf(startBytePos, startOffset, lengthInBits()
                - (startBytePos * 8L + startOffset));
    }

    /**
     * Returns part of block of special length from start position.
     *
     * @param startBytePos
     *            start position in bytes in bounds
     * @param startOffset
     *            detail bit offset of start position, [0, 7]
     * @param lengthInBytes
     *            special length in byte, positive
     * @param redundantBits
     *            redundant bits more than length of multiple of bytes, [0, 7]
     * @return part of block of special length from start position
     * @throws IllegalArgumentException
     *             thrown if given parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    public Bitsss partOf(final int startBytePos, final int startOffset,
            final int lengthInBytes, final int redundantBits) {
        checkPosOrLengthLegality(startBytePos, startOffset);
        checkPosOrLengthLegality(lengthInBytes, redundantBits);
        checkBoundsInBounds(startBytePos * 8L + startOffset, lengthInBytes * 8L
                + redundantBits);
        if (lengthInBytes == 0 && redundantBits == 0) {
            return new Bitsss();
        } else {
            return new Bitsss(base.partOf(startBytePos, startOffset,
                    lengthInBytes, redundantBits));
        }
    }

    /**
     * Returns part of block of special length from start position.
     *
     * @param startBytePos
     *            start position in bytes in bounds
     * @param startOffset
     *            detail bit offset of start position, [0, 7]
     * @param length
     *            special length in bits in bounds
     * @return part of block of special length from start position
     * @throws IllegalArgumentException
     *             thrown if given parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    public Bitsss partOf(final int startBytePos, final int startOffset,
            final long length) {
        checkPosOrLengthLegality(startBytePos, startOffset);
        checkPosOrLengthLegality(length);
        checkBoundsInBounds(startBytePos * 8L + startOffset, length);
        if (length == 0L) {
            return new Bitsss();
        } else {
            return new Bitsss(
                    base.partOf(startBytePos * 8L + startOffset, length));
        }
    }

    /**
     * Returns part of block from start position to end.
     *
     * @param startPos
     *            start position in bounds
     * @return part of block from start position to end
     * @throws IllegalArgumentException
     *             thrown if given parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    public Bitsss partOf(final long startPos) {
        return partOf(startPos, lengthInBits() - startPos);
    }

    /**
     * Returns part of block of special length from start position.
     *
     * @param startPos
     *            start position in bounds
     * @param lengthInBytes
     *            special length in byte, positive
     * @param redundantBits
     *            redundant bits more than length of multiple of bytes, [0, 7]
     * @return part of block of special length from start position
     * @throws IllegalArgumentException
     *             thrown if given parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    public Bitsss partOf(final long startPos, final int lengthInBytes,
            final int redundantBits) {
        checkPosOrLengthLegality(startPos);
        checkPosOrLengthLegality(lengthInBytes, redundantBits);
        checkBoundsInBounds(startPos, lengthInBytes * 8L + redundantBits);
        if (lengthInBytes == 0 && redundantBits == 0) {
            return new Bitsss();
        } else {
            return new Bitsss(base.partOf(startPos, lengthInBytes * 8L
                    + redundantBits));
        }
    }

    /**
     * Returns part of block of special length from start position.
     *
     * @param startPos
     *            start position in bounds
     * @param length
     *            special length in bits in bounds
     * @return part of block of special length from start position
     * @throws IllegalArgumentException
     *             thrown if given parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    public Bitsss partOf(final long startPos, final long length) {
        checkPosOrLengthLegality(startPos);
        checkPosOrLengthLegality(length);
        checkBoundsInBounds(startPos, length);
        if (length == 0L) {
            return new Bitsss();
        } else {
            return new Bitsss(base.partOf(startPos, length));
        }
    }

    /**
     * Returns part of block from start position to end.
     *
     * @param startPos
     *            start position in bounds
     * @return part of block from start position to end
     * @throws NullPointerException
     *             thrown if exist one of parameters is null
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    public Bitsss partOf(final DataNumber startPos) {
        if (null == startPos) {
            throw new NullPointerException("Position or length cannot be null!");
        }
        return partOf(startPos.sumBitsAsLong(),
                lengthInBits() - startPos.sumBitsAsLong());
    }

    /**
     * Returns part of block of special length from start position.
     *
     * @param startPos
     *            start position in bounds
     * @param length
     *            special length in bits in bounds
     * @return part of block of special length from start position
     * @throws NullPointerException
     *             thrown if exist one of parameters is null
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    public Bitsss partOf(final DataNumber startPos, final DataNumber length) {
        if (null == startPos || null == length) {
            throw new NullPointerException("Position or length cannot be null!");
        }
        checkPosOrLengthLegality(startPos.sumBitsAsLong());
        checkPosOrLengthLegality(length.sumBitsAsLong());
        checkBoundsInBounds(startPos.sumBitsAsLong(), length.sumBitsAsLong());
        if (length.isZero()) {
            return new Bitsss();
        } else {
            return new Bitsss(base.partOf(startPos.sumBitsAsLong(),
                    length.sumBitsAsLong()));
        }
    }

    /**
     * Checks whether has enough bits to manipulate start from special position.
     *
     * @param bytePos
     *            special start position in bytes in bounds, default legal
     * @param bitOffset
     *            detail bit offset of start position, default in [0, 7]
     * @param bits
     *            special bits to be manipulated
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    private void checkHasEnoughBits(final int bytePos, final int bitOffset,
            final int bits) {
        checkBoundsInBounds(bytePos * 8L + bitOffset, bits);
    }

    /**
     * Checks whether has enough bits to manipulate start from special position.
     *
     * @param pos
     *            special start position in bytes in bounds, default legal
     * @param bits
     *            special bits to be manipulated
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    private void checkHasEnoughBits(final long pos, final int bits) {
        checkBoundsInBounds(pos, bits);
    }

    /**
     * Checks whether has enough bits to manipulate start from special position.
     *
     * @param pos
     *            special start position in bytes in bounds, default not null
     * @param bits
     *            special bits to be manipulated
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    private void checkHasEnoughBits(final DataNumber pos, final int bits) {
        checkBoundsInBounds(pos.sumBitsAsLong(), bits);
    }

    /**
     * Checks whether given bits in [1, max].
     *
     * @param bits
     *            given bits
     * @param max
     *            max bits
     * @throws IllegalArgumentException
     *             thrown if out of bits bounds
     */
    private void checkBitsInPrimitiveBounds(final int bits, final int max) {
        if (bits < 1 || bits > max) {
            throw new IllegalArgumentException(
                    "Out of bits bounds, should be in [1, " + max + "]!");
        }
    }

    /**
     * Checks whether this block is empty, if empty, an
     * {@linkplain OutOfBoundsException} will be thrown.
     *
     * @throws OutOfBoundsException
     *             thrown if empty
     */
    private void checkEmpty() {
        if (isEmpty()) {
            throw new OutOfBoundsException("Empty block!");
        }
    }

    /**
     * Returns last bit as boolean type. If this block is empty, an
     * {@linkplain OutOfBoundsException} will be thrown.
     *
     * @return last bit as boolean type
     * @throws OutOfBoundsException
     *             thrown if block is empty
     */
    public boolean toBoolean() {
        checkEmpty();
        return readBoolean(lengthInBits() - 1);
    }

    /**
     * Returns last byte of this block. If the block's length less than bits
     * number of byte type, it align total bits of the block into right, and
     * fill 0 in the head of byte.
     *
     * @return last byte of this block
     * @throws OutOfBoundsException
     *             thrown if block is empty
     */
    public byte toByte() {
        checkEmpty();
        final int bits = lengthInBits() > Byte.SIZE ? Byte.SIZE
                : (int) lengthInBits();
        final long pos = lengthInBits() > Byte.SIZE ? lengthInBits()
                - Byte.SIZE : 0L;
        return readBitsAsByte(pos, bits);
    }

    /**
     * Returns last short of this block. If the block's length less than bits
     * number of short type, it align total bits of the block into right, and
     * fill 0 in the head of short.
     *
     * @return last short of this block
     * @throws OutOfBoundsException
     *             thrown if block is empty
     */
    public short toShort() {
        checkEmpty();
        final int bits = lengthInBits() > Short.SIZE ? Short.SIZE
                : (int) lengthInBits();
        final long pos = lengthInBits() > Short.SIZE ? lengthInBits()
                - Short.SIZE : 0L;
        return readBitsAsShort(pos, bits);
    }

    /**
     * Returns last char of this block. If the block's length less than bits
     * number of char type, it align total bits of the block into right, and
     * fill 0 in the head of char.
     *
     * @return last char of this block
     * @throws OutOfBoundsException
     *             thrown if block is empty
     */
    public char toChar() {
        checkEmpty();
        final int bits = lengthInBits() > Character.SIZE ? Character.SIZE
                : (int) lengthInBits();
        final long pos = lengthInBits() > Character.SIZE ? lengthInBits()
                - Character.SIZE : 0L;
        return readBitsAsChar(pos, bits);
    }

    /**
     * Returns last int of this block. If the block's length less than bits
     * number of int type, it align total bits of the block into right, and fill
     * 0 in the head of int.
     *
     * @return last int of this block
     * @throws OutOfBoundsException
     *             thrown if block is empty
     */
    public int toInt() {
        checkEmpty();
        final int bits = lengthInBits() > Integer.SIZE ? Integer.SIZE
                : (int) lengthInBits();
        final long pos = lengthInBits() > Integer.SIZE ? lengthInBits()
                - Integer.SIZE : 0L;
        return readBitsAsInt(pos, bits);
    }

    /**
     * Returns last float of this block. If the block's length less than bits
     * number of float type, it align total bits of the block into right, and
     * fill 0 in the head of float.
     *
     * @return last float of this block
     * @throws OutOfBoundsException
     *             thrown if block is empty
     */
    public float toFloat() {
        checkEmpty();
        final int bits = lengthInBits() > Float.SIZE ? Float.SIZE
                : (int) lengthInBits();
        final long pos = lengthInBits() > Float.SIZE ? lengthInBits()
                - Float.SIZE : 0L;
        return readBitsAsFloat(pos, bits);
    }

    /**
     * Returns last long of this block. If the block's length less than bits
     * number of long type, it align total bits of the block into right, and
     * fill 0 in the head of long.
     *
     * @return last long of this block
     * @throws OutOfBoundsException
     *             thrown if block is empty
     */
    public long toLong() {
        checkEmpty();
        final int bits = lengthInBits() > Long.SIZE ? Long.SIZE
                : (int) lengthInBits();
        final long pos = lengthInBits() > Long.SIZE ? lengthInBits()
                - Long.SIZE : 0L;
        return readBitsAsLong(pos, bits);
    }

    /**
     * Returns last double of this block. If the block's length less than bits
     * number of double type, it align total bits of the block into right, and
     * fill 0 in the head of double.
     *
     * @return last double of this block
     * @throws OutOfBoundsException
     *             thrown if block is empty
     */
    public double toDouble() {
        checkEmpty();
        final int bits = lengthInBits() > Double.SIZE ? Double.SIZE
                : (int) lengthInBits();
        final long pos = lengthInBits() > Double.SIZE ? lengthInBits()
                - Double.SIZE : 0L;
        return readBitsAsDouble(pos, bits);
    }

    /**
     * Checks whether given index is legality.
     *
     * @param index
     *            given index
     * @throws IllegalArgumentException
     *             thrown if given index is illegal
     */
    private void checkIndexLegality(final int index) {
        if (index < 0) {
            throw new IllegalArgumentException("Index should be positive!");
        }
    }

    /**
     * Checks whether given index in bounds of special array length.
     *
     * @param arrayLength
     *            special array length
     * @param index
     *            given index
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    private void checkIndexInBounds(final int arrayLength, final int index) {
        if (index > arrayLength - 1) {
            throw new OutOfBoundsException(index);
        }
    }

    /**
     * Converts this block to a boolean array, each bits is an element, 1 is
     * true, 2 is false. If length of block is more than
     * {@linkplain Integer#MAX_VALUE} bits, it only convert
     * {@linkplain Integer#MAX_VALUE} bits.
     *
     * @return array converted from this block
     */
    public boolean[] toBooleanArray() {
        final boolean[] array = new boolean[(int) lengthInBits()];
        base.toBooleanArray(array, 0);
        return array;
    }

    /**
     * Puts data of this block into special boolean array, each bits is an
     * element, 1 is true, 2 is false. The data will be put until reach to end
     * of array or block. Actual put data number in bits will be returned.
     *
     * @param dest
     *            special array, not null
     * @return actual put data number in bits
     * @throws NullPointerException
     *             thrown if given array is null
     */
    public int toBooleanArray(final boolean[] dest) {
        return base.toBooleanArray(Objects.requireNonNull(dest), 0);
    }

    /**
     * Puts data of this block into special boolean array start from special
     * index, each bits is an element, 1 is true, 2 is false. The data will be
     * put until reach to end of array or block. Actual put data number in bits
     * will be returned.
     *
     * @param dest
     *            special array, not null
     * @param startIndex
     *            start index in bounds
     * @return actual put data number in bits
     * @throws NullPointerException
     *             thrown if given array is null
     * @throws IllegalArgumentException
     *             thrown if start index is illegal
     * @throws OutOfBoundsException
     *             thrown if start index out of bounds
     */
    public int toBooleanArray(boolean[] dest, final int startIndex) {
        dest = Objects.requireNonNull(dest);
        checkIndexLegality(startIndex);
        checkIndexInBounds(dest.length, startIndex);
        return base.toBooleanArray(dest, startIndex);
    }

    /**
     * Converts this block to a byte array. If length of this block is not
     * multiple of byte, the rest space of last byte will fill 0 in the tail.
     *
     * @return array converted from this block
     */
    public byte[] toByteArray() {
        final int arrayLength = lengthInBits() % Byte.SIZE == 0 ? lengthInBytes()
                : lengthInBytes() + 1;
        final byte[] array = new byte[arrayLength];
        base.toByteArray(array, 0, 0);
        return array;
    }

    /**
     * Puts data of this block into special byte array. The data will be put
     * until reach to end of array or block. Actual put data number in bits will
     * be returned.
     *
     * @param dest
     *            special array, not null
     * @return actual put data number in bits
     * @throws NullPointerException
     *             thrown if given array is null
     */
    public long toByteArray(final byte[] dest) {
        return base.toByteArray(Objects.requireNonNull(dest), 0, 0);
    }

    /**
     * Puts data of this block into special byte array start from special
     * position. The data will be put until reach to end of array or block.
     * Actual put data number in bits will be returned.
     *
     * @param dest
     *            special array, not null
     * @param startIndex
     *            start index in bounds
     * @param startOffset
     *            bit offset of start index, [0, 7]
     * @return actual put data number in bits
     * @throws NullPointerException
     *             thrown if given array is null
     * @throws IllegalArgumentException
     *             thrown if start position is illegal
     * @throws OutOfBoundsException
     *             thrown if start position out of bounds
     */
    public long toByteArray(byte[] dest, final int startIndex,
            final int startOffset) {
        dest = Objects.requireNonNull(dest);
        checkIndexLegality(startIndex);
        checkIndexInBounds(dest.length, startIndex);
        checkBitsInPrimitiveBounds(startOffset, Byte.SIZE);
        return base.toByteArray(dest, startIndex, startOffset);
    }

    /**
     * Puts data of this block into special byte array start from special
     * position. The data will be put until reach to end of array or block.
     * Actual put data number in bits will be returned.
     *
     * @param dest
     *            special array, not null
     * @param startPos
     *            start position in bounds, not null
     * @return actual put data number in bits
     * @throws NullPointerException
     *             thrown if given array is null
     * @throws IllegalArgumentException
     *             thrown if start position is illegal
     * @throws OutOfBoundsException
     *             thrown if start position out of bounds
     */
    public long toByteArray(final byte[] dest, ArrayPos startPos) {
        startPos = Objects.requireNonNull(startPos);
        return toByteArray(dest, startPos.index, startPos.offset);
    }

    /**
     * Converts this block to a short array. If length of this block is not
     * multiple of short, the rest space of last byte will fill 0 in the tail.
     *
     * @return array converted from this block
     */
    public short[] toShortArray() {
        final int arrayLength = lengthInBits() % Short.SIZE == 0 ? lengthInBytes()
                : lengthInBytes() + 1;
        final short[] array = new short[arrayLength];
        base.toShortArray(array, 0, 0);
        return array;
    }

    /**
     * Puts data of this block into special short array. The data will be put
     * until reach to end of array or block. Actual put data number in bits will
     * be returned.
     *
     * @param dest
     *            special array, not null
     * @return actual put data number in bits
     * @throws NullPointerException
     *             thrown if given array is null
     */
    public long toShortArray(final short[] dest) {
        return base.toShortArray(Objects.requireNonNull(dest), 0, 0);
    }

    /**
     * Puts data of this block into special short array start from special
     * position. The data will be put until reach to end of array or block.
     * Actual put data number in bits will be returned.
     *
     * @param dest
     *            special array, not null
     * @param startIndex
     *            start index in bounds
     * @param startOffset
     *            bit offset of start index, [0, 7]
     * @return actual put data number in bits
     * @throws NullPointerException
     *             thrown if given array is null
     * @throws IllegalArgumentException
     *             thrown if start position is illegal
     * @throws OutOfBoundsException
     *             thrown if start position out of bounds
     */
    public long toShortArray(short[] dest, final int startIndex,
            final int startOffset) {
        dest = Objects.requireNonNull(dest);
        checkIndexLegality(startIndex);
        checkIndexInBounds(dest.length, startIndex);
        checkBitsInPrimitiveBounds(startOffset, Short.SIZE);
        return base.toShortArray(dest, startIndex, startOffset);
    }

    /**
     * Puts data of this block into special short array start from special
     * position. The data will be put until reach to end of array or block.
     * Actual put data number in bits will be returned.
     *
     * @param dest
     *            special array, not null
     * @param startPos
     *            start position in bounds, not null
     * @return actual put data number in bits
     * @throws NullPointerException
     *             thrown if given array is null
     * @throws IllegalArgumentException
     *             thrown if start position is illegal
     * @throws OutOfBoundsException
     *             thrown if start position out of bounds
     */
    public long toShortArray(final short[] dest, ArrayPos startPos) {
        startPos = Objects.requireNonNull(startPos);
        return toShortArray(dest, startPos.index, startPos.offset);
    }

    /**
     * Converts this block to a char array. If length of this block is not
     * multiple of char, the rest space of last byte will fill 0 in the tail.
     *
     * @return array converted from this block
     */
    public char[] toCharArray() {
        final int arrayLength = lengthInBits() % Character.SIZE == 0 ? lengthInBytes()
                : lengthInBytes() + 1;
        final char[] array = new char[arrayLength];
        base.toCharArray(array, 0, 0);
        return array;
    }

    /**
     * Puts data of this block into special char array. The data will be put
     * until reach to end of array or block. Actual put data number in bits will
     * be returned.
     *
     * @param dest
     *            special array, not null
     * @return actual put data number in bits
     * @throws NullPointerException
     *             thrown if given array is null
     */
    public long toCharArray(final char[] dest) {
        return base.toCharArray(Objects.requireNonNull(dest), 0, 0);
    }

    /**
     * Puts data of this block into special char array start from special
     * position. The data will be put until reach to end of array or block.
     * Actual put data number in bits will be returned.
     *
     * @param dest
     *            special array, not null
     * @param startIndex
     *            start index in bounds
     * @param startOffset
     *            bit offset of start index, [0, 7]
     * @return actual put data number in bits
     * @throws NullPointerException
     *             thrown if given array is null
     * @throws IllegalArgumentException
     *             thrown if start position is illegal
     * @throws OutOfBoundsException
     *             thrown if start position out of bounds
     */
    public long toCharArray(char[] dest, final int startIndex,
            final int startOffset) {
        dest = Objects.requireNonNull(dest);
        checkIndexLegality(startIndex);
        checkIndexInBounds(dest.length, startIndex);
        checkBitsInPrimitiveBounds(startOffset, Character.SIZE);
        return base.toCharArray(dest, startIndex, startOffset);
    }

    /**
     * Puts data of this block into special char array start from special
     * position. The data will be put until reach to end of array or block.
     * Actual put data number in bits will be returned.
     *
     * @param dest
     *            special array, not null
     * @param startPos
     *            start position in bounds, not null
     * @return actual put data number in bits
     * @throws NullPointerException
     *             thrown if given array is null
     * @throws IllegalArgumentException
     *             thrown if start position is illegal
     * @throws OutOfBoundsException
     *             thrown if start position out of bounds
     */
    public long toCharArray(final char[] dest, ArrayPos startPos) {
        startPos = Objects.requireNonNull(startPos);
        return toCharArray(dest, startPos.index, startPos.offset);
    }

    /**
     * Converts this block to a int array. If length of this block is not
     * multiple of int, the rest space of last byte will fill 0 in the tail.
     *
     * @return array converted from this block
     */
    public int[] toIntArray() {
        final int arrayLength = lengthInBits() % Integer.SIZE == 0 ? lengthInBytes()
                : lengthInBytes() + 1;
        final int[] array = new int[arrayLength];
        base.toIntArray(array, 0, 0);
        return array;
    }

    /**
     * Puts data of this block into special int array. The data will be put
     * until reach to end of array or block. Actual put data number in bits will
     * be returned.
     *
     * @param dest
     *            special array, not null
     * @return actual put data number in bits
     * @throws NullPointerException
     *             thrown if given array is null
     */
    public long toIntArray(final int[] dest) {
        return base.toIntArray(Objects.requireNonNull(dest), 0, 0);
    }

    /**
     * Puts data of this block into special int array start from special
     * position. The data will be put until reach to end of array or block.
     * Actual put data number in bits will be returned.
     *
     * @param dest
     *            special array, not null
     * @param startIndex
     *            start index in bounds
     * @param startOffset
     *            bit offset of start index, [0, 7]
     * @return actual put data number in bits
     * @throws NullPointerException
     *             thrown if given array is null
     * @throws IllegalArgumentException
     *             thrown if start position is illegal
     * @throws OutOfBoundsException
     *             thrown if start position out of bounds
     */
    public long toIntArray(int[] dest, final int startIndex,
            final int startOffset) {
        dest = Objects.requireNonNull(dest);
        checkIndexLegality(startIndex);
        checkIndexInBounds(dest.length, startIndex);
        checkBitsInPrimitiveBounds(startOffset, Integer.SIZE);
        return base.toIntArray(dest, startIndex, startOffset);
    }

    /**
     * Puts data of this block into special int array start from special
     * position. The data will be put until reach to end of array or block.
     * Actual put data number in bits will be returned.
     *
     * @param dest
     *            special array, not null
     * @param startPos
     *            start position in bounds, not null
     * @return actual put data number in bits
     * @throws NullPointerException
     *             thrown if given array is null
     * @throws IllegalArgumentException
     *             thrown if start position is illegal
     * @throws OutOfBoundsException
     *             thrown if start position out of bounds
     */
    public long toIntArray(final int[] dest, ArrayPos startPos) {
        startPos = Objects.requireNonNull(startPos);
        return toIntArray(dest, startPos.index, startPos.offset);
    }

    /**
     * Converts this block to a float array. If length of this block is not
     * multiple of float, the rest space of last byte will fill 0 in the tail.
     *
     * @return array converted from this block
     */
    public float[] toFloatArray() {
        final int arrayLength = lengthInBits() % Float.SIZE == 0 ? lengthInBytes()
                : lengthInBytes() + 1;
        final float[] array = new float[arrayLength];
        base.toFloatArray(array, 0, 0);
        return array;
    }

    /**
     * Puts data of this block into special float array. The data will be put
     * until reach to end of array or block. Actual put data number in bits will
     * be returned.
     *
     * @param dest
     *            special array, not null
     * @return actual put data number in bits
     * @throws NullPointerException
     *             thrown if given array is null
     */
    public long toFloatArray(final float[] dest) {
        return base.toFloatArray(Objects.requireNonNull(dest), 0, 0);
    }

    /**
     * Puts data of this block into special float array start from special
     * position. The data will be put until reach to end of array or block.
     * Actual put data number in bits will be returned.
     *
     * @param dest
     *            special array, not null
     * @param startIndex
     *            start index in bounds
     * @param startOffset
     *            bit offset of start index, [0, 7]
     * @return actual put data number in bits
     * @throws NullPointerException
     *             thrown if given array is null
     * @throws IllegalArgumentException
     *             thrown if start position is illegal
     * @throws OutOfBoundsException
     *             thrown if start position out of bounds
     */
    public long toFloatArray(float[] dest, final int startIndex,
            final int startOffset) {
        dest = Objects.requireNonNull(dest);
        checkIndexLegality(startIndex);
        checkIndexInBounds(dest.length, startIndex);
        checkBitsInPrimitiveBounds(startOffset, Float.SIZE);
        return base.toFloatArray(dest, startIndex, startOffset);
    }

    /**
     * Puts data of this block into special float array start from special
     * position. The data will be put until reach to end of array or block.
     * Actual put data number in bits will be returned.
     *
     * @param dest
     *            special array, not null
     * @param startPos
     *            start position in bounds, not null
     * @return actual put data number in bits
     * @throws NullPointerException
     *             thrown if given array is null
     * @throws IllegalArgumentException
     *             thrown if start position is illegal
     * @throws OutOfBoundsException
     *             thrown if start position out of bounds
     */
    public long toFloatArray(final float[] dest, ArrayPos startPos) {
        startPos = Objects.requireNonNull(startPos);
        return toFloatArray(dest, startPos.index, startPos.offset);
    }

    /**
     * Converts this block to a long array. If length of this block is not
     * multiple of long, the rest space of last byte will fill 0 in the tail.
     *
     * @return array converted from this block
     */
    public long[] toLongArray() {
        final int arrayLength = lengthInBits() % Long.SIZE == 0 ? lengthInBytes()
                : lengthInBytes() + 1;
        final long[] array = new long[arrayLength];
        base.toLongArray(array, 0, 0);
        return array;
    }

    /**
     * Puts data of this block into special long array. The data will be put
     * until reach to end of array or block. Actual put data number in bits will
     * be returned.
     *
     * @param dest
     *            special array, not null
     * @return actual put data number in bits
     * @throws NullPointerException
     *             thrown if given array is null
     */
    public long toLongArray(final long[] dest) {
        return base.toLongArray(Objects.requireNonNull(dest), 0, 0);
    }

    /**
     * Puts data of this block into special long array start from special
     * position. The data will be put until reach to end of array or block.
     * Actual put data number in bits will be returned.
     *
     * @param dest
     *            special array, not null
     * @param startIndex
     *            start index in bounds
     * @param startOffset
     *            bit offset of start index, [0, 7]
     * @return actual put data number in bits
     * @throws NullPointerException
     *             thrown if given array is null
     * @throws IllegalArgumentException
     *             thrown if start position is illegal
     * @throws OutOfBoundsException
     *             thrown if start position out of bounds
     */
    public long toLongArray(long[] dest, final int startIndex,
            final int startOffset) {
        dest = Objects.requireNonNull(dest);
        checkIndexLegality(startIndex);
        checkIndexInBounds(dest.length, startIndex);
        checkBitsInPrimitiveBounds(startOffset, Long.SIZE);
        return base.toLongArray(dest, startIndex, startOffset);
    }

    /**
     * Puts data of this block into special byte array start from special
     * position. The data will be put until reach to end of array or block.
     * Actual put data number in bits will be returned.
     *
     * @param dest
     *            special array, not null
     * @param startPos
     *            start position in bounds, not null
     * @return actual put data number in bits
     * @throws NullPointerException
     *             thrown if given array is null
     * @throws IllegalArgumentException
     *             thrown if start position is illegal
     * @throws OutOfBoundsException
     *             thrown if start position out of bounds
     */
    public long toLongArray(final long[] dest, ArrayPos startPos) {
        startPos = Objects.requireNonNull(startPos);
        return toLongArray(dest, startPos.index, startPos.offset);
    }

    /**
     * Converts this block to a double array. If length of this block is not
     * multiple of double, the rest space of last byte will fill 0 in the tail.
     *
     * @return array converted from this block
     */
    public double[] toDoubleArray() {
        final int arrayLength = lengthInBits() % Double.SIZE == 0 ? lengthInBytes()
                : lengthInBytes() + 1;
        final double[] array = new double[arrayLength];
        base.toDoubleArray(array, 0, 0);
        return array;
    }

    /**
     * Puts data of this block into special double array. The data will be put
     * until reach to end of array or block. Actual put data number in bits will
     * be returned.
     *
     * @param dest
     *            special array, not null
     * @return actual put data number in bits
     * @throws NullPointerException
     *             thrown if given array is null
     */
    public long toDoubleArray(final double[] dest) {
        return base.toDoubleArray(Objects.requireNonNull(dest), 0, 0);
    }

    /**
     * Puts data of this block into special double array start from special
     * position. The data will be put until reach to end of array or block.
     * Actual put data number in bits will be returned.
     *
     * @param dest
     *            special array, not null
     * @param startIndex
     *            start index in bounds
     * @param startOffset
     *            bit offset of start index, [0, 7]
     * @return actual put data number in bits
     * @throws NullPointerException
     *             thrown if given array is null
     * @throws IllegalArgumentException
     *             thrown if start position is illegal
     * @throws OutOfBoundsException
     *             thrown if start position out of bounds
     */
    public long toDoubleArray(double[] dest, final int startIndex,
            final int startOffset) {
        dest = Objects.requireNonNull(dest);
        checkIndexLegality(startIndex);
        checkIndexInBounds(dest.length, startIndex);
        checkBitsInPrimitiveBounds(startOffset, Double.SIZE);
        return base.toDoubleArray(dest, startIndex, startOffset);
    }

    /**
     * Puts data of this block into special double array start from special
     * position. The data will be put until reach to end of array or block.
     * Actual put data number in bits will be returned.
     *
     * @param dest
     *            special array, not null
     * @param startPos
     *            start position in bounds, not null
     * @return actual put data number in bits
     * @throws NullPointerException
     *             thrown if given array is null
     * @throws IllegalArgumentException
     *             thrown if start position is illegal
     * @throws OutOfBoundsException
     *             thrown if start position out of bounds
     */
    public long toDoubleArray(final double[] dest, ArrayPos startPos) {
        startPos = Objects.requireNonNull(startPos);
        return toDoubleArray(dest, startPos.index, startPos.offset);
    }

    /**
     * Returns whether this block's length is multiple of byte's length in bits
     * and not empty.
     *
     * @return whether this block's length is multiple of byte's length in bits
     *         and not empty
     */
    public boolean isMultipleOfByte() {
        return lengthInBits() % Byte.SIZE == 0 && lengthInBits() != 0;
    }

    /**
     * Returns whether this block's length is multiple of short's length in bits
     * and not empty.
     *
     * @return whether this block's length is multiple of short's length in bits
     *         and not empty
     */
    public boolean isMultipleOfShort() {
        return lengthInBits() % Short.SIZE == 0 && lengthInBits() != 0;
    }

    /**
     * Returns whether this block's length is multiple of char's length in bits
     * and not empty.
     *
     * @return whether this block's length is multiple of char's length in bits
     *         and not empty
     */
    public boolean isMultipleOfChar() {
        return lengthInBits() % Character.SIZE == 0 && lengthInBits() != 0;
    }

    /**
     * Returns whether this block's length is multiple of int's length in bits
     * and not empty.
     *
     * @return whether this block's length is multiple of int's length in bits
     *         and not empty
     */
    public boolean isMultipleOfInt() {
        return lengthInBits() % Integer.SIZE == 0 && lengthInBits() != 0;
    }

    /**
     * Returns whether this block's length is multiple of float's length in bits
     * and not empty.
     *
     * @return whether this block's length is multiple of float's length in bits
     *         and not empty
     */
    public boolean isMultipleOfFloat() {
        return lengthInBits() % Float.SIZE == 0 && lengthInBits() != 0;
    }

    /**
     * Returns whether this block's length is multiple of long's length in bits
     * and not empty.
     *
     * @return whether this block's length is multiple of long's length in bits
     *         and not empty
     */
    public boolean isMultipleOfLong() {
        return lengthInBits() % Long.SIZE == 0 && lengthInBits() != 0;
    }

    /**
     * Returns whether this block's length is multiple of double's length in
     * bits and not empty.
     *
     * @return whether this block's length is multiple of double's length in
     *         bits and not empty
     */
    public boolean isMultipleOfDouble() {
        return lengthInBits() % Double.SIZE == 0 && lengthInBits() != 0;
    }

    /**
     * Gets promoted length which is multiple of byte in bits, if this block's
     * length has already be multiple of byte, return its length. For example,
     * if this block's length is 7 bits, it will be promoted to 8 bits.
     * <p>
     * Default not empty.
     *
     * @return promoted length which is multiple of byte in bits
     */
    private long promoteLengthMultipleOfByte() {
        if (isMultipleOfByte()) {
            return lengthInBits();
        } else {
            return lengthInBits() / Byte.SIZE * Byte.SIZE + Byte.SIZE;
        }
    }

    /**
     * Converts this data block to {@linkplain BigInteger}, the first bit is
     * sign bit. If this block is empty, an {@linkplain OutOfBoundsException}
     * will be thrown.
     *
     * @return {@linkplain BigInteger} of this block's binary content
     * @throws OutOfBoundsException
     *             thrown if block is empty
     */
    public BigInteger toBigInteger() {
        checkEmpty();
        final Bitsss data = new Bitsss(promoteLengthMultipleOfByte());
        copy(data);
        data.arithmeticRight(data.lengthInBits() - lengthInBits());
        return new BigInteger(data.toByteArray());
    }

    /**
     * Converts this data block to unsigned {@linkplain BigInteger}. If this
     * block is empty, an {@linkplain OutOfBoundsException} will be thrown.
     *
     * @return unsigned {@linkplain BigInteger} of this block's binary content
     * @throws OutOfBoundsException
     *             thrown if block is empty
     */
    public BigInteger toUnsignedBigInteger() {
        checkEmpty();
        long len;
        if (isMultipleOfByte()) {
            len = lengthInBits();
            if (readBoolean(0L)) {
                len += 8L;
            }
        } else {
            len = promoteLengthMultipleOfByte();
        }
        final Bitsss data = new Bitsss(len);
        copy(data);
        data.logicalRight(data.lengthInBits() - lengthInBits());
        return new BigInteger(data.toByteArray());
    }

    /**
     * Converts this data block to {@linkplain BigDecimal} with special scale
     * and rounding according to the context settings. If this block is empty,
     * an {@linkplain OutOfBoundsException} will be thrown.
     *
     * @param scale
     *            scale of the {@linkplain BigDecimal}
     * @param mc
     *            the context to use, not null
     * @return {@linkplain BigDecimal} of this block's binary content
     * @throws OutOfBoundsException
     *             thrown if block is empty
     * @throws NullPointerException
     *             thrown if context is null
     * @throws ArithmeticException
     *             thrown if the result is inexact but the rounding mode is
     *             UNNECESSARY, or other arithmetic error occur
     */
    public BigDecimal toBigDecimal(final int scale, final MathContext mc) {
        return new BigDecimal(toBigInteger(), scale, mc);
    }

    /**
     * Converts this data block to {@linkplain BigDecimal} with special scale.
     * If this block is empty, an {@linkplain OutOfBoundsException} will be
     * thrown.
     *
     * @param scale
     *            scale of the {@linkplain BigDecimal}
     * @return {@linkplain BigDecimal} of this block's binary content
     * @throws OutOfBoundsException
     *             thrown if block is empty
     * @throws ArithmeticException
     *             thrown if arithmetic error occur
     */
    public BigDecimal toBigDecimal(final int scale) {
        return new BigDecimal(toBigInteger(), scale);
    }

    /**
     * Converts this data block to {@linkplain BigDecimal}. If this block is
     * empty, an {@linkplain OutOfBoundsException} will be thrown.
     *
     * @return {@linkplain BigDecimal} of this block's binary content
     * @throws OutOfBoundsException
     *             thrown if block is empty
     */
    public BigDecimal toBigDecimal() {
        return new BigDecimal(toBigInteger());
    }

    /**
     * Converts this data block to unsigned {@linkplain BigDecimal} with special
     * scale and rounding according to the context settings. If this block is
     * empty, an {@linkplain OutOfBoundsException} will be thrown.
     *
     * @param scale
     *            scale of the {@linkplain BigDecimal}
     * @param mc
     *            the context to use, not null
     * @return unsigned {@linkplain BigDecimal} of this block's binary content
     * @throws OutOfBoundsException
     *             thrown if block is empty
     * @throws NullPointerException
     *             thrown if context is null
     * @throws ArithmeticException
     *             thrown if the result is inexact but the rounding mode is
     *             UNNECESSARY, or other arithmetic error occur
     */
    public BigDecimal toUnsignedBigDecimal(final int scale, final MathContext mc) {
        return new BigDecimal(toUnsignedBigInteger(), scale, mc);
    }

    /**
     * Converts this data block to unsigned {@linkplain BigDecimal} with special
     * scale. If this block is empty, an {@linkplain OutOfBoundsException} will
     * be thrown.
     *
     * @param scale
     *            scale of the {@linkplain BigDecimal}
     * @return unsigned {@linkplain BigDecimal} of this block's binary content
     * @throws OutOfBoundsException
     *             thrown if block is empty
     * @throws ArithmeticException
     *             thrown if arithmetic error occur
     */
    public BigDecimal toUnsignedBigDecimal(final int scale) {
        return new BigDecimal(toUnsignedBigInteger(), scale);
    }

    /**
     * Converts this data block to unsigned {@linkplain BigDecimal}. If this
     * block is empty, an {@linkplain OutOfBoundsException} will be thrown.
     *
     * @return unsigned {@linkplain BigDecimal} of this block's binary content
     * @throws OutOfBoundsException
     *             thrown if block is empty
     */
    public BigDecimal toUnsignedBigDecimal() {
        return new BigDecimal(toUnsignedBigInteger());
    }

    /**
     * Set all bits of this block to special value ,true is 1, false is 0.
     *
     * @param value
     *            special value
     */
    public void setBits(final boolean value) {
        base.setBits(value);
    }

    /**
     * Copies data of this block to special dest block, the copied length is
     * indicated by shorter one of two block, the actual number of bits copied
     * will be returned. This method copies data from head to tail.
     *
     * @param dest
     *            special dest block, not null
     * @return actual number of bits copied
     * @throws NullPointerException
     *             thrown if dest block is null
     */
    public long copy(final Bitsss dest) {
        return base.copyTo(Objects.requireNonNull(dest).base);
    }

    /**
     * And operation between this block and target block like:
     *
     * <pre>
     * block = block &amp; target;
     * </pre>
     *
     * The operation length is indicated by shorter one of two block, the actual
     * number of bits manipulated will be returned.
     *
     * @param target
     *            target block, not null
     * @return actual number of bits manipulated
     * @throws NullPointerException
     *             thrown if dest block is null
     */
    public long and(final Bitsss target) {
        return base.and(target.base);
    }

    /**
     * Or operation between this block and target block like:
     *
     * <pre>
     * block = block | target;
     * </pre>
     *
     * The operation length is indicated by shorter one of two block, the actual
     * number of bits manipulated will be returned.
     *
     * @param target
     *            target block, not null
     * @return actual number of bits manipulated
     * @throws NullPointerException
     *             thrown if dest block is null
     */
    public long or(final Bitsss target) {
        return base.or(target.base);
    }

    /**
     * Xor operation between this block and target block like:
     *
     * <pre>
     * block = block &circ; target;
     * </pre>
     *
     * The operation length is indicated by shorter one of two block, the actual
     * number of bits manipulated will be returned.
     *
     * @param target
     *            target block, not null
     * @return actual number of bits manipulated
     * @throws NullPointerException
     *             thrown if dest block is null
     */
    public long xor(final Bitsss target) {
        return base.xor(target.base);
    }

    /**
     * Not operation for this block like:
     *
     * <pre>
     * block = ^block;
     * </pre>
     *
     */
    public void not() {
        base.not();
    }

    /**
     * Reverses this block in bits.
     *
     */
    public void reverse() {
        base.reverse();
    }

    /**
     * Reverses this block in bytes. If the length of this block is not multiple
     * of bytes, do nothing.
     */
    public void reverseInBytes() {
        base.reverseInBytes();
    }

    /**
     * Logical shift left bits of special number like:
     *
     * <pre>
     * block = block &lt;&lt; bits;
     * </pre>
     *
     * The special number should be in [1, length - 1], if out of bounds, do
     * nothing.
     * <p>
     * This method is same with {@linkplain #arithmeticLeft(long)}.
     *
     * @param bits
     *            special number of shifted bits, [1, length - 1]
     * @see #arithmeticLeft(long)
     */
    public void logicalLeft(final long bits) {
        if (bits < 1 || bits >= lengthInBits()) {
            return;
        }
        base.logicalLeft(bits);
    }

    /**
     * Logical shift right bits of special number like:
     *
     * <pre>
     * block = block &gt;&gt;&gt; bits;
     * </pre>
     *
     * The special number should be in [1, length - 1], if out of bounds, do
     * nothing.
     *
     * @param bits
     *            special number of shifted bits, [1, length - 1]
     */
    public void logicalRight(final long bits) {
        if (bits < 1 || bits >= lengthInBits()) {
            return;
        }
        base.logicalRight(bits);
    }

    /**
     * Arithmetic shift left bits of special number like:
     *
     * <pre>
     * block = block &lt;&lt; bits;
     * </pre>
     *
     * The special number should be in [1, length - 1], if out of bounds, do
     * nothing.
     * <p>
     * This method is same with {@linkplain #logicalLeft(long)}.
     *
     * @param bits
     *            special number of shifted bits, [1, length - 1]
     * @see #logicalLeft(long)
     */
    public void arithmeticLeft(final long bits) {
        if (bits < 1 || bits >= lengthInBits()) {
            return;
        }
        base.arithmeticLeft(bits);
    }

    /**
     * Arithmetic shift right bits of special number like:
     *
     * <pre>
     * block = block &gt;&gt; bits;
     * </pre>
     *
     * The special number should be in [1, length - 1], if out of bounds, do
     * nothing.
     *
     * @param bits
     *            special number of shifted bits, [1, length - 1]
     */
    public void arithmeticRight(final long bits) {
        if (bits < 1 || bits >= lengthInBits()) {
            return;
        }
        base.arithmeticRight(bits);
    }

    /**
     * Rotates left bits of special number like:
     *
     * <pre>
     * ROL AL, bits
     * </pre>
     *
     * The special number should be in [1, length - 1], if out of bounds, do
     * nothing.
     *
     * @param bits
     *            number of rotated bits
     */
    public void rotateLeft(final long bits) {
        if (bits < 1 || bits >= lengthInBits()) {
            return;
        }
        base.rotateLeft(bits);
    }

    /**
     * Rotates right bits of special number like:
     *
     * <pre>
     * ROR AL, bits
     * </pre>
     *
     * The special number should be in [1, length - 1], if out of bounds, do
     * nothing.
     *
     * @param bits
     *            number of rotated bits
     */
    public void rotateRight(final long bits) {
        if (bits < 1 || bits >= lengthInBits()) {
            return;
        }
        base.rotateRight(bits);
    }

    /**
     * Represents an operation that accepts a boolean value and a serial number
     * to operate, no return.
     *
     * @author Fred Suvn
     * @version 0.0.0, 2015-06-19 15:52:08
     */
    @FunctionalInterface
    private static interface BlockBooleanLongConsumer {

        /**
         * Performs this operation on the given boolean, given serial is serial
         * number of total block.
         *
         * @param value
         *            given boolean
         * @param serial
         *            serial number of total block
         */
        void accept(boolean value, long serial);
    }

    /**
     * Represents an operation that accepts a byte value and a serial number to
     * operate, no return.
     *
     * @author Fred Suvn
     * @version 0.0.0, 2015-06-19 15:52:08
     */
    @FunctionalInterface
    private static interface BlockByteIntConsumer {

        /**
         * Performs this operation on the given byte, given serial is serial
         * number of total block.
         *
         * @param value
         *            given byte
         * @param serial
         *            serial number of total block
         */
        void accept(byte value, int serial);
    }

    /**
     * Represents an operation that accepts a short value and a serial number to
     * operate, no return.
     *
     * @author Fred Suvn
     * @version 0.0.0, 2015-06-19 15:52:08
     */
    @FunctionalInterface
    private static interface BlockShortIntConsumer {

        /**
         * Performs this operation on the given short, given serial is serial
         * number of total block.
         *
         * @param value
         *            given short
         * @param serial
         *            serial number of total block
         */
        void accept(short value, int serial);
    }

    /**
     * Represents an operation that accepts a char value and a serial number to
     * operate, no return.
     *
     * @author Fred Suvn
     * @version 0.0.0, 2015-06-19 15:52:08
     */
    @FunctionalInterface
    private static interface BlockCharIntConsumer {

        /**
         * Performs this operation on the given char, given serial is serial
         * number of total block.
         *
         * @param value
         *            given char
         * @param serial
         *            serial number of total block
         */
        void accept(char value, int serial);
    }

    /**
     * Represents an operation that accepts a int value and a serial number to
     * operate, no return.
     *
     * @author Fred Suvn
     * @version 0.0.0, 2015-06-19 15:52:08
     */
    @FunctionalInterface
    private static interface BlockIntIntConsumer {

        /**
         * Performs this operation on the given int, given serial is serial
         * number of total block.
         *
         * @param value
         *            given int
         * @param serial
         *            serial number of total block
         */
        void accept(int value, int serial);
    }

    /**
     * Represents an operation that accepts a float value and a serial number to
     * operate, no return.
     *
     * @author Fred Suvn
     * @version 0.0.0, 2015-06-19 15:52:08
     */
    @FunctionalInterface
    private static interface BlockFloatIntConsumer {

        /**
         * Performs this operation on the given float, given serial is serial
         * number of total block.
         *
         * @param value
         *            given float
         * @param serial
         *            serial number of total block
         */
        void accept(float value, int serial);
    }

    /**
     * Represents an operation that accepts a long value and a serial number to
     * operate, no return.
     *
     * @author Fred Suvn
     * @version 0.0.0, 2015-06-19 15:52:08
     */
    @FunctionalInterface
    private static interface BlockLongIntConsumer {

        /**
         * Performs this operation on the given long, given serial is serial
         * number of total block.
         *
         * @param value
         *            given long
         * @param serial
         *            serial number of total block
         */
        void accept(long value, int serial);
    }

    /**
     * Represents an operation that accepts a double value and a serial number
     * to operate, no return.
     *
     * @author Fred Suvn
     * @version 0.0.0, 2015-06-19 15:52:08
     */
    @FunctionalInterface
    private static interface BlockDoubleIntConsumer {

        /**
         * Performs this operation on the given double, given serial is serial
         * number of total block.
         *
         * @param value
         *            given double
         * @param serial
         *            serial number of total block
         */
        void accept(double value, int serial);
    }

    /**
     * Operates for each bit of block.
     *
     * @param consumer
     *            operation, first parameter is bit value, 1 is true, 0 is
     *            false, second parameter is serial number, not null
     * @throws NullPointerException
     *             thrown if consumer is null
     */
    public void forEachBoolean(final BlockBooleanLongConsumer consumer) {
        if (isEmpty()) {
            return;
        }
        for (long s = 0; s < lengthInBits(); s++) {
            consumer.accept(readBoolean(s), s);
        }
    }

    /**
     * Operates for each byte of block, if length of block is not multiple of
     * byte, the last bits that not enough to byte will be put into last
     * operation align to right and filled 0 in the head of byte.
     *
     * @param consumer
     *            operation, first parameter is byte value, second parameter is
     *            serial number, not null
     * @throws NullPointerException
     *             thrown if consumer is null
     */
    public void forEachByte(final BlockByteIntConsumer consumer) {
        if (isEmpty()) {
            return;
        }
        final int restBits = (int) (lengthInBits() % Byte.SIZE);
        int s = 0;
        for (long p = 0; p < lengthInBits() - restBits; p += Byte.SIZE, s++) {
            consumer.accept(readByte(p), s);
        }
        if (restBits > 0) {
            consumer.accept(readLastByte(lengthInBits() - restBits), s);
        }
    }

    /**
     * Operates for each short of block, if length of block is not multiple of
     * short, the last bits that not enough to short will be put into last
     * operation align to right and filled 0 in the head of short.
     *
     * @param consumer
     *            operation, first parameter is short value, second parameter is
     *            serial number, not null
     * @throws NullPointerException
     *             thrown if consumer is null
     */
    public void forEachShort(final BlockShortIntConsumer consumer) {
        if (isEmpty()) {
            return;
        }
        final int restBits = (int) (lengthInBits() % Short.SIZE);
        int s = 0;
        for (long p = 0; p < lengthInBits() - restBits; p += Short.SIZE, s++) {
            consumer.accept(readShort(p), s);
        }
        if (restBits > 0) {
            consumer.accept(readLastShort(lengthInBits() - restBits), s);
        }
    }

    /**
     * Operates for each char of block, if length of block is not multiple of
     * char, the last bits that not enough to char will be put into last
     * operation align to right and filled 0 in the head of char.
     *
     * @param consumer
     *            operation, first parameter is char value, second parameter is
     *            serial number, not null
     * @throws NullPointerException
     *             thrown if consumer is null
     */
    public void forEachChar(final BlockCharIntConsumer consumer) {
        if (isEmpty()) {
            return;
        }
        final int restBits = (int) (lengthInBits() % Character.SIZE);
        int s = 0;
        for (long p = 0; p < lengthInBits() - restBits; p += Character.SIZE, s++) {
            consumer.accept(readChar(p), s);
        }
        if (restBits > 0) {
            consumer.accept(readLastChar(lengthInBits() - restBits), s);
        }
    }

    /**
     * Operates for each int of block, if length of block is not multiple of
     * int, the last bits that not enough to int will be put into last operation
     * align to right and filled 0 in the head of int.
     *
     * @param consumer
     *            operation, first parameter is int value, second parameter is
     *            serial number, not null
     * @throws NullPointerException
     *             thrown if consumer is null
     */
    public void forEachInt(final BlockIntIntConsumer consumer) {
        if (isEmpty()) {
            return;
        }
        final int restBits = (int) (lengthInBits() % Integer.SIZE);
        int s = 0;
        for (long p = 0; p < lengthInBits() - restBits; p += Integer.SIZE, s++) {
            consumer.accept(readInt(p), s);
        }
        if (restBits > 0) {
            consumer.accept(readLastInt(lengthInBits() - restBits), s);
        }
    }

    /**
     * Operates for each float of block, if length of block is not multiple of
     * float, the last bits that not enough to float will be put into last
     * operation align to right and filled 0 in the head of float.
     *
     * @param consumer
     *            operation, first parameter is float value, second parameter is
     *            serial number, not null
     * @throws NullPointerException
     *             thrown if consumer is null
     */
    public void forEachFloat(final BlockFloatIntConsumer consumer) {
        if (isEmpty()) {
            return;
        }
        final int restBits = (int) (lengthInBits() % Float.SIZE);
        int s = 0;
        for (long p = 0; p < lengthInBits() - restBits; p += Float.SIZE, s++) {
            consumer.accept(readFloat(p), s);
        }
        if (restBits > 0) {
            consumer.accept(readLastFloat(lengthInBits() - restBits), s);
        }
    }

    /**
     * Operates for each long of block, if length of block is not multiple of
     * long, the last bits that not enough to long will be put into last
     * operation align to right and filled 0 in the head of long.
     *
     * @param consumer
     *            operation, first parameter is long value, second parameter is
     *            serial number, not null
     * @throws NullPointerException
     *             thrown if consumer is null
     */
    public void forEachLong(final BlockLongIntConsumer consumer) {
        if (isEmpty()) {
            return;
        }
        final int restBits = (int) (lengthInBits() % Long.SIZE);
        int s = 0;
        for (long p = 0; p < lengthInBits() - restBits; p += Long.SIZE, s++) {
            consumer.accept(readLong(p), s);
        }
        if (restBits > 0) {
            consumer.accept(readLastLong(lengthInBits() - restBits), s);
        }
    }

    /**
     * Operates for each double of block, if length of block is not multiple of
     * double, the last bits that not enough to double will be put into last
     * operation align to right and filled 0 in the head of double.
     *
     * @param consumer
     *            operation, first parameter is double value, second parameter
     *            is serial number, not null
     * @throws NullPointerException
     *             thrown if consumer is null
     */
    public void forEachDouble(final BlockDoubleIntConsumer consumer) {
        if (isEmpty()) {
            return;
        }
        final int restBits = (int) (lengthInBits() % Double.SIZE);
        int s = 0;
        for (long p = 0; p < lengthInBits() - restBits; p += Double.SIZE, s++) {
            consumer.accept(readDouble(p), s);
        }
        if (restBits > 0) {
            consumer.accept(readLastDouble(lengthInBits() - restBits), s);
        }
    }

    /**
     * Operates for each part of special length in bits of block, the last
     * block's length may be not enough to special length if total length is not
     * multiple of special length.
     *
     * @param length
     *            special length in bits, positive
     * @param consumer
     *            operation, not null
     * @throws IllegalArgumentException
     *             thrown if special length in bits is illegal
     * @throws NullPointerException
     *             thrown if consumer is null
     */
    public void forEach(final long length, final ObjLongConsumer<Bitsss> consumer) {
        if (isEmpty()) {
            return;
        }
        checkPosOrLengthLegality(length);
        if (length == 0) {
            throw new IllegalArgumentException("Length cannot be 0!");
        }
        final long restBits = (int) (lengthInBits() % length);
        long s = 0;
        for (long p = 0; p < lengthInBits() - restBits; p += length, s++) {
            consumer.accept(partOf(p, length), s);
        }
        if (restBits > 0) {
            consumer.accept(partOf(lengthInBits() - restBits, restBits), s);
        }
    }

    /**
     * Returns a block array split from this block block in special length in
     * bits, that is, each block of element of array's length is special length.
     * If the total length of this block is not multiple of special length, the
     * last block of array's length is rest length in bits rather than special
     * length. If array's element's count is more than max of array's element's
     * count, it will only return array of top max count part.
     *
     * @param length
     *            special length in bits, positive
     * @return a block array split from this block block in special length in
     *         bits
     * @throws IllegalArgumentException
     *             thrown if special length is illegal
     */
    public Bitsss[] split(final long length) {
        if (isEmpty()) {
            return new Bitsss[0];
        }
        checkPosOrLengthLegality(length);
        if (length == 0) {
            throw new IllegalArgumentException("Length cannot be 0!");
        }
        final long count = lengthInBits() / length;
        final long restBits = lengthInBits() % length;
        Bitsss[] dbs;
        if (restBits > 0) {
            dbs = new Bitsss[count >= Integer.MAX_VALUE ? Integer.MAX_VALUE
                    : (int) (count + 1)];
        } else {
            dbs = new Bitsss[count > Integer.MAX_VALUE ? Integer.MAX_VALUE
                    : (int) count];
        }
        forEach(length, (b, s) -> {
            if (s < dbs.length) {
                dbs[(int) s] = b;
            }
        });
        return dbs;
    }

    /**
     * Returns a block array split around matches of given separator block, the
     * matches use {@linkplain #equals(Object)} method. For example, if source
     * block is 0xfe01cca0100fe: <blockquote>
     * <table cellpadding="1" cellspacing="0" summary="Split example showing separator, limit, and result">
     * <tr>
     * <th>Given Block</th>
     * <th>Result</th>
     * </tr>
     * <tr>
     * <td align=center>0x01</td>
     * <td>{@code 0xfe, 0xcca, 0x00fe }</td>
     * </tr>
     * <tr>
     * <td align=center>0xfe</td>
     * <td>{@code 0x01cca0100 }</td>
     * </tr>
     * </table>
     * </blockquote>
     *
     * @param separator
     *            given separator block, not null or empty
     * @return a block array split around matches of given block
     * @throws NullPointerException
     *             thrown if given block is null
     */
    public Bitsss[] split(Bitsss separator) {
        if (isEmpty()) {
            return new Bitsss[0];
        }
        separator = Objects.requireNonNull(separator);
        if (separator.isEmpty()) {
            throw new IllegalArgumentException("Given block cannot be empty!");
        }
        final List<Bitsss> list = new ArrayList<>();
        for (long l = 0L, m = 0L; l < this.base.lengthInBits();) {
            if (remainderLength(l) < separator.base.lengthInBits()) {
                list.add(partOf(m, this.base.lengthInBits() - m));
                break;
            }
            final Bits b = this.base.partOf(l,
                    separator.base.lengthInBits());
            if (separator.base.equals(b)) {
                list.add(partOf(m, l - m));
                l += separator.base.lengthInBits();
                m = l;
                continue;
            } else {
                l++;
            }
        }
        return list.toArray(new Bitsss[list.size()]);
    }

    /**
     * Returns a {@linkplain Stream} of which each element is part of this block
     * of special length in bits. If the total length of this block is not
     * multiple of special length, the last block's length is rest length in
     * bits rather than special length.
     *
     * @param length
     *            special length in bits, positive
     * @return a {@linkplain Stream} of which each element is part of this block
     *         of special length in bits
     * @throws IllegalArgumentException
     *             thrown if special length is illegal
     */
    public Stream<Bitsss> stream(final long length) {
        if (isEmpty()) {
            return Stream.generate(() -> {
                return this;
            }).limit(1L);
        }
        checkPosOrLengthLegality(length);
        if (length == 0) {
            throw new IllegalArgumentException("Length cannot be 0!");
        }
        final long count = lengthInBits() / length;
        final long restBits = lengthInBits() % length;
        long maxSize;
        if (restBits > 0) {
            maxSize = count + 1;
        } else {
            maxSize = count;
        }
        final Counter c = new Counter();
        return Stream.generate(() -> {
            Bitsss db;
            if (c.getCounter() < count) {
                db = partOf(c.getCounter() * length, length);
                c.increment();
                return db;
            } else if (restBits > 0) {
                return partOf(c.getCounter() * length, restBits);
            } else {
                return null; // Never execute!
            }
        }).limit(maxSize);
    }

    /**
     * Checks whether given value in special range.
     *
     * @param value
     *            given value
     * @param min
     *            min range inclusive
     * @param max
     *            max range inclusive
     */
    private void checkStreamRange(final int value, final int min, final int max) {
        if (value < min || value > max) {
            throw new IllegalArgumentException("Length should be in [" + min
                    + ", " + max + "]!");
        }
    }

    /**
     * Returns a {@linkplain IntStream} of which each element is an int type
     * which is part of this block of special length in bits, the bits in [1,
     * 32]. If the total length of this block is not multiple of special length,
     * the last int align rest bits to right and fill 0 in the head.
     * <p>
     * This block cannot be empty.
     *
     * @param length
     *            special length in bits, [1, 32]
     * @return a {@linkplain IntStream} of which each element is an int type
     *         which is part of this block of special length in bits, the bits
     *         in [1, 32]
     * @throws OutOfBoundsException
     *             thrown if this block is empty
     * @throws IllegalArgumentException
     *             thrown if special length is illegal
     */
    public IntStream intStream(final int length) {
        checkEmpty();
        checkStreamRange(length, 1, 32);
        final long count = lengthInBits() / length;
        final long restBits = lengthInBits() % length;
        long maxSize;
        if (restBits > 0) {
            maxSize = count + 1;
        } else {
            maxSize = count;
        }
        final Counter c = new Counter();
        return IntStream.generate(() -> {
            if (c.getCounter() < count) {
                final int i = readBitsAsInt(c.getCounter() * length, length);
                c.increment();
                return i;
            } else if (restBits > 0) {
                return readBitsAsInt(c.getCounter() * length, (int) restBits);
            } else {
                return 0; // Never execute!
            }
        }).limit(maxSize);
    }

    /**
     * Returns a {@linkplain LongStream} of which each element is a long type
     * which is part of this block of special length in bits, the bits in [1,
     * 64]. If the total length of this block is not multiple of special length,
     * the last long align rest bits to right and fill 0 in the head.
     * <p>
     * This block cannot be empty.
     *
     * @param length
     *            special length in bits, [1, 64]
     * @return a {@linkplain LongStream} of which each element is a long type
     *         which is part of this block of special length in bits, the bits
     *         in [1, 64]
     * @throws OutOfBoundsException
     *             thrown if this block is empty
     * @throws IllegalArgumentException
     *             thrown if special length is illegal
     */
    public LongStream longStream(final int length) {
        checkEmpty();
        checkStreamRange(length, 1, 64);
        final long count = lengthInBits() / length;
        final long restBits = lengthInBits() % length;
        long maxSize;
        if (restBits > 0) {
            maxSize = count + 1;
        } else {
            maxSize = count;
        }
        final Counter c = new Counter();
        return LongStream.generate(() -> {
            if (c.getCounter() < count) {
                final long l = readBitsAsLong(c.getCounter() * length, length);
                c.increment();
                return l;
            } else if (restBits > 0) {
                return readBitsAsLong(c.getCounter() * length, (int) restBits);
            } else {
                return 0L; // Never execute!
            }
        }).limit(maxSize);
    }

    /**
     * Returns a {@linkplain DoubleStream} of which each element is a double
     * type which is part of this block of special length in bits, the bits in
     * [1, 64]. If the total length of this block is not multiple of special
     * length, the last double align rest bits to right and fill 0 in the head.
     * <p>
     * This block cannot be empty.
     *
     * @param length
     *            special length in bits, [1, 64]
     * @return a {@linkplain DoubleStream} of which each element is a double
     *         type which is part of this block of special length in bits, the
     *         bits in [1, 64]
     * @throws OutOfBoundsException
     *             thrown if this block is empty
     * @throws IllegalArgumentException
     *             thrown if special length is illegal
     */
    public DoubleStream doubleStream(final int length) {
        checkEmpty();
        checkStreamRange(length, 1, 64);
        final long count = lengthInBits() / length;
        final long restBits = lengthInBits() % length;
        long maxSize;
        if (restBits > 0) {
            maxSize = count + 1;
        } else {
            maxSize = count;
        }
        final Counter c = new Counter();
        return DoubleStream.generate(
                () -> {
                    if (c.getCounter() < count) {
                        final double d = readBitsAsDouble(c.getCounter()
                                * length, length);
                        c.increment();
                        return d;
                    } else if (restBits > 0) {
                        return readBitsAsDouble(c.getCounter() * length,
                                (int) restBits);
                    } else {
                        return 0.0; // Never execute!
                    }
                }).limit(maxSize);
    }

    /**
     * Returns binary string in base 2 of this block like 0101010101.
     *
     * @return binary string in base 2 of this block
     */
    public String toBinaryString() {
        if (isEmpty()) {
            return "";
        }
        final StringBuilder sb = new StringBuilder();
        final int restBits = (int) (lengthInBits() % Long.SIZE);
        final char[] temp = new char[Long.SIZE];
        for (long p = 0; p < lengthInBits() - restBits; p += Long.SIZE) {
            final char[] sl = Long.toBinaryString(readLong(p)).toCharArray();
            Long.toHexString(readLong(p));
            int i = sl.length - 1;
            int j = temp.length - 1;
            for (; i >= 0; i--, j--) {
                temp[j] = sl[i];
            }
            for (; j >= 0; j--) {
                temp[j] = '0';
            }
            sb.append(temp);
            Arrays.fill(temp, '0');
        }
        if (restBits > 0) {
            final long last = readLastLong(lengthInBits() - restBits);
            final char[] tempLast = new char[(int) restBits];
            String restStr = Long.toBinaryString(last);
            restStr = restStr.length() > tempLast.length ? restStr
                    .substring(restStr.length() - tempLast.length) : restStr;
            final char[] sl = restStr.toCharArray();
            int i = sl.length - 1;
            int j = tempLast.length - 1;
            for (; i >= 0; i--, j--) {
                tempLast[j] = sl[i];
            }
            for (; j >= 0; j--) {
                tempLast[j] = '0';
            }
            sb.append(tempLast);
        }
        return sb.toString();
    }

    /**
     * Returns a string representation of this data block in base 2, separate in
     * bytes by given separator. If given separator is null, no separator.
     *
     * @param separator
     *            given separator, can be null
     * @return a string representation of this data block in base 2
     */
    public String toBinaryString(final String separator) {
        if (isEmpty()) {
            return "";
        }
        final StringBuilder sb = new StringBuilder();
        final int restBits = (int) (lengthInBits() % Byte.SIZE);
        final char[] temp = new char[Byte.SIZE];
        for (long p = 0; p < lengthInBits() - restBits; p += Byte.SIZE) {
            if (p != 0 && separator != null) {
                sb.append(separator);
            }
            final String s = Integer.toBinaryString(readByte(p));
            final char[] si = s.toCharArray();
            int i = si.length - 1;
            int j = temp.length - 1;
            for (; i >= 0; i--, j--) {
                temp[j] = si[i];
            }
            for (; j >= 0; j--) {
                temp[j] = '0';
            }
            sb.append(temp);
            Arrays.fill(temp, '0');
        }
        if (restBits > 0) {
            if (lengthInBits() > restBits && separator != null) {
                sb.append(separator);
            }
            final int last = readLastByte(lengthInBits() - restBits);
            final char[] tempLast = new char[(int) restBits];
            String restStr = Integer.toBinaryString(last);
            restStr = restStr.length() > tempLast.length ? restStr
                    .substring(restStr.length() - tempLast.length) : restStr;
            final char[] si = restStr.toCharArray();
            int i = si.length - 1;
            int j = tempLast.length - 1;
            for (; i >= 0; i--, j--) {
                tempLast[j] = si[i];
            }
            for (; j >= 0; j--) {
                tempLast[j] = '0';
            }
            sb.append(tempLast);
        }
        return sb.toString();
    }

    /**
     * Returns a string representation of this data block in base 2, separate in
     * special length in bits by given separator. If given separator is null, no
     * separator.
     *
     * @param length
     *            special length in bits to split string with separator,
     *            positive
     * @param separator
     *            given separator, can be null
     * @return a string representation of this data block in base 2
     * @throws IllegalArgumentException
     *             thrown if special length is illegal
     */
    public String toBinaryString(final int length, final String separator) {
        checkPosOrLengthLegality(length);
        if (isEmpty()) {
            return "";
        }
        final StringBuilder sb = new StringBuilder();
        final int restBits = (int) (lengthInBits() % length);
        final char[] temp = new char[length];
        for (long p = 0; p < lengthInBits() - restBits; p += length) {
            if (p != 0 && separator != null) {
                sb.append(separator);
            }
            final char[] ss = partOf(p, length).toBinaryString().toCharArray();
            int i = ss.length - 1;
            int j = temp.length - 1;
            for (; i >= 0; i--, j--) {
                temp[j] = ss[i];
            }
            for (; j >= 0; j--) {
                temp[j] = '0';
            }
            sb.append(temp);
            Arrays.fill(temp, '0');
        }
        if (restBits > 0) {
            if (lengthInBits() > restBits && separator != null) {
                sb.append(separator);
            }
            final char[] tempLast = new char[(int) restBits];
            String restStr = partOf(lengthInBits() - restBits).toBinaryString();
            restStr = restStr.length() > tempLast.length ? restStr
                    .substring(restStr.length() - tempLast.length) : restStr;
            final char[] ss = restStr.toCharArray();
            int i = ss.length - 1;
            int j = tempLast.length - 1;
            for (; i >= 0; i--, j--) {
                tempLast[j] = ss[i];
            }
            for (; j >= 0; j--) {
                tempLast[j] = '0';
            }
            sb.append(tempLast);
        }
        return sb.toString();
    }

    /**
     * Returns hex string of this block like FF. If the length of block is not
     * multiple of 8 bits, the tail fill 0 to align 8 bits.
     *
     * @return hex string of this block
     */
    public String toHexString() {
        if (isEmpty()) {
            return "";
        }
        final StringBuilder sb = new StringBuilder();
        final int restBits = (int) (lengthInBits() % Long.SIZE);
        final char[] temp = new char[Long.SIZE / 4];
        for (long p = 0; p < lengthInBits() - restBits; p += Long.SIZE) {
            final char[] sl = Long.toHexString(readLong(p)).toCharArray();
            int i = sl.length - 1;
            int j = temp.length - 1;
            for (; i >= 0; i--, j--) {
                temp[j] = sl[i];
            }
            for (; j >= 0; j--) {
                temp[j] = '0';
            }
            sb.append(temp);
            Arrays.fill(temp, '0');
        }
        if (restBits > 0) {
            long last = readLastLong(lengthInBits() - restBits);
            last <<= Long.SIZE - restBits;
            final char[] tempLast = new char[restBits % 4 == 0 ? restBits / 4
                    : restBits / 4 + 1];
            final char[] sl = Long.toHexString(last).toCharArray();
            int i = 0;
            int j = 0;
            for (; i < sl.length && j < tempLast.length; i++, j++) {
                tempLast[j] = sl[i];
            }
            for (; j < tempLast.length; j++) {
                tempLast[j] = '0';
            }
            sb.append(tempLast);
        }
        return sb.toString();
    }

    /**
     * Returns a string representation of this data block in base 16, separate
     * in bytes by given separator. If given separator is null, no separator. If
     * the length of block is not multiple of 8 bits, the tail fill 0 to align 8
     * bits.
     *
     * @param separator
     *            given separator, can be null
     * @return a string representation of this data block in base 16
     */
    public String toHexString(final String separator) {
        if (isEmpty()) {
            return "";
        }
        final StringBuilder sb = new StringBuilder();
        final int restBits = (int) (lengthInBits() % Byte.SIZE);
        final char[] temp = new char[Byte.SIZE / 4];
        for (long p = 0; p < lengthInBits() - restBits; p += Byte.SIZE) {
            if (p != 0 && separator != null) {
                sb.append(separator);
            }
            final String s = Integer.toHexString(readByte(p));
            final char[] si = s.toCharArray();
            int i = si.length - 1;
            int j = temp.length - 1;
            for (; i >= 0; i--, j--) {
                temp[j] = si[i];
            }
            for (; j >= 0; j--) {
                temp[j] = '0';
            }
            sb.append(temp);
            Arrays.fill(temp, '0');
        }
        if (restBits > 0) {
            if (lengthInBits() > restBits && separator != null) {
                sb.append(separator);
            }
            int last = readLastByte(lengthInBits() - restBits);
            last <<= Integer.SIZE - restBits;
            final char[] tempLast = new char[restBits % 4 == 0 ? restBits / 4
                    : restBits / 4 + 1];
            final char[] si = Integer.toHexString(last).toCharArray();
            int i = 0;
            int j = 0;
            for (; i < si.length && j < tempLast.length; i++, j++) {
                tempLast[j] = si[i];
            }
            for (; j < tempLast.length; j++) {
                tempLast[j] = '0';
            }
            sb.append(tempLast);
        }
        return sb.toString();
    }

    /**
     * Returns a string representation of this data block in base 16, separate
     * in special length in bits by given separator. If given separator is null,
     * no separator. If the length of block is not multiple of 8 bits, the tail
     * fill 0 to align 8 bits.
     *
     * @param length
     *            special length in bits to split string with separator,
     *            positive and multiple of 4 bits
     * @param separator
     *            given separator, can be null
     * @return a string representation of this data block in base 16
     * @throws IllegalArgumentException
     *             thrown if special length is illegal
     */
    public String toHexString(final int length, final String separator) {
        checkPosOrLengthLegality(length);
        if (length % 4 != 0) {
            throw new IllegalArgumentException(
                    "Length should be mutiple of 4 bits!");
        }
        if (isEmpty()) {
            return "";
        }
        final StringBuilder sb = new StringBuilder();
        final int restBits = (int) (lengthInBits() % length);
        final char[] temp = new char[length / 4];
        for (long p = 0; p < lengthInBits() - restBits; p += length) {
            if (p != 0 && separator != null) {
                sb.append(separator);
            }
            final char[] ss = partOf(p, length).toHexString().toCharArray();
            int i = ss.length - 1;
            int j = temp.length - 1;
            for (; i >= 0; i--, j--) {
                temp[j] = ss[i];
            }
            for (; j >= 0; j--) {
                temp[j] = '0';
            }
            sb.append(temp);
            Arrays.fill(temp, '0');
        }
        if (restBits > 0) {
            if (lengthInBits() > restBits && separator != null) {
                sb.append(separator);
            }
            final String last = partOf(lengthInBits() - restBits).toHexString();
            final char[] tempLast = new char[restBits % 4 == 0 ? restBits / 4
                    : restBits / 4 + 1];
            final char[] ss = last.toCharArray();
            int i = 0;
            int j = 0;
            for (; i < ss.length && j < tempLast.length; i++, j++) {
                tempLast[j] = ss[i];
            }
            for (; j < tempLast.length; j++) {
                tempLast[j] = '0';
            }
            sb.append(tempLast);
        }
        return sb.toString();
    }

    /**
     * An functional interface inner of {@linkplain Bitsss} used to convert data
     * to custom string, commonly the data will be split and convert part by
     * part.
     *
     * @author Fred Suvn
     * @version 0.0.0, 2015-06-29 10:52:08
     */
    @FunctionalInterface
    private static interface DataToStringConverter {
        /**
         * Converts data to custom string, commonly the data will be split and
         * convert part by part, this method convert one part.
         *
         * @param block
         *            given block, not null
         * @param serial
         *            serial of child block of mother block
         * @return custom string
         */
        public String toString(Bitsss block, long serial);
    }

    /**
     * Returns a string representation of this data block by custom converter,
     * separate in special length in bits by given separator. If given separator
     * is null, no separator. The converter deal with block of each part split
     * by special length in bits.
     *
     * @param length
     *            special length in bits to split string with separator,
     *            positive
     * @param separator
     *            given separator, can be null
     * @param converter
     *            converter to convert data to string, not null
     * @return a string representation of this data block by custom converter
     * @throws NullPointerException
     *             thrown if converter is null
     * @throws IllegalArgumentException
     *             thrown if special length is illegal
     */
    public String toString(final int length, final String separator,
            DataToStringConverter converter) {
        checkPosOrLengthLegality(length);
        converter = Objects.requireNonNull(converter,
                "Converter cannot be null!");
        if (isEmpty()) {
            return "";
        }
        final StringBuilder sb = new StringBuilder();
        final int restBits = (int) (lengthInBits() % length);
        final char[] temp = new char[length];
        long s = 0;
        for (long p = 0; p < lengthInBits() - restBits; p += length, s++) {
            if (p != 0 && separator != null) {
                sb.append(separator);
            }
            final char[] ss = converter.toString(partOf(p, length), s)
                    .toCharArray();
            int i = ss.length - 1;
            int j = temp.length - 1;
            for (; i >= 0; i--, j--) {
                temp[j] = ss[i];
            }
            for (; j >= 0; j--) {
                temp[j] = '0';
            }
            sb.append(temp);
            Arrays.fill(temp, '0');
        }
        if (restBits > 0) {
            if (lengthInBits() > restBits && separator != null) {
                sb.append(separator);
            }
            final String last = converter.toString(partOf(lengthInBits()
                    - restBits), s);
            final char[] tempLast = new char[(int) restBits];
            final char[] ss = last.toCharArray();
            int i = ss.length - 1;
            int j = tempLast.length - 1;
            for (; i >= 0; i--, j--) {
                tempLast[j] = ss[i];
            }
            for (; j >= 0; j--) {
                tempLast[j] = '0';
            }
            sb.append(tempLast);
        }
        return sb.toString();
    }

    /**
     * Returns a string by encoding bytes of this block, the charset is local
     * system default.
     *
     * @return a string by encoding bytes of this block
     */
    public String toText() {
        return new String(toByteArray());
    }

    /**
     * Returns a string by encoding bytes of this block, the chaset is specify
     * by charset name.
     *
     * @param charsetName
     *            charset name, not null
     * @return a string by encoding bytes of this block
     * @throws NullPointerException
     *             thrown if charset name is null
     * @throws UnsupportedEncodingException
     *             thrown if charset name is illegal or unsupported
     */
    public String toText(final String charsetName) {
        try {
            return new String(toByteArray(), charsetName);
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns source data object of this block. The source data object is base
     * and underline object which directly read, written and manipulated by this
     * block. Maybe it is a primitive array, or a native object represents a
     * block of memory, or even itself.
     * <p>
     * If specify a type of primitive when constructed this block, this method
     * will return the reference of created or connected primitive array. For
     * example:
     *
     * <pre>
     * byte[] bs = { 1, 2, 3, 4 };
     * DataBlock block = new DataBlock(bs, 1, 0, 2, 0);
     * System.out.println(block.getSourceArray() == bs); // print true!
     * </pre>
     *
     * @return source data object of this block
     */
    public Object getSourceArray() {
        return base.getSourceData();
    }

    /**
     * Returns hash code of this block.
     *
     * @return hash code of this block
     */
    @Override
    public int hashCode() {
        return base.hashCode();
    }

    /**
     * Returns whether two block are fully same.
     *
     * @param obj
     *            given another block
     * @return whether two block are fully same
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        return base.equals(Objects.requireNonNull(obj));
    }

    @Override
    public void putBoolean(final long posInBits, final boolean value) {
        // TODO Auto-generated method stub

    }

    @Override
    public void putBoolean(final int posInBytes, final boolean value) {
        // TODO Auto-generated method stub

    }

    @Override
    public void putByte(final long posInBits, final byte value) {
        // TODO Auto-generated method stub

    }

    @Override
    public void putByte(final int posInBytes, final byte value) {
        // TODO Auto-generated method stub

    }

    @Override
    public void putShort(final long posInBits, final short value) {
        // TODO Auto-generated method stub

    }

    @Override
    public void putShort(final int posInBytes, final short value) {
        // TODO Auto-generated method stub

    }

    @Override
    public void putChar(final long posInBits, final char value) {
        // TODO Auto-generated method stub

    }

    @Override
    public void putChar(final int posInBytes, final char value) {
        // TODO Auto-generated method stub

    }

    @Override
    public void putInt(final long posInBits, final int value) {
        // TODO Auto-generated method stub

    }

    @Override
    public void putInt(final int posInBytes, final int value) {
        // TODO Auto-generated method stub

    }

    @Override
    public void putFloat(final long posInBits, final float value) {
        // TODO Auto-generated method stub

    }

    @Override
    public void putFloat(final int posInBytes, final float value) {
        // TODO Auto-generated method stub

    }

    @Override
    public void putLong(final long posInBits, final long value) {
        // TODO Auto-generated method stub

    }

    @Override
    public void putLong(final int posInBytes, final long value) {
        // TODO Auto-generated method stub

    }

    @Override
    public void putDouble(final long posInBits, final double value) {
        // TODO Auto-generated method stub

    }

    @Override
    public void putDouble(final int posInBytes, final double value) {
        // TODO Auto-generated method stub

    }

    @Override
    public int putByteInBounds(final long posInBits, final byte value) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int putByteInBounds(final int posInBytes, final byte value) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int putShortInBounds(final long posInBits, final short value) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int putShortInBounds(final int posInBytes, final short value) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int putCharInBounds(final long posInBits, final char value) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int putCharInBounds(final int posInBytes, final char value) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int putIntInBounds(final long posInBits, final int value) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int putIntInBounds(final int posInBytes, final int value) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int putFloatInBounds(final long posInBits, final float value) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int putFloatInBounds(final int posInBytes, final float value) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int putLongInBounds(final long posInBits, final long value) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int putLongInBounds(final int posInBytes, final long value) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int putDoubleInBounds(final long posInBits, final double value) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int putDoubleInBounds(final int posInBytes, final double value) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void putBits(final long posInBits, final byte value, final int bits) {
        // TODO Auto-generated method stub

    }

    @Override
    public void putBits(final int posInBytes, final byte value, final int bits) {
        // TODO Auto-generated method stub

    }

    @Override
    public void putBits(final long posInBits, final short value, final int bits) {
        // TODO Auto-generated method stub

    }

    @Override
    public void putBits(final int posInBytes, final short value, final int bits) {
        // TODO Auto-generated method stub

    }

    @Override
    public void putBits(final long posInBits, final char value, final int bits) {
        // TODO Auto-generated method stub

    }

    @Override
    public void putBits(final int posInBytes, final char value, final int bits) {
        // TODO Auto-generated method stub

    }

    @Override
    public void putBits(final long posInBits, final int value, final int bits) {
        // TODO Auto-generated method stub

    }

    @Override
    public void putBits(final int posInBytes, final int value, final int bits) {
        // TODO Auto-generated method stub

    }

    @Override
    public void putBits(final long posInBits, final float value, final int bits) {
        // TODO Auto-generated method stub

    }

    @Override
    public void putBits(final int posInBytes, final float value, final int bits) {
        // TODO Auto-generated method stub

    }

    @Override
    public void putBits(final long posInBits, final long value, final int bits) {
        // TODO Auto-generated method stub

    }

    @Override
    public void putBits(final int posInBytes, final long value, final int bits) {
        // TODO Auto-generated method stub

    }

    @Override
    public void putBits(final long posInBits, final double value, final int bits) {
        // TODO Auto-generated method stub

    }

    @Override
    public void putBits(final int posInBytes, final double value, final int bits) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean getBoolean(final long posInBits) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean getBoolean(final int posInBytes) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public byte getByte(final long posInBits) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public byte getByte(final int posInBytes) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getUnsignedByte(final long posInBits) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getUnsignedByte(final int posInBytes) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long getLongUnsignedByte(final long posInBits) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long getLongUnsignedByte(final int posInBytes) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public short getShort(final long posInBits) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public short getShort(final int posInBytes) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getUnsignedShort(final long posInBits) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getUnsignedShort(final int posInBytes) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long getLongUnsignedShort(final long posInBits) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long getLongUnsignedShort(final int posInBytes) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public char getChar(final long posInBits) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public char getChar(final int posInBytes) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getInt(final long posInBits) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getInt(final int posInBytes) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long getLongUnsignedInt(final long posInBits) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long getLongUnsignedInt(final int posInBytes) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public float getFloat(final long posInBits) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public float getFloat(final int posInBytes) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long getLong(final long posInBits) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long getLong(final int posInBytes) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double getDouble(final long posInBits) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double getDouble(final int posInBytes) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public byte getByteInBounds(final long posInBits) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public byte getByteInBounds(final int posInBytes) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getUnsignedByteInBounds(final long posInBits) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getUnsignedByteInBounds(final int posInBytes) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long getLongUnsignedByteInBounds(final long posInBits) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long getLongUnsignedByteInBounds(final int posInBytes) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public short getShortInBounds(final long posInBits) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public short getShortInBounds(final int posInBytes) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getUnsignedShortInBounds(final long posInBits) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getUnsignedShortInBounds(final int posInBytes) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long getLongUnsignedShortInBounds(final long posInBits) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long getLongUnsignedShortInBounds(final int posInBytes) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public char getCharInBounds(final long posInBits) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public char getCharInBounds(final int posInBytes) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getIntInBounds(final long posInBits) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getIntInBounds(final int posInBytes) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long getLongUnsignedIntInBounds(final long posInBits) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long getLongUnsignedIntInBounds(final int posInBytes) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public float getFloatInBounds(final long posInBits) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public float getFloatInBounds(final int posInBytes) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long getLongInBounds(final long posInBits) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long getLongInBounds(final int posInBytes) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double getDoubleInBounds(final long posInBits) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double getDoubleInBounds(final int posInBytes) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public byte getBitsAsByte(final long posInBits, final int bits) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public byte getBitsAsByte(final int posInBytes, final int bits) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public short getBitsAsShort(final long posInBits, final int bits) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public short getBitsAsShort(final int posInBytes, final int bits) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public char getBitsAsChar(final long posInBits, final int bits) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public char getBitsAsChar(final int posInBytes, final int bits) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getBitsAsInt(final long posInBits, final int bits) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getBitsAsInt(final int posInBytes, final int bits) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public float getBitsAsFloat(final long posInBits, final int bits) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public float getBitsAsFloat(final int posInBytes, final int bits) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long getBitsAsLong(final long posInBits, final int bits) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long getBitsAsLong(final int posInBytes, final int bits) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double getBitsAsDouble(final long posInBits, final int bits) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double getBitsAsDouble(final int posInBytes, final int bits) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Bitsss sub(final long startPos) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Bitsss sub(final long startPos, final long endPos) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Bitsss subClone(final long startPos) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Bitsss subClone(final long startPos, final long endPos) {
        // TODO Auto-generated method stub
        return null;
    }

}
