package com.cogician.quicker;

/**
 * <p>
 * This interface is used to random get bits from a bits block. The bits block
 * is considered as a continuous block of memory, which just like a primitive
 * type of which length greater than 64 bit(long/double) but less than or equal
 * to {@linkplain Integer#MAX_VALUE} bytes.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2015-05-07 14:02:48
 * @since 0.0.0
 */
public interface BitsReader {

    /**
     * <p>
     * Gets bit value at specified position in bits, true is 1, false is 0.
     * </p>
     *
     * @param posInBits
     *            specified position in bits, [0L, bits' bit length - 1L]
     * @return bit value at specified position in bits, true is 1, false is 0
     * @throws IllegalArgumentException
     *             if specified bit position is negative
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     * @since 0.0.0
     */
    public boolean getBoolean(long posInBits);

    /**
     * <p>
     * Gets bit value at first bit of specified position in bytes, true is 1,
     * false is 0.
     * </p>
     *
     * @param posInBytes
     *            specified position in bytes, [0, bits' byte length - 1]
     * @return bit value at first bit of specified position in bytes, true is 1,
     *         false is 0
     * @throws IllegalArgumentException
     *             if specified bit position is negative
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     * @since 0.0.0
     */
    public boolean getBoolean(int posInBytes);

    /**
     * <p>
     * Gets byte value of next 8 bits from specified position in bits.
     * </p>
     *
     * @param posInBits
     *            specified position in bits, [0L, bits' bit length - 8L]
     * @return byte value of next 8 bits from specified position in bits
     * @throws IllegalArgumentException
     *             if specified bit position is negative
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     * @since 0.0.0
     */
    public byte getByte(long posInBits);

    /**
     * <p>
     * Gets byte value of next 1 bytes from specified position in bytes.
     * </p>
     *
     * @param posInBytes
     *            specified position in bytes, [0, bits' byte length - 1]
     * @return byte value of next 1 bytes from specified position in bytes
     * @throws IllegalArgumentException
     *             if specified bit position is negative
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     * @since 0.0.0
     */
    public byte getByte(int posInBytes);

    /**
     * <p>
     * Gets unsigned byte value of next 8 bits as int type from specified
     * position in bits. The higher 24 orders will be filled 0.
     * </p>
     *
     * @param posInBits
     *            specified position in bits, [0L, bits' bit length - 8L]
     * @return unsigned byte value of next 8 bits as int type from specified
     *         position in bits
     * @throws IllegalArgumentException
     *             if specified bit position is negative
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     * @since 0.0.0
     */
    public int getUnsignedByte(long posInBits);

    /**
     * <p>
     * Gets unsigned byte value of next 1 bytes as int type from specified
     * position in bytes. The higher 24 orders will be filled 0.
     * </p>
     *
     * @param posInBytes
     *            specified position in bytes, [0, bits' byte length - 1]
     * @return unsigned byte value of next 1 bytes as int type from specified
     *         position in bytes
     * @throws IllegalArgumentException
     *             if specified bit position is negative
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     * @since 0.0.0
     */
    public int getUnsignedByte(int posInBytes);

    /**
     * <p>
     * Gets unsigned byte value of next 8 bits as long type from specified
     * position in bits. The higher 56 orders will be filled 0.
     * </p>
     *
     * @param posInBits
     *            specified position in bits, [0L, bits' bit length - 8L]
     * @return unsigned byte value of next 8 bits as long type from specified
     *         position in bits
     * @throws IllegalArgumentException
     *             if specified bit position is negative
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     * @since 0.0.0
     */
    public long getLongUnsignedByte(long posInBits);

    /**
     * <p>
     * Gets unsigned byte value of next 1 bytes as long type from specified
     * position in bytes. The higher 56 orders will be filled 0.
     * </p>
     *
     * @param posInBytes
     *            specified position in bytes, [0, bits' byte length - 1]
     * @return unsigned byte value of next 1 bytes as long type from specified
     *         position in bytes
     * @throws IllegalArgumentException
     *             if specified bit position is negative
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     * @since 0.0.0
     */
    public long getLongUnsignedByte(int posInBytes);

    /**
     * <p>
     * Gets short value of next 16 bits from specified position in bits.
     * </p>
     *
     * @param posInBits
     *            specified position in bits, [0L, bits' bit length - 16L]
     * @return short value of next 16 bits from specified position in bits
     * @throws IllegalArgumentException
     *             if specified bit position is negative
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     * @since 0.0.0
     */
    public short getShort(long posInBits);

    /**
     * <p>
     * Gets short value of next 2 bytes from specified position in bytes.
     * </p>
     *
     * @param posInBytes
     *            specified position in bytes, [0, bits' byte length - 2]
     * @return short value of next 2 bytes from specified position in bytes
     * @throws IllegalArgumentException
     *             if specified bit position is negative
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     * @since 0.0.0
     */
    public short getShort(int posInBytes);

    /**
     * <p>
     * Gets unsigned short value of next 16 bits as int type from specified
     * position in bits. The higher 16 orders will be filled 0.
     * </p>
     *
     * @param posInBits
     *            specified position in bits, [0L, bits' bit length - 16L]
     * @return unsigned short value of next 16 bits as int type from specified
     *         position in bits
     * @throws IllegalArgumentException
     *             if specified bit position is negative
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     * @since 0.0.0
     */
    public int getUnsignedShort(long posInBits);

    /**
     * <p>
     * Gets unsigned short value of next 2 bytes as int type from specified
     * position in bytes. The higher 16 orders will be filled 0.
     * </p>
     *
     * @param posInBytes
     *            specified position in bytes, [0, bits' bit length - 2]
     * @return unsigned short value of next 2 bytes as int type from specified
     *         position in bytes
     * @throws IllegalArgumentException
     *             if specified bit position is negative
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     * @since 0.0.0
     */
    public int getUnsignedShort(int posInBytes);

