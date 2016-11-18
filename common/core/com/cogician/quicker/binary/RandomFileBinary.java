package com.cogician.quicker.binary;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;

import com.cogician.quicker.Checker;
import com.cogician.quicker.Quicker;
import com.cogician.quicker.QuickerProperties;

/**
 * <p>
 * This class uses {@linkplain RandomAccessFile} to implement {@linkplain FileBinary}. This class is buffered, if buffer
 * size is not specified when constructing, use default.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-08-30T20:21:36+08:00
 * @since 0.0.0, 2016-08-30T20:21:36+08:00
 */
public class RandomFileBinary extends AbstractBinary implements FileBinary {

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

    private final RandomFileReadWrite base;

    /**
     * <p>
     * Constructs with specified file name and mode, big-endian. The mode is same as mode of
     * {@linkplain RandomAccessFile}.
     * </p>
     * 
     * @param file
     *            specified file name
     * @param mode
     *            specified mode
     * @throws NullPointerException
     *             if specified file name is null
     * @throws IllegalArgumentException
     *             if mode is illegal
     * @throws BinaryException
     *             if file not found and cannot be created, or other IO problem occurs
     * @since 0.0.0
     */
    public RandomFileBinary(String fileName, String mode)
            throws NullPointerException, IllegalArgumentException, BinaryException {
        this(fileName, mode, null, DEFAULT_BUFFER_SIZE);
    }

    /**
     * <p>
     * Constructs with specified file name, mode, byte order processor and buffer size. The mode is same as mode of
     * {@linkplain RandomAccessFile}. If given byte order processor is null, use default
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
     *             if specified file name is null
     * @throws IllegalArgumentException
     *             if mode or buffer size is illegal
     * @throws BinaryException
     *             if file not found and cannot be created, or other IO problem occurs
     * @since 0.0.0
     */
    public RandomFileBinary(String fileName, String mode, ByteOrderProcessor orderProcessor, int bufferSize)
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
     *             if specified file is null
     * @throws IllegalArgumentException
     *             if mode is illegal
     * @throws BinaryException
     *             if file not found and cannot be created, or other IO problem occurs
     * @since 0.0.0
     */
    public RandomFileBinary(File file, String mode)
            throws NullPointerException, IllegalArgumentException, BinaryException {
        this(file, mode, null, DEFAULT_BUFFER_SIZE);
    }

    /**
     * <p>
     * Constructs with specified file, mode, byte order processor and buffer size. The mode is same as mode of
     * {@linkplain RandomAccessFile}. If given byte order processor is null, use default
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
     *             if specified file is null
     * @throws IllegalArgumentException
     *             if mode or buffer size is illegal
     * @throws BinaryException
     *             if file not found and cannot be created, or other IO problem occurs
     * @since 0.0.0
     */
    public RandomFileBinary(File file, String mode, ByteOrderProcessor orderProcessor, int bufferSize)
            throws NullPointerException, IllegalArgumentException, BinaryException {
        super(new RandomFileReadWrite(getRandomAccessFile(Quicker.require(file), mode), bufferSize), null);
        this.file = file;
        this.base = (RandomFileReadWrite)getBaseReadWrite();
    }

    @Override
    public long length() {
        try {
            return base.getSource().length();
        } catch (IOException e) {
            throw new BinaryException(e);
        }
    }

