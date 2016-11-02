package com.cogician.quicker;

/**
 * <p>
 * This interface is used to random put bits into a bits block. The bits block
 * is considered as a continuous block of memory, which just like a primitive
 * type of which length greater than 64 bit(long/double) but less than or equal
 * to {@linkplain Integer#MAX_VALUE} bytes.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2015-10-18 11:46:12
 * @since 0.0.0
 */
public interface BitsWriter {

    /**
     * <p>
     * Puts specified bit value at specified position in bits, true is 1, false
     * is 0.
     * </p>
     *
     * @param posInBits
     *            specified position in bits, [0L, bits' bit length - 1L]
     * @param value
     *            written value
     * @throws IllegalArgumentException
     *             if specified bit position is negative
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     * @since 0.0.0
     */
    public void putBoolean(long posInBits, boolean value);

    /**
     * <p>
     * Puts specified bit value at first bit of specified position in bytes,
     * true is 1, false is 0.
     * </p>
     *
     * @param posInBytes
     *            specified position in bytes, [0, bits' byte length - 1]
     * @param value
     *            written value
     * @throws IllegalArgumentException
     *             if specified bit position is negative
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     * @since 0.0.0
     */
    public void putBoolean(int posInBytes, boolean value);

    /**
     * <p>
     * Puts specified byte value from specified position in bits, with
     * high-to-low (left-to-right, big-endian) orders.
     * </p>
     *
     * @param posInBits
     *            specified position in bits, [0L, bits' bit length - 8L]
     * @param value
     *            written value
     * @throws IllegalArgumentException
     *             if specified bit position is negative
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     * @since 0.0.0
     */
    public void putByte(long posInBits, byte value);

    /**
     * <p>
     * Puts specified byte value from specified position in bytes, with
     * high-to-low (left-to-right, big-endian) orders.
     * </p>
     *
     * @param posInBytes
     *            specified position in bytes, [0, bits' byte length - 1]
     * @param value
     *            written value
     * @throws IllegalArgumentException
     *             if specified bit position is negative
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     * @since 0.0.0
     */
    public void putByte(int posInBytes, byte value);

    /**
     * <p>
     * Puts specified short value from specified position in bits, with
     * high-to-low (left-to-right, big-endian) orders.
     * </p>
     *
     * @param posInBits
     *            specified position in bits, [0L, bits' bit length - 16L]
     * @param value
     *            written value
     * @throws IllegalArgumentException
     *             if specified bit position is negative
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     * @since 0.0.0
     */
    public void putShort(long posInBits, short value);

    /**
     * <p>
     * Puts specified short value from specified position in bytes, with
     * high-to-low (left-to-right, big-endian) orders.
     * </p>
     *
     * @param posInBytes
     *            specified position in bytes, [0, bits' byte length - 2]
     * @param value
     *            written value
     * @throws IllegalArgumentException
     *             if specified bit position is negative
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     * @since 0.0.0
     */
    public void putShort(int posInBytes, short value);

    /**
     * <p>
     * Puts specified char value from specified position in bits, with
     * high-to-low (left-to-right, big-endian) orders.
     * </p>
     *
     * @param posInBits
     *            specified position in bits, [0L, bits' bit length - 16L]
     * @param value
     *            written value
     * @throws IllegalArgumentException
     *             if specified bit position is negative
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     * @since 0.0.0
     */
    public void putChar(long posInBits, char value);

    /**
     * <p>
     * Puts specified char value from specified position in bytes, with
     * high-to-low (left-to-right, big-endian) orders.
     * </p>
     *
     * @param posInBytes
     *            specified position in bytes, [0, bits' byte length - 2]
     * @param value
     *            written value
     * @throws IllegalArgumentException
     *             if specified bit position is negative
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     * @since 0.0.0
     */
    public void putChar(int posInBytes, char value);

    /**
     * <p>
     * Puts specified int value from specified position in bits, with
     * high-to-low (left-to-right, big-endian) orders.
     * </p>
     *
     * @param posInBits
     *            specified position in bits, [0L, bits' bit length - 32L]
     * @param value
     *            written value
     * @throws IllegalArgumentException
     *             if specified bit position is negative
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     * @since 0.0.0
     */
    public void putInt(long posInBits, int value);

    /**
     * <p>
     * Puts specified int value from specified position in bytes, with
     * high-to-low (left-to-right, big-endian) orders.
     * </p>
     *
     * @param posInBytes
     *            specified position in bytes, [0, bits' byte length - 4]
     * @param value
     *            written value
     * @throws IllegalArgumentException
     *             if specified bit position is negative
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     * @since 0.0.0
     */
    public void putInt(int posInBytes, int value);