    /**
     * <p>
     * Gets unsigned short value of next 16 bits as long type from specified
     * position in bits. The higher 48 orders will be filled 0.
     * </p>
     *
     * @param posInBits
     *            specified position in bits, [0L, bits' bit length - 16L]
     * @return unsigned short value of next 16 bits as long type from specified
     *         position in bits
     * @throws IllegalArgumentException
     *             if specified bit position is negative
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     * @since 0.0.0
     */
    public long getLongUnsignedShort(long posInBits);

    /**
     * <p>
     * Gets unsigned short value of next 2 bytes as long type from specified
     * position in bytes. The higher 48 orders will be filled 0.
     * </p>
     *
     * @param posInBytes
     *            specified position in bytes, [0, bits' byte length - 2]
     * @return unsigned short value of next 2 bytes as long type from specified
     *         position in bytes
     * @throws IllegalArgumentException
     *             if specified bit position is negative
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     * @since 0.0.0
     */
    public long getLongUnsignedShort(int posInBytes);

    /**
     * <p>
     * Gets char value of next 16 bits from specified position in bits.
     * </p>
     *
     * @param posInBits
     *            specified position in bits, [0L, bits' bit length - 16L]
     * @return char value of next 16 bits from specified position in bits
     * @throws IllegalArgumentException
     *             if specified bit position is negative
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     * @since 0.0.0
     */
    public char getChar(long posInBits);

    /**
     * <p>
     * Gets char value of next 2 bytes from specified position in bytes.
     * </p>
     *
     * @param posInBytes
     *            specified position in bytes, [0, bits' byte length - 2]
     * @return char value of next 2 bytes from specified position in bytes
     * @throws IllegalArgumentException
     *             if specified bit position is negative
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     * @since 0.0.0
     */
    public char getChar(int posInBytes);

    /**
     * <p>
     * Gets int value of next 32 bits from specified position in bits.
     * </p>
     *
     * @param posInBits
     *            specified position in bits, [0L, bits' bit length - 32L]
     * @return int value of next 32 bits from specified position in bits
     * @throws IllegalArgumentException
     *             if specified bit position is negative
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     * @since 0.0.0
     */
    public int getInt(long posInBits);

    /**
     * <p>
     * Gets int value of next 4 bytes from specified position in bytes.
     * </p>
     *
     * @param posInBytes
     *            specified position in bytes, [0, bits' byte length - 4]
     * @return int value of next 4 bytes from specified position in bytes
     * @throws IllegalArgumentException
     *             if specified bit position is negative
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     * @since 0.0.0
     */
    public int getInt(int posInBytes);

    /**
     * <p>
     * Gets unsigned int value of next 32 bits as long type from specified
     * position in bits. The higher 32 orders will be filled 0.
     * </p>
     *
     * @param posInBits
     *            specified position in bits, [0L, bits' bit length - 32L]
     * @return unsigned int value of next 32 bits as long type from specified
     *         position in bits
     * @throws IllegalArgumentException
     *             if specified bit position is negative
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     * @since 0.0.0
     */
    public long getLongUnsignedInt(long posInBits);

    /**
     * <p>
     * Gets unsigned int value of next 4 bytes as long type from specified
     * position in bytes. The higher 32 orders will be filled 0.
     * </p>
     *
     * @param posInBytes
     *            specified position in bytes, [0, bits' byte length - 4]
     * @return unsigned int value of next 4 bytes as long type from specified
     *         position in bytes
     * @throws IllegalArgumentException
     *             if specified bit position is negative
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     * @since 0.0.0
     */
    public long getLongUnsignedInt(int posInBytes);

    /**
     * <p>
     * Gets float value of next-32-bits from specified position in bits, the
     * returned value's bit layout is the next-32-bits.
     * </p>
     *
     * @param posInBits
     *            specified position in bits, [0L, bits' bit length - 32L]
     * @return float value of next 32 bits from specified position in bits
     * @throws IllegalArgumentException
     *             if specified bit position is negative
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     * @since 0.0.0
     */
    public float getFloat(long posInBits);

    /**
     * <p>
     * Gets float value of next-4-bytes from specified position in bytes, the
     * returned value's bit layout is the next-4-bytes.
     * </p>
     *
     * @param posInBytes
     *            specified position in bytes, [0, bits' byte length - 4]
     * @return float value of next 4 bytes from specified position in bytes
     * @throws IllegalArgumentException
     *             if specified bit position is negative
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     * @since 0.0.0
     */
    public float getFloat(int posInBytes);

    /**
     * <p>
     * Gets long value of next 64 bits from specified position in bits.
     * </p>
     *
     * @param posInBits
     *            specified position in bits, [0L, bits' bit length - 64L]
     * @return long value of next 64 bits from specified position in bits
     * @throws IllegalArgumentException
     *             if specified bit position is negative
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     * @since 0.0.0
     */
    public long getLong(long posInBits);

    /**
     * <p>
     * Gets long value of next 8 bytes from specified position in bytes.
     * </p>
     *
     * @param posInBytes
     *            specified position in bytes, [0, bits' byte length - 8]
     * @return long value of next 8 bytes from specified position in bytes
     * @throws IllegalArgumentException
     *             if specified bit position is negative
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     * @since 0.0.0
     */
    public long getLong(int posInBytes);

    /**
     * <p>
     * Gets double value of next-64-bits from specified position in bits, the
     * returned value's bit layout is the next-64-bits.
     * </p>
     *
     * @param posInBits
     *            specified position in bits, [0L, bits' bit length - 64L]
     * @return double value of next 64 bits from specified position in bits
     * @throws IllegalArgumentException
     *             if specified bit position is negative
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     * @since 0.0.0
     */
    public double getDouble(long posInBits);

