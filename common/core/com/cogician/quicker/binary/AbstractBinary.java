package com.cogician.quicker.binary;

import com.cogician.quicker.Checker;
import com.cogician.quicker.Quicker;
import com.cogician.quicker.bigarray.BigArray;
import com.sun.istack.internal.Nullable;

/**
 * <p>
 * An abstract implementation of {@linkplain Binary}. This class implements most methods of binary by a
 * {@linkplain BaseReadWrite}.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-08-29T16:55:21+08:00
 * @since 0.0.0, 2016-08-29T16:55:21+08:00
 */
public abstract class AbstractBinary implements Binary {

    private final BaseReadWrite base;

    private final ByteOrderProcessor orderProcessor;

    /**
     * <p>
     * Constructs with specified base read write and byte order processor. If given byte order processor is null, use
     * default {@linkplain ByteOrderProcessor#BIG_ENDIAN}.
     * </p>
     * 
     * @param base
     *            specified base read write
     * @param orderProcessor
     *            given byte order processor
     * @throws NullPointerException
     *             if specified base read write is null
     * @since 0.0.0
     */
    protected AbstractBinary(BaseReadWrite base, @Nullable ByteOrderProcessor orderProcessor)
            throws NullPointerException {
        this.base = Quicker.require(base);
        this.orderProcessor = orderProcessor == null ? ByteOrderProcessor.BIG_ENDIAN : orderProcessor;
    }

    protected BaseReadWrite getBaseReadWrite() {
        return base;
    }

    @Override
    public ByteOrderProcessor getByteOrderProcessor() {
        return orderProcessor;
    }

    @Override
    public abstract long length();

    @Override
    public byte getByte(long index) throws IndexOutOfBoundsException, BinaryException {
        Checker.checkRangeIndexes(index, index + 1, length());
        return base._getByte(index);
    }

    @Override
    public short getShort(long index) throws IndexOutOfBoundsException, BinaryException {
        Checker.checkRangeIndexes(index, index + 2, length());
        return getByteOrderProcessor().doShort(base._getShort(index));
    }

    @Override
    public char getChar(long index) throws IndexOutOfBoundsException, BinaryException {
        Checker.checkRangeIndexes(index, index + 2, length());
        return getByteOrderProcessor().doChar(base._getChar(index));
    }

    @Override
    public int getMedium(long index) throws IndexOutOfBoundsException, BinaryException {
        Checker.checkRangeIndexes(index, index + 3, length());
        return getByteOrderProcessor().doMedium(base._getMedium(index));
    }

    @Override
    public int getInt(long index) throws IndexOutOfBoundsException, BinaryException {
        Checker.checkRangeIndexes(index, index + 4, length());
        return getByteOrderProcessor().doInt(base._getInt(index));
    }

    @Override
    public long getLong(long index) throws IndexOutOfBoundsException, BinaryException {
        Checker.checkRangeIndexes(index, index + 8, length());
        return getByteOrderProcessor().doLong(base._getLong(index));
    }

    @Override
    public float getFloat(long index) throws IndexOutOfBoundsException, BinaryException {
        Checker.checkRangeIndexes(index, index + 4, length());
        return Float.intBitsToFloat(getInt(index));
    }

    @Override
    public double getDouble(long index) throws IndexOutOfBoundsException, BinaryException {
        Checker.checkRangeIndexes(index, index + 8, length());
        return Double.longBitsToDouble(getLong(index));
    }

    @Override
    public long getPrimitive(long index, int bytesNum)
            throws IllegalArgumentException, IndexOutOfBoundsException, BinaryException {
        Checker.checkRangeIndexes(index, index + bytesNum, length());
        long row;
        switch (bytesNum) {
            case 1: {
                return getByte(index);
            }
            case 2: {
                return getShort(index);
            }
            case 3: {
                return getMedium(index);
            }
            case 4: {
                return getInt(index);
            }
            case 5: {
                long l1 = base._getByte(index);
                long l2 = BytesUtil.toUnsignedInt(base._getInt(index + 1));
                row = (l1 << 32) | l2;
                break;
            }
            case 6: {
                long l1 = base._getShort(index);
                long l2 = BytesUtil.toUnsignedInt(base._getInt(index + 2));
                row = (l1 << 32) | l2;
                break;
            }
            case 7: {
                long l1 = base._getMedium(index);
                long l2 = BytesUtil.toUnsignedInt(base._getInt(index + 3));
                row = (l1 << 32) | l2;
                break;
            }
            case 8: {
                return getLong(index);
            }
            default: {
                throw new IllegalArgumentException("Bytes number must in [1, 8]");
            }
        }
        return getByteOrderProcessor().doBytes(row, bytesNum);
    }

