package com.cogician.quicker.binary;

import com.cogician.quicker.Checker;
import com.cogician.quicker.Quicker;
import com.cogician.quicker.bigarray.ByteArray;
import com.sun.istack.internal.Nullable;

/**
 * <p>
 * A {@linkplain Binary} implementation which use {@linkplain ByteArray} as backed data storage. This class can be
 * created from {@linkplain Binary#alloc(long)} or {@linkplain Binary#alloc(long, ByteOrderProcessor)}.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-08-30T16:12:36+08:00
 * @since 0.0.0, 2016-08-30T16:12:36+08:00
 */
public class HeapBinary extends AbstractBinary implements Binary {

    private final HeapReadWrite base;

    /**
     * <p>
     * Constructs with specified length. This binary is big-endian.
     * </p>
     * 
     * @param length
     *            specified length
     * @throws IllegalArgumentException
     *             if specified length is negative
     * @since 0.0.0
     */
    public HeapBinary(long length) throws IllegalArgumentException {
        this(length, null);
    }

    /**
     * <p>
     * Constructs with specified length and byte order processor. If given byte order processor is null, use default
     * {@linkplain ByteOrderProcessor#BIG_ENDIAN}.
     * </p>
     * 
     * @param length
     *            specified length
     * @param orderProcessor
     *            specified byte order processor
     * @throws IllegalArgumentException
     *             if specified length is negative
     * @since 0.0.0
     */
    public HeapBinary(long length, @Nullable ByteOrderProcessor orderProcessor) throws IllegalArgumentException {
        this(new ByteArray(length), orderProcessor);
    }

    /**
     * <p>
     * Constructs with specified big byte array and byte order processor. If given byte order processor is null, use
     * default {@linkplain ByteOrderProcessor#BIG_ENDIAN}.
     * </p>
     * 
     * @param data
     *            specified big byte array
     * @param orderProcessor
     *            specified byte order processor
     * @throws NullPointerException
     *             if specified big byte array is null
     * @throws IllegalArgumentException
     *             if specified length is negative
     * @since 0.0.0
     */
    public HeapBinary(ByteArray data, @Nullable ByteOrderProcessor orderProcessor)
            throws NullPointerException, IllegalArgumentException {
        super(new HeapReadWrite(data), orderProcessor);
        this.base = (HeapReadWrite)getBaseReadWrite();
    }

    @Override
    public void copy(long srcIndex, Binary dest, long destIndex, long length)
            throws IndexOutOfBoundsException, IllegalArgumentException, BinaryException, BinaryException {
        if (dest instanceof HeapBinary) {
            Checker.checkLength(length);
            Checker.checkRangeIndexes(srcIndex, srcIndex + length, length());
            Checker.checkRangeIndexes(destIndex, destIndex + length, dest.length());
            base.data.copy(srcIndex, ((HeapBinary)dest).base.data, destIndex, length);
        } else {
            Binary.super.copy(srcIndex, dest, destIndex, length);
        }
    }

    @Override
    public byte[] wrappedArray() {
        return null;
    }

    @Override
    public long length() {
        return base.data.length();
    }

    private static class HeapReadWrite implements BaseReadWrite {

        private final ByteArray data;

        private HeapReadWrite(ByteArray data) throws NullPointerException {
            this.data = Quicker.require(data);
        }

        @Override
        public byte _getByte(long index) throws IndexOutOfBoundsException, BinaryException {
            return data.get(index);
        }

        @Override
        public short _getShort(long index) throws IndexOutOfBoundsException, BinaryException {
            return BytesUtil.toShort(data.get(index), data.get(index + 1));
        }

        @Override
        public char _getChar(long index) throws IndexOutOfBoundsException, BinaryException {
            return BytesUtil.toChar(data.get(index), data.get(index + 1));
        }

        @Override
        public int _getMedium(long index) throws IndexOutOfBoundsException, BinaryException {
            return BytesUtil.toMedium(data.get(index), data.get(index + 1), data.get(index + 2));
        }

        @Override
        public int _getInt(long index) throws IndexOutOfBoundsException, BinaryException {
            return BytesUtil.toInt(data.get(index), data.get(index + 1), data.get(index + 2), data.get(index + 3));
        }

        @Override
        public long _getLong(long index) throws IndexOutOfBoundsException, BinaryException {
            return BytesUtil.toLong(data.get(index), data.get(index + 1), data.get(index + 2), data.get(index + 3),
                    data.get(index + 4), data.get(index + 5), data.get(index + 6), data.get(index + 7));
        }

        @Override
        public void _setByte(long index, int value) throws IndexOutOfBoundsException, BinaryException {
            data.set(index, (byte)value);
        }

        @Override
        public void _setShort(long index, int value) throws IndexOutOfBoundsException, BinaryException {
            data.set(index, (byte)(value >> 8));
            data.set(index + 1, (byte)value);
        }

        @Override
        public void _setChar(long index, int value) throws IndexOutOfBoundsException, BinaryException {
            data.set(index, (byte)(value >> 8));
            data.set(index + 1, (byte)value);
        }

        @Override
        public void _setMedium(long index, int value) throws IndexOutOfBoundsException, BinaryException {
            data.set(index, (byte)(value >> 16));
            data.set(index + 1, (byte)(value >> 8));
            data.set(index + 2, (byte)value);
        }

        @Override
        public void _setInt(long index, int value) throws IndexOutOfBoundsException, BinaryException {
            data.set(index, (byte)(value >> 24));
            data.set(index + 1, (byte)(value >> 16));
            data.set(index + 2, (byte)(value >> 8));
            data.set(index + 3, (byte)value);
        }

        @Override
        public void _setLong(long index, long value) throws IndexOutOfBoundsException, BinaryException {
            data.set(index, (byte)(value >> 56));
            data.set(index + 1, (byte)(value >> 48));
            data.set(index + 2, (byte)(value >> 40));
            data.set(index + 3, (byte)(value >> 32));
            data.set(index + 4, (byte)(value >> 24));
            data.set(index + 5, (byte)(value >> 16));
            data.set(index + 6, (byte)(value >> 8));
            data.set(index + 7, (byte)value);
        }
    }
}
