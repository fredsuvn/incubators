package com.cogician.quicker.binary;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

import com.cogician.quicker.Checker;
import com.cogician.quicker.Quicker;
import com.cogician.quicker.Uniforms;
import com.cogician.quicker.bigarray.BigArray;

/**
 * <p>
 * Inputer represents an input stream to read bytes and compose them into primitive type in current byte order.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2015-09-07 15:21:37
 * @version 0.0.0, 2016-03-14T17:01:39+08:00
 * @since 0.0.0
 * @see QuickInputer
 * @see BinaryInputer
 */
public abstract class Inputer extends InputStream implements MaybeOfLength, ByteOrderSensitive {

    /**
     * <p>
     * Returns next byte.
     * </p>
     * 
     * @return next byte
     * @throws EOFException
     *             if reaches to end of stream before reading all bytes
     * @throws IOException
     *             if an I/O error occurs
     * @since 0.0.0
     */
    protected abstract byte _readByte() throws EOFException, IOException;

    /**
     * <p>
     * Returns next 2-bytes and composes into a short in big-endian.
     * </p>
     * 
     * @return next short
     * @throws EOFException
     *             if reaches to end of stream before reading all bytes
     * @throws IOException
     *             if an I/O error occurs
     * @since 0.0.0
     */
    protected abstract short _readShort() throws EOFException, IOException;

    /**
     * <p>
     * Returns next 2-bytes and composes into a char in big-endian.
     * </p>
     * 
     * @return next char
     * @throws EOFException
     *             if reaches to end of stream before reading all bytes
     * @throws IOException
     *             if an I/O error occurs
     * @since 0.0.0
     */
    protected abstract char _readChar() throws EOFException, IOException;

    /**
     * <p>
     * Returns next 3-bytes and composes into a medium in big-endian. The highest unused byte order is ignored.
     * </p>
     * 
     * @return next medium
     * @throws EOFException
     *             if reaches to end of stream before reading all bytes
     * @throws IOException
     *             if an I/O error occurs
     * @since 0.0.0
     */
    protected abstract int _readMedium() throws EOFException, IOException;

    /**
     * <p>
     * Returns next 4-bytes and composes into an int in big-endian.
     * </p>
     * 
     * @return next int
     * @throws EOFException
     *             if reaches to end of stream before reading all bytes
     * @throws IOException
     *             if an I/O error occurs
     * @since 0.0.0
     */
    protected abstract int _readInt() throws EOFException, IOException;

    /**
     * <p>
     * Returns next 8-bytes and composes into a long in big-endian.
     * </p>
     * 
     * @return next long
     * @throws EOFException
     *             if reaches to end of stream before reading all bytes
     * @throws IOException
     *             if an I/O error occurs
     * @since 0.0.0
     */
    protected abstract long _readLong() throws EOFException, IOException;

    /**
     * <p>
     * Returns next 4-bytes and composes into a float in big-endian.
     * </p>
     * 
     * @return next float
     * @throws EOFException
     *             if reaches to end of stream before reading all bytes
     * @throws IOException
     *             if an I/O error occurs
     * @since 0.0.0
     */
    protected abstract float _readFloat() throws EOFException, IOException;

    /**
     * <p>
     * Returns next 8-bytes and composes into a double in big-endian.
     * </p>
     * 
     * @return next double
     * @throws EOFException
     *             if reaches to end of stream before reading all bytes
     * @throws IOException
     *             if an I/O error occurs
     * @since 0.0.0
     */
    protected abstract double _readDouble() throws EOFException, IOException;

    /**
     * <p>
     * Returns next byte.
     * </p>
     * 
     * @return next byte
     * @throws EOFException
     *             if reaches to end of stream
     * @throws IOException
     *             if an I/O error occurs
     * @since 0.0.0
     */
    public byte readByte() throws EOFException, IOException {
        return _readByte();
    }

    /**
     * <p>
     * Returns next byte and zero-extends it.
     * </p>
     * 
     * @return next unsigned byte
     * @throws EOFException
     *             if reaches to end of stream
     * @throws IOException
     *             if an I/O error occurs
     * @since 0.0.0
     */
    public int readUnsignedByte() throws EOFException, IOException {
        return BytesUtil.toUnsignedByte(readByte());
    }

