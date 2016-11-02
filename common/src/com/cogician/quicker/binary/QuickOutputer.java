package com.cogician.quicker.binary;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.SyncFailedException;

import javax.annotation.Nullable;

import com.cogician.quicker.Checker;
import com.cogician.quicker.Quicker;
import com.cogician.quicker.QuickerProperties;

/**
 * <p>
 * Outputer which outputs data into an {@linkplain OutputStream} or a file. This class is buffered.
 * </p>
 * 
 * @author Fred Suvn
 * @version 0.0.0, 2016-08-29T10:53:23+08:00
 * @since 0.0.0, 2016-08-29T10:53:23+08:00
 */
public class QuickOutputer extends Outputer {

    private static final int DEFAULT_BUFFER_SIZE = QuickerProperties.get("io.buffer.size").asInt();

    private final DataOutputStream source;

    private final ByteOrderProcessor orderProcessor;

    private final StreamPointer pointer;

    private final FileDescriptor fd;

    /**
     * <p>
     * Constructs with specified file name. This inputer uses big-endian, and outputs start at beginning of the file.
     * </p>
     * 
     * @param file
     *            specified file name
     * @throws NullPointerException
     *             if specified file is null
     * @throws FileNotFoundException
     *             if file not found and cannot be created
     * @throws IOException
     *             if an I/O error occurs
     * @since 0.0.0
     */
    public QuickOutputer(String fileName) throws NullPointerException, FileNotFoundException, IOException {
        this(fileName, false, null, DEFAULT_BUFFER_SIZE);
    }

    /**
     * <p>
     * Constructs with specified file name, whether write append on end of the file, byte order processor and buffer
     * size. If specified byte order processor is null, use default big-endian processor.
     * </p>
     * 
     * @param file
     *            specified file name
     * @param append
     *            whether write append on end of the file
     * @param orderProcessor
     *            specified byte order processor
     * @param bufferSize
     *            size of buffer
     * @throws NullPointerException
     *             if specified file is null
     * @throws IllegalArgumentException
     *             if buffer size &lt;= 0
     * @throws FileNotFoundException
     *             if file not found and cannot be created
     * @throws IOException
     *             if an I/O error occurs
     * @since 0.0.0
     */
    public QuickOutputer(String fileName, boolean append, @Nullable ByteOrderProcessor orderProcessor, int bufferSize)
            throws NullPointerException, IllegalArgumentException, FileNotFoundException, IOException {
        this(new File(fileName), append, orderProcessor, bufferSize);
    }

    /**
     * <p>
     * Constructs with specified file. This inputer uses big-endian, and outputs start at beginning of the file.
     * </p>
     * 
     * @param file
     *            specified file
     * @throws NullPointerException
     *             if specified file is null
     * @throws FileNotFoundException
     *             if file not found and cannot be created
     * @throws IOException
     *             if an I/O error occurs
     * @since 0.0.0
     */
    public QuickOutputer(File file) throws NullPointerException, FileNotFoundException, IOException {
        this(file, false, null, DEFAULT_BUFFER_SIZE);
    }

    /**
     * <p>
     * Constructs with specified file, whether write append on end of the file, byte order processor and buffer size. If
     * specified byte order processor is null, use default big-endian processor.
     * </p>
     * 
     * @param file
     *            specified file
     * @param append
     *            whether write append on end of the file
     * @param orderProcessor
     *            specified byte order processor
     * @param bufferSize
     *            size of buffer
     * @throws NullPointerException
     *             if specified file is null
     * @throws IllegalArgumentException
     *             if buffer size &lt;= 0
     * @throws FileNotFoundException
     *             if file not found and cannot be created
     * @throws IOException
     *             if an I/O error occurs
     * @since 0.0.0
     */
    public QuickOutputer(File file, boolean append, @Nullable ByteOrderProcessor orderProcessor, int bufferSize)
            throws NullPointerException, IllegalArgumentException, FileNotFoundException, IOException {
        Checker.checkNull(file);
        FileOutputStream fout = new FileOutputStream(file, append);
        this.source = new DataOutputStream(new BufferedOutputStream(fout, bufferSize));
        this.orderProcessor = null == orderProcessor ? ByteOrderProcessor.BIG_ENDIAN : orderProcessor;
        this.pointer = StreamPointer.unlimited();
        this.fd = fout.getFD();
    }

