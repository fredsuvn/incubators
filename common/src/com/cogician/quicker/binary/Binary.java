package com.cogician.quicker.binary;

import java.io.EOFException;
import java.io.IOException;

import com.cogician.quicker.Checker;
import com.cogician.quicker.Quicker;
import com.cogician.quicker.QuickerUniform;
import com.sun.istack.internal.Nullable;

/**
 * <p>
 * This interface represent a block of binary data, accessed in bytes. It can be the data in the memory, or maps a file,
 * or other data source.
 * </p>
 * <p>
 * There are two kinds of method to get primitive value: getXxx() and getUnsignedXxx(). The former reserves sign of
 * value and the value will be sign-extend when it cast to a wider type, and the latter will zero-extend.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-03-16T11:01:34+08:00
 * @since 0.0.0, 2016-03-16T11:01:34+08:00
 */
public interface Binary extends ByteOrderSensitive {

    /**
     * <p>
     * Wraps a byte array into a {@linkplain ByteArrayBinary}, big-endian. {@linkplain #wrappedArray()} of returned
     * binary will return given wrapped array.
     * </p>
     * 
     * @param array
     *            given wrapped array.
     * @return a {@linkplain ByteArrayBinary}
     * @throws NullPointerException
     *             if given wrapped array is null
     * @since 0.0.0
     */
    public static Binary wrap(byte[] array) throws NullPointerException {
        return new ByteArrayBinary(array);
    }

    /**
     * <p>
     * Wraps a byte array between specified start index inclusive and end index exclusive into a
     * {@linkplain ByteArrayBinary}. {@linkplain #wrappedArray()} of returned binary will return given wrapped array. If
     * given byte order processor is null, use default {@linkplain ByteOrderProcessor#BIG_ENDIAN}.
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
    public static Binary wrap(byte[] array, int startIndex, int endIndex, ByteOrderProcessor orderProcessor)
            throws NullPointerException, IndexOutOfBoundsException {
        return new ByteArrayBinary(array, startIndex, endIndex, orderProcessor);
    }

    /**
     * <p>
     * Allocates a binary of specified length, big-endian.
     * </p>
     * 
     * @param length
     *            specified length
     * @return a binary of specified length
     * @throws IllegalArgumentException
     *             if specified length is negative
     * @since 0.0.0
     */
    public static Binary alloc(long length) throws IllegalArgumentException {
        return new HeapBinary(length);
    }

    /**
     * <p>
     * Allocates a binary of specified length with specified byte order processor. If given byte order processor is
     * null, use default {@linkplain ByteOrderProcessor#BIG_ENDIAN}.
     * </p>
     * 
     * @param length
     *            specified length
     * @param orderProcessor
     *            specified byte order processor
     * @return a binary of specified length with specified byte order processor
     * @throws IllegalArgumentException
     *             if specified length is negative
     * @since 0.0.0
     */
    public static Binary alloc(long length, ByteOrderProcessor orderProcessor) throws IllegalArgumentException {
        return new HeapBinary(length, orderProcessor);
    }

    /**
     * <p>
     * Returns length of this binary.
     * </p>
     * 
     * @return length of this binary
     * @since 0.0.0
     */
    public long length();

    /**
     * <p>
     * Gets byte at specified index.
     * </p>
     * 
     * @param index
     *            specified index
     * @return byte at specified index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @throws BinaryException
     *             if any problem occurs when getting
     * @since 0.0.0
     */
    public byte getByte(long index) throws IndexOutOfBoundsException, BinaryException;

    /**
     * <p>
     * Gets unsigned byte at specified index.
     * </p>
     * 
     * @param index
     *            specified index
     * @return unsigned byte at specified index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @throws BinaryException
     *             if any problem occurs when getting
     * @since 0.0.0
     */
    default int getUnsignedByte(long index) throws IndexOutOfBoundsException, BinaryException {
        return BytesUtil.toUnsignedByte(getByte(index));
    }

    /**
     * <p>
     * Gets 2-bytes short at specified index in current byte order.
     * </p>
     * 
     * @param index
     *            specified index
     * @return short at specified index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds or it leads to read out of bounds
     * @throws BinaryException
     *             if any problem occurs when getting
     * @since 0.0.0
     */
    public short getShort(long index) throws IndexOutOfBoundsException, BinaryException;

