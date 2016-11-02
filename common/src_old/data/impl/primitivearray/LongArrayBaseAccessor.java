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
 * This implementation use long array to store and manipulate data randomly,
 * each element takes 64 bits. The effective data start from specified bit
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
 * @version 0.0.0, 2015-05-23 19:47:20
 * @since 0.0.0
 * @see Bits
 * @see Bitss
 */
final class LongArrayBaseAccessor extends Bitss {

    /**
     * Serial ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Array to store data.
     */
    private final long[] array;

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
    LongArrayBaseAccessor(final long[] array, final int startIndex,
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
    LongArrayBaseAccessor(final long[] array, final int startIndex,
            final int startOffset) {
        this(array, startIndex, startOffset, array.length - 1, 63);
    }

    /**
     * <p>
     * Constructs an instance with specified array. All array's data included.
     * </p>
     *
     * @param array
     *            specified array, not null
     */
    LongArrayBaseAccessor(final long[] array) {
        this(array, 0, 0, array.length - 1, 63);
    }

    @Override
    protected int elementWide() {
        return 64;
    }

    @Override
    protected Bits shadowClone(final int startIndex,
            final int startOffset, final int endIndex, final int endOffset) {
        return new LongArrayBaseAccessor(array, startIndex, startOffset,
                endIndex, endOffset);
    }

    @Override
    public Bits deepClone() {
        // Byte alignment.
        // If startOffset is not 0, deep copied accessor's startOffset should be
        // aligned to 0.
        if (startOffset == 0) {
            final long[] clone = new long[endIndex - startIndex + 1];
            System.arraycopy(array, startIndex, clone, 0, clone.length);
            return new LongArrayBaseAccessor(clone, 0, 0, clone.length - 1,
                    endOffset);
        } else {
            final long[] clone = new long[lengthInBits % elementWide == 0 ? (int) (lengthInBits / elementWide)
                    : (int) (lengthInBits / elementWide + 1)];
            final Bits cloneAccessor = new LongArrayBaseAccessor(clone,
                    0, 0, clone.length - 1,
                    (int) ((lengthInBits - 1L) % elementWide));
            this.copyTo(cloneAccessor);
            return cloneAccessor;
        }
    }

    @Override
    public long copyTo(final Bits dest) {
        if (this.lengthInBits >= MEM_COPY_OPTIMIZATION_THRESHOLD_IN_BITS
                && dest.lengthInBits() >= MEM_COPY_OPTIMIZATION_THRESHOLD_IN_BITS
                && dest instanceof LongArrayBaseAccessor) {
            final LongArrayBaseAccessor shorter = (LongArrayBaseAccessor) PrimitiveImplUtil
                    .getShorterAccessor(this, dest);
            final LongArrayBaseAccessor copyDest = (LongArrayBaseAccessor) dest;
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
                    long thisTail = this.array[this.startIndex + len];
                    long destTail = copyDest.array[copyDest.startIndex + len];
                    thisTail >>>= (elementWide - shorter.endOffset);
                    thisTail <<= (elementWide - shorter.endOffset);
                    destTail <<= (shorter.endOffset);
                    destTail >>>= (shorter.endOffset);
                    copyDest.array[copyDest.startIndex + len] = (thisTail | destTail);
                    return shorter.lengthInBits;
                } else if (this.startOffset != 0
                        && shorter.endIndex - shorter.startIndex >= 2) {
                    // Copies except head and tail
                    System.arraycopy(this.array, this.startIndex + 1,
                            copyDest.array, copyDest.startIndex + 1,
                            shorter.endIndex - shorter.startIndex - 1);
                    // Copies head
                    long thisHead = this.array[this.startIndex];
                    long destHead = copyDest.array[copyDest.startIndex];
                    thisHead <<= (this.startOffset);
                    thisHead >>>= (this.startOffset);
                    destHead >>>= (elementWide - this.startOffset);
                    destHead <<= (elementWide - this.startOffset);
                    copyDest.array[copyDest.startIndex] = (thisHead | destHead);
                    // Copies tail
                    final int len = shorter.endIndex - shorter.startIndex;
                    long thisTail = this.array[this.startIndex + len];
                    long destTail = copyDest.array[copyDest.startIndex + len];
                    thisTail >>>= (elementWide - shorter.endOffset);
                    thisTail <<= (elementWide - shorter.endOffset);
                    destTail <<= (shorter.endOffset);
                    destTail >>>= (shorter.endOffset);
                    copyDest.array[copyDest.startIndex + len] = (thisTail | destTail);
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
        throw new BaseException("This method should not be called.");
    }

    @Override
    protected long readArrayElementsAsLong(final int index,
            final int elementsNum) {
        switch (elementsNum) {
            case 1: {
                return array[index];
            }
            default: {
                throw new BaseException("Elements number should be 1.");
            }
        }
    }

    @Override
    protected void writeArrayElements(final int index, final int value,
            final int elementsNum) {
        throw new BaseException("This method should not be called.");
    }

    @Override
    protected void writeArrayElements(final int index, final long value,
            final int elementsNum) {
        switch (elementsNum) {
            case 1: {
                array[index] = value;
                return;
            }
            default: {
                throw new BaseException("Elements number should be 1.");
            }
        }
    }
    // /**
    // * Serial ID.
    // */
    // private static final long serialVersionUID = 1L;
    //
    // /**
    // * Array to store data, data managed by accessor maybe be stored in whole
    // array, maybe part of
    // * array which from start index with its bit offset to end index with its
    // bit offset.
    // */
    // private final long[] array;
    //
    // /**
    // * Constructs an instance with special array, start index, bit offset of
    // start index, end index
    // * and bit offset of end index. Data managed by accessor start from given
    // start index and its
    // * bit offset to given end index and its bit offset.
    // *
    // * @param array
    // * special array, not null
    // * @param startIndex
    // * start index, [0, array length - 1]
    // * @param startOffset
    // * bit offset of start index, [0, element wide - 1]
    // * @param endIndex
    // * end index, [0, array length - 1]
    // * @param endOffset
    // * bit offset of end index, [0, element wide - 1]
    // */
    // LongArrayBaseAccessor(long[] array, int startIndex, int startOffset, int
    // endIndex, int endOffset)
    // {
    // this.array = array;
    // this.mark = new LongArrayMark(startIndex, startOffset, endIndex,
    // endOffset);
    // this.base = getBaseReadWrite();
    // }
    //
    // /**
    // * Constructs an instance with special array, start index, bit offset of
    // start index, end index
    // * and bit offset of end index. Data managed by accessor in whole of
    // array.
    // *
    // * @param array
    // * special array, not null
    // */
    // LongArrayBaseAccessor(long[] array)
    // {
    // this(array, 0, 0, array.length - 1, Long.SIZE - 1);
    // }
    //
    // /**
    // * Constructs an instance with special array, start index, bit offset of
    // start index, end index
    // * and bit offset of end index. Data managed by accessor start from given
    // start index and its
    // * bit offset to end of array.
    // *
    // * @param array
    // * special array, not null
    // * @param startIndex
    // * start index, [0, array length - 1]
    // * @param startOffset
    // * bit offset of start index, [0, element wide - 1]
    // */
    // LongArrayBaseAccessor(long[] array, int startIndex, int startOffset)
    // {
    // this(array, startIndex, startOffset, array.length - 1, Long.SIZE - 1);
    // }
    //
    // @Override
    // protected BaseReadWrite getBaseReadWrite()
    // {
    // if (mark.startOffset == 0)
    // {
    // return new AlignedLongArrayBaseReadWrite(this.array);
    // }
    // else
    // {
    // return new LongArrayBaseReadWrite(this.array);
    // }
    // }
    //
    // @Override
    // protected BaseAccessor shadowClone(int startIndex, int startOffset, int
    // endIndex, int endOffset)
    // {
    // return new LongArrayBaseAccessor(array, startIndex, startOffset,
    // endIndex, endOffset);
    // }
    //
    // @Override
    // public BaseAccessor deepClone()
    // {
    // long[] copy = new long[mark.endIndex - mark.startIndex + 1];
    // System.arraycopy(array, mark.startIndex, copy, 0, copy.length);
    // return new LongArrayBaseAccessor(copy, 0, mark.startOffset, copy.length -
    // 1, mark.endOffset);
    // }
    //
    // @Override
    // public void setBits(boolean value)
    // {
    // if (mark.startIndex == mark.endIndex)
    // {
    // long l = PrimitiveImplUtils.fillLongBits(array[mark.startIndex],
    // mark.startOffset,
    // mark.endOffset - mark.startOffset + 1, value);
    // array[mark.startIndex] = (l);
    // }
    // else
    // {
    // if (mark.endIndex - mark.startIndex > 1)
    // {
    // if (value)
    // {
    // for (int i = mark.startIndex + 1; i < mark.endIndex; i++)
    // {
    // array[i] = 0xffffffffffffffffL;
    // }
    // }
    // else
    // {
    // for (int i = mark.startIndex + 1; i < mark.endIndex; i++)
    // {
    // array[i] = 0L;
    // }
    // }
    // }
    // long l = PrimitiveImplUtils.fillLongBits(array[mark.startIndex],
    // mark.startOffset,
    // mark.elementWide() - mark.startOffset, value);
    // array[mark.startIndex] = (l);
    // l = PrimitiveImplUtils.fillLongBits(array[mark.endIndex], 0,
    // mark.endOffset + 1, value);
    // array[mark.endIndex] = (l);
    // }
    // }
    //
    // /**
    // * Returns shorter array base accessor implementation between given two
    // implementations.
    // *
    // * @param a1
    // * an implementation
    // * @param a2
    // * another implementation
    // * @return shorter one
    // */
    // private LongArrayBaseAccessor getShorterAccessor(LongArrayBaseAccessor
    // a1,
    // LongArrayBaseAccessor a2)
    // {
    // return a1.lengthInBits() <= a2.lengthInBits() ? a1 : a2;
    // }
    //
    // /**
    // * Primitively copies data from this to dest, copied length is shorter
    // length of two accessor.
    // *
    // * @param dest
    // * dest accessor
    // * @return actual copied length
    // */
    // private long primitiveCopy(BaseAccessor dest)
    // {
    // LongArrayBaseAccessor impl = (LongArrayBaseAccessor)dest;
    // LongArrayBaseAccessor shorter = getShorterAccessor(this, impl);
    // if (this.mark.startOffset == impl.mark.startOffset)
    // {
    // if (shorter.mark.startIndex == shorter.mark.endIndex)
    // {
    // long l = PrimitiveImplUtils.putLongBits(this.array[this.mark.startIndex],
    // impl.array[impl.mark.startIndex], this.mark.startOffset,
    // (int)shorter.lengthInBits());
    // impl.array[impl.mark.startIndex] = (l);
    // }
    // else
    // {
    // if (shorter.mark.endIndex - shorter.mark.startIndex > 1)
    // {
    // System.arraycopy(this.array, this.mark.startIndex + 1, impl.array,
    // impl.mark.startIndex + 1, shorter.mark.endIndex
    // - shorter.mark.startIndex - 1);
    // }
    // long l = PrimitiveImplUtils.putLongBits(this.array[this.mark.startIndex],
    // impl.array[impl.mark.startIndex], this.mark.startOffset,
    // mark.elementWide()
    // - this.mark.startOffset);
    // impl.array[impl.mark.startIndex] = (l);
    // l = PrimitiveImplUtils.putLongBits(this.array[this.mark.startIndex
    // + (shorter.mark.endIndex - shorter.mark.startIndex)],
    // impl.array[impl.mark.startIndex
    // + (shorter.mark.endIndex - shorter.mark.startIndex)], 0,
    // shorter.mark.endOffset + 1);
    // impl.array[impl.mark.startIndex + (shorter.mark.endIndex -
    // shorter.mark.startIndex)] = (l);
    // }
    // return shorter.lengthInBits();
    // }
    // else
    // {
    // return super.copyTo(dest);
    // }
    // }
    //
    // @Override
    // public long copyTo(BaseAccessor dest)
    // {
    // if (PrimitiveImplUtils.isSameClass(this, dest))
    // {
    // return primitiveCopy(dest);
    // }
    // return super.copyTo(dest);
    // }
    //
    // @Override
    // public void not()
    // {
    // if (mark.startIndex == mark.endIndex)
    // {
    // long l = array[mark.startIndex];
    // l = PrimitiveImplUtils.putLongBits(~l, l, mark.startOffset,
    // (int)lengthInBits());
    // array[mark.startIndex] = (l);
    // }
    // else
    // {
    // if (mark.endIndex - mark.startIndex > 1)
    // {
    // for (int i = mark.startIndex + 1; i <= mark.endIndex - 1; i++)
    // {
    // array[i] = ~array[i];
    // }
    // }
    // long l = array[mark.startIndex];
    // l = PrimitiveImplUtils.putLongBits(~l, l, mark.startOffset,
    // mark.elementWide()
    // - mark.startOffset);
    // array[mark.startIndex] = (l);
    // l = array[mark.endIndex];
    // l = PrimitiveImplUtils.putLongBits(~l, l, 0, mark.endOffset + 1);
    // array[mark.endIndex] = (l);
    // }
    // }
    //
    // @Override
    // public Object getSourceData()
    // {
    // return array;
    // }
    //
    // /**
    // * For serializing. If the data are only part of array, this method ensure
    // store part of array
    // * rather than whole of array.
    // *
    // * @return clone with part of array
    // * @throws ObjectStreamException
    // */
    // private Object writeReplace() throws ObjectStreamException
    // {
    // if (!(mark.startIndex == 0 && mark.endIndex == array.length - 1))
    // {
    // return deepClone();
    // }
    // else
    // {
    // return this;
    // }
    // }
    //
    // /**
    // * For deserializing, create a new base read write.
    // *
    // * @return this instance
    // * @throws ObjectStreamException
    // */
    // private Object readResolve() throws ObjectStreamException
    // {
    // this.base = getBaseReadWrite();
    // return this;
    // }
    //
    // /**
    // * This class represents an aligned implementation of {@linkplain
    // BaseReadWrite} of long array.
    // * Its bit offset of start byte array should align to 0.
    // *
    // * @author Fred Suvn
    // * @version 0.0.0, 2015-06-01 09:26:22
    // */
    // private static final class AlignedLongArrayBaseReadWrite implements
    // BaseReadWrite
    // {
    // /**
    // * Array of data
    // */
    // private final long[] array;
    //
    // /**
    // * Construction.
    // *
    // * @param array
    // * array of data
    // */
    // private AlignedLongArrayBaseReadWrite(long[] array)
    // {
    // this.array = array;
    // }
    //
    // @Override
    // public int readInt(ArrayPos arrayPosInfo)
    // {
    // return (int)(array[arrayPosInfo.index] >>> 32);
    // }
    //
    // @Override
    // public long readLong(ArrayPos arrayPosInfo)
    // {
    // return array[arrayPosInfo.index];
    // }
    //
    // @Override
    // public int readBitsAsInt(ArrayPos arrayPosInfo, int bits)
    // {
    // if (bits == 32)
    // {
    // return readInt(arrayPosInfo);
    // }
    // else
    // {
    // return PrimitiveImplUtils.partOfInt(readInt(arrayPosInfo), 0, bits);
    // }
    // }
    //
    // @Override
    // public long readBitsAsLong(ArrayPos arrayPosInfo, int bits)
    // {
    // if (bits <= 32)
    // {
    // return readBitsAsInt(arrayPosInfo, bits);
    // }
    // else if (bits == 64)
    // {
    // return readLong(arrayPosInfo);
    // }
    // else
    // {
    // return PrimitiveImplUtils.partOfLong(readLong(arrayPosInfo), 0, bits);
    // }
    // }
    //
    // @Override
    // public void writeInt(ArrayPos arrayPosInfo, int value)
    // {
    // array[arrayPosInfo.index] = PrimitiveImplUtils.putLongBits(((long)value)
    // << 32,
    // array[arrayPosInfo.index], 0, 32);
    // }
    //
    // @Override
    // public void writeLong(ArrayPos arrayPosInfo, long value)
    // {
    // array[arrayPosInfo.index] = value;
    // }
    //
    // @Override
    // public void writeBitsInOrder(ArrayPos arrayPosInfo, int value, int bits)
    // {
    // if (bits == 32)
    // {
    // writeInt(arrayPosInfo, value);
    // }
    // else
    // {
    // int i = readInt(arrayPosInfo);
    // i = PrimitiveImplUtils.putIntBits(value, i, 0, bits);
    // writeInt(arrayPosInfo, i);
    // }
    // }
    //
    // @Override
    // public void writeBitsInOrder(ArrayPos arrayPosInfo, long value, int bits)
    // {
    // if (bits <= 32)
    // {
    // writeBitsInOrder(arrayPosInfo, (int)(value >>> 32), bits);
    // }
    // else if (bits == 64)
    // {
    // writeLong(arrayPosInfo, value);
    // }
    // else
    // {
    // long l = readLong(arrayPosInfo);
    // l = PrimitiveImplUtils.putLongBits(value, l, 0, bits);
    // writeLong(arrayPosInfo, l);
    // }
    // }
    //
    // }
    //
    // /**
    // * This class represents implementation of {@linkplain BaseReadWrite} of
    // long array. Its bit
    // * offset of start byte array shouldn't align to 0.
    // *
    // * @author Fred Suvn
    // * @version 0.0.0, 2015-06-01 09:25:13
    // */
    // private static final class LongArrayBaseReadWrite implements
    // BaseReadWrite
    // {
    // /**
    // * Array of data
    // */
    // private final long[] array;
    //
    // /**
    // * Base read write, to read and write align to multiple of byte element.
    // */
    // private final AlignedLongArrayBaseReadWrite base;
    //
    // /**
    // * Construction.
    // *
    // * @param array
    // * array of data
    // */
    // private LongArrayBaseReadWrite(long[] array)
    // {
    // this.array = array;
    // this.base = new AlignedLongArrayBaseReadWrite(this.array);
    // }
    //
    // @Override
    // public int readInt(ArrayPos arrayPosInfo)
    // {
    // if (arrayPosInfo.offset <= 32)
    // {
    // return (int)(array[arrayPosInfo.index] >>> (32 - arrayPosInfo.offset));
    // }
    // else
    // {
    // long l = readLong(arrayPosInfo);
    // return (int)(l >>> 32);
    // }
    // }
    //
    // @Override
    // public long readLong(ArrayPos arrayPosInfo)
    // {
    // long l1 = array[arrayPosInfo.index];
    // long l2 = array[arrayPosInfo.index + 1];
    // l1 <<= arrayPosInfo.offset;
    // l2 >>>= Long.SIZE - arrayPosInfo.offset;
    // return l1 | l2;
    // }
    //
    // @Override
    // public int readBitsAsInt(ArrayPos arrayPosInfo, int bits)
    // {
    // if (bits == 32)
    // {
    // return readInt(arrayPosInfo);
    // }
    // else
    // {
    // if (arrayPosInfo.offset + bits <= 64)
    // {
    // return (int)PrimitiveImplUtils.partOfLong(base.readLong(arrayPosInfo),
    // arrayPosInfo.offset, bits);
    // }
    // else
    // {
    // int i = readInt(arrayPosInfo);
    // return PrimitiveImplUtils.partOfInt(i, 0, bits);
    // }
    // }
    // }
    //
    // @Override
    // public long readBitsAsLong(ArrayPos arrayPosInfo, int bits)
    // {
    // if (bits <= 32)
    // {
    // return readBitsAsInt(arrayPosInfo, bits);
    // }
    // else if (bits == 64)
    // {
    // return readLong(arrayPosInfo);
    // }
    // else
    // {
    // if (arrayPosInfo.offset + bits <= 64)
    // {
    // return PrimitiveImplUtils.partOfLong(base.readLong(arrayPosInfo),
    // arrayPosInfo.offset, bits);
    // }
    // else
    // {
    // long l = readLong(arrayPosInfo);
    // return PrimitiveImplUtils.partOfLong(l, 0, bits);
    // }
    // }
    // }
    //
    // @Override
    // public void writeInt(ArrayPos arrayPosInfo, int value)
    // {
    // long v = ((long)value) << 32;
    // if (arrayPosInfo.offset <= 32)
    // {
    // long l = array[arrayPosInfo.index];
    // l = PrimitiveImplUtils.putLongBits(v >>> arrayPosInfo.offset, l,
    // arrayPosInfo.offset, 32);
    // array[arrayPosInfo.index] = l;
    // }
    // else
    // {
    // long l1 = array[arrayPosInfo.index];
    // long l2 = array[arrayPosInfo.index + 1];
    // l1 = PrimitiveImplUtils.putLongBits(v >>> arrayPosInfo.offset, l1,
    // arrayPosInfo.offset, 64 - arrayPosInfo.offset);
    // l2 = PrimitiveImplUtils.putLongBits(v << (Long.SIZE -
    // arrayPosInfo.offset), l2, 0,
    // arrayPosInfo.offset - 32);
    // array[arrayPosInfo.index] = l1;
    // array[arrayPosInfo.index + 1] = l2;
    // }
    // }
    //
    // @Override
    // public void writeLong(ArrayPos arrayPosInfo, long value)
    // {
    // long l1 = array[arrayPosInfo.index];
    // long l2 = array[arrayPosInfo.index + 1];
    // l1 = PrimitiveImplUtils.putLongBits(value >>> arrayPosInfo.offset, l1,
    // arrayPosInfo.offset, 64 - arrayPosInfo.offset);
    // l2 = PrimitiveImplUtils.putLongBits(value << (Long.SIZE -
    // arrayPosInfo.offset), l2, 0,
    // arrayPosInfo.offset);
    // array[arrayPosInfo.index] = l1;
    // array[arrayPosInfo.index + 1] = l2;
    // }
    //
    // @Override
    // public void writeBitsInOrder(ArrayPos arrayPosInfo, int value, int bits)
    // {
    // if (bits == 32)
    // {
    // writeInt(arrayPosInfo, value);
    // }
    // else
    // {
    // if (arrayPosInfo.offset + bits <= 64)
    // {
    // long l = array[arrayPosInfo.index];
    // l = PrimitiveImplUtils.putLongBits(value >>> arrayPosInfo.offset, l,
    // arrayPosInfo.offset, bits);
    // array[arrayPosInfo.index] = l;
    // }
    // else
    // {
    // long l1 = array[arrayPosInfo.index];
    // long l2 = array[arrayPosInfo.index + 1];
    // int leftBits = 32 - arrayPosInfo.offset;
    // int rightBits = bits - leftBits;
    // l1 = PrimitiveImplUtils.putLongBits(value >>> arrayPosInfo.offset, l1,
    // arrayPosInfo.offset, leftBits);
    // l2 = PrimitiveImplUtils.putLongBits(value << (Long.SIZE - rightBits), l2,
    // 0,
    // rightBits);
    // array[arrayPosInfo.index] = l1;
    // array[arrayPosInfo.index + 1] = l2;
    // }
    // }
    // }
    //
    // @Override
    // public void writeBitsInOrder(ArrayPos arrayPosInfo, long value, int bits)
    // {
    // if (bits <= 32)
    // {
    // writeBitsInOrder(arrayPosInfo, (int)(value >>> 32), bits);
    // }
    // else if (bits == 64)
    // {
    // writeLong(arrayPosInfo, value);
    // }
    // else
    // {
    // if (arrayPosInfo.offset + bits <= 64)
    // {
    // long l = array[arrayPosInfo.index];
    // l = PrimitiveImplUtils.putLongBits(value >>> arrayPosInfo.offset, l,
    // arrayPosInfo.offset, bits);
    // array[arrayPosInfo.index] = l;
    // }
    // else
    // {
    // long l1 = array[arrayPosInfo.index];
    // long l2 = array[arrayPosInfo.index + 1];
    // int leftBits = 64 - arrayPosInfo.offset;
    // int rightBits = bits - leftBits;
    // l1 = PrimitiveImplUtils.putLongBits(value >>> arrayPosInfo.offset, l1,
    // arrayPosInfo.offset, leftBits);
    // l2 = PrimitiveImplUtils.putLongBits(value << (Long.SIZE - rightBits), l2,
    // 0,
    // rightBits);
    // array[arrayPosInfo.index] = l1;
    // array[arrayPosInfo.index + 1] = l2;
    // }
    // }
    // }
    // }
}
