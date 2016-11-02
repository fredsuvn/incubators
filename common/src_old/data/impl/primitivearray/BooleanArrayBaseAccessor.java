package com.cogician.quicker.binary.data.impl.primitivearray;

import com.cogician.quicker.annotation.Base;
import com.cogician.quicker.binary.Bits;
import com.cogician.quicker.binary.Bitss;

/**
 * <p>
 * Implementation of {@link Bits}, more detail for seeing
 * {@link Bits}. Note this implementation cannot be empty.
 * </p>
 * <p>
 * This implementation use boolean array to store and manipulate data randomly,
 * each element takes 1 bit. The effective data start from specified bit offset
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
 * @version 0.0.0, 2015-10-31 20:44:13
 * @since 0.0.0
 * @see Bits
 * @see Bitss
 */
public class BooleanArrayBaseAccessor extends Bitss {

    /**
     * Serial ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Array to store data.
     */
    private final boolean[] array;

    /**
     * <p>
     * Constructs an instance with specified array, start index and end index.
     * End index should greater than or equal to start index.
     * </p>
     *
     * @param array
     *            specified array, not null
     * @param startIndex
     *            start index, [0, array length - 1]
     * @param endIndex
     *            end index, [start index, array length - 1]
     */
    BooleanArrayBaseAccessor(final boolean[] array, final int startIndex,
            final int endIndex) {
        super(startIndex, 0, endIndex, 0);
        this.array = array;
    }

    /**
     * <p>
     * Constructs an instance with specified array and start index. Data
     * included from start index to end of array.
     * </p>
     *
     * @param array
     *            specified array, not null
     * @param startIndex
     *            start index, [0, array length - 1]
     */
    BooleanArrayBaseAccessor(final boolean[] array, final int startIndex) {
        this(array, startIndex, array.length - 1);
    }

    /**
     * <p>
     * Constructs an instance with specified array. All array's data included.
     * </p>
     *
     * @param array
     *            specified array, not null
     */
    BooleanArrayBaseAccessor(final boolean[] array) {
        this(array, 0, array.length - 1);
    }

    @Override
    public boolean readBoolean(final int posInBytes) {
        return array[posInBytes * 8 + startIndex];
    }

    @Override
    public boolean readBoolean(final long posInBits) {
        return array[(int) posInBits + startIndex];
    }

    @Override
    public int readInt(final int posInBytes) {
        return readArrayElements(posInBytes * 8 + startIndex, 32);
    }

    @Override
    public int readInt(final long posInBits) {
        return readArrayElements((int) posInBits + startIndex, 32);
    }

    @Override
    public long readLong(final int posInBytes) {
        return readArrayElementsAsLong(posInBytes * 8 + startIndex, 64);
    }

    @Override
    public long readLong(final long posInBits) {
        return readArrayElementsAsLong((int) posInBits + startIndex, 64);
    }

    @Override
    public void writeBoolean(final int posInBytes, final boolean value) {
        array[posInBytes * 8 + startIndex] = value;
    }

    @Override
    public void writeBoolean(final long posInBits, final boolean value) {
        array[(int) posInBits + startIndex] = value;
    }

    @Override
    public void writeInt(final int posInBytes, final int value) {
        writeArrayElements(posInBytes * 8 + startIndex, value, 32);
    }

    @Override
    public void writeInt(final long posInBits, final int value) {
        writeArrayElements((int) posInBits + startIndex, value, 32);
    }

    @Override
    public void writeLong(final int posInBytes, final long value) {
        writeArrayElements(posInBytes * 8 + startIndex, value, 64);
    }

    @Override
    public void writeLong(final long posInBits, final long value) {
        writeArrayElements((int) posInBits + startIndex, value, 64);
    }

    @Override
    public int toBooleanArray(final boolean[] dest, final int startIndex) {
        final int length = lengthInBits <= dest.length - startIndex ? (int) lengthInBits
                : dest.length - startIndex;
        System.arraycopy(array, this.startIndex, dest, startIndex, length);
        return length;
    }

    @Override
    public boolean[] toBooleanArray() {
        final boolean[] clone = new boolean[(int) lengthInBits];
        System.arraycopy(array, startIndex, clone, 0, clone.length);
        return clone;
    }

    @Override
    public Bits deepClone() {
        final boolean[] cloneArray = toBooleanArray();
        return new BooleanArrayBaseAccessor(cloneArray);
    }

    @Override
    public void setBits(final boolean value) {
        for (int i = startIndex; i <= endIndex; i++) {
            array[i] = value;
        }
    }

    @Override
    public long copyTo(final Bits dest) {
        if (dest instanceof BooleanArrayBaseAccessor) {
            final BooleanArrayBaseAccessor shorter = (BooleanArrayBaseAccessor) PrimitiveImplUtil
                    .getShorterAccessor(this, dest);
            final BooleanArrayBaseAccessor copyDest = (BooleanArrayBaseAccessor) dest;
            System.arraycopy(array, startIndex, copyDest, copyDest.startIndex,
                    (int) shorter.lengthInBits);
            return shorter.lengthInBits;
        }
        return super.copyTo(dest);
    }

    @Override
    public Object getSourceData() {
        return array;
    }

    @Override
    protected int elementWide() {
        return 1;
    }

    @Override
    protected Bits shadowClone(final int startIndex,
            final int startOffset, final int endIndex, final int endOffset) {
        return new BooleanArrayBaseAccessor(array, startIndex, endIndex);
    }

    @Override
    protected int readArrayElements(final int index, final int elementsNum) {
        int result = 0;
        for (int i = 0; i < elementsNum; i++) {
            if (array[index + i]) {
                result |= (1 << (31 - i));
            }
        }
        return result;
    }

    @Override
    protected long readArrayElementsAsLong(final int index,
            final int elementsNum) {
        long result = 0;
        for (int i = 0; i < elementsNum; i++) {
            if (array[index + i]) {
                result |= (1L << (63 - i));
            }
        }
        return result;
    }

    @Override
    protected void writeArrayElements(final int index, final int value,
            final int elementsNum) {
        for (int i = 0; i < elementsNum; i++) {
            array[index + i] = ((value >>> (31 - i)) & 1) == 1;
        }
    }

    @Override
    protected void writeArrayElements(final int index, final long value,
            final int elementsNum) {
        for (int i = 0; i < elementsNum; i++) {
            array[index + i] = ((value >>> (63 - i)) & 1L) == 1L;
        }
    }
}
