package com.cogician.quicker.binary;

import com.cogician.quicker.Checker;
import com.cogician.quicker.Quicker;
import com.sun.istack.internal.Nullable;

/**
 * <p>
 * A {@linkplain Binary} implementation which use byte array as backed data storage. This class can be created from
 * {@linkplain Binary#wrap(byte[])} or {@linkplain Binary#wrap(byte[], int, int)}.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-08-29T17:47:01+08:00
 * @since 0.0.0, 2016-08-29T17:47:01+08:00
 */
public class ByteArrayBinary extends AbstractBinary implements Binary {

    private final byte[] data;

    private final int startIndex;

    private final int endIndex;

    /**
     * <p>
     * Constructs with specified byte array. This binary is big-endian.
     * </p>
     * 
     * @param data
     *            specified byte array
     * @throws NullPointerException
     *             if specified byte array is null
     * @since 0.0.0
     */
    public ByteArrayBinary(byte[] data) throws NullPointerException {
        this(data, 0, data.length, null);
    }

    /**
     * <p>
     * Constructs with specified byte array between specified start index inclusive and end index exclusive, and byte
     * order processor. If given byte order processor is null, use default {@linkplain ByteOrderProcessor#BIG_ENDIAN}.
     * </p>
     * 
     * @param data
     *            specified byte array
     * @param startIndex
     *            specified start index inclusive
     * @param endIndex
     *            specified end index exclusive
     * @param orderProcessor
     *            given byte order processor
     * @throws NullPointerException
     *             if specified byte array is null
     * @throws IndexOutOfBoundsException
     *             if indexes out of bounds
     * @since 0.0.0
     */
    public ByteArrayBinary(byte[] data, int startIndex, int endIndex, @Nullable ByteOrderProcessor orderProcessor)
            throws NullPointerException, IndexOutOfBoundsException {
        super(new ByteArrayReadWrite(data, startIndex), orderProcessor);
        this.data = data;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    @Override
    public Binary getBinary(long index, long length)
            throws IndexOutOfBoundsException, IllegalArgumentException, BinaryException {
        Checker.checkRangeIndexes(index, index + length, length());
        byte[] copy = new byte[(int)length];
        Binary copyBin = new ByteArrayBinary(copy, 0, copy.length, getByteOrderProcessor());
        copy(index, copyBin, 0, length);
        return copyBin;
    }

    @Override
    public void copy(long srcIndex, Binary dest, long destIndex, long length)
            throws IndexOutOfBoundsException, IllegalArgumentException, BinaryException, BinaryException {
        if (dest instanceof ByteArrayBinary) {
            Checker.checkLength(length);
            Checker.checkRangeIndexes(srcIndex, srcIndex + length, length());
            Checker.checkRangeIndexes(destIndex, destIndex + length, dest.length());
            System.arraycopy(data, (int)srcIndex, ((ByteArrayBinary)dest).data, (int)destIndex, (int)length);
        } else {
            Binary.super.copy(srcIndex, dest, destIndex, length);
        }
    }

    @Override
    public byte[] wrappedArray() {
        return data;
    }

    @Override
    public long length() {
        return endIndex - startIndex;
    }

    private static class ByteArrayReadWrite implements BaseReadWrite {

        private final byte[] data;

        private final int offset;

        private ByteArrayReadWrite(byte[] data, int offset) throws NullPointerException, IndexOutOfBoundsException {
            this.data = Quicker.require(data);
            Checker.checkIndex(offset, data);
            this.offset = offset;
        }

        @Override
        public byte _getByte(long index) throws IndexOutOfBoundsException, BinaryException {
            return data[(int)(offset + index)];
        }

        @Override
        public short _getShort(long index) throws IndexOutOfBoundsException, BinaryException {
            return BytesUtil.toShort(data[(int)(offset + index)], data[(int)(offset + index + 1)]);
        }

        @Override
        public char _getChar(long index) throws IndexOutOfBoundsException, BinaryException {
            return BytesUtil.toChar(data[(int)(offset + index)], data[(int)(offset + index + 1)]);
        }

        @Override
        public int _getMedium(long index) throws IndexOutOfBoundsException, BinaryException {
            return BytesUtil.toMedium(data[(int)(offset + index)], data[(int)(offset + index + 1)],
                    data[(int)(offset + index + 2)]);
        }

        @Override
        public int _getInt(long index) throws IndexOutOfBoundsException, BinaryException {
            return BytesUtil.toInt(data[(int)(offset + index)], data[(int)(offset + index + 1)],
                    data[(int)(offset + index + 2)], data[(int)(offset + index + 3)]);
        }

        @Override
        public long _getLong(long index) throws IndexOutOfBoundsException, BinaryException {
            return BytesUtil.toLong(data[(int)(offset + index)], data[(int)(offset + index + 1)],
                    data[(int)(offset + index + 2)], data[(int)(offset + index + 3)], data[(int)(offset + index + 4)],
                    data[(int)(offset + index + 5)], data[(int)(offset + index + 6)], data[(int)(offset + index + 7)]);
        }

        @Override
        public void _setByte(long index, int value) throws IndexOutOfBoundsException, BinaryException {
            data[(int)(offset + index)] = (byte)value;
        }

        @Override
        public void _setShort(long index, int value) throws IndexOutOfBoundsException, BinaryException {
            data[(int)(offset + index)] = (byte)(value >> 8);
            data[(int)(offset + index + 1)] = (byte)value;
        }

        @Override
        public void _setChar(long index, int value) throws IndexOutOfBoundsException, BinaryException {
            data[(int)(offset + index)] = (byte)(value >> 8);
            data[(int)(offset + index + 1)] = (byte)value;
        }

        @Override
        public void _setMedium(long index, int value) throws IndexOutOfBoundsException, BinaryException {
            data[(int)(offset + index)] = (byte)(value >> 16);
            data[(int)(offset + index + 1)] = (byte)(value >> 8);
            data[(int)(offset + index + 2)] = (byte)value;
        }

        @Override
        public void _setInt(long index, int value) throws IndexOutOfBoundsException, BinaryException {
            data[(int)(offset + index)] = (byte)(value >> 24);
            data[(int)(offset + index + 1)] = (byte)(value >> 16);
            data[(int)(offset + index + 2)] = (byte)(value >> 8);
            data[(int)(offset + index + 3)] = (byte)value;
        }

        @Override
        public void _setLong(long index, long value) throws IndexOutOfBoundsException, BinaryException {
            data[(int)(offset + index)] = (byte)(value >> 56);
            data[(int)(offset + index + 1)] = (byte)(value >> 48);
            data[(int)(offset + index + 2)] = (byte)(value >> 40);
            data[(int)(offset + index + 3)] = (byte)(value >> 32);
            data[(int)(offset + index + 4)] = (byte)(value >> 24);
            data[(int)(offset + index + 5)] = (byte)(value >> 16);
            data[(int)(offset + index + 6)] = (byte)(value >> 8);
            data[(int)(offset + index + 7)] = (byte)value;
        }
    }
}
