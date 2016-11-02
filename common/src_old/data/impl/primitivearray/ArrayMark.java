package com.cogician.quicker.binary.data.impl.primitivearray;

import java.io.ObjectStreamException;
import java.io.Serializable;

import com.cogician.quicker.annotation.Base;
import com.cogician.quicker.struct.ArrayPos;

/**
 * <p>
 * This class is used to mark start and end position info of array of primitive
 * type.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2015-06-14 19:00:50
 * @since 0.0.0
 * @deprecated Stop used this class indefinitely.
 */
@Base
@Deprecated
abstract class ArrayMark implements Serializable {

    /**
     * Serial ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * <p>
     * Start index of array, data managed by accessor start from this index.
     * </p>
     */
    final int startIndex;

    /**
     * <p>
     * Bit offset of {@linkplain #startIndex}.
     * </p>
     */
    final int startOffset;

    /**
     * <p>
     * End index of array, data managed by accessor end at this index.
     * </p>
     */
    final int endIndex;

    /**
     * <p>
     * Bit offset of {@linkplain #endIndex}.
     * </p>
     */
    final int endOffset;

    /**
     * Length in bits of this array part.
     */
    private transient long lengthInBits;

    /**
     * Temporary data, commonly used to store temporary value about index and
     * bit offset when computing or manipulating.
     */
    private transient ArrayPos temp = null;

    /**
     * Returns length from start position to end position of array.
     */
    private long initLength() {
        return getArrayBitPosFromArrayIndex(endIndex, endOffset)
                - getArrayBitPosFromArrayIndex(startIndex, startOffset) + 1;
    }

    /**
     * Returns {@linkplain #temp} with single instance.
     *
     * @param index
     *            special index
     * @param offset
     *            special offset
     * @return {@linkplain #temp} with single instance
     */
    private ArrayPos getTempData(final int index, final int offset) {
        if (temp == null) {
            temp = new ArrayPos(index, offset);
            return temp;
        } else {
            temp.index = index;
            temp.offset = offset;
            return temp;
        }
    }

    /**
     * Returns element wide of the array in bits.
     *
     * @return element wide of the array in bits
     */
    abstract int elementWide();

    /**
     * Constructs with start index, bit offset of start index, end index, bit
     * offset of end index. Parameters should be legal and in bounds.
     *
     * @param startIndex
     *            start index in bounds
     * @param startOffset
     *            bit offset of start index, [0, element wide - 1]
     * @param endIndex
     *            end index in bounds
     * @param endOffset
     *            bit offset of end index, [0, element wide - 1]
     */
    ArrayMark(final int startIndex, final int startOffset, final int endIndex,
            final int endOffset) {
        this.startIndex = startIndex;
        this.startOffset = startOffset;
        this.endIndex = endIndex;
        this.endOffset = endOffset;
        this.lengthInBits = initLength();
    }

    /**
     * Returns bit position (start from 0) relative to array from given index
     * and its bit offset, the index and its bit offset are also relative to
     * array.
     *
     * @param index
     *            given index relative to array in bounds
     * @param offset
     *            bit offset of index, [0, element wide - 1]
     * @return bit position (start from 0) relative to array from given index
     *         and its bit offset
     */
    long getArrayBitPosFromArrayIndex(final int index, final int offset) {
        return (long) index * elementWide() + offset;
    }

    /**
     * Gets bit position (start from 0) relative to array from given byte
     * position relative to accessor.
     *
     * @param bytePos
     *            byte position relative to accessor in bounds
     * @param bitOffset
     *            bit offset of byte position, [0, 7]
     * @return bit position (start from 0) relative to array
     */
    long getArrayBitPosFromAccessorBytePos(final int bytePos,
            final int bitOffset) {
        final long startPos = getArrayBitPosFromArrayIndex(startIndex,
                startOffset);
        final long relativePos = bytePos * 8L + bitOffset;
        return startPos + relativePos;
    }

