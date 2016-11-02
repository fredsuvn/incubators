package org.suvn.common.base.data.impl.primitivearray.old;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Objects;
import java.util.stream.Stream;

import javax.xml.stream.events.StartDocument;

import org.suvn.common.OutOfBoundsException;
import org.suvn.common.RandomReadWrite;
import org.suvn.common.base.data.BaseAccessor;
import org.suvn.common.base.data.BaseAccessorFactory;
import org.suvn.common.base.data.impl.primitivearray.PrimitiveArrayBaseAccessorFactoryImpl;

/**
 * This class represents a block of data， the length is less than {@link Integer#MAX_VALUE}, can be
 * access random accurate to bits.
 * <p>
 * A primitive value, boolean(a bit), byte, short, int, long, etc, can be read from a data block at
 * any position where in bounds. The data of this class also can be iterated or as a stream by
 * splitting in any length accurate to bits.
 * <p>
 * The data of this class is stored in a byte array, the modification between these is synchronized:
 * 
 * <pre>
 * byte[] array = {1, 2, 3};
 * DataBlock block = new DataBlock(array);
 * block.writeByte(0, 2);
 * System.out.println(array[0]); // Print 2!
 * </pre>
 * 
 * That is why it is recommended to only use {@code DataBlock} to manipulate data:
 * 
 * <pre>
 * <del>byte[] array = {1, 2, 3};</del>
 * DataBlock block = new DataBlock(3);
 * block.writeByte(0, 2);
 * System.out.println(block.readByte(0)); // Print 2!
 * </pre>
 * 
 * Note when a instance was created, its length cannot be changed.
 * <p>
 * This class is serializable and cloneable.
 * 
 * @author Fred Suvn
 * @version 0.0.0, 2015-03-04 14:56:20
 */
public final class DataBlockOld implements RandomReadWrite, Serializable, Cloneable
{
    /**
     * Serial ID
     */
    private static final long serialVersionUID = 0L;
    
    /**
     * Factory to create base accessor.
     */
    private static final BaseAccessorFactory factory = PrimitiveArrayBaseAccessorFactoryImpl.getFactory();
    
    /**
     * Base data accessor.
     */
    private final BaseAccessor accessor;
    
    /**
     * Constructs an empty data block, an empty data block'length is 0.
     */
    public DataBlockOld()
    {
        this.accessor = null;
    }
    
    /**
     * Constructs a data block with special length, the length is indicated by a byte length and
     * redundant bits of the byte length, initial data value is 0.
     * <p>
     * If byte length and redundant bit are both 0, an empty instance will be created.
     * 
     * @param lengthInBytes
     *            byte length, positive
     * @param redundantBits
     *            redundant bits of the byte length, [0, 7]
     * @throws IllegalArgumentException
     *             thrown if byte length is negative or redundant bits out of [0, 7]
     */
    public DataBlockOld(int lengthInBytes, int redundantBits)
    {
        if (lengthInBytes == 0 && redundantBits == 0)
        {
            this.accessor = null;
        }
        else if (lengthInBytes > 0 && redundantBits >= 0 && redundantBits <= 7)
        {
            this.accessor = factory.create(lengthInBytes, redundantBits);
        }
        else
        {
            throw new IllegalArgumentException(
                    "Byte length should be positive and redundant bits should be in [0, 7].");
        }
    }
    
    /**
     * Constructs a data block with special length in bits.
     * 
     * @param length
     *            length in bits, positive
     * @throws IllegalArgumentException
     *             thrown if length is negative
     */
    public DataBlockOld(long length)
    {
        if (length == 0)
        {
            this.accessor = null;
        }
        else if (length > 0)
        {
            this.accessor = factory.create(length);
        }
        else
        {
            throw new IllegalArgumentException("Length in bits should be positive.");
        }
    }
    
    /**
     * Constructs a data block with special byte array.
     * <p>
     * If length of array is 0, an empty instance will be created.
     * 
     * @param source
     *            special byte array, not null
     * @throws NullPointerException
     *             thrown if special byte array is null
     */
    public DataBlockOld(byte[] source)
    {
        source = Objects.requireNonNull(source);
        if (source.length == 0)
        {
            this.accessor = null;
        }
        else
        {
            this.accessor = factory.create(source);
        }
    }
    
    /**
     * Checks whether illegal or out of bounds.
     */
    private void checkArrayParams(byte[] source, int startIndex, int startBitOffset, int endIndex,
            int endBitOffset)
    {
        if (startIndex < 0 || startBitOffset < 0 || startBitOffset > 7 || endIndex < 0
                || endIndex < 0 || endIndex > 7)
        {
            throw new IllegalArgumentException("Index and bit offset should be legal.");
        }
        if (startIndex >= source.length || endIndex >= source.length || startIndex > endIndex
                || (startIndex == endIndex && startBitOffset > endBitOffset))
        {
            throw new OutOfBoundsException("Index and bit offset should be in bounds.");
        }
    }
    
    /**
     * Constructs a data block with part of special byte array, part from special start position to
     * end of array.
     * <p>
     * If length of array is 0, an empty instance will be created.
     * 
     * @param source
     *            special byte array, not null
     * @param startIndex
     *            start index in bounds
     * @param startBitOffset
     *            bit offset of start index, [0, 7]
     * @exception NullPointerException
     *                thrown if special byte array is null
     * @throws IllegalArgumentException
     *             thrown if special index or bit offset is illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    public DataBlockOld(byte[] source, int startIndex, int startBitOffset)
    {
        source = Objects.requireNonNull(source);
        if (source.length == 0)
        {
            this.accessor = null;
        }
        else
        {
            checkArrayParams(source, startIndex, startBitOffset, source.length - 1, 7);
            this.accessor = factory
                    .create(source, startIndex, startBitOffset, source.length - 1, 7);
        }
    }
    
    /**
     * Constructs a data block with part of special byte array, part from special start position to
     * special end position of array.
     * <p>
     * If length of array is 0, an empty instance will be created.
     * 
     * @param source
     *            special byte array, not null
     * @param startIndex
     *            start index in bounds
     * @param startBitOffset
     *            bit offset of start index, [0, 7]
     * @param endIndex
     *            end index in bounds
     * @param endBitOffset
     *            bit offset of end index, [0, 7]
     * @exception NullPointerException
     *                thrown if special byte array is null
     * @throws IllegalArgumentException
     *             thrown if special index or bit offset is illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    public DataBlockOld(byte[] source, int startIndex, int startBitOffset, int endIndex,
            int endBitOffset)
    {
        source = Objects.requireNonNull(source);
        if (source.length == 0)
        {
            this.accessor = null;
        }
        else
        {
            checkArrayParams(source, startIndex, startBitOffset, endIndex, endBitOffset);
            this.accessor = factory.create(source, startIndex, startBitOffset, endIndex,
                    endBitOffset);
        }
    }
    
    /**
     * Returns whether this data block is empty. An empty accesor's length is 0.
     * 
     * @return whether this data block is empty
     */
    public boolean isEmpty()
    {
        return null == accessor;
    }
    