    /**
     * <p>
     * Gets unsigned 2-bytes short at specified index in current byte order.
     * </p>
     * 
     * @param index
     *            specified index
     * @return unsigned short at specified index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds or it leads to read out of bounds
     * @throws BinaryException
     *             if any problem occurs when getting
     * @since 0.0.0
     */
    default int getUnsignedShort(long index) throws IndexOutOfBoundsException, BinaryException {
        return BytesUtil.toUnsignedShort(getShort(index));
    }

    /**
     * <p>
     * Gets 2-bytes char at specified index in current byte order.
     * </p>
     * 
     * @param index
     *            specified index
     * @return char at specified index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds or it leads to read out of bounds
     * @throws BinaryException
     *             if any problem occurs when getting
     * @since 0.0.0
     */
    public char getChar(long index) throws IndexOutOfBoundsException, BinaryException;

    /**
     * <p>
     * Gets 3-bytes medium at specified index in current byte order.
     * </p>
     * 
     * @param index
     *            specified index
     * @return medium at specified index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds or it leads to read out of bounds
     * @throws BinaryException
     *             if any problem occurs when getting
     * @since 0.0.0
     */
    public int getMedium(long index) throws IndexOutOfBoundsException, BinaryException;

    /**
     * <p>
     * Gets unsigned 3-bytes medium at specified index in current byte order.
     * </p>
     * 
     * @param index
     *            specified index
     * @return unsigned medium at specified index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds or it leads to read out of bounds
     * @throws BinaryException
     *             if any problem occurs when getting
     * @since 0.0.0
     */
    default int getUnsignedMedium(long index) throws IndexOutOfBoundsException, BinaryException {
        return BytesUtil.toUnsignedMedium(getMedium(index));
    }

    /**
     * <p>
     * Gets 4-bytes int at specified index in current byte order.
     * </p>
     * 
     * @param index
     *            specified index
     * @return int at specified index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds or it leads to read out of bounds
     * @throws BinaryException
     *             if any problem occurs when getting
     * @since 0.0.0
     */
    public int getInt(long index) throws IndexOutOfBoundsException, BinaryException;

    /**
     * <p>
     * Gets unsigned 4-bytes int at specified index in current byte order.
     * </p>
     * 
     * @param index
     *            specified index
     * @return unsigned int at specified index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds or it leads to read out of bounds
     * @throws BinaryException
     *             if any problem occurs when getting
     * @since 0.0.0
     */
    default long getUnsignedInt(long index) throws IndexOutOfBoundsException, BinaryException {
        return BytesUtil.toUnsignedInt(getInt(index));
    }

    /**
     * <p>
     * Gets 8-bytes long at specified index in current byte order.
     * </p>
     * 
     * @param index
     *            specified index
     * @return long at specified index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds or it leads to read out of bounds
     * @throws BinaryException
     *             if any problem occurs when getting
     * @since 0.0.0
     */
    public long getLong(long index) throws IndexOutOfBoundsException, BinaryException;

    /**
     * <p>
     * Gets 4-bytes float at specified index in current byte order.
     * </p>
     * 
     * @param index
     *            specified index
     * @return float at specified index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds or it leads to read out of bounds
     * @throws BinaryException
     *             if any problem occurs when getting
     * @since 0.0.0
     */
    public float getFloat(long index) throws IndexOutOfBoundsException, BinaryException;

    /**
     * <p>
     * Gets 8-bytes double at specified index in current byte order.
     * </p>
     * 
     * @param index
     *            specified index
     * @return double at specified index
     * @throws IndexOutOfBoundsException
     *             if index out of bounds or it leads to read out of bounds
     * @throws BinaryException
     *             if any problem occurs when getting
     * @since 0.0.0
     */
    public double getDouble(long index) throws IndexOutOfBoundsException, BinaryException;

    /**
     * <p>
     * Gets specified-bytes value at specified index in current byte order and sign-extends it.
     * </p>
     * 
     * @param index
     *            specified index
     * @param bytesNum
     *            number of specified bytes, [1, 8]
     * @return specified-bytes value
     * @throws IllegalArgumentException
     *             if number of specified bytes is not in [1, 8]
     * @throws IndexOutOfBoundsException
     *             if specified index out of bounds or it leads to read out of bounds
     * @throws BinaryException
     *             if any problem occurs when getting
     * @since 0.0.0
     */
    public long getPrimitive(long index, int bytesNum)
            throws IllegalArgumentException, IndexOutOfBoundsException, BinaryException;