    /**
     * Gets bit position (start from 0) relative to array from given bit
     * position relative to accessor.
     *
     * @param pos
     *            bit position in bounds
     * @return bit position (start from 0) relative to array
     */
    long getArrayBitPosFromAccessorBitPos(final long pos) {
        return getArrayBitPosFromArrayIndex(startIndex, startOffset) + pos;
    }

    /**
     * Gets index info relative to array from given byte position info relative
     * to accessor.
     *
     * @param bytePos
     *            byte position relative to accessor in bounds
     * @param bitOffset
     *            bit offset of byte position, [0, 7]
     * @return index info relative to array
     */
    ArrayPos getArrayIndexPosFromAccessorBytePos(final int bytePos,
            final int bitOffset) {
        final long pos = getArrayBitPosFromAccessorBytePos(bytePos, bitOffset);
        return getTempData((int) (pos / elementWide()),
                (int) (pos % elementWide()));
    }

    /**
     * Gets index info relative to array from given bit position info relative
     * to accessor.
     *
     * @param pos
     *            bit position in bounds
     * @return index info relative to array
     */
    ArrayPos getArrayIndexPosFromAccessorBitPos(long pos) {
        pos = getArrayBitPosFromAccessorBitPos(pos);
        return getTempData((int) (pos / elementWide()),
                (int) (pos % elementWide()));
    }

    /**
     * Returns {@linkplain #lengthInBits}.
     *
     * @return {@linkplain #lengthInBits}
     */
    long lengthInBits() {
        return lengthInBits;
    }

    /**
     * For deserializing, manipulate length and buff it.
     *
     * @return this instance buffered
     * @throws ObjectStreamException
     */
    private Object readResolve() throws ObjectStreamException {
        this.lengthInBits = initLength();
        return this;
    }
}

/**
 * {@linkplain ArrayMark} of byte array.
 *
 * @author Fred Suvn
 * @version 0.0.0, 2015-06-16 09:58:58
 */
final class ByteArrayMark extends ArrayMark {

    /**
     * Serial ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs with start index, bit offset of start index, end index, bit
     * offset of end index. Parameters should be legal and in bounds.
     *
     * @param startIndex
     *            start index in bounds
     * @param startOffset
     *            bit offset of start index, [0, element wide - 1]
     * @param endIndex
     *            end index in bounds
     * @param endOffset
     *            bit offset of end index, [0, element wide - 1]
     */
    ByteArrayMark(final int startIndex, final int startOffset,
            final int endIndex, final int endOffset) {
        super(startIndex, startOffset, endIndex, endOffset);
    }

    @Override
    int elementWide() {
        return Byte.SIZE;
    }
}

/**
 * {@linkplain ArrayMark} of short array.
 *
 * @author Fred Suvn
 * @version 0.0.0, 2015-06-16 09:58:58
 */
final class ShortArrayMark extends ArrayMark {

    /**
     * Serial ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs with start index, bit offset of start index, end index, bit
     * offset of end index. Parameters should be legal and in bounds.
     *
     * @param startIndex
     *            start index in bounds
     * @param startOffset
     *            bit offset of start index, [0, element wide - 1]
     * @param endIndex
     *            end index in bounds
     * @param endOffset
     *            bit offset of end index, [0, element wide - 1]
     */
    ShortArrayMark(final int startIndex, final int startOffset,
            final int endIndex, final int endOffset) {
        super(startIndex, startOffset, endIndex, endOffset);
    }

    @Override
    int elementWide() {
        return Short.SIZE;
    }
}

/**
 * {@linkplain ArrayMark} of char array.
 *
 * @author Fred Suvn
 * @version 0.0.0, 2015-06-16 09:58:58
 */
final class CharArrayMark extends ArrayMark {