    /**
     * Checks whether this block is empty, if empty, an {@linkplain OutOfBoundsException} will be
     * thrown.
     * 
     * @throws OutOfBoundsException
     *             thrown if empty
     */
    private void checkEmpty()
    {
        if (isEmpty())
        {
            throw new OutOfBoundsException("This block is empty.");
        }
    }
    
    /**
     * Checks whether special position is legal.
     * 
     * @param bytePos
     *            special byte position, positive
     * @param bitOffset
     *            bit offset of special byte position, [0, 7]
     * @throws IllegalArgumentException
     *             thrown if special position are illegal
     */
    private void checkPositionLegal(int bytePos, int bitOffset)
    {
        if (bytePos < 0 || bitOffset < 0 || bitOffset > 7)
        {
            throw new IllegalArgumentException(
                    "Special position should be positive, bit offset should be in [0, 7].");
        }
    }
    
    /**
     * Checks whether special position is legal.
     * 
     * @param pos
     *            special position, positive
     * @throws IllegalArgumentException
     *             thrown if special position are illegal
     */
    private void checkPositionLegal(long pos)
    {
        if (pos < 0)
        {
            throw new IllegalArgumentException("Special position should be positive.");
        }
    }
    
    /**
     * Checks whether special position in bounds.
     * 
     * @param bytePos
     *            special byte position in bounds
     * @param bitOffset
     *            bit offset of special byte position, [0, 7]
     * @throws OutOfBoundsException
     *             thrown if special position out of bounds
     */
    private void checkPositionInBounds(int bytePos, int bitOffset)
    {
        if (bytePos * 8L + bitOffset >= lengthInBits())
        {
            throw new OutOfBoundsException(bytePos * 8L + bitOffset);
        }
    }
    
    /**
     * Checks whether special position in bounds.
     * 
     * @param pos
     *            special position in bounds
     * @throws OutOfBoundsException
     *             thrown if special position out of bounds
     */
    private void checkPositionInBounds(long pos)
    {
        if (pos >= lengthInBits())
        {
            throw new OutOfBoundsException(pos);
        }
    }
    
    /**
     * Checks whether this block is empty or special position is legal and in bounds.
     * 
     * @param bytePos
     *            special byte position in bounds
     * @param bitOffset
     *            bit offset of special byte position, [0, 7]
     * @throws IllegalArgumentException
     *             thrown if special position are illegal
     * @throws OutOfBoundsException
     *             thrown if special position out of bounds
     */
    private void checkEmptyAndPosition(int bytePos, int bitOffset)
    {
        checkEmpty();
        checkPositionLegal(bytePos, bitOffset);
        checkPositionInBounds(bytePos, bitOffset);
    }
    
    /**
     * Checks whether this block is empty or special position is legal and in bounds.
     * 
     * @param pos
     *            special position in bounds
     * @throws IllegalArgumentException
     *             thrown if special position are illegal
     * @throws OutOfBoundsException
     *             thrown if special position out of bounds
     */
    private void checkEmptyAndPosition(long pos)
    {
        checkEmpty();
        checkPositionLegal(pos);
        checkPositionInBounds(pos);
    }
    
    /**
     * Checks whether this block is empty or special position and range from special position to
     * special bits length is in bounds.
     * 
     * @param bytePos
     *            special byte position in bounds
     * @param bitOffset
     *            bit offset of special byte position, [0, 7]
     * @param bits
     *            bits length
     * @throws IllegalArgumentException
     *             thrown if special position are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    private void checkEmptyAndPositionInSpecialBounds(int bytePos, int bitOffset, int bits)
    {
        checkEmpty();
        checkPositionLegal(bytePos, bitOffset);
        if (bits < 0)
        {
            throw new IllegalArgumentException("Bits length should be >= 0.");
        }
        long length = accessor.lengthInBits();
        if (bytePos * 8L + bitOffset + (bits - 1) > length - 1)
        {
            throw new OutOfBoundsException(bytePos * 8L + bitOffset + (bits - 1));
        }
    }
    
    /**
     * Checks whether this block is empty or special position and range from special position to
     * bits length is in bounds.
     * 
     * @param pos
     *            special position in bits in bounds
     * @param bits
     *            bits length
     * @throws IllegalArgumentException
     *             thrown if special position are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    private void checkEmptyAndPositionInSpecialBounds(long pos, int bits)
    {
        checkEmpty();
        checkPositionLegal(pos);
        if (bits < 0)
        {
            throw new IllegalArgumentException("Bits length should be >= 0.");
        }
        long length = accessor.lengthInBits();
        if (pos + (bits - 1) > length - 1)
        {
            throw new OutOfBoundsException(pos + (bits - 1));
        }
    }
    
    /**
     * Returns bits number of this data block.
     * 
     * @return bits number of this data block
     */
    public long lengthInBits()
    {
        return accessor.lengthInBits();
    }
    
    /**
     * Returns byte length of this data block. If the actual length is not a multiple of byte (8
     * bits), it will return length rounded down to byte.
     * 
     * @return byte length of this data block
     */
    public int lengthInBytes()
    {
        return accessor.lengthInBytes();
    }
    
    /**
     * Returns remainder bits number from special position inclusive to end of this data block
     * inclusive in bits, or -1 if this accessor is empty.
     * <p>
     * The special position is indicated by a byte position (specified by {@code bytePos}) and a bit
     * offset of the byte position (specified by {@code bitOffset}), byte position should be
     * positive and in bounds of space, bit offset should be in 0 inclusive to 8 exclusive.
     * 
     * @param bytePos
     *            special byte position in bounds
     * @param bitOffset
     *            bit offset of special byte position, [0, 7]
     * @return distance between special position and end of this data block in bits, or -1 if this
     *         accessor is empty
     * @throws OutOfBoundsException
     *             thrown if special position out of bounds
     */
    public long remainderLength(int bytePos, int bitOffset)
    {
        checkEmptyAndPosition(bytePos, bitOffset);
        return accessor.remainderLength(bytePos, bitOffset);
    }
    
    /**
     * Returns remainder bits number from special position inclusive to end of this data block
     * inclusive in bits, or -1 if this accessor is empty.
     * <p>
     * The special position is in bits and (specified by {@code pos}) should be positive and in
     * bounds of space.
     * <p>
     * The parameters of this method will not be checked. It will throw exceptions or return wrong
     * results if given parameters don't meet the requirements. The details of exceptions and wrong
     * results depend on implementations.
     * 
     * @param pos
     *            special position in bits in bounds return distance between special position and
     *            end of this data block in bits, or -1 if this accessor is empty
     * @throws OutOfBoundsException
     *             thrown if special position out of bounds
     */
    public long remainderLength(long pos)
    {
        checkEmptyAndPosition(pos);
        return accessor.remainderLength(pos);
    }
    
    /**
     * Constructs an instance with special base accessor.
     * <p>
     * If given accessor is null, an empty block will be created.
     * 
     * @param accessor
     *            base accessor
     */
    private DataBlockOld(BaseAccessor accessor)
    {
        this.accessor = accessor;
    }
    
