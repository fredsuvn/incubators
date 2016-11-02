package com.cogician.quicker;

import java.io.BufferedReader;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Stream;

import com.cogician.quicker.util.reflect.ReflectionException;

/**
 * An type of implementation of {@linkplain DataInputAdapter}, the adaptee is an
 * input stream.
 *
 * @author Fred Suvn
 * @version 0.0.0, 2015-06-29 11:12:31
 * @since 0.0.0
 */
public class InputStreamAdapter implements DataInputAdapter<InputStream>,
        AutoCloseable, IndexedSubable<InputStreamAdapter> {

    /**
     * Input stream, the source of data, never null.
     */
    private final InputStream in;

    /**
     * Buffer of data input stream, buffer when
     * {@linkplain #toDataInputStream()} called
     */
    private DataInputStream dataInput;

    /**
     * Buffer of buffered reader, buffer when {@linkplain #toBufferedReader()}
     * called.
     */
    private BufferedReader bufferedReader = null;

    /**
     * Buffer of scanner, buffer when {@linkplain #toScanner()} called.
     */
    private Scanner scanner = null;

    /**
     * Constructs with special input stream.
     *
     * @param in
     *            special input stream, not null
     * @throws NullPointerException
     *             thrown if given input stream is null
     */
    public InputStreamAdapter(final InputStream in) {
        this.in = Objects.requireNonNull(in);
    }

    /**
     * Returns an estimate of the number of bytes that can be read (or skipped
     * over) from this input stream without blocking by the next invocation of a
     * method for this input stream.
     * <p>
     * See {@linkplain InputStream#available()}.
     *
     * @return an estimate of the number of bytes
     * @throws ReflectionException
     *             if a data error occurs
     */
    public int available() {
        try {
            return in.available();
        } catch (final IOException e) {
            throw new ReflectionException(e);
        }
    }

    /**
     * Returns whether this input stream supports the mark and reset methods.
     * <p>
     * See {@linkplain InputStream#markSupported()}.
     *
     * @return whether this input stream supports the mark and reset methods
     */
    public boolean markSupported() {
        return in.markSupported();
    }

    /**
     * Skips over and discards n bytes of data from this input stream, the
     * actual number of bytes skipped is returned.
     * <p>
     * See {@linkplain InputStream#skip(long)}.
     *
     * @param n
     *            the number of bytes to be skipped, positive
     * @return the actual number of bytes skipped is returned
     * @throws IllegalArgumentException
     *             thrown if n is not positive
     * @throws ReflectionException
     *             if a data error occurs
     */
    public long skip(final long n) {
        try {
            Checker.checkPositive(n, "Skipped bytes should be positive!");
            return in.skip(n);
        } catch (final IOException e) {
            throw new ReflectionException(e);
        }
    }

    /**
     * Marks the current position in this input stream.
     * <p>
     * See {@linkplain InputStream#mark(int)}.
     *
     * @param readlimit
     *            the maximum limit of bytes that can be read before the mark
     *            position becomes invalid, positive
     * @throws IllegalArgumentException
     *             thrown if special maximum limit is less than 0
     */
    public void mark(final int readlimit) {
        Checker
                .checkPositive(readlimit, "Limit of bytes should be positive!");
        in.mark(readlimit);
    }

    /**
     * Repositions this stream to the position at the time the mark method was
     * last called on this input stream.
     * <p>
     * See {@linkplain InputStream#reset()}.
     *
     * @throws ReflectionException
     *             if a data error occurs
     */
    public void reset() {
        try {
            in.reset();
        } catch (final IOException e) {
            throw new ReflectionException(e);
        }
    }

    /**
     * Returns next byte as an int from 0 to 255, or -1 if reach to the end.
     *
     * @return next byte as an int from 0 to 255, or -1 if reach to the end
     * @throws ReflectionException
     *             if a data error occurs
     */
    public int nextByteAsInt() {
        try {
            return in.read();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Reads next byte.
     *
     * @return next byte
     * @throws ReflectionException
     *             if a data error occurs or reach to the end of stream
     */
    public byte nextByte() {
        try {
            final int i = in.read();
            if (i == -1) {
                throw new EOFException();
            }
            return (byte) i;
        } catch (final IOException e) {
            throw new ReflectionException(e);
        }
    }

    /**
     * Reads next short.
     *
     * @return next short
     * @throws ReflectionException
     *             if a data error occurs or reach to the end of stream
     */
    public short nextShort() {
        try {
            final int i1 = in.read();
            final int i2 = in.read();
            if (i1 == -1 || i2 == -1) {
                throw new EOFException();
            }
            return (short) ((i1 << Byte.SIZE) | i2);
        } catch (final IOException e) {
            throw new ReflectionException(e);
        }
    }

    /**
     * Reads next char.
     *
     * @return next char
     * @throws ReflectionException
     *             if a data error occurs or reach to the end of stream
     */
    public char nextChar() {
        try {
            final int i1 = in.read();
            final int i2 = in.read();
            if (i1 == -1 || i2 == -1) {
                throw new EOFException();
            }
            return (char) ((i1 << Byte.SIZE) | i2);
        } catch (final IOException e) {
            throw new ReflectionException(e);
        }
    }

    /**
     * Reads next int.
     *
     * @return next int
     * @throws ReflectionException
     *             if a data error occurs or reach to the end of stream
     */
    public int nextInt() {
        try {
            final int i1 = in.read();
            final int i2 = in.read();
            final int i3 = in.read();
            final int i4 = in.read();
            if (i1 == -1 || i2 == -1 || i3 == -1 || i4 == -1) {
                throw new EOFException();
            }
            return (i1 << 24) | (i2 << 16) | (i3 << Byte.SIZE) | i4;
        } catch (final IOException e) {
            throw new ReflectionException(e);
        }
    }

    /**
     * Reads next float.
     *
     * @return next float
     * @throws ReflectionException
     *             if a data error occurs or reach to the end of stream
     */
    public float nextFloat() {
        try {
            final int i1 = in.read();
            final int i2 = in.read();
            final int i3 = in.read();
            final int i4 = in.read();
            if (i1 == -1 || i2 == -1 || i3 == -1 || i4 == -1) {
                throw new EOFException();
            }
            return Float.intBitsToFloat((i1 << 24) | (i2 << 16)
                    | (i3 << Byte.SIZE) | i4);
        } catch (final IOException e) {
            throw new ReflectionException(e);
        }
    }

    /**
     * Reads next long.
     *
     * @return next long
     * @throws ReflectionException
     *             if a data error occurs or reach to the end of stream
     */
    public long nextLong() {
        try {
            int i1 = in.read();
            int i2 = in.read();
            int i3 = in.read();
            int i4 = in.read();
            if (i1 == -1 || i2 == -1 || i3 == -1 || i4 == -1) {
                throw new EOFException();
            }
            final long l1 = ((i1 << 24) | (i2 << 16) | (i3 << Byte.SIZE) | i4) & 0x00000000ffffffffL;
            i1 = in.read();
            i2 = in.read();
            i3 = in.read();
            i4 = in.read();
            if (i1 == -1 || i2 == -1 || i3 == -1 || i4 == -1) {
                throw new EOFException();
            }
            final long l2 = ((i1 << 24) | (i2 << 16) | (i3 << Byte.SIZE) | i4) & 0x00000000ffffffffL;
            return (l1 << 32) | l2;
        } catch (final IOException e) {
            throw new ReflectionException(e);
        }
    }

    /**
     * Reads next double.
     *
     * @return next double
     * @throws ReflectionException
     *             if a data error occurs or reach to the end of stream
     */
    public double nextDouble() {
        try {
            int i1 = in.read();
            int i2 = in.read();
            int i3 = in.read();
            int i4 = in.read();
            if (i1 == -1 || i2 == -1 || i3 == -1 || i4 == -1) {
                throw new EOFException();
            }
            final long l1 = ((i1 << 24) | (i2 << 16) | (i3 << Byte.SIZE) | i4) & 0x00000000ffffffffL;
            i1 = in.read();
            i2 = in.read();
            i3 = in.read();
            i4 = in.read();
            if (i1 == -1 || i2 == -1 || i3 == -1 || i4 == -1) {
                throw new EOFException();
            }
            final long l2 = ((i1 << 24) | (i2 << 16) | (i3 << Byte.SIZE) | i4) & 0x00000000ffffffffL;
            return Double.longBitsToDouble((l1 << 32) | l2);
        } catch (final IOException e) {
            throw new ReflectionException(e);
        }
    }

    /**
     * Reads data into special byte array to fill it, the actual number of read
     * data will be returned.
     * <p>
     * See {@linkplain InputStream#read(byte[])}.
     *
     * @param dest
     *            special byte array, not null
     * @return the actual number of read data will be returned
     * @throws NullPointerException
     *             if dest array is null
     * @throws ReflectionException
     *             if a data error occurs
     */
    public int read(byte[] dest) {
        dest = Objects.requireNonNull(dest);
        try {
            return in.read(dest);
        } catch (final IOException e) {
            throw new ReflectionException(e);
        }
    }

    /**
     * Reads up to length bytes of data from the input stream into an array of
     * bytes.
     * <p>
     * See {@linkplain InputStream#read(byte[], int, int)}.
     *
     * @param dest
     *            the buffer into which the data is read, not null
     * @param startIndex
     *            the start index in array dest at which the data is written in
     *            bounds
     * @param length
     *            the maximum number of bytes to read in bounds
     * @return the total number of bytes read into the buffer, or -1 if there is
     *         no more data because the end of the stream has been reached
     * @throws NullPointerException
     *             if dest array is null
     * @throws IllegalArgumentException
     *             if index and length are illegal
     * @throws OutOfBoundsException
     *             if start index + length out of bounds
     * @throws ReflectionException
     *             if a data error occurs
     */
    public int read(byte[] dest, final int startIndex, final int length) {
        dest = Objects.requireNonNull(dest);
        Checker.checkIndex(startIndex);
        Checker.checkLengthPositive(length);
        Checker.checkInArrayBounds(startIndex, length, dest.length);
        try {
            return in.read(dest, startIndex, length);
        } catch (final IOException e) {
            throw new ReflectionException(e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException
     *             if dest array is null
     * @throws ReflectionException
     *             if a data error occurs
     */
    @Override
    public long read(final Bitsss dest) {
        final int len = dest.lengthInBytes() % Byte.SIZE == 0 ? (int) (dest
                .lengthInBytes() / Byte.SIZE)
                : (int) (dest.lengthInBytes() / Byte.SIZE) + 1;
        final Bitsss block = read(len * 8L);
        block.copy(dest);
        return block.lengthInBits();
    }

    /**
     * {@inheritDoc}
     * <p>
     * The length in bits must be multiple of bytes.
     *
     * @throws IllegalArgumentException
     *             if length in bits is negative or not multiple of bytes
     * @throws ReflectionException
     *             if a data error occurs
     */
    @Override
    public Bitsss read(final long lengthInBits) {
        Checker.checkPositive(lengthInBits);
        Checker.checkLengthIsMutipleOfByte(lengthInBits);
        final int length = (int) (lengthInBits % Byte.SIZE);
        int availableLength;
        int actualLength;
        byte[] array;
        try {
            availableLength = in.available();
            array = new byte[Math.min(length, availableLength)];
            actualLength = in.read(array);
            final Bitsss block = MemAllocator.wrap(array, 0, 0, actualLength - 1,
                    7);
            return block;
        } catch (final IOException e) {
            throw new ReflectionException(e);
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * The length in bits must be multiple of bytes.
     *
     * @throws IllegalArgumentException
     *             if length in bits is negative or not multiple of bytes
     * @throws ReflectionException
     *             if a data error occurs
     */
    @Override
    public Stream<Bitsss> stream(final long lengthInBits) {
        Checker.checkPosition(lengthInBits);
        Checker.checkLengthIsMutipleOfByte(lengthInBits);
        final int available = available();
        final Counter c = new Counter(available * 8L);
        final Counter limit = new Counter();
        return Stream.generate(() -> {
            Bitsss block = null;
            if (c.getCounter() >= lengthInBits) {
                block = read(lengthInBits);
            } else {
                block = read(c.getCounter());
            }
            if (c.getCounter() > 0) {
                limit.increment();
            }
            c.decrement(lengthInBits);
            return block;
        }).limit(limit.getCounter());
    }

    /**
     * Converts this input stream to a data input.
     * <p>
     * Instance from this method are same at every calling.
     *
     * @return a data input, not null
     */
    public DataInput toDataInputStream() {
        if (dataInput == null) {
            dataInput = new DataInputStream(in);
        }
        return dataInput;
    }

    /**
     * Converts this input stream to a buffered reader with default charset.
     * <p>
     * Instance from this method will be buffered and the same instance will be
     * returned at next calling.
     *
     * @return a buffered reader, not null
     */
    public BufferedReader toBufferedReader() {
        if (bufferedReader == null) {
            bufferedReader = new BufferedReader(new InputStreamReader(in));
        }
        return bufferedReader;
    }

    /**
     * Converts this input stream to a buffered reader with special charset
     * name.
     * <p>
     * New instance will be create at every calling.
     *
     * @param charsetName
     *            charset name, not null
     * @return a buffered reader, not null
     * @throws ReflectionException
     *             if any error occurs
     */
    public BufferedReader toBufferedReader(final String charsetName) {
        try {
            return new BufferedReader(new InputStreamReader(in, charsetName));
        } catch (final UnsupportedEncodingException e) {
            throw new ReflectionException(e);
        }
    }

    /**
     * Converts this stream to a scanner.
     * <p>
     * Instance from this method will be buffered and the same instance will be
     * returned at next calling.
     *
     * @return a scanner, not null
     */
    public Scanner toScanner() {
        if (scanner == null) {
            scanner = new Scanner(in);
        }
        return scanner;
    }

    /**
     * Close this stream.
     * <p>
     * See {@linkplain InputStream#close()}.
     *
     * @throws ReflectionException
     *             if any error occur
     */
    @Override
    public void close() {
        try {
            in.close();
        } catch (final IOException e) {
            throw new ReflectionException(e);
        }
    }

    /**
     * Close this stream.
     * <p>
     * This method is same with {@linkplain #close()}, the difference is, this
     * method declares an {@linkplain IOException} may be thrown.
     *
     * @throws IOException
     *             any IO error occur
     */
    public void closeThrowable() throws IOException {
        in.close();
    }

    @Override
    public InputStream adaptee() {
        return in;
    }

    @Override
    public InputStreamAdapter sub(final long startPos, final long endPos) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public InputStreamAdapter subClone(final long startPos, final long endPos) {
        // TODO Auto-generated method stub
        return null;
    }
}