    /**
     * Serial ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs with start index, bit offset of start index, end index, bit
     * offset of end index. Parameters should be legal and in bounds.
     *
     * @param startIndex
     *            start index in bounds
     * @param startOffset
     *            bit offset of start index, [0, element wide - 1]
     * @param endIndex
     *            end index in bounds
     * @param endOffset
     *            bit offset of end index, [0, element wide - 1]
     */
    CharArrayMark(final int startIndex, final int startOffset,
            final int endIndex, final int endOffset) {
        super(startIndex, startOffset, endIndex, endOffset);
    }

    @Override
    int elementWide() {
        return Character.SIZE;
    }
}

/**
 * {@linkplain ArrayMark} of int array.
 *
 * @author Fred Suvn
 * @version 0.0.0, 2015-06-16 09:58:58
 */
final class IntArrayMark extends ArrayMark {

    /**
     * Serial ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs with start index, bit offset of start index, end index, bit
     * offset of end index. Parameters should be legal and in bounds.
     *
     * @param startIndex
     *            start index in bounds
     * @param startOffset
     *            bit offset of start index, [0, element wide - 1]
     * @param endIndex
     *            end index in bounds
     * @param endOffset
     *            bit offset of end index, [0, element wide - 1]
     */
    IntArrayMark(final int startIndex, final int startOffset,
            final int endIndex, final int endOffset) {
        super(startIndex, startOffset, endIndex, endOffset);
    }

    @Override
    int elementWide() {
        return Integer.SIZE;
    }
}

/**
 * {@linkplain ArrayMark} of float array.
 *
 * @author Fred Suvn
 * @version 0.0.0, 2015-06-16 09:58:58
 */
final class FloatArrayMark extends ArrayMark {

    /**
     * Serial ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs with start index, bit offset of start index, end index, bit
     * offset of end index. Parameters should be legal and in bounds.
     *
     * @param startIndex
     *            start index in bounds
     * @param startOffset
     *            bit offset of start index, [0, element wide - 1]
     * @param endIndex
     *            end index in bounds
     * @param endOffset
     *            bit offset of end index, [0, element wide - 1]
     */
    FloatArrayMark(final int startIndex, final int startOffset,
            final int endIndex, final int endOffset) {
        super(startIndex, startOffset, endIndex, endOffset);
    }

    @Override
    int elementWide() {
        return Float.SIZE;
    }
}

/**
 * {@linkplain ArrayMark} of long array.
 *
 * @author Fred Suvn
 * @version 0.0.0, 2015-06-16 09:58:58
 */
final class LongArrayMark extends ArrayMark {

    /**
     * Serial ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs with start index, bit offset of start index, end index, bit
     * offset of end index. Parameters should be legal and in bounds.
     *
     * @param startIndex
     *            start index in bounds
     * @param startOffset
     *            bit offset of start index, [0, element wide - 1]
     * @param endIndex
     *            end index in bounds
     * @param endOffset
     *            bit offset of end index, [0, element wide - 1]
     */
    LongArrayMark(final int startIndex, final int startOffset,
            final int endIndex, final int endOffset) {
        super(startIndex, startOffset, endIndex, endOffset);
    }

    @Override
    int elementWide() {
        return Long.SIZE;
    }
}

/**
 * {@linkplain ArrayMark} of double array.
 *
 * @author Fred Suvn
 * @version 0.0.0, 2015-06-16 09:58:58
 */
final class DoubleArrayMark extends ArrayMark {

    /**
     * Serial ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs with start index, bit offset of start index, end index, bit
     * offset of end index. Parameters should be legal and in bounds.
     *
     * @param startIndex
     *            start index in bounds
     * @param startOffset
     *            bit offset of start index, [0, element wide - 1]
     * @param endIndex
     *            end index in bounds
     * @param endOffset
     *            bit offset of end index, [0, element wide - 1]
     */
    DoubleArrayMark(final int startIndex, final int startOffset,
            final int endIndex, final int endOffset) {
        super(startIndex, startOffset, endIndex, endOffset);
    }

    @Override
    int elementWide() {
        return Double.SIZE;
    }
}
