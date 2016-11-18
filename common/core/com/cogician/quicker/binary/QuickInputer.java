package com.cogician.quicker.binary;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.LongSupplier;

import javax.annotation.Nullable;

import com.cogician.quicker.Checker;
import com.cogician.quicker.Quicker;
import com.cogician.quicker.QuickerProperties;

/**
 * <p>
 * Inputer which inputs data from an {@linkplain InputStream} or a file. This class is buffered.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-08-22T21:36:37+08:00
 * @since 0.0.0, 2016-08-22T21:36:37+08:00
 */
public class QuickInputer extends Inputer {

    private static final int DEFAULT_BUFFER_SIZE = QuickerProperties.get("io.buffer.size").asInt();

    private final DataInputStream source;

    private final ByteOrderProcessor orderProcessor;

    private final StreamPointer pointer;

    /**
     * <p>
     * Constructs with specified file name. This inputer uses big-endian.
     * </p>
     * 
     * @param file
     *            specified file name
     * @throws NullPointerException
     *             if specified file is null
     * @throws FileNotFoundException
     *             if file not found
     * @since 0.0.0
     */
    public QuickInputer(String fileName) throws FileNotFoundException {
        this(fileName, null, DEFAULT_BUFFER_SIZE);
    }

    /**
     * <p>
     * Constructs with specified file name, byte order processor and buffer size. If specified byte order processor is
     * null, use default big-endian processor.
     * </p>
     * 
     * @param file
     *            specified file name
     * @param orderProcessor
     *            specified byte order processor
     * @param bufferSize
     *            size of buffer
     * @throws NullPointerException
     *             if specified file is null
     * @throws IllegalArgumentException
     *             if buffer size &lt;= 0
     * @throws FileNotFoundException
     *             if file not found
     * @since 0.0.0
     */
    public QuickInputer(String fileName, @Nullable ByteOrderProcessor orderProcessor, int bufferSize)
            throws FileNotFoundException {
        this(new File(fileName), orderProcessor, bufferSize);
    }

    /**
     * <p>
     * Constructs with specified file. This inputer uses big-endian.
     * </p>
     * 
     * @param file
     *            specified file
     * @throws NullPointerException
     *             if specified file is null
     * @throws FileNotFoundException
     *             if file not found
     * @since 0.0.0
     */
    public QuickInputer(File file) throws FileNotFoundException {
        this(file, null, DEFAULT_BUFFER_SIZE);
    }

    /**
     * <p>
     * Constructs with specified file, byte order processor and buffer size. If specified byte order processor is null,
     * use default big-endian processor.
     * </p>
     * 
     * @param file
     *            specified file
     * @param orderProcessor
     *            specified byte order processor
     * @param bufferSize
     *            size of buffer
     * @throws NullPointerException
     *             if specified file is null
     * @throws IllegalArgumentException
     *             if buffer size &lt;= 0
     * @throws FileNotFoundException
     *             if file not found
     * @since 0.0.0
     */
    public QuickInputer(File file, @Nullable ByteOrderProcessor orderProcessor, int bufferSize)
            throws NullPointerException, IllegalArgumentException, FileNotFoundException {
        Checker.checkNull(file);
        this.source = new DataInputStream(new BufferedInputStream(new FileInputStream(file), bufferSize));
        this.orderProcessor = null == orderProcessor ? ByteOrderProcessor.BIG_ENDIAN : orderProcessor;
        this.pointer = StreamPointer.ofLength(() -> file.length());
    }

    /**
     * <p>
     * Constructs with specified input stream. This inputer uses big-endian and its length is considered as unlimited.
     * </p>
     * 
     * @param source
     *            specified input stream
     * @throws NullPointerException
     *             if specified input stream is null
     * @since 0.0.0
     */
    public QuickInputer(InputStream source) throws NullPointerException {
        this(source, null, -1, DEFAULT_BUFFER_SIZE);
    }

    /**
     * <p>
     * Constructs with specified input stream and buffer size. This inputer uses big-endian and its length is considered
     * as unlimited.
     * </p>
     * 
     * @param source
     *            specified input stream
     * @param bufferSize
     *            size of buffer
     * @throws NullPointerException
     *             if specified input stream is null
     * @throws IllegalArgumentException
     *             if buffer size &lt;= 0
     * @since 0.0.0
     */
    public QuickInputer(InputStream source, int bufferSize) throws NullPointerException, IllegalArgumentException {
        this(source, null, -1, bufferSize);
    }

