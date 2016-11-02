package com.cogician.quicker.binary.data.impl.primitivearray;

import java.nio.ByteBuffer;

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
 * This implementation use byte array to store and manipulate data randomly,
 * each element takes 8 bits. The effective data start from specified bit offset
 * of start index, end to specified bit offset of end index. This class is one
 * subclass of XxxArrayBaseAccessor in this package.
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
 * @version 0.0.0, 2015-05-23 19:46:02
 * @since 0.0.0
 * @see Bits
 * @see Bitss
 */
class ByteArrayBaseAccessor extends Bitss {

    /**
     * Serial ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Array to store data.
     */
    protected final byte[] array;

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
     *            bit offset of start index, [0, 7]
     * @param endIndex
     *            end index, [start index, array length - 1]
     * @param endOffset
     *            bit offset of end index, [0, 7]
     */
    ByteArrayBaseAccessor(final byte[] array, final int startIndex,
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
     *            bit offset of start index, [0, 7]
     */
    ByteArrayBaseAccessor(final byte[] array, final int startIndex,
            final int startOffset) {
        this(array, startIndex, startOffset, array.length - 1, 7);
    }

    /**
     * <p>
     * Constructs an instance with specified array. All array's data included.
     * </p>
     *
     * @param array
     *            specified array, not null
     */
    ByteArrayBaseAccessor(final byte[] array) {
        this(array, 0, 0, array.length - 1, 7);
    }

    @Override
    protected int elementWide() {
        return 8;
    }

    @Override
    protected Bits shadowClone(final int startIndex,
            final int startOffset, final int endIndex, final int endOffset) {
        return new ByteArrayBaseAccessor(array, startIndex, startOffset,
                endIndex, endOffset);
    }

    @Override
    public Bits deepClone() {
        // Byte alignment.
        // If startOffset is not 0, deep copied accessor's startOffset should be
        // aligned to 0.
        if (startOffset == 0) {
            final byte[] clone = new byte[endIndex - startIndex + 1];
            System.arraycopy(array, startIndex, clone, 0, clone.length);
            return new ByteArrayBaseAccessor(clone, 0, 0, clone.length - 1,
                    endOffset);
        } else {
            final byte[] clone = new byte[lengthInBits % elementWide == 0 ? (int) (lengthInBits / elementWide)
                    : (int) (lengthInBits / elementWide + 1)];
            final Bits cloneAccessor = new ByteArrayBaseAccessor(clone,
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
                && dest instanceof ByteArrayBaseAccessor) {
            final ByteArrayBaseAccessor shorter = (ByteArrayBaseAccessor) PrimitiveImplUtil
                    .getShorterAccessor(this, dest);
            final ByteArrayBaseAccessor copyDest = (ByteArrayBaseAccessor) dest;
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
                    byte thisTail = this.array[this.startIndex + len];
                    byte destTail = copyDest.array[copyDest.startIndex + len];
                    thisTail >>>= (elementWide - shorter.endOffset);
                    thisTail <<= (elementWide - shorter.endOffset);
                    destTail <<= (shorter.endOffset + 24);
                    destTail >>>= (shorter.endOffset + 24);
                    copyDest.array[copyDest.startIndex + len] = (byte) (thisTail | destTail);
                    return shorter.lengthInBits;
                } else if (this.startOffset != 0
                        && shorter.endIndex - shorter.startIndex >= 2) {
                    // Copies except head and tail
                    System.arraycopy(this.array, this.startIndex + 1,
                            copyDest.array, copyDest.startIndex + 1,
                            shorter.endIndex - shorter.startIndex - 1);
                    // Copies head
                    byte thisHead = this.array[this.startIndex];
                    byte destHead = copyDest.array[copyDest.startIndex];
                    thisHead <<= (this.startOffset + 24);
                    thisHead >>>= (this.startOffset + 24);
                    destHead >>>= (elementWide - this.startOffset);
                    destHead <<= (elementWide - this.startOffset);
                    copyDest.array[copyDest.startIndex] = (byte) (thisHead | destHead);
                    // Copies tail
                    final int len = shorter.endIndex - shorter.startIndex;
                    byte thisTail = this.array[this.startIndex + len];
                    byte destTail = copyDest.array[copyDest.startIndex + len];
                    thisTail >>>= (elementWide - shorter.endOffset);
                    thisTail <<= (elementWide - shorter.endOffset);
                    destTail <<= (shorter.endOffset + 24);
                    destTail >>>= (shorter.endOffset + 24);
                    copyDest.array[copyDest.startIndex + len] = (byte) (thisTail | destTail);
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
                return (array[index] & 0xff) << 24;
            }
            case 2: {
                return ((array[index] & 0xff) << 24)
                        | ((array[index] & 0xff) << 16);
            }
            case 3: {
                return ((array[index] & 0xff) << 24)
                        | ((array[index] & 0xff) << 16)
                        | ((array[index] & 0xff) << 8);
            }
            case 4: {
                return ((array[index] & 0xff) << 24)
                        | ((array[index] & 0xff) << 16)
                        | ((array[index] & 0xff) << 8)
                        | ((array[index] & 0xff) << 0);
            }
            default: {
                throw new BaseException("Elements number should be 1, 2, 3, 4.");
            }
        }
    }

    @Override
    protected long readArrayElementsAsLong(final int index,
            final int elementsNum) {
        switch (elementsNum) {
            case 1:
            case 2:
            case 3:
            case 4: {
                final long l = readArrayElements(index, elementsNum);
                return l << 32;
            }
            case 5: {
                final long l1 = readArrayElements(index, 4);
                final long l2 = readArrayElements(index + 4, 1) & 0x00000000FFFFFFFFL;
                return (l1 << 32) | l2;
            }
            case 6: {
                final long l1 = readArrayElements(index, 4);
                final long l2 = readArrayElements(index + 4, 2) & 0x00000000FFFFFFFFL;
                return (l1 << 32) | l2;
            }
            case 7: {
                final long l1 = readArrayElements(index, 4);
                final long l2 = readArrayElements(index + 4, 3) & 0x00000000FFFFFFFFL;
                return (l1 << 32) | l2;
            }
            case 8: {
                final long l1 = readArrayElements(index, 4);
                final long l2 = readArrayElements(index + 4, 4) & 0x00000000FFFFFFFFL;
                return (l1 << 32) | l2;
            }
            default: {
                throw new BaseException(
                        "Elements number should be 1, 2, 3, 4, 5, 6, 7, 8.");
            }
        }
    }

    @Override
    protected void writeArrayElements(final int index, final int value,
            final int elementsNum) {
        switch (elementsNum) {
            case 1: {
                array[index] = (byte) (value >>> 24);
                return;
            }
            case 2: {
                array[index] = (byte) (value >>> 24);
                array[index + 1] = (byte) (value >>> 16);
                return;
            }
            case 3: {
                array[index] = (byte) (value >>> 24);
                array[index + 1] = (byte) (value >>> 16);
                array[index + 2] = (byte) (value >>> 8);
                return;
            }
            case 4: {
                array[index] = (byte) (value >>> 24);
                array[index + 1] = (byte) (value >>> 16);
                array[index + 2] = (byte) (value >>> 8);
                array[index + 3] = (byte) (value >>> 0);
                return;
            }
            default: {
                throw new BaseException(
                        "Elements number should be 1, 2, 3 , 4.");
            }
        }
    }
    
    @Override
    public long readLong(final int posInBytes) {
        return readAsLong(posInBytes * 8L, 64);
    }

    @Override
    protected void writeArrayElements(final int index, final long value,
            final int elementsNum) {
        switch (elementsNum) {
            case 1: {
                array[index] = (byte) (value >>> 56);
                return;
            }
            case 2: {
                array[index] = (byte) (value >>> 56);
                array[index + 1] = (byte) (value >>> 48);
                return;
            }
            case 3: {
                array[index] = (byte) (value >>> 56);
                array[index + 1] = (byte) (value >>> 48);
                array[index + 2] = (byte) (value >>> 40);
                return;
            }
            case 4: {
                array[index] = (byte) (value >>> 56);
                array[index + 1] = (byte) (value >>> 48);
                array[index + 2] = (byte) (value >>> 40);
                array[index + 3] = (byte) (value >>> 32);
                return;
            }
            case 5: {
                array[index] = (byte) (value >>> 56);
                array[index + 1] = (byte) (value >>> 48);
                array[index + 2] = (byte) (value >>> 40);
                array[index + 3] = (byte) (value >>> 32);
                array[index + 4] = (byte) (value >>> 24);
                return;
            }
            case 6: {
                array[index] = (byte) (value >>> 56);
                array[index + 1] = (byte) (value >>> 48);
                array[index + 2] = (byte) (value >>> 40);
                array[index + 3] = (byte) (value >>> 32);
                array[index + 4] = (byte) (value >>> 24);
                array[index + 5] = (byte) (value >>> 16);
                return;
            }
            case 7: {
                array[index] = (byte) (value >>> 56);
                array[index + 1] = (byte) (value >>> 48);
                array[index + 2] = (byte) (value >>> 40);
                array[index + 3] = (byte) (value >>> 32);
                array[index + 4] = (byte) (value >>> 24);
                array[index + 5] = (byte) (value >>> 16);
                array[index + 6] = (byte) (value >>> 8);
                return;
            }
            case 8: {
                array[index] = (byte) (value >>> 56);
                array[index + 1] = (byte) (value >>> 48);
                array[index + 2] = (byte) (value >>> 40);
                array[index + 3] = (byte) (value >>> 32);
                array[index + 4] = (byte) (value >>> 24);
                array[index + 5] = (byte) (value >>> 16);
                array[index + 6] = (byte) (value >>> 8);
                array[index + 7] = (byte) (value >>> 0);
                return;
            }
            default: {
                throw new BaseException(
                        "Elements number should be 1, 2, 3, 4, 5, 6, 7, 8.");
            }
        }
    }

    @Override
    protected int readIntDirect(int posInBytes) {
        return ((array[startIndex + posInBytes] & 0xff) << 24)
                | ((array[startIndex + posInBytes + 1] & 0xff) << 16)
                | ((array[startIndex + posInBytes + 2] & 0xff) << 8)
                | ((array[startIndex + posInBytes + 3] & 0xff) << 0);
    }

    @Override
    protected void writeIntDirect(int posInBytes, int value) {
        array[startIndex + posInBytes] = (byte) (value >>> 24);
        array[startIndex + posInBytes + 1] = (byte) (value >>> 16);
        array[startIndex + posInBytes + 2] = (byte) (value >>> 8);
        array[startIndex + posInBytes + 3] = (byte) (value >>> 0);
    }

//    @Override
//    protected int readArrayElements(final int index, final int elementsNum) {
//        switch (elementsNum) {
//            case 1: {
//                return (array[index] & 0x000000FF) << 24;
//            }
//            case 2: {
//                return ((array[index] & 0x000000FF) << 24)
//                        | ((array[index + 1] & 0x000000FF) << 16);
//            }
//            case 3: {
//                return ((array[index] & 0x000000FF) << 24)
//                        | ((array[index + 1] & 0x000000FF) << 16)
//                        | ((array[index + 2] & 0x000000FF) << 8);
//            }
//            case 4: {
//                return ((array[index] & 0x000000FF) << 24)
//                        | ((array[index + 1] & 0x000000FF) << 16)
//                        | ((array[index + 2] & 0x000000FF) << 8)
//                        | ((array[index + 3] & 0x000000FF) << 0);
//            }
//            default: {
//                throw new BaseException("Elements number should be 1, 2, 3, 4.");
//            }
//        }
//    }
//
//    @Override
//    protected long readArrayElementsAsLong(final int index,
//            final int elementsNum) {
//        switch (elementsNum) {
//            case 1:
//            case 2:
//            case 3:
//            case 4: {
//                final long l = readArrayElements(index, elementsNum);
//                return l << 32;
//            }
//            case 5: {
//                final long l1 = readArrayElements(index, 4);
//                final long l2 = readArrayElements(index + 4, 1) & 0x00000000FFFFFFFFL;
//                return (l1 << 32) | l2;
//            }
//            case 6: {
//                final long l1 = readArrayElements(index, 4);
//                final long l2 = readArrayElements(index + 4, 2) & 0x00000000FFFFFFFFL;
//                return (l1 << 32) | l2;
//            }
//            case 7: {
//                final long l1 = readArrayElements(index, 4);
//                final long l2 = readArrayElements(index + 4, 3) & 0x00000000FFFFFFFFL;
//                return (l1 << 32) | l2;
//            }
//            case 8: {
//                final long l1 = readArrayElements(index, 4);
//                final long l2 = readArrayElements(index + 4, 4) & 0x00000000FFFFFFFFL;
//                return (l1 << 32) | l2;
//            }
//            default: {
//                throw new BaseException(
//                        "Elements number should be 1, 2, 3, 4, 5, 6, 7, 8.");
//            }
//        }
//    }
//
//    @Override
//    protected void writeArrayElements(final int index, final int value,
//            final int elementsNum) {
//        switch (elementsNum) {
//            case 1: {
//                array[index] = (byte) (value >>> 24);
//                return;
//            }
//            case 2: {
//                array[index] = (byte) (value >>> 24);
//                array[index + 1] = (byte) (value >>> 16);
//                return;
//            }
//            case 3: {
//                array[index] = (byte) (value >>> 24);
//                array[index + 1] = (byte) (value >>> 16);
//                array[index + 2] = (byte) (value >>> 8);
//                return;
//            }
//            case 4: {
//                array[index] = (byte) (value >>> 24);
//                array[index + 1] = (byte) (value >>> 16);
//                array[index + 2] = (byte) (value >>> 8);
//                array[index + 3] = (byte) (value >>> 0);
//                return;
//            }
//            default: {
//                throw new BaseException(
//                        "Elements number should be 1, 2, 3 , 4.");
//            }
//        }
//    }
//
//    @Override
//    protected void writeArrayElements(final int index, final long value,
//            final int elementsNum) {
//        switch (elementsNum) {
//            case 1: {
//                array[index] = (byte) (value >>> 56);
//                return;
//            }
//            case 2: {
//                array[index] = (byte) (value >>> 56);
//                array[index + 1] = (byte) (value >>> 48);
//                return;
//            }
//            case 3: {
//                array[index] = (byte) (value >>> 56);
//                array[index + 1] = (byte) (value >>> 48);
//                array[index + 2] = (byte) (value >>> 40);
//                return;
//            }
//            case 4: {
//                array[index] = (byte) (value >>> 56);
//                array[index + 1] = (byte) (value >>> 48);
//                array[index + 2] = (byte) (value >>> 40);
//                array[index + 3] = (byte) (value >>> 32);
//                return;
//            }
//            case 5: {
//                array[index] = (byte) (value >>> 56);
//                array[index + 1] = (byte) (value >>> 48);
//                array[index + 2] = (byte) (value >>> 40);
//                array[index + 3] = (byte) (value >>> 32);
//                array[index + 4] = (byte) (value >>> 24);
//                return;
//            }
//            case 6: {
//                array[index] = (byte) (value >>> 56);
//                array[index + 1] = (byte) (value >>> 48);
//                array[index + 2] = (byte) (value >>> 40);
//                array[index + 3] = (byte) (value >>> 32);
//                array[index + 4] = (byte) (value >>> 24);
//                array[index + 5] = (byte) (value >>> 16);
//                return;
//            }
//            case 7: {
//                array[index] = (byte) (value >>> 56);
//                array[index + 1] = (byte) (value >>> 48);
//                array[index + 2] = (byte) (value >>> 40);
//                array[index + 3] = (byte) (value >>> 32);
//                array[index + 4] = (byte) (value >>> 24);
//                array[index + 5] = (byte) (value >>> 16);
//                array[index + 6] = (byte) (value >>> 8);
//                return;
//            }
//            case 8: {
//                array[index] = (byte) (value >>> 56);
//                array[index + 1] = (byte) (value >>> 48);
//                array[index + 2] = (byte) (value >>> 40);
//                array[index + 3] = (byte) (value >>> 32);
//                array[index + 4] = (byte) (value >>> 24);
//                array[index + 5] = (byte) (value >>> 16);
//                array[index + 6] = (byte) (value >>> 8);
//                array[index + 7] = (byte) (value >>> 0);
//                return;
//            }
//            default: {
//                throw new BaseException(
//                        "Elements number should be 1, 2, 3, 4, 5, 6, 7, 8.");
//            }
//        }
//    }
    //
    // @Override
    // public int readInt(int posInBytes) {
    // if (startOffset == 0) {
    // int index = startIndex + posInBytes;
    // return ((array[index] & 0x000000FF) << 24)
    // | ((array[index + 1] & 0x000000FF) << 16)
    // | ((array[index + 2] & 0x000000FF) << 8)
    // | ((array[index + 3] & 0x000000FF) << 0);
    // } else {
    // return read(posInBytes * 8L, 32);
    // }
    // }
    //
    // @Override
    // public int readInt(long posInBits) {
    // long absolutePos = absolutePos(posInBits);
    // int index = (int) (absolutePos >>> 32);
    // int offset = (int) absolutePos;
    // if (offset == 0) {
    // return ((array[index] & 0x000000FF) << 24)
    // | ((array[index + 1] & 0x000000FF) << 16)
    // | ((array[index + 2] & 0x000000FF) << 8)
    // | ((array[index + 3] & 0x000000FF) << 0);
    // } else {
    // return readByAbsolutePos(absolutePos, 32);
    // }
    // }
    //
    // @Override
    // public long readLong(int posInBytes) {
    // if (startOffset == 0) {
    // int index = startIndex + posInBytes;
    // int i1 = ((array[index] & 0x000000FF) << 24)
    // | ((array[index + 1] & 0x000000FF) << 16)
    // | ((array[index + 2] & 0x000000FF) << 8)
    // | ((array[index + 3] & 0x000000FF) << 0);
    // int i2 = ((array[index + 4] & 0x000000FF) << 24)
    // | ((array[index + 5] & 0x000000FF) << 16)
    // | ((array[index + 6] & 0x000000FF) << 8)
    // | ((array[index + 7] & 0x000000FF) << 0);
    // return (((long) i1) << 32) | (i2 & 0x00000000FFFFFFFFL);
    // } else {
    // return readAsLong(posInBytes * 8L, 64);
    // }
    // }
    //
    // @Override
    // public long readLong(long posInBits) {
    // long absolutePos = absolutePos(posInBits);
    // int index = (int) (absolutePos >>> 32);
    // int offset = (int) absolutePos;
    // if (offset == 0) {
    // int i1 = ((array[index] & 0x000000FF) << 24)
    // | ((array[index + 1] & 0x000000FF) << 16)
    // | ((array[index + 2] & 0x000000FF) << 8)
    // | ((array[index + 3] & 0x000000FF) << 0);
    // int i2 = ((array[index + 4] & 0x000000FF) << 24)
    // | ((array[index + 5] & 0x000000FF) << 16)
    // | ((array[index + 6] & 0x000000FF) << 8)
    // | ((array[index + 7] & 0x000000FF) << 0);
    // return (((long) i1) << 32) | (i2 & 0x00000000FFFFFFFFL);
    // } else {
    // return readByAbsolutePosAsLong(absolutePos, 64);
    // }
    // }
    //
    // @Override
    // public int readBitsAsInt(int posInBytes, int bitsNum) {
    // if (this.startOffset == 0 && bitsNum == 8) {
    // return array[startIndex + posInBytes] & 0x000000FF;
    // } else {
    // return read(posInBytes * 8L, bitsNum);
    // }
    // }
    //
    // @Override
    // public int readBitsAsInt(long posInBits, int bitsNum) {
    // //if ()
    // return read(posInBits, bitsNum);
    // }
    //
    // @Override
    // public long readBitsAsLong(int posInBytes, int bitsNum) {
    // return readAsLong(posInBytes * 8L, bitsNum);
    // }
    //
    // @Override
    // public long readBitsAsLong(long posInBits, int bitsNum) {
    // return readAsLong(posInBits, bitsNum);
    // }
    //
    // @Override
    // public void writeInt(int posInBytes, int value) {
    // write(posInBytes * 8L, value, 32);
    // }
    //
    // @Override
    // public void writeInt(long posInBits, int value) {
    // write(posInBits, value, 32);
    // }
    //
    // @Override
    // public void writeLong(int posInBytes, long value) {
    // write(posInBytes * 8L, value, 64);
    // }
    //
    // @Override
    // public void writeLong(long posInBits, long value) {
    // write(posInBits, value, 64);
    // }
    //
    // @Override
    // public void writeBits(int posInBytes, int value, int bitsNum) {
    // write(posInBytes * 8L, value, bitsNum);
    // }
    //
    // @Override
    // public void writeBits(long posInBits, int value, int bitsNum) {
    // write(posInBits, value, bitsNum);
    // }
    //
    // @Override
    // public void writeBits(int posInBytes, long value, int bitsNum) {
    // write(posInBytes, value, bitsNum);
    // }
    //
    // @Override
    // public void writeBits(long posInBits, long value, int bitsNum) {
    // write(posInBits, value, bitsNum);
    // }

    // @Override
    // protected int read1to7Bits(long posInBits, int bitsNum) {
    // long absolutePos = absolutePos(posInBits);
    // int index = (int) (absolutePos >>> 32);
    // int offset = (int) absolutePos;
    // if (offset == 0) {
    // return (array[index] & 0x000000FF) >>> (8 - bitsNum);
    // } else if (offset + bitsNum <= 8) {
    // int i = (array[index] << offset) & 0x000000FF;
    // i >>>= (8 - bitsNum);
    // return i;
    // } else {
    // int i = ((array[index] & 0x000000FF) << 8)
    // | (array[index + 1] & 0x000000FF);
    // i <<= offset;
    // i &= 0x0000FFFF;
    // i >>>= (16 - bitsNum);
    // return i;
    // }
    // }
    //
    //
    // @Override
    // protected int readBytes(long posInBits, int bytesNum) {
    // long absolutePos = absolutePos(posInBits);
    // int index = (int) (absolutePos >>> 32);
    // int offset = (int) absolutePos;
    // if (offset == 0) {
    // switch (bytesNum) {
    // case 1: {
    // return array[index] & 0x000000FF;
    // }
    // case 2: {
    // return ((array[index] & 0x000000FF) << 8)
    // | (array[index + 1] & 0x000000FF);
    // }
    // case 3: {
    // return ((array[index] & 0x000000FF) << 16)
    // | ((array[index + 1] & 0x000000FF) << 8)
    // | (array[index + 2] & 0x000000FF);
    // }
    // case 4: {
    // return ((array[index] & 0x000000FF) << 24)
    // | ((array[index + 1] & 0x000000FF) << 16)
    // | ((array[index + 2] & 0x000000FF) << 8)
    // | (array[index + 3] & 0x000000FF);
    // }
    // default: {
    // throw new IllegalArgumentException(
    // "bytesNum should be one of (1, 2, 3, 4).");
    // }
    // }
    // } else {
    // switch (bytesNum) {
    // case 1: {
    // int i = ((array[index] & 0x000000FF) << 8)
    // | (array[index + 1] & 0x000000FF);
    // return ((i << offset) & 0x0000FF00) >>> 8;
    // }
    // case 2: {
    // int i = ((array[index] & 0x000000FF) << 16)
    // | ((array[index + 1] & 0x000000FF) << 8)
    // | (array[index + 2] & 0x000000FF);
    // return ((i << offset) & 0x00FFFF00) >>> 8;
    // }
    // case 3: {
    // int i = ((array[index] & 0x000000FF) << 24)
    // | ((array[index + 1] & 0x000000FF) << 16)
    // | ((array[index + 2] & 0x000000FF) << 8)
    // | (array[index + 3] & 0x000000FF);
    // return ((i << offset) & 0xFFFFFF00) >>> 8;
    // }
    // case 4: {
    // long l = ((array[index] & 0x00000000000000FFL) << 32)
    // | ((array[index + 1] & 0x00000000000000FFL) << 24)
    // | ((array[index + 2] & 0x00000000000000FFL) << 16)
    // | ((array[index + 3] & 0x00000000000000FFL) << 8)
    // | (array[index + 4] & 0x00000000000000FFL);
    // return (int) (((l << offset) & 0x000000FFFFFFFF00L) >>> 8);
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