    /**
     * <p>
     * Gets specified-bytes value at specified index in current byte order and zero-extends it.
     * </p>
     * 
     * @param index
     *            specified index
     * @param bytesNum
     *            number of specified bytes, [1, 8]
     * @return unsigned specified-bytes value
     * @throws IllegalArgumentException
     *             if number of specified bytes is not in [1, 8]
     * @throws IndexOutOfBoundsException
     *             if specified index out of bounds or it leads to read out of bounds
     * @throws BinaryException
     *             if any problem occurs when getting
     * @since 0.0.0
     */
    default long getUnsignedPrimitive(long index, int bytesNum)
            throws IllegalArgumentException, IndexOutOfBoundsException, BinaryException {
        return BytesUtil.toUnsignedBytes(getPrimitive(index, bytesNum), bytesNum);
    }

    /**
     * <p>
     * Gets a byte as ascii character at specified index.
     * </p>
     * 
     * @param index
     *            specified index
     * @return a byte as ascii character
     * @throws IndexOutOfBoundsException
     *             if index out of bounds or it leads to read out of bounds
     * @throws BinaryException
     *             if any problem occurs when getting
     * @since 0.0.0
     */
    default char getASCII(long index) throws IndexOutOfBoundsException, BinaryException {
        return (char)getUnsignedByte(index);
    }

    /**
     * <p>
     * Gets a byte as hex string at specified index, the string length is fixed 2.
     * </p>
     * 
     * @param index
     *            specified index
     * @return a byte as hex string
     * @throws IndexOutOfBoundsException
     *             if index out of bounds or it leads to read out of bounds
     * @throws BinaryException
     *             if any problem occurs when getting
     * @since 0.0.0
     */
    default String getHex(long index) throws EOFException, IOException {
        return Quicker.leftPad(Integer.toHexString(getUnsignedByte(index)), 2, "0").toUpperCase(QuickerUniform.LOCALE);
    }

    /**
     * <p>
     * Gets binary of specified length at specified index.
     * </p>
     * 
     * @param index
     *            specified index
     * @param length
     *            specified length
     * @return binary of specified length at specified index
     * @throws IndexOutOfBoundsException
     *             if specified index out of bounds or it leads to read out of bounds
     * @throws IllegalArgumentException
     *             if specified length is negative
     * @throws BinaryException
     *             if any problem occurs when getting
     * @since 0.0.0
     */
    public Binary getBinary(long index, long length)
            throws IndexOutOfBoundsException, IllegalArgumentException, BinaryException;

    /**
     * <p>
     * Writes low-byte of given value at specified index in current byte order.
     * </p>
     * 
     * @param index
     *            specified index
     * @param value
     *            given value
     * @throws IndexOutOfBoundsException
     *             if specified index out of bounds or it leads to write out of bounds
     * @throws BinaryException
     *             if any problem occurs when putting
     * @since 0.0.0
     */
    public void setByte(long index, int value) throws IndexOutOfBoundsException, BinaryException;

    /**
     * <p>
     * Writes low-2-bytes of given value at specified index in current byte order.
     * </p>
     * 
     * @param index
     *            specified index
     * @param value
     *            given value
     * @throws IndexOutOfBoundsException
     *             if specified index out of bounds or it leads to write out of bounds
     * @throws BinaryException
     *             if any problem occurs when putting
     * @since 0.0.0
     */
    public void setShort(long index, int value) throws IndexOutOfBoundsException, BinaryException;

    /**
     * <p>
     * Writes low-2-bytes of given value at specified index in current byte order.
     * </p>
     * 
     * @param index
     *            specified index
     * @param value
     *            given value
     * @throws IndexOutOfBoundsException
     *             if specified index out of bounds or it leads to write out of bounds
     * @throws BinaryException
     *             if any problem occurs when putting
     * @since 0.0.0
     */
    public void setChar(long index, int value) throws IndexOutOfBoundsException, BinaryException;

    /**
     * <p>
     * Writes low-3-bytes of given value at specified index in current byte order.
     * </p>
     * 
     * @param index
     *            specified index
     * @param value
     *            given value
     * @throws IndexOutOfBoundsException
     *             if specified index out of bounds or it leads to write out of bounds
     * @throws BinaryException
     *             if any problem occurs when putting
     * @since 0.0.0
     */
    public void setMedium(long index, int value) throws IndexOutOfBoundsException, BinaryException;

