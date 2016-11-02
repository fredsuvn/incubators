package com.cogician.quicker.binary.data.impl.primitivearray;

import com.cogician.quicker.annotation.Base;
import com.cogician.quicker.binary.BaseException;
import com.cogician.quicker.binary.Bits;
import com.cogician.quicker.binary.Bitss;

/**
 * <p>
 * Implementation of {@link Bits}, more detail for seeing
 * {@link Bits}. Note this implementation cannot be empty.
 * </p>
 * <p>
 * This implementation use short array to store and manipulate data randomly,
 * each element takes 16 bits. The effective data start from specified bit
 * offset of start index, end to specified bit offset of end index. This class
 * is one subclass of XxxArrayBaseAccessor in this package.
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
 * @version 0.0.0, 2015-06-01 15:33:32
 * @since 0.0.0
 * @see Bits
 * @see Bitss
 */
final class ShortArrayBaseAccessor extends Bitss {

    /**
     * Serial ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Array to store data.
     */
    private final short[] array;

    /**
     * <p>
     * Constructs an instance with specified array, start index, bit offset of
     * start index, end index and bit offset of end index. End position
     * indicated by end index and bit offset of end index should be greater than
     * or equal to start position indicated by start index and bit offset of
     * start index.
     * </p>
     *
     * @param array
     *            specified array, not null
     * @param startIndex
     *            start index, [0, array length - 1]
     * @param startOffset
     *            bit offset of start index, [0, 15]
     * @param endIndex
     *            end index, [start index, array length - 1]
     * @param endOffset
     *            bit offset of end index, [0, 15]
     */
    ShortArrayBaseAccessor(final short[] array, final int startIndex,
            final int startOffset, final int endIndex, final int endOffset) {
        super(startIndex, startOffset, endIndex, endOffset);
        this.array = array;
    }

    /**
     * <p>
     * Constructs an instance with specified array, start index and bit offset
     * of start index. Data included from start position indicated by start
     * index and bit offset of start index to end of array.
     * </p>
     *
     * @param array
     *            specified array, not null
     * @param startIndex
     *            start index, [0, array length - 1]
     * @param startOffset
     *            bit offset of start index, [0, 15]
     */
    ShortArrayBaseAccessor(final short[] array, final int startIndex,
            final int startOffset) {
        this(array, startIndex, startOffset, array.length - 1, 15);
    }

    /**
     * <p>
     * Constructs an instance with specified array. All array's data included.
     * </p>
     *
     * @param array
     *            specified array, not null
     */
    ShortArrayBaseAccessor(final short[] array) {
        this(array, 0, 0, array.length - 1, 15);
    }

    @Override
    protected int elementWide() {
        return 16;
    }

    @Override
    protected Bits shadowClone(final int startIndex,
            final int startOffset, final int endIndex, final int endOffset) {
        return new ShortArrayBaseAccessor(array, startIndex, startOffset,
                endIndex, endOffset);
    }

    @Override
    public Bits deepClone() {
        // Byte alignment.
        // If startOffset is not 0, deep copied accessor's startOffset should be
        // aligned to 0.
        if (startOffset == 0) {
            final short[] clone = new short[endIndex - startIndex + 1];
            System.arraycopy(array, startIndex, clone, 0, clone.length);
            return new ShortArrayBaseAccessor(clone, 0, 0, clone.length - 1,
                    endOffset);
        } else {
            final short[] clone = new short[lengthInBits % elementWide == 0 ? (int) (lengthInBits / elementWide)
                    : (int) (lengthInBits / elementWide + 1)];
            final Bits cloneAccessor = new ShortArrayBaseAccessor(
                    clone, 0, 0, clone.length - 1,
                    (int) ((lengthInBits - 1L) % elementWide));
            this.copyTo(cloneAccessor);
            return cloneAccessor;
        }
    }