    @Override
    public void setLength(long length) throws BinaryException {
        try {
            base.getSource().setLength(length);
            base.flush();
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
        try {
            flush();
            base.getSource().close();
        } catch (IOException e) {
            throw new BinaryException(e);
        }
    }

    @Override
    public void sync() throws BinaryException {
        try {
            flush();
            base.getSource().getFD().sync();
        } catch (IOException e) {
            throw new BinaryException(e);
        }
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

    private static class RandomFileReadWrite implements BaseReadWrite {

        private final RandomAccessFile source;

        private final byte[] bufferData;

        private final Binary buffer;

        private long curPos = Long.MAX_VALUE;

        private int size = -1;

        private boolean bufferChanged = false;

        private RandomFileReadWrite(RandomAccessFile source, int bufferSize)
                throws NullPointerException, IllegalArgumentException {
            this.source = Quicker.require(source);
            if (bufferSize < 8) {
                throw new IllegalArgumentException("Buffer size cannot less than 8.");
            }
            Checker.checkLength(bufferSize);
            this.bufferData = new byte[bufferSize];
            this.buffer = Binary.wrap(bufferData);
        }

        private void fillBuffer(long index) {
            flush();
            try {
                this.curPos = index;
                source.seek(index);
                this.size = source.read(bufferData);
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
            return buffer.getByte(index - curPos);
        }

        @Override
        public short _getShort(long index) throws IndexOutOfBoundsException, BinaryException {
            if (!inBuffer(index, 2)) {
                fillBuffer(index);
            }
            return buffer.getShort(index - curPos);
        }

        @Override
        public char _getChar(long index) throws IndexOutOfBoundsException, BinaryException {
            if (!inBuffer(index, 2)) {
                fillBuffer(index);
            }
            return buffer.getChar(index - curPos);
        }

        @Override
        public int _getMedium(long index) throws IndexOutOfBoundsException, BinaryException {
            if (!inBuffer(index, 3)) {
                fillBuffer(index);
            }
            return buffer.getMedium(index - curPos);
        }

        @Override
        public int _getInt(long index) throws IndexOutOfBoundsException, BinaryException {
            if (!inBuffer(index, 4)) {
                fillBuffer(index);
            }
            return buffer.getInt(index - curPos);
        }

        @Override
        public long _getLong(long index) throws IndexOutOfBoundsException, BinaryException {
            if (!inBuffer(index, 8)) {
                fillBuffer(index);
            }
            return buffer.getLong(index - curPos);
        }

        @Override
        public void _setByte(long index, int value) throws IndexOutOfBoundsException, BinaryException {
            if (!inBuffer(index, 1)) {
                fillBuffer(index);
            }
            buffer.setByte(index - curPos, value);
            bufferChanged = true;
        }

        @Override
        public void _setShort(long index, int value) throws IndexOutOfBoundsException, BinaryException {
            if (!inBuffer(index, 2)) {
                fillBuffer(index);
            }
            buffer.setShort(index - curPos, value);
            bufferChanged = true;
        }

        @Override
        public void _setChar(long index, int value) throws IndexOutOfBoundsException, BinaryException {
            if (!inBuffer(index, 2)) {
                fillBuffer(index);
            }
            buffer.setChar(index - curPos, value);
            bufferChanged = true;
        }

        @Override
        public void _setMedium(long index, int value) throws IndexOutOfBoundsException, BinaryException {
            if (!inBuffer(index, 3)) {
                fillBuffer(index);
            }
            buffer.setMedium(index - curPos, value);
            bufferChanged = true;
        }

        @Override
        public void _setInt(long index, int value) throws IndexOutOfBoundsException, BinaryException {
            if (!inBuffer(index, 4)) {
                fillBuffer(index);
            }
            buffer.setInt(index - curPos, value);
            bufferChanged = true;
        }

        @Override
        public void _setLong(long index, long value) throws IndexOutOfBoundsException, BinaryException {
            if (!inBuffer(index, 8)) {
                fillBuffer(index);
            }
            buffer.setLong(index - curPos, value);
            bufferChanged = true;
        }

        public Binary readBuffer(long index) {
            fillBuffer(index);
            return Binary.wrap(bufferData, 0, size, null);
        }

        public void flush() {
            try {
                long remainder = source.length() - curPos;
                if (size > 0 && bufferChanged && remainder > 0) {
                    source.seek(curPos);
                    int actualSize = (int)Math.min(size, remainder);
                    source.write(bufferData, 0, actualSize);
                }
                size = -1;
                bufferChanged = false;
            } catch (IOException e) {
                throw new BinaryException(e);
            }
        }

        public RandomAccessFile getSource() {
            return source;
        }
    }
}
