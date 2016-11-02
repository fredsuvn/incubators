package com.cogician.quicker.binary.data.impl.primitivearray;

import com.cogician.quicker.annotation.Base;
import com.cogician.quicker.binary.Bits;
import com.cogician.quicker.binary.data.BaseAccessorFactory;
import com.cogician.quicker.util.BitsQuicker;

/**
 * <p>
 * To create instance of {@linkplain Bits}, the implementations use
 * array of primitive type to store bits data.
 * </p>
 * <p>
 * This factory is default creation factory.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2015-05-11 14:06:58
 * @since 0.0.0
 * @see Bits
 */
@Base
public final class PrimitiveArrayBaseAccessorFactoryImpl implements
        BaseAccessorFactory {

    /**
     * Single instance.
     */
    private static PrimitiveArrayBaseAccessorFactoryImpl impl = new PrimitiveArrayBaseAccessorFactoryImpl();

    /**
     * Single instance mode to hide constructor.
     */
    private PrimitiveArrayBaseAccessorFactoryImpl() {

    }

    /**
     * <p>
     * Returns single instance of factory.
     * </p>
     *
     * @return single instance of factory
     */
    public static BaseAccessorFactory singleInstance() {
        return impl;
    }

    @Override
    public Bits create(final long lengthInBits) {
        final long[] array = new long[BitsQuicker.leastLengthOfBitsToArray(
                lengthInBits, 64)];
        return new LongArrayBaseAccessor(array, 0, 0, array.length - 1,
                (int) ((lengthInBits - 1) % 64));
    }

    @Override
    public Bits create(final int lengthInByte) {
        return create(lengthInByte * 8L);
    }

    @Override
    public Bits wrap(final boolean[] array, final int startIndex,
            final int endIndex) {
        return new BooleanArrayBaseAccessor(array, startIndex, endIndex);
    }

    @Override
    public Bits wrap(final byte[] array, final int startIndex,
            final int startOffset, final int endIndex, final int endOffset) {
        return new ByteArrayBaseAccessor(array, startIndex, startOffset,
                endIndex, endOffset);
    }

    @Override
    public Bits wrap(final short[] array, final int startIndex,
            final int startOffset, final int endIndex, final int endOffset) {
        return new ShortArrayBaseAccessor(array, startIndex, startOffset,
                endIndex, endOffset);
    }

    @Override
    public Bits wrap(final char[] array, final int startIndex,
            final int startOffset, final int endIndex, final int endOffset) {
        return new CharArrayBaseAccessor(array, startIndex, startOffset,
                endIndex, endOffset);
    }

    @Override
    public Bits wrap(final int[] array, final int startIndex,
            final int startOffset, final int endIndex, final int endOffset) {
        return new IntArrayBaseAccessor(array, startIndex, startOffset,
                endIndex, endOffset);
    }

    @Override
    public Bits wrap(final float[] array, final int startIndex,
            final int startOffset, final int endIndex, final int endOffset) {
        return new FloatArrayBaseAccessor(array, startIndex, startOffset,
                endIndex, endOffset);
    }

    @Override
    public Bits wrap(final long[] array, final int startIndex,
            final int startOffset, final int endIndex, final int endOffset) {
        return new LongArrayBaseAccessor(array, startIndex, startOffset,
                endIndex, endOffset);
    }

    @Override
    public Bits wrap(final double[] array, final int startIndex,
            final int startOffset, final int endIndex, final int endOffset) {
        return new DoubleArrayBaseAccessor(array, startIndex, startOffset,
                endIndex, endOffset);
    }

}