    @Override
    public long copyTo(final Bits dest) {
        if (this.lengthInBits >= MEM_COPY_OPTIMIZATION_THRESHOLD_IN_BITS
                && dest.lengthInBits() >= MEM_COPY_OPTIMIZATION_THRESHOLD_IN_BITS
                && dest instanceof ShortArrayBaseAccessor) {
            final ShortArrayBaseAccessor shorter = (ShortArrayBaseAccessor) PrimitiveImplUtil
                    .getShorterAccessor(this, dest);
            final ShortArrayBaseAccessor copyDest = (ShortArrayBaseAccessor) dest;
            if (this.startOffset == copyDest.startOffset) {
                if (this.startOffset == 0
                        && this.endOffset == this.elementWide - 1
                        && copyDest.endOffset == this.endOffset) {
                    System.arraycopy(this.array, this.startIndex,
                            copyDest.array, copyDest.startIndex,
                            shorter.endIndex - shorter.startIndex + 1);
                    return shorter.lengthInBits;
                } else if (this.startOffset == 0
                        && shorter.endIndex - shorter.startIndex >= 1) {
                    // Copies except tail
                    System.arraycopy(this.array, this.startIndex,
                            copyDest.array, copyDest.startIndex,
                            shorter.endIndex - shorter.startIndex);
                    // Copies tail
                    final int len = shorter.endIndex - shorter.startIndex;
                    short thisTail = this.array[this.startIndex + len];
                    short destTail = copyDest.array[copyDest.startIndex + len];
                    thisTail >>>= (elementWide - shorter.endOffset);
                    thisTail <<= (elementWide - shorter.endOffset);
                    destTail <<= (shorter.endOffset + 16);
                    destTail >>>= (shorter.endOffset + 16);
                    copyDest.array[copyDest.startIndex + len] = (short) (thisTail | destTail);
                    return shorter.lengthInBits;
                } else if (this.startOffset != 0
                        && shorter.endIndex - shorter.startIndex >= 2) {
                    // Copies except head and tail
                    System.arraycopy(this.array, this.startIndex + 1,
                            copyDest.array, copyDest.startIndex + 1,
                            shorter.endIndex - shorter.startIndex - 1);
                    // Copies head
                    short thisHead = this.array[this.startIndex];
                    short destHead = copyDest.array[copyDest.startIndex];
                    thisHead <<= (this.startOffset + 16);
                    thisHead >>>= (this.startOffset + 16);
                    destHead >>>= (elementWide - this.startOffset);
                    destHead <<= (elementWide - this.startOffset);
                    copyDest.array[copyDest.startIndex] = (short) (thisHead | destHead);
                    // Copies tail
                    final int len = shorter.endIndex - shorter.startIndex;
                    short thisTail = this.array[this.startIndex + len];
                    short destTail = copyDest.array[copyDest.startIndex + len];
                    thisTail >>>= (elementWide - shorter.endOffset);
                    thisTail <<= (elementWide - shorter.endOffset);
                    destTail <<= (shorter.endOffset + 16);
                    destTail >>>= (shorter.endOffset + 16);
                    copyDest.array[copyDest.startIndex + len] = (short) (thisTail | destTail);
                    return shorter.lengthInBits;
                }
            }
        }
        return super.copyTo(dest);
    };

    @Override
    public Object getSourceData() {
        return array;
    }

    @Override
    protected int readArrayElements(final int index, final int elementsNum) {
        switch (elementsNum) {
            case 1: {
                return (array[index] & 0x0000FFFF) << 16;
            }
            case 2: {
                return ((array[index] & 0x0000FFFF) << 16)
                        | ((array[index + 1] & 0x0000FFFF) << 0);
            }
            default: {
                throw new BaseException("Elements number should be 1, 2.");
            }
        }
    }

    @Override
    protected long readArrayElementsAsLong(final int index,
            final int elementsNum) {
        switch (elementsNum) {
            case 1:
            case 2: {
                final long l = readArrayElements(index, elementsNum);
                return l << 32;
            }
            case 3: {
                final long l1 = readArrayElements(index, 2);
                final long l2 = readArrayElements(index + 2, 1) & 0x00000000FFFFFFFFL;
                return (l1 << 32) | l2;
            }
            case 4: {
                final long l1 = readArrayElements(index, 2);
                final long l2 = readArrayElements(index + 2, 2) & 0x00000000FFFFFFFFL;
                return (l1 << 32) | l2;
            }
            default: {
                throw new BaseException("Elements number should be 1, 2, 3, 4.");
            }
        }
    }

    @Override
    protected void writeArrayElements(final int index, final int value,
            final int elementsNum) {
        switch (elementsNum) {
            case 1: {
                array[index] = (short) (value >>> 16);
                return;
            }
            case 2: {
                array[index] = (short) (value >>> 16);
                array[index + 1] = (short) (value >>> 0);
                return;
            }
            default: {
                throw new BaseException("Elements number should be 1, 2.");
            }
        }
    }

