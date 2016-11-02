package org.suvn.common.base.data.impl.primitivearray.old;

import org.suvn.common.base.data.BaseAccessor;
import org.suvn.common.base.data.impl.primitivearray.PrimitiveArrayBaseAccessorFactoryImpl;
import org.suvn.common.base.data.impl.primitivearray.PrimitiveImplUtils;
import org.suvn.common.quickstruct.ArrayPos;

/**
 * Common implementation of {@link BaseAccessor}, more detail for seeing {@link BaseAccessor}. Note
 * this implementation cannot be empty.
 * <p>
 * This implementation use short array to store and manipulate data, randomly. This is one of
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
 * @version 0.0.0, 2015-05-22 14:56:13
 */
class ShortArrayBaseAccessorOld implements BaseAccessor
{
    /**
     * Serial ID.
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * Array to store data, data managed by accessor maybe be stored in whole array, maybe part of
     * array which from start index with its bit offset to end index with its bit offset.
     */
    private final short[] array;
    
    /**
     * Element wide of array.
     */
    private static int ELE_WIDE = Short.SIZE;
    
    /**
     * Start index of array, data managed by accessor start from this index, see {@linkplain #array}
     * .
     */
    private final int startIndex;
    
    /**
     * Bit offset of {@linkplain #startIndex}.
     */
    private final int startOffset;
    
    /**
     * End index of array, data managed by accessor end at this index, see {@linkplain #array}.
     */
    private final int endIndex;
    
    /**
     * Bit offset of {@linkplain #endIndex}.
     */
    private final int endOffset;
    
    /**
     * Length of this data managed by accessor.
     */
    private long lengthInBits;
    
    /**
     * Temporary data, commonly used to store temporary value about index and bit offset when
     * computing or manipulating.
     */
    private transient ArrayPos temp = null;
    
    /**
     * Returns bit position (start from 0) relative to array from given index and its bit offset,
     * the index and its bit offset are also relative to array.
     * 
     * @param index
     *            given index relative to array
     * @param offset
     *            bit offset of index
     * @return bit position (start from 0) relative to array from given index and its bit offset
     */
    private long getArrayBitPosFromArrayIndex(int index, int offset)
    {
        return (long)index * ELE_WIDE + offset;
    }
    
    /**
     * Constructs an instance with special array, start index, bit offset of start index, end index
     * and bit offset of end index. Data managed by accessor start from given start index and its
     * bit offset to given end index and its bit offset.
     * 
     * @param array
     *            special array, not null
     * @param startIndex
     *            start index, [0, array length - 1]
     * @param startOffset
     *            bit offset of start index, [0, element wide - 1]
     * @param endIndex
     *            end index, [0, array length - 1]
     * @param endOffset
     *            bit offset of end index, [0, element wide - 1]
     */
    ShortArrayBaseAccessorOld(short[] array, int startIndex, int startOffset, int endIndex,
            int endOffset)
    {
        this.array = array;
        this.startIndex = startIndex;
        this.startOffset = startOffset;
        this.endIndex = endIndex;
        this.endOffset = endOffset;
        this.lengthInBits = getArrayBitPosFromArrayIndex(endIndex, endOffset)
                - getArrayBitPosFromArrayIndex(startIndex, startOffset) + 1;
    }
    
    /**
     * Constructs an instance with special array, start index, bit offset of start index, end index
     * and bit offset of end index. Data managed by accessor in whole of array.
     * 
     * @param array
     *            special array, not null
     */
    ShortArrayBaseAccessorOld(short[] array)
    {
        this(array, 0, 0, array.length - 1, ELE_WIDE - 1);
    }
    
    /**
     * Constructs an instance with special array, start index, bit offset of start index, end index
     * and bit offset of end index. Data managed by accessor start from given start index and its
     * bit offset to end of array.
     * 
     * @param array
     *            special array, not null
     * @param startIndex
     *            start index, [0, array length - 1]
     * @param startOffset
     *            bit offset of start index, [0, element wide - 1]
     */
    ShortArrayBaseAccessorOld(short[] array, int startIndex, int startOffset)
    {
        this(array, startIndex, startOffset, array.length - 1, ELE_WIDE - 1);
    }
    
    @Override
    public boolean isEmpty()
    {
        return false;
    }
    
    @Override
    public long lengthInBits()
    {
        return lengthInBits;
    }
    
    @Override
    public int lengthInBytes()
    {
        return (int)(lengthInBits() / 8L);
    }
    
    /**
     * Returns single instance of {@linkplain #temp}.
     * 
     * @return single instance of {@linkplain #temp}
     */
    private ArrayPos getTemp()
    {
        if (temp == null)
        {
            return new ArrayPos();
        }
        else
        {
            return temp;
        }
    }
    
    /**
     * Returns single instance of {@linkplain #temp}.
     * 
     * @return single instance of {@linkplain #temp}
     */
    private ArrayPos getTemp(int index, int offset)
    {
        if (temp == null)
        {
            return new ArrayPos(index, offset);
        }
        else
        {
            temp.index = index;
            temp.offset = offset;
            return temp;
        }
    }
    
    /**
     * Gets bit position (start from 0) relative to array from given byte position relative to
     * accessor.
     * 
     * @param bytePos
     *            byte position relative to accessor
     * @param bitOffset
     *            bit offset of byte position
     * @return bit position (start from 0) relative to array
     */
    private long getArrayBitPosFromAccessorBytePos(int bytePos, int bitOffset)
    {
        long startPos = getArrayBitPosFromArrayIndex(startIndex, startOffset);
        long relativePos = bytePos * 8L + bitOffset;
        return startPos + relativePos;
    }
    
