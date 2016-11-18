package com.cogician.quicker.binary;

import com.cogician.quicker.Checker;
import com.cogician.quicker.Quicker;

/**
 * <p>
 * A {@linkplain Binary} which if sub-binary of another binary.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-08-30T11:37:21+08:00
 * @since 0.0.0, 2016-08-30T11:37:21+08:00
 */
public class SubBinary implements Binary {

    private final Binary source;

    private final long startIndex;

    private final long endIndex;

    /**
     * <p>
     * Constructs with specified source binary, start index inclusive and end index exclusive.
     * </p>
     * 
     * @param source
     *            specified source binary
     * @param startIndex
     *            start index inclusive
     * @param endIndex
     *            end index exclusive
     * @throws NullPointerException
     *             if specified source binary is null
     * @throws IndexOutOfBoundsException
     *             if indexes out of bounds
     * @since 0.0.0
     */
    public SubBinary(Binary source, long startIndex, long endIndex)
            throws NullPointerException, IndexOutOfBoundsException {
        this.source = Quicker.require(source);
        Checker.checkRangeIndexes(startIndex, endIndex, source.length());
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    @Override
    public ByteOrderProcessor getByteOrderProcessor() {
        return source.getByteOrderProcessor();
    }

    @Override
    public long length() {
        return endIndex - startIndex;
    }

    @Override
    public byte getByte(long index) throws IndexOutOfBoundsException, BinaryException {
        return source.getByte(startIndex + index);
    }

    @Override
    public short getShort(long index) throws IndexOutOfBoundsException, BinaryException {
        return source.getShort(startIndex + index);
    }

    @Override
    public char getChar(long index) throws IndexOutOfBoundsException, BinaryException {
        return source.getChar(startIndex + index);
    }

    @Override
    public int getMedium(long index) throws IndexOutOfBoundsException, BinaryException {
        return source.getMedium(startIndex + index);
    }

    @Override
    public int getInt(long index) throws IndexOutOfBoundsException, BinaryException {
        return source.getInt(startIndex + index);
    }

    @Override
    public long getLong(long index) throws IndexOutOfBoundsException, BinaryException {
        return source.getLong(startIndex + index);
    }

    @Override
    public float getFloat(long index) throws IndexOutOfBoundsException, BinaryException {
        return source.getFloat(startIndex + index);
    }

    @Override
    public double getDouble(long index) throws IndexOutOfBoundsException, BinaryException {
        return source.getDouble(startIndex + index);
    }

    @Override
    public long getPrimitive(long index, int bytesNum)
            throws IllegalArgumentException, IndexOutOfBoundsException, BinaryException {
        return source.getPrimitive(startIndex + index, bytesNum);
    }

    @Override
    public Binary getBinary(long index, long length)
            throws IndexOutOfBoundsException, IllegalArgumentException, BinaryException {
        return source.getBinary(startIndex + index, length);
    }

    @Override
    public void setByte(long index, int value) throws IndexOutOfBoundsException, BinaryException {
        source.setByte(startIndex + index, value);
    }

    @Override
    public void setShort(long index, int value) throws IndexOutOfBoundsException, BinaryException {
        source.setShort(startIndex + index, value);
    }

    @Override
    public void setChar(long index, int value) throws IndexOutOfBoundsException, BinaryException {
        source.setChar(startIndex + index, value);
    }

    @Override
    public void setMedium(long index, int value) throws IndexOutOfBoundsException, BinaryException {
        source.setMedium(startIndex + index, value);
    }

    @Override
    public void setInt(long index, int value) throws IndexOutOfBoundsException, BinaryException {
        source.setInt(startIndex + index, value);
    }

    @Override
    public void setLong(long index, long value) throws IndexOutOfBoundsException, BinaryException {
        source.setLong(startIndex + index, value);
    }

    @Override
    public void setFloat(long index, float value) throws IndexOutOfBoundsException, BinaryException {
        source.setFloat(startIndex + index, value);
    }

    @Override
    public void setDouble(long index, double value) throws IndexOutOfBoundsException, BinaryException {
        source.setDouble(startIndex + index, value);
    }

    @Override
    public void setPrimitive(long index, long value, int bytesNum)
            throws IllegalArgumentException, IndexOutOfBoundsException, BinaryException {
        source.setPrimitive(startIndex + index, value, bytesNum);
    }

    @Override
    public Binary subBinary(long from, long to) throws IndexOutOfBoundsException, IllegalArgumentException {
        return new SubBinary(this, from, to);
    }

    @Override
    public byte[] wrappedArray() {
        return source.wrappedArray();
    }
}