    @Override
    protected void writeArrayElements(final int index, final long value,
            final int elementsNum) {
        switch (elementsNum) {
            case 1: {
                array[index] = (short) (value >>> 48);
                return;
            }
            case 2: {
                array[index] = (short) (value >>> 48);
                array[index + 1] = (short) (value >>> 32);
                return;
            }
            case 3: {
                array[index] = (short) (value >>> 48);
                array[index + 1] = (short) (value >>> 32);
                array[index + 2] = (short) (value >>> 16);
                return;
            }
            case 4: {
                array[index] = (short) (value >>> 48);
                array[index + 1] = (short) (value >>> 32);
                array[index + 2] = (short) (value >>> 16);
                array[index + 3] = (short) (value >>> 0);
                return;
            }
            default: {
                throw new BaseException("Elements number should be 1, 2, 3, 4.");
            }
        }
    }

    // @Override
    // protected int read1to7Bits(long posInBits, int bitsNum) {
    // long absolutePos = absolutePos(posInBits);
    // int index = (int) (absolutePos >>> 32);
    // int offset = (int) absolutePos;
    // if (offset == 0) {
    // return (array[index] & 0x0000FFFF) >>> (16 - bitsNum);
    // } else if (offset + bitsNum <= 16) {
    // int i = (array[index] << offset) & 0x0000FFFF;
    // i >>>= (16 - bitsNum);
    // return i;
    // } else {
    // int i = ((array[index] & 0x0000FFFF) << 16)
    // | (array[index + 1] & 0x0000FFFF);
    // i <<= offset;
    // i >>>= (32 - bitsNum);
    // return i;
    // }
    // }
    //
    // @Override
    // protected int readBytes(long posInBits, int bytesNum) {
    // long absolutePos = absolutePos(posInBits);
    // int index = (int) (absolutePos >>> 32);
    // int offset = (int) absolutePos;
    // if (offset == 0) {
    // switch (bytesNum) {
    // case 1: {
    // return (array[index] & 0x0000FFFF) >>> 8;
    // }
    // case 2: {
    // return array[index] & 0x0000FFFF;
    // }
    // case 3: {
    // int i = ((array[index] & 0x0000FFFF) << 16)
    // | (array[index + 1] & 0x0000FFFF);
    // return i >>> 8;
    // }
    // case 4: {
    // return ((array[index] & 0x0000FFFF) << 16)
    // | (array[index + 1] & 0x0000FFFF);
    // }
    // default: {
    // throw new IllegalArgumentException(
    // "bytesNum should be one of (1, 2, 3, 4).");
    // }
    // }
    // } else {
    // switch (bytesNum) {
    // case 1: {
    // if (offset <= 8) {
    // int i = (array[index] << offset) & 0x0000FFFF;
    // return i >>> 8;
    // } else {
    // int i = ((array[index] & 0x0000FFFF) << 16)
    // | (array[index + 1] & 0x0000FFFF);
    // return (i << offset) >>> 24;
    // }
    // }
    // case 2: {
    // int i = ((array[index] & 0x0000FFFF) << 16)
    // | (array[index + 1] & 0x0000FFFF);
    // return (i << offset) >>> 16;
    // }
    // case 3: {
    // if (offset <= 8) {
    // int i = ((array[index] & 0x0000FFFF) << 16)
    // | (array[index + 1] & 0x0000FFFF);
    // return (i << offset) >>> 8;
    // } else {
    // int i1 = ((array[index] & 0x0000FFFF) << 16)
    // | (array[index + 1] & 0x0000FFFF);
    // int i2 = array[index + 2] & 0x0000FFFF;
    // i1 <<= offset;
    // i1 >>>= 8;
    // i2 >>>= (16 - offset);
    // return i1 | i2;
    // }
    // }
    // case 4: {
    // int i1 = ((array[index] & 0x0000FFFF) << 16)
    // | (array[index + 1] & 0x0000FFFF);
    // int i2 = array[index + 2] & 0x0000FFFF;
    // i1 <<= offset;
    // i2 >>>= (16 - offset);
    // return i1 | i2;
    // }
    // default: {
    // throw new IllegalArgumentException(
    // "bytesNum should be one of (1, 2, 3, 4).");
    // }
    // }
    // }
    // }
    //
    // @Override
    // protected long readBytesAsLong(long posInBits, int bytesNum) {
    // if (bytesNum <= 4) {
    // return readBytes(posInBits, bytesNum) & 0x00000000FFFFFFFFL;
    // }
    // long absolutePos = absolutePos(posInBits);
    // int index = (int) (absolutePos >>> 32);
    // int offset = (int) absolutePos;
    // if (offset == 0) {
    // switch (bytesNum) {
    // case 5: {
    // return ((array[index] & 0x00000000000000FFL) << 32)
    // | ((array[index + 1] & 0x00000000000000FFL) << 24)
    // | ((array[index + 2] & 0x00000000000000FFL) << 16)
    // | ((array[index + 3] & 0x00000000000000FFL) << 8)
    // | (array[index + 4] & 0x00000000000000FFL);
    // }
    // case 6: {
    // return ((array[index] & 0x00000000000000FFL) << 40)
    // | ((array[index + 1] & 0x00000000000000FFL) << 32)
    // | ((array[index + 2] & 0x00000000000000FFL) << 24)
    // | ((array[index + 3] & 0x00000000000000FFL) << 16)
    // | ((array[index + 4] & 0x00000000000000FFL) << 8)
    // | (array[index + 5] & 0x00000000000000FFL);
    // }
    // case 7: {
    // return ((array[index] & 0x00000000000000FFL) << 48)
    // | ((array[index + 1] & 0x00000000000000FFL) << 40)
    // | ((array[index + 2] & 0x00000000000000FFL) << 32)
    // | ((array[index + 3] & 0x00000000000000FFL) << 24)
    // | ((array[index + 4] & 0x00000000000000FFL) << 16)
    // | ((array[index + 5] & 0x00000000000000FFL) << 8)
    // | (array[index + 6] & 0x00000000000000FFL);
    // }
    // case 8: {
    // return ((array[index] & 0x00000000000000FFL) << 56)
    // | ((array[index + 1] & 0x00000000000000FFL) << 48)
    // | ((array[index + 2] & 0x00000000000000FFL) << 40)
    // | ((array[index + 3] & 0x00000000000000FFL) << 32)
    // | ((array[index + 4] & 0x00000000000000FFL) << 24)
    // | ((array[index + 5] & 0x00000000000000FFL) << 16)
    // | ((array[index + 6] & 0x00000000000000FFL) << 8)
    // | (array[index + 7] & 0x00000000000000FFL);
    // }
    // default: {
    // throw new IllegalArgumentException(
    // "bytesNum should be one of (5, 6, 7, 8).");
    // }
    // }
    // } else {
    // switch (bytesNum) {
    // case 5: {
    // long l = ((array[index] & 0x00000000000000FFL) << 40)
    // | ((array[index + 1] & 0x00000000000000FFL) << 32)
    // | ((array[index + 2] & 0x00000000000000FFL) << 24)
    // | ((array[index + 3] & 0x00000000000000FFL) << 16)
    // | ((array[index + 4] & 0x00000000000000FFL) << 8)
    // | (array[index + 5] & 0x00000000000000FFL);
    // return ((l << offset) & 0x0000FFFFFFFFFF00L) >>> 8;
    // }
    // case 6: {
    // long l = ((array[index] & 0x00000000000000FFL) << 48)
    // | ((array[index + 1] & 0x00000000000000FFL) << 40)
    // | ((array[index + 2] & 0x00000000000000FFL) << 32)
    // | ((array[index + 3] & 0x00000000000000FFL) << 24)
    // | ((array[index + 4] & 0x00000000000000FFL) << 16)
    // | ((array[index + 5] & 0x00000000000000FFL) << 8)
    // | (array[index + 6] & 0x00000000000000FFL);
    // return ((l << offset) & 0x00FFFFFFFFFFFF00L) >>> 8;
    // }
    // case 7: {
    // long l = ((array[index] & 0x00000000000000FFL) << 56)
    // | ((array[index + 1] & 0x00000000000000FFL) << 48)
    // | ((array[index + 2] & 0x00000000000000FFL) << 40)
    // | ((array[index + 3] & 0x00000000000000FFL) << 32)
    // | ((array[index + 4] & 0x00000000000000FFL) << 24)
    // | ((array[index + 5] & 0x00000000000000FFL) << 16)
    // | ((array[index + 6] & 0x00000000000000FFL) << 8)
    // | (array[index + 7] & 0x00000000000000FFL);
    // return ((l << offset) & 0xFFFFFFFFFFFFFF00L) >>> 8;
    // }
    // case 8: {
    // long l1 = ((array[index] & 0x00000000000000FFL) << 56)
    // | ((array[index + 1] & 0x00000000000000FFL) << 48)
    // | ((array[index + 2] & 0x00000000000000FFL) << 40)
    // | ((array[index + 3] & 0x00000000000000FFL) << 32)
    // | ((array[index + 4] & 0x00000000000000FFL) << 24)
    // | ((array[index + 5] & 0x00000000000000FFL) << 16)
    // | ((array[index + 6] & 0x00000000000000FFL) << 8)
    // | (array[index + 7] & 0x00000000000000FFL);
    // l1 <<= offset;
    // long l2 = (array[index + 8] & 0x00000000000000FFL) >>> (8 - offset);
    // return l1 | l2;
    // }
    // default: {
    // throw new IllegalArgumentException(
    // "bytesNum should be one of (5, 6, 7, 8).");
    // }
    // }
    // }
    // }
    //
    // @Override
    // protected void write1to7Bits(long posInBits, int value, int bitsNum) {
    // long absolutePos = absolutePos(posInBits);
    // int index = (int) (absolutePos >>> 32);
    // int offset = (int) absolutePos;
    // if (offset == 0) {
    // int i = array[index] & 0x000000FF;
    // i <<= bitsNum;
    // i >>>= bitsNum;
    // value >>>= 24 + (8 - bitsNum);
    // value <<= (8 - bitsNum);
    // array[index] = (byte) (i | value);
    // } else if (offset + bitsNum <= 8) {
    // int i = array[index] & 0x000000FF;
    // int j = i;
    // j <<= offset;
    // j &= 0x000000FF;
    // j >>>= offset;
    // j >>>= (8 - offset - bitsNum);
    // j <<= (8 - offset - bitsNum);
    // j = ~j;
    // i = i & j;
    // value >>>= 24 + offset;
    // value >>>= (8 - offset - bitsNum);
    // value <<= (8 - offset - bitsNum);
    // array[index] = (byte) (i | value);
    // } else {
    // int i = ((array[index] & 0x000000FF) << 8)
    // | (array[index + 1] & 0x000000FF);
    // int j = i;
    // j <<= offset;
    // j &= 0x0000FFFF;
    // j >>>= offset;
    // j >>>= (16 - offset - bitsNum);
    // j <<= (16 - offset - bitsNum);
    // j = ~j;
    // i = i & j;
    // value >>>= 16 + offset;
    // value >>>= (16 - offset - bitsNum);
    // value <<= (16 - offset - bitsNum);
    // int result = i | value;
    // array[index] = (byte) (result >>> 8);
    // array[index + 1] = (byte) (result);
    // }
    // }
    //
    // @Override
    // protected void writeBytes(long posInBits, int value, int bytesNum) {
    // long absolutePos = absolutePos(posInBits);
    // int index = (int) (absolutePos >>> 32);
    // int offset = (int) absolutePos;
    // if (offset == 0) {
    // switch (bytesNum) {
    // case 1: {
    // array[index] = (byte) (value >>> 24);
    // }
    // case 2: {
    // array[index] = (byte) (value >>> 24);
    // array[index + 1] = (byte) (value >>> 16);
    // }
    // case 3: {
    // array[index] = (byte) (value >>> 24);
    // array[index + 1] = (byte) (value >>> 16);
    // array[index + 2] = (byte) (value >>> 8);
    // }
    // case 4: {
    // array[index] = (byte) (value >>> 24);
    // array[index + 1] = (byte) (value >>> 16);
    // array[index + 2] = (byte) (value >>> 8);
    // array[index + 3] = (byte) (value);
    // }
    // default: {
    // throw new IllegalArgumentException(
    // "bytesNum should be one of (1, 2, 3, 4).");
    // }
    // }
    // } else {
    // switch (bytesNum) {
    // case 1: {
    // int i = ((array[index] & 0x000000FF) << 24)
    // | ((array[index + 1] & 0x000000FF) << 16);
    // i = BitsUtils.bitsCopy(value, 0, i, offset, 8);
    // array[index] = (byte) (i >>> 24);
    // array[index + 1] = (byte) (i >>> 16);
    // }
    // case 2: {
    // int i = ((array[index] & 0x000000FF) << 24)
    // | ((array[index + 1] & 0x000000FF) << 16)
    // | ((array[index + 2] & 0x000000FF) << 8);
    // i = BitsUtils.bitsCopy(value, 0, i, offset, 16);
    // array[index] = (byte) (i >>> 24);
    // array[index + 1] = (byte) (i >>> 16);
    // array[index + 2] = (byte) (i >>> 8);
    // }
    // case 3: {
    // int i = ((array[index] & 0x000000FF) << 24)
    // | ((array[index + 1] & 0x000000FF) << 16)
    // | ((array[index + 2] & 0x000000FF) << 8)
    // | (array[index + 3] & 0x000000FF);
    // i = BitsUtils.bitsCopy(value, 0, i, offset, 24);
    // array[index] = (byte) (i >>> 24);
    // array[index + 1] = (byte) (i >>> 16);
    // array[index + 2] = (byte) (i >>> 8);
    // array[index + 3] = (byte) (i);
    // }
    // case 4: {
    // long l = ((array[index] & 0x00000000000000FFL) << 56)
    // | ((array[index + 1] & 0x00000000000000FFL) << 48)
    // | ((array[index + 2] & 0x00000000000000FFL) << 40)
    // | ((array[index + 3] & 0x00000000000000FFL) << 32)
    // | ((array[index + 4] & 0x00000000000000FFL) << 24);
    // l = BitsUtils.bitsCopy(((long) value) << 32, 0, l, offset,
    // 32);
    // array[index] = (byte) (l >>> 56);
    // array[index + 1] = (byte) (l >>> 48);
    // array[index + 2] = (byte) (l >>> 40);
    // array[index + 3] = (byte) (l >>> 32);
    // array[index + 4] = (byte) (l >>> 24);
    // }
    // default: {
    // throw new IllegalArgumentException(
    // "bytesNum should be one of (1, 2, 3, 4).");
    // }
    // }
    // }
    // }
    //
    // @Override
    // protected void writeLongBytes(long posInBits, long value, int bytesNum) {
    // if (bytesNum >= 4) {
    // writeBytes(posInBits, (int) (value >>> 32), bytesNum);
    // return;
    // }
    // long absolutePos = absolutePos(posInBits);
    // int index = (int) (absolutePos >>> 32);
    // int offset = (int) absolutePos;
    // if (offset == 0) {
    // switch (bytesNum) {
    // case 5: {
    // array[index] = (byte) (value >>> 56);
    // array[index + 1] = (byte) (value >>> 48);
    // array[index + 2] = (byte) (value >>> 40);
    // array[index + 3] = (byte) (value >>> 32);
    // array[index + 4] = (byte) (value >>> 24);
    // }
    // case 6: {
    // array[index] = (byte) (value >>> 56);
    // array[index + 1] = (byte) (value >>> 48);
    // array[index + 2] = (byte) (value >>> 40);
    // array[index + 3] = (byte) (value >>> 32);
    // array[index + 4] = (byte) (value >>> 24);
    // array[index + 5] = (byte) (value >>> 16);
    // }
    // case 7: {
    // array[index] = (byte) (value >>> 56);
    // array[index + 1] = (byte) (value >>> 48);
    // array[index + 2] = (byte) (value >>> 40);
    // array[index + 3] = (byte) (value >>> 32);
    // array[index + 4] = (byte) (value >>> 24);
    // array[index + 5] = (byte) (value >>> 16);
    // array[index + 6] = (byte) (value >>> 8);
    // }
    // case 8: {
    // array[index] = (byte) (value >>> 56);
    // array[index + 1] = (byte) (value >>> 48);
    // array[index + 2] = (byte) (value >>> 40);
    // array[index + 3] = (byte) (value >>> 32);
    // array[index + 4] = (byte) (value >>> 24);
    // array[index + 5] = (byte) (value >>> 16);
    // array[index + 6] = (byte) (value >>> 8);
    // array[index + 7] = (byte) (value);
    // }
    // default: {
    // throw new IllegalArgumentException(
    // "bytesNum should be one of (5, 6, 7, 8).");
    // }
    // }
    // } else {
    // switch (bytesNum) {
    // case 5: {
    // long l = ((array[index] & 0x00000000000000FFL) << 56)
    // | ((array[index + 1] & 0x00000000000000FFL) << 48)
    // | ((array[index + 2] & 0x00000000000000FFL) << 40)
    // | ((array[index + 3] & 0x00000000000000FFL) << 32)
    // | ((array[index + 4] & 0x00000000000000FFL) << 24)
    // | ((array[index + 5] & 0x00000000000000FFL) << 16);
    // l = BitsUtils.bitsCopy(value, 0, l, offset, 40);
    // array[index] = (byte) (l >>> 56);
    // array[index + 1] = (byte) (l >>> 48);
    // array[index + 2] = (byte) (l >>> 40);
    // array[index + 3] = (byte) (l >>> 32);
    // array[index + 4] = (byte) (l >>> 24);
    // array[index + 5] = (byte) (l >>> 16);
    // }
    // case 6: {
    // long l = ((array[index] & 0x00000000000000FFL) << 56)
    // | ((array[index + 1] & 0x00000000000000FFL) << 48)
    // | ((array[index + 2] & 0x00000000000000FFL) << 40)
    // | ((array[index + 3] & 0x00000000000000FFL) << 32)
    // | ((array[index + 4] & 0x00000000000000FFL) << 24)
    // | ((array[index + 5] & 0x00000000000000FFL) << 16)
    // | ((array[index + 6] & 0x00000000000000FFL) << 8);
    // l = BitsUtils.bitsCopy(value, 0, l, offset, 48);
    // array[index] = (byte) (l >>> 56);
    // array[index + 1] = (byte) (l >>> 48);
    // array[index + 2] = (byte) (l >>> 40);
    // array[index + 3] = (byte) (l >>> 32);
    // array[index + 4] = (byte) (l >>> 24);
    // array[index + 5] = (byte) (l >>> 16);
    // array[index + 6] = (byte) (l >>> 8);
    // }
    // case 7: {
    // long l = ((array[index] & 0x00000000000000FFL) << 56)
    // | ((array[index + 1] & 0x00000000000000FFL) << 48)
    // | ((array[index + 2] & 0x00000000000000FFL) << 40)
    // | ((array[index + 3] & 0x00000000000000FFL) << 32)
    // | ((array[index + 4] & 0x00000000000000FFL) << 24)
    // | ((array[index + 5] & 0x00000000000000FFL) << 16)
    // | ((array[index + 6] & 0x00000000000000FFL) << 8)
    // | (array[index + 7] & 0x00000000000000FFL);
    // l = BitsUtils.bitsCopy(value, 0, l, offset, 56);
    // array[index] = (byte) (l >>> 56);
    // array[index + 1] = (byte) (l >>> 48);
    // array[index + 2] = (byte) (l >>> 40);
    // array[index + 3] = (byte) (l >>> 32);
    // array[index + 4] = (byte) (l >>> 24);
    // array[index + 5] = (byte) (l >>> 16);
    // array[index + 6] = (byte) (l >>> 8);
    // array[index + 7] = (byte) (l);
    // }
    // case 8: {
    // long l1 = ((array[index] & 0x00000000000000FFL) << 56)
    // | ((array[index + 1] & 0x00000000000000FFL) << 48)
    // | ((array[index + 2] & 0x00000000000000FFL) << 40)
    // | ((array[index + 3] & 0x00000000000000FFL) << 32)
    // | ((array[index + 4] & 0x00000000000000FFL) << 24)
    // | ((array[index + 5] & 0x00000000000000FFL) << 16)
    // | ((array[index + 6] & 0x00000000000000FFL) << 8)
    // | (array[index + 7] & 0x00000000000000FFL);
    // l1 = BitsUtils.bitsCopy(value, 0, l1, offset, 64 - offset);
    // long l2 = array[index + 8] & 0x00000000000000FFL;
    // l2 = BitsUtils.bitsCopy(value, 64 - offset, l2, 56, offset);
    // array[index] = (byte) (l1 >>> 56);
    // array[index + 1] = (byte) (l1 >>> 48);
    // array[index + 2] = (byte) (l1 >>> 40);
    // array[index + 3] = (byte) (l1 >>> 32);
    // array[index + 4] = (byte) (l1 >>> 24);
    // array[index + 5] = (byte) (l1 >>> 16);
    // array[index + 6] = (byte) (l1 >>> 8);
    // array[index + 7] = (byte) (l1);
    // array[index + 8] = (byte) (l2);
    // }
    // default: {
    // throw new IllegalArgumentException(
    // "bytesNum should be one of (5, 6, 7, 8).");
    // }
    // }
    // }
    // }
}