    /**
     * <p>
     * Puts specified float value as 32-bits-long value from specified position
     * in bits, with high-to-low (left-to-right, big-endian) orders.
     * </p>
     *
     * @param posInBits
     *            specified position in bits, [0L, bits' bit length - 32L]
     * @param value
     *            written value
     * @throws IllegalArgumentException
     *             if specified bit position is negative
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     * @since 0.0.0
     */
    public void putFloat(long posInBits, float value);

    /**
     * <p>
     * Puts specified float value as 4-bytes-long value from specified position
     * in bytes, with high-to-low (left-to-right, big-endian) orders.
     * </p>
     *
     * @param posInBytes
     *            specified position in bytes, [0, bits' byte length - 4]
     * @param value
     *            written value
     * @throws IllegalArgumentException
     *             if specified bit position is negative
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     * @since 0.0.0
     */
    public void putFloat(int posInBytes, float value);

    /**
     * <p>
     * Puts specified long value from specified position in bits, with
     * high-to-low (left-to-right, big-endian) orders.
     * </p>
     *
     * @param posInBits
     *            specified position in bits, [0L, bits' bit length - 64L]
     * @param value
     *            written value
     * @throws IllegalArgumentException
     *             if specified bit position is negative
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     * @since 0.0.0
     */
    public void putLong(long posInBits, long value);

    /**
     * <p>
     * Puts specified long value from specified position in bytes, with
     * high-to-low (left-to-right, big-endian) orders.
     * </p>
     *
     * @param posInBytes
     *            specified position in bytes, [0, bits' byte length - 8]
     * @param value
     *            written value
     * @throws IllegalArgumentException
     *             if specified bit position is negative
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     * @since 0.0.0
     */
    public void putLong(int posInBytes, long value);

    /**
     * <p>
     * Puts specified double value as 64-bits-long value from specified position
     * in bits, with high-to-low (left-to-right, big-endian) orders.
     * </p>
     *
     * @param posInBits
     *            specified position in bits, [0L, bits' bit length - 64L]
     * @param value
     *            written value
     * @throws IllegalArgumentException
     *             if specified bit position is negative
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     * @since 0.0.0
     */
    public void putDouble(long posInBits, double value);

    /**
     * <p>
     * Puts specified double value as 8-bytes-long value from specified position
     * in bytes, with high-to-low (left-to-right, big-endian) orders.
     * </p>
     *
     * @param posInBytes
     *            specified position in bytes, [0, bits' byte length - 8]
     * @param value
     *            written value
     * @throws IllegalArgumentException
     *             if specified bit position is negative
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     * @since 0.0.0
     */
    public void putDouble(int posInBytes, double value);

    /**
     * <p>
     * Puts specified byte value from specified position in bits, with
     * high-to-low (left-to-right, big-endian) orders. Differs from
     * {@linkplain #putByte(long, byte)}, this method stop putting when reach to
     * the bounds instead of throwing {@linkplain OutOfBoundsException}. The
     * actual number of put bits will be returned.
     * </p>
     *
     * @param posInBits
     *            specified position in bits, [0L, bits' bit length - 1L]
     * @param value
     *            written value
     * @return actual number of put bits
     * @since 0.0.0
     */
    public int putByteInBounds(long posInBits, byte value);

    /**
     * <p>
     * Puts specified byte value from specified position in bytes, with
     * high-to-low (left-to-right, big-endian) orders. Differs from
     * {@linkplain #putByte(int, byte)}, this method stop putting when reach to
     * the bounds instead of throwing {@linkplain OutOfBoundsException}. The
     * actual number of put bits will be returned.
     * </p>
     *
     * @param posInBytes
     *            specified position in bytes, [0, bits' byte length - 1]
     * @param value
     *            written value
     * @return actual number of put bits
     * @since 0.0.0
     */
    public int putByteInBounds(int posInBytes, byte value);

    /**
     * <p>
     * Puts specified short value from specified position in bits, with
     * high-to-low (left-to-right, big-endian) orders. Differs from
     * {@linkplain #putShort(long, byte)}, this method stop putting when reach
     * to the bounds instead of throwing {@linkplain OutOfBoundsException}. The
     * actual number of put bits will be returned.
     * </p>
     *
     * @param posInBits
     *            specified position in bits, [0L, bits' bit length - 1L]
     * @param value
     *            written value
     * @return actual number of put bits
     * @since 0.0.0
     */
    public int putShortInBounds(long posInBits, short value);