    /**
     * <p>
     * Writes 4-bytes of given value at specified index in current byte order.
     * </p>
     * 
     * @param index
     *            specified index
     * @param value
     *            given value
     * @throws IndexOutOfBoundsException
     *             if specified index out of bounds or it leads to write out of bounds
     * @throws BinaryException
     *             if any problem occurs when putting
     * @since 0.0.0
     */
    public void setInt(long index, int value) throws IndexOutOfBoundsException, BinaryException;

    /**
     * <p>
     * Writes 8-bytes of given value at specified index in current byte order.
     * </p>
     * 
     * @param index
     *            specified index
     * @param value
     *            given value
     * @throws IndexOutOfBoundsException
     *             if specified index out of bounds or it leads to write out of bounds
     * @throws BinaryException
     *             if any problem occurs when putting
     * @since 0.0.0
     */
    public void setLong(long index, long value) throws IndexOutOfBoundsException, BinaryException;

    /**
     * <p>
     * Writes 4-bytes of given value at specified index in current byte order.
     * </p>
     * 
     * @param index
     *            specified index
     * @param value
     *            given value
     * @throws IndexOutOfBoundsException
     *             if specified index out of bounds or it leads to write out of bounds
     * @throws BinaryException
     *             if any problem occurs when putting
     * @since 0.0.0
     */
    public void setFloat(long index, float value) throws IndexOutOfBoundsException, BinaryException;

    /**
     * <p>
     * Writes 8-bytes of given value at specified index in current byte order.
     * </p>
     * 
     * @param index
     *            specified index
     * @param value
     *            given value
     * @throws IndexOutOfBoundsException
     *             if specified index out of bounds or it leads to write out of bounds
     * @throws BinaryException
     *             if any problem occurs when putting
     * @since 0.0.0
     */
    public void setDouble(long index, double value) throws IndexOutOfBoundsException, BinaryException;

    /**
     * <p>
     * Writes low-specified-bytes of given value at specified index in current byte order.
     * </p>
     * 
     * @param index
     *            specified index
     * @param value
     *            given value
     * @param bytesNum
     *            number of specified bytes, [1, 8]
     * @throws IllegalArgumentException
     *             if number of specified bytes is not in [1, 8]
     * @throws IndexOutOfBoundsException
     *             if specified index out of bounds or it leads to write out of bounds
     * @throws BinaryException
     *             if any problem occurs when writing
     * @since 0.0.0
     */
    public void setPrimitive(long index, long value, int bytesNum)
            throws IllegalArgumentException, IndexOutOfBoundsException, BinaryException;

    /**
     * <p>
     * Writes a byte as ascii at specified index.
     * </p>
     * 
     * @param index
     *            specified index
     * @param value
     *            given ascii value
     * @throws IndexOutOfBoundsException
     *             if specified index out of bounds or it leads to write out of bounds
     * @throws BinaryException
     *             if any problem occurs when putting
     * @since 0.0.0
     */
    default void setASCII(long index, char value) throws IndexOutOfBoundsException, BinaryException {
        setByte(index, value);
    }

    /**
     * <p>
     * Writes given binary at specified index in current byte order.
     * </p>
     * 
     * @param index
     *            specified index
     * @param value
     *            given binary
     * @throws IndexOutOfBoundsException
     *             if specified index out of bounds or it leads to write out of bounds
     * @throws NullPointerException
     *             if given binary is null
     * @throws BinaryException
     *             if any problem occurs when read given binary
     * @throws BinaryException
     *             if any problem occurs when writing
     * @since 0.0.0
     */
    default void setBinary(long index, Binary value)
            throws IndexOutOfBoundsException, NullPointerException, BinaryException, BinaryException {
        Quicker.require(value).copy(0, this, index, value.length());
    }

    /**
     * <p>
     * Returns an inputer which can input data from specified index inclusive to end of this binary. This binary shares
     * data with returned inputer and any operation will reflect to returned inputer, and vice-versa.
     * </p>
     * <p>
     * Note the available length of returned inputer is specified at beginning, if length of this binary is changed,
     * returned inputer will be invalid.
     * </p>
     * 
     * @param index
     *            specified index
     * @return an inputer which can input data from specified index
     * @throws IndexOutOfBoundsException
     *             if specified index out of bounds
     * @since 0.0.0
     */
    default Inputer getInputer(long index) throws IndexOutOfBoundsException {
        return new BinaryInputer(this, index);
    }

    /**
     * <p>
     * Returns an inputer which can input data from specified start index inclusive to end index exclusive of this
     * binary. This binary shares data with returned inputer and any operation will reflect to returned inputer, and
     * vice-versa.
     * </p>
     * <p>
     * Note the available length of returned inputer is specified at beginning, if length of this binary is changed,
     * returned inputer will be invalid.
     * </p>
     * 
     * @param startIndex
     *            specified start index inclusive
     * @param endIndex
     *            specified end index exclusive
     * @return an inputer which can input data from specified start index inclusive to end index exclusive
     * @throws IndexOutOfBoundsException
     *             if specified index out of bounds
     * @since 0.0.0
     */
    default Inputer getInputer(long startIndex, long endIndex) throws IndexOutOfBoundsException {
        return new BinaryInputer(this, startIndex, endIndex);
    }

    /**
     * <p>
     * Returns an outputer which can output data from specified index to end of this binary. This binary shares data
     * with returned outputer and any operation will reflect to returned outputer, and vice-versa.
     * </p>
     * <p>
     * Note the available length of returned outputer is specified at beginning, if length of this binary is changed,
     * returned outputer will be invalid.
     * </p>
     * 
     * @param index
     *            specified index
     * @return an outputer which can output data from specified index
     * @throws IndexOutOfBoundsException
     *             if specified index out of bounds
     * @since 0.0.0
     */
    default Outputer getOutputer(long index) throws IndexOutOfBoundsException {
        return new BinaryOutputer(this, index);
    }

    /**
     * <p>
     * Returns an outputer which can output data from specified start index inclusive to end index exclusive of this
     * binary. This binary shares data with returned outputer and any operation will reflect to returned outputer, and
     * vice-versa.
     * </p>
     * <p>
     * Note the available length of returned outputer is specified at beginning, if length of this binary is changed,
     * returned outputer will be invalid.
     * </p>
     * 
     * @param startIndex
     *            specified start index inclusive
     * @param endIndex
     *            specified end index exclusive
     * @return an outputer which can output data from specified index
     * @throws IndexOutOfBoundsException
     *             if specified index out of bounds
     * @since 0.0.0
     */
    default Outputer getOutputer(long startIndex, long endIndex) throws IndexOutOfBoundsException {
        return new BinaryOutputer(this, startIndex, endIndex);
    }

    /**
     * <p>
     * Returns a sub-binary of this binary. This binary shares data with returned sub-binary and any operation will
     * reflect to returned sub-binary, and vice-versa.
     * </p>
     * 
     * @param from
     *            from index inclusive
     * @param to
     *            to index exclusive
     * @return a sub-binary of this binary
     * @throws IndexOutOfBoundsException
     *             if indexes out of bounds
     * @since 0.0.0
     */
    default Binary subBinary(long from, long to) throws IndexOutOfBoundsException {
        return new SubBinary(this, from, to);
    }

    /**
     * <p>
     * Copies data from this binary to specified binary.
     * </p>
     * 
     * @param srcIndex
     *            start index of this binary to copy
     * @param dest
     *            specified destination binary
     * @param destIndex
     *            start index of destination binary to copy
     * @param length
     *            copied data length
     * @throws NullPointerException
     *             if specified destination binary is null
     * @throws IndexOutOfBoundsException
     *             if indexes out of bounds or lead to copy out of bounds
     * @throws IllegalArgumentException
     *             if length is negative
     * @throws BinaryException
     *             if any problem occurs when read given binary
     * @throws BinaryException
     *             if any problem occurs when writing
     * @since 0.0.0
     */
    default void copy(long srcIndex, Binary dest, long destIndex, long length) throws NullPointerException,
            IndexOutOfBoundsException, IllegalArgumentException, BinaryException, BinaryException {
        Checker.checkNull(dest);
        Checker.checkLength(length);
        Checker.checkRangeIndexes(srcIndex, srcIndex + length, length());
        Checker.checkRangeIndexes(destIndex, destIndex + length, dest.length());
        for (long i = 0; i < length; i++) {
            dest.setByte(destIndex + i, getByte(srcIndex + i));
        }
    }

    /**
     * <p>
     * If this instance has a backed byte array (such as from {@linkplain #wrap(byte[])}), return it, else return null.
     * </p>
     * 
     * @return backed byte array if is has or null
     * @since 0.0.0
     */
    public @Nullable byte[] wrappedArray();
}