    /**
     * <p>
     * Returns next 2-bytes and composes into a short in current byte order.
     * </p>
     * 
     * @return next short
     * @throws EOFException
     *             if reaches to end of stream before reading all bytes
     * @throws IOException
     *             if an I/O error occurs
     * @since 0.0.0
     */
    public short readShort() throws EOFException, IOException {
        return getByteOrderProcessor().doShort(_readShort());
    }

    /**
     * <p>
     * Zero-extends {@linkplain #readShort()}.
     * </p>
     * 
     * @return next unsigned short
     * @throws EOFException
     *             if reaches to end of stream before reading all bytes
     * @throws IOException
     *             if an I/O error occurs
     * @since 0.0.0
     */
    public int readUnsignedShort() throws EOFException, IOException {
        return BytesUtil.toUnsignedShort(readShort());
    }

    /**
     * <p>
     * Returns next 2-bytes and composes into a char in current byte order.
     * </p>
     * 
     * @return next char
     * @throws EOFException
     *             if reaches to end of stream before reading all bytes
     * @throws IOException
     *             if an I/O error occurs
     * @since 0.0.0
     */
    public char readChar() throws EOFException, IOException {
        return getByteOrderProcessor().doChar(_readChar());
    }

    /**
     * <p>
     * Returns next 3-bytes and composes into a medium in current byte order.
     * </p>
     * 
     * @return next medium
     * @throws EOFException
     *             if reaches to end of stream before reading all bytes
     * @throws IOException
     *             if an I/O error occurs
     * @since 0.0.0
     */
    public int readMedium() throws EOFException, IOException {
        return getByteOrderProcessor().doMedium(_readMedium());
    }

    /**
     * <p>
     * Zero-extends {@linkplain #readMedium()}.
     * </p>
     * 
     * @return next unsigned medium
     * @throws EOFException
     *             if reaches to end of stream before reading all bytes
     * @throws IOException
     *             if an I/O error occurs
     * @since 0.0.0
     */
    public int readUnsignedMedium() throws EOFException, IOException {
        return BytesUtil.toUnsignedMedium(readMedium());
    }

    /**
     * <p>
     * Returns next 4-bytes and composes into an int in current byte order.
     * </p>
     * 
     * @return next int
     * @throws EOFException
     *             if reaches to end of stream before reading all bytes
     * @throws IOException
     *             if an I/O error occurs
     * @since 0.0.0
     */
    public int readInt() throws EOFException, IOException {
        return getByteOrderProcessor().doInt(_readInt());
    }

    /**
     * <p>
     * Zero-extends {@linkplain #readInt()}.
     * </p>
     * 
     * @return next unsigned int
     * @throws EOFException
     *             if reaches to end of stream before reading all bytes
     * @throws IOException
     *             if an I/O error occurs
     * @since 0.0.0
     */
    public long readUnsignedInt() throws EOFException, IOException {
        return BytesUtil.toUnsignedInt(readInt());
    }

    /**
     * <p>
     * Returns next 8-bytes and composes into a long in current byte order.
     * </p>
     * 
     * @return next long
     * @throws EOFException
     *             if reaches to end of stream before reading all bytes
     * @throws IOException
     *             if an I/O error occurs
     * @since 0.0.0
     */
    public long readLong() throws EOFException, IOException {
        return getByteOrderProcessor().doLong(_readLong());
    }

    /**
     * <p>
     * Returns next 4-bytes and composes into a float in current byte order.
     * </p>
     * 
     * @return next float
     * @throws EOFException
     *             if reaches to end of stream before reading all bytes
     * @throws IOException
     *             if an I/O error occurs
     * @since 0.0.0
     */
    public float readFloat() throws EOFException, IOException {
        return getByteOrderProcessor().doFloat(_readFloat());
    }

    /**
     * <p>
     * Returns next 8-bytes and composes into a double in current byte order.
     * </p>
     * 
     * @return next double
     * @throws EOFException
     *             if reaches to end of stream before reading all bytes
     * @throws IOException
     *             if an I/O error occurs
     * @since 0.0.0
     */
    public double readDouble() throws EOFException, IOException {
        return getByteOrderProcessor().doDouble(_readDouble());
    }

