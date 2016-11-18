package com.cogician.quicker.binary;

import java.io.EOFException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.SyncFailedException;

import com.cogician.quicker.Checker;

/**
 * <p>
 * Outputer represents an output stream to compose bytes into primitive type in current byte order and write them.
 * </p>
 * <p>
 * Default implementation are {@linkplain QuickOutputer} and {@linkplain BinaryOutputer}.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2015-09-09 09:43:56
 * @version 0.0.0, 2016-03-15T16:27:37+08:00
 * @since 0.0.0
 * @see QuickOutputer
 * @see BinaryOutputer
 */
public abstract class Outputer extends OutputStream implements MaybeOfLength, ByteOrderSensitive {

    /**
     * <p>
     * Writes low-byte of given value.
     * </p>
     * 
     * @param value
     *            given value
     * @throws EOFException
     *             if reaches to end of stream before writing all bytes
     * @throws IOException
     *             if an I/O error occurs
     * @since 0.0.0
     */
    protected abstract void _writeByte(int value) throws EOFException, IOException;

    /**
     * <p>
     * Writes low-2-bytes of given value in big-endian.
     * </p>
     * 
     * @param value
     *            given value
     * @throws EOFException
     *             if reaches to end of stream before writing all bytes
     * @throws IOException
     *             if an I/O error occurs
     * @since 0.0.0
     */
    protected abstract void _writeShort(int value) throws EOFException, IOException;

    /**
     * <p>
     * Writes low-2-bytes of given value in big-endian.
     * </p>
     * 
     * @param value
     *            given value
     * @throws EOFException
     *             if reaches to end of stream before writing all bytes
     * @throws IOException
     *             if an I/O error occurs
     * @since 0.0.0
     */
    protected abstract void _writeChar(int value) throws EOFException, IOException;

    /**
     * <p>
     * Writes low-3-bytes of given value in big-endian.
     * </p>
     * 
     * @param value
     *            given value
     * @throws EOFException
     *             if reaches to end of stream before writing all bytes
     * @throws IOException
     *             if an I/O error occurs
     * @since 0.0.0
     */
    protected abstract void _writeMedium(int value) throws EOFException, IOException;

    /**
     * <p>
     * Writes 4-bytes of given value in big-endian.
     * </p>
     * 
     * @param value
     *            given value
     * @throws EOFException
     *             if reaches to end of stream before writing all bytes
     * @throws IOException
     *             if an I/O error occurs
     * @since 0.0.0
     */
    protected abstract void _writeInt(int value) throws EOFException, IOException;

    /**
     * <p>
     * Writes 8-bytes of given value in big-endian.
     * </p>
     * 
     * @param value
     *            given value
     * @throws EOFException
     *             if reaches to end of stream before writing all bytes
     * @throws IOException
     *             if an I/O error occurs
     * @since 0.0.0
     */
    protected abstract void _writeLong(long value) throws EOFException, IOException;

    /**
     * <p>
     * Writes 4-bytes of given value in big-endian.
     * </p>
     * 
     * @param value
     *            given value
     * @throws EOFException
     *             if reaches to end of stream before writing all bytes
     * @throws IOException
     *             if an I/O error occurs
     * @since 0.0.0
     */
    protected abstract void _writeFloat(float value) throws EOFException, IOException;

    /**
     * <p>
     * Writes 8-bytes of given value in big-endian.
     * </p>
     * 
     * @param value
     *            given value
     * @throws EOFException
     *             if reaches to end of stream before writing all bytes
     * @throws IOException
     *             if an I/O error occurs
     * @since 0.0.0
     */
    protected abstract void _writeDouble(double value) throws EOFException, IOException;

    /**
     * <p>
     * Writes low-byte of given value.
     * </p>
     * 
     * @param value
     *            given value
     * @throws EOFException
     *             if reaches to end of stream before writing all bytes
     * @throws IOException
     *             if an I/O error occurs
     * @since 0.0.0
     */
    public void writeByte(int value) throws EOFException, IOException {
        _writeByte(value);
    }

    /**
     * <p>
     * Writes low-2-bytes of given value in current byte order.
     * </p>
     * 
     * @param value
     *            given value
     * @throws EOFException
     *             if reaches to end of stream before writing all bytes
     * @throws IOException
     *             if an I/O error occurs
     * @since 0.0.0
     */
    public void writeShort(int value) throws EOFException, IOException {
        _writeShort(getByteOrderProcessor().doShort((short)value));
    }

    /**
     * <p>
     * Writes low-2-bytes of given value in current byte order.
     * </p>
     * 
     * @param value
     *            given value
     * @throws EOFException
     *             if reaches to end of stream before writing all bytes
     * @throws IOException
     *             if an I/O error occurs
     * @since 0.0.0
     */
    public void writeChar(int value) throws EOFException, IOException {
        _writeChar(getByteOrderProcessor().doChar((char)value));
    }

    /**
     * <p>
     * Writes low-3-bytes of given value in current byte order.
     * </p>
     * 
     * @param value
     *            given value
     * @throws EOFException
     *             if reaches to end of stream before writing all bytes
     * @throws IOException
     *             if an I/O error occurs
     * @since 0.0.0
     */
    public void writeMedium(int value) throws EOFException, IOException {
        _writeMedium(getByteOrderProcessor().doMedium(value));
    }

    /**
     * <p>
     * Writes 4-bytes of given value in current byte order.
     * </p>
     * 
     * @param value
     *            given value
     * @throws EOFException
     *             if reaches to end of stream before writing all bytes
     * @throws IOException
     *             if an I/O error occurs
     * @since 0.0.0
     */
    public void writeInt(int value) throws EOFException, IOException {
        _writeInt(getByteOrderProcessor().doInt(value));
    }