    /**
     * Gets bit position (start from 0) relative to array from given bit position relative to
     * accessor.
     * 
     * @param pos
     *            bit position
     * @return bit position (start from 0) relative to array
     */
    private long getArrayBitPosFromAccessorBitPos(long pos)
    {
        return getArrayBitPosFromArrayIndex(startIndex, startOffset) + pos;
    }
    
    /**
     * Gets index info relative to array from given byte position info relative to accessor.
     * 
     * @param bytePos
     *            byte position relative to accessor
     * @param bitOffset
     *            bit offset of byte position
     * @return index info relative to array
     */
    private ArrayPos getArrayIndexPosFromAccessorBytePos(int bytePos, int bitOffset)
    {
        long pos = getArrayBitPosFromAccessorBytePos(bytePos, bitOffset);
        return getTemp((int)(pos / ELE_WIDE), (int)(pos % ELE_WIDE));
    }
    
    /**
     * Gets index info relative to array from given bit position info relative to accessor.
     * 
     * @param pos
     *            bit position
     * @return index info relative to array
     */
    private ArrayPos getArrayIndexPosFromAccessorBitPos(long pos)
    {
        pos = getArrayBitPosFromAccessorBitPos(pos);
        return getTemp((int)(pos / ELE_WIDE), (int)(pos % ELE_WIDE));
    }
    
    @Override
    public long remainderLength(int bytePos, int bitOffset)
    {
        return lengthInBits() - getArrayBitPosFromAccessorBytePos(bytePos, bitOffset);
    }
    
    @Override
    public long remainderLength(long pos)
    {
        return lengthInBits() - getArrayBitPosFromAccessorBitPos(pos);
    }
    
    @Override
    public BaseAccessor partOf(int startBytePos, int startBitOffset, int lengthInBytes,
            int redundantBits)
    {
        temp = getArrayIndexPosFromAccessorBytePos(startBytePos, startBitOffset);
        int startIndex = temp.index;
        int startOffset = temp.offset;
        
        temp.index = startBytePos + lengthInBytes;
        temp.offset = startBitOffset + redundantBits;
        if (temp.offset > 7)
        {
            temp.offset -= 8;
            temp.index++;
        }
        temp = getArrayIndexPosFromAccessorBytePos(temp.index, temp.offset);
        return new ShortArrayBaseAccessorOld(array, startIndex, startOffset, temp.index, temp.offset);
    }
    
    @Override
    public BaseAccessor partOf(long startPos, long length)
    {
        temp = getArrayIndexPosFromAccessorBitPos(startPos);
        int startIndex = temp.index;
        int startOffset = temp.offset;
        temp = getArrayIndexPosFromAccessorBitPos(startPos + length);
        return new ShortArrayBaseAccessorOld(array, startIndex, startOffset, temp.index, temp.offset);
    }
    
    @Override
    public BaseAccessor shadowClone()
    {
        return new ShortArrayBaseAccessorOld(array, startIndex, startOffset, endIndex, endOffset);
    }
    
    @Override
    public BaseAccessor deepClone()
    {
        short[] copy = new short[endIndex - startIndex + 1];
        System.arraycopy(array, startIndex, copy, 0, copy.length);
        return new ShortArrayBaseAccessorOld(copy, 0, startOffset, copy.length - 1, endOffset);
    }
    
    @Override
    public BaseAccessor clone()
    {
        return deepClone();
    }
    
    /**
     * Returns an int type from 1 short type, effective short value in low orders, high orders are
     * 0.
     * 
     * @param s
     *            low order
     * @return an int type from 1 short type
     */
    private int getInt(short s)
    {
        return s & 0x0000ffff;
    }
    
    /**
     * Returns an int type from 2 short type, s1 is high order, s2 is low order.
     * 
     * @param s1
     *            high order
     * @param s2
     *            low order
     * @return an int type from two short type
     */
    private int getInt(short s1, short s2)
    {
        return (s1 << ELE_WIDE) | ((s2) & 0x0000ffff);
    }
    
    /**
     * Returns a long type from 1 short type, the short type in low order, high orders are 0.
     * 
     * @param s
     *            low order
     * @return a long type from 1 short type
     */
    private long getLong(short s)
    {
        return getInt(s);
    }

    /**
     * Returns a long type from 2 short type, s1 and s2 in low orders, s2 in lowest order.
     * 
     * @param s1
     *            low order
     * @param s2
     *            lowest order
     * @return a long type from 2 short type
     */
    private long getLong(short s1, short s2)
    {
        return getInt(s1, s2) & 0x00000000ffffffffL;
    }

    /**
     * Returns a long type from 3 short type, s1, s2, s3 in high order, low order, lowest order.
     * 
     * @param s1
     *            high order
     * @param s2
     *            low order
     * @param s3
     *            lowest order
     * @return a long type from 3 short type
     */
    private long getLong(short s1, short s2, short s3)
    {
        long l1 = getInt(s1, s2) << 16;
        long l2 = getInt(s3);
        return l1 | l2;
    }

