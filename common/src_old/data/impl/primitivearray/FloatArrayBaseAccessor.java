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
 * This implementation use float array to store and manipulate data randomly,
 * each element takes 32 bits. The effective data start from specified bit
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
 * @version 0.0.0, 2015-05-23 19:46:55
 * @since 0.0.0
 * @see Bits
 * @see Bitss
 */
final class FloatArrayBaseAccessor extends Bitss {

    /**
     * Serial ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Array to store data.
     */
    private final float[] array;

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
    FloatArrayBaseAccessor(final float[] array, final int startIndex,
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
    FloatArrayBaseAccessor(final float[] array, final int startIndex,
            final int startOffset) {
        this(array, startIndex, startOffset, array.length - 1, 31);
    }

    /**
     * <p>
     * Constructs an instance with specified array. All array's data included.
     * </p>
     *
     * @param array
     *            specified array, not null
     */
    FloatArrayBaseAccessor(final float[] array) {
        this(array, 0, 0, array.length - 1, 31);
    }

    @Override
    protected int elementWide() {
        return 32;
    }

    @Override
    protected Bits shadowClone(final int startIndex,
            final int startOffset, final int endIndex, final int endOffset) {
        return new FloatArrayBaseAccessor(array, startIndex, startOffset,
                endIndex, endOffset);
    }

    @Override
    public Bits deepClone() {
        // Byte alignment.
        // If startOffset is not 0, deep copied accessor's startOffset should be
        // aligned to 0.
        if (startOffset == 0) {
            final float[] clone = new float[endIndex - startIndex + 1];
            System.arraycopy(array, startIndex, clone, 0, clone.length);
            return new FloatArrayBaseAccessor(clone, 0, 0, clone.length - 1,
                    endOffset);
        } else {
            final float[] clone = new float[lengthInBits % elementWide == 0 ? (int) (lengthInBits / elementWide)
                    : (int) (lengthInBits / elementWide + 1)];
            final Bits cloneAccessor = new FloatArrayBaseAccessor(
                    clone, 0, 0, clone.length - 1,
                    (int) ((lengthInBits - 1L) % elementWide));
            this.copyTo(cloneAccessor);
            return cloneAccessor;
        }
    }

    /**
     * Converts float to int.
     *
     * @param f
     *            value to be converted
     * @return int value after converting
     * @since 0.0.0
     */
    private int toInt(final float f) {
        return Float.floatToRawIntBits(f);
    }

    /**
     * Converts int to float.
     *
     * @param i
     *            value to be converted
     * @return float value after converting
     * @since 0.0.0
     */
    private float toFloat(final int i) {
        return Float.intBitsToFloat(i);
    }

    @Override
    public long copyTo(final Bits dest) {
        if (this.lengthInBits >= MEM_COPY_OPTIMIZATION_THRESHOLD_IN_BITS
                && dest.lengthInBits() >= MEM_COPY_OPTIMIZATION_THRESHOLD_IN_BITS
                && dest instanceof FloatArrayBaseAccessor) {
            final FloatArrayBaseAccessor shorter = (FloatArrayBaseAccessor) PrimitiveImplUtil
                    .getShorterAccessor(this, dest);
            final FloatArrayBaseAccessor copyDest = (FloatArrayBaseAccessor) dest;
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
                    int thisTail = toInt(this.array[this.startIndex + len]);
                    int destTail = toInt(copyDest.array[copyDest.startIndex
                            + len]);
                    thisTail >>>= (elementWide - shorter.endOffset);
                    thisTail <<= (elementWide - shorter.endOffset);
                    destTail <<= (shorter.endOffset);
                    destTail >>>= (shorter.endOffset);
                    copyDest.array[copyDest.startIndex + len] = toFloat(thisTail
                            | destTail);
                    return shorter.lengthInBits;
                } else if (this.startOffset != 0
                        && shorter.endIndex - shorter.startIndex >= 2) {
                    // Copies except head and tail
                    System.arraycopy(this.array, this.startIndex + 1,
                            copyDest.array, copyDest.startIndex + 1,
                            shorter.endIndex - shorter.startIndex - 1);
                    // Copies head
                    int thisHead = toInt(this.array[this.startIndex]);
                    int destHead = toInt(copyDest.array[copyDest.startIndex]);
                    thisHead <<= (this.startOffset);
                    thisHead >>>= (this.startOffset);
                    destHead >>>= (elementWide - this.startOffset);
                    destHead <<= (elementWide - this.startOffset);
                    copyDest.array[copyDest.startIndex] = toFloat(thisHead
                            | destHead);
                    // Copies tail
                    final int len = shorter.endIndex - shorter.startIndex;
                    int thisTail = toInt(this.array[this.startIndex + len]);
                    int destTail = toInt(copyDest.array[copyDest.startIndex
                            + len]);
                    thisTail >>>= (elementWide - shorter.endOffset);
                    thisTail <<= (elementWide - shorter.endOffset);
                    destTail <<= (shorter.endOffset);
                    destTail >>>= (shorter.endOffset);
                    copyDest.array[copyDest.startIndex + len] = toFloat(thisTail
                            | destTail);
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
                return toInt(array[index]);
            }
            default: {
                throw new BaseException("Elements number should be 1.");
            }
        }
    }

