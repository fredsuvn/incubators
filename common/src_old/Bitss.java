package com.cogician.quicker.binary;

import com.cogician.quicker.annotation.Base;
import com.cogician.quicker.binary.data.impl.primitivearray.BooleanArrayBaseAccessor;
import com.cogician.quicker.binary.data.impl.primitivearray.ByteArrayBaseAccessor;
import com.cogician.quicker.binary.data.impl.primitivearray.CharArrayBaseAccessor;
import com.cogician.quicker.binary.data.impl.primitivearray.DoubleArrayBaseAccessor;
import com.cogician.quicker.binary.data.impl.primitivearray.FloatArrayBaseAccessor;
import com.cogician.quicker.binary.data.impl.primitivearray.IntArrayBaseAccessor;
import com.cogician.quicker.binary.data.impl.primitivearray.LongArrayBaseAccessor;
import com.cogician.quicker.binary.data.impl.primitivearray.PrimitiveImplUtil;
import com.cogician.quicker.binary.data.impl.primitivearray.ShortArrayBaseAccessor;
import com.cogician.quicker.util.BitsQuicker;

/**
 * <p>
 * Implementation of {@link Bits}, more detail for seeing
 * {@link Bits}. This implementation cannot be empty.
 * </p>
 * <p>
 * This implementation use array of primitive type to store and manipulate data,
 * randomly. The effective data start from specified bit offset of start index,
 * end to specified bit offset of end index. This class is super class of all
 * detail implementation. See subType like XxxArrayBaseAccessor in this package
 * for more detail.
 * </p>
 * <p>
 * <b>Note</b> the parameters of methods of this interface will not be checked.
 * Methods will throw exceptions or return wrong results if given parameters
 * don't meet the requirements. The details of exceptions and wrong results
 * depend on implementations.
 * </p>
 * <p>
 * This class is {@linkplain Base}, serializable and cloneable.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2015-06-15 11:32:51
 * @since 0.0.0
 * @see Bits
 */
@Base
abstract class Bitss implements Bits {

    /**
     * Serial ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * <p>
     * A threshold in bits for using quick method
     * {@linkplain System#arraycopy(Object, int, Object, int, int)} to optimize
     * memory copy operation like copy method.
     * </p>
     */
    protected static final long MEM_COPY_OPTIMIZATION_THRESHOLD_IN_BITS = 8L * 1024L * 10L;

    /**
     * <p>
     * Absolute start index of array.
     * </p>
     */
    protected final int startIndex;

    /**
     * <p>
     * Bit offset of absolute start index.
     * </p>
     */
    protected final int startOffset;

    /**
     * <p>
     * Absolute start position of array in bits. startPos = startIndex * element
     * wide of array + startOffset.
     * </p>
     */
    protected final long startPos;

    /**
     * <p>
     * Absolute end index of array.
     * </p>
     */
    protected final int endIndex;

    /**
     * <p>
     * Bit offset of absolute end index.
     * </p>
     */
    protected final int endOffset;

    /**
     * <p>
     * Absolute end position of array in bits. endPos = endIndex * element wide
     * of array + endOffset.
     * </p>
     */
    protected final long endPos;

    /**
     * <p>
     * Buffer of length in bits.
     * </p>
     */
    protected final long lengthInBits;

    /**
     * <p>
     * Element wide of array in bits. boolean[] is 1, byte[] is 8, short[] is 16
     * and so on. This field initialized by {@linkplain #elementWide()}.
     * </p>
     */
    protected final int elementWide;

    /**
     * <p>
     * Element wide of array manipulated by this accessor in bits, should be one
     * of 1 (boolean as bit), 8 (byte), 16 (short, char), 32 (int, float), 64
     * (long, double).
     * </p>
     * <p>
     * This method is underlying, it is used to initialize
     * {@linkplain #elementWide}.
     * </p>
     *
     * @return element wide of array manipulated by this accessor in bits
     * @since 0.0.0
     */
    abstract protected int elementWide();