    /**
     * Returns a long type from 4 short type, s1 to s4 represent high order to low order.
     * 
     * @param s1
     *            highest order
     * @param s2
     *            higher order
     * @param s3
     *            lower order
     * @param s4
     *            lowest order
     * @return a long type from 4 short type
     */
    private long getLong(short s1, short s2, short s3, short s4)
    {
        long l1 = getInt(s1, s2) << 32;
        long l2 = getInt(s3, s4) & 0x00000000ffffffffL;
        return l1 | l2;
    }

    @Override
    public boolean readBit(int bytePos, int bitOffset)
    {
        temp = getArrayIndexPosFromAccessorBytePos(bytePos, bitOffset);
        return ((getInt(array[temp.index]) >> (ELE_WIDE - temp.offset - 1)) & 0x00000001) == 0x00000001 ? true
                : false;
    }
    
    @Override
    public boolean readBit(long pos)
    {
        temp = getArrayIndexPosFromAccessorBitPos(pos);
        return ((getInt(array[temp.index]) >> (ELE_WIDE - temp.offset - 1)) & 0x00000001) == 0x00000001 ? true
                : false;
    }
    
    /**
     * Reads int value from special array position.
     * 
     * @param temp
     *            array position
     * @return int value from special array position
     */
    private int read4BytesFromTemp(ArrayPos temp)
    {
        if (temp.offset == 0)
        {
            return getInt(array[temp.index], array[temp.index + 1]);
        }
        else
        {
            int i1 = getInt(array[temp.index], array[temp.index + 1]);
            i1 <<= temp.offset;
            int i2 = getInt(array[temp.index + 2]);
            i2 >>>= ELE_WIDE - temp.offset;
            return i1 | i2;
        }
    }
    
    /**
     * Reads long value from special array position.
     * 
     * @param temp
     *            array position
     * @return long value from special array position
     */
    private long read8BytesFromTemp(ArrayPos temp)
    {
        if (temp.offset == 0)
        {
            return getLong(array[temp.index], array[temp.index + 1], array[temp.index + 2],
                    array[temp.index + 3]);
        }
        else
        {
            long l1 = getLong(array[temp.index], array[temp.index + 1], array[temp.index + 2],
                    array[temp.index + 3]);
            l1 <<= temp.offset;
            long l2 = getLong(array[temp.index + 4]);
            l2 >>>= ELE_WIDE - temp.offset;
            return l1 | l2;
        }
    }

    /**
     * Reads bits of special number as int type from special array position.
     * 
     * @param temp
     *            array position
     * @param bitsNum
     *            number of special bits
     * @return bits of special number as int type from special array position
     */
    private int readBitsAs4BytesFromTemp(ArrayPos temp, int bitsNum)
    {
        if (bitsNum == 32)
        {
            return read4BytesFromTemp(temp);
        }
        if (temp.offset == 0)
        {
            if (bitsNum <= ELE_WIDE)
            {
                return getInt(array[temp.index]) >>> (ELE_WIDE - bitsNum);
            }
            else
            {
                return getInt(array[temp.index], array[temp.index + 1]) >>> (ELE_WIDE * 2 - bitsNum);
            }
        }
        else
        {
            if (bitsNum <= ELE_WIDE - temp.offset)
            {
                return PrimitiveImplUtils.partOfInt(getInt(array[temp.index]), ELE_WIDE
                        + temp.offset, bitsNum);
            }
            else if (bitsNum <= ELE_WIDE * 2 - temp.offset)
            {
                return PrimitiveImplUtils.partOfInt(
                        getInt(array[temp.index], array[temp.index + 1]), temp.offset, bitsNum);
            }
            else
            {
                return read4BytesFromTemp(temp) >>> (32 - bitsNum);
            }
        }
    }

    /**
     * Reads bits of special number as long type from special array position.
     * 
     * @param temp
     *            array position
     * @param bitsNum
     *            number of special bits
     * @return bits of special number as long type from special array position
     */
    private long readBitsAs8BytesFromTemp(ArrayPos temp, int bitsNum)
    {
        if (bitsNum == 64)
        {
            return read8BytesFromTemp(temp);
        }
        else if (bitsNum <= 32)
        {
            return readBitsAs4BytesFromTemp(temp, bitsNum) & 0x00000000ffffffffL;
        }
        else
        {
            if (temp.offset == 0)
            {
                if (bitsNum <= ELE_WIDE * 3)
                {
                    return getLong(array[temp.index], array[temp.index + 1], array[temp.index + 2]) >>> (ELE_WIDE * 3 - bitsNum);
                }
                else
                {
                    return getLong(array[temp.index], array[temp.index + 1], array[temp.index + 2],
                            array[temp.index + 3]) >>> (ELE_WIDE * 4 - bitsNum);
                }
            }
            else
            {
                if (bitsNum <= ELE_WIDE * 3 - temp.offset)
                {
                    return PrimitiveImplUtils
                            .partOfLong(
                                    getLong(array[temp.index], array[temp.index + 1],
                                            array[temp.index + 2]), ELE_WIDE + temp.offset, bitsNum);
                }
                else if (bitsNum <= ELE_WIDE * 4 - temp.offset)
                {
                    return PrimitiveImplUtils.partOfLong(
                            getLong(array[temp.index], array[temp.index + 1],
                                    array[temp.index + 2], array[temp.index + 3]), temp.offset,
                            bitsNum);
                }
                else
                {
                    return read8BytesFromTemp(temp) >>> (64 - bitsNum);
                }
            }
        }
    }