    /**
     * <p>
     * Returns next byte as an ASCII character.
     * </p>
     * 
     * @return next byte as an ASCII character
     * @throws EOFException
     *             if reaches to end of stream before reading all bytes
     * @throws IOException
     *             if an I/O error occurs
     * @since 0.0.0
     */
    public char readASCII() throws EOFException, IOException {
        return (char)readUnsignedByte();
    }

    /**
     * <p>
     * Returns next byte as a hex string, the string length is fixed 2.
     * </p>
     * 
     * @return next byte as a hex string
     * @throws EOFException
     *             if reaches to end of stream before reading all bytes
     * @throws IOException
     *             if an I/O error occurs
     * @since 0.0.0
     */
    public String readHex() throws EOFException, IOException {
        return Quicker.leftPad(Integer.toHexString(readUnsignedByte()), 2, "0").toUpperCase(Uniforms.LOCALE);
    }

    /**
     * <p>
     * Returns next bytes of specified number and composes into a primitive numeric type as long in current byte order.
     * For example, 2-bytes will compose into a short, or 4 bytes into an int.
     * </p>
     * 
     * @param bytesNum
     *            specified bytes number
     * @return next primitive numeric type as long
     * @throws IllegalArgumentException
     *             if specified bytes number is negative
     * @throws EOFException
     *             if reaches to end of stream before reading all bytes
     * @throws IOException
     *             if an I/O error occurs
     * @since 0.0.0
     */
    public long readPrimitive(int bytesNum) throws IllegalArgumentException, EOFException, IOException {
        Checker.checkPositiveOr0(bytesNum);
        long row;
        switch (bytesNum) {
            case 1: {
                return readByte();
            }
            case 2: {
                return readShort();
            }
            case 3: {
                return readMedium();
            }
            case 4: {
                return readInt();
            }
            case 5: {
                long l1 = _readByte();
                long l2 = BytesUtil.toUnsignedInt(_readInt());
                row = (l1 << 32) | l2;
                break;
            }
            case 6: {
                long l1 = _readShort();
                long l2 = BytesUtil.toUnsignedInt(_readInt());
                row = (l1 << 32) | l2;
                break;
            }
            case 7: {
                long l1 = _readMedium();
                long l2 = BytesUtil.toUnsignedInt(_readInt());
                row = (l1 << 32) | l2;
                break;
            }
            case 8: {
                return readLong();
            }
            default: {
                throw new IllegalArgumentException("Bytes number must in [1, 8]");
            }
        }
        return getByteOrderProcessor().doBytes(row, bytesNum);
    }

    /**
     * <p>
     * Zero-extends {@linkplain #readPrimitive(int)}.
     * </p>
     * 
     * @param bytesNum
     *            specified bytes number
     * @return next primitive numeric type as long
     * @throws IllegalArgumentException
     *             if specified bytes number is negative
     * @throws EOFException
     *             if reaches to end of stream before reading all bytes
     * @throws IOException
     *             if an I/O error occurs
     * @since 0.0.0
     */
    public long readUnsignedPrimitive(int bytesNum) throws IllegalArgumentException, EOFException, IOException {
        return BytesUtil.toUnsignedBytes(readPrimitive(bytesNum), bytesNum);
    }

    /**
     * <p>
     * Returns a byte array with specified length of which bytes are read from this inputer.
     * </p>
     * 
     * @param bytesNum
     *            specified length of read bytes
     * @return a byte array with specified length of which bytes are read from this inputer
     * @throws IllegalArgumentException
     *             specified length of read bytes is negative
     * @throws EOFException
     *             if reaches to end of stream before reading all bytes
     * @throws IOException
     *             if an I/O error occurs
     * @since 0.0.0
     */
    public byte[] readBytes(int bytesNum) throws IllegalArgumentException, EOFException, IOException {
        Checker.checkPositiveOr0(bytesNum);
        byte[] b = new byte[bytesNum];
        readFully(b);
        return b;
    }