    /**
     * <p>
     * Gets double value of next-8-bytes from specified position in bytes, the
     * returned value's bit layout is the next-8-bytes.
     * </p>
     *
     * @param posInBytes
     *            specified position in bytes, [0, bits' byte length - 8]
     * @return double value of next 8 bytes from specified position in bytes
     * @throws IllegalArgumentException
     *             if specified bit position is negative
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     * @since 0.0.0
     */
    public double getDouble(int posInBytes);

    /**
     * <p>
     * Gets byte value of next at least 1 bits from specified position in bits.
     * The actual gotten bits are depended on number of remainder bits of this
     * bits block. If it is less than 8 bits, it will get all remainder bits and
     * promote them to byte type by zero-padding in front, else equal to
     * {@linkplain #getByte(long)}.
     * </p>
     * <p>
     * Generally, this method need check remainder number of bits, so it run
     * more slowly than {@linkplain #getByte(long)}. It is recommended that use
     * {@linkplain #getByte(long)} if the number of remainder bits is clear and
     * enough.
     * </p>
     *
     * @param posInBits
     *            specified position in bits, [0L, bits' bit length - 1L]
     * @return byte value of next at least 1 bits from specified position in
     *         bits
     * @throws IllegalArgumentException
     *             if specified bit position is negative
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     * @see #getByte(long)
     * @since 0.0.0
     */
    public byte getByteInBounds(long posInBits);

    /**
     * <p>
     * Gets byte value of next at least 1 bits from specified position in bytes.
     * The actual gotten bits are depended on number of remainder bits of this
     * bits block. If it is less than 8 bits, it will get all remainder bits and
     * promote them to byte type by zero-padding in front, else equal to
     * {@linkplain #getByte(int)}.
     * </p>
     * <p>
     * Generally, this method need check remainder number of bits, so it run
     * more slowly than {@linkplain #getByte(int)}. It is recommended that use
     * {@linkplain #getByte(int)} if the number of remainder bits is clear and
     * enough.
     * </p>
     *
     * @param posInBytes
     *            specified position in bytes, if turn the position in bytes to
     *            in bits, it should be: [0L, bits' bit length - 1L]
     * @return byte value of next at least 1 bits from specified position in
     *         bytes
     * @throws IllegalArgumentException
     *             if specified bit position is negative
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     * @see #getByte(long)
     * @since 0.0.0
     */
    public byte getByteInBounds(int posInBytes);

    /**
     * <p>
     * Gets unsigned byte value of next at least 1 bits as int type from
     * specified position in bits. The actual gotten bits are depended on number
     * of remainder bits of this bits block. If it is less than 8 bits, it will
     * get all remainder bits and promote them to int type by zero-padding in
     * front, else equal to {@linkplain #getUnsignedByte(long)}.
     * </p>
     * <p>
     * Generally, this method need check remainder number of bits, so it run
     * more slowly than {@linkplain #getUnsignedByte(long)}. It is recommended
     * that use {@linkplain #getUnsignedByte(long)} if the number of remainder
     * bits is clear and enough.
     * </p>
     *
     * @param posInBits
     *            specified position in bits, [0L, bits' bit length - 1L]
     * @return unsigned byte value of next at least 1 bits as int type from
     *         specified position in bits
     * @throws IllegalArgumentException
     *             if specified bit position is negative
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     * @see #getUnsignedByte(long)
     * @since 0.0.0
     */
    public int getUnsignedByteInBounds(long posInBits);

    /**
     * <p>
     * Gets unsigned byte value of next at least 1 bits as int type from
     * specified position in bytes. The actual gotten bits are depended on
     * number of remainder bits of this bits block. If it is less than 8 bits,
     * it will get all remainder bits and promote them to int type by
     * zero-padding in front, else equal to {@linkplain #getUnsignedByte(int)}.
     * </p>
     * <p>
     * Generally, this method need check remainder number of bits, so it run
     * more slowly than {@linkplain #getUnsignedByte(int)}. It is recommended
     * that use {@linkplain #getUnsignedByte(int)} if the number of remainder
     * bits is clear and enough.
     * </p>
     *
     * @param posInBytes
     *            specified position in bytes, if turn the position in bytes to
     *            in bits, it should be: [0L, bits' bit length - 1L]
     * @return unsigned byte value of next at least 1 bits as int type from
     *         specified position in bytes
     * @throws IllegalArgumentException
     *             if specified bit position is negative
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     * @see #getUnsignedByte(long)
     * @since 0.0.0
     */
    public int getUnsignedByteInBounds(int posInBytes);

    /**
     * <p>
     * Gets unsigned byte value of next at least 1 bits as long type from
     * specified position in bits. The actual gotten bits are depended on number
     * of remainder bits of this bits block. If it is less than 8 bits, it will
     * get all remainder bits and promote them to long type by zero-padding in
     * front, else equal to {@linkplain #getLongUnsignedByte(long)}.
     * </p>
     * <p>
     * Generally, this method need check remainder number of bits, so it run
     * more slowly than {@linkplain #getLongUnsignedByte(long)}. It is
     * recommended that use {@linkplain #getLongUnsignedByte(long)} if the
     * number of remainder bits is clear and enough.
     * </p>
     *
     * @param posInBits
     *            specified position in bits, [0L, bits' bit length - 1L]
     * @return unsigned byte value of next at least 1 bits as long type from
     *         specified position in bits
     * @throws IllegalArgumentException
     *             if specified bit position is negative
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     * @see #getLongUnsignedByte(long)
     * @since 0.0.0
     */
    public long getLongUnsignedByteInBounds(long posInBits);

