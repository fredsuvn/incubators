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
 * This implementation use char array to store and manipulate data randomly,
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
 * @version 0.0.0, 2015-05-23 19:46:24
 * @since 0.0.0
 * @see Bits
 * @see Bitss
 */
final class CharArrayBaseAccessor extends Bitss {

    /**
     * Serial ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Array to store data.
     */
    private final char[] array;

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
    CharArrayBaseAccessor(final char[] array, final int startIndex,
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
    CharArrayBaseAccessor(final char[] array, final int startIndex,
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
    CharArrayBaseAccessor(final char[] array) {
        this(array, 0, 0, array.length - 1, 15);
    }

    @Override
    protected int elementWide() {
        return 16;
    }

    @Override
    protected Bits shadowClone(final int startIndex,
            final int startOffset, final int endIndex, final int endOffset) {
        return new CharArrayBaseAccessor(array, startIndex, startOffset,
                endIndex, endOffset);
    }

    @Override
    public Bits deepClone() {
        // Byte alignment.
        // If startOffset is not 0, deep copied accessor's startOffset should be
        // aligned to 0.
        if (startOffset == 0) {
            final char[] clone = new char[endIndex - startIndex + 1];
            System.arraycopy(array, startIndex, clone, 0, clone.length);
            return new CharArrayBaseAccessor(clone, 0, 0, clone.length - 1,
                    endOffset);
        } else {
            final char[] clone = new char[lengthInBits % elementWide == 0 ? (int) (lengthInBits / elementWide)
                    : (int) (lengthInBits / elementWide + 1)];
            final Bits cloneAccessor = new CharArrayBaseAccessor(clone,
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
                && dest instanceof CharArrayBaseAccessor) {
            final CharArrayBaseAccessor shorter = (CharArrayBaseAccessor) PrimitiveImplUtil
                    .getShorterAccessor(this, dest);
            final CharArrayBaseAccessor copyDest = (CharArrayBaseAccessor) dest;
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
                    char thisTail = this.array[this.startIndex + len];
                    char destTail = copyDest.array[copyDest.startIndex + len];
                    thisTail >>>= (elementWide - shorter.endOffset);
                    thisTail <<= (elementWide - shorter.endOffset);
                    destTail <<= (shorter.endOffset + 16);
                    destTail >>>= (shorter.endOffset + 16);
                    copyDest.array[copyDest.startIndex + len] = (char) (thisTail | destTail);
                    return shorter.lengthInBits;
                } else if (this.startOffset != 0
                        && shorter.endIndex - shorter.startIndex >= 2) {
                    // Copies except head and tail
                    System.arraycopy(this.array, this.startIndex + 1,
                            copyDest.array, copyDest.startIndex + 1,
                            shorter.endIndex - shorter.startIndex - 1);
                    // Copies head
                    char thisHead = this.array[this.startIndex];
                    char destHead = copyDest.array[copyDest.startIndex];
                    thisHead <<= (this.startOffset + 16);
                    thisHead >>>= (this.startOffset + 16);
                    destHead >>>= (elementWide - this.startOffset);
                    destHead <<= (elementWide - this.startOffset);
                    copyDest.array[copyDest.startIndex] = (char) (thisHead | destHead);
                    // Copies tail
                    final int len = shorter.endIndex - shorter.startIndex;
                    char thisTail = this.array[this.startIndex + len];
                    char destTail = copyDest.array[copyDest.startIndex + len];
                    thisTail >>>= (elementWide - shorter.endOffset);
                    thisTail <<= (elementWide - shorter.endOffset);
                    destTail <<= (shorter.endOffset + 16);
                    destTail >>>= (shorter.endOffset + 16);
                    copyDest.array[copyDest.startIndex + len] = (char) (thisTail | destTail);
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
                array[index] = (char) (value >>> 16);
                return;
            }
            case 2: {
                array[index] = (char) (value >>> 16);
                array[index + 1] = (char) (value >>> 0);
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
                array[index] = (char) (value >>> 48);
                return;
            }
            case 2: {
                array[index] = (char) (value >>> 48);
                array[index + 1] = (char) (value >>> 32);
                return;
            }
            case 3: {
                array[index] = (char) (value >>> 48);
                array[index + 1] = (char) (value >>> 32);
                array[index + 2] = (char) (value >>> 16);
                return;
            }
            case 4: {
                array[index] = (char) (value >>> 48);
                array[index + 1] = (char) (value >>> 32);
                array[index + 2] = (char) (value >>> 16);
                array[index + 3] = (char) (value >>> 0);
                return;
            }
            default: {
                throw new BaseException("Elements number should be 1, 2, 3, 4.");
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
    // private final char[] array;
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
    // CharArrayBaseAccessor(char[] array, int startIndex, int startOffset, int
    // endIndex, int endOffset)
    // {
    // this.array = array;
    // this.mark = new CharArrayMark(startIndex, startOffset, endIndex,
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
    // CharArrayBaseAccessor(char[] array)
    // {
    // this(array, 0, 0, array.length - 1, Character.SIZE - 1);
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
    // CharArrayBaseAccessor(char[] array, int startIndex, int startOffset)
    // {
    // this(array, startIndex, startOffset, array.length - 1, Character.SIZE -
    // 1);
    // }
    //
    // @Override
    // protected BaseReadWrite getBaseReadWrite()
    // {
    // if (mark.startOffset == 0)
    // {
    // return new AlignedCharArrayBaseReadWrite(this.array);
    // }
    // else
    // {
    // return new CharArrayBaseReadWrite(this.array);
    // }
    // }
    //
    // @Override
    // protected BaseAccessor shadowClone(int startIndex, int startOffset, int
    // endIndex, int endOffset)
    // {
    // return new CharArrayBaseAccessor(array, startIndex, startOffset,
    // endIndex, endOffset);
    // }
    //
    // @Override
    // public BaseAccessor deepClone()
    // {
    // char[] copy = new char[mark.endIndex - mark.startIndex + 1];
    // System.arraycopy(array, mark.startIndex, copy, 0, copy.length);
    // return new CharArrayBaseAccessor(copy, 0, mark.startOffset, copy.length -
    // 1, mark.endOffset);
    // }
    //
    // @Override
    // public void setBits(boolean value)
    // {
    // if (mark.startIndex == mark.endIndex)
    // {
    // int i = PrimitiveImplUtils.fillIntBits(array[mark.startIndex], 16 +
    // mark.startOffset,
    // mark.endOffset - mark.startOffset + 1, value);
    // array[mark.startIndex] = (char)(i);
    // }
    // else
    // {
    // if (mark.endIndex - mark.startIndex > 1)
    // {
    // if (value)
    // {
    // for (int i = mark.startIndex + 1; i < mark.endIndex; i++)
    // {
    // array[i] = (char)0xffffffff;
    // }
    // }
    // else
    // {
    // for (int i = mark.startIndex + 1; i < mark.endIndex; i++)
    // {
    // array[i] = 0;
    // }
    // }
    // }
    // int i = PrimitiveImplUtils.fillIntBits(array[mark.startIndex], 16 +
    // mark.startOffset,
    // mark.elementWide() - mark.startOffset, value);
    // array[mark.startIndex] = (char)(i);
    // i = PrimitiveImplUtils.fillIntBits(array[mark.endIndex], 16,
    // mark.endOffset + 1, value);
    // array[mark.endIndex] = (char)(i);
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
    // private CharArrayBaseAccessor getShorterAccessor(CharArrayBaseAccessor
    // a1,
    // CharArrayBaseAccessor a2)
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
    // CharArrayBaseAccessor impl = (CharArrayBaseAccessor)dest;
    // CharArrayBaseAccessor shorter = getShorterAccessor(this, impl);
    // if (this.mark.startOffset == impl.mark.startOffset)
    // {
    // if (shorter.mark.startIndex == shorter.mark.endIndex)
    // {
    // int i = PrimitiveImplUtils.putIntBits(this.array[this.mark.startIndex],
    // impl.array[impl.mark.startIndex], 16 + this.mark.startOffset,
    // (int)shorter.mark.lengthInBits());
    // impl.array[impl.mark.startIndex] = (char)(i);
    // }
    // else
    // {
    // if (shorter.mark.endIndex - shorter.mark.startIndex > 1)
    // {
    // System.arraycopy(this.array, this.mark.startIndex + 1, impl.array,
    // impl.mark.startIndex + 1, shorter.mark.endIndex
    // - shorter.mark.startIndex - 1);
    // }
    // int i = PrimitiveImplUtils.putIntBits(this.array[this.mark.startIndex],
    // impl.array[impl.mark.startIndex], 16 + this.mark.startOffset,
    // mark.elementWide() - this.mark.startOffset);
    // impl.array[impl.mark.startIndex] = (char)(i);
    // i = PrimitiveImplUtils.putIntBits(this.array[this.mark.startIndex
    // + (shorter.mark.endIndex - shorter.mark.startIndex)],
    // impl.array[impl.mark.startIndex
    // + (shorter.mark.endIndex - shorter.mark.startIndex)], 16,
    // shorter.mark.endOffset + 1);
    // impl.array[impl.mark.startIndex + (shorter.mark.endIndex -
    // shorter.mark.startIndex)] = (char)(i);
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
    // int i = array[mark.startIndex];
    // i = PrimitiveImplUtils.putIntBits(~i, i, 16 + mark.startOffset,
    // (int)mark.lengthInBits());
    // array[mark.startIndex] = (char)(i);
    // }
    // else
    // {
    // if (mark.endIndex - mark.startIndex > 1)
    // {
    // for (int i = mark.startIndex + 1; i <= mark.endIndex - 1; i++)
    // {
    // array[i] = (char)~array[i];
    // }
    // }
    // int i = array[mark.startIndex];
    // i = PrimitiveImplUtils.putIntBits(~i, i, 16 + mark.startOffset,
    // mark.elementWide()
    // - mark.startOffset);
    // array[mark.startIndex] = (char)(i);
    // i = array[mark.endIndex];
    // i = PrimitiveImplUtils.putIntBits(~i, i, 16, mark.endOffset + 1);
    // array[mark.endIndex] = (char)(i);
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
    // BaseReadWrite} of char array.
    // * Its bit offset of start byte array should align to 0.
    // *
    // * @author Fred Suvn
    // * @version 0.0.0, 2015-06-01 09:26:22
    // */
    // private static final class AlignedCharArrayBaseReadWrite implements
    // BaseReadWrite
    // {
    // /**
    // * Array of data
    // */
    // private final char[] array;
    //
    // /**
    // * Construction.
    // *
    // * @param array
    // * array of data
    // */
    // private AlignedCharArrayBaseReadWrite(char[] array)
    // {
    // this.array = array;
    // }
    //
    // @Override
    // public int readInt(ArrayPos arrayPosInfo)
    // {
    // int i = array[arrayPosInfo.index] << 16;
    // i |= array[arrayPosInfo.index + 1] & 0x0000ffff;
    // return i;
    // }
    //
    // @Override
    // public long readLong(ArrayPos arrayPosInfo)
    // {
    // long l = array[arrayPosInfo.index] << 48;
    // l |= (array[arrayPosInfo.index + 1] & 0x000000000000ffffL) << 32;
    // l |= (array[arrayPosInfo.index + 2] & 0x000000000000ffffL) << 16;
    // l |= array[arrayPosInfo.index + 3] & 0x000000000000ffffL;
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
    // if (bits <= 16)
    // {
    // return PrimitiveImplUtils.partOfInt(array[arrayPosInfo.index], 16, bits);
    // }
    // else
    // {
    // return PrimitiveImplUtils.partOfInt(readInt(arrayPosInfo), 0, bits);
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
    // if (bits <= 48)
    // {
    // long l = array[arrayPosInfo.index] << 32;
    // l |= (array[arrayPosInfo.index + 1] & 0x000000000000ffffL) << 16;
    // l |= array[arrayPosInfo.index + 2] & 0x000000000000ffffL;
    // return PrimitiveImplUtils.partOfLong(l, 16, bits);
    // }
    // else
    // {
    // return PrimitiveImplUtils.partOfLong(readLong(arrayPosInfo), 0, bits);
    // }
    // }
    // }
    //
    // @Override
    // public void writeInt(ArrayPos arrayPosInfo, int value)
    // {
    // array[arrayPosInfo.index] = (char)(value >>> 16);
    // array[arrayPosInfo.index + 1] = (char)(value);
    // }
    //
    // @Override
    // public void writeLong(ArrayPos arrayPosInfo, long value)
    // {
    // array[arrayPosInfo.index] = (char)(value >>> 48);
    // array[arrayPosInfo.index + 1] = (char)(value >>> 32);
    // array[arrayPosInfo.index + 2] = (char)(value >>> 16);
    // array[arrayPosInfo.index + 3] = (char)(value);
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
    // if (bits <= 16)
    // {
    // int i = PrimitiveImplUtils.putIntBits(value >>> 16,
    // array[arrayPosInfo.index],
    // 16, bits);
    // array[arrayPosInfo.index] = (char)i;
    // }
    // else
    // {
    // int i = readInt(arrayPosInfo);
    // i = PrimitiveImplUtils.putIntBits(value, i, 0, bits);
    // writeInt(arrayPosInfo, i);
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
    // if (bits <= 48)
    // {
    // long l = array[arrayPosInfo.index] << 32;
    // l |= (array[arrayPosInfo.index + 1] & 0x000000000000ffffL) << 16;
    // l |= array[arrayPosInfo.index + 2] & 0x000000000000ffffL;
    // l = PrimitiveImplUtils.putLongBits(value >>> 16, l, 16, bits);
    // array[arrayPosInfo.index] = (char)(l >> 32);
    // array[arrayPosInfo.index + 1] = (char)(l >> 16);
    // array[arrayPosInfo.index + 2] = (char)l;
    // }
    // else
    // {
    // long l = readLong(arrayPosInfo);
    // l = PrimitiveImplUtils.putLongBits(value, l, 0, bits);
    // writeLong(arrayPosInfo, l);
    // }
    // }
    // }
    //
    // }
    //
    // /**
    // * This class represents implementation of {@linkplain BaseReadWrite} of
    // char array. Its bit
    // * offset of start byte array shouldn't align to 0.
    // *
    // * @author Fred Suvn
    // * @version 0.0.0, 2015-06-01 09:25:13
    // */
    // private static final class CharArrayBaseReadWrite implements
    // BaseReadWrite
    // {
    // /**
    // * Array of data
    // */
    // private final char[] array;
    //
    // /**
    // * Base read write, to read and write align to multiple of byte element.
    // */
    // private final AlignedCharArrayBaseReadWrite base;
    //
    // /**
    // * Construction.
    // *
    // * @param array
    // * array of data
    // */
    // private CharArrayBaseReadWrite(char[] array)
    // {
    // this.array = array;
    // this.base = new AlignedCharArrayBaseReadWrite(this.array);
    // }
    //
    // @Override
    // public int readInt(ArrayPos arrayPosInfo)
    // {
    // int i1 = base.readInt(arrayPosInfo);
    // int i2 = array[arrayPosInfo.index + 2];
    // i1 <<= arrayPosInfo.offset;
    // i2 >>>= Character.SIZE - arrayPosInfo.offset;
    // i2 &= 0x0000ffff;
    // return i1 | i2;
    // }
    //
    // @Override
    // public long readLong(ArrayPos arrayPosInfo)
    // {
    // long l1 = base.readLong(arrayPosInfo);
    // long l2 = array[arrayPosInfo.index + 4];
    // l1 <<= arrayPosInfo.offset;
    // l2 >>>= Character.SIZE - arrayPosInfo.offset;
    // l2 &= 0x000000000000ffffL;
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
    // if (arrayPosInfo.offset + bits <= 16)
    // {
    // return PrimitiveImplUtils.partOfInt(array[arrayPosInfo.index],
    // 16 + arrayPosInfo.offset, bits);
    // }
    // else if (arrayPosInfo.offset + bits <= 32)
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
    // if (arrayPosInfo.offset + bits <= 48)
    // {
    // long l = array[arrayPosInfo.index] << 32;
    // l |= (array[arrayPosInfo.index + 1] & 0x000000000000ffffL) << 16;
    // l |= array[arrayPosInfo.index + 2] & 0x000000000000ffffL;
    // return PrimitiveImplUtils.partOfLong(l, 16 + arrayPosInfo.offset, bits);
    // }
    // else if (arrayPosInfo.offset + bits <= 64)
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
    // int i2 = array[arrayPosInfo.index + 2];
    // i1 = PrimitiveImplUtils.putIntBits(value >>> arrayPosInfo.offset, i1,
    // arrayPosInfo.offset, 32 - arrayPosInfo.offset);
    // i2 = PrimitiveImplUtils.putIntBits(value << (Character.SIZE -
    // arrayPosInfo.offset), i2,
    // 0, 16 + arrayPosInfo.offset);
    // base.writeInt(arrayPosInfo, i1);
    // array[arrayPosInfo.index + 2] = (char)(i2);
    // }
    //
    // @Override
    // public void writeLong(ArrayPos arrayPosInfo, long value)
    // {
    // long l1 = base.readLong(arrayPosInfo);
    // long l2 = array[arrayPosInfo.index + 4];
    // l1 = PrimitiveImplUtils.putLongBits(value >>> arrayPosInfo.offset, l1,
    // arrayPosInfo.offset, 64 - arrayPosInfo.offset);
    // l2 = PrimitiveImplUtils.putLongBits(value << (Character.SIZE -
    // arrayPosInfo.offset),
    // l2, 0, 48 + arrayPosInfo.offset);
    // base.writeLong(arrayPosInfo, l1);
    // array[arrayPosInfo.index + 4] = (char)(l2);
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
    // if (arrayPosInfo.offset + bits <= 16)
    // {
    // int i = PrimitiveImplUtils.putIntBits(value >>> (16 +
    // arrayPosInfo.offset),
    // array[arrayPosInfo.index], 16 + arrayPosInfo.offset, bits);
    // array[arrayPosInfo.index] = (char)i;
    // }
    // else if (arrayPosInfo.offset + bits <= 32)
    // {
    // int i = base.readInt(arrayPosInfo);
    // i = PrimitiveImplUtils.putIntBits(value >>> arrayPosInfo.offset, i,
    // arrayPosInfo.offset, bits);
    // base.writeInt(arrayPosInfo, i);
    // }
    // else
    // {
    // int i1 = base.readInt(arrayPosInfo);
    // int i2 = array[arrayPosInfo.index + 2];
    // int leftBits = 32 - arrayPosInfo.offset;
    // int rightBits = bits - leftBits;
    // i1 = PrimitiveImplUtils.putIntBits(value >>> arrayPosInfo.offset, i1,
    // arrayPosInfo.offset, leftBits);
    // i2 = PrimitiveImplUtils.putIntBits(value << (Character.SIZE - rightBits),
    // i2,
    // 0, 16 + rightBits);
    // base.writeInt(arrayPosInfo, i1);
    // array[arrayPosInfo.index + 2] = (char)(i2);
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
    // if (arrayPosInfo.offset + bits <= 48)
    // {
    // long l = array[arrayPosInfo.index] << 32;
    // l |= (array[arrayPosInfo.index + 1] & 0x000000000000ffffL) << 16;
    // l |= array[arrayPosInfo.index + 2] & 0x000000000000ffffL;
    // l = PrimitiveImplUtils.putLongBits(value >>> (16 + arrayPosInfo.offset),
    // l,
    // 16 + arrayPosInfo.offset, bits);
    // array[arrayPosInfo.index] = (char)(l >> 32);
    // array[arrayPosInfo.index + 1] = (char)(l >> 16);
    // array[arrayPosInfo.index + 2] = (char)l;
    // }
    // else if (arrayPosInfo.offset + bits <= 64)
    // {
    // long l = base.readLong(arrayPosInfo);
    // l = PrimitiveImplUtils.putLongBits(value >>> arrayPosInfo.offset, l,
    // arrayPosInfo.offset, bits);
    // base.writeLong(arrayPosInfo, l);
    // }
    // else
    // {
    // long l1 = base.readLong(arrayPosInfo);
    // long l2 = array[arrayPosInfo.index + 4];
    // int leftBits = 64 - arrayPosInfo.offset;
    // int rightBits = bits - leftBits;
    // l1 = PrimitiveImplUtils.putLongBits(value >>> arrayPosInfo.offset, l1,
    // arrayPosInfo.offset, leftBits);
    // l2 = PrimitiveImplUtils.putLongBits(value << (Character.SIZE -
    // rightBits), l2,
    // 0, 48 + rightBits);
    // base.writeLong(arrayPosInfo, l1);
    // array[arrayPosInfo.index + 4] = (char)(l2);
    // }
    // }
    // }
    // }
}