    @Override
    protected long readArrayElementsAsLong(final int index,
            final int elementsNum) {
        switch (elementsNum) {
            case 1: {
                final long l = toInt(array[index]);
                return l << 32;
            }
            case 2: {
                return ((toInt(array[index]) & 0x00000000FFFFFFFFL) << 32)
                        | ((toInt(array[index + 1]) & 0x00000000FFFFFFFFL) << 0);
            }
            default: {
                throw new BaseException("Elements number should be 1, 2.");
            }
        }
    }

    @Override
    protected void writeArrayElements(final int index, final int value,
            final int elementsNum) {
        switch (elementsNum) {
            case 1: {
                array[index] = toFloat(value);
                return;
            }
            default: {
                throw new BaseException("Elements number should be 1.");
            }
        }
    }

    @Override
    protected void writeArrayElements(final int index, final long value,
            final int elementsNum) {
        switch (elementsNum) {
            case 1: {
                array[index] = toFloat((int) (value >>> 32));
                return;
            }
            case 2: {
                array[index] = toFloat((int) (value >>> 32));
                array[index + 1] = toFloat((int) (value >>> 0));
                return;
            }
            default: {
                throw new BaseException("Elements number should be 1, 2.");
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
    // private final float[] array;
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
    // FloatArrayBaseAccessor(float[] array, int startIndex, int startOffset,
    // int endIndex,
    // int endOffset)
    // {
    // this.array = array;
    // this.mark = new FloatArrayMark(startIndex, startOffset, endIndex,
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
    // FloatArrayBaseAccessor(float[] array)
    // {
    // this(array, 0, 0, array.length - 1, Float.SIZE - 1);
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
    // FloatArrayBaseAccessor(float[] array, int startIndex, int startOffset)
    // {
    // this(array, startIndex, startOffset, array.length - 1, Float.SIZE - 1);
    // }
    //
    // @Override
    // protected BaseReadWrite getBaseReadWrite()
    // {
    // if (mark.startOffset == 0)
    // {
    // return new AlignedFloatArrayBaseReadWrite(this.array);
    // }
    // else
    // {
    // return new FloatArrayBaseReadWrite(this.array);
    // }
    // }
    //
    // @Override
    // protected BaseAccessor shadowClone(int startIndex, int startOffset, int
    // endIndex, int endOffset)
    // {
    // return new FloatArrayBaseAccessor(array, startIndex, startOffset,
    // endIndex, endOffset);
    // }
    //
    // @Override
    // public BaseAccessor deepClone()
    // {
    // float[] copy = new float[mark.endIndex - mark.startIndex + 1];
    // System.arraycopy(array, mark.startIndex, copy, 0, copy.length);
    // return new FloatArrayBaseAccessor(copy, 0, mark.startOffset, copy.length
    // - 1,
    // mark.endOffset);
    // }
    //
    // @Override
    // public void setBits(boolean value)
    // {
    // if (mark.startIndex == mark.endIndex)
    // {
    // int i =
    // PrimitiveImplUtils.fillIntBits(Float.floatToRawIntBits(array[mark.startIndex]),
    // mark.startOffset, mark.endOffset - mark.startOffset + 1, value);
    // array[mark.startIndex] = Float.intBitsToFloat(i);
    // }
    // else
    // {
    // if (mark.endIndex - mark.startIndex > 1)
    // {
    // if (value)
    // {
    // for (int i = mark.startIndex + 1; i < mark.endIndex; i++)
    // {
    // array[i] = Float.intBitsToFloat(0xffffffff);
    // }
    // }
    // else
    // {
    // for (int i = mark.startIndex + 1; i < mark.endIndex; i++)
    // {
    // array[i] = Float.intBitsToFloat(0);
    // }
    // }
    // }
    // int i =
    // PrimitiveImplUtils.fillIntBits(Float.floatToRawIntBits(array[mark.startIndex]),
    // mark.startOffset, mark.elementWide() - mark.startOffset, value);
    // array[mark.startIndex] = Float.intBitsToFloat(i);
    // i =
    // PrimitiveImplUtils.fillIntBits(Float.floatToRawIntBits(array[mark.endIndex]),
    // 0,
    // mark.endOffset + 1, value);
    // array[mark.endIndex] = Float.intBitsToFloat(i);
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
    // private FloatArrayBaseAccessor getShorterAccessor(FloatArrayBaseAccessor
    // a1,
    // FloatArrayBaseAccessor a2)
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
    // FloatArrayBaseAccessor impl = (FloatArrayBaseAccessor)dest;
    // FloatArrayBaseAccessor shorter = getShorterAccessor(this, impl);
    // if (this.mark.startOffset == impl.mark.startOffset)
    // {
    // if (shorter.mark.startIndex == shorter.mark.endIndex)
    // {
    // int i = PrimitiveImplUtils.putIntBits(
    // Float.floatToRawIntBits(this.array[this.mark.startIndex]),
    // Float.floatToRawIntBits(impl.array[impl.mark.startIndex]),
    // this.mark.startOffset, (int)shorter.lengthInBits());
    // impl.array[impl.mark.startIndex] = Float.intBitsToFloat(i);
    // }
    // else
    // {
    // if (shorter.mark.endIndex - shorter.mark.startIndex > 1)
    // {
    // System.arraycopy(this.array, this.mark.startIndex + 1, impl.array,
    // impl.mark.startIndex + 1, shorter.mark.endIndex
    // - shorter.mark.startIndex - 1);
    // }
    // int i = PrimitiveImplUtils.putIntBits(
    // Float.floatToRawIntBits(this.array[this.mark.startIndex]),
    // Float.floatToRawIntBits(impl.array[impl.mark.startIndex]),
    // this.mark.startOffset, mark.elementWide() - this.mark.startOffset);
    // impl.array[impl.mark.startIndex] = Float.intBitsToFloat(i);
    // i = PrimitiveImplUtils.putIntBits(
    // Float.floatToRawIntBits(this.array[this.mark.startIndex
    // + (shorter.mark.endIndex - shorter.mark.startIndex)]),
    // Float.floatToRawIntBits(impl.array[impl.mark.startIndex
    // + (shorter.mark.endIndex - shorter.mark.startIndex)]), 0,
    // shorter.mark.endOffset + 1);
    // impl.array[impl.mark.startIndex + (shorter.mark.endIndex -
    // shorter.mark.startIndex)] = Float
    // .intBitsToFloat(i);
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
    // int i = Float.floatToRawIntBits(array[mark.startIndex]);
    // i = PrimitiveImplUtils.putIntBits(~i, i, mark.startOffset,
    // (int)lengthInBits());
    // array[mark.startIndex] = Float.intBitsToFloat(i);
    // }
    // else
    // {
    // if (mark.endIndex - mark.startIndex > 1)
    // {
    // for (int i = mark.startIndex + 1; i <= mark.endIndex - 1; i++)
    // {
    // array[i] = Float.intBitsToFloat(~Float.floatToRawIntBits(array[i]));
    // }
    // }
    // int i = Float.floatToRawIntBits(array[mark.startIndex]);
    // i = PrimitiveImplUtils.putIntBits(~i, i, mark.startOffset,
    // mark.elementWide()
    // - mark.startOffset);
    // array[mark.startIndex] = Float.intBitsToFloat(i);
    // i = Float.floatToRawIntBits(array[mark.endIndex]);
    // i = PrimitiveImplUtils.putIntBits(~i, i, 0, mark.endOffset + 1);
    // array[mark.endIndex] = Float.intBitsToFloat(i);
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
    // BaseReadWrite} of float array.
    // * Its bit offset of start byte array should align to 0.
    // *
    // * @author Fred Suvn
    // * @version 0.0.0, 2015-06-01 09:26:22
    // */
    // private static final class AlignedFloatArrayBaseReadWrite implements
    // BaseReadWrite
    // {
    // /**
    // * Array of data
    // */
    // private final float[] array;
    //
    // /**
    // * Construction.
    // *
    // * @param array
    // * array of data
    // */
    // private AlignedFloatArrayBaseReadWrite(float[] array)
    // {
    // this.array = array;
    // }
    //
    // @Override
    // public int readInt(ArrayPos arrayPosInfo)
    // {
    // return Float.floatToRawIntBits(array[arrayPosInfo.index]);
    // }
    //
    // @Override
    // public long readLong(ArrayPos arrayPosInfo)
    // {
    // long l = ((long)Float.floatToRawIntBits(array[arrayPosInfo.index])) <<
    // 32;
    // l |= Float.floatToRawIntBits(array[arrayPosInfo.index + 1]) &
    // 0x00000000ffffffffL;
    // return l;
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
    // array[arrayPosInfo.index] = Float.intBitsToFloat(value);
    // }
    //
    // @Override
    // public void writeLong(ArrayPos arrayPosInfo, long value)
    // {
    // array[arrayPosInfo.index] = Float.intBitsToFloat((int)(value >>> 32));
    // array[arrayPosInfo.index + 1] = Float.intBitsToFloat((int)value);
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
    // float array. Its bit
    // * offset of start byte array shouldn't align to 0.
    // *
    // * @author Fred Suvn
    // * @version 0.0.0, 2015-06-01 09:25:13
    // */
    // private static final class FloatArrayBaseReadWrite implements
    // BaseReadWrite
    // {
    // /**
    // * Array of data
    // */
    // private final float[] array;
    //
    // /**
    // * Base read write, to read and write align to multiple of byte element.
    // */
    // private final AlignedFloatArrayBaseReadWrite base;
    //
    // /**
    // * Construction.
    // *
    // * @param array
    // * array of data
    // */
    // private FloatArrayBaseReadWrite(float[] array)
    // {
    // this.array = array;
    // this.base = new AlignedFloatArrayBaseReadWrite(this.array);
    // }
    //
    // @Override
    // public int readInt(ArrayPos arrayPosInfo)
    // {
    // int i1 = base.readInt(arrayPosInfo);
    // int i2 = Float.floatToRawIntBits(array[arrayPosInfo.index + 1]);
    // i1 <<= arrayPosInfo.offset;
    // i2 >>>= Float.SIZE - arrayPosInfo.offset;
    // return i1 | i2;
    // }
    //
    // @Override
    // public long readLong(ArrayPos arrayPosInfo)
    // {
    // long l1 = base.readLong(arrayPosInfo);
    // long l2 = Float.floatToRawIntBits(array[arrayPosInfo.index + 2]);
    // l1 <<= arrayPosInfo.offset;
    // l2 >>>= Float.SIZE - arrayPosInfo.offset;
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
    // if (arrayPosInfo.offset + bits <= 32)
    // {
    // int i = base.readInt(arrayPosInfo);
    // return PrimitiveImplUtils.partOfInt(i, arrayPosInfo.offset, bits);
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
    // long l = base.readLong(arrayPosInfo);
    // return PrimitiveImplUtils.partOfLong(l, arrayPosInfo.offset, bits);
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
    // int i1 = base.readInt(arrayPosInfo);
    // int i2 = Float.floatToRawIntBits(array[arrayPosInfo.index + 1]);
    // i1 = PrimitiveImplUtils.putIntBits(value >>> arrayPosInfo.offset, i1,
    // arrayPosInfo.offset, 32 - arrayPosInfo.offset);
    // i2 = PrimitiveImplUtils.putIntBits(value << (Float.SIZE -
    // arrayPosInfo.offset), i2, 0,
    // arrayPosInfo.offset);
    // base.writeInt(arrayPosInfo, i1);
    // array[arrayPosInfo.index + 1] = Float.intBitsToFloat(i2);
    // }
    //
    // @Override
    // public void writeLong(ArrayPos arrayPosInfo, long value)
    // {
    // long l1 = base.readLong(arrayPosInfo);
    // long l2 = Float.floatToRawIntBits(array[arrayPosInfo.index + 2]);
    // l1 = PrimitiveImplUtils.putLongBits(value >>> arrayPosInfo.offset, l1,
    // arrayPosInfo.offset, 64 - arrayPosInfo.offset);
    // l2 = PrimitiveImplUtils.putLongBits(value << (Float.SIZE -
    // arrayPosInfo.offset), l2, 0,
    // 32 + arrayPosInfo.offset);
    // base.writeLong(arrayPosInfo, l1);
    // array[arrayPosInfo.index + 2] = Float.intBitsToFloat((int)l2);
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
    // if (arrayPosInfo.offset + bits <= 32)
    // {
    // int i = base.readInt(arrayPosInfo);
    // i = PrimitiveImplUtils.putIntBits(value >>> arrayPosInfo.offset, i,
    // arrayPosInfo.offset, bits);
    // base.writeInt(arrayPosInfo, i);
    // }
    // else
    // {
    // int i1 = base.readInt(arrayPosInfo);
    // int i2 = Float.floatToRawIntBits(array[arrayPosInfo.index + 1]);
    // int leftBits = 32 - arrayPosInfo.offset;
    // int rightBits = bits - leftBits;
    // i1 = PrimitiveImplUtils.putIntBits(value >>> arrayPosInfo.offset, i1,
    // arrayPosInfo.offset, leftBits);
    // i2 = PrimitiveImplUtils.putIntBits(value << (Float.SIZE - rightBits), i2,
    // 0,
    // rightBits);
    // base.writeInt(arrayPosInfo, i1);
    // array[arrayPosInfo.index + 1] = Float.intBitsToFloat((int)i2);
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
    // long l = base.readLong(arrayPosInfo);
    // l = PrimitiveImplUtils.putLongBits(value >>> arrayPosInfo.offset, l,
    // arrayPosInfo.offset, bits);
    // base.writeLong(arrayPosInfo, l);
    // }
    // else
    // {
    // long l1 = base.readLong(arrayPosInfo);
    // long l2 = Float.floatToRawIntBits(array[arrayPosInfo.index + 2]);
    // int leftBits = 64 - arrayPosInfo.offset;
    // int rightBits = bits - leftBits;
    // l1 = PrimitiveImplUtils.putLongBits(value >>> arrayPosInfo.offset, l1,
    // arrayPosInfo.offset, leftBits);
    // l2 = PrimitiveImplUtils.putLongBits(value << (Float.SIZE - rightBits),
    // l2, 0,
    // 32 + rightBits);
    // base.writeLong(arrayPosInfo, l1);
    // array[arrayPosInfo.index + 2] = Float.intBitsToFloat((int)l2);
    // }
    // }
    // }
    // }
}