    /**
     * <p>
     * Gets unsigned byte value of next at least 1 bits as long type from
     * specified position in bytes. The actual gotten bits are depended on
     * number of remainder bits of this bits block. If it is less than 8 bits,
     * it will get all remainder bits and promote them to long type by
     * zero-padding in front, else equal to
     * {@linkplain #getLongUnsignedByte(int)}.
     * </p>
     * <p>
     * Generally, this method need check remainder number of bits, so it run
     * more slowly than {@linkplain #getLongUnsignedByte(int)}. It is
     * recommended that use {@linkplain #getLongUnsignedByte(int)} if the number
     * of remainder bits is clear and enough.
     * </p>
     *
     * @param posInBytes
     *            specified position in bytes, if turn the position in bytes to
     *            in bits, it should be: [0L, bits' bit length - 1L]
     * @return unsigned byte value of next at least 1 bits as long type from
     *         specified position in bytes
     * @throws IllegalArgumentException
     *             if specified bit position is negative
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     * @see #getLongUnsignedByte(long)
     * @since 0.0.0
     */
    public long getLongUnsignedByteInBounds(int posInBytes);

    /**
     * <p>
     * Gets short value of next at least 1 bits from specified position in bits.
     * The actual gotten bits are depended on number of remainder bits of this
     * bits block. If it is less than 16 bits, it will get all remainder bits
     * and promote them to short type by zero-padding in front, else equal to
     * {@linkplain #getShort(long)}.
     * </p>
     * <p>
     * Generally, this method need check remainder number of bits, so it run
     * more slowly than {@linkplain #getShort(long)}. It is recommended that use
     * {@linkplain #getShort(long)} if the number of remainder bits is clear and
     * enough.
     * </p>
     *
     * @param posInBits
     *            specified position in bits, [0L, bits' bit length - 1L]
     * @return short value of next at least 1 bits from specified position in
     *         bits
     * @throws IllegalArgumentException
     *             if specified bit position is negative
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     * @see #getShort(long)
     * @since 0.0.0
     */
    public short getShortInBounds(long posInBits);

    /**
     * <p>
     * Gets short value of next at least 1 bits from specified position in
     * bytes. The actual gotten bits are depended on number of remainder bits of
     * this bits block. If it is less than 16 bits, it will get all remainder
     * bits and promote them to short type by zero-padding in front, else equal
     * to {@linkplain #getShort(int)}.
     * </p>
     * <p>
     * Generally, this method need check remainder number of bits, so it run
     * more slowly than {@linkplain #getShort(int)}. It is recommended that use
     * {@linkplain #getShort(int)} if the number of remainder bits is clear and
     * enough.
     * </p>
     *
     * @param posInBytes
     *            specified position in bytes, if turn the position in bytes to
     *            in bits, it should be: [0L, bits' bit length - 1L]
     * @return short value of next at least 1 bits from specified position in
     *         bytes
     * @throws IllegalArgumentException
     *             if specified bit position is negative
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     * @see #getShort(long)
     * @since 0.0.0
     */
    public short getShortInBounds(int posInBytes);

    /**
     * <p>
     * Gets unsigned short value of next at least 1 bits as int type from
     * specified position in bits. The actual gotten bits are depended on number
     * of remainder bits of this bits block. If it is less than 16 bits, it will
     * get all remainder bits and promote them to int type by zero-padding in
     * front, else equal to {@linkplain #getUnsignedShort(long)}.
     * </p>
     * <p>
     * Generally, this method need check remainder number of bits, so it run
     * more slowly than {@linkplain #getUnsignedShort(long)}. It is recommended
     * that use {@linkplain #getUnsignedShort(long)} if the number of remainder
     * bits is clear and enough.
     * </p>
     *
     * @param posInBits
     *            specified position in bits, [0L, bits' bit length - 1L]
     * @return unsigned short value of next at least 1 bits as int type from
     *         specified position in bits
     * @throws IllegalArgumentException
     *             if specified bit position is negative
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     * @see #getUnsignedShort(long)
     * @since 0.0.0
     */
    public int getUnsignedShortInBounds(long posInBits);

    /**
     * <p>
     * Gets unsigned short value of next at least 1 bits as int type from
     * specified position in bytes. The actual gotten bits are depended on
     * number of remainder bits of this bits block. If it is less than 16 bits,
     * it will get all remainder bits and promote them to int type by
     * zero-padding in front, else equal to {@linkplain #getUnsignedShort(int)}
     * .
     * </p>
     * <p>
     * Generally, this method need check remainder number of bits, so it run
     * more slowly than {@linkplain #getUnsignedShort(int)}. It is recommended
     * that use {@linkplain #getUnsignedShort(int)} if the number of remainder
     * bits is clear and enough.
     * </p>
     *
     * @param posInBytes
     *            specified position in bytes, if turn the position in bytes to
     *            in bits, it should be: [0L, bits' bit length - 1L]
     * @return unsigned short value of next at least 1 bits as int type from
     *         specified position in bytes
     * @throws IllegalArgumentException
     *             if specified bit position is negative
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     * @see #getUnsignedShort(long)
     * @since 0.0.0
     */
    public int getUnsignedShortInBounds(int posInBytes);

    /**
     * <p>
     * Gets unsigned short value of next at least 1 bits as long type from
     * specified position in bits. The actual gotten bits are depended on number
     * of remainder bits of this bits block. If it is less than 16 bits, it will
     * get all remainder bits and promote them to long type by zero-padding in
     * front, else equal to {@linkplain #getLongUnsignedShort(long)}.
     * </p>
     * <p>
     * Generally, this method need check remainder number of bits, so it run
     * more slowly than {@linkplain #getLongUnsignedShort(long)}. It is
     * recommended that use {@linkplain #getLongUnsignedShort(long)} if the
     * number of remainder bits is clear and enough.
     * </p>
     *
     * @param posInBits
     *            specified position in bits, [0L, bits' bit length - 1L]
     * @return unsigned short value of next at least 1 bits as long type from
     *         specified position in bits
     * @throws IllegalArgumentException
     *             if specified bit position is negative
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     * @see #getLongUnsignedShort(long)
     * @since 0.0.0
     */
    public long getLongUnsignedShortInBounds(long posInBits);

