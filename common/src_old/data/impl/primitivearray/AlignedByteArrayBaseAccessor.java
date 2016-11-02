package com.cogician.quicker.binary.data.impl.primitivearray;

import java.nio.ByteBuffer;

import com.cogician.quicker.binary.BaseException;
import com.cogician.quicker.binary.Bits;

/**
 * 
 * 
 * @author Fred Suvn
 * @version 0.0.0, 2015-11-24 14:45:45
 * @since 0.0.0
 */
public class AlignedByteArrayBaseAccessor extends ByteArrayBaseAccessor {
    /**
     * Serial ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Array to store data.
     */
    //private final byte[] array;

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
    AlignedByteArrayBaseAccessor(final byte[] array, final int startIndex,
            final int startOffset, final int endIndex, final int endOffset) {
//        super(startIndex, startOffset, endIndex, endOffset);
//        this.array = array;
        super(array, startIndex, startOffset, endIndex, endOffset);
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
    AlignedByteArrayBaseAccessor(final byte[] array, final int startIndex,
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
    AlignedByteArrayBaseAccessor(final byte[] array) {
        this(array, 0, 0, array.length - 1, 7);
    }

    @Override
    protected int elementWide() {
        return 8;
    }

    @Override
    protected Bits shadowClone(final int startIndex,
            final int startOffset, final int endIndex, final int endOffset) {
        return new AlignedByteArrayBaseAccessor(array, startIndex, startOffset,
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
            return new AlignedByteArrayBaseAccessor(clone, 0, 0,
                    clone.length - 1, endOffset);
        } else {
            final byte[] clone = new byte[lengthInBits % elementWide == 0 ? (int) (lengthInBits / elementWide)
                    : (int) (lengthInBits / elementWide + 1)];
            final Bits cloneAccessor = new AlignedByteArrayBaseAccessor(
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
                && dest instanceof AlignedByteArrayBaseAccessor) {
            final AlignedByteArrayBaseAccessor shorter = (AlignedByteArrayBaseAccessor) PrimitiveImplUtil
                    .getShorterAccessor(this, dest);
            final AlignedByteArrayBaseAccessor copyDest = (AlignedByteArrayBaseAccessor) dest;
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
    public byte readByte(final int posInBytes) {
        return array[startIndex + posInBytes];
    }

    @Override
    public int readInt(final int posInBytes) {
        return ((array[startIndex + posInBytes] & 0xff) << 24)
                | ((array[startIndex + posInBytes + 1] & 0xff) << 16)
                | ((array[startIndex + posInBytes + 2] & 0xff) << 8)
                | ((array[startIndex + posInBytes + 3] & 0xff) << 0);
    }
    
    @Override
    public void writeByte(final int posInBytes, final byte value) {
        array[startIndex + posInBytes] = value;
    }
    
    @Override
    public void writeInt(final int posInBytes, final int value) {
        array[startIndex + posInBytes] = (byte) (value >>> 24);
        array[startIndex + posInBytes + 1] = (byte) (value >>> 16);
        array[startIndex + posInBytes + 2] = (byte) (value >>> 8);
        array[startIndex + posInBytes + 3] = (byte) (value >>> 0);
    }
}