    @Override
    public int readInt(int bytePos, int bitOffset)
    {
        temp = getArrayIndexPosFromAccessorBytePos(bytePos, bitOffset);
        return read4BytesFromTemp(temp);
    }
    
    @Override
    public int read4Bytes(long pos)
    {
        temp = getArrayIndexPosFromAccessorBitPos(pos);
        return read4BytesFromTemp(temp);
    }
    
    @Override
    public long read8Bytes(int bytePos, int bitOffset)
    {
        temp = getArrayIndexPosFromAccessorBytePos(bytePos, bitOffset);
        return read8BytesFromTemp(temp);
    }
    
    @Override
    public long read8Bytes(long pos)
    {
        temp = getArrayIndexPosFromAccessorBitPos(pos);
        return read8BytesFromTemp(temp);
    }
    
    @Override
    public int readBitsAs4Bytes(int bytePos, int bitOffset, int bitsNum)
    {
        temp = getArrayIndexPosFromAccessorBytePos(bytePos, bitOffset);
        return readBitsAs4BytesFromTemp(temp, bitsNum);
    }
    
    @Override
    public int readBitsAs4Bytes(long pos, int bitsNum)
    {
        temp = getArrayIndexPosFromAccessorBitPos(pos);
        return readBitsAs4BytesFromTemp(temp, bitsNum);
    }
    
    @Override
    public long readBitsAs8Bytes(int bytePos, int bitOffset, int bitsNum)
    {
        temp = getArrayIndexPosFromAccessorBytePos(bytePos, bitOffset);
        return readBitsAs8BytesFromTemp(temp, bitsNum);
    }
    
    @Override
    public long readBitsAs8Bytes(long pos, int bitsNum)
    {
        temp = getArrayIndexPosFromAccessorBitPos(pos);
        return readBitsAs8BytesFromTemp(temp, bitsNum);
    }
    
    @Override
    public int readLast4Bytes(int bytePos, int bitOffset)
    {
        long remainder = remainderLength(bytePos, bitOffset);
        if (remainder >= 32)
        {
            return readInt(bytePos, bitOffset);
        }
        else
        {
            return readBitsAs4Bytes(bytePos, bitOffset, (int)remainder);
        }
    }
    
    @Override
    public int readLast4Bytes(long pos)
    {
        if (lengthInBits - pos >= 32)
        {
            return read4Bytes(pos);
        }
        else
        {
            return readBitsAs4Bytes(pos, (int)(lengthInBits - pos));
        }
    }
    
    @Override
    public long readLast8Bytes(int bytePos, int bitOffset)
    {
        long remainder = remainderLength(bytePos, bitOffset);
        if (lengthInBits - remainder >= 64)
        {
            return read8Bytes(bytePos, bitOffset);
        }
        else
        {
            return readBitsAs8Bytes(bytePos, bitOffset, (int)(lengthInBits - remainder));
        }
    }
    
    @Override
    public long readLast8Bytes(long pos)
    {
        long remainder = remainderLength(pos);
        if (lengthInBits - remainder >= 64)
        {
            return read8Bytes(pos);
        }
        else
        {
            return readBitsAs8Bytes(pos, (int)(lengthInBits - remainder));
        }
    }
    
    @Override
    public void writeBit(int bytePos, int bitOffset, boolean value)
    {
        temp = getArrayIndexPosFromAccessorBytePos(bytePos, bitOffset);
        int i = PrimitiveImplUtils.fillIntBits(getInt(array[temp.index]), ELE_WIDE + temp.offset,
                1, value);
        array[temp.index] = (short)i;
    }
    
    @Override
    public void writeBit(long pos, boolean value)
    {
        temp = getArrayIndexPosFromAccessorBitPos(pos);
        int i = PrimitiveImplUtils.fillIntBits(getInt(array[temp.index]), ELE_WIDE + temp.offset,
                1, value);
        array[temp.index] = (short)i;
    }
    
    /**
     * Writes int from given array index info.
     * 
     * @param temp
     *            given array index info
     * @param value
     *            written value
     */
    private void write4BytesFromTemp(ArrayPos temp, int value)
    {
        if (temp.offset == 0)
        {
            array[temp.index] = (short)(value >>> ELE_WIDE);
            array[temp.index + 1] = (short)value;
        }
        else
        {
            int i1 = getInt(array[temp.index], array[temp.index + 1]);
            int i2 = getInt(array[temp.index + 2]);
            i1 = PrimitiveImplUtils.putIntBits(value >>> temp.offset, i1, temp.offset,
                    32 - temp.offset);
            i2 = PrimitiveImplUtils.putIntBits(value << (ELE_WIDE - temp.offset), i2, 0, ELE_WIDE
                    + temp.offset);
            array[temp.index] = (short)(i1 >>> ELE_WIDE);
            array[temp.index + 1] = (short)i1;
            array[temp.index + 2] = (short)i2;
        }
    }
    
