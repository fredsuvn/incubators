package com.cogician.quicker.binary;

import java.io.Closeable;
import java.io.File;
import java.io.Flushable;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel.MapMode;

/**
 * <p>
 * File binary is a extension of {@linkplain Binary}, used to operate file. A file binary represents a file.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-08-30T11:45:18+08:00
 * @since 0.0.0, 2016-08-30T11:45:18+08:00
 */
public interface FileBinary extends Binary, Closeable, Flushable {

    /**
     * <p>
     * Using buffered {@linkplain RandomAccessFile} to open a file and access. The specified mode is same as mode of
     * {@linkplain RandomAccessFile}.
     * </p>
     * 
     * @param fileName
     *            name of specified file to be opened
     * @param mode
     *            specified mode
     * @return a file binary using buffered {@linkplain RandomAccessFile} to open
     * @throws NullPointerException
     *             if specified file is null
     * @throws IllegalArgumentException
     *             if specified mode is illegal
     * @throws BinaryException
     *             if file not found and cannot be created, or other IO problem occurs
     * @since 0.0.0
     */
    public static FileBinary open(String fileName, String mode)
            throws NullPointerException, IllegalArgumentException, BinaryException {
        return new RandomFileBinary(fileName, mode);
    }

    /**
     * <p>
     * Using buffered {@linkplain RandomAccessFile} to open a file and access. The specified mode is same as mode of
     * {@linkplain RandomAccessFile}.
     * </p>
     * 
     * @param file
     *            specified file to be opened
     * @param mode
     *            specified mode
     * @return a file binary using buffered {@linkplain RandomAccessFile} to open
     * @throws NullPointerException
     *             if specified file is null
     * @throws IllegalArgumentException
     *             if specified mode is illegal
     * @throws BinaryException
     *             if file not found and cannot be created, or other IO problem occurs
     * @since 0.0.0
     */
    public static FileBinary open(File file, String mode)
            throws NullPointerException, IllegalArgumentException, BinaryException {
        return new RandomFileBinary(file, mode);
    }

    /**
     * <p>
     * Using {@linkplain MappedByteBuffer} to map a file and access. The specified mode is same as mode of
     * {@linkplain MappedByteBuffer}.
     * </p>
     * 
     * @param fileName
     *            name of specified file to be mapped
     * @param mode
     *            specified mode
     * @return a file binary using buffered {@linkplain MappedByteBuffer} to map
     * @throws NullPointerException
     *             if specified file or mode is null
     * @throws BinaryException
     *             if file not found and cannot be created, or other IO problem occurs
     * @since 0.0.0
     */
    public static FileBinary map(String fileName, MapMode mode)
            throws NullPointerException, IllegalArgumentException, BinaryException {
        return new MappedFileBinary(fileName, mode);
    }

    /**
     * <p>
     * Using {@linkplain MappedByteBuffer} to map a file and access. The specified mode is same as mode of
     * {@linkplain MappedByteBuffer}.
     * </p>
     * 
     * @param file
     *            specified file to be mapped
     * @param mode
     *            specified mode
     * @return a file binary using buffered {@linkplain MappedByteBuffer} to map
     * @throws NullPointerException
     *             if specified file or mode is null
     * @throws BinaryException
     *             if file not found and cannot be created, or other IO problem occurs
     * @since 0.0.0
     */
    public static FileBinary map(File file, MapMode mode)
            throws NullPointerException, IllegalArgumentException, BinaryException {
        return new MappedFileBinary(file, mode);
    }

    /**
     * <p>
     * Resizes the length of file. If specified length is less than original, the file will be truncated; else if
     * greater than, the file will be extended and the content of extending is unknown.
     * </p>
     * 
     * @param length
     *            specified length in bytes.
     * @throws BinaryException
     *             if any problem occurs when flushing
     * @since 0.0.0
     */
    public void setLength(long length) throws BinaryException;

    /**
     * <p>
     * Returns a file inputer at specified index. Length of returned inputer is associated with length of this file
     * binary, that means, if length of this binary is increasing, the inputer can read more. But if the file length is
     * less than specified index, returned inputer will be invalid.
     * </p>
     * 
     * @param index
     *            specified index
     * @return a variable-length inputer from specified index
     * @throws IndexOutOfBoundsException
     *             if specified index out of bounds
     * @since 0.0.0
     */
    public Inputer fileInputer(long index) throws IndexOutOfBoundsException;

    /**
     * <p>
     * Returns a file outputer at specified index. Length of returned outputer is associated with length of this file
     * binary, that means, length of file will increase if returned outputer outputs beyond the original length. The
     * index can over the file length but file length will be changed until a real write operation occurs.
     * </p>
     * 
     * @param index
     *            specified index
     * @return a variable-length inputer from specified index
     * @throws IndexOutOfBoundsException
     *             if specified index is negative
     * @since 0.0.0
     */
    public Outputer fileOutputer(long index) throws IndexOutOfBoundsException;

    /**
     * <p>
     * Flush this output.
     * </p>
     * 
     * @throws BinaryException
     *             if any problem occurs when flushing
     * @since 0.0.0
     */
    @Override
    public void flush() throws BinaryException;

    /**
     * <p>
     * Closes this inputer.
     * </p>
     * 
     * @throws BinaryException
     *             if any problem occurs when closing
     * @since 0.0.0
     */
    @Override
    public void close() throws BinaryException;

    /**
     * <p>
     * Forces all system buffers to synchronize with the underlying device. This method returns after all modified data
     * and attributes have been written to the relevant device(s).
     * </p>
     * 
     * @throws BinaryException
     *             if sync failed
     * @since 0.0.0
     */
    public abstract void sync() throws BinaryException;
}