    /**
     * <p>
     * Gets unsigned short value of next at least 1 bits as long type from
     * specified position in bytes. The actual gotten bits are depended on
     * number of remainder bits of this bits block. If it is less than 16 bits,
     * it will get all remainder bits and promote them to long type by
     * zero-padding in front, else equal to
     * {@linkplain #getLongUnsignedShort(int)}.
     * </p>
     * <p>
     * Generally, this method need check remainder number of bits, so it run
     * more slowly than {@linkplain #getLongUnsignedShort(int)}. It is
     * recommended that use {@linkplain #getLongUnsignedShort(int)} if the
     * number of remainder bits is clear and enough.
     * </p>
     *
     * @param posInBytes
     *            specified position in bytes, if turn the position in bytes to
     *            in bits, it should be: [0L, bits' bit length - 1L]
     * @return unsigned short value of next at least 1 bits as long type from
     *         specified position in bytes
     * @throws IllegalArgumentException
     *             if specified bit position is negative
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     * @see #getLongUnsignedShort(long)
     * @since 0.0.0
     */
    public long getLongUnsignedShortInBounds(int posInBytes);

    /**
     * <p>
     * Gets char value of next at least 1 bits from specified position in bits.
     * The actual gotten bits are depended on number of remainder bits of this
     * bits block. If it is less than 16 bits, it will get all remainder bits
     * and promote them to char type by zero-padding in front, else equal to
     * {@linkplain #getChar(long)}.
     * </p>
     * <p>
     * Generally, this method need check remainder number of bits, so it run
     * more slowly than {@linkplain #getChar(long)}. It is recommended that use
     * {@linkplain #getChar(long)} if the number of remainder bits is clear and
     * enough.
     * </p>
     *
     * @param posInBits
     *            specified position in bits, [0L, bits' bit length - 1L]
     * @return char value of next at least 1 bits from specified position in
     *         bits
     * @throws IllegalArgumentException
     *             if specified bit position is negative
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     * @see #getChar(long)
     * @since 0.0.0
     */
    public char getCharInBounds(long posInBits);

    /**
     * <p>
     * Gets char value of next at least 1 bits from specified position in bytes.
     * The actual gotten bits are depended on number of remainder bits of this
     * bits block. If it is less than 16 bits, it will get all remainder bits
     * and promote them to char type by zero-padding in front, else equal to
     * {@linkplain #getChar(int)}.
     * </p>
     * <p>
     * Generally, this method need check remainder number of bits, so it run
     * more slowly than {@linkplain #getChar(int)}. It is recommended that use
     * {@linkplain #getChar(int)} if the number of remainder bits is clear and
     * enough.
     * </p>
     *
     * @param posInBytes
     *            specified position in bytes, if turn the position in bytes to
     *            in bits, it should be: [0L, bits' bit length - 1L]
     * @return char value of next at least 1 bits from specified position in
     *         bytes
     * @throws IllegalArgumentException
     *             if specified bit position is negative
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     * @see #getChar(long)
     * @since 0.0.0
     */
    public char getCharInBounds(int posInBytes);

    /**
     * <p>
     * Gets int value of next at least 1 bits from specified position in bits.
     * The actual gotten bits are depended on number of remainder bits of this
     * bits block. If it is less than 32 bits, it will get all remainder bits
     * and promote them to int type by zero-padding in front, else equal to
     * {@linkplain #getInt(long)}.
     * </p>
     * <p>
     * Generally, this method need check remainder number of bits, so it run
     * more slowly than {@linkplain #getInt(long)}. It is recommended that use
     * {@linkplain #getInt(long)} if the number of remainder bits is clear and
     * enough.
     * </p>
     *
     * @param posInBits
     *            specified position in bits, [0L, bits' bit length - 1L]
     * @return int value of next at least 1 bits from specified position in bits
     * @throws IllegalArgumentException
     *             if specified bit position is negative
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     * @see #getInt(long)
     * @since 0.0.0
     */
    public int getIntInBounds(long posInBits);

    /**
     * <p>
     * Gets int value of next at least 1 bits from specified position in bytes.
     * The actual gotten bits are depended on number of remainder bits of this
     * bits block. If it is less than 32 bits, it will get all remainder bits
     * and promote them to int type by zero-padding in front, else equal to
     * {@linkplain #getInt(int)}.
     * </p>
     * <p>
     * Generally, this method need check remainder number of bits, so it run
     * more slowly than {@linkplain #getInt(int)}. It is recommended that use
     * {@linkplain #getInt(int)} if the number of remainder bits is clear and
     * enough.
     * </p>
     *
     * @param posInBytes
     *            specified position in bytes, if turn the position in bytes to
     *            in bits, it should be: [0L, bits' bit length - 1L]
     * @return int value of next at least 1 bits from specified position in
     *         bytes
     * @throws IllegalArgumentException
     *             if specified bit position is negative
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     * @see #getInt(long)
     * @since 0.0.0
     */
    public int getIntInBounds(int posInBytes);

    /**
     * <p>
     * Gets unsigned int value of next at least 1 bits as long type from
     * specified position in bits. The actual gotten bits are depended on number
     * of remainder bits of this bits block. If it is less than 32 bits, it will
     * get all remainder bits and promote them to long type by zero-padding in
     * front, else equal to {@linkplain #getLongUnsignedInt(long)}.
     * </p>
     * <p>
     * Generally, this method need check remainder number of bits, so it run
     * more slowly than {@linkplain #getLongUnsignedInt(long)}. It is
     * recommended that use {@linkplain #getLongUnsignedInt(long)} if the number
     * of remainder bits is clear and enough.
     * </p>
     *
     * @param posInBits
     *            specified position in bits, [0L, bits' bit length - 1L]
     * @return unsigned int value of next at least 1 bits as long type from
     *         specified position in bits
     * @throws IllegalArgumentException
     *             if specified bit position is negative
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     * @see #getLongUnsignedInt(long)
     * @since 0.0.0
     */
    public long getLongUnsignedIntInBounds(long posInBits);

