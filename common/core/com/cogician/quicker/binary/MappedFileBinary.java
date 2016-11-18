package com.cogician.quicker.binary;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

import com.cogician.quicker.Checker;
import com.cogician.quicker.Quicker;
import com.cogician.quicker.QuickerProperties;

/**
 * <p>
 * This class uses {@linkplain MappedByteBuffer} to implement {@linkplain FileBinary}. This class is buffered, if buffer
 * size is not specified when constructing, use default.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-08-31T23:09:28+08:00
 * @since 0.0.0, 2016-08-31T23:09:28+08:00
 */
public class MappedFileBinary extends AbstractBinary implements FileBinary {

    private static final int DEFAULT_BUFFER_SIZE = QuickerProperties.get("io.buffer.size").asInt();

    private static RandomAccessFile getRandomAccessFile(File file, String mode)
            throws NullPointerException, IllegalArgumentException, BinaryException {
        try {
            return new RandomAccessFile(Quicker.require(file), mode);
        } catch (FileNotFoundException e) {
            throw new BinaryException(e);
        }
    }

    private final File file;

    private final MappedByteBufferReadWrite base;

    /**
     * <p>
     * Constructs with specified file name and mode, big-endian. The mode is same as mode of
     * {@linkplain MappedByteBuffer}.
     * </p>
     * 
     * @param file
     *            specified file name
     * @param mode
     *            specified mode
     * @throws NullPointerException
     *             if specified file name or mode is null
     * @throws BinaryException
     *             if file not found and cannot be created, or other IO problem occurs
     * @since 0.0.0
     */
    public MappedFileBinary(String fileName, MapMode mode) throws NullPointerException, BinaryException {
        this(fileName, mode, null, DEFAULT_BUFFER_SIZE);
    }

    /**
     * <p>
     * Constructs with specified file name, mode, byte order processor and buffer size. The mode is same as mode of
     * {@linkplain MappedByteBuffer}. If given byte order processor is null, use default
     * {@linkplain ByteOrderProcessor#BIG_ENDIAN}. The buffer size cannot be less than 8.
     * </p>
     * 
     * @param file
     *            specified file name
     * @param mode
     *            specified mode
     * @param orderProcessor
     *            specified byte order processor
     * @param bufferSize
     *            specified buffer size
     * @throws NullPointerException
     *             if specified file name or mode is null
     * @throws IllegalArgumentException
     *             if buffer size is illegal
     * @throws BinaryException
     *             if file not found and cannot be created, or other IO problem occurs
     * @since 0.0.0
     */
    public MappedFileBinary(String fileName, MapMode mode, ByteOrderProcessor orderProcessor, int bufferSize)
            throws NullPointerException, IllegalArgumentException, BinaryException {
        this(new File(Quicker.require(fileName)), mode, orderProcessor, bufferSize);
    }

    /**
     * <p>
     * Constructs with specified file and mode, big-endian. The mode is same as mode of {@linkplain RandomAccessFile}.
     * </p>
     * 
     * @param file
     *            specified file
     * @param mode
     *            specified mode
     * @throws NullPointerException
     *             if specified file or mode is null
     * @throws BinaryException
     *             if file not found and cannot be created, or other IO problem occurs
     * @since 0.0.0
     */
    public MappedFileBinary(File file, MapMode mode)
            throws NullPointerException, IllegalArgumentException, BinaryException {
        this(file, mode, null, DEFAULT_BUFFER_SIZE);
    }

    /**
     * <p>
     * Constructs with specified file, mode, byte order processor and buffer size. The mode is same as mode of
     * {@linkplain MappedByteBuffer}. If given byte order processor is null, use default
     * {@linkplain ByteOrderProcessor#BIG_ENDIAN}. The buffer size cannot be less than 8.
     * </p>
     * 
     * @param file
     *            specified file
     * @param mode
     *            specified mode
     * @param orderProcessor
     *            specified byte order processor
     * @param bufferSize
     *            specified buffer size
     * @throws NullPointerException
     *             if specified file or mode is null
     * @throws IllegalArgumentException
     *             if buffer size is illegal
     * @throws BinaryException
     *             if file not found and cannot be created, or other IO problem occurs
     * @since 0.0.0
     */
    public MappedFileBinary(File file, MapMode mode, ByteOrderProcessor orderProcessor, int bufferSize)
            throws NullPointerException, IllegalArgumentException, BinaryException {
        super(new MappedByteBufferReadWrite(Quicker.require(file), mode, bufferSize), orderProcessor);
        this.file = file;
        this.base = (MappedByteBufferReadWrite)getBaseReadWrite();
    }

    @Override
    public long length() {
        return file.length();
    }

    @Override
    public void setLength(long length) throws BinaryException {
        try (RandomAccessFile raf = new RandomAccessFile(file, "rws")) {
            raf.setLength(length);
        } catch (IOException e) {
            throw new BinaryException(e);
        }
    }

    @Override
    public void copy(long srcIndex, Binary dest, long destIndex, long length) throws NullPointerException,
            IndexOutOfBoundsException, IllegalArgumentException, BinaryException, BinaryException {
        Checker.checkNull(dest);
        Checker.checkLength(length);
        Checker.checkRangeIndexes(srcIndex, srcIndex + length, length());
        Checker.checkRangeIndexes(destIndex, destIndex + length, dest.length());
        long offset = 0;
        long remainder = length;
        while (remainder > 0) {
            Binary bin = base.readBuffer(srcIndex + offset);
            long actual = Math.min(bin.length(), remainder);
            bin.copy(0, dest, destIndex + offset, actual);
            offset += actual;
            remainder -= actual;
        }
    }

    @Override
    public byte[] wrappedArray() {
        return null;
    }

    @Override
    public void flush() throws BinaryException {
        base.flush();
    }

    @Override
    public void close() throws BinaryException {
        flush();
    }

    @Override
    public void sync() throws BinaryException {
        flush();
    }

    @Override
    public Inputer fileInputer(long index) throws IndexOutOfBoundsException {
        Checker.checkIndex(index, length());
        try {
            return new QuickInputer(new InputStream() {

                private final RandomAccessFile raf = new RandomAccessFile(file, "r");

                {
                    raf.seek(index);
                }

                @Override
                public int read() throws IOException {
                    return raf.read();
                }

                @Override
                public int read(byte[] b, int off, int len) throws IOException {
                    return raf.read(b, off, len);
                }

                @Override
                public void close() throws IOException {
                    raf.close();
                }
            }, getByteOrderProcessor(), () -> file.length() - index, DEFAULT_BUFFER_SIZE);
        } catch (NullPointerException | IllegalArgumentException | IOException e) {
            throw new BinaryException(e);
        }
    }

    @Override
    public Outputer fileOutputer(long index) throws IndexOutOfBoundsException {
        if (index < 0) {
            throw new IndexOutOfBoundsException("Index should be 0 or positive.");
        }
        try {
            return new QuickOutputer(new OutputStream() {

                private final RandomAccessFile raf = new RandomAccessFile(file, "rw");

                {
                    raf.seek(index);
                }

                @Override
                public void write(int b) throws IOException {
                    raf.write(b);
                }

                @Override
                public void write(byte[] b, int off, int len) throws IOException {
                    raf.write(b, off, len);
                }

                @Override
                public void close() throws IOException {
                    raf.close();
                }
            }, DEFAULT_BUFFER_SIZE);
        } catch (NullPointerException | IllegalArgumentException | IOException e) {
            throw new BinaryException(e);
        }
    }

    private static class MappedByteBufferReadWrite implements BaseReadWrite {

        private final File file;

        private final FileChannel channel;

        private final MapMode mode;

        private final int bufferSize;

        private MappedByteBuffer buffer;

        private long curPos = Long.MAX_VALUE;

        private int size = -1;

        private boolean bufferChanged = false;

        private MappedByteBufferReadWrite(File file, MapMode mode, int bufferSize)
                throws NullPointerException, IllegalArgumentException, BinaryException {
            this.file = Quicker.require(file);
            this.mode = Quicker.require(mode);
            if (bufferSize < 8) {
                throw new IllegalArgumentException("Buffer size cannot less than 8.");
            }
            this.bufferSize = bufferSize;
            if (mode == MapMode.READ_ONLY) {
                this.channel = getRandomAccessFile(file, "r").getChannel();
            } else {
                this.channel = getRandomAccessFile(file, "rw").getChannel();
            }
        }