    /**
     * <p>
     * Puts specified short value from specified position in bytes, with
     * high-to-low (left-to-right, big-endian) orders. Differs from
     * {@linkplain #putShort(int, byte)}, this method stop putting when reach to
     * the bounds instead of throwing {@linkplain OutOfBoundsException}. The
     * actual number of put bits will be returned.
     * </p>
     *
     * @param posInBytes
     *            specified position in bytes, [0, bits' byte length - 1]
     * @param value
     *            written value
     * @return actual number of put bits
     * @since 0.0.0
     */
    public int putShortInBounds(int posInBytes, short value);

    /**
     * <p>
     * Puts specified char value from specified position in bits, with
     * high-to-low (left-to-right, big-endian) orders. Differs from
     * {@linkplain #putChar(long, byte)}, this method stop putting when reach to
     * the bounds instead of throwing {@linkplain OutOfBoundsException}. The
     * actual number of put bits will be returned.
     * </p>
     *
     * @param posInBits
     *            specified position in bits, [0L, bits' bit length - 1L]
     * @param value
     *            written value
     * @return actual number of put bits
     * @since 0.0.0
     */
    public int putCharInBounds(long posInBits, char value);

    /**
     * <p>
     * Puts specified char value from specified position in bytes, with
     * high-to-low (left-to-right, big-endian) orders. Differs from
     * {@linkplain #putChar(int, byte)}, this method stop putting when reach to
     * the bounds instead of throwing {@linkplain OutOfBoundsException}. The
     * actual number of put bits will be returned.
     * </p>
     *
     * @param posInBytes
     *            specified position in bytes, [0, bits' byte length - 1]
     * @param value
     *            written value
     * @return actual number of put bits
     * @since 0.0.0
     */
    public int putCharInBounds(int posInBytes, char value);

    /**
     * <p>
     * Puts specified int value from specified position in bits, with
     * high-to-low (left-to-right, big-endian) orders. Differs from
     * {@linkplain #putInt(long, byte)}, this method stop putting when reach to
     * the bounds instead of throwing {@linkplain OutOfBoundsException}. The
     * actual number of put bits will be returned.
     * </p>
     *
     * @param posInBits
     *            specified position in bits, [0L, bits' bit length - 1L]
     * @param value
     *            written value
     * @return actual number of put bits
     * @since 0.0.0
     */
    public int putIntInBounds(long posInBits, int value);

    /**
     * <p>
     * Puts specified int value from specified position in bytes, with
     * high-to-low (left-to-right, big-endian) orders. Differs from
     * {@linkplain #putInt(int, byte)}, this method stop putting when reach to
     * the bounds instead of throwing {@linkplain OutOfBoundsException}. The
     * actual number of put bits will be returned.
     * </p>
     *
     * @param posInBytes
     *            specified position in bytes, [0, bits' byte length - 1]
     * @param value
     *            written value
     * @return actual number of put bits
     * @since 0.0.0
     */
    public int putIntInBounds(int posInBytes, int value);

    /**
     * <p>
     * Puts specified float value as 32-bits-long value from specified position
     * in bits, with high-to-low (left-to-right, big-endian) orders. Differs
     * from {@linkplain #putFloat(long, byte)}, this method stop putting when
     * reach to the bounds instead of throwing {@linkplain OutOfBoundsException}
     * . The actual number of put bits will be returned.
     * </p>
     *
     * @param posInBits
     *            specified position in bits, [0L, bits' bit length - 1L]
     * @param value
     *            written value
     * @return actual number of put bits
     * @since 0.0.0
     */
    public int putFloatInBounds(long posInBits, float value);

    /**
     * <p>
     * Puts specified float value as 8-bytes-long value from specified position
     * in bytes, with high-to-low (left-to-right, big-endian) orders. Differs
     * from {@linkplain #putFloat(int, byte)}, this method stop putting when
     * reach to the bounds instead of throwing {@linkplain OutOfBoundsException}
     * . The actual number of put bits will be returned.
     * </p>
     *
     * @param posInBytes
     *            specified position in bytes, [0, bits' byte length - 1]
     * @param value
     *            written value
     * @return actual number of put bits
     * @since 0.0.0
     */
    public int putFloatInBounds(int posInBytes, float value);

    /**
     * <p>
     * Puts specified long value from specified position in bits, with
     * high-to-low (left-to-right, big-endian) orders. Differs from
     * {@linkplain #putLong(long, byte)}, this method stop putting when reach to
     * the bounds instead of throwing {@linkplain OutOfBoundsException}. The
     * actual number of put bits will be returned.
     * </p>
     *
     * @param posInBits
     *            specified position in bits, [0L, bits' bit length - 1L]
     * @param value
     *            written value
     * @return actual number of put bits
     * @since 0.0.0
     */
    public int putLongInBounds(long posInBits, long value);