    /**
     * <p>
     * Gets unsigned int value of next at least 1 bits as long type from
     * specified position in bytes. The actual gotten bits are depended on
     * number of remainder bits of this bits block. If it is less than 32 bits,
     * it will get all remainder bits and promote them to long type by
     * zero-padding in front, else equal to
     * {@linkplain #getLongUnsignedInt(int)}.
     * </p>
     * <p>
     * Generally, this method need check remainder number of bits, so it run
     * more slowly than {@linkplain #getLongUnsignedInt(int)}. It is recommended
     * that use {@linkplain #getLongUnsignedInt(int)} if the number of remainder
     * bits is clear and enough.
     * </p>
     *
     * @param posInBytes
     *            specified position in bytes, if turn the position in bytes to
     *            in bits, it should be: [0L, bits' bit length - 1L]
     * @return unsigned int value of next at least 1 bits as long type from
     *         specified position in bytes
     * @throws IllegalArgumentException
     *             if specified bit position is negative
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     * @see #getLongUnsignedInt(long)
     * @since 0.0.0
     */
    public long getLongUnsignedIntInBounds(int posInBytes);

    /**
     * <p>
     * Gets float value of next at least 1 bits from specified position in bits.
     * The actual gotten bits are depended on number of remainder bits of this
     * bits block. If it is less than 32 bits, it will get all remainder bits
     * and promote them to 32-bits-long by zero-padding in front -- returned
     * value will be use the 32-bits-long value's bit layout directly, else
     * equal to {@linkplain #getFloat(long)}.
     * </p>
     * <p>
     * Generally, this method need check remainder number of bits, so it run
     * more slowly than {@linkplain #getFloat(long)}. It is recommended that use
     * {@linkplain #getFloat(long)} if the number of remainder bits is clear and
     * enough.
     * </p>
     *
     * @param posInBits
     *            specified position in bits, [0L, bits' bit length - 1L]
     * @return float value of next at least 1 bits from specified position in
     *         bits
     * @throws IllegalArgumentException
     *             if specified bit position is negative
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     * @see #getFloat(long)
     * @since 0.0.0
     */
    public float getFloatInBounds(long posInBits);

    /**
     * <p>
     * Gets float value of next at least 1 bits from specified position in
     * bytes. The actual gotten bits are depended on number of remainder bits of
     * this bits block. If it is less than 32 bits, it will get all remainder
     * bits and promote them to 32-bits-long by zero-padding in front --
     * returned value will be use the 32-bits-long value's bit layout directly,
     * else equal to {@linkplain #getFloat(int)}.
     * </p>
     * <p>
     * Generally, this method need check remainder number of bits, so it run
     * more slowly than {@linkplain #getFloat(int)}. It is recommended that use
     * {@linkplain #getFloat(int)} if the number of remainder bits is clear and
     * enough.
     * </p>
     *
     * @param posInBytes
     *            specified position in bytes, if turn the position in bytes to
     *            in bits, it should be: [0L, bits' bit length - 1L]
     * @return float value of next at least 1 bits from specified position in
     *         bytes
     * @throws IllegalArgumentException
     *             if specified bit position is negative
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     * @see #getFloat(long)
     * @since 0.0.0
     */
    public float getFloatInBounds(int posInBytes);

    /**
     * <p>
     * Gets long value of next at least 1 bits from specified position in bits.
     * The actual gotten bits are depended on number of remainder bits of this
     * bits block. If it is less than 64 bits, it will get all remainder bits
     * and promote them to long type by zero-padding in front, else equal to
     * {@linkplain #getLong(long)}.
     * </p>
     * <p>
     * Generally, this method need check remainder number of bits, so it run
     * more slowly than {@linkplain #getLong(long)}. It is recommended that use
     * {@linkplain #getLong(long)} if the number of remainder bits is clear and
     * enough.
     * </p>
     *
     * @param posInBits
     *            specified position in bits, [0L, bits' bit length - 1L]
     * @return long value of next at least 1 bits from specified position in
     *         bits
     * @throws IllegalArgumentException
     *             if specified bit position is negative
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     * @see #getLong(long)
     * @since 0.0.0
     */
    public long getLongInBounds(long posInBits);

    /**
     * <p>
     * Gets long value of next at least 1 bits from specified position in bytes.
     * The actual gotten bits are depended on number of remainder bits of this
     * bits block. If it is less than 64 bits, it will get all remainder bits
     * and promote them to long type by zero-padding in front, else equal to
     * {@linkplain #getLong(int)}.
     * </p>
     * <p>
     * Generally, this method need check remainder number of bits, so it run
     * more slowly than {@linkplain #getLong(int)}. It is recommended that use
     * {@linkplain #getLong(int)} if the number of remainder bits is clear and
     * enough.
     * </p>
     *
     * @param posInBytes
     *            specified position in bytes, if turn the position in bytes to
     *            in bits, it should be: [0L, bits' bit length - 1L]
     * @return long value of next at least 1 bits from specified position in
     *         bytes
     * @throws IllegalArgumentException
     *             if specified bit position is negative
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     * @see #getLong(long)
     * @since 0.0.0
     */
    public long getLongInBounds(int posInBytes);

    /**
     * <p>
     * Gets double value of next at least 1 bits from specified position in
     * bits. The actual gotten bits are depended on number of remainder bits of
     * this bits block. If it is less than 64 bits, it will get all remainder
     * bits and promote them to 64-bits-long type by zero-padding in front --
     * returned value will be use the 64-bits-long value's bit layout directly,
     * else equal to {@linkplain #getDouble(long)}.
     * </p>
     * <p>
     * Generally, this method need check remainder number of bits, so it run
     * more slowly than {@linkplain #getDouble(long)}. It is recommended that
     * use {@linkplain #getDouble(long)} if the number of remainder bits is
     * clear and enough.
     * </p>
     *
     * @param posInBits
     *            specified position in bits, [0L, bits' bit length - 1L]
     * @return double value of next at least 1 bits from specified position in
     *         bits
     * @throws IllegalArgumentException
     *             if specified bit position is negative
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     * @see #getDouble(long)
     * @since 0.0.0
     */
    public double getDoubleInBounds(long posInBits);

    /**
     * <p>
     * Gets double value of next at least 1 bits from specified position in
     * bytes. The actual gotten bits are depended on number of remainder bits of
     * this bits block. If it is less than 64 bits, it will get all remainder
     * bits and promote them to 64-bits-long type by zero-padding in front --
     * returned value will be use the 64-bits-long value's bit layout directly,
     * else equal to {@linkplain #getDouble(int)}.
     * </p>
     * <p>
     * Generally, this method need check remainder number of bits, so it run
     * more slowly than {@linkplain #getDouble(int)}. It is recommended that use
     * {@linkplain #getDouble(int)} if the number of remainder bits is clear and
     * enough.
     * </p>
     *
     * @param posInBytes
     *            specified position in bytes, if turn the position in bytes to
     *            in bits, it should be: [0L, bits' bit length - 1L]
     * @return double value of next at least 1 bits from specified position in
     *         bytes
     * @throws IllegalArgumentException
     *             if specified bit position is negative
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     * @see #getDouble(long)
     * @since 0.0.0
     */
    public double getDoubleInBounds(int posInBytes);

    /**
     * <p>
     * Gets next bits of specified number as byte type from special position in
     * bits. If specified number of bits is less than 8, it will get bits of
     * specified number and promote them to byte type by zero-padding in front,
     * else equal to {@linkplain #getByte(long)}.
     * </p>
     *
     * @param posInBits
     *            specified position in bits, [0L, bits' bit length - specified
     *            number of bits]
     * @param bits
     *            specified number of bits, [1, 8]
     * @return bits of specified number as byte type from special position in
     *         bits
     * @throws IllegalArgumentException
     *             if specified bit position is negative and/or specified number
     *             of bits out of bounds
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     */
    public byte getBitsAsByte(long posInBits, int bits);

    /**
     * <p>
     * Gets next bits of specified number as byte type from special position in
     * bytes. If specified number of bits is less than 8, it will get bits of
     * specified number and promote them to byte type by zero-padding in front,
     * else equal to {@linkplain #getByte(int)}.
     * </p>
     *
     * @param posInBytes
     *            specified position in bytes, if turn the position in bytes to
     *            in bits, it should be: [0L, bits' bit length - specified
     *            number of bits]
     * @param bits
     *            specified number of bits, [1, 8]
     * @return bits of specified number as byte type from special position in
     *         bytes
     * @throws IllegalArgumentException
     *             if specified bit position is negative and/or specified number
     *             of bits out of bounds
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     */
    public byte getBitsAsByte(int posInBytes, int bits);

    /**
     * <p>
     * Gets next bits of specified number as short type from special position in
     * bits. If specified number of bits is less than 16, it will get bits of
     * specified number and promote them to short type by zero-padding in front,
     * else equal to {@linkplain #getByte(long)}.
     * </p>
     *
     * @param posInBits
     *            specified position in bits, [0L, bits' bit length - specified
     *            number of bits]
     * @param bits
     *            specified number of bits, [1, 16]
     * @return bits of specified number as short type from special position in
     *         bits
     * @throws IllegalArgumentException
     *             if specified bit position is negative and/or specified number
     *             of bits out of bounds
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     */
    public short getBitsAsShort(long posInBits, int bits);

    /**
     * <p>
     * Gets next bits of specified number as short type from special position in
     * bytes. If specified number of bits is less than 16, it will get bits of
     * specified number and promote them to short type by zero-padding in front,
     * else equal to {@linkplain #getByte(int)}.
     * </p>
     *
     * @param posInBytes
     *            specified position in bytes, if turn the position in bytes to
     *            in bits, it should be: [0L, bits' bit length - specified
     *            number of bits]
     * @param bits
     *            specified number of bits, [1, 16]
     * @return bits of specified number as short type from special position in
     *         bytes
     * @throws IllegalArgumentException
     *             if specified bit position is negative and/or specified number
     *             of bits out of bounds
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     */
    public short getBitsAsShort(int posInBytes, int bits);

    /**
     * <p>
     * Gets next bits of specified number as char type from special position in
     * bits. If specified number of bits is less than 16, it will get bits of
     * specified number and promote them to char type by zero-padding in front,
     * else equal to {@linkplain #getByte(long)}.
     * </p>
     *
     * @param posInBits
     *            specified position in bits, [0L, bits' bit length - specified
     *            number of bits]
     * @param bits
     *            specified number of bits, [1, 16]
     * @return bits of specified number as char type from special position in
     *         bits
     * @throws IllegalArgumentException
     *             if specified bit position is negative and/or specified number
     *             of bits out of bounds
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     */
    public char getBitsAsChar(long posInBits, int bits);

    /**
     * <p>
     * Gets next bits of specified number as char type from special position in
     * bytes. If specified number of bits is less than 16, it will get bits of
     * specified number and promote them to char type by zero-padding in front,
     * else equal to {@linkplain #getByte(int)}.
     * </p>
     *
     * @param posInBytes
     *            specified position in bytes, if turn the position in bytes to
     *            in bits, it should be: [0L, bits' bit length - specified
     *            number of bits]
     * @param bits
     *            specified number of bits, [1, 16]
     * @return bits of specified number as char type from special position in
     *         bytes
     * @throws IllegalArgumentException
     *             if specified bit position is negative and/or specified number
     *             of bits out of bounds
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     */
    public char getBitsAsChar(int posInBytes, int bits);

    /**
     * <p>
     * Gets next bits of specified number as int type from special position in
     * bits. If specified number of bits is less than 32, it will get bits of
     * specified number and promote them to int type by zero-padding in front,
     * else equal to {@linkplain #getByte(long)}.
     * </p>
     *
     * @param posInBits
     *            specified position in bits, [0L, bits' bit length - specified
     *            number of bits]
     * @param bits
     *            specified number of bits, [1, 32]
     * @return bits of specified number as int type from special position in
     *         bits
     * @throws IllegalArgumentException
     *             if specified bit position is negative and/or specified number
     *             of bits out of bounds
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     */
    public int getBitsAsInt(long posInBits, int bits);

    /**
     * <p>
     * Gets next bits of specified number as int type from special position in
     * bytes. If specified number of bits is less than 32, it will get bits of
     * specified number and promote them to int type by zero-padding in front,
     * else equal to {@linkplain #getByte(int)}.
     * </p>
     *
     * @param posInBytes
     *            specified position in bytes, if turn the position in bytes to
     *            in bits, it should be: [0L, bits' bit length - specified
     *            number of bits]
     * @param bits
     *            specified number of bits, [1, 32]
     * @return bits of specified number as int type from special position in
     *         bytes
     * @throws IllegalArgumentException
     *             if specified bit position is negative and/or specified number
     *             of bits out of bounds
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     */
    public int getBitsAsInt(int posInBytes, int bits);

    /**
     * <p>
     * Gets next bits of specified number as float type from special position in
     * bits. If specified number of bits is less than 32, it will get bits of
     * specified number and promote them to 32-bits-long type by zero-padding in
     * front -- returned value will be use the 32-bits-long value's bit layout
     * directly, else equal to {@linkplain #getByte(long)}.
     * </p>
     *
     * @param posInBits
     *            specified position in bits, [0L, bits' bit length - specified
     *            number of bits]
     * @param bits
     *            specified number of bits, [1, 32]
     * @return bits of specified number as float type from special position in
     *         bits
     * @throws IllegalArgumentException
     *             if specified bit position is negative and/or specified number
     *             of bits out of bounds
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     */
    public float getBitsAsFloat(long posInBits, int bits);

    /**
     * <p>
     * Gets next bits of specified number as float type from special position in
     * bytes. If specified number of bits is less than 32, it will get bits of
     * specified number and promote them to 32-bits-long type by zero-padding in
     * front -- returned value will be use the 32-bits-long value's bit layout
     * directly, else equal to {@linkplain #getByte(int)}.
     * </p>
     *
     * @param posInBytes
     *            specified position in bytes, if turn the position in bytes to
     *            in bits, it should be: [0L, bits' bit length - specified
     *            number of bits]
     * @param bits
     *            specified number of bits, [1, 32]
     * @return bits of specified number as float type from special position in
     *         bytes
     * @throws IllegalArgumentException
     *             if specified bit position is negative and/or specified number
     *             of bits out of bounds
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     */
    public float getBitsAsFloat(int posInBytes, int bits);

    /**
     * <p>
     * Gets next bits of specified number as long type from special position in
     * bits. If specified number of bits is less than 64, it will get bits of
     * specified number and promote them to long type by zero-padding in front,
     * else equal to {@linkplain #getLong(long)}.
     * </p>
     *
     * @param posInBits
     *            specified position in bits, [0L, bits' bit length - specified
     *            number of bits]
     * @param bits
     *            specified number of bits, [1, 64]
     * @return bits of specified number as long type from special position in
     *         bits
     * @throws IllegalArgumentException
     *             if specified bit position is negative and/or specified number
     *             of bits out of bounds
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     */
    public long getBitsAsLong(long posInBits, int bits);

    /**
     * <p>
     * Gets next bits of specified number as long type from special position in
     * bytes. If specified number of bits is less than 64, it will get bits of
     * specified number and promote them to long type by zero-padding in front,
     * else equal to {@linkplain #getLong(int)}.
     * </p>
     *
     * @param posInBytes
     *            specified position in bytes, if turn the position in bytes to
     *            in bits, it should be: [0L, bits' bit length - specified
     *            number of bits]
     * @param bits
     *            specified number of bits, [1, 64]
     * @return bits of specified number as long type from special position in
     *         bytes
     * @throws IllegalArgumentException
     *             if specified bit position is negative and/or specified number
     *             of bits out of bounds
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     */
    public long getBitsAsLong(int posInBytes, int bits);

    /**
     * <p>
     * Gets next bits of specified number as double type from special position
     * in bits. If specified number of bits is less than 64, it will get bits of
     * specified number and promote them to 64-bits-long type by zero-padding in
     * front -- returned value will be use the 64-bits-long value's bit layout
     * directly, else equal to {@linkplain #getDouble(long)}.
     * </p>
     *
     * @param posInBits
     *            specified position in bits, [0L, bits' bit length - specified
     *            number of bits]
     * @param bits
     *            specified number of bits, [1, 64]
     * @return bits of specified number as double type from special position in
     *         bits
     * @throws IllegalArgumentException
     *             if specified bit position is negative and/or specified number
     *             of bits out of bounds
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     */
    public double getBitsAsDouble(long posInBits, int bits);

    /**
     * <p>
     * Gets next bits of specified number as double type from special position
     * in bytes. If specified number of bits is less than 64, it will get bits
     * of specified number and promote them to 64-bits-long type by zero-padding
     * in front -- returned value will be use the 64-bits-long value's bit
     * layout directly, else equal to {@linkplain #getDouble(int)}.
     * </p>
     *
     * @param posInBytes
     *            specified position in bytes, if turn the position in bytes to
     *            in bits, it should be: [0L, bits' bit length - specified
     *            number of bits]
     * @param bits
     *            specified number of bits, [1, 64]
     * @return bits of specified number as double type from special position in
     *         bytes
     * @throws IllegalArgumentException
     *             if specified bit position is negative and/or specified number
     *             of bits out of bounds
     * @throws OutOfBoundsException
     *             if specified bit position out of bounds
     */
    public double getBitsAsDouble(int posInBytes, int bits);
}
