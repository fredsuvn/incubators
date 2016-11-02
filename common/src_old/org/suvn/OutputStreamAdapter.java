package com.cogician.quicker;

import java.io.BufferedWriter;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Objects;

import com.cogician.quicker.util.reflect.ReflectionException;

/**
 * An encapsulated output stream to convenient to write data.
 *
 * @author Fred Suvn
 * @version 0.0.0, 2015-06-30 11:14:19
 */
public class OutputStreamAdapter implements DataOutputAdapter<OutputStream>,
        AutoCloseable {
    /**
     * Output stream, the source of data, never null.
     */
    private final OutputStream out;

    /**
     * Buffer of data output stream, buffer when
     * {@linkplain #toDataOutputStream()}
     */
    private DataOutputStream dataOutput;

    /**
     * Buffer of buffered writer, buffer when {@linkplain #toBufferedWriter()}
     * called.
     */
    private BufferedWriter bufferedWriter;

    /**
     * Constructs with special output stream.
     *
     * @param in
     *            special output stream, not null
     * @throws NullPointerException
     *             thrown if given output stream is null
     */
    public OutputStreamAdapter(final OutputStream out) {
        this.out = Objects.requireNonNull(out);
        this.dataOutput = new DataOutputStream(out);
    }

    /**
     * Flushes this output stream and forces any buffered output bytes to be
     * written out.
     * <p>
     * See {@linkplain OutputStream#flush()}.
     *
     * @throws ReflectionException
     *             if any error occurs
     */
    public void flush() {
        try {
            out.flush();
        } catch (final IOException e) {
            throw new ReflectionException(e);
        }
    }

    /**
     * Write a byte.
     *
     * @param v
     *            written value
     * @throws ReflectionException
     *             if a data error occurs
     */
    public void writeByte(final byte v) {
        try {
            out.write(v);
        } catch (final IOException e) {
            throw new ReflectionException(e);
        }
    }

    /**
     * Write a short.
     *
     * @param v
     *            written value
     * @throws ReflectionException
     *             if a data error occurs
     */
    public void writeShort(final short v) {
        try {
            out.write((v >>> 8) & 0xFF);
            out.write((v >>> 0) & 0xFF);
        } catch (final IOException e) {
            throw new ReflectionException(e);
        }
    }

    /**
     * Write a char.
     *
     * @param v
     *            written value
     * @throws ReflectionException
     *             if a data error occurs
     */
    public void writeChar(final char v) {
        try {
            out.write((v >>> 8) & 0xFF);
            out.write((v >>> 0) & 0xFF);
        } catch (final IOException e) {
            throw new ReflectionException(e);
        }
    }

    /**
     * Write a int.
     *
     * @param v
     *            written value
     * @throws ReflectionException
     *             if a data error occurs
     */
    public void writeInt(final int v) {
        try {
            out.write((v >>> 24) & 0xFF);
            out.write((v >>> 16) & 0xFF);
            out.write((v >>> 8) & 0xFF);
            out.write((v >>> 0) & 0xFF);
        } catch (final IOException e) {
            throw new ReflectionException(e);
        }
    }

    /**
     * Write a float.
     *
     * @param v
     *            written value
     * @throws ReflectionException
     *             if a data error occurs
     */
    public void writeFloat(final float v) {
        writeInt(Float.floatToIntBits(v));
    }

    /**
     * Write a long.
     *
     * @param v
     *            written value
     * @throws ReflectionException
     *             if a data error occurs
     */
    public void writeLong(final long v) {
        try {
            out.write((byte) (v >>> 56));
            out.write((byte) (v >>> 48));
            out.write((byte) (v >>> 40));
            out.write((byte) (v >>> 32));
            out.write((byte) (v >>> 24));
            out.write((byte) (v >>> 16));
            out.write((byte) (v >>> 8));
            out.write((byte) (v >>> 0));
        } catch (final IOException e) {
            throw new ReflectionException(e);
        }
    }

    /**
     * Write a double.
     *
     * @param v
     *            written value
     * @throws ReflectionException
     *             if a data error occurs
     */
    public void writeDouble(final double v) {
        writeLong(Double.doubleToLongBits(v));
    }

    /**
     * {@inheritDoc}
     * <p>
     * Special data block'length should be multiple of bytes.
     *
     * @throws NullPointerException
     *             thrown if special block is null
     * @throws ReflectionException
     *             thrown if special block'length is not multiple of bytes or
     *             other data error occurs
     */
    @Override
    public long write(final Bitsss data) {
        Checker.checkLengthIsMutipleOfByte(Objects.requireNonNull(data)
                .lengthInBits());
        try {
            out.write(data.toByteArray());
        } catch (final IOException e) {
            throw new ReflectionException(e);
        }
        return data.lengthInBits();
    }

    /**
     * Converts this output stream to a data input.
     * <p>
     * Instance from this method are same at every calling.
     *
     * @return a data input, not null
     */
    public DataOutput toDataOutputStream() {
        if (dataOutput == null) {
            dataOutput = new DataOutputStream(dataOutput);
        }
        return dataOutput;
    }

    /**
     * Converts this output stream to a buffered writer with default charset.
     * <p>
     * Instance from this method will be buffered and the same instance will be
     * returned at next calling.
     *
     * @return a buffered writer, not null
     */
    public BufferedWriter toBufferedWriter() {
        if (bufferedWriter == null) {
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(out));
        }
        return bufferedWriter;
    }

    /**
     * Close this stream.
     * <p>
     * See {@linkplain OutputStream#close()}.
     *
     * @throws ReflectionException
     *             if any error occur
     */
    @Override
    public void close() {
        try {
            out.close();
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
        out.close();
    }

    @Override
    public OutputStream adaptee() {
        return out;
    }

}