    /**
     * Writes long from given array index info.
     * 
     * @param temp
     *            given array index info
     * @param value
     *            written value
     */
    private void write8BytesFromTemp(ArrayPos temp, long value)
    {
        if (temp.offset == 0)
        {
            array[temp.index] = (short)(value >>> (ELE_WIDE * 3));
            array[temp.index + 1] = (short)(value >>> (ELE_WIDE * 2));
            array[temp.index + 2] = (short)(value >>> ELE_WIDE);
            array[temp.index + 3] = (short)value;
        }
        else
        {
            long l1 = getLong(array[temp.index], array[temp.index + 1], array[temp.index + 2],
                    array[temp.index + 3]);
            long l2 = getInt(array[temp.index + 4]);
            l1 = PrimitiveImplUtils.putLongBits(value >>> temp.offset, l1, temp.offset,
                    64 - temp.offset);
            l2 = PrimitiveImplUtils.putLongBits(value << (ELE_WIDE - temp.offset), l2, 0, ELE_WIDE
                    * 3 + temp.offset);
            array[temp.index] = (short)(l1 >>> (ELE_WIDE * 3));
            array[temp.index + 1] = (short)(l1 >>> (ELE_WIDE * 2));
            array[temp.index + 2] = (short)(l1 >>> ELE_WIDE);
            array[temp.index + 3] = (short)l1;
            array[temp.index + 4] = (short)l2;
        }
    }

    /**
     * Writes bits of special number as int type in order from array index info.
     * 
     * @param temp
     *            array index info
     * @param value
     *            written value
     * @param bitsNum
     *            number of written bits
     */
    private void write4BytesInOrderFromTemp(ArrayPos temp, int value, int bitsNum)
    {
        if (bitsNum == 32)
        {
            write4BytesFromTemp(temp, value);
        }
        else
        {
            if (temp.offset == 0)
            {
                if (bitsNum <= ELE_WIDE)
                {
                    int i = getInt(array[temp.index]);
                    i = PrimitiveImplUtils.putIntBits(value >>> ELE_WIDE, i, ELE_WIDE, bitsNum);
                    array[temp.index] = (short)i;
                }
                else
                {
                    int i = getInt(array[temp.index], array[temp.index + 1]);
                    i = PrimitiveImplUtils.putIntBits(value, i, 0, bitsNum);
                    array[temp.index] = (short)(i >>> ELE_WIDE);
                    array[temp.index + 1] = (short)i;
                }
            }
            else
            {
                if (bitsNum <= ELE_WIDE - temp.offset)
                {
                    int i = getInt(array[temp.index]);
                    i = PrimitiveImplUtils.putIntBits(value >>> (ELE_WIDE + temp.offset), i,
                            ELE_WIDE + temp.offset, bitsNum);
                    array[temp.index] = (short)i;
                }
                else if (bitsNum <= ELE_WIDE * 2 - temp.offset)
                {
                    int i = getInt(array[temp.index], array[temp.index + 1]);
                    i = PrimitiveImplUtils.putIntBits(value >>> temp.offset, i, temp.offset,
                            bitsNum);
                    array[temp.index] = (short)(i >>> ELE_WIDE);
                    array[temp.index + 1] = (short)i;
                }
                else
                {
                    int i1 = getInt(array[temp.index], array[temp.index + 1]);
                    int i2 = getInt(array[temp.index + 2]);
                    int leftBits = (ELE_WIDE * 2) - temp.offset;
                    int rightBits = bitsNum - leftBits;
                    i1 = PrimitiveImplUtils.putIntBits(value >>> temp.offset, i1, temp.offset,
                            leftBits);
                    i2 = PrimitiveImplUtils.putIntBits(value << (ELE_WIDE - rightBits), i2,
                            ELE_WIDE, rightBits);
                    array[temp.index] = (short)(i1 >>> ELE_WIDE);
                    array[temp.index + 1] = (short)i1;
                    array[temp.index + 2] = (short)i2;
                }
            }
        }
    }