    /**
     * <p>
     * Puts specified long value from specified position in bytes, with
     * high-to-low (left-to-right, big-endian) orders. Differs from
     * {@linkplain #putLong(int, byte)}, this method stop putting when reach to
     * the bounds instead of throwing {@linkplain OutOfBoundsException}. The
     * actual number of put bits will be returned.
     * </p>
     *
     * @param posInBytes
     *            specified position in bytes, [0, bits' byte length - 1]
     * @param value
     *            written value
     * @return actual number of put bits
     * @since 0.0.0
     */
    public int putLongInBounds(int posInBytes, long value);

    /**
     * <p>
     * Puts specified double value as 64-bits-long value from specified position
     * in bits, with high-to-low (left-to-right, big-endian) orders. Differs
     * from {@linkplain #putDouble(long, byte)}, this method stop putting when
     * reach to the bounds instead of throwing {@linkplain OutOfBoundsException}
     * . The actual number of put bits will be returned.
     * </p>
     *
     * @param posInBits
     *            specified position in bits, [0L, bits' bit length - 1L]
     * @param value
     *            written value
     * @return actual number of put bits
     * @since 0.0.0
     */
    public int putDoubleInBounds(long posInBits, double value);

    /**
     * <p>
     * Puts specified double value as 8-bytes-long value from specified position
     * in bytes, with high-to-low (left-to-right, big-endian) orders. Differs
     * from {@linkplain #putDouble(int, byte)}, this method stop putting when
     * reach to the bounds instead of throwing {@linkplain OutOfBoundsException}
     * . The actual number of put bits will be returned.
     * </p>
     *
     * @param posInBytes
     *            specified position in bytes, [0, bits' byte length - 1]
     * @param value
     *            written value
     * @return actual number of put bits
     * @since 0.0.0
     */
    public int putDoubleInBounds(int posInBytes, double value);

    /**
     * <p>
     * Puts specified number of bits from specified position in bits, with
     * high-to-low (left-to-right, big-endian) orders.
     * </p>
     *
     * @param posInBits
     *            specified position in bits, [0L, bits' bit length - specified
     *            number of bits to be put]
     * @param value
     *            written value
     * @param bits
     *            specified number of bits to be put, [1, 8]
     * @since 0.0.0
     */
    public void putBits(long posInBits, byte value, int bits);

    /**
     * <p>
     * Puts specified number of bits from specified position in bytes, with
     * high-to-low (left-to-right, big-endian) orders.
     * </p>
     *
     * @param posInBytes
     *            specified position in bytes, if turn the position in bytes to
     *            in bits, it should be: [0L, bits' bit length - specified
     *            number of bits to be put]
     * @param value
     *            written value
     * @param bits
     *            specified number of bits to be put, [1, 8]
     * @since 0.0.0
     */
    public void putBits(int posInBytes, byte value, int bits);

    /**
     * <p>
     * Puts specified number of bits from specified position in bits, with
     * high-to-low (left-to-right, big-endian) orders.
     * </p>
     *
     * @param posInBits
     *            specified position in bits, [0L, bits' bit length - specified
     *            number of bits to be put]
     * @param value
     *            written value
     * @param bits
     *            specified number of bits to be put, [1, 16]
     * @since 0.0.0
     */
    public void putBits(long posInBits, short value, int bits);

    /**
     * <p>
     * Puts specified number of bits from specified position in bytes, with
     * high-to-low (left-to-right, big-endian) orders.
     * </p>
     *
     * @param posInBytes
     *            specified position in bytes, if turn the position in bytes to
     *            in bits, it should be: [0L, bits' bit length - specified
     *            number of bits to be put]
     * @param value
     *            written value
     * @param bits
     *            specified number of bits to be put, [1, 16]
     * @since 0.0.0
     */
    public void putBits(int posInBytes, short value, int bits);

    /**
     * <p>
     * Puts specified number of bits from specified position in bits, with
     * high-to-low (left-to-right, big-endian) orders.
     * </p>
     *
     * @param posInBits
     *            specified position in bits, [0L, bits' bit length - specified
     *            number of bits to be put]
     * @param value
     *            written value
     * @param bits
     *            specified number of bits to be put, [1, 16]
     * @since 0.0.0
     */
    public void putBits(long posInBits, char value, int bits);

