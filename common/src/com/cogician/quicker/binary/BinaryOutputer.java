package com.cogician.quicker.binary;

import java.io.EOFException;
import java.io.IOException;
import java.io.SyncFailedException;

import com.cogician.quicker.Checker;
import com.cogician.quicker.Quicker;

/**
 * <p>
 * Outputer which outputs data into a binary. This type of outputer commonly come from
 * {@linkplain Binary#getOutputer(long)}.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-08-22T21:27:44+08:00
 * @since 0.0.0, 2016-08-22T21:27:44+08:00
 */
public class BinaryOutputer extends Outputer {

    private final Binary source;

    private final long startIndex;

    private final long endIndex;

    private long cur;

    /**
     * <p>
     * Constructs an outputer of which data will be written into specified binary.
     * </p>
     * 
     * @param source
     *            specified binary
     * @throws NullPointerException
     *             if specified binary is null
     * @since 0.0.0
     */
    public BinaryOutputer(Binary source) throws NullPointerException {
        this(source, 0, source.length());
    }

    /**
     * <p>
     * Constructs an outputer of which data will be written into specified binary between from index inclusive and end
     * of this binary.
     * </p>
     * 
     * @param source
     *            specified binary
     * @param from
     *            from index inclusive
     * @throws NullPointerException
     *             if specified binary is null
     * @throws IndexOutOfBoundsException
     *             if index out of bounds
     * @since 0.0.0
     */
    public BinaryOutputer(Binary source, long from) throws NullPointerException, IndexOutOfBoundsException {
        this(source, from, source.length());
    }

    /**
     * <p>
     * Constructs an outputer of which data will be written into specified binary between from index inclusive and to
     * index exclusive.
     * </p>
     * 
     * @param source
     *            specified binary
     * @param from
     *            from index inclusive
     * @param to
     *            to index exclusive
     * @throws NullPointerException
     *             if specified binary is null
     * @throws IndexOutOfBoundsException
     *             if indexes out of bounds
     * @since 0.0.0
     */
    public BinaryOutputer(Binary source, long from, long to) throws NullPointerException, IndexOutOfBoundsException {
        this.source = Quicker.require(source);
        Checker.checkRangeIndexes(from, to, source.length());
        this.startIndex = from;
        this.endIndex = to;
        this.cur = from;
    }

    private void checkEnd(long wide) throws EndOfBinaryException {
        if (cur >= endIndex || cur + wide - 1 >= endIndex) {
            throw new EndOfBinaryException();
        }
    }

    @Override
    public ByteOrderProcessor getByteOrderProcessor() {
        return source.getByteOrderProcessor();
    }

    @Override
    protected void _writeByte(int value) throws EOFException, IOException {
        checkEnd(1);
        source.setByte(cur, value);
        cur += 1;
    }

    @Override
    protected void _writeShort(int value) throws EOFException, IOException {
        checkEnd(2);
        source.setShort(cur, value);
        cur += 2;
    }

    @Override
    protected void _writeChar(int value) throws EOFException, IOException {
        checkEnd(2);
        source.setChar(cur, value);
        cur += 2;
    }

    @Override
    protected void _writeMedium(int value) throws EOFException, IOException {
        checkEnd(3);
        source.setMedium(cur, value);
        cur += 3;
    }

    @Override
    protected void _writeInt(int value) throws EOFException, IOException {
        checkEnd(4);
        source.setInt(cur, value);
        cur += 4;
    }

    @Override
    protected void _writeLong(long value) throws EOFException, IOException {
        checkEnd(8);
        source.setLong(cur, value);
        cur += 8;
    }

    @Override
    protected void _writeFloat(float value) throws EOFException, IOException {
        checkEnd(4);
        source.setFloat(cur, value);
        cur += 4;
    }

    @Override
    protected void _writeDouble(double value) throws EOFException, IOException {
        checkEnd(8);
        source.setDouble(cur, value);
        cur += 8;
    }

    @Override
    public void writeByte(int value) throws EOFException, IOException {
        _writeByte(value);
    }

    @Override
    public void writeShort(int value) throws EOFException, IOException {
        _writeShort(value);
    }

    @Override
    public void writeChar(int value) throws EOFException, IOException {
        _writeChar(value);
    }

    @Override
    public void writeMedium(int value) throws EOFException, IOException {
        _writeMedium(value);
    }

    @Override
    public void writeInt(int value) throws EOFException, IOException {
        _writeInt(value);
    }

    @Override
    public void writeLong(long value) throws EOFException, IOException {
        _writeLong(value);
    }

    @Override
    public void writeFloat(float value) throws EOFException, IOException {
        _writeFloat(value);
    }

    @Override
    public void writeDouble(double value) throws EOFException, IOException {
        _writeDouble(value);
    }

    @Override
    public void writePrimitive(long value, int bytesNum) throws IllegalArgumentException, EOFException, IOException {
        source.setPrimitive(cur, value, bytesNum);
        cur += bytesNum;
    }

    @Override
    public void flush() throws IOException {
        // source.flush();
    }

    @Override
    public void sync() throws SyncFailedException {
        // source.sync();
    }

    @Override
    public long length() {
        return endIndex - startIndex;
    }

    @Override
    public long remainderLength() {
        return endIndex - cur;
    }
}