    /**
     * <p>
     * Constructs with specified absolute start index, bit offset of absolute
     * start index, absolute end index, bit offset of absolute end index.
     * Absolute end position should be greater than or equal to (if length is i
     * bit) absolute start position.
     * </p>
     *
     * @param startIndex
     *            absolute start index, [0, array's length - 1]
     * @param startOffset
     *            bit offset of absolute start index, [0, element wide - 1]
     * @param endIndex
     *            absolute end index, [absolute start index, array's length - 1]
     * @param endOffset
     *            bit offset of absolute end index, [0, element wide 1]
     */
    protected Bitss(final int startIndex, final int startOffset,
            final int endIndex, final int endOffset) {
        this.elementWide = elementWide();
        this.startIndex = startIndex;
        this.startOffset = startOffset;
        this.endIndex = endIndex;
        this.endOffset = endOffset;
        this.startPos = ((long) startIndex) * this.elementWide + startOffset;
        this.endPos = ((long) endIndex) * this.elementWide + endOffset;
        this.lengthInBits = this.endPos - this.startPos + 1L;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public long lengthInBits() {
        return lengthInBits;
    }

    @Override
    public int lengthInBytes() {
        return (int) (lengthInBits / 8L);
    }

    @Override
    public long remainderLength(final int posInBytes) {
        return remainderLength(posInBytes * 8L);
    }

    @Override
    public long remainderLength(final long posInBits) {
        return lengthInBits - posInBits;
    }

    /**
     * <p>
     * Shadow clone specified part of array into a new instance of subtype.
     * Absolute end position (indicated by absolute end index and bit offset of
     * absolute end index) should be greater than absolute start position
     * (indicated by absolute start index and bit offset of absolute start
     * index).
     * </p>
     *
     * @param startIndex
     *            absolute start index, [0, array's length - 1]
     * @param startOffset
     *            bit offset of absolute start index, [0, element wide - 1]
     * @param endIndex
     *            absolute end index, [absolute start index, array's length - 1]
     * @param endOffset
     *            bit offset of absolute end index, [0, element wide - 1]
     * @return subtype of this class for more detail implementation of primitive
     *         array
     */
    protected abstract Bits shadowClone(int startIndex,
            int startOffset, int endIndex, int endOffset);

    @Override
    abstract public Bits deepClone();

    /**
     * <p>
     * Returns absolute position of array from given relative position of
     * accessor. The high 32 orders is absolute index, low 32 orders is absolute
     * bit offset of absolute index. Like:
     *
     * <pre>
     * long absolutePos = absolutePos(relativePos);
     * int index = (int) (absolutePos &gt;&gt;&gt; 32);
     * int offset = (int) absolutePos;
     * </pre>
     *
     * </p>
     *
     * @param relativePos
     * @return
     * @since 0.0.0
     */
    protected long absolutePos(final long relativePos) {
        final long abs = startPos + relativePos;
        final long index = abs / elementWide;
        final long bitOffset = abs % elementWide;
        return (index << 32) | (bitOffset & 0x00000000FFFFFFFFL);
    }

    @Override
    public Bits partOf(final int startInBytes) {
        return partOf(startInBytes * 8L);
    }

    @Override
    public Bits partOf(final int startInclusive, final int endExclusive) {
        return partOf(startInclusive * 8L, endExclusive * 8L);
    }

    @Override
    public Bits partOf(final long startInBits) {
        final long absolutePos = absolutePos(startInBits);
        final int index = (int) (absolutePos >>> 32);
        final int offset = (int) absolutePos;
        return shadowClone(index, offset, this.endIndex, this.endOffset);
    }

    @Override
    public Bits partOf(final long startInclusive,
            final long endExclusive) {
        long absolutePos = absolutePos(startInclusive);
        final int startIndex = (int) (absolutePos >>> 32);
        final int startOffset = (int) absolutePos;
        absolutePos = absolutePos(endExclusive - 1L);
        final int endIndex = (int) (absolutePos >>> 32);
        final int endOffset = (int) absolutePos;
        return shadowClone(startIndex, startOffset, endIndex, endOffset);
    }

    @Override
    public Bits shadowClone() {
        return shadowClone(startIndex, startOffset, endIndex, endOffset);
    }

    @Override
    public Bits clone() {
        return deepClone();
    }

    @Override
    public void setBits(final boolean value) {
        final long longCount = lengthInBits / 64L;
        long pos = 0L;
        if (value) {
            for (long p = 0; p < longCount; p++) {
                pos = p * 64L;
                this.writeLong(pos, 0xFFFFFFFFFFFFFFFFL);
            }
        } else {
            for (long p = 0; p < longCount; p++) {
                pos = p * 64L;
                this.writeLong(pos, 0x0000000000000000L);
            }
        }
        final int restBits = (int) (lengthInBits % 64L);
        if (restBits > 0) {
            pos = longCount * 64L;
            if (value) {
                this.writeBits(pos, 0xFFFFFFFFFFFFFFFFL, restBits);
            } else {
                this.writeBits(pos, 0x0000000000000000L, restBits);
            }
        }
    }

    @Override
    public long copyTo(final Bits dest) {
        final Bits shorter = PrimitiveImplUtil.getShorterAccessor(this,
                dest);
        final long longCount = shorter.lengthInBits() / 64L;
        long pos = 0L;
        for (long p = 0; p < longCount; p++) {
            pos = p * 64L;
            dest.writeLong(pos, this.readLong(pos));
        }
        final int restBits = (int) (shorter.lengthInBits() % 64L);
        if (restBits > 0) {
            pos = longCount * 64L;
            dest.writeBits(pos,
                    this.readBitsAsLong(pos, restBits) << (64 - restBits),
                    restBits);
        }
        return shorter.lengthInBits();
    }

    @Override
    public long and(final Bits target) {
        final Bits shorter = PrimitiveImplUtil.getShorterAccessor(this,
                target);
        final long longCount = shorter.lengthInBits() / 64L;
        long pos = 0L;
        for (long p = 0; p < longCount; p++) {
            pos = p * 64L;
            this.writeLong(pos, this.readLong(pos) & target.readLong(pos));
        }
        final int restBits = (int) (shorter.lengthInBits() % 64L);
        if (restBits > 0) {
            pos = longCount * 64L;
            this.writeBits(
                    pos,
                    (this.readBitsAsLong(pos, restBits) << (64 - restBits))
                            & (target.readBitsAsLong(pos, restBits) << (64 - restBits)),
                    restBits);
        }
        return shorter.lengthInBits();
    }

    @Override
    public long or(final Bits target) {
        final Bits shorter = PrimitiveImplUtil.getShorterAccessor(this,
                target);
        final long longCount = shorter.lengthInBits() / 64L;
        long pos = 0L;
        for (long p = 0; p < longCount; p++) {
            pos = p * 64L;
            this.writeLong(pos, this.readLong(pos) | target.readLong(pos));
        }
        final int restBits = (int) (shorter.lengthInBits() % 64L);
        if (restBits > 0) {
            pos = longCount * 64L;
            this.writeBits(
                    pos,
                    (this.readBitsAsLong(pos, restBits) << (64 - restBits))
                            | (target.readBitsAsLong(pos, restBits) << (64 - restBits)),
                    restBits);
        }
        return shorter.lengthInBits();
    }

    @Override
    public long xor(final Bits target) {
        final Bits shorter = PrimitiveImplUtil.getShorterAccessor(this,
                target);
        final long longCount = shorter.lengthInBits() / 64L;
        long pos = 0L;
        for (long p = 0; p < longCount; p++) {
            pos = p * 64L;
            this.writeLong(pos, this.readLong(pos) ^ target.readLong(pos));
        }
        final int restBits = (int) (shorter.lengthInBits() % 64L);
        if (restBits > 0) {
            pos = longCount * 64L;
            this.writeBits(
                    pos,
                    (this.readBitsAsLong(pos, restBits) << (64 - restBits))
                            ^ (target.readBitsAsLong(pos, restBits) << (64 - restBits)),
                    restBits);
        }
        return shorter.lengthInBits();
    }

    @Override
    public void not() {
        final long longCount = lengthInBits / 64L;
        long pos = 0L;
        for (long p = 0; p < longCount; p++) {
            pos = p * 64L;
            writeLong(pos, ~readLong(p));
        }
        final int restBits = (int) (lengthInBits % 64L);
        if (restBits > 0) {
            pos = longCount * 64L;
            writeBits(pos,
                    ~(readBitsAsLong(longCount, restBits) << (64 - restBits)),
                    restBits);
        }
    }

    @Override
    public void logicalLeft(final long bits) {
        final long restBits = lengthInBits - bits;
        partOf(bits).copyTo(partOf(0L, restBits));
        this.partOf(restBits).setBits(false);
    }

    @Override
    public void arithmeticLeft(final long bits) {
        logicalLeft(bits);
    }

    /**
     * Shift right with specified sign bit in front.
     *
     * @param bits
     *            shifted bits number
     * @param signBit
     *            soecified sign bit in front
     */
    private void shiftRight(final long bits, final boolean signBit) {
        final long restBits = lengthInBits - bits;
        Bits copy = this.partOf(0L, restBits).deepClone();
        copy.copyTo(this.partOf(bits));
        copy = null;
        this.partOf(0L, bits).setBits(signBit);
    }

    @Override
    public void logicalRight(final long bits) {
        shiftRight(bits, false);
    }

    @Override
    public void arithmeticRight(final long bits) {
        final int i = readBitsAsInt(0L, 1);
        shiftRight(bits, i == 1 ? true : false);
    }

    @Override
    public void rotateLeft(final long bits) {
        final long restBits = lengthInBits - bits;
        if (bits < lengthInBits / 2) {
            final Bits copy = this.partOf(0L, bits).deepClone();
            this.partOf(bits).copyTo(this.partOf(0L, restBits));
            copy.copyTo(this.partOf(restBits));
        } else {
            final Bits copy = this.partOf(bits).deepClone();
            this.partOf(0L, bits).copyTo(this.partOf(restBits));
            copy.copyTo(this.partOf(0L, restBits));
        }
    }

    @Override
    public void rotateRight(final long bits) {
        rotateLeft(lengthInBits - bits);
    }

    @Override
    public void reverse() {
        if (lengthInBits <= 64L) {
            long v = readBitsAsLong(0L, (int) lengthInBits);
            v = Long.reverse(v);
            writeBits(0L, v, (int) lengthInBits);
            return;
        }
        final long halfBits = lengthInBits / 2L;
        final long intCount = halfBits / 32L;
        long pos = 0L;
        for (long p = 0; p < intCount; p++) {
            pos = p * 32L;
            int head = readInt(pos);
            int tail = readInt(lengthInBits - (pos + 32L));
            head = Integer.reverse(head);
            tail = Integer.reverse(tail);
            writeInt(pos, tail);
            writeInt(lengthInBits - (pos + 32L), head);
        }
        int restBits = (int) (halfBits % 32L);
        if (restBits > 0) {
            pos = intCount * 32L;
            restBits = (int) (lengthInBits - (2 * halfBits));
            writeBits(pos, Long.reverse((readBitsAsLong(pos, restBits))),
                    restBits);
        }
    }

    @Override
    public void reverseInBytes() {
        if (lengthInBits % 8L != 0) {
            return;
        }
        if (lengthInBits <= 64L) {
            long v = readBitsAsLong(0L, (int) lengthInBits);
            v = Long.reverseBytes(v);
            writeBits(0L, v, (int) lengthInBits);
            return;
        }
        final long halfBits = lengthInBits / 2L;
        final long intCount = halfBits / 32L;
        long pos = 0L;
        for (long p = 0; p < intCount; p++) {
            pos = p * 32L;
            int head = readInt(pos);
            int tail = readInt(lengthInBits - (pos + 32L));
            head = Integer.reverseBytes(head);
            tail = Integer.reverseBytes(tail);
            writeInt(pos, tail);
            writeInt(lengthInBits - (pos + 32L), head);
        }
        int restBits = (int) (halfBits % 32L);
        if (restBits > 0) {
            pos = intCount * 32L;
            restBits = (int) (lengthInBits - (2 * halfBits));
            writeBits(pos, Long.reverseBytes((readBitsAsLong(pos, restBits))),
                    restBits);
        }
    }

    @Override
    public abstract Object getSourceData();

    /**
     * <p>
     * Gets elements of array from specified index as int type. First element in
     * highest orders, second in next high orders and so on like:
     *
     * <pre>
     * byte[] array = {1, 2, 3...};
     * If use:
     * getArrayElements(0, 2);
     * will return:
     * 0x01020000;
     * </pre>
     *
     * </p>
     *
     * @param index
     *            specified index, [1, array length - 1]
     * @param elementsNum
     *            number of elements gotten, total bits of elements should less
     *            than or equal to 32
     * @return int value consist of gotten element
     * @since 0.0.0
     */
    abstract protected int readArrayElements(int index, int elementsNum);

    /**
     * <p>
     * Gets elements of array from specified index as long type. First element
     * in highest orders, second in next high orders and so on like:
     *
     * <pre>
     * byte[] array = {1, 2, 3...};
     * If use:
     * getArrayElementsAsLong(0, 3);
     * will return:
     * 0x0102030000000000L;
     * </pre>
     *
     * </p>
     *
     * @param index
     *            specified index, [1, array length - 1]
     * @param elementsNum
     *            number of elements gotten, total bits of elements should less
     *            than or equal to 64
     * @return long value consist of gotten element
     * @since 0.0.0
     */
    abstract protected long readArrayElementsAsLong(int index, int elementsNum);

    /**
     * <p>
     * Writes value into array from specified index in elements. For example:
     *
     * <pre>
     * int i = 0x01020304;
     * 
     * byte[] array = {0,0,0,0};
     * If writes i into above array and writes just 2 elements:
     * writeArrayElements(0, i, 2);
     * will be:
     * array[0] == 0x01;
     * array[1] == 0x02;
     * array[2] == 0x00;
     * array[3] == 0x00;
     * 
     * or
     * 
     * into a short array:
     * short[] array = {0,0,0,0};
     * writeArrayElements(0, i, 2);
     * will be:
     * array[0] = 0x0102;
     * array[0] = 0x0304;
     * array[0] = 0x0000;
     * array[0] = 0x0000;
     * </pre>
     *
     * </p>
     *
     * @param index
     *            specified index, [1, array length - 1]
     * @param value
     *            written value
     * @param elementsNum
     *            number of elements written, total bits of elements should less
     *            than or equal to 32
     * @since 0.0.0
     */
    abstract protected void writeArrayElements(int index, int value,
            int elementsNum);

    /**
     * <p>
     * Writes value into array from specified index in elements. For example:
     *
     * <pre>
     * long l = 0x0102030400000000L;
     * 
     * byte[] array = {0,0,0,0};
     * If writes l into above array and writes just 2 elements:
     * writeArrayElements(0, l, 2);
     * will be:
     * array[0] == 0x01;
     * array[1] == 0x02;
     * array[2] == 0x00;
     * array[3] == 0x00;
     * 
     * or
     * 
     * into a short array:
     * short[] array = {0,0,0,0};
     * writeArrayElements(0, l, 2);
     * will be:
     * array[0] = 0x0102;
     * array[0] = 0x0304;
     * array[0] = 0x0000;
     * array[0] = 0x0000;
     * </pre>
     *
     * </p>
     *
     * @param index
     *            specified index, [1, array length - 1]
     * @param value
     *            written value
     * @param elementsNum
     *            number of elements written, total bits of elements should less
     *            than or equal to 64
     * @since 0.0.0
     */
    abstract protected void writeArrayElements(int index, long value,
            int elementsNum);

    /**
     * <p>
     * Returns number of bits required at least by given offset of element,
     * actual bits number required and element wide of array for array of
     * primitive type. For example:
     *
     * <pre>
     * byte[] array = {1, 2...};
     * If we need bits from 4th bits of first index of array inclusive
     * to 5th bits of second index of array exclusive, total is 8 bits.
     * We should get first and second index of array at least:
     * int i1 = array[0] & 0x000000FF;
     * int i2 = array[1] & 0x000000FF;
     * Then extract bits we need:
     * int i = (i1 << 8) | i2;
     * return ((i << 3) & 0x000000FF) >>> 8;
     * The lowest 8 orders are bits we need, before this, we should know
     * i1 and i2, total 16 bits, and this method returns the 16 bits.
     * </pre>
     *
     * Note the returned value always multiple of element wide of array.
     * </p>
     *
     * @param offset
     *            given offset of element, [0, 63]
     * @param bitsNum
     *            actual bits number required, [1, 64]
     * @return number of bits required at least
     * @since 0.0.0
     */
    private int leastRequireBitsByOffsetAndBitsNum(final int offset,
            final int bitsNum) {
        final int i = offset + bitsNum;
        final int elementsNum = i % elementWide == 0 ? i / elementWide : i
                / elementWide + 1;
        return elementsNum * elementWide;
    }

    /**
     * <p>
     * Reads int value of next at least 1 bits from specified absolute position
     * in bits. The absolute position is detail index and bit offset of array of
     * primitive type, high 32 order is index of array, low 32 orders is bit
     * offset of the index. If read number of read bits is less than 32, it will
     * be promoted to int type by filling 0 in front.
     * </p>
     *
     * @param absolutePos
     *            specified absolute position in bits, [0L, bit length - 1]
     * @param bitsNum
     *            specified number of bits, [1, 32]
     * @return int value of next at least 1 bits from specified absolute
     *         position in bits
     * @since 0.0.0
     */
    protected int readBitsByAbsolutePos(final long absolutePos,
            final int bitsNum) {
        final int index = (int) (absolutePos >>> 32);
        final int offset = (int) absolutePos;
        final int leastBits = leastRequireBitsByOffsetAndBitsNum(offset,
                bitsNum);
        if (leastBits <= 32) {
            return BitsQuicker.partOfInt(
                    readArrayElements(index, leastBits / elementWide), offset,
                    offset + bitsNum);
        } else if (leastBits <= 64) {
            return (int) BitsQuicker.partOfLong(
                    readArrayElementsAsLong(index, leastBits / elementWide),
                    offset, offset + bitsNum);
        } else {// Certainly <= 128
            final int headElementCount = 64 / elementWide;
            final int tailElementCount = (leastBits - 64) / elementWide;
            long l1 = readArrayElementsAsLong(index, headElementCount);
            long l2 = readArrayElementsAsLong(index + headElementCount,
                    tailElementCount);
            l1 <<= offset;
            l1 >>>= (64 - bitsNum);
            l2 >>>= (128 - offset - bitsNum);
            return (int) (l1 | l2);
        }
    }

    /**
     * <p>
     * Reads long value of next at least 1 bits from specified absolute position
     * in bits. The absolute position is detail index and bit offset of array of
     * primitive type, high 32 order is index of array, low 32 orders is bit
     * offset of the index. If read number of read bits is less than 64, it will
     * be promoted to long type by filling 0 in front.
     * </p>
     *
     * @param absolutePos
     *            specified absolute position in bits, [0L, bit length - 1]
     * @param bitsNum
     *            specified number of bits, [1, 64]
     * @return long value of next at least 1 bits from specified absolute
     *         position in bits
     * @since 0.0.0
     */
    protected long readBitsByAbsolutePosAsLong(final long absolutePos,
            final int bitsNum) {
        if (bitsNum <= 32) {
            return readBitsByAbsolutePos(absolutePos, bitsNum) & 0x00000000FFFFFFFFL;
        }
        final int index = (int) (absolutePos >>> 32);
        final int offset = (int) absolutePos;
        final int leastBits = leastRequireBitsByOffsetAndBitsNum(offset,
                bitsNum);
        if (leastBits <= 64) {// Certainly > 32
            return BitsQuicker.partOfLong(
                    readArrayElementsAsLong(index, leastBits / elementWide),
                    offset, offset + bitsNum);
        } else {// Certainly <= 128
            final int headElementCount = 64 / elementWide;
            final int tailElementCount = (leastBits - 64) / elementWide;
            long l1 = readArrayElementsAsLong(index, headElementCount);
            long l2 = readArrayElementsAsLong(index + headElementCount,
                    tailElementCount);
            l1 <<= offset;
            l1 >>>= (64 - bitsNum);
            l2 >>>= (128 - offset - bitsNum);
            return (l1 | l2);
        }
    }

    /**
     * <p>
     * Writes specified number of bits from specified absolute position in bits,
     * with high-to-low (left-to-right, big-endian) orders. The absolute
     * position is detail index and bit offset of array of primitive type, high
     * 32 order is index of array, low 32 orders is bit offset of the index.
     * </p>
     *
     * @param absolutePos
     *            specified position in bits, [0L, bit length - 1]
     * @param value
     *            written value
     * @param bitsNum
     *            specified number of bits to be put, [1, 32]
     * @since 0.0.0
     */
    protected void writeBitsByAbsolutePos(final long absolutePos,
            final int value, final int bitsNum) {
        final int index = (int) (absolutePos >>> 32);
        final int offset = (int) absolutePos;
        final int leastBits = leastRequireBitsByOffsetAndBitsNum(offset,
                bitsNum);
        if (leastBits <= 32) {
            int i = readArrayElements(index, leastBits / elementWide);
            i = BitsQuicker.bitsCopy(value, 0, i, offset, bitsNum);
            writeArrayElements(index, i, leastBits / elementWide);
        } else if (leastBits <= 64) {
            long l = readArrayElementsAsLong(index, leastBits / elementWide);
            l = BitsQuicker.bitsCopy(((long) value) << 32, 0, l, offset, bitsNum);
            writeArrayElements(index, l, leastBits / elementWide);
        } else {// Certainly <= 128
            final int headElementCount = 64 / elementWide;
            final int tailElementCount = (leastBits - 64) / elementWide;
            long l1 = readArrayElementsAsLong(index, headElementCount);
            long l2 = readArrayElementsAsLong(index + headElementCount,
                    tailElementCount);
            final long v = ((long) value) << 32;
            l1 = BitsQuicker.bitsCopy(v, 0, l1, offset, 64 - offset);
            l2 = BitsQuicker
                    .bitsCopy(v, 64 - offset, l2, 0, offset + bitsNum - 64);
            writeArrayElements(index, l1, headElementCount);
            writeArrayElements(index + headElementCount, l2, tailElementCount);
        }
    }

    /**
     * <p>
     * Writes specified number of bits from specified absolute position in bits,
     * with high-to-low (left-to-right, big-endian) orders. The absolute
     * position is detail index and bit offset of array of primitive type, high
     * 32 order is index of array, low 32 orders is bit offset of the index.
     * </p>
     *
     * @param absolutePos
     *            specified position in bits, [0L, bit length - 1]
     * @param value
     *            written value
     * @param bitsNum
     *            specified number of bits to be put, [1, 64]
     * @since 0.0.0
     */
    protected void writeBitsByAbsolutePos(final long absolutePos,
            final long value, final int bitsNum) {
        if (bitsNum <= 32) {
            writeBitsByAbsolutePos(absolutePos, (int) (value >>> 32), bitsNum);
            return;
        }
        final int index = (int) (absolutePos >>> 32);
        final int offset = (int) absolutePos;
        final int leastBits = leastRequireBitsByOffsetAndBitsNum(offset,
                bitsNum);
        if (leastBits <= 64) {// Certainly > 32
            long l = readArrayElementsAsLong(index, leastBits / elementWide);
            l = BitsQuicker.bitsCopy(value, 0, l, offset, bitsNum);
            writeArrayElements(index, l, leastBits / elementWide);
        } else {// Certainly <= 128
            final int headElementCount = 64 / elementWide;
            final int tailElementCount = (leastBits - 64) / elementWide;
            long l1 = readArrayElementsAsLong(index, headElementCount);
            long l2 = readArrayElementsAsLong(index + headElementCount,
                    tailElementCount);
            // long v = ((long) value) << 32;//bug
            final long v = value;
            l1 = BitsQuicker.bitsCopy(v, 0, l1, offset, 64 - offset);
            l2 = BitsQuicker
                    .bitsCopy(v, 64 - offset, l2, 0, offset + bitsNum - 64);
            writeArrayElements(index, l1, headElementCount);
            writeArrayElements(index + headElementCount, l2, tailElementCount);
        }
    }

    /**
     * <p>
     * Reads int value of next at least 1 bits from specified position in bits.
     * If read number of read bits is less than 32, it will be promoted to int
     * type by filling 0 in front.
     * </p>
     *
     * @param posInBits
     *            specified position in bits, [0L, bit length - specified number
     *            of bits]
     * @param bitsNum
     *            specified number of bits, [1, 32]
     * @return int value of next at least 1 bits from specified position in bits
     * @since 0.0.0
     */
    protected int read(final long posInBits, final int bitsNum) {
        final long absolutePos = absolutePos(posInBits);
        return readBitsByAbsolutePos(absolutePos, bitsNum);
    }

    /**
     * <p>
     * Reads long value of next at least 1 bits from specified position in bits.
     * If read number of read bits is less than 64, it will be promoted to long
     * type by filling 0 in front.
     * </p>
     *
     * @param posInBits
     *            specified position in bits, [0L, bit length - specified number
     *            of bits]
     * @param bitsNum
     *            specified number of bits, [1, 64]
     * @return long value of next at least 1 bits from specified position in
     *         bits
     * @since 0.0.0
     */
    protected long readAsLong(final long posInBits, final int bitsNum) {
        final long absolutePos = absolutePos(posInBits);
        return readBitsByAbsolutePosAsLong(absolutePos, bitsNum);
    }

    /**
     * <p>
     * Writes specified number of bits from specified position in bits, with
     * high-to-low (left-to-right, big-endian) orders.
     * </p>
     *
     * @param posInBits
     *            specified position in bits, [0L, bit length - specified number
     *            of bits to be put]
     * @param value
     *            written value
     * @param bitsNum
     *            specified number of bits to be put, [1, 32]
     * @since 0.0.0
     */
    protected void write(final long posInBits, final int value,
            final int bitsNum) {
        final long absolutePos = absolutePos(posInBits);
        writeBitsByAbsolutePos(absolutePos, value, bitsNum);
    }

    /**
     * <p>
     * Writes specified number of bits from specified position in bits, with
     * high-to-low (left-to-right, big-endian) orders.
     * </p>
     *
     * @param posInBits
     *            specified position in bits, [0L, bit length - specified number
     *            of bits to be put]
     * @param value
     *            written value
     * @param bitsNum
     *            specified number of bits to be put, [1, 64]
     * @since 0.0.0
     */
    protected void write(final long posInBits, final long value,
            final int bitsNum) {
        final long absolutePos = absolutePos(posInBits);
        writeBitsByAbsolutePos(absolutePos, value, bitsNum);
    }

    @Override
    public boolean readBoolean(final int posInBytes) {
        final int i = read(posInBytes * 8L, 1);
        return i == 1 ? true : false;
    }

    @Override
    public boolean readBoolean(final long posInBits) {
        final int i = read(posInBits, 1);
        return i == 1 ? true : false;
    }

    @Override
    public byte readByte(final int posInBytes) {
        return (byte) read(posInBytes * 8L, 8);
    }

    @Override
    public byte readByte(final long posInBits) {
        return (byte) read(posInBits, 8);
    }

    @Override
    public short readShort(final int posInBytes) {
        return (short) read(posInBytes * 8L, 16);
    }

    @Override
    public short readShort(final long posInBits) {
        return (short) read(posInBits, 16);
    }

    @Override
    public char readChar(final int posInBytes) {
        return (char) read(posInBytes * 8L, 16);
    }

    @Override
    public char readChar(final long posInBits) {
        return (char) read(posInBits, 16);
    }

    @Override
    public int readInt(final int posInBytes) {
//        final long absolutePos = absolutePos(posInBytes * 8L);
//        final int index = (int) (absolutePos >>> 32);
//        final int offset = (int) absolutePos;
//        if (offset == 0) {
//            return readIntDirect(posInBytes);
//        } else {
            return read(posInBytes * 8L, 32);
       // }
    }

    abstract protected int readIntDirect(final int posInBytes);

    @Override
    public int readInt(final long posInBits) {
        return read(posInBits, 32);
    }

    @Override
    public long readLong(final int posInBytes) {
        return readAsLong(posInBytes * 8L, 64);
    }

    @Override
    public long readLong(final long posInBits) {
        return readAsLong(posInBits, 64);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Note if specified number of bits is 8, 16 or 32, this method will only
     * called corresponding readXxx method and return directly. If sub class
     * want to optimize for the 8, 16 or 32, just override corresponding readXxx
     * method.
     * </p>
     */
    @Override
    public int readBitsAsInt(final int posInBytes, final int bitsNum) {
        switch (bitsNum) {
            case 8: {
                return readByte(posInBytes) & 0x000000FF;
            }
            case 16: {
                return readShort(posInBytes) & 0x0000FFFF;
            }
            case 32: {
                return readInt(posInBytes);
            }
            default: {
                return read(posInBytes * 8L, bitsNum);
            }
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Note if specified number of bits is 8, 16 or 32, this method will only
     * called corresponding readXxx method and return directly. If sub class
     * want to optimize for the 8, 16 or 32, just override corresponding readXxx
     * method.
     * </p>
     */
    @Override
    public int readBitsAsInt(final long posInBits, final int bitsNum) {
        switch (bitsNum) {
            case 8: {
                return readByte(posInBits) & 0x000000FF;
            }
            case 16: {
                return readShort(posInBits) & 0x0000FFFF;
            }
            case 32: {
                return readInt(posInBits);
            }
            default: {
                return read(posInBits, bitsNum);
            }
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Note if specified number of bits is 8, 16, 32 or 64, this method will
     * only called corresponding readXxx method and return directly. If sub
     * class want to optimize for the 8, 16, 32 or 64, just override
     * corresponding readXxx method.
     * </p>
     */
    @Override
    public long readBitsAsLong(final int posInBytes, final int bitsNum) {
        switch (bitsNum) {
            case 8: {
                return readByte(posInBytes) & 0x00000000000000FFL;
            }
            case 16: {
                return readShort(posInBytes) & 0x000000000000FFFFL;
            }
            case 32: {
                return readInt(posInBytes) & 0x00000000FFFFFFFFL;
            }
            case 64: {
                return readLong(posInBytes);
            }
            default: {
                return readAsLong(posInBytes * 8L, bitsNum);
            }
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Note if specified number of bits is 8, 16, 32 or 64, this method will
     * only called corresponding readXxx method and return directly. If sub
     * class want to optimize for the 8, 16, 32 or 64, just override
     * corresponding readXxx method.
     * </p>
     */
    @Override
    public long readBitsAsLong(final long posInBits, final int bitsNum) {
        switch (bitsNum) {
            case 8: {
                return readByte(posInBits) & 0x00000000000000FFL;
            }
            case 16: {
                return readShort(posInBits) & 0x000000000000FFFFL;
            }
            case 32: {
                return readInt(posInBits) & 0x00000000FFFFFFFFL;
            }
            case 64: {
                return readLong(posInBits);
            }
            default: {
                return readAsLong(posInBits, bitsNum);
            }
        }
    }

    @Override
    public void writeBoolean(final int posInBytes, final boolean value) {
        write(posInBytes * 8L, value ? 0x80000000 : 0, 1);
    }

    @Override
    public void writeBoolean(final long posInBits, final boolean value) {
        write(posInBits, value ? 0x80000000 : 0, 1);
    }

    @Override
    public void writeByte(final int posInBytes, final byte value) {
        write(posInBytes * 8L, value << 24, 8);
    }

    @Override
    public void writeByte(final long posInBits, final byte value) {
        write(posInBits, value << 24, 8);
    }

    @Override
    public void writeShort(final int posInBytes, final short value) {
        write(posInBytes * 8L, value << 16, 16);
    }

    @Override
    public void writeShort(final long posInBits, final short value) {
        write(posInBits, value << 16, 16);
    }

    @Override
    public void writeChar(final int posInBytes, final char value) {
        write(posInBytes * 8L, value << 16, 16);
    }

    @Override
    public void writeChar(final long posInBits, final char value) {
        write(posInBits, value << 16, 16);
    }

    @Override
    public void writeInt(final int posInBytes, final int value) {
//        final long absolutePos = absolutePos(posInBytes * 8L);
//        final int index = (int) (absolutePos >>> 32);
//        final int offset = (int) absolutePos;
//        if (offset == 0) {
//            writeIntDirect(posInBytes, value);
//        } else {
            write(posInBytes * 8L, value, 32);
       // }
    }
    abstract protected void writeIntDirect(final int posInBytes, final int value);

    @Override
    public void writeInt(final long posInBits, final int value) {
        write(posInBits, value, 32);
    }

    @Override
    public void writeLong(final int posInBytes, final long value) {
        write(posInBytes * 8L, value, 64);
    }

    @Override
    public void writeLong(final long posInBits, final long value) {
        write(posInBits, value, 64);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Note if specified number of bits is 8, 16 or 32, this method will only
     * called corresponding writeXxx method and return directly. If sub class
     * want to optimize for the 8, 16 or 32, just override corresponding
     * writeXxx method.
     * </p>
     */
    @Override
    public void writeBits(final int posInBytes, final int value,
            final int bitsNum) {
        switch (bitsNum) {
            case 8: {
                writeByte(posInBytes, (byte) (value >>> 24));
                return;
            }
            case 16: {
                writeShort(posInBytes, (short) (value >>> 16));
                return;
            }
            case 32: {
                writeInt(posInBytes, value);
                return;
            }
            default: {
                write(posInBytes * 8L, value, bitsNum);
            }
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Note if specified number of bits is 8, 16 or 32, this method will only
     * called corresponding writeXxx method and return directly. If sub class
     * want to optimize for the 8, 16 or 32, just override corresponding
     * writeXxx method.
     * </p>
     */
    @Override
    public void writeBits(final long posInBits, final int value,
            final int bitsNum) {
        switch (bitsNum) {
            case 8: {
                writeByte(posInBits, (byte) (value >>> 24));
                return;
            }
            case 16: {
                writeShort(posInBits, (short) (value >>> 16));
                return;
            }
            case 32: {
                writeInt(posInBits, value);
                return;
            }
            default: {
                write(posInBits, value, bitsNum);
            }
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Note if specified number of bits is 8, 16, 32 or 64, this method will
     * only called corresponding writeXxx method and return directly. If sub
     * class want to optimize for the 8, 16, 32 or 64, just override
     * corresponding writeXxx method.
     * </p>
     */
    @Override
    public void writeBits(final int posInBytes, final long value,
            final int bitsNum) {
        switch (bitsNum) {
            case 8: {
                writeByte(posInBytes, (byte) (value >>> 56));
                return;
            }
            case 16: {
                writeShort(posInBytes, (short) (value >>> 48));
                return;
            }
            case 32: {
                writeInt(posInBytes, (int) (value >>> 32));
                return;
            }
            case 64: {
                writeLong(posInBytes, value);
                return;
            }
            default: {
                write(posInBytes * 8L, value, bitsNum);
            }
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Note if specified number of bits is 8, 16, 32 or 64, this method will
     * only called corresponding writeXxx method and return directly. If sub
     * class want to optimize for the 8, 16, 32 or 64, just override
     * corresponding writeXxx method.
     * </p>
     */
    @Override
    public void writeBits(final long posInBits, final long value,
            final int bitsNum) {
        switch (bitsNum) {
            case 8: {
                writeByte(posInBits, (byte) (value >>> 56));
                return;
            }
            case 16: {
                writeShort(posInBits, (short) (value >>> 48));
                return;
            }
            case 32: {
                writeInt(posInBits, (int) (value >>> 32));
                return;
            }
            case 64: {
                writeLong(posInBits, value);
                return;
            }
            default: {
                write(posInBits, value, bitsNum);
            }
        }
    }

    @Override
    public int toBooleanArray(final boolean[] dest, final int startIndex) {
        return (int) copyTo(new BooleanArrayBaseAccessor(dest, startIndex));
    }

    @Override
    public long toByteArray(final byte[] dest, final int startIndex,
            final int bitOffset) {
        return copyTo(new ByteArrayBaseAccessor(dest, startIndex, bitOffset));
    }

    @Override
    public long toShortArray(final short[] dest, final int startIndex,
            final int bitOffset) {
        return copyTo(new ShortArrayBaseAccessor(dest, startIndex, bitOffset));
    }

    @Override
    public long toCharArray(final char[] dest, final int startIndex,
            final int bitOffset) {
        return copyTo(new CharArrayBaseAccessor(dest, startIndex, bitOffset));
    }

    @Override
    public long toIntArray(final int[] dest, final int startIndex,
            final int bitOffset) {
        return copyTo(new IntArrayBaseAccessor(dest, startIndex, bitOffset));
    }

    @Override
    public long toFloatArray(final float[] dest, final int startIndex,
            final int bitOffset) {
        return copyTo(new FloatArrayBaseAccessor(dest, startIndex, bitOffset));
    }

    @Override
    public long toLongArray(final long[] dest, final int startIndex,
            final int bitOffset) {
        return copyTo(new LongArrayBaseAccessor(dest, startIndex, bitOffset));
    }

    @Override
    public long toDoubleArray(final double[] dest, final int startIndex,
            final int bitOffset) {
        return copyTo(new DoubleArrayBaseAccessor(dest, startIndex, bitOffset));
    }

    /**
     * <p>
     * Gets required array length by given element wide and length of this
     * accessor in bits. For example, if this accessor's length in bits is 9,
     * given element wide is 8 (byte type), then data of this accessor need
     * 2-element length byte[] to store.
     * </p>
     *
     * @param elementWide
     *            element wide in bits
     * @return required array length
     * @since 0.0.0
     */
    protected int getRequiredArrayLength(final int elementWide) {
        // return (int) (lengthInBits % elementWide == 0 ? lengthInBits
        // / elementWide : lengthInBits / elementWide + 1);
        return BitsQuicker.leastLengthOfBitsToArray(lengthInBits, elementWide);
    }

    @Override
    public boolean[] toBooleanArray() {
        final boolean[] array = new boolean[getRequiredArrayLength(1)];
        copyTo(new BooleanArrayBaseAccessor(array, 0));
        return array;
    }

    @Override
    public byte[] toByteArray() {
        final byte[] array = new byte[getRequiredArrayLength(8)];
        copyTo(new ByteArrayBaseAccessor(array, 0, 0));
        return array;
    }

    @Override
    public short[] toShortArray() {
        final short[] array = new short[getRequiredArrayLength(32)];
        copyTo(new ShortArrayBaseAccessor(array, 0, 0));
        return array;
    }

    @Override
    public char[] toCharArray() {
        final char[] array = new char[getRequiredArrayLength(16)];
        copyTo(new CharArrayBaseAccessor(array, 0, 0));
        return array;
    }

    @Override
    public int[] toIntArray() {
        final int[] array = new int[getRequiredArrayLength(32)];
        copyTo(new IntArrayBaseAccessor(array, 0, 0));
        return array;
    }

    @Override
    public float[] toFloatArray() {
        final float[] array = new float[getRequiredArrayLength(32)];
        copyTo(new FloatArrayBaseAccessor(array, 0, 0));
        return array;
    }

    @Override
    public long[] toLongArray() {
        final long[] array = new long[getRequiredArrayLength(64)];
        copyTo(new LongArrayBaseAccessor(array, 0, 0));
        return array;
    }

    @Override
    public double[] toDoubleArray() {
        final double[] array = new double[getRequiredArrayLength(64)];
        copyTo(new DoubleArrayBaseAccessor(array, 0, 0));
        return array;
    }

    /**
     * <p>
     * Returns hash code. The hash code is generated by
     * {@linkplain Long#hashCode(long)}, the long argument of that method is
     * accumulated by adding each 64-bits-lone part of instance with
     * {@linkplain #readLong(long)}, or {@linkplain #readBitsAsLong(long, int)}
     * if length in bits of last part is less than 64 bits.
     * </p>
     *
     * @return hash code generated by {@linkplain Long#hashCode(long)}
     */
    @Override
    public int hashCode() {
        final long longCount = lengthInBits / 64L;
        long pos = 0L;
        long l = 0L;
        for (long p = 0; p < longCount; p++) {
            pos = p * 64L;
            l += readLong(pos);
        }
        final int restBits = (int) (lengthInBits % 64L);
        if (restBits > 0) {
            pos = longCount * 64L;
            l += readBitsAsLong(pos, restBits) << (64 - restBits);
        }
        return Long.hashCode(l);
    }

    /**
     * <p>
     * Returns whether content of given instance of {@linkplain Bits}
     * and this instance are equal, true if equal, else false.
     * </p>
     *
     * @param obj
     *            given instance
     * @return whether content of given instance and this instance are equal
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (null == obj) {
            return false;
        }
        if (!(obj instanceof Bits)) {
            return false;
        }
        final Bits compared = (Bits) obj;
        if (this.lengthInBits != compared.lengthInBits()) {
            return false;
        }
        final long longCount = lengthInBits() / 64L;
        long pos = 0L;
        for (long p = 0; p < longCount; p++) {
            pos = p * 64L;
            if (!(this.readLong(pos) == compared.readLong(pos))) {
                return false;
            }
        }
        final int restBits = (int) (lengthInBits() % 64L);
        if (restBits > 0) {
            pos = longCount * 64L;
            return this.readBitsAsLong(pos, restBits) << (64 - restBits) == compared
                    .readBitsAsLong(pos, restBits) << (64 - restBits);
        }
        return true;
    }
}