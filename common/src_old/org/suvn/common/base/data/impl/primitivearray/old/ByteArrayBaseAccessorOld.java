package org.suvn.common.base.data.impl.primitivearray.old;

import org.suvn.common.annotation.Base;
import org.suvn.common.base.data.BaseAccessor;
import org.suvn.common.base.data.impl.primitivearray.PrimitiveArrayBaseAccessorFactoryImpl;

/**
 * Common implementation of {@link BaseAccessor}, more detail for seeing {@link BaseAccessor}. Note
 * this acessor cannot be empty.
 * <p>
 * This implementation use byte array to store and manipulate data, randomly. This is one of
 * implementations which are implemented by primitive array, to create an instance of these in
 * corresponding case, use {@linkplain PrimitiveArrayBaseAccessorFactoryImpl}. For more detail see
 * {@linkplain PrimitiveArrayBaseAccessorFactoryImpl}.
 * <p>
 * This class don't check parameters passed in methods, <b>use higher class or interface like
 * {@link DataBlockOld} rather than this class directly.</b>
 * <p>
 * This class is serializable and cloneable.
 * 
 * @author Fred Suvn
 * @version 0.0.0, 2015-03-05 14:08:03
 * @see BaseAccessor
 */
@Base
public final class ByteArrayBaseAccessorOld implements BaseAccessor
{
    /**
     * Serial ID.
     */
    private static final long serialVersionUID = 0L;
    
    /**
     * Byte array which is used to stored data, the data stored in all or part of this array.
     */
    private final byte[] array;
    
    /**
     * Start index of array.
     */
    private final int startIndex;
    
    /**
     * Bit offset of start index, [0, 7].
     */
    private final int startBitOffset;
    
    /**
     * End index of array.
     */
    private final int endIndex;
    
    /**
     * Bit offset of end index, [0, 7].
     */
    private final int endBitOffset;
    
    /**
     * Translated byte array index, from position relative to this accessor to index relative to
     * source byte array.
     * <p>
     * Sometimes to be a spare space.
     */
    private int tempIndex;
    
    /**
     * Bit offset of {@linkplain #tempIndex}.
     * <p>
     * Sometimes to be a spare space.
     */
    private int tempBitOffset;
    
    /**
     * Constructs an instance with a byte array.
     * 
     * @param array
     *            byte array, not null
     */
    ByteArrayBaseAccessorOld(byte[] array)
    {
        this(array, 0, 0, array.length - 1, 7);
    }
    
    /**
     * Constructs an instance with a byte array, start index and bit offset of start index.
     * 
     * @param array
     *            special array, not null
     * @param startIndex
     *            start index, [0, array length - 1]
     * @param startBitOffset
     *            bit offset of start index, [0, 7]
     */
    ByteArrayBaseAccessorOld(byte[] array, int startIndex, int startBitOffset)
    {
        this(array, startIndex, startBitOffset, array.length - 1, 7);
    }
    
    /**
     * Constructs an instance with array, start index, bit offset of start index, length in bytes,
     * bit offset of length.
     * 
     * @param array
     *            special array, not null
     * @param startIndex
     *            start index, [0, array length - 1]
     * @param startBitOffset
     *            bit offset of start index, [0, 7]
     * @param endIndex
     *            end index, [0, array length - 1]
     * @param endBitOffset
     *            bit offset of end index, [0, 7]
     */
    ByteArrayBaseAccessorOld(byte[] array, int startIndex, int startBitOffset, int endIndex,
            int endBitOffset)
    {
        this.array = array;
        this.startIndex = startIndex;
        this.startBitOffset = startBitOffset;
        this.endIndex = endIndex;
        this.endBitOffset = endBitOffset;
    }
    
    @Override
    public boolean isEmpty()
    {
        return false;
    }
    
    /**
     * Buffer of length in bits.
     */
    private long lengthInBits = -1L;
    
    @Override
    public long lengthInBits()
    {
        if (lengthInBits == -1)
        {
            long startPos = this.startIndex * 8L + this.startBitOffset;
            long endPos = this.endIndex * 8L + this.endBitOffset;
            return endPos - startPos + 1;
        }
        else
        {
            return lengthInBits;
        }
    }
    
    @Override
    public int lengthInBytes()
    {
        return (int)(lengthInBits() / 8L);
    }
    
    @Override
    public long remainderLength(int bytePos, int bitOffset)
    {
        return remainderLength(bytePos * 8L + bitOffset);
    }
    
    @Override
    public long remainderLength(long pos)
    {
        return lengthInBits() - pos;
    }
    
    /**
     * Translates special position relative to this accessor to the position relative to the byte
     * array. Results value will be put on {@linkplain #tempIndex} and {@linkplain #tempBitOffset}.
     * 
     * @param bytePos
     *            special byte position relative to this accessor
     * @param bitOffset
     *            bit offset of special byte position
     */
    private void transPos(int bytePos, int bitOffset)
    {
        int actualIndex = this.startIndex + bytePos;
        int actualBitOffset = this.startBitOffset + bitOffset;
        if (actualBitOffset > 7)
        {
            actualIndex++;
            actualBitOffset -= 8;
        }
        this.tempIndex = actualIndex;
        this.tempBitOffset = actualBitOffset;
    }
    
    /**
     * Table show 1 at each bit.
     */
    private static final int[] BIT_SETTING_TABLE = {0x00000080, 0x00000040, 0x00000020, 0x00000010,
            0x00000008, 0x00000004, 0x00000002, 0x00000001};
    
    @Override
    public boolean readBit(int bytePos, int bitOffset)
    {
        transPos(bytePos, bitOffset);
        int b = array[this.tempIndex];
        return (b & BIT_SETTING_TABLE[this.tempBitOffset]) > 0 ? true : false;
    }
    
    @Override
    public boolean readBit(long pos)
    {
        return readBit((int)(pos / 8L), (int)(pos % 8L));
    }
    
    @Override
    public void writeBit(int bytePos, int bitOffset, boolean value)
    {
        boolean b = readBit(bytePos, bitOffset);
        if (b == value)
        {
            return;
        }
        transPos(bytePos, bitOffset);
        if (b)
        {
            array[this.tempIndex] &= ~BIT_SETTING_TABLE[this.tempBitOffset];
        }
        else
        {
            array[this.tempIndex] |= BIT_SETTING_TABLE[this.tempBitOffset];
        }
    }
    
    @Override
    public void writeBit(long pos, boolean value)
    {
        writeBit((int)(pos / 8L), (int)(pos % 8L), value);
    }
    
    /**
     * Table used for set 0 on special bits.
     */
    private static final int[] AND_TABLE = {0x000000ff, 0x0000007f, 0x0000003f, 0x0000001f,
            0x0000000f, 0x00000007, 0x00000003, 0x00000001, 0x00000000};
    
    /**
     * Reads a byte as an int value at special position (relative to this accessor rather than the
     * byte array), the high-24 orders will be filled 0.
     * 
     * @param bytePos
     *            special byte position
     * @param bitOffset
     *            bit offset of special byte position
     * @return a byte as an int value at special position
     */
    private int read(int bytePos, int bitOffset)
    {
        transPos(bytePos, bitOffset);
        if (this.tempBitOffset == 0)
        {
            return array[this.tempIndex];
        }
        int b1 = array[this.tempIndex] & AND_TABLE[this.tempBitOffset];
        int b2 = array[this.tempIndex + 1] & 0x000000ff;
        return (b1 << this.tempBitOffset) | (b2 >>> (8 - bitOffset));
    }
    
    /**
     * Reads 2-bytes as an int value at special position (relative to this accessor rather than the
     * byte array), the high-16 orders will be filled 0.
     * 
     * @param bytePos
     *            special byte position
     * @param bitOffset
     *            bit offset of special byte position
     * @return a 2-bytes as an int value at special position
     */
    private int read2(int bytePos, int bitOffset)
    {
        transPos(bytePos, bitOffset);
        int b1 = array[this.tempIndex];
        int b2 = array[this.tempIndex + 1] & 0x000000ff;
        if (this.tempBitOffset == 0)
        {
            return (b1 << 8) | b2;
        }
        b1 &= AND_TABLE[this.tempBitOffset];
        int b3 = array[this.tempIndex + 2] & 0x000000ff;
        b3 >>>= 8 - this.tempBitOffset;
        b2 <<= this.tempBitOffset;
        b1 <<= 8 + this.tempBitOffset;
        return b1 | b2 | b3;
    }
    
    /**
     * Reads 4-bytes as an int value at special position (relative to this accessor rather than the
     * byte array).
     * 
     * @param bytePos
     *            special byte position
     * @param bitOffset
     *            bit offset of special byte position
     * @return 4-bytes as an int value at special position
     */
    private int read4(int bytePos, int bitOffset)
    {
        transPos(bytePos, bitOffset);
        int b1 = array[this.tempIndex];
        int b2 = array[this.tempIndex + 1] & 0x000000ff;
        int b3 = array[this.tempIndex + 2] & 0x000000ff;
        int b4 = array[this.tempIndex + 3] & 0x000000ff;
        if (this.tempBitOffset == 0)
        {
            return (b1 << 24) | (b2 << 16) | (b3 << 8) | b4;
        }
        b1 &= AND_TABLE[this.tempBitOffset];
        int b5 = array[this.tempIndex + 4] & 0x000000ff;
        b5 >>>= 8 - this.tempBitOffset;
        b4 <<= this.tempBitOffset;
        b3 <<= 8 + this.tempBitOffset;
        b2 <<= 16 + this.tempBitOffset;
        b1 <<= 24 + this.tempBitOffset;
        return b1 | b2 | b3 | b4 | b5;
    }
    
    @Override
    public byte readByte(int bytePos, int bitOffset)
    {
        return (byte)read(bytePos, bitOffset);
    }
    
    @Override
    public byte readByte(long pos)
    {
        return readByte((int)(pos / 8L), (int)(pos % 8L));
    }
    
    @Override
    public short read2Bytes(int bytePos, int bitOffset)
    {
        return (short)read2(bytePos, bitOffset);
    }
    
    @Override
    public short read2Bytes(long pos)
    {
        return read2Bytes((int)(pos / 8L), (int)(pos % 8L));
    }
    
    @Override
    public int readInt(int bytePos, int bitOffset)
    {
        return read4(bytePos, bitOffset);
    }
    
    @Override
    public int read4Bytes(long pos)
    {
        return readInt((int)(pos / 8L), (int)(pos % 8L));
    }
    
    @Override
    public long read8Bytes(int bytePos, int bitOffset)
    {
        long l1 = read4(bytePos, bitOffset) & 0x00000000ffffffffL;
        long l2 = read4(bytePos + 4, bitOffset) & 0x00000000ffffffffL;
        return (l1 << 32) | l2;
    }
    
    @Override
    public long read8Bytes(long pos)
    {
        return read8Bytes((int)(pos / 8L), (int)(pos % 8L));
    }
    
    /**
     * Reads remainder bits as an int at special position. If remainder number of bits is more than
     * or equal to 32, this method will equal to {@linkplain #read4(int, int)}, else this method
     * will read remainder bit and align them to right by filling 0 in high orders.
     * 
     * @param bytePos
     *            special byte position
     * @param bitOffset
     *            bit offset of special byte position
     * @return remainder bits as an int at special position
     */
    private int readRemainderBits(int bytePos, int bitOffset)
    {
        long lRemainderLength = remainderLength(bytePos, bitOffset);
        if (lRemainderLength >= 32)
        {
            return read4(bytePos, bitOffset);
        }
        else if (lRemainderLength == 24)
        {
            int i1 = read2(bytePos, bitOffset);
            int i2 = read(bytePos + 2, bitOffset);
            return (i1 << 8) | i2;
        }
        else if (lRemainderLength == 16)
        {
            return read2(bytePos, bitOffset);
        }
        else if (lRemainderLength == 8)
        {
            return read(bytePos, bitOffset);
        }
        else
        {
            transPos(bytePos, bitOffset);
            int remainderLength = (int)lRemainderLength;
            int bIndex = this.tempIndex + (remainderLength - 1) / 8;
            int bOffset = this.tempBitOffset + (remainderLength - 1) % 8;
            if (bOffset > 7)
            {
                bIndex++;
                bOffset -= 8;
            }
            if (bIndex - this.tempIndex == 4)
            {
                int b1 = array[this.tempIndex] & AND_TABLE[this.tempBitOffset];
                int b2 = array[this.tempIndex + 1] & 0x000000ff;
                int b3 = array[this.tempIndex + 2] & 0x000000ff;
                int b4 = array[this.tempIndex + 3] & 0x000000ff;
                int b5 = array[this.tempIndex + 4] & 0x000000ff;
                b5 >>>= 8 - (bOffset + 1);
                b4 <<= bOffset + 1;
                b3 <<= bOffset + 1 + 8;
                b2 <<= bOffset + 1 + 16;
                b1 <<= bOffset + 1 + 24;
                return b1 | b2 | b3 | b4 | b5;
            }
            else if (bIndex - this.tempIndex == 3)
            {
                int b1 = array[this.tempIndex] & AND_TABLE[this.tempBitOffset];
                int b2 = array[this.tempIndex + 1] & 0x000000ff;
                int b3 = array[this.tempIndex + 2] & 0x000000ff;
                int b4 = array[this.tempIndex + 3] & 0x000000ff;
                b4 >>>= 8 - (bOffset + 1);
                b3 <<= bOffset + 1;
                b2 <<= bOffset + 1 + 8;
                b1 <<= bOffset + 1 + 16;
                return b1 | b2 | b3 | b4;
            }
            else if (bIndex - this.tempIndex == 2)
            {
                int b1 = array[this.tempIndex] & AND_TABLE[this.tempBitOffset];
                int b2 = array[this.tempIndex + 1] & 0x000000ff;
                int b3 = array[this.tempIndex + 2] & 0x000000ff;
                b3 >>>= 8 - (bOffset + 1);
                b2 <<= bOffset + 1;
                b1 <<= bOffset + 1 + 8;
                return b1 | b2 | b3;
            }
            else if (bIndex - this.tempIndex == 1)
            {
                int b1 = array[this.tempIndex] & AND_TABLE[this.tempBitOffset];
                int b2 = array[this.tempIndex + 1] & 0x000000ff;
                b2 >>>= 8 - (bOffset + 1);
                b1 <<= bOffset + 1;
                return b1 | b2;
            }
            else
            {
                int b = array[this.tempIndex] & AND_TABLE[this.tempBitOffset];
                b >>>= 8 - (bOffset + 1);
                return b;
            }
        }
    }
    
    /**
     * Reads bits of special number as an int value at special position (relative to this accessor
     * rather than the byte array), the high-non-filled orders will be filled 0. The number of
     * special bits should be positive and in [1, 32].
     * 
     * @param bytePos
     *            special byte position
     * @param bitOffset
     *            bit offset of special byte position
     * @param bitsNum
     *            special read number, [1, 32]
     * @return bits of special number as an int value at special position
     */
    private int readBits(int bytePos, int bitOffset, int bitsNum)
    {
        long lRemainderLength = remainderLength(bytePos, bitOffset);
        if (lRemainderLength >= 32)
        {
            return read4(bytePos, bitOffset) >>> (32 - bitsNum);
        }
        else
        {
            int remainderLength = (int)lRemainderLength;
            return readRemainderBits(bytePos, bitOffset) >>> (remainderLength - bitsNum);
        }
    }
    
    @Override
    public byte readBitsAsByte(int bytePos, int bitOffset, int bitsNum)
    {
        return (byte)readBits(bytePos, bitOffset, bitsNum);
    }
    
    @Override
    public byte readBitsAsByte(long pos, int bitsNum)
    {
        return readBitsAsByte((int)(pos / 8L), (int)(pos % 8L), bitsNum);
    }
    
    @Override
    public short readBitsAs2Bytes(int bytePos, int bitOffset, int bitsNum)
    {
        return (short)readBits(bytePos, bitOffset, bitsNum);
    }
    
    @Override
    public short readBitsAs2Bytes(long pos, int bitsNum)
    {
        return readBitsAs2Bytes((int)(pos / 8L), (int)(pos % 8L), bitsNum);
    }
    
    @Override
    public int readBitsAs4Bytes(int bytePos, int bitOffset, int bitsNum)
    {
        return readBits(bytePos, bitOffset, bitsNum);
    }
    
    @Override
    public int readBitsAs4Bytes(long pos, int bitsNum)
    {
        return readBitsAs4Bytes((int)(pos / 8L), (int)(pos % 8L), bitsNum);
    }
    
    @Override
    public long readBitsAs8Bytes(int bytePos, int bitOffset, int bitsNum)
    {
        if (bitsNum <= 32)
        {
            return readBits(bytePos, bitOffset, bitsNum);
        }
        else
        {
            long l1 = read4(bytePos, bitOffset) & 0x00000000ffffffffL;
            long l2 = readBits(bytePos + 4, bitOffset, bitsNum - 32);
            return (l1 << (bitsNum - 32)) | l2;
        }
    }
    
    @Override
    public long readBitsAs8Bytes(long pos, int bitsNum)
    {
        return readBitsAs8Bytes((int)(pos / 8L), (int)(pos % 8L), bitsNum);
    }
    
    @Override
    public void writeByte(int bytePos, int bitOffset, byte value)
    {
        transPos(bytePos, bitOffset);
        if (this.tempBitOffset == 0)
        {
            array[this.tempIndex] = value;
        }
        int b1 = (array[this.tempIndex] & ~AND_TABLE[this.tempBitOffset]) & 0x000000ff;
        int b2 = array[this.tempIndex + 1] & AND_TABLE[this.tempBitOffset];
        int v = value & 0x000000ff;
        array[this.tempIndex] = (byte)(b1 | (v >>> (this.tempBitOffset)));
        array[this.tempIndex + 1] = (byte)(b2 | (v << (8 - this.tempBitOffset)));
    }
    
    @Override
    public void writeByte(long pos, byte value)
    {
        writeByte((int)(pos / 8L), (int)(pos % 8L), value);
    }
    
    @Override
    public void write2Bytes(int bytePos, int bitOffset, short value)
    {
        transPos(bytePos, bitOffset);
        if (this.tempBitOffset == 0)
        {
            array[this.tempIndex] = (byte)(value >>> 8);
            array[this.tempIndex + 1] = (byte)value;
        }
        int b1 = (array[this.tempIndex] & ~AND_TABLE[this.tempBitOffset]) & 0x000000ff;
        int b3 = array[this.tempIndex + 2] & AND_TABLE[this.tempBitOffset];
        int v = value & 0x0000ffff;
        array[this.tempIndex] = (byte)(b1 | (v >>> (this.tempBitOffset + 8)));
        array[this.tempIndex + 1] = (byte)(v >>> (this.tempBitOffset));
        array[this.tempIndex + 2] = (byte)(b3 | (v << (8 - this.tempBitOffset)));
    }
    
    @Override
    public void write2Bytes(long pos, short value)
    {
        write2Bytes((int)(pos / 8L), (int)(pos % 8L), value);
    }
    
    @Override
    public void write4Bytes(int bytePos, int bitOffset, int value)
    {
        transPos(bytePos, bitOffset);
        if (this.tempBitOffset == 0)
        {
            array[this.tempIndex] = (byte)(value >>> 24);
            array[this.tempIndex + 1] = (byte)(value >>> 16);
            array[this.tempIndex + 2] = (byte)(value >>> 8);
            array[this.tempIndex + 3] = (byte)value;
        }
        int b1 = (array[this.tempIndex] & ~AND_TABLE[this.tempBitOffset]) & 0x000000ff;
        int b5 = array[this.tempIndex + 4] & AND_TABLE[this.tempBitOffset];
        array[this.tempIndex] = (byte)(b1 | (value >>> (this.tempBitOffset + 24)));
        array[this.tempIndex + 1] = (byte)(value >>> (this.tempBitOffset + 16));
        array[this.tempIndex + 2] = (byte)(value >>> (this.tempBitOffset + 8));
        array[this.tempIndex + 3] = (byte)(value >>> (this.tempBitOffset));
        array[this.tempIndex + 4] = (byte)(b5 | (value << (8 - this.tempBitOffset)));
    }
    
    @Override
    public void write4Bytes(long pos, int value)
    {
        write4Bytes((int)(pos / 8L), (int)(pos % 8L), value);
    }
    
    @Override
    public void write8Bytes(int bytePos, int bitOffset, long value)
    {
        write4Bytes(bytePos, bitOffset, (int)(value >>> 32));
        write4Bytes(bytePos + 4, bitOffset, (int)value);
    }
    
    @Override
    public void write8Bytes(long pos, long value)
    {
        write8Bytes((int)(pos / 8L), (int)(pos % 8L), value);
    }
    
    @Override
    public void write4BytesInOrder(int bytePos, int bitOffset, int value, int bitsNum)
    {
        if (bitsNum == 32)
        {
            write4Bytes(bytePos, bitOffset, value);
        }
        else if (bitsNum == 16)
        {
            write2Bytes(bytePos, bitOffset, (short)(value >>> 16));
        }
        else if (bitsNum == 8)
        {
            writeByte(bytePos, bitOffset, (byte)(value >>> 24));
        }
        else if (bitsNum == 24)
        {
            writeByte(bytePos, bitOffset, (byte)(value >>> 24));
            write2Bytes(bytePos + 1, bitOffset, (short)(value >>> 8));
        }
        else
        {
            int bIndex = this.startIndex + bytePos + (bitsNum - 1) / 8;
            int bOffset = this.startBitOffset + bitOffset + (bitsNum - 1) % 8;
            if (bOffset > 7)
            {
                bIndex++;
                bOffset -= 8;
            }
            value = (value >>> (32 - bitsNum)) << (32 - bitsNum);
            transPos(bytePos, bitOffset);
            if (bIndex - this.tempIndex == 4)
            {
                int b1 = (array[this.tempIndex] & ~AND_TABLE[this.tempBitOffset]) & 0x000000ff;
                int b5 = array[this.tempIndex + 4] & AND_TABLE[bOffset + 1];
                array[this.tempIndex] = (byte)(b1 | (value >>> (this.tempBitOffset + 24)));
                array[this.tempIndex + 1] = (byte)(value >>> (this.tempBitOffset + 16));
                array[this.tempIndex + 2] = (byte)(value >>> (this.tempBitOffset + 8));
                array[this.tempIndex + 3] = (byte)(value >>> (this.tempBitOffset));
                array[this.tempIndex + 4] = (byte)(b5 | (value << (8 - this.tempBitOffset)));
            }
            else if (bIndex - this.tempIndex == 3)
            {
                int b1 = (array[this.tempIndex] & ~AND_TABLE[this.tempBitOffset]) & 0x000000ff;
                int b4 = array[this.tempIndex + 4] & AND_TABLE[bOffset + 1];
                array[this.tempIndex] = (byte)(b1 | (value >>> (this.tempBitOffset + 24)));
                array[this.tempIndex + 1] = (byte)(value >>> (this.tempBitOffset + 16));
                array[this.tempIndex + 2] = (byte)(value >>> (this.tempBitOffset + 8));
                array[this.tempIndex + 3] = (byte)(b4 | (value >>> (this.tempBitOffset)));
            }
            else if (bIndex - this.tempIndex == 2)
            {
                int b1 = (array[this.tempIndex] & ~AND_TABLE[this.tempBitOffset]) & 0x000000ff;
                int b3 = array[this.tempIndex + 4] & AND_TABLE[bOffset + 1];
                array[this.tempIndex] = (byte)(b1 | (value >>> (this.tempBitOffset + 24)));
                array[this.tempIndex + 1] = (byte)(value >>> (this.tempBitOffset + 16));
                array[this.tempIndex + 2] = (byte)(b3 | (value >>> (this.tempBitOffset + 8)));
            }
            else if (bIndex - this.tempIndex == 1)
            {
                int b1 = (array[this.tempIndex] & ~AND_TABLE[this.tempBitOffset]) & 0x000000ff;
                int b2 = array[this.tempIndex + 1] & AND_TABLE[bOffset + 1];
                array[this.tempIndex] = (byte)(b1 | (value >>> (this.tempBitOffset + 24)));
                array[this.tempIndex + 1] = (byte)(b2 | (value >>> (this.tempBitOffset + 16)));
            }
            else
            {
                int b = array[this.tempIndex];
                b &= (~AND_TABLE[this.tempBitOffset] | AND_TABLE[bOffset + 1]);
                value >>>= this.tempBitOffset + 24;
                value &= AND_TABLE[this.tempBitOffset];
                array[this.tempIndex] = (byte)(b | value);
            }
        }
    }
    
    @Override
    public void write4BytesInOrder(long pos, int value, int bitsNum)
    {
        write4BytesInOrder((int)(pos / 8L), (int)(pos % 8L), value, bitsNum);
    }
    
    @Override
    public void write8BytesInOrder(int bytePos, int bitOffset, long value, int bitsNum)
    {
        if (bitsNum <= 32)
        {
            write4BytesInOrder(bytePos, bitOffset, (int)(value >>> 32), bitsNum);
        }
        else
        {
            write4Bytes(bytePos, bitOffset, (int)(value >>> 32));
            write4BytesInOrder(bytePos, bitOffset, (int)value, bitsNum - 32);
        }
    }
    
    @Override
    public void write8BytesInOrder(long pos, long value, int bitsNum)
    {
        write8BytesInOrder((int)(pos / 8L), (int)(pos % 8L), value, bitsNum);
    }
    
    @Override
    public BaseAccessor partOf(int startBytePos, int startBitOffset, int lengthInBytes,
            int redundantBits)
    {
        transPos(startBytePos, startBitOffset);
        int endIndex;
        int endBitOffset;
        if (redundantBits == 0)
        {
            endIndex = this.tempIndex + lengthInBytes - 1;
            endBitOffset = this.tempBitOffset + 7;
        }
        else
        {
            endIndex = this.tempIndex + lengthInBytes;
            endBitOffset = this.tempBitOffset + redundantBits - 1;
        }
        if (endBitOffset > 7)
        {
            endIndex++;
            endBitOffset -= 8;
        }
        return new ByteArrayBaseAccessorOld(array, this.tempIndex, this.tempBitOffset, endIndex,
                endBitOffset);
    }
    
    @Override
    public BaseAccessor partOf(long startPos, long length)
    {
        return partOf((int)(startPos / 8L), (int)(startPos % 8L), (int)(length / 8L),
                (int)(length % 8L));
    }
    
    @Override
    public BaseAccessor deepClone()
    {
        byte[] newArray = new byte[this.endIndex - this.startIndex + 1];
        System.arraycopy(array, this.startIndex, newArray, 0, newArray.length);
        return new ByteArrayBaseAccessorOld(newArray, 0, this.startBitOffset, newArray.length - 1,
                this.endBitOffset);
    }
    
    /**
     * Same as {@linkplain #deepClone()}.
     */
    @Override
    protected Object clone() throws CloneNotSupportedException
    {
        return deepClone();
    }
    
    /**
     * Sets special element of {@linkplain #array} to 0xff or 0, true is 0xff,false is 0.
     * 
     * @param startIndex
     *            start index
     * @param endIndex
     *            end index
     * @param value
     *            true is 0xff,false is 0
     */
    private void setArray(int startIndex, int endIndex, boolean value)
    {
        if (value)
        {
            for (int i = startIndex; i <= endIndex; i++)
            {
                array[i] = (byte)0xff;
            }
        }
        else
        {
            for (int i = startIndex; i <= endIndex; i++)
            {
                array[i] = (byte)0;
            }
        }
    }
    
    @Override
    public void setBits(boolean value)
    {
        if (this.startBitOffset == 0 && this.endBitOffset == 7)
        {
            setArray(this.startIndex, this.endIndex, value);
        }
        else
        {
            if (this.startIndex == this.endIndex)
            {
                if (value)
                {
                    array[this.startIndex] |= (byte)(AND_TABLE[this.startBitOffset] & ~AND_TABLE[this.endIndex + 1]);
                }
                else
                {
                    array[this.startIndex] &= (byte)(~AND_TABLE[this.startBitOffset] | AND_TABLE[this.endIndex + 1]);
                }
                return;
            }
            if (value)
            {
                array[this.startIndex] |= (byte)(AND_TABLE[this.startBitOffset]);
                array[this.endIndex] |= (byte)(~AND_TABLE[this.endIndex + 1]);
            }
            else
            {
                array[this.startIndex] &= (byte)(~AND_TABLE[this.startBitOffset]);
                array[this.endIndex] &= (byte)(AND_TABLE[this.endIndex + 1]);
            }
            if (this.endIndex - this.startIndex > 1)
            {
                setArray(this.startIndex + 1, this.endIndex - 1, value);
            }
        }
    }
    
    /**
     * Checks whether special accessor implementation is same type of this implementation.
     * 
     * @param accessor
     *            special accessor
     * @return true if it is, else false
     */
    private boolean isSameImplementWithThis(BaseAccessor accessor)
    {
        return accessor.getClass().getName().equals(this.getClass().getName());
    }
    
    /**
     * Returns shorter length in bytes of two {@linkplain BaseAccessor}.
     * 
     * @param a
     *            one {@linkplain BaseAccessor}
     * @param b
     *            another {@linkplain BaseAccessor}
     * @return shorter length in bytes of two {@linkplain BaseAccessor}
     */
    private int getShorterByteLength(BaseAccessor a, BaseAccessor b)
    {
        int la = a.lengthInBytes();
        int lb = b.lengthInBytes();
        return la < lb ? la : lb;
    }
    
    /**
     * Returns shorter length in bits of two {@linkplain BaseAccessor}.
     * 
     * @param a
     *            one {@linkplain BaseAccessor}
     * @param b
     *            another {@linkplain BaseAccessor}
     * @return shorter length in bits of two {@linkplain BaseAccessor}
     */
    private long getShorterBitsLength(BaseAccessor a, BaseAccessor b)
    {
        long la = a.lengthInBits();
        long lb = b.lengthInBits();
        return la < lb ? la : lb;
    }
    
    /**
     * Common copies data from this accessor to dest accessor.
     * 
     * @param dest
     *            special dest accessor
     */
    private void baseCopy(BaseAccessor dest)
    {
        long length = getShorterBitsLength(this, dest);
        int byteLen = (int)(length / 32L);
        for (int i = 0; i < byteLen; i += 4)
        {
            dest.write4Bytes(i, 0, this.readInt(i, 0));
        }
        int restBits = (int)(length % 32L);
        if (restBits > 0)
        {
            dest.write4BytesInOrder(byteLen, 0,
                    this.readBitsAs4Bytes(byteLen, 0, restBits) << (32 - restBits), restBits);
        }
    }
    
    /**
     * Copies data of this byte array accessor into special dest byte array accessor.
     * 
     * @param dest
     *            special byte array dest, not null
     */
    private void byteArrayCopy(ByteArrayBaseAccessorOld dest)
    {
        if (this.startBitOffset == 0 && this.endBitOffset == 7 && dest.startBitOffset == 0
                && dest.endBitOffset == 7)
        {
            System.arraycopy(this.array, this.startIndex, dest.array, dest.startIndex,
                    getShorterByteLength(this, dest));
            return;
        }
        else if (this.startBitOffset == dest.startBitOffset)
        {
            ByteArrayBaseAccessorOld shorter = this.lengthInBits() < dest.lengthInBits() ? this : dest;
            int fullElementCount = shorter.endIndex - shorter.startIndex - 1;
            if (fullElementCount > 0)
            {
                System.arraycopy(this.array, this.startIndex + 1, dest.array, dest.startIndex + 1,
                        fullElementCount);
            }
            int srcEle;
            int destEle;
            if (shorter.startIndex != shorter.endIndex)
            {
                // head
                srcEle = this.array[this.startIndex];
                destEle = dest.array[dest.startIndex];
                srcEle &= AND_TABLE[this.startBitOffset];
                destEle &= ~AND_TABLE[dest.startBitOffset];
                dest.array[dest.startIndex] = (byte)(srcEle | destEle);
                // tail
                srcEle = this.array[this.startIndex + fullElementCount + 1];
                destEle = dest.array[dest.startIndex + fullElementCount + 1];
                srcEle &= ~AND_TABLE[shorter.endBitOffset + 1];
                destEle &= AND_TABLE[shorter.endBitOffset + 1];
                dest.array[dest.startIndex + fullElementCount + 1] = (byte)(srcEle | destEle);
                return;
            }
            else
            {
                srcEle = this.array[this.startIndex];
                destEle = dest.array[dest.startIndex];
                srcEle &= (AND_TABLE[this.startBitOffset] | ~AND_TABLE[shorter.endBitOffset + 1]);
                destEle &= (~AND_TABLE[this.startBitOffset] | AND_TABLE[shorter.endBitOffset + 1]);
                dest.array[dest.startIndex] = (byte)(srcEle | destEle);
                return;
            }
        }
        else
        {
            baseCopy(dest);
        }
    }
    
    @Override
    public void copy(BaseAccessor dest)
    {
        if (isSameImplementWithThis(dest))
        {
            byteArrayCopy((ByteArrayBaseAccessorOld)dest);
        }
        else
        {
            baseCopy(dest);
        }
    }
    
    @Override
    public void and(BaseAccessor target)
    {
        long length = getShorterBitsLength(this, target);
        int byteLen = (int)(length / 32L);
        for (int i = 0; i < byteLen; i += 4)
        {
            target.write4Bytes(i, 0, this.readInt(i, 0) & target.readInt(i, 0));
        }
        int restBits = (int)(length % 32L);
        if (restBits > 0)
        {
            target.write4BytesInOrder(byteLen, 0, (this.readBitsAs4Bytes(byteLen, 0, restBits) & target
                    .readBitsAs4Bytes(byteLen, 0, restBits)) << (32 - restBits), restBits);
        }
    }
    
    @Override
    public void or(BaseAccessor target)
    {
        long length = getShorterBitsLength(this, target);
        int byteLen = (int)(length / 32L);
        for (int i = 0; i < byteLen; i += 4)
        {
            target.write4Bytes(i, 0, this.readInt(i, 0) | target.readInt(i, 0));
        }
        int restBits = (int)(length % 32L);
        if (restBits > 0)
        {
            target.write4BytesInOrder(byteLen, 0, (this.readBitsAs4Bytes(byteLen, 0, restBits) | target
                    .readBitsAs4Bytes(byteLen, 0, restBits)) << (32 - restBits), restBits);
        }
    }
    
    @Override
    public void xor(BaseAccessor target)
    {
        long length = getShorterBitsLength(this, target);
        int byteLen = (int)(length / 32L);
        for (int i = 0; i < byteLen; i += 4)
        {
            target.write4Bytes(i, 0, this.readInt(i, 0) ^ target.readInt(i, 0));
        }
        int restBits = (int)(length % 32L);
        if (restBits > 0)
        {
            target.write4BytesInOrder(byteLen, 0, (this.readBitsAs4Bytes(byteLen, 0, restBits) ^ target
                    .readBitsAs4Bytes(byteLen, 0, restBits)) << (32 - restBits), restBits);
        }
    }
    
    @Override
    public void not()
    {
        long length = this.lengthInBits();
        int byteLen = (int)(length / 32L);
        for (int i = 0; i < byteLen; i += 4)
        {
            this.write4Bytes(i, 0, ~this.readInt(i, 0));
        }
        int restBits = (int)(length % 32L);
        if (restBits > 0)
        {
            this.write4BytesInOrder(byteLen, 0,
                    ~(this.readBitsAs4Bytes(byteLen, 0, restBits) << (32 - restBits)), restBits);
        }
    }
    
    @Override
    public void reverse()
    {
        long length = lengthInBits();
        if (length <= 64)
        {
            long v = readBitsAs8Bytes(0L, (int)length);
            v = Long.reverse(v);
            write8BytesInOrder(0L, v, (int)length);
            return;
        }
        long haflLength = length / 2L;
        int rest = (int)(haflLength % 32L);
        long longLen = haflLength - rest;
        for (long l = 0L; l < longLen; l += 32)
        {
            int head = read4Bytes(l);
            int tail = read4Bytes(length - ((l + 1) * 32));
            head = Integer.reverse(head);
            tail = Integer.reverse(tail);
            write4Bytes(l, tail);
            write4Bytes(length - ((l + 1) * 32), head);
        }
        if (rest > 0)
        {
            int bits = rest * 2;
            if (length % 2 == 1)
            {
                bits++;
            }
            write8BytesInOrder(haflLength - rest, Long.reverse(readBitsAs8Bytes(haflLength - rest, bits)),
                    bits);
        }
    }
    
    @Override
    public void reverseInBytes()
    {
        long length = lengthInBits();
        if (length % 8L != 0)
        {
            return;
        }
        long haflLength = length / 2L;
        long longLen = haflLength;
        for (long l = 0L; l < longLen; l += 32)
        {
            int head = read4Bytes(l);
            int tail = read4Bytes(length - ((l + 1) * 32));
            write4Bytes(l, tail);
            write4Bytes(length - ((l + 1) * 32), head);
        }
    }
    
    @Override
    public void logicalLeft(long bits)
    {
        long length = lengthInBits();
        long restLength = length - bits;
        partOf(bits, restLength).copy(partOf(0, restLength));
        partOf(restLength, bits).setBits(false);
    }
    
    /**
     * Shift right with special sign bit in head.
     * 
     * @param bits
     *            shifted bits number
     * @param signBit
     *            sign bit in head
     */
    private void shiftRight(long bits, boolean signBit)
    {
        BaseAccessor copy = deepClone();
        long length = lengthInBits();
        long restLength = length - bits;
        copy.partOf(0, restLength).copy(partOf(bits, restLength));
        copy = null;
        partOf(0, bits).setBits(signBit);
    }
    
    @Override
    public void logicalRight(long bits)
    {
        shiftRight(bits, false);
    }
    
    @Override
    public void arithmeticLeft(long bits)
    {
        logicalLeft(bits);
    }
    
    @Override
    public void arithmeticRight(long bits)
    {
        shiftRight(bits, readBit(0));
    }
    
    @Override
    public void rotateLeft(long bits)
    {
        BaseAccessor copy = deepClone();
        long length = lengthInBits();
        copy.partOf(bits, length - bits).copy(partOf(0, length - bits));
        copy.partOf(0, bits).copy(partOf(length - bits, bits));
        copy = null;
    }
    
    @Override
    public void rotateRight(long bits)
    {
        BaseAccessor copy = deepClone();
        long length = lengthInBits();
        copy.partOf(length - bits, bits).copy(partOf(0, bits));
        copy.partOf(0, length - bits).copy(partOf(bits, length - bits));
        copy = null;
    }
    
    /**
     * Returns min value of two number.
     */
    private long min(long a, long b)
    {
        return a < b ? a : b;
    }
    
    @Override
    public int toBooleanArray(boolean[] array, int startIndex)
    {
        long length = min(this.lengthInBits(), array.length - startIndex);
        int byteCount = (int)(length / 8L);
        for (int i = 0, j = startIndex; i < byteCount; i += 8)
        {
            int v = read(i, 0);
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
        int restBits = (int)(length % 8L);
        if (restBits > 0)
        {
            for (int i = (int)(length - restBits); i < length; i++)
            {
                array[i] = readBit((long)i);
            }
        }
    }
    
    @Override
    public long toByteArray(byte[] dest, int startIndex, int bitOffset)
    {
        copy(new ByteArrayBaseAccessorOld(dest, startIndex, bitOffset, dest.length - 1, 7));
    }
    
    /**
     * Sets 0 or 1 for special bits of given int value. The set bits start from start bit inclusive,
     * number of set bits will be set.
     * 
     * @param value
     *            given int value
     * @param startBit
     *            start bit, default in [0, 31]
     * @param bitsNum
     *            number of set bits, default in [1, 32]
     * @param bitValue
     *            value will be set, true is 1, false is 0
     * @return value after set bits
     */
    private int setIntBits(int value, int startBit, int bitsNum, boolean bitValue)
    {
        if (startBit == 0)
        {
            if (startBit + bitsNum == 32)
            {
                return bitValue ? 0xffffffff : 0x00000000;
            }
            else if (bitValue)
            {
                value = ~value;
                value <<= bitsNum;
                value >>>= bitsNum;
                return ~value;
            }
            else
            {
                value <<= bitsNum;
                value >>>= bitsNum;
                return value;
            }
        }
        else if (startBit + bitsNum == 32)
        {
            if (bitValue)
            {
                value = ~value;
                value >>>= bitsNum;
                value <<= bitsNum;
                return ~value;
            }
            else
            {
                value >>>= bitsNum;
                value <<= bitsNum;
                return value;
            }
        }
        else
        {
            if (bitValue)
            {
                int i1 = ~value;
                i1 >>>= (32 - bitsNum);
                i1 <<= (32 - bitsNum);
                int i2 = ~value;
                i2 <<= (startBit + bitsNum);
                i2 >>>= (startBit + bitsNum);
                return ~(i1 | i2);
            }
            else
            {
                int i1 = value;
                i1 >>>= (32 - bitsNum);
                i1 <<= (32 - bitsNum);
                int i2 = value;
                i2 <<= (startBit + bitsNum);
                i2 >>>= (startBit + bitsNum);
                return i1 | i2;
            }
        }
    }
    
    /**
     * Gets end index and bit offset and put them in to {@linkplain #tempIndex} and
     * {@linkplain #tempBitOffset}. This method is used to
     * {@code toXxxArray(short[] dest, int startIndex, int bitOffset)}.
     * 
     * @param startIndex
     *            start index
     * @param bitOffset
     *            bit offset of start index, [0, elementLength - 1]
     * @param elementLength
     *            element length of spaecial array
     * @param arrayEffectiveLength
     *            effective length of spaecial array
     */
    private long getEndIndexAndBitOffset(int startIndex, int bitOffset, int elementLength,
            long arrayEffectiveLength)
    {
        this.tempIndex = startIndex + (int)(arrayEffectiveLength / 16L);
        this.tempBitOffset = bitOffset + (int)(arrayEffectiveLength % 16L);
        if (this.tempBitOffset > 15)
        {
            this.tempIndex++;
            this.tempBitOffset -= 16;
        }
    }
    
    @Override
    public long toShortArray(short[] dest, int startIndex, int bitOffset)
    {
        long length = min(lengthInBits(), (dest.length - startIndex) * 16L - bitOffset);
        getEndIndexAndBitOffset(startIndex, bitOffset, 16, length);
        if (startIndex == this.tempIndex)
        {
            int s = dest[startIndex];
            int v = readBitsAs4Bytes(0L, (int)length) << (16 - (this.tempBitOffset + 1));
            s = setIntBits(s, bitOffset + 16, (int)length, false);
            v = setIntBits(v, 0, 16 + bitOffset, false);
            dest[startIndex] = (short)(s | v);
        }
        else
        {
            int elementCount = this.tempIndex - startIndex - 1;
            if (elementCount > 0)
            {
                for (int i = 0, j = startIndex + 1; i < elementCount; i++, j++)
                {
                    dest[j] = read2Bytes(i * 16L + (16 - bitOffset));
                }
            }
            // Head and tail
            int s = dest[startIndex];
            int v = readBitsAs4Bytes(0L, 16 - bitOffset);
            s = setIntBits(s, bitOffset + 16, 16 - bitOffset, false);
            v = setIntBits(v, 0, 16 + bitOffset, false);
            dest[startIndex] = (short)(s | v);
            
            s = dest[this.tempIndex];
            v = readBitsAs4Bytes(elementCount * 16L + (16 - bitOffset), this.tempBitOffset) << (16 - (this.tempBitOffset + 1));
            s = setIntBits(s, 0, 16 + this.tempBitOffset + 1, false);
            dest[startIndex] = (short)(s | v);
        }
    }
    
    @Override
    public long toCharArray(char[] dest, int startIndex, int bitOffset)
    {
        long length = min(lengthInBits(), (dest.length - startIndex) * 16L - bitOffset);
        getEndIndexAndBitOffset(startIndex, bitOffset, 16, length);
        if (startIndex == this.tempIndex)
        {
            int c = dest[startIndex];
            int v = readBitsAs4Bytes(0L, (int)length) << (16 - (this.tempBitOffset + 1));
            c = setIntBits(c, bitOffset + 16, (int)length, false);
            v = setIntBits(v, 0, 16 + bitOffset, false);
            dest[startIndex] = (char)(c | v);
        }
        else
        {
            int elementCount = this.tempIndex - startIndex - 1;
            if (elementCount > 0)
            {
                for (int i = 0, j = startIndex + 1; i < elementCount; i++, j++)
                {
                    dest[j] = (char)read2Bytes(i * 16L + (16 - bitOffset));
                }
            }
            // Head and tail
            int c = dest[startIndex];
            int v = readBitsAs4Bytes(0L, 16 - bitOffset);
            c = setIntBits(c, bitOffset + 16, 16 - bitOffset, false);
            v = setIntBits(v, 0, 16 + bitOffset, false);
            dest[startIndex] = (char)(c | v);
            
            c = dest[this.tempIndex];
            v = readBitsAs4Bytes(elementCount * 16L + (16 - bitOffset), this.tempBitOffset) << (16 - this.tempBitOffset - 1);
            c = setIntBits(c, 0, 16 + this.tempBitOffset + 1, false);
            dest[startIndex] = (char)(c | v);
        }
    }
    
    @Override
    public long toIntArray(int[] dest, int startIndex, int bitOffset)
    {
        long length = min(lengthInBits(), (dest.length - startIndex) * 32L - bitOffset);
        getEndIndexAndBitOffset(startIndex, bitOffset, 32, length);
        if (startIndex == this.tempIndex)
        {
            int i = dest[startIndex];
            int v = readBitsAs4Bytes(0L, (int)length) << (32 - (this.tempBitOffset + 1));
            i = setIntBits(i, bitOffset, (int)length, false);
            v = setIntBits(v, 0, bitOffset, false);
            dest[startIndex] = i | v;
        }
        else
        {
            int elementCount = this.tempIndex - startIndex - 1;
            if (elementCount > 0)
            {
                for (int i = 0, j = startIndex + 1; i < elementCount; i++, j++)
                {
                    dest[j] = read4Bytes(i * 32L + (32 - bitOffset));
                }
            }
            // Head and tail
            int i = dest[startIndex];
            int v = readBitsAs4Bytes(0L, 32 - bitOffset);
            i = setIntBits(i, bitOffset, 32 - bitOffset, false);
            v = setIntBits(v, 0, bitOffset, false);
            dest[startIndex] = i | v;
            
            i = dest[this.tempIndex];
            v = readBitsAs4Bytes(elementCount * 32L + (32 - bitOffset), this.tempBitOffset) << (32 - (this.tempBitOffset + 1));
            i = setIntBits(i, 0, this.tempBitOffset + 1, false);
            dest[startIndex] = i | v;
        }
    }
    
    @Override
    public long toFloatArray(float[] dest, int startIndex, int bitOffset)
    {
        long length = min(lengthInBits(), (dest.length - startIndex) * 32L - bitOffset);
        getEndIndexAndBitOffset(startIndex, bitOffset, 32, length);
        if (startIndex == this.tempIndex)
        {
            int i = Float.floatToRawIntBits(dest[startIndex]);
            int v = readBitsAs4Bytes(0L, (int)length) << (32 - (this.tempBitOffset + 1));
            i = setIntBits(i, bitOffset, (int)length, false);
            v = setIntBits(v, 0, bitOffset, false);
            dest[startIndex] = Float.intBitsToFloat(i | v);
        }
        else
        {
            int elementCount = this.tempIndex - startIndex - 1;
            if (elementCount > 0)
            {
                for (int i = 0, j = startIndex + 1; i < elementCount; i++, j++)
                {
                    dest[j] = Float.intBitsToFloat(read4Bytes(i * 32L + (32 - bitOffset)));
                }
            }
            // Head and tail
            int i = Float.floatToRawIntBits(dest[startIndex]);
            int v = readBitsAs4Bytes(0L, 32 - bitOffset);
            i = setIntBits(i, bitOffset, 32 - bitOffset, false);
            v = setIntBits(v, 0, bitOffset, false);
            dest[startIndex] = Float.intBitsToFloat(i | v);
            
            i = Float.floatToRawIntBits(dest[this.tempIndex]);
            v = readBitsAs4Bytes(elementCount * 32L + (32 - bitOffset), this.tempBitOffset) << (32 - (this.tempBitOffset + 1));
            i = setIntBits(i, 0, this.tempBitOffset + 1, false);
            dest[startIndex] = Float.intBitsToFloat(i | v);
        }
    }
    
    /**
     * Sets 0 or 1 for special bits of given long value. The set bits start from start bit
     * inclusive, number of set bits will be set.
     * 
     * @param value
     *            given long value
     * @param startBit
     *            start bit, default in [0, 31]
     * @param bitsNum
     *            number of set bits, default in [1, 32]
     * @param bitValue
     *            value will be set, true is 1, false is 0
     * @return value after set bits
     */
    private long setLongBits(long value, int startBit, int bitsNum, boolean bitValue)
    {
        if (startBit == 0)
        {
            if (startBit + bitsNum == 64)
            {
                return bitValue ? 0xffffffffffffffffL : 0x0000000000000000L;
            }
            else if (bitValue)
            {
                value = ~value;
                value <<= bitsNum;
                value >>>= bitsNum;
                return ~value;
            }
            else
            {
                value <<= bitsNum;
                value >>>= bitsNum;
                return value;
            }
        }
        else if (startBit + bitsNum == 64)
        {
            if (bitValue)
            {
                value = ~value;
                value >>>= bitsNum;
                value <<= bitsNum;
                return ~value;
            }
            else
            {
                value >>>= bitsNum;
                value <<= bitsNum;
                return value;
            }
        }
        else
        {
            if (bitValue)
            {
                long l1 = ~value;
                l1 >>>= (32 - bitsNum);
                l1 <<= (32 - bitsNum);
                long l2 = ~value;
                l2 <<= (startBit + bitsNum);
                l2 >>>= (startBit + bitsNum);
                return ~(l1 | l2);
            }
            else
            {
                long l1 = value;
                l1 >>>= (32 - bitsNum);
                l1 <<= (32 - bitsNum);
                long l2 = value;
                l2 <<= (startBit + bitsNum);
                l2 >>>= (startBit + bitsNum);
                return l1 | l2;
            }
        }
    }
    
    @Override
    public long toLongArray(long[] dest, int startIndex, int bitOffset)
    {
        long length = min(lengthInBits(), (dest.length - startIndex) * 64L - bitOffset);
        getEndIndexAndBitOffset(startIndex, bitOffset, 64, length);
        if (startIndex == this.tempIndex)
        {
            long l = dest[startIndex];
            long v = readBitsAs8Bytes(0L, (int)length) << (64 - (this.tempBitOffset + 1));
            l = setLongBits(l, bitOffset, (int)length, false);
            v = setLongBits(v, 0, bitOffset, false);
            dest[startIndex] = l | v;
        }
        else
        {
            int elementCount = this.tempIndex - startIndex - 1;
            if (elementCount > 0)
            {
                for (int i = 0, j = startIndex + 1; i < elementCount; i++, j++)
                {
                    dest[j] = read8Bytes(i * 64L + (64 - bitOffset));
                }
            }
            // Head and tail
            long l = dest[startIndex];
            long v = readBitsAs8Bytes(0L, 64 - bitOffset);
            l = setLongBits(l, bitOffset, 64 - bitOffset, false);
            v = setLongBits(v, 0, bitOffset, false);
            dest[startIndex] = l | v;
            
            l = dest[this.tempIndex];
            v = readBitsAs8Bytes(elementCount * 64L + (64 - bitOffset), this.tempBitOffset) << (64 - (this.tempBitOffset + 1));
            l = setLongBits(l, 0, this.tempBitOffset + 1, false);
            dest[startIndex] = l | v;
        }
    }
    
    @Override
    public long toDoubleArray(double[] dest, int startIndex, int bitOffset)
    {
        long length = min(lengthInBits(), (dest.length - startIndex) * 64L - bitOffset);
        getEndIndexAndBitOffset(startIndex, bitOffset, 64, length);
        if (startIndex == this.tempIndex)
        {
            long l = Double.doubleToRawLongBits(dest[startIndex]);
            long v = readBitsAs8Bytes(0L, (int)length) << (64 - (this.tempBitOffset + 1));
            l = setLongBits(l, bitOffset, (int)length, false);
            v = setLongBits(v, 0, bitOffset, false);
            dest[startIndex] = Double.longBitsToDouble(l | v);
        }
        else
        {
            int elementCount = this.tempIndex - startIndex - 1;
            if (elementCount > 0)
            {
                for (int i = 0, j = startIndex + 1; i < elementCount; i++, j++)
                {
                    dest[j] = Double.longBitsToDouble(read8Bytes(i * 64L + (64 - bitOffset)));
                }
            }
            // Head and tail
            long l = Double.doubleToRawLongBits(dest[startIndex]);
            long v = readBitsAs8Bytes(0L, 64 - bitOffset);
            l = setLongBits(l, bitOffset, 64 - bitOffset, false);
            v = setLongBits(v, 0, bitOffset, false);
            dest[startIndex] = Double.longBitsToDouble(l | v);
            
            l = Double.doubleToRawLongBits(dest[this.tempIndex]);
            v = readBitsAs8Bytes(elementCount * 64L + (64 - bitOffset), this.tempBitOffset) << (64 - (this.tempBitOffset + 1));
            l = setLongBits(l, 0, this.tempBitOffset + 1, false);
            dest[startIndex] = Double.longBitsToDouble(l | v);
        }
    }
}
/*
 * Complete at 2015-05-07 09:03:34 Copyright @Fred Suvn All right reserved.
 */