    /**
     * Writes bits of special number as int type in order from array index info.
     * 
     * @param temp
     *            array index info
     * @param value
     *            written value
     * @param bitsNum
     *            number of written bits
     */
    private void write8BytesInOrderFromTemp(ArrayPos temp, long value, int bitsNum)
    {
        if (bitsNum == 64)
        {
            write8BytesFromTemp(temp, value);
        }
        else if (bitsNum <= 32)
        {
            write4BytesInOrderFromTemp(temp, (int)(value >>> 32), bitsNum);
        }
        else
        {
            if (temp.offset == 0)
            {
                if (bitsNum <= ELE_WIDE * 3)
                {
                    long l = getLong(array[temp.index], array[temp.index + 1],
                            array[temp.index + 2]);
                    l = PrimitiveImplUtils.putLongBits(value >>> ELE_WIDE, l, ELE_WIDE, bitsNum);
                    array[temp.index] = (short)(l >>> (ELE_WIDE * 2));
                    array[temp.index + 1] = (short)(l >>> ELE_WIDE);
                    array[temp.index + 2] = (short)(l);
                }
                else
                {
                    long l = getLong(array[temp.index], array[temp.index + 1],
                            array[temp.index + 2], array[temp.index + 3]);
                    l = PrimitiveImplUtils.putLongBits(value, l, 0, bitsNum);
                    array[temp.index] = (short)(l >>> (ELE_WIDE * 3));
                    array[temp.index + 1] = (short)(l >>> (ELE_WIDE * 2));
                    array[temp.index + 2] = (short)(l >>> ELE_WIDE);
                    array[temp.index + 3] = (short)(l);
                }
            }
            else
            {
                if (bitsNum <= ELE_WIDE * 3 - temp.offset)
                {
                    long l = getLong(array[temp.index], array[temp.index + 1],
                            array[temp.index + 2]);
                    l = PrimitiveImplUtils.putLongBits(value >>> (ELE_WIDE + temp.offset), l,
                            ELE_WIDE + temp.offset, bitsNum);
                    array[temp.index] = (short)(l >>> (ELE_WIDE * 2));
                    array[temp.index + 1] = (short)(l >>> ELE_WIDE);
                    array[temp.index + 2] = (short)(l);
                }
                else if (bitsNum <= ELE_WIDE * 4 - temp.offset)
                {
                    long l = getLong(array[temp.index], array[temp.index + 1],
                            array[temp.index + 2], array[temp.index + 3]);
                    l = PrimitiveImplUtils.putLongBits(value >>> temp.offset, l, temp.offset,
                            bitsNum);
                    array[temp.index] = (short)(l >>> (ELE_WIDE * 3));
                    array[temp.index + 1] = (short)(l >>> (ELE_WIDE * 2));
                    array[temp.index + 2] = (short)(l >>> ELE_WIDE);
                    array[temp.index + 3] = (short)(l);
                }
                else
                {
                    long l1 = getLong(array[temp.index], array[temp.index + 1],
                            array[temp.index + 2], array[temp.index + 3]);
                    long l2 = getLong(array[temp.index + 4]);
                    int leftBits = (ELE_WIDE * 4) - temp.offset;
                    int rightBits = bitsNum - leftBits;
                    l1 = PrimitiveImplUtils.putLongBits(value >>> temp.offset, l1, temp.offset,
                            leftBits);
                    l2 = PrimitiveImplUtils.putLongBits(value << (ELE_WIDE - rightBits), l2,
                            ELE_WIDE * 3, rightBits);
                    array[temp.index] = (short)(l1 >>> (ELE_WIDE * 3));
                    array[temp.index + 1] = (short)(l1 >>> (ELE_WIDE * 2));
                    array[temp.index + 2] = (short)(l1 >>> ELE_WIDE);
                    array[temp.index + 3] = (short)(l1);
                    array[temp.index + 4] = (short)(l2);
                }
            }
        }
    }

    @Override
    public void write4Bytes(int bytePos, int bitOffset, int value)
    {
        temp = getArrayIndexPosFromAccessorBytePos(bytePos, bitOffset);
        write4BytesFromTemp(temp, value);
    }
    
    @Override
    public void write4Bytes(long pos, int value)
    {
        temp = getArrayIndexPosFromAccessorBitPos(pos);
        write4BytesFromTemp(temp, value);
    }
    
    @Override
    public void write8Bytes(int bytePos, int bitOffset, long value)
    {
        temp = getArrayIndexPosFromAccessorBytePos(bytePos, bitOffset);
        write8BytesFromTemp(temp, value);
    }
    
    @Override
    public void write8Bytes(long pos, long value)
    {
        temp = getArrayIndexPosFromAccessorBitPos(pos);
        write8BytesFromTemp(temp, value);
    }
    
    @Override
    public void write4BytesInOrder(int bytePos, int bitOffset, int value, int bitsNum)
    {
        temp = getArrayIndexPosFromAccessorBytePos(bytePos, bitOffset);
        write4BytesInOrderFromTemp(temp, value, bitsNum);
    }
    
    @Override
    public void write4BytesInOrder(long pos, int value, int bitsNum)
    {
        temp = getArrayIndexPosFromAccessorBitPos(pos);
        write4BytesInOrderFromTemp(temp, value, bitsNum);
    }
    
    @Override
    public void write8BytesInOrder(int bytePos, int bitOffset, long value, int bitsNum)
    {
        temp = getArrayIndexPosFromAccessorBytePos(bytePos, bitOffset);
        write8BytesInOrderFromTemp(temp, value, bitsNum);
    }
    
    @Override
    public void write8BytesInOrder(long pos, long value, int bitsNum)
    {
        temp = getArrayIndexPosFromAccessorBitPos(pos);
        write8BytesInOrderFromTemp(temp, value, bitsNum);
    }
    
    @Override
    public int writeLast4BytesInOrder(int bytePos, int bitOffset, int value)
    {
        long remainder = remainderLength(bytePos, bitOffset);
        if (remainder >= 32)
        {
            write4Bytes(bytePos, bitOffset, value);
            return 32;
        }
        else
        {
            write4BytesInOrder(bytePos, bitOffset, value, (int)remainder);
            return (int)remainder;
        }
    }
    
    @Override
    public int writeLast4BytesInOrder(long pos, int value)
    {
        if (lengthInBits - pos >= 32)
        {
            write4Bytes(pos, value);
            return 32;
        }
        else
        {
            write4BytesInOrder(pos, value, (int)(lengthInBits - pos));
            return (int)(lengthInBits - pos);
        }
    }
    
    @Override
    public int writeLast8BytesInOrder(int bytePos, int bitOffset, long value)
    {
        long remainder = remainderLength(bytePos, bitOffset);
        if (remainder >= 64)
        {
            write8Bytes(bytePos, bitOffset, value);
            return 64;
        }
        else
        {
            write8BytesInOrder(bytePos, bitOffset, value, (int)remainder);
            return (int)remainder;
        }
    }
    
    @Override
    public int writeLast8BytesInOrder(long pos, long value)
    {
        if (lengthInBits - pos >= 64)
        {
            write8Bytes(pos, value);
            return 64;
        }
        else
        {
            write8BytesInOrder(pos, value, (int)(lengthInBits - pos));
            return (int)(lengthInBits - pos);
        }
    }
    