    /**
     * <p>
     * Constructs with specified output stream. This outputer uses big-endian and its length is considered as unlimited.
     * </p>
     * 
     * @param source
     *            specified output stream
     * @throws NullPointerException
     *             if specified output stream is null
     * @throws IOException
     *             if an I/O error occurs
     * @since 0.0.0
     */
    public QuickOutputer(OutputStream source) throws NullPointerException, IllegalArgumentException, IOException {
        this(source, null, -1, DEFAULT_BUFFER_SIZE);
    }

    /**
     * <p>
     * Constructs with specified output stream and buffer size. This outputer uses big-endian and its length is
     * considered as unlimited.
     * </p>
     * 
     * @param source
     *            specified output stream
     * @param bufferSize
     *            size of buffer
     * @throws NullPointerException
     *             if specified output stream is null
     * @throws IllegalArgumentException
     *             if buffer size &lt;= 0
     * @throws IOException
     *             if an I/O error occurs
     * @since 0.0.0
     */
    public QuickOutputer(OutputStream source, int bufferSize)
            throws NullPointerException, IllegalArgumentException, IOException {
        this(source, null, -1, bufferSize);
    }

    /**
     * <p>
     * Constructs with specified output stream, byte order processor, length of output stream and buffer size. If
     * specified byte order processor is null, use default big-endian processor. If length of output stream is
     * inestimable, set -1.
     * </p>
     * 
     * @param source
     *            specified output stream
     * @param orderProcessor
     *            specified byte order processor
     * @param length
     *            length of output stream
     * @param bufferSize
     *            size of buffer
     * @throws NullPointerException
     *             if specified output stream is null
     * @throws IllegalArgumentException
     *             if length &lt; 0 && length != -1 buffer size &lt;= 0
     * @throws IOException
     *             if an I/O error occurs
     * @since 0.0.0
     */
    public QuickOutputer(OutputStream source, @Nullable ByteOrderProcessor orderProcessor, long length, int bufferSize)
            throws NullPointerException, IllegalArgumentException, IOException {
        this.source = new DataOutputStream(new BufferedOutputStream(Quicker.require(source), bufferSize));
        this.orderProcessor = null == orderProcessor ? ByteOrderProcessor.BIG_ENDIAN : orderProcessor;
        this.pointer = StreamPointer.ofLength(length);
        if (source instanceof FileOutputStream) {
            this.fd = ((FileOutputStream)source).getFD();
        } else {
            this.fd = null;
        }
    }

    @Override
    public ByteOrderProcessor getByteOrderProcessor() {
        return orderProcessor;
    }

    @Override
    protected void _writeByte(int value) throws EOFException, IOException {
        source.writeByte(value);
        pointer.increase(1);
    }

    @Override
    protected void _writeShort(int value) throws EOFException, IOException {
        source.writeShort(value);
        pointer.increase(2);
    }

    @Override
    protected void _writeChar(int value) throws EOFException, IOException {
        source.writeChar(value);
        pointer.increase(2);
    }

    @Override
    protected void _writeMedium(int value) throws EOFException, IOException {
        source.writeByte(value >> 16);
        source.writeShort(value);
        pointer.increase(3);
    }

    @Override
    protected void _writeInt(int value) throws EOFException, IOException {
        source.writeInt(value);
        pointer.increase(4);
    }

    @Override
    protected void _writeLong(long value) throws EOFException, IOException {
        source.writeLong(value);
        pointer.increase(8);
    }

    @Override
    protected void _writeFloat(float value) throws EOFException, IOException {
        source.writeFloat(value);
        pointer.increase(4);
    }

    @Override
    protected void _writeDouble(double value) throws EOFException, IOException {
        source.writeDouble(value);
        pointer.increase(8);
    }

    @Override
    public void flush() throws IOException {
        source.flush();
    }

    @Override
    public void sync() throws SyncFailedException {
        if (fd != null) {
            fd.sync();
        }
    }

    @Override
    public void close() throws IOException {
        flush();
        source.close();
    }

    @Override
    public long length() {
        return pointer.length();
    }

    @Override
    public long remainderLength() {
        return pointer.remainderLength();
    }
}