        private void fillBuffer(long index) throws BinaryException {
            try {
                flush();
                this.buffer = channel.map(mode, index, Math.min(bufferSize, file.length() - index));
                this.curPos = index;
                this.size = buffer.limit();
            } catch (IOException e) {
                throw new BinaryException(e);
            }
        }

        private boolean inBuffer(long index, int bytesNum) {
            return size > 0 && index >= curPos && index + bytesNum < curPos + size;
        }

        @Override
        public byte _getByte(long index) throws IndexOutOfBoundsException, BinaryException {
            if (!inBuffer(index, 1)) {
                fillBuffer(index);
            }
            return buffer.get((int)(index - curPos));
        }

        @Override
        public short _getShort(long index) throws IndexOutOfBoundsException, BinaryException {
            if (!inBuffer(index, 2)) {
                fillBuffer(index);
            }
            return buffer.getShort((int)(index - curPos));
        }

        @Override
        public char _getChar(long index) throws IndexOutOfBoundsException, BinaryException {
            if (!inBuffer(index, 2)) {
                fillBuffer(index);
            }
            return buffer.getChar((int)(index - curPos));
        }

        @Override
        public int _getMedium(long index) throws IndexOutOfBoundsException, BinaryException {
            if (!inBuffer(index, 3)) {
                fillBuffer(index);
            }
            int i1 = buffer.get((int)(index - curPos));
            int i2 = BytesUtil.toUnsignedShort(buffer.getShort((int)(index - curPos + 1)));
            return (i1 << 16) | i2;
        }

        @Override
        public int _getInt(long index) throws IndexOutOfBoundsException, BinaryException {
            if (!inBuffer(index, 4)) {
                fillBuffer(index);
            }
            return buffer.getInt((int)(index - curPos));
        }

        @Override
        public long _getLong(long index) throws IndexOutOfBoundsException, BinaryException {
            if (!inBuffer(index, 8)) {
                fillBuffer(index);
            }
            return buffer.getLong((int)(index - curPos));
        }

        @Override
        public void _setByte(long index, int value) throws IndexOutOfBoundsException, BinaryException {
            if (!inBuffer(index, 1)) {
                fillBuffer(index);
            }
            buffer.put((int)(index - curPos), (byte)value);
            bufferChanged = true;
        }

        @Override
        public void _setShort(long index, int value) throws IndexOutOfBoundsException, BinaryException {
            if (!inBuffer(index, 2)) {
                fillBuffer(index);
            }
            buffer.putShort((int)(index - curPos), (short)value);
            bufferChanged = true;
        }

        @Override
        public void _setChar(long index, int value) throws IndexOutOfBoundsException, BinaryException {
            if (!inBuffer(index, 2)) {
                fillBuffer(index);
            }
            buffer.putChar((int)(index - curPos), (char)value);
            bufferChanged = true;
        }

        @Override
        public void _setMedium(long index, int value) throws IndexOutOfBoundsException, BinaryException {
            if (!inBuffer(index, 3)) {
                fillBuffer(index);
            }
            buffer.put((int)(index - curPos), (byte)(value >> 16));
            buffer.putShort((int)(index - curPos + 1), (short)value);
            bufferChanged = true;
        }

        @Override
        public void _setInt(long index, int value) throws IndexOutOfBoundsException, BinaryException {
            if (!inBuffer(index, 4)) {
                fillBuffer(index);
            }
            buffer.putInt((int)(index - curPos), value);
            bufferChanged = true;
        }

        @Override
        public void _setLong(long index, long value) throws IndexOutOfBoundsException, BinaryException {
            if (!inBuffer(index, 8)) {
                fillBuffer(index);
            }
            buffer.putLong((int)(index - curPos), value);
            bufferChanged = true;
        }

        public Binary readBuffer(long index) {
            fillBuffer(index);
            byte[] b = new byte[size];
            buffer.get(b);
            return Binary.wrap(b, 0, size, null);
        }

        public void flush() {
            if (size > 0 && bufferChanged) {
                buffer.force();
            }
            size = -1;
            bufferChanged = false;
        }
    }
}