    @Override
    public void setBits(boolean value)
    {
        if (startIndex == endIndex)
        {
            int i = PrimitiveImplUtils.fillIntBits(getInt(array[startIndex]), ELE_WIDE
                    + startOffset, endOffset - startOffset + 1, value);
            array[startIndex] = (short)(i);
        }
        else
        {
            if (endIndex - startIndex > 1)
            {
                if (value)
                {
                    for (int i = startIndex + 1; i < endIndex; i++)
                    {
                        array[i] = 0xffffffff;
                    }
                }
                else
                {
                    for (int i = startIndex + 1; i < endIndex; i++)
                    {
                        array[i] = 0;
                    }
                }
            }
            int i = PrimitiveImplUtils.fillIntBits(getInt(array[startIndex]), ELE_WIDE
                    + startOffset, ELE_WIDE - startOffset, value);
            array[startIndex] = (short)(i);
            i = PrimitiveImplUtils.fillIntBits(getInt(array[endIndex]), ELE_WIDE, endOffset + 1,
                    value);
            array[endIndex] = (short)(i);
        }
    }
    
    /**
     * Returns shorter short array base accessor implementation between given two implementations.
     * 
     * @param a1
     *            an implementation
     * @param a2
     *            another implementation
     * @return shorter one
     */
    private ShortArrayBaseAccessorOld getShorterAccessor(ShortArrayBaseAccessorOld a1,
            ShortArrayBaseAccessorOld a2)
    {
        return a1.lengthInBits <= a2.lengthInBits ? a1 : a2;
    }
    
    /**
     * Copies data from this to dest, copied length is shorter length of two accessor.
     * 
     * @param dest
     *            dest accessor
     * @return actual copied length
     */
    private long baseCopy(BaseAccessor dest)
    {
        BaseAccessor shorter = PrimitiveImplUtils.getShorterAccessor(this, dest);
        long longWide = shorter.lengthInBits() / 64L;
        for (long p = 0; p < longWide; p += 64)
        {
            dest.write8Bytes(p, this.read8Bytes(p));
        }
        int restBits = (int)(shorter.lengthInBits() % 64L);
        dest.write8BytesInOrder(longWide,
                this.readBitsAs8Bytes(longWide, restBits) << (64 - restBits), restBits);
        return shorter.lengthInBits();
    }
    
    @Override
    public long copy(BaseAccessor dest)
    {
        if (PrimitiveImplUtils.isSameClass(this, dest))
        {
            ShortArrayBaseAccessorOld impl = (ShortArrayBaseAccessorOld)dest;
            ShortArrayBaseAccessorOld shorter = getShorterAccessor(this, impl);
            if (this.startOffset == impl.startOffset)
            {
                if (shorter.startIndex == shorter.endIndex)
                {
                    int i = PrimitiveImplUtils.putIntBits(getInt(this.array[this.startIndex]),
                            getInt(impl.array[impl.startIndex]), ELE_WIDE * 2 + this.startOffset,
                            (int)shorter.lengthInBits);
                    impl.array[impl.startIndex] = (short)(i);
                }
                else
                {
                    if (shorter.endIndex - shorter.startIndex > 1)
                    {
                        System.arraycopy(this.array, this.startIndex + 1, impl.array,
                                impl.startIndex + 1, shorter.endIndex - shorter.startIndex - 1);
                    }
                    int i = PrimitiveImplUtils.putIntBits(getInt(this.array[this.startIndex]),
                            getInt(impl.array[impl.startIndex]), ELE_WIDE * 2 + this.startOffset,
                            ELE_WIDE - this.startOffset);
                    impl.array[impl.startIndex] = (short)(i);
                    i = PrimitiveImplUtils.putIntBits(getInt(this.array[this.startIndex
                            + (shorter.endIndex - shorter.startIndex)]),
                            getInt(impl.array[impl.startIndex
                                    + (shorter.endIndex - shorter.startIndex)]), ELE_WIDE * 2,
                            shorter.endOffset + 1);
                    impl.array[impl.startIndex + (shorter.endIndex - shorter.startIndex)] = (short)(i);
                }
                return shorter.lengthInBits;
            }
        }
        return baseCopy(dest);
    }
    
    @Override
    public long and(BaseAccessor target)
    {
        BaseAccessor shorter = PrimitiveImplUtils.getShorterAccessor(this, target);
        long longWide = shorter.lengthInBits() / 64L;
        for (long p = 0; p < longWide; p += 64)
        {
            this.write8Bytes(p, this.read8Bytes(p) & target.read8Bytes(p));
        }
        int restBits = (int)(shorter.lengthInBits() % 64L);
        this.write8BytesInOrder(
                longWide,
                (this.readBitsAs8Bytes(longWide, restBits) << (64 - restBits))
                        & (target.readBitsAs8Bytes(longWide, restBits) << (64 - restBits)),
                restBits);
        return shorter.lengthInBits();
    }
    