    /**
     * <p>
     * Reads bytes till encounter the specified separator (read inclusively). Read bytes will be put into a new array
     * and return.
     * </p>
     * 
     * @param separator
     *            specified separator
     * @return a new array contains read bytes
     * @throws NullPointerException
     *             if specified separator is null
     * @throws IllegalArgumentException
     *             if specified separator is empty
     * @throws EOFException
     *             if reaches to end of stream before reading all bytes
     * @throws IOException
     *             if an I/O error occurs
     * @since 0.0.0
     */
    public byte[] readBytesTill(byte[] separator)
            throws NullPointerException, IllegalArgumentException, EOFException, IOException {
        Checker.checkEmpty(separator);
        ByteQueue bq = new ByteQueue();
        while (true) {
            byte b = readByte();
            if (b == separator[0]) {
                int i = 1;
                for (; i < separator.length; i++) {
                    byte v = readByte();
                    bq.addLast(v);
                    if (v != separator[i]) {
                        break;
                    }
                }
                if (i == separator.length) {
                    return bq.toArray();
                }
            } else {
                bq.addLast(b);
            }
        }
    }

    /**
     * <p>
     * Returns a binary with specified length of which bytes are read from this inputer.
     * </p>
     * 
     * @param bytesNum
     *            specified length of read bytes
     * @return a binary with specified length of which bytes are read from this inputer
     * @throws IllegalArgumentException
     *             specified length of read bytes is negative
     * @throws EOFException
     *             if reaches to end of stream before reading all bytes
     * @throws IOException
     *             if an I/O error occurs
     * @since 0.0.0
     */
    public Binary readBinary(long bytesNum) throws IllegalArgumentException, EOFException, IOException {
        Checker.checkPositiveOr0(bytesNum);
        Binary bin = Binary.alloc(bytesNum);
        readFully(bin);
        return bin;
    }

    /**
     * <p>
     * Reads bytes till encounter the specified separator (read inclusively). Read bytes will be put into a new binary
     * and return.
     * </p>
     * 
     * @param separator
     *            specified separator
     * @return a new binary contains read bytes
     * @throws NullPointerException
     *             if specified separator is null
     * @throws IllegalArgumentException
     *             if specified separator is empty
     * @throws EOFException
     *             if reaches to end of stream before reading all bytes
     * @throws IOException
     *             if an I/O error occurs
     * @since 0.0.0
     */
    public Binary readBinaryTill(Binary separator) throws IllegalArgumentException, EOFException, IOException {
        Checker.checkEmpty(separator);
        ByteQueue bq = new ByteQueue();
        while (true) {
            byte b = readByte();
            if (b == separator.getByte(0)) {
                long i = 1;
                for (; i < separator.length(); i++) {
                    byte v = readByte();
                    bq.addLast(v);
                    if (v != separator.getByte(i)) {
                        break;
                    }
                }
                if (i == separator.length()) {
                    return bq.toBinary();
                }
            } else {
                bq.addLast(b);
            }
        }
    }

    /**
     * <p>
     * Skips bytes of specified number fully.
     * </p>
     * 
     * @param bytesNum
     *            specified bytes number
     * @throws IllegalArgumentException
     *             if specified bytes number is negative
     * @throws EOFException
     *             if reaches to end of stream before reading all bytes
     * @throws IOException
     *             if an I/O error occurs
     * @since 0.0.0
     */
    public void skipFully(long bytesNum) throws IllegalArgumentException, EOFException, IOException {
        long actual = 0;
        while ((actual = skip(bytesNum)) < bytesNum) {
            bytesNum -= actual;
        }
    }

    /**
     * <p>
     * Reads bytes into specified binary. Actual number of read bytes will be returned. If return -1, that means reach
     * to the end of stream.
     * </p>
     * 
     * @param binary
     *            specified binary
     * @return actual number of read bytes
     * @throws NullPointerException
     *             if specified binary is null
     * @throws IOException
     *             if an I/O error occurs
     * @since 0.0.0
     */
    public long read(Binary binary) throws NullPointerException, IOException {
        return read(binary, 0, binary.length());
    }

    /**
     * <p>
     * Reads bytes of specified length into specified binary at specified offset index. Actual number of read bytes will
     * be returned. If return -1, that means reach to the end of stream.
     * </p>
     * 
     * @param binary
     *            specified binary
     * @param offset
     *            specified offset index
     * @param length
     *            specified length
     * @return actual number of read bytes will be returned
     * @throws NullPointerException
     *             if specified binary is null
     * @throws IndexOutOfBoundsException
     *             if specified offset index or length leads to out of bounds
     * @throws IOException
     *             if any other IO problem occurs
     * @since 0.0.0
     */
    public long read(Binary binary, long offset, long length)
            throws NullPointerException, IndexOutOfBoundsException, IOException {
        Checker.checkNull(binary);
        Checker.checkRangeIndexes(offset, offset + length, binary.length());
        long remainder = length;
        while (remainder > 0) {
            byte[] b = new byte[(int)Math.min(remainder, BigArray.BLOCK_SIZE)];
            int actual = read(b);
            Binary.wrap(b).copy(0, binary, offset, actual);
            offset += actual;
            remainder -= actual;
            if (actual < b.length) {
                break;
            }
        }
        return length - remainder;
    }