    /**
     * <p>
     * Puts specified number of bits from specified position in bytes, with
     * high-to-low (left-to-right, big-endian) orders.
     * </p>
     *
     * @param posInBytes
     *            specified position in bytes, if turn the position in bytes to
     *            in bits, it should be: [0L, bits' bit length - specified
     *            number of bits to be put]
     * @param value
     *            written value
     * @param bits
     *            specified number of bits to be put, [1, 16]
     * @since 0.0.0
     */
    public void putBits(int posInBytes, char value, int bits);

    /**
     * <p>
     * Puts specified number of bits from specified position in bits, with
     * high-to-low (left-to-right, big-endian) orders.
     * </p>
     *
     * @param posInBits
     *            specified position in bits, [0L, bits' bit length - specified
     *            number of bits to be put]
     * @param value
     *            written value
     * @param bits
     *            specified number of bits to be put, [1, 32]
     * @since 0.0.0
     */
    public void putBits(long posInBits, int value, int bits);

    /**
     * <p>
     * Puts specified number of bits from specified position in bytes, with
     * high-to-low (left-to-right, big-endian) orders.
     * </p>
     *
     * @param posInBytes
     *            specified position in bytes, if turn the position in bytes to
     *            in bits, it should be: [0L, bits' bit length - specified
     *            number of bits to be put]
     * @param value
     *            written value
     * @param bits
     *            specified number of bits to be put, [1, 32]
     * @since 0.0.0
     */
    public void putBits(int posInBytes, int value, int bits);

    /**
     * <p>
     * Puts specified number of bits from specified position in bits, with
     * high-to-low (left-to-right, big-endian) orders.
     * </p>
     *
     * @param posInBits
     *            specified position in bits, [0L, bits' bit length - specified
     *            number of bits to be put]
     * @param value
     *            written value, as 32-bits-long value
     * @param bits
     *            specified number of bits to be put, [1, 32]
     * @since 0.0.0
     */
    public void putBits(long posInBits, float value, int bits);

    /**
     * <p>
     * Puts specified number of bits from specified position in bytes, with
     * high-to-low (left-to-right, big-endian) orders.
     * </p>
     *
     * @param posInBytes
     *            specified position in bytes, if turn the position in bytes to
     *            in bits, it should be: [0L, bits' bit length - specified
     *            number of bits to be put]
     * @param value
     *            written value, as 4-bytes-long value
     * @param bits
     *            specified number of bits to be put, [1, 32]
     * @since 0.0.0
     */
    public void putBits(int posInBytes, float value, int bits);

    /**
     * <p>
     * Puts specified number of bits from specified position in bits, with
     * high-to-low (left-to-right, big-endian) orders.
     * </p>
     *
     * @param posInBits
     *            specified position in bits, [0L, bits' bit length - specified
     *            number of bits to be put]
     * @param value
     *            written value
     * @param bits
     *            specified number of bits to be put, [1, 64]
     * @since 0.0.0
     */
    public void putBits(long posInBits, long value, int bits);

    /**
     * <p>
     * Puts specified number of bits from specified position in bytes, with
     * high-to-low (left-to-right, big-endian) orders.
     * </p>
     *
     * @param posInBytes
     *            specified position in bytes, if turn the position in bytes to
     *            in bits, it should be: [0L, bits' bit length - specified
     *            number of bits to be put]
     * @param value
     *            written value
     * @param bits
     *            specified number of bits to be put, [1, 64]
     * @since 0.0.0
     */
    public void putBits(int posInBytes, long value, int bits);

    /**
     * <p>
     * Puts specified number of bits from specified position in bits, with
     * high-to-low (left-to-right, big-endian) orders.
     * </p>
     *
     * @param posInBits
     *            specified position in bits, [0L, bits' bit length - specified
     *            number of bits to be put]
     * @param value
     *            written value, as 64-bits-long value
     * @param bits
     *            specified number of bits to be put, [1, 64]
     * @since 0.0.0
     */
    public void putBits(long posInBits, double value, int bits);

    /**
     * <p>
     * Puts specified number of bits from specified position in bytes, with
     * high-to-low (left-to-right, big-endian) orders.
     * </p>
     *
     * @param posInBytes
     *            specified position in bytes, if turn the position in bytes to
     *            in bits, it should be: [0L, bits' bit length - specified
     *            number of bits to be put]
     * @param value
     *            written value, as 8-bytes-long value
     * @param bits
     *            specified number of bits to be put, [1, 64]
     * @since 0.0.0
     */
    public void putBits(int posInBytes, double value, int bits);
}