    /**
     * Returns part of this data block from special start position by shadow copy, special length
     * will be copied. Data are shared between both accessor, modification is synchronized.
     * <p>
     * The special start position inclusive is indicated by a byte position (specified by
     * {@code startPos} ) and a bit offset of the byte position (specified by {@code startBitOffset}
     * ), byte position should be positive and in bounds of space, bit offset should be in 0
     * inclusive to 8 exclusive. Special length is indicated by a length in bytes (specified by
     * {@code lengthInByte}) and a redundant bits number of length in bytes (specified by
     * {@code redundantBits}), length in bytes should be positive , redundant bits number should be
     * in 0 inclusive to 8 exclusive. All copied data should be in bounds of this space.
     * 
     * @param startPos
     *            start byte position in bounds
     * @param startBitOffset
     *            bit offset of start byte position, [0, 7]
     * @param lengthInBytes
     *            length in bytes
     * @param redundantBits
     *            redundant bits number of byte length, [0, 7]
     * @return part of this data block from special start position by shadow copy
     * @throws IllegalArgumentException
     *             thrown if special position are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    public DataBlockOld partOf(int startPos, int startBitOffset, int lengthInBytes, int redundantBits)
    {
        checkEmptyAndPosition(startPos, startBitOffset);
        long remainder = accessor.remainderLength(startPos, startBitOffset);
        if (remainder >= lengthInBytes * 8L + redundantBits)
        {
            return new DataBlockOld(accessor.partOf(startPos, startBitOffset, lengthInBytes,
                    redundantBits));
        }
        else
        {
            throw new OutOfBoundsException(lengthInBytes * 8L + redundantBits);
        }
    }
    
    /**
     * Returns part of this data block from special start position by shadow copy, special length
     * will be copied. Data are shared between both accessor, modification is synchronized.
     * <p>
     * The special start position and special length are in bits and positive. All copied data
     * should be in bounds of this space.
     * 
     * @param startPos
     *            start position inclusive in bounds in bits
     * @param length
     *            special length in bounds in bits
     * @return part of this accessor from special start position by shadow copy
     * @throws IllegalArgumentException
     *             thrown if special position are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    public DataBlockOld partOf(long startPos, long length)
    {
        checkEmptyAndPosition(startPos);
        long remainder = accessor.remainderLength(startPos);
        if (remainder >= length)
        {
            return new DataBlockOld(accessor.partOf(startPos, length));
        }
        else
        {
            throw new OutOfBoundsException(length);
        }
    }
    
    /**
     * Returns part of this data block from special start position by shadow copy, special length
     * will be copied. Data are shared between both accessor, modification is synchronized.
     * <p>
     * The special start position inclusive is indicated by a byte position (specified by
     * {@code startPos} ) and a bit offset of the byte position (specified by {@code startBitOffset}
     * ), byte position should be positive and in bounds of space, bit offset should be in 0
     * inclusive to 8 exclusive. Special length are in bits and positive. All copied data should be
     * in bounds of this space.
     * 
     * @param startPos
     *            start byte position in bounds
     * @param startBitOffset
     *            bit offset of start byte position, [0, 7]
     * @param length
     *            special length in bounds in bits
     * @return part of this data block from special start position by shadow copy
     * @throws IllegalArgumentException
     *             thrown if special position are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    public DataBlockOld partOf(int startPos, int startBitOffset, long length)
    {
        checkEmptyAndPosition(startPos, startBitOffset);
        long remainder = accessor.remainderLength(startPos, startBitOffset);
        if (remainder >= length)
        {
            return new DataBlockOld(accessor.partOf(startPos * 8L + startBitOffset, length));
        }
        else
        {
            throw new OutOfBoundsException(length);
        }
    }
    
    /**
     * Deep copy this data block, return a new one.
     * 
     * @return a new instance deep copied by this data block
     */
    public DataBlockOld deepCopy()
    {
        return new DataBlockOld(accessor.deepClone());
    }
    
    /**
     * Set all bits of this data block to special value ,true is 1, false is 0.
     * 
     * @param value
     *            special value
     */
    public void setBits(boolean value)
    {
        accessor.setBits(value);
    }
    
    /**
     * Copies data of this data block to special dest data block, the copied length is indicated by
     * shorter one of two data block. This method copies data from head to tail.
     * 
     * @param dest
     *            special dest data block, not null
     * @throws NullPointerException
     *             thrown if dest data block is null
     */
    public void copy(DataBlockOld dest)
    {
        accessor.copy(Objects.requireNonNull(dest.accessor));
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public boolean readBoolean(int bytePos, int bitOffset)
    {
        checkEmptyAndPositionInSpecialBounds(bytePos, bitOffset, 1);
        return accessor.readBit(bytePos, bitOffset);
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public boolean readBoolean(long pos)
    {
        checkEmptyAndPositionInSpecialBounds(pos, 1);
        return accessor.readBit(pos);
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public byte readByte(int bytePos, int bitOffset)
    {
        checkEmptyAndPositionInSpecialBounds(bytePos, bitOffset, 8);
        return accessor.readByte(bytePos, bitOffset);
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public byte readByte(long pos)
    {
        checkEmptyAndPositionInSpecialBounds(pos, 0);
        return accessor.readByte(pos);
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public short readShort(int bytePos, int bitOffset)
    {
        checkEmptyAndPositionInSpecialBounds(bytePos, bitOffset, 16);
        return accessor.read2Bytes(bytePos, bitOffset);
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public short readShort(long pos)
    {
        checkEmptyAndPositionInSpecialBounds(pos, 16);
        return accessor.read2Bytes(pos);
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public char readChar(int bytePos, int bitOffset)
    {
        return (char)readShort(bytePos, bitOffset);
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public char readChar(long pos)
    {
        return (char)readShort(pos);
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public int readInt(int bytePos, int bitOffset)
    {
        checkEmptyAndPositionInSpecialBounds(bytePos, bitOffset, 32);
        return accessor.readInt(bytePos, bitOffset);
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public int readInt(long pos)
    {
        checkEmptyAndPositionInSpecialBounds(pos, 32);
        return accessor.read4Bytes(pos);
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public float readFloat(int bytePos, int bitOffset)
    {
        return Float.intBitsToFloat(readInt(bytePos, bitOffset));
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public float readFloat(long pos)
    {
        return Float.intBitsToFloat(readInt(pos));
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public long readLong(int bytePos, int bitOffset)
    {
        checkEmptyAndPositionInSpecialBounds(bytePos, bitOffset, 64);
        return accessor.read8Bytes(bytePos, bitOffset);
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public long readLong(long pos)
    {
        checkEmptyAndPositionInSpecialBounds(pos, 64);
        return accessor.read8Bytes(pos);
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public double readDouble(int bytePos, int bitOffset)
    {
        return Double.longBitsToDouble(readLong(bytePos, bitOffset));
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public double readDouble(long pos)
    {
        return Double.longBitsToDouble(readLong(pos));
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public byte readLastByte(int bytePos, int bitOffset)
    {
        checkEmptyAndPosition(bytePos, bitOffset);
        long remainder = accessor.remainderLength(bytePos, bitOffset);
        if (remainder >= 8L)
        {
            return accessor.readByte(bytePos, bitOffset);
        }
        else
        {
            return accessor.readBitsAsByte(bytePos, bitOffset, (int)remainder);
        }
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public byte readRemainderByte(long pos)
    {
        checkEmptyAndPosition(pos);
        long remainder = accessor.remainderLength(pos);
        if (remainder >= 8L)
        {
            return accessor.readByte(pos);
        }
        else
        {
            return accessor.readBitsAsByte(pos, (int)remainder);
        }
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public short readRemainderShort(int bytePos, int bitOffset)
    {
        checkEmptyAndPosition(bytePos, bitOffset);
        long remainder = accessor.remainderLength(bytePos, bitOffset);
        if (remainder >= 16L)
        {
            return accessor.read2Bytes(bytePos, bitOffset);
        }
        else
        {
            return accessor.readBitsAs2Bytes(bytePos, bitOffset, (int)remainder);
        }
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public short readRemainderShort(long pos)
    {
        checkEmptyAndPosition(pos);
        long remainder = accessor.remainderLength(pos);
        if (remainder >= 8L)
        {
            return accessor.readByte(pos);
        }
        else
        {
            return accessor.readBitsAsByte(pos, (int)remainder);
        }
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public char readRemainderChar(int bytePos, int bitOffset)
    {
        return (char)readRemainderShort(bytePos, bitOffset);
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public char readRemainderChar(long pos)
    {
        return (char)readRemainderShort(pos);
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public int readRemainderInt(int bytePos, int bitOffset)
    {
        checkEmptyAndPosition(bytePos, bitOffset);
        long remainder = accessor.remainderLength(bytePos, bitOffset);
        if (remainder >= 32L)
        {
            return accessor.readInt(bytePos, bitOffset);
        }
        else
        {
            return accessor.readBitsAs4Bytes(bytePos, bitOffset, (int)remainder);
        }
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public int readRemainderInt(long pos)
    {
        checkEmptyAndPosition(pos);
        long remainder = accessor.remainderLength(pos);
        if (remainder >= 32L)
        {
            return accessor.read4Bytes(pos);
        }
        else
        {
            return accessor.readBitsAs4Bytes(pos, (int)remainder);
        }
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public float readRemainderFloat(int bytePos, int bitOffset)
    {
        return Float.intBitsToFloat(readRemainderInt(bytePos, bitOffset));
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public float readRemainderFloat(long pos)
    {
        return Float.intBitsToFloat(readRemainderInt(pos));
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public long readRemainderLong(int bytePos, int bitOffset)
    {
        checkEmptyAndPosition(bytePos, bitOffset);
        long remainder = accessor.remainderLength(bytePos, bitOffset);
        if (remainder >= 64L)
        {
            return accessor.read8Bytes(bytePos, bitOffset);
        }
        else
        {
            return accessor.readBitsAs8Bytes(bytePos, bitOffset, (int)remainder);
        }
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public long readRemainderLong(long pos)
    {
        checkEmptyAndPosition(pos);
        long remainder = accessor.remainderLength(pos);
        if (remainder >= 64L)
        {
            return accessor.read8Bytes(pos);
        }
        else
        {
            return accessor.readBitsAs8Bytes(pos, (int)remainder);
        }
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public double readRemainderDouble(int bytePos, int bitOffset)
    {
        return Double.longBitsToDouble(readRemainderLong(bytePos, bitOffset));
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public double readRemainderDouble(long pos)
    {
        return Double.longBitsToDouble(readRemainderLong(pos));
    }
    
    /**
     * Checks whether bits in special bounds.
     * 
     * @param bits
     *            special bits value
     * @param max
     *            max value inclusive
     * @throws IllegalArgumentException
     *             thrown if out of bounds
     */
    private void checkBitsInSpecialBounds(int bits, int max)
    {
        if (bits < 0 || bits > max)
        {
            throw new IllegalArgumentException("Bits length should be in [0, " + max + "].");
        }
    }
    
    /**
     * Checks whether this block is empty or special position and range from special position to
     * special bits length is in bounds, and the special bits should be in bounds of max bits
     * 
     * @param bytePos
     *            special byte position in bounds
     * @param bitOffset
     *            bit offset of special byte position, [0, 7]
     * @param bits
     *            bits length, in bounds of max bits
     * @param maxBits
     *            max bits
     * @throws IllegalArgumentException
     *             thrown if special position are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    private void checkEmptyAndPositionInBitsBounds(int bytePos, int bitOffset, int bits, int maxBits)
    {
        checkEmpty();
        checkBitsInSpecialBounds(bits, maxBits);
        checkPositionLegal(bytePos, bitOffset);
        if (bits < 0)
        {
            throw new IllegalArgumentException("Bits length should be >= 0.");
        }
        long length = accessor.lengthInBits();
        if (bytePos * 8L + bitOffset + (bits - 1) > length - 1)
        {
            throw new OutOfBoundsException(bytePos * 8L + bitOffset + (bits - 1));
        }
    }
    
    /**
     * Checks whether this block is empty or special position and range from special position to
     * special bits length is in bounds, and the special bits should be in bounds of max bits
     * 
     * @param pos
     *            special position in bounds
     * @param bits
     *            bits length, in bounds of max bits
     * @param maxBits
     *            max bits
     * @throws IllegalArgumentException
     *             thrown if special position are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    private void checkEmptyAndPositionInBitsBounds(long pos, int bits, int maxBits)
    {
        checkEmpty();
        checkBitsInSpecialBounds(bits, maxBits);
        checkPositionLegal(pos);
        if (bits < 0)
        {
            throw new IllegalArgumentException("Bits length should be >= 0.");
        }
        long length = accessor.lengthInBits();
        if (pos + (bits - 1) > length - 1)
        {
            throw new OutOfBoundsException(pos + (bits - 1));
        }
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public byte readBitsAsByte(int bytePos, int bitOffset, int bits)
    {
        checkEmptyAndPositionInBitsBounds(bytePos, bitOffset, bits, 8);
        return accessor.readBitsAsByte(bytePos, bitOffset, bits);
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public byte readBitsAsByte(long pos, int bits)
    {
        checkEmptyAndPositionInBitsBounds(pos, bits, 8);
        return accessor.readBitsAsByte(pos, bits);
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public short readBitsAsShort(int bytePos, int bitOffset, int bits)
    {
        checkEmptyAndPositionInBitsBounds(bytePos, bitOffset, bits, 16);
        return accessor.readBitsAs2Bytes(bytePos, bitOffset, bits);
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public short readBitsAsShort(long pos, int bits)
    {
        checkEmptyAndPositionInBitsBounds(pos, bits, 16);
        return accessor.readBitsAs2Bytes(pos, bits);
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public char readBitsAsChar(int bytePos, int bitOffset, int bits)
    {
        return (char)readBitsAsShort(bytePos, bitOffset, bits);
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public char readBitsAsChar(long pos, int bits)
    {
        return (char)readBitsAsShort(pos, bits);
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public int readBitsAsInt(int bytePos, int bitOffset, int bits)
    {
        checkEmptyAndPositionInBitsBounds(bytePos, bitOffset, bits, 32);
        return accessor.readBitsAs4Bytes(bytePos, bitOffset, bits);
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public int readBitsAsInt(long pos, int bits)
    {
        checkEmptyAndPositionInBitsBounds(pos, bits, 32);
        return accessor.readBitsAs4Bytes(pos, bits);
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public float readBitsAsFloat(int bytePos, int bitOffset, int bits)
    {
        return Float.intBitsToFloat(readBitsAsInt(bytePos, bitOffset, bits));
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public float readBitsAsFloat(long pos, int bits)
    {
        return Float.intBitsToFloat(readBitsAsInt(pos, bits));
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public long readBitsAsLong(int bytePos, int bitOffset, int bits)
    {
        checkEmptyAndPositionInBitsBounds(bytePos, bitOffset, bits, 64);
        return accessor.readBitsAs8Bytes(bytePos, bitOffset, bits);
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public long readBitsAsLong(long pos, int bits)
    {
        checkEmptyAndPositionInBitsBounds(pos, bits, 64);
        return accessor.readBitsAs8Bytes(pos, bits);
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public double readBitsAsDouble(int bytePos, int bitOffset, int bits)
    {
        return Double.longBitsToDouble(readBitsAsLong(bytePos, bitOffset, bits));
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public double readBitsAsDouble(long pos, int bits)
    {
        return Double.longBitsToDouble(readBitsAsLong(pos, bits));
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public void writeBoolean(int bytePos, int bitOffset, boolean value)
    {
        checkEmptyAndPositionInSpecialBounds(bytePos, bitOffset, 1);
        accessor.writeBit(bytePos, bitOffset, value);
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public void writeBoolean(long pos, boolean value)
    {
        checkEmptyAndPositionInSpecialBounds(pos, 1);
        accessor.writeBit(pos, value);
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public void writeByte(int bytePos, int bitOffset, byte value)
    {
        checkEmptyAndPositionInSpecialBounds(bytePos, bitOffset, 8);
        accessor.writeByte(bytePos, bitOffset, value);
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public void writeByte(long pos, byte value)
    {
        checkEmptyAndPositionInSpecialBounds(pos, 8);
        accessor.writeByte(pos, value);
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public void writeShort(int bytePos, int bitOffset, short value)
    {
        checkEmptyAndPositionInSpecialBounds(bytePos, bitOffset, 16);
        accessor.write2Bytes(bytePos, bitOffset, value);
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public void writeShort(long pos, short value)
    {
        checkEmptyAndPositionInSpecialBounds(pos, 16);
        accessor.write2Bytes(pos, value);
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public void writeChar(int bytePos, int bitOffset, char value)
    {
        writeShort(bytePos, bitOffset, (short)value);
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public void writeChar(long pos, char value)
    {
        writeShort(pos, (short)value);
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public void writeInt(int bytePos, int bitOffset, int value)
    {
        checkEmptyAndPositionInSpecialBounds(bytePos, bitOffset, 32);
        accessor.write4Bytes(bytePos, bitOffset, value);
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public void writeInt(long pos, int value)
    {
        checkEmptyAndPositionInSpecialBounds(pos, 32);
        accessor.write4Bytes(pos, value);
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public void writeFloat(int bytePos, int bitOffset, float value)
    {
        writeInt(bytePos, bitOffset, Float.floatToRawIntBits(value));
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public void writeFloat(long pos, float value)
    {
        writeInt(pos, Float.floatToRawIntBits(value));
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public void writeLong(int bytePos, int bitOffset, long value)
    {
        checkEmptyAndPositionInSpecialBounds(bytePos, bitOffset, 64);
        accessor.write8Bytes(bytePos, bitOffset, value);
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public void writeLong(long pos, long value)
    {
        checkEmptyAndPositionInSpecialBounds(pos, 64);
        accessor.write8Bytes(pos, value);
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public void writeDouble(int bytePos, int bitOffset, double value)
    {
        writeLong(bytePos, bitOffset, Double.doubleToRawLongBits(value));
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public void writeDouble(long pos, double value)
    {
        writeLong(pos, Double.doubleToRawLongBits(value));
    }
    
    /**
     * Write bits in order, default parameters are in bounds.
     * 
     * @param bytePos
     *            byte position,
     * @param bitOffset
     *            bit offset of byte position
     * @param value
     *            written value
     * @param maxBits
     *            max written bits [1, 32]
     * @return actual written bits
     */
    private int writeBitsInBounds(int bytePos, int bitOffset, int value, int maxBits)
    {
        long remainder = accessor.remainderLength(bytePos, bitOffset);
        if (remainder >= maxBits)
        {
            accessor.write4BytesInOrder(bytePos, bitOffset, value, maxBits);
            return maxBits;
        }
        else
        {
            int remainderInt = (int)remainder;
            accessor.write4BytesInOrder(bytePos, bitOffset, value, remainderInt);
            return remainderInt;
        }
    }
    
    /**
     * Write bits in order, default parameters are in bounds.
     * 
     * @param pos
     *            position in bits
     * @param value
     *            written value
     * @param maxBits
     *            max written bits [1, 32]
     * @return actual written bits
     */
    private int writeBitsInBounds(long pos, int value, int maxBits)
    {
        long remainder = accessor.remainderLength(pos);
        if (remainder >= maxBits)
        {
            accessor.write4BytesInOrder(pos, value, maxBits);
            return maxBits;
        }
        else
        {
            int remainderInt = (int)remainder;
            accessor.write4BytesInOrder(pos, value, remainderInt);
            return remainderInt;
        }
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public int writeByteInBounds(int bytePos, int bitOffset, byte value)
    {
        checkEmptyAndPosition(bytePos, bitOffset);
        int v = value << 24;
        return writeBitsInBounds(bytePos, bitOffset, v, 8);
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public int writeByteInBounds(long pos, byte value)
    {
        checkEmptyAndPosition(pos);
        int v = value << 24;
        return writeBitsInBounds(pos, v, 8);
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public int writeShortInBounds(int bytePos, int bitOffset, short value)
    {
        checkEmptyAndPosition(bytePos, bitOffset);
        int v = value << 16;
        return writeBitsInBounds(bytePos, bitOffset, v, 16);
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public int writeShortInBounds(long pos, short value)
    {
        checkEmptyAndPosition(pos);
        int v = value << 16;
        return writeBitsInBounds(pos, v, 16);
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public int writeCharInBounds(int bytePos, int bitOffset, char value)
    {
        return writeShortInBounds(bytePos, bitOffset, (short)value);
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public int writeCharInBounds(long pos, char value)
    {
        return writeShortInBounds(pos, (short)value);
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public int writeIntInBounds(int bytePos, int bitOffset, int value)
    {
        checkEmptyAndPosition(bytePos, bitOffset);
        return writeBitsInBounds(bytePos, bitOffset, value, 32);
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public int writeIntInBounds(long pos, int value)
    {
        checkEmptyAndPosition(pos);
        return writeBitsInBounds(pos, value, 32);
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public int writeFloatInBounds(int bytePos, int bitOffset, float value)
    {
        return writeIntInBounds(bytePos, bitOffset, Float.floatToRawIntBits(value));
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public int writeFloatInBounds(long pos, float value)
    {
        return writeIntInBounds(pos, Float.floatToRawIntBits(value));
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public int writeLongInBounds(int bytePos, int bitOffset, long value)
    {
        checkEmptyAndPosition(bytePos, bitOffset);
        long remainder = accessor.remainderLength(bytePos, bitOffset);
        if (remainder >= 64L)
        {
            accessor.write8BytesInOrder(bytePos, bitOffset, value, 64);
            return 64;
        }
        else
        {
            int remainderInt = (int)remainder;
            accessor.write8BytesInOrder(bytePos, bitOffset, value, remainderInt);
            return remainderInt;
        }
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public int writeLongInBounds(long pos, long value)
    {
        checkEmptyAndPosition(pos);
        long remainder = accessor.remainderLength(pos);
        if (remainder >= 64L)
        {
            accessor.write8BytesInOrder(pos, value, 64);
            return 64;
        }
        else
        {
            int remainderInt = (int)remainder;
            accessor.write8BytesInOrder(pos, value, remainderInt);
            return remainderInt;
        }
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public int writeDoubleInBounds(int bytePos, int bitOffset, double value)
    {
        return writeLongInBounds(bytePos, bitOffset, Double.doubleToRawLongBits(value));
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public int writeDoubleInBounds(long pos, double value)
    {
        return writeLongInBounds(pos, Double.doubleToRawLongBits(value));
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public void writeByteBits(int bytePos, int bitOffset, byte value, int bits)
    {
        checkEmptyAndPositionInBitsBounds(bytePos, bitOffset, bits, 8);
        accessor.write4BytesInOrder(bytePos, bitOffset, (int)value << 24, bits);
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public void writeByteBits(long pos, byte value, int bits)
    {
        checkEmptyAndPositionInBitsBounds(pos, bits, 8);
        accessor.write4BytesInOrder(pos, (int)value << 24, bits);
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public void writeShortBits(int bytePos, int bitOffset, short value, int bits)
    {
        checkEmptyAndPositionInBitsBounds(bytePos, bitOffset, bits, 16);
        accessor.write4BytesInOrder(bytePos, bitOffset, (int)value << 16, bits);
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public void writeShortBits(long pos, short value, int bits)
    {
        checkEmptyAndPositionInBitsBounds(pos, bits, 16);
        accessor.write4BytesInOrder(pos, (int)value << 16, bits);
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public void writeCharBits(int bytePos, int bitOffset, char value, int bits)
    {
        writeShortBits(bytePos, bitOffset, (short)value, bits);
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public void writeCharBits(long pos, char value, int bits)
    {
        writeShortBits(pos, (short)value, bits);
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public void writeIntBits(int bytePos, int bitOffset, int value, int bits)
    {
        checkEmptyAndPositionInBitsBounds(bytePos, bitOffset, bits, 32);
        accessor.write4BytesInOrder(bytePos, bitOffset, value, bits);
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public void writeIntBits(long pos, int value, int bits)
    {
        checkEmptyAndPositionInBitsBounds(pos, bits, 32);
        accessor.write4BytesInOrder(pos, value, bits);
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public void writeFloatBits(int bytePos, int bitOffset, float value, int bits)
    {
        writeIntBits(bytePos, bitOffset, Float.floatToRawIntBits(value), bits);
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public void writeFloatBits(long pos, float value, int bits)
    {
        writeIntBits(pos, Float.floatToRawIntBits(value), bits);
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public void writeLongBits(int bytePos, int bitOffset, long value, int bits)
    {
        checkEmptyAndPositionInBitsBounds(bytePos, bitOffset, bits, 64);
        accessor.write8BytesInOrder(bytePos, bitOffset, value, bits);
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public void writeLongBits(long pos, long value, int bits)
    {
        checkEmptyAndPositionInBitsBounds(pos, bits, 64);
        accessor.write8BytesInOrder(pos, value, bits);
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public void writeDoubleBits(int bytePos, int bitOffset, double value, int bits)
    {
        writeLongBits(bytePos, bitOffset, Double.doubleToRawLongBits(value), bits);
    }
    
    /**
     * @throws IllegalArgumentException
     *             thrown if special parameters are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    @Override
    public void writeDoubleBits(long pos, double value, int bits)
    {
        writeLongBits(pos, Double.doubleToRawLongBits(value), bits);
    }
    
    /**
     * And operation between this data block and target data block like:
     * 
     * <pre>
     * block = block &amp; target;
     * </pre>
     * 
     * The operation length is indicated by shorter one of two block.
     * 
     * @param target
     *            target block, not null
     * @throws NullPointerException
     *             thrown if target block is null
     */
    public void and(DataBlockOld target)
    {
        if (!isEmpty())
        {
            accessor.and(target.accessor);
        }
    }
    
    /**
     * Or operation between this data block and target block like:
     * 
     * <pre>
     * block = block | target;
     * </pre>
     * 
     * The operation length is indicated by shorter one of two block.
     * 
     * @param target
     *            target accessor, not null
     * @throws NullPointerException
     *             thrown if target block is null
     */
    public void or(DataBlockOld target)
    {
        if (!isEmpty())
        {
            accessor.or(target.accessor);
        }
    }
    
    /**
     * Xor operation between this data block and target block like:
     * 
     * <pre>
     * block = block &circ; target;
     * </pre>
     * 
     * The operation length is indicated by shorter one of two block.
     * 
     * @param target
     *            target accessor, not null
     * @throws NullPointerException
     *             thrown if target block is null
     */
    public void xor(DataBlockOld target)
    {
        if (!isEmpty())
        {
            accessor.xor(target.accessor);
        }
    }
    
    /**
     * Not operation for this data block like:
     * 
     * <pre>
     * block = ^block;
     * </pre>
     * 
     */
    public void not()
    {
        if (!isEmpty())
        {
            accessor.not();
        }
    }
    
    /**
     * Reverses this data block.
     */
    public void reverse()
    {
        if (!isEmpty())
        {
            accessor.reverse();
        }
    }
    
    /**
     * Checks whether special number of shifted bits is illegal.
     * 
     * @param bits
     *            special number of shifted bits, [1, length - 1]
     * @throws IllegalArgumentException
     *             thrown if number of shifted bits is illegal
     */
    private void checkShiftedBits(long bits)
    {
        if (bits < 1 || bits >= lengthInBits())
        {
            throw new IllegalArgumentException(
                    "Special number of shifted bits should be in [1, length - 1].");
        }
    }
    
    /**
     * Shifts left bits of special number like:
     * 
     * <pre>
     * block = block &lt;&lt; bits;
     * </pre>
     * 
     * The special number should be in [1, length - 1].
     * 
     * @param bits
     *            special number of shifted bits, [1, length - 1]
     * @throws IllegalArgumentException
     *             thrown if number of shifted bits is illegal
     */
    public void shiftLeft(long bits)
    {
        if (!isEmpty())
        {
            checkShiftedBits(bits);
            accessor.logicalLeft(bits);
        }
    }
    
    /**
     * Logical shift right bits of special number like:
     * 
     * <pre>
     * block = block &gt;&gt;&gt; bits;
     * </pre>
     * 
     * The special number should be in [1, length - 1].
     * 
     * @param bits
     *            special number of shifted bits, [1, length - 1]
     * @throws IllegalArgumentException
     *             thrown if number of shifted bits is illegal
     */
    public void logicalRight(long bits)
    {
        if (!isEmpty())
        {
            checkShiftedBits(bits);
            accessor.logicalRight(bits);
        }
    }
    
    /**
     * Arithmetic shift right bits of special number like:
     * 
     * <pre>
     * block = block &gt;&gt; bits;
     * </pre>
     * 
     * The special number should be in [1, length - 1].
     * 
     * @param bits
     *            special number of shifted bits, [1, length - 1]
     * @throws IllegalArgumentException
     *             thrown if number of shifted bits is illegal
     */
    public void arithmeticRight(long bits)
    {
        if (!isEmpty())
        {
            checkShiftedBits(bits);
            accessor.arithmeticRight(bits);
        }
    }
    
    /**
     * Rotates left bits of special number like:
     * 
     * <pre>
     * ROL AL, bits
     * </pre>
     * 
     * The special number should be in [1, length - 1].
     * 
     * @param bits
     *            number of rotated bits
     * @throws IllegalArgumentException
     *             thrown if number of shifted bits is illegal
     */
    public void rotateLeft(long bits)
    {
        if (!isEmpty())
        {
            checkShiftedBits(bits);
            accessor.rotateLeft(bits);
        }
    }
    
    /**
     * Rotates right bits of special number like:
     * 
     * <pre>
     * ROR AL, bits
     * </pre>
     * 
     * The special number should be in [1, length - 1].
     * 
     * @param bits
     *            number of rotated bits
     * @throws IllegalArgumentException
     *             thrown if number of shifted bits is illegal
     */
    public void rotateRight(long bits)
    {
        if (!isEmpty())
        {
            checkShiftedBits(bits);
            accessor.rotateRight(bits);
        }
    }
    
    /**
     * Converts this data block to a bit, if length of this block is more than or equal to 1 bit, it
     * will return last bit(as lowest order), true is 1, false is 0.
     * <p>
     * If length of this block is 0, return false.
     * 
     * @return bit value, true is 1, false is 0
     */
    public boolean toBoolean()
    {
        if (isEmpty())
        {
            return false;
        }
        else
        {
            return accessor.readBit(accessor.lengthInBits() - 1);
        }
    }
    
    /**
     * Converts this data block to a byte, if length of this block is more than 8 bits, high orders
     * will be truncated, if length of this block is lees than 8 bits, 0 will be filled in the head.
     * <p>
     * If length of this block is 0, return 0.
     * 
     * @return byte value
     */
    public byte toByte()
    {
        if (isEmpty())
        {
            return 0;
        }
        else
        {
            long length = accessor.lengthInBits();
            if (length >= 8)
            {
                return accessor.readByte(length - 8);
            }
            else
            {
                return accessor.readBitsAsByte(0, (int)length);
            }
        }
    }
    
    /**
     * Converts this data block to a short value, if length of this block is more than 16 bits, high
     * orders will be truncated, if length of this block is lees than 16 bits, 0 will be filled in
     * the head.
     * <p>
     * If length of this block is 0, return 0.
     * 
     * @return short value
     */
    public short toShort()
    {
        if (isEmpty())
        {
            return 0;
        }
        else
        {
            long length = accessor.lengthInBits();
            if (length >= 16)
            {
                return accessor.read2Bytes(length - 16);
            }
            else
            {
                return accessor.readBitsAs2Bytes(0, (int)length);
            }
        }
    }
    
    /**
     * Converts this data block to a char value, if length of this block is more than 16 bits, high
     * orders will be truncated, if length of this block is lees than 16 bits, 0 will be filled in
     * the head.
     * <p>
     * If length of this block is 0, return 0.
     * 
     * @return char value
     */
    public char toChar()
    {
        return (char)toShort();
    }
    
    /**
     * Converts this data block to a int value, if length of this block is more than 32 bits, high
     * orders will be truncated, if length of this block is lees than 32 bits, 0 will be filled in
     * the head.
     * <p>
     * If length of this block is 0, return 0.
     * 
     * @return int value
     */
    public int toInt()
    {
        if (isEmpty())
        {
            return 0;
        }
        else
        {
            long length = accessor.lengthInBits();
            if (length >= 32)
            {
                return accessor.read4Bytes(length - 32);
            }
            else
            {
                return accessor.readBitsAs4Bytes(0, (int)length);
            }
        }
    }
    
    /**
     * Converts this data block to a float value, if length of this block is more than 32 bits, high
     * orders will be truncated, if length of this block is lees than 32 bits, 0 will be filled in
     * the head.
     * <p>
     * If length of this block is 0, return 0.
     * 
     * @return float value
     */
    public float toFloat()
    {
        return Float.intBitsToFloat(toInt());
    }
    
    /**
     * Converts this data block to a long value, if length of this block is more than 64 bits, high
     * orders will be truncated, if length of this block is lees than 64 bits, 0 will be filled in
     * the head.
     * <p>
     * If length of this block is 0, return 0.
     * 
     * @return long value
     */
    public long toLong()
    {
        if (isEmpty())
        {
            return 0;
        }
        else
        {
            long length = accessor.lengthInBits();
            if (length >= 64)
            {
                return accessor.read8Bytes(length - 64);
            }
            else
            {
                return accessor.readBitsAs8Bytes(0, (int)length);
            }
        }
    }
    
    /**
     * Converts this data block to a double value, if length of this block is more than 64 bits,
     * high orders will be truncated, if length of this block is lees than 64 bits, 0 will be filled
     * in the head.
     * <p>
     * If length of this block is 0, return 0.
     * 
     * @return double value
     */
    public double toDouble()
    {
        return Double.longBitsToDouble(toLong());
    }
    
    /**
     * 
     * 
     * @return
     */
    public boolean[] toBooleanArray()
    {
        return null;
    }
    
    public byte[] toByteArray()
    {
        return null;
    }
    
    public short[] toShortArray()
    {
        return null;
    }
    
    public char[] toCharArray()
    {
        return null;
    }
    
    public int[] toIntArray()
    {
        return null;
    }
    
    public float[] toFloatArray()
    {
        return null;
    }
    
    public long[] toLongArray()
    {
        return null;
    }
    
    public double[] toDoubleArray()
    {
        return null;
    }
    
    /**
     * Returns min value of two number.
     */
    private long min(long a, long b)
    {
        return a < b ? a : b;
    }
    
    /**
     * Checks whether index and bit offset are illegal or out of bounds.
     * 
     * @param arrayLength
     *            length of checked array
     * @param index
     *            index of array, [0, arrayLength - 1]
     * @param maxElementBits
     *            max bits number of checked array, should be (1, 8, 16, 32, 64)
     * @param bitOffset
     *            bit offset, [0, maxElementBits - 1]
     * @throws IllegalArgumentException
     *             thrown if index and bit offset are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    private void checkIndexIllegalAndBounds(int arrayLength, int index, int maxElementBits,
            int bitOffset)
    {
        if (arrayLength < 0
                || (maxElementBits != 1 && maxElementBits != 8 && maxElementBits != 16
                        && maxElementBits != 32 && maxElementBits != 64) || index < 0
                || bitOffset < 0 || bitOffset > maxElementBits - 1)
        {
            throw new IllegalArgumentException(
                    "Special index should be >= 0, bit offset shoul be in [0, maxElementBits - 1].");
        }
        if (index > arrayLength - 1)
        {
            throw new OutOfBoundsException(index);
        }
    }
    
    /**
     * Puts data from this block to special boolean array, each bit is an element. Array will be put
     * from 0 index, end when reach to end of array or end of block.
     * 
     * @param array
     *            special array, not null
     * @throws NullPointerException
     *             thrown if special array is null
     */
    public void toBooleanArray(boolean[] array)
    {
        if (!isEmpty())
        {
            accessor.toBooleanArray(array, 0);
        }
    }
    
    /**
     * Puts data from this block to special boolean array, each bit is an element. Array will be put
     * from special index, end when reach to end of array or end of block.
     * 
     * @param array
     *            special array, not null
     * @param startIndex
     *            start index of array
     * @throws NullPointerException
     *             thrown if special array is null
     * @throws IllegalArgumentException
     *             thrown if start index is illegal
     * @throws OutOfBoundsException
     *             thrown if start index out of bounds
     */
    public void toBooleanArray(boolean[] array, int startIndex)
    {
        if (isEmpty())
        {
            return;
        }
        array = Objects.requireNonNull(array);
        checkIndexIllegalAndBounds(array.length, startIndex, 1, 0);
        long length = min(array.length - startIndex, accessor.lengthInBits());
        int restBits = (int)(length % 8L);
        for (int i = 0, j = startIndex; i < (int)(length - restBits); i += 8)
        {
            int v = accessor.readBitsAsByte(i, 8);
            array[j] = (v & 1) == 1 ? true : false;
            v >>>= 1;
            j++;
            array[j] = (v & 1) == 1 ? true : false;
            v >>>= 1;
            j++;
            array[j] = (v & 1) == 1 ? true : false;
            v >>>= 1;
            j++;
            array[j] = (v & 1) == 1 ? true : false;
            v >>>= 1;
            j++;
            array[j] = (v & 1) == 1 ? true : false;
            v >>>= 1;
            j++;
            array[j] = (v & 1) == 1 ? true : false;
            v >>>= 1;
            j++;
            array[j] = (v & 1) == 1 ? true : false;
            v >>>= 1;
            j++;
            array[j] = (v & 1) == 1 ? true : false;
            j++;
        }
        if (restBits > 0)
        {
            for (int i = (int)(length - restBits); i < length; i++)
            {
                array[i] = accessor.readBit(i);
            }
        }
    }
    
    /**
     * Puts data from this block to special byte array. The array is seen as a continuous storage
     * space, the data are put in bits and independent with type of array. Array putting will be end
     * when reach to end of array or end of block.
     * 
     * @param array
     *            special array, not null
     * @throws NullPointerException
     *             thrown if special array is null
     */
    public void toByteArray(byte[] array)
    {
        array = Objects.requireNonNull(array);
        long length = min(array.length * 8L, accessor.lengthInBits());
        int restBits = (int)(length % 8L);
        int byteCount = (int)(length / 8L);
        for (int i = 0; i < byteCount; i++)
        {
            array[i] = accessor.readByte(i, 0);
        }
        if (restBits > 0)
        {
            int last = (accessor.readBitsAsByte(length - restBits, 8) << (8 - restBits)) & 0x000000ff;
            int org = array[byteCount];
            org <<= restBits;
            org &= 0x000000ff;
            org >>>= restBits;
            array[byteCount] = (byte)(last | org);
        }
    }
    
    /**
     * Puts data from this block to special byte array from special position which indicated by
     * special index and bit offset. The array is seen as a continuous storage space, the data are
     * put in bits and independent with type of array. Array putting will be end when reach to end
     * of array or end of block.
     * 
     * @param array
     *            special array, not null
     * @param startIndex
     *            start index,[0, array length - 1]
     * @param bitOffset
     *            bit offset of start index, [0, 7]
     * @throws NullPointerException
     *             thrown if special array is null
     * @throws IllegalArgumentException
     *             thrown if index and bit offset are illegal
     * @throws OutOfBoundsException
     *             thrown if out of bounds
     */
    public void toByteArray(byte[] array, int startIndex, int bitOffset)
    {
        array = Objects.requireNonNull(array);
        checkIndexIllegalAndBounds(array.length, startIndex, 8, bitOffset);
        long length = min(array.length * 8L - (startIndex * 8L + bitOffset + 1),
                accessor.lengthInBits());
        int restBits = (int)(length % 8L);
        int byteCount = (int)(length / 8L);
        for (int i = 0, j = startIndex; i < byteCount; i++, j++)
        {
            byte b = accessor.readByte(i, 0);
            array[i] = accessor.readByte(i, 0);
        }
        if (restBits > 0)
        {
            int last = (accessor.readBitsAsByte(length - restBits, 8) << (8 - restBits)) & 0x000000ff;
            int org = array[byteCount];
            org <<= restBits;
            org &= 0x000000ff;
            org >>>= restBits;
            array[byteCount] = (byte)(last | org);
        }
    }
    
    public void toShortArray(short[] array)
    {
        
    }
    
    public void toShortArray(short[] array, int startIndex, int bitOffset)
    {
        
    }
    
    public void toCharArray(char[] array)
    {
        
    }
    
    public void toCharArray(char[] array, int startIndex, int bitOffset)
    {
        
    }
    
    public void toIntArray(int[] array)
    {
        
    }
    
    public void toIntArray(int[] array, int startIndex, int bitOffset)
    {
        
    }
    
    public void toFloatArray(float[] array)
    {
        
    }
    
    public void toFloatArray(float[] array, int startIndex, int bitOffset)
    {
        
    }
    
    public void toLongArray(long[] array)
    {
        
    }
    
    public void toLongArray(long[] array, int startIndex, int bitOffset)
    {
        
    }
    
    public void toDoubleArray(double[] array)
    {
        
    }
    
    public void toDoubleArray(double[] array, int startIndex, int bitOffset)
    {
        
    }
    
    public void toArray(Object array)
    {
        
    }
    
    public void toArray(Object array, int startIndex, int bitOffset)
    {
        
    }
    
    public String toText()
    {
        return null;
    }
    
    @Override
    public String toString()
    {
        // TODO Auto-generated method stub
        return super.toString();
    }
}