    /**
     * <p>
     * Writes 8-bytes of given value in current byte order.
     * </p>
     * 
     * @param value
     *            given value
     * @throws EOFException
     *             if reaches to end of stream before writing all bytes
     * @throws IOException
     *             if an I/O error occurs
     * @since 0.0.0
     */
    public void writeLong(long value) throws EOFException, IOException {
        _writeLong(getByteOrderProcessor().doLong(value));
    }

    /**
     * <p>
     * Writes 4-bytes of given value in current byte order.
     * </p>
     * 
     * @param value
     *            given value
     * @throws EOFException
     *             if reaches to end of stream before writing all bytes
     * @throws IOException
     *             if an I/O error occurs
     * @since 0.0.0
     */
    public void writeFloat(float value) throws EOFException, IOException {
        _writeFloat(getByteOrderProcessor().doFloat(value));
    }

    /**
     * <p>
     * Writes 8-bytes of given value in current byte order.
     * </p>
     * 
     * @param value
     *            given value
     * @throws EOFException
     *             if reaches to end of stream before writing all bytes
     * @throws IOException
     *             if an I/O error occurs
     * @since 0.0.0
     */
    public void writeDouble(double value) throws EOFException, IOException {
        _writeDouble(getByteOrderProcessor().doDouble(value));
    }

    /**
     * <p>
     * Writes given character as an ASCII character (1 byte).
     * </p>
     * 
     * @param character
     *            given character
     * @throws EOFException
     *             if reaches to end of stream before writing all bytes
     * @throws IOException
     *             if an I/O error occurs
     * @since 0.0.0
     */
    public void writeASCII(char character) throws EOFException, IOException {
        writeByte(character);
    }

    /**
     * <p>
     * Writes low-specified-number-bytes of given value in current byte order.
     * </p>
     * 
     * @param value
     *            given value
     * @param bytesNum
     *            number of specified byte, [1, 8]
     * @throws IllegalArgumentException
     *             if number of specified byte is not in [1, 8]
     * @throws EOFException
     *             if reaches to end of stream before writing all bytes
     * @throws IOException
     *             if an I/O error occurs
     * @since 0.0.0
     */
    public void writePrimitive(long value, int bytesNum) throws IllegalArgumentException, EOFException, IOException {
        switch (bytesNum) {
            case 1: {
                writeByte((int)value);
                break;
            }
            case 2: {
                writeShort((int)value);
                break;
            }
            case 3: {
                writeMedium((int)value);
                break;
            }
            case 4: {
                writeInt((int)value);
                break;
            }
            case 5: {
                long b = getByteOrderProcessor().doBytes(value, bytesNum);
                _writeByte((int)(b >> 32));
                _writeInt((int)b);
                break;
            }
            case 6: {
                long b = getByteOrderProcessor().doBytes(value, bytesNum);
                _writeShort((int)(b >> 32));
                _writeInt((int)b);
                break;
            }
            case 7: {
                long b = getByteOrderProcessor().doBytes(value, bytesNum);
                _writeMedium((int)(b >> 32));
                _writeInt((int)b);
                break;
            }
            case 8: {
                writeLong(value);
                break;
            }
            default: {
                throw new IllegalArgumentException("Bytes number must in [1, 8]");
            }
        }
    }

    @Override
    public void write(int b) throws IOException {
        writeByte(b);
    }

    /**
     * <p>
     * Writes bytes from specified binary.
     * </p>
     * 
     * @param binary
     *            specified binary
     * @throws NullPointerException
     *             if specified binary is null
     * @throws IOException
     *             if an I/O error occurs
     * @since 0.0.0
     */
    public void write(Binary binary) throws NullPointerException, IOException {
        write(binary, 0, binary.length());
    };

    /**
     * <p>
     * Writes bytes of specified length from specified binary at specified offset index.
     * </p>
     * 
     * @param binary
     *            specified binary
     * @param offset
     *            specified offset index
     * @param length
     *            specified length
     * @throws NullPointerException
     *             if specified binary is null
     * @throws IndexOutOfBoundsException
     *             if specified offset index or length out of bounds
     * @throws IOException
     *             if an I/O error occurs
     * @since 0.0.0
     */
    public void write(Binary binary, long offset, long length)
            throws NullPointerException, IndexOutOfBoundsException, IOException {
        Checker.checkNull(binary);
        Checker.checkRangeIndexes(offset, offset + length, binary.length());
        for (long i = offset; i < offset + length; i++) {
            write(binary.getByte(i));
        }
    }

    /**
     * <p>
     * Forces all system buffers to synchronize with the underlying device. This method returns after all modified data
     * and attributes have been written to the relevant device(s).
     * </p>
     * 
     * @throws SyncFailedException
     *             if sync failed
     * @since 0.0.0
     */
    public abstract void sync() throws SyncFailedException;

    /**
     * <p>
     * Returns whether this outputer has reached to the end and never can be write. This method is valid if length of
     * this inputer can be estimated by {@linkplain #length()}; otherwise, it always return false.
     * </p>
     * 
     * @return whether this inputer has reached to the end and never can be write
     * @since 0.0.0
     */
    @Override
    public boolean end() {
        return MaybeOfLength.super.end();
    }

    /**
     * <p>
     * Returns length of this outputer in bytes if this stream is of length, or -1 if its length can not be estimated.
     * </p>
     * 
     * @return length of this outputer
     * @since 0.0.0
     */
    @Override
    public abstract long length();

    /**
     * <p>
     * Returns remainder length of this outputer between current position in bytes and end if this stream is of length,
     * or -1 if its length can not be estimated.
     * </p>
     * 
     * @return remainder length of this outputer
     * @since 0.0.0
     */
    @Override
    public abstract long remainderLength();
}