    /**
     * <p>
     * Read bytes into specified array until the array is filled.
     * </p>
     * 
     * @param offset
     *            specified offset index
     * @param length
     *            specified length
     * @param array
     *            specified array
     * @throws NullPointerException
     *             if specified array is null
     * @throws EOFException
     *             if reaches to end of stream before reading all bytes
     * @throws IOException
     *             if an I/O error occurs
     * @since 0.0.0
     */
    public void readFully(byte[] array) throws NullPointerException, EOFException, IOException {
        readFully(array, 0, array.length);
    }

    /**
     * <p>
     * Read bytes of specified length into specified array at specified offset index until the array is filled.
     * </p>
     * 
     * @param array
     *            specified array
     * @throws NullPointerException
     *             if specified array is null
     * @throws IndexOutOfBoundsException
     *             if specified offset index or length leads to out of bounds
     * @throws EOFException
     *             if reaches to end of stream before reading all bytes
     * @throws IOException
     *             if an I/O error occurs
     * @since 0.0.0
     */
    public void readFully(byte[] array, int offset, int length)
            throws NullPointerException, IndexOutOfBoundsException, EOFException, IOException {
        int actual = 0;
        while ((actual = read(array, offset, length)) < length) {
            if (actual == -1) {
                throw new EOFException();
            }
            length -= actual;
            offset += actual;
        }
    }

    /**
     * <p>
     * Read bytes into specified binary until the binary is filled.
     * </p>
     * 
     * @param offset
     *            specified offset index
     * @param length
     *            specified length
     * @param binary
     *            specified binary
     * @throws NullPointerException
     *             if specified binary is null
     * @throws EOFException
     *             if reaches to end of stream before reading all bytes
     * @throws IOException
     *             if an I/O error occurs
     * @since 0.0.0
     */
    public void readFully(Binary binary) throws NullPointerException, EOFException, IOException {
        readFully(binary, 0, binary.length());
    }

    /**
     * <p>
     * Read bytes of specified length into specified binary at specified offset index until the binary is filled.
     * </p>
     * 
     * @param binary
     *            specified binary
     * @throws NullPointerException
     *             if specified binary is null
     * @throws IndexOutOfBoundsException
     *             if specified offset index or length leads to out of bounds
     * @throws EOFException
     *             if reaches to end of stream before reading all bytes
     * @throws IOException
     *             if an I/O error occurs
     * @since 0.0.0
     */
    public void readFully(Binary binary, long offset, long length)
            throws NullPointerException, IndexOutOfBoundsException, EOFException, IOException {
        Checker.checkNull(binary);
        Checker.checkRangeIndexes(offset, offset + length, binary.length());
        long remainder = length;
        while (remainder > 0) {
            byte[] b = new byte[(int)Math.min(remainder, BigArray.BLOCK_SIZE)];
            readFully(b);
            Binary.wrap(b).copy(0, binary, offset, b.length);
            offset += b.length;
            remainder -= b.length;
        }
    }

    /**
     * <p>
     * Returns whether this inputer has reached to the end and never can be read. This method is valid if length of this
     * inputer can be estimated by {@linkplain #length()}; otherwise, it always return false.
     * </p>
     * 
     * @return whether this inputer has reached to the end and never can be read
     * @since 0.0.0
     */
    @Override
    public boolean end() {
        return MaybeOfLength.super.end();
    }

    /**
     * <p>
     * Returns length of this inputer in bytes if this stream is of length, or -1 if its length can not be estimated.
     * </p>
     * 
     * @return length of this inputer
     * @since 0.0.0
     */
    @Override
    public abstract long length();

    /**
     * <p>
     * Returns remainder length of this inputer between current position in bytes and end if this stream is of length,
     * or -1 if its length can not be estimated.
     * </p>
     * 
     * @return remainder length of this inputer
     * @since 0.0.0
     */
    @Override
    public abstract long remainderLength();
}