    @Override
    public Binary getBinary(long index, long length)
            throws IndexOutOfBoundsException, IllegalArgumentException, BinaryException {
        Checker.checkRangeIndexes(index, index + length, length());
        Binary copy;
        if (length <= BigArray.BLOCK_SIZE) {
            copy = Binary.wrap(new byte[(int)length]);
        } else {
            copy = Binary.alloc(length);
        }
        copy(index, copy, 0, length);
        return copy;
    }

    @Override
    public void setByte(long index, int value) throws IndexOutOfBoundsException, BinaryException {
        Checker.checkRangeIndexes(index, index + 1, length());
        base._setByte(index, value);
    }

    @Override
    public void setShort(long index, int value) throws IndexOutOfBoundsException, BinaryException {
        Checker.checkRangeIndexes(index, index + 2, length());
        base._setShort(index, getByteOrderProcessor().doShort((short)value));
    }

    @Override
    public void setChar(long index, int value) throws IndexOutOfBoundsException, BinaryException {
        Checker.checkRangeIndexes(index, index + 2, length());
        base._setChar(index, getByteOrderProcessor().doChar((char)value));
    }

    @Override
    public void setMedium(long index, int value) throws IndexOutOfBoundsException, BinaryException {
        Checker.checkRangeIndexes(index, index + 3, length());
        base._setMedium(index, getByteOrderProcessor().doMedium(value));
    }

    @Override
    public void setInt(long index, int value) throws IndexOutOfBoundsException, BinaryException {
        Checker.checkRangeIndexes(index, index + 4, length());
        base._setInt(index, getByteOrderProcessor().doInt(value));
    }

    @Override
    public void setLong(long index, long value) throws IndexOutOfBoundsException, BinaryException {
        Checker.checkRangeIndexes(index, index + 8, length());
        base._setLong(index, getByteOrderProcessor().doLong(value));
    }

    @Override
    public void setFloat(long index, float value) throws IndexOutOfBoundsException, BinaryException {
        Checker.checkRangeIndexes(index, index + 4, length());
        base._setFloat(index, getByteOrderProcessor().doFloat(value));
    }

    @Override
    public void setDouble(long index, double value) throws IndexOutOfBoundsException, BinaryException {
        Checker.checkRangeIndexes(index, index + 8, length());
        base._setDouble(index, getByteOrderProcessor().doDouble(value));
    }

    @Override
    public void setPrimitive(long index, long value, int bytesNum)
            throws IllegalArgumentException, IndexOutOfBoundsException, BinaryException {
        Checker.checkRangeIndexes(index, index + bytesNum, length());
        switch (bytesNum) {
            case 1: {
                setByte(index, (int)value);
                break;
            }
            case 2: {
                setShort(index, (int)value);
                break;
            }
            case 3: {
                setMedium(index, (int)value);
                break;
            }
            case 4: {
                setInt(index, (int)value);
                break;
            }
            case 5: {
                long b = getByteOrderProcessor().doBytes(value, bytesNum);
                base._setByte(index, (int)(b >> 32));
                base._setInt(index + 1, (int)b);
                break;
            }
            case 6: {
                long b = getByteOrderProcessor().doBytes(value, bytesNum);
                base._setShort(index, (int)(b >> 32));
                base._setInt(index + 2, (int)b);
                break;
            }
            case 7: {
                long b = getByteOrderProcessor().doBytes(value, bytesNum);
                base._setMedium(index, (int)(b >> 32));
                base._setInt(index + 3, (int)b);
                break;
            }
            case 8: {
                setLong(index, (int)value);
                break;
            }
            default: {
                throw new IllegalArgumentException("Bytes number must in [1, 8]");
            }
        }
    }
}