    /**
     * <p>
     * Constructs with specified input stream, byte order processor, length of input stream and buffer size. If
     * specified byte order processor is null, use default big-endian processor. If length of input stream is
     * inestimable, set -1.
     * </p>
     * 
     * @param source
     *            specified input stream
     * @param orderProcessor
     *            specified byte order processor
     * @param length
     *            length of input stream
     * @param bufferSize
     *            size of buffer
     * @throws NullPointerException
     *             if specified input stream is null
     * @throws IllegalArgumentException
     *             if length &lt; 0 && length != -1 buffer size &lt;= 0
     * @since 0.0.0
     */
    public QuickInputer(InputStream source, @Nullable ByteOrderProcessor orderProcessor, long length, int bufferSize)
            throws NullPointerException, IllegalArgumentException {
        this.source = new DataInputStream(new BufferedInputStream(Quicker.require(source), bufferSize));
        this.orderProcessor = null == orderProcessor ? ByteOrderProcessor.BIG_ENDIAN : orderProcessor;
        this.pointer = StreamPointer.ofLength(length);
    }

    /**
     * <p>
     * Constructs with specified input stream, byte order processor, length supplier and buffer size. If specified byte
     * order processor is null, use default big-endian processor.
     * </p>
     * 
     * @param source
     *            specified input stream
     * @param orderProcessor
     *            specified byte order processor
     * @param lengthSupplier
     *            specified length supplier
     * @param bufferSize
     *            size of buffer
     * @throws NullPointerException
     *             if specified input stream or length supplier is null
     * @throws IllegalArgumentException
     *             if length &lt; 0 && length != -1 buffer size &lt;= 0
     * @since 0.0.0
     */
    public QuickInputer(InputStream source, @Nullable ByteOrderProcessor orderProcessor, LongSupplier lengthSupplier,
            int bufferSize) throws NullPointerException, IllegalArgumentException {
        this.source = new DataInputStream(new BufferedInputStream(Quicker.require(source), bufferSize));
        this.orderProcessor = null == orderProcessor ? ByteOrderProcessor.BIG_ENDIAN : orderProcessor;
        this.pointer = StreamPointer.ofLength(lengthSupplier);
    }

    @Override
    public ByteOrderProcessor getByteOrderProcessor() {
        return orderProcessor;
    }

    @Override
    protected byte _readByte() throws EOFException, IOException {
        byte v = source.readByte();
        pointer.increase(1);
        return v;
    }

    @Override
    protected short _readShort() throws EOFException, IOException {
        short v = source.readShort();
        pointer.increase(2);
        return v;
    }

    @Override
    protected char _readChar() throws EOFException, IOException {
        char v = source.readChar();
        pointer.increase(2);
        return v;
    }

    @Override
    protected int _readMedium() throws EOFException, IOException {
        int i1 = source.readByte();
        int i2 = source.readUnsignedShort();
        int v = (i1 << 16) | i2;
        pointer.increase(3);
        return v;
    }

    @Override
    protected int _readInt() throws EOFException, IOException {
        int v = source.readInt();
        pointer.increase(4);
        return v;
    }

    @Override
    protected long _readLong() throws EOFException, IOException {
        long v = source.readLong();
        pointer.increase(8);
        return v;
    }

    @Override
    protected float _readFloat() throws EOFException, IOException {
        float v = source.readFloat();
        pointer.increase(4);
        return v;
    }

    @Override
    protected double _readDouble() throws EOFException, IOException {
        double v = source.readDouble();
        pointer.increase(8);
        return v;
    }

    @Override
    public int read() throws IOException {
        int v = source.read();
        if (v != -1) {
            pointer.increase(1);
        }
        return v;
    }

    @Override
    public long length() {
        return pointer.length();
    }

    @Override
    public long remainderLength() {
        return pointer.remainderLength();
    }

    @Override
    public void close() throws IOException {
        source.close();
    }
}