    @Override
    public long or(BaseAccessor target)
    {
        BaseAccessor shorter = PrimitiveImplUtils.getShorterAccessor(this, target);
        long longWide = shorter.lengthInBits() / 64L;
        for (long p = 0; p < longWide; p += 64)
        {
            this.write8Bytes(p, this.read8Bytes(p) | target.read8Bytes(p));
        }
        int restBits = (int)(shorter.lengthInBits() % 64L);
        this.write8BytesInOrder(
                longWide,
                (this.readBitsAs8Bytes(longWide, restBits) << (64 - restBits))
                        | (target.readBitsAs8Bytes(longWide, restBits) << (64 - restBits)),
                restBits);
        return shorter.lengthInBits();
    }
    
    @Override
    public long xor(BaseAccessor target)
    {
        BaseAccessor shorter = PrimitiveImplUtils.getShorterAccessor(this, target);
        long longWide = shorter.lengthInBits() / 64L;
        for (long p = 0; p < longWide; p += 64)
        {
            this.write8Bytes(p, this.read8Bytes(p) ^ target.read8Bytes(p));
        }
        int restBits = (int)(shorter.lengthInBits() % 64L);
        this.write8BytesInOrder(
                longWide,
                (this.readBitsAs8Bytes(longWide, restBits) << (64 - restBits))
                        ^ (target.readBitsAs8Bytes(longWide, restBits) << (64 - restBits)),
                restBits);
        return shorter.lengthInBits();
    }
    
    @Override
    public void not()
    {
        if (startIndex == endIndex)
        {
            int i = getInt(array[startIndex]);
            i = PrimitiveImplUtils.putIntBits(~i, i, ELE_WIDE * 2 + startOffset, (int)lengthInBits);
            array[startIndex] = (short)(i);
        }
        else
        {
            if (endIndex - startIndex > 1)
            {
                for (int i = startIndex + 1; i <= endIndex - 1; i++)
                {
                    array[i] = (short)~array[i];
                }
            }
            int i = getInt(array[startIndex]);
            i = PrimitiveImplUtils.putIntBits(~i, i, ELE_WIDE * 2 + startOffset, ELE_WIDE
                    - startOffset);
            array[startIndex] = (short)(i);
            i = getInt(array[endIndex]);
            i = PrimitiveImplUtils.putIntBits(~i, i, ELE_WIDE * 2, endOffset + 1);
            array[endIndex] = (short)(i);
        }
    }
    
    @Override
    public void reverse()
    {
        if (lengthInBits <= 64)
        {
            long v = readBitsAs8Bytes(0L, (int)lengthInBits);
            v = Long.reverse(v);
            write8BytesInOrder(0L, v, (int)lengthInBits);
            return;
        }
        long haflLength = lengthInBits / 2L;
        int rest = (int)(haflLength % 32L);
        long longLen = haflLength - rest;
        for (long p = 0L; p < longLen; p += 32)
        {
            int head = read4Bytes(p);
            int tail = read4Bytes(lengthInBits - ((p + 1) * 32));
            head = Integer.reverse(head);
            tail = Integer.reverse(tail);
            write4Bytes(p, tail);
            write4Bytes(lengthInBits - ((p + 1) * 32), head);
        }
        if (rest > 0)
        {
            int bits = rest * 2;
            if (lengthInBits % 2 == 1)
            {
                bits++;
            }
            write8BytesInOrder(haflLength - rest,
                    Long.reverse(readBitsAs8Bytes(haflLength - rest, bits)), bits);
        }
    }
    
    @Override
    public void reverseInBytes()
    {
        if (lengthInBits % 8L != 0)
        {
            return;
        }
        long haflLength = lengthInBits / 2L;
        long longLen = haflLength;
        for (long p = 0L; p < longLen; p += 32)
        {
            int head = read4Bytes(p);
            int tail = read4Bytes(lengthInBits - ((p + 1) * 32));
            write4Bytes(p, tail);
            write4Bytes(lengthInBits - ((p + 1) * 32), head);
        }
    }
    
    @Override
    public void logicalLeft(long bits)
    {
        long restLength = lengthInBits - bits;
        partOf(bits, restLength).copy(partOf(0, restLength));
        partOf(restLength, bits).setBits(false);
    }
    
    @Override
    public void arithmeticLeft(long bits)
    {
        logicalLeft(bits);
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
        long restLength = lengthInBits - bits;
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
    
    @Override
    public int toBooleanArray(boolean[] dest, int startIndex)
    {
        // TODO Auto-generated method stub
        return 0;
    }
    
    @Override
    public long toByteArray(byte[] dest, int startIndex, int bitOffset)
    {
        // TODO Auto-generated method stub
        return 0;
    }
    
    @Override
    public long toShortArray(short[] dest, int startIndex, int bitOffset)
    {
        return copy(new ShortArrayBaseAccessorOld(dest, startIndex, bitOffset));
    }
    
    @Override
    public long toCharArray(char[] dest, int startIndex, int bitOffset)
    {
        // TODO Auto-generated method stub
        return 0;
    }
    
    @Override
    public long toIntArray(int[] dest, int startIndex, int bitOffset)
    {
        // TODO Auto-generated method stub
        return 0;
    }
    
    @Override
    public long toFloatArray(float[] dest, int startIndex, int bitOffset)
    {
        // TODO Auto-generated method stub
        return 0;
    }
    
    @Override
    public long toLongArray(long[] dest, int startIndex, int bitOffset)
    {
        // TODO Auto-generated method stub
        return 0;
    }
    
    @Override
    public long toDoubleArray(double[] dest, int startIndex, int bitOffset)
    {
        // TODO Auto-generated method stub
        return 0;
    }
}
