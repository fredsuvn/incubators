package com.cogician.quicker.binary;

import java.io.EOFException;
import java.io.IOException;

import com.cogician.quicker.Checker;
import com.cogician.quicker.Quicker;

/**
 * <p>
 * Inputer which inputs data from a binary. This type of inputer commonly come from {@linkplain Binary#getInputer(long)}
 * .
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-08-22T17:01:26+08:00
 * @since 0.0.0, 2016-08-22T17:01:26+08:00
 */
public class BinaryInputer extends Inputer {

    private final Binary source;

    private final long startIndex;

    private final long endIndex;

    private long cur;

    /**
     * <p>
     * Constructs an inputer of which data from specified binary.
     * </p>
     * 
     * @param source
     *            specified binary
     * @throws NullPointerException
     *             if specified binary is null
     * @since 0.0.0
     */
    public BinaryInputer(Binary source) throws NullPointerException {
        this(source, 0, source.length());
    }

    /**
     * <p>
     * Constructs an inputer of which data from specified binary between from index inclusive and end of this binary.
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
    public BinaryInputer(Binary source, long from) throws NullPointerException, IndexOutOfBoundsException {
        this(source, from, source.length());
    }

    /**
     * <p>
     * Constructs an inputer of which data from specified binary between from index inclusive and to index exclusive.
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
    public BinaryInputer(Binary source, long from, long to) throws NullPointerException, IndexOutOfBoundsException {
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
    protected byte _readByte() throws EOFException, IOException {
        checkEnd(1);
        byte v = source.getByte(cur);
        cur += 1;
        return v;
    }

    @Override
    protected short _readShort() throws EOFException, IOException {
        checkEnd(2);
        short v = source.getShort(cur);
        cur += 2;
        return v;
    }

    @Override
    protected char _readChar() throws EOFException, IOException {
        checkEnd(2);
        char v = source.getChar(cur);
        cur += 2;
        return v;
    }

    @Override
    protected int _readMedium() throws EOFException, IOException {
        checkEnd(3);
        int v = source.getMedium(cur);
        cur += 3;
        return v;
    }

    @Override
    protected int _readInt() throws EOFException, IOException {
        checkEnd(4);
        int v = source.getInt(cur);
        cur += 4;
        return v;
    }

    @Override
    protected long _readLong() throws EOFException, IOException {
        checkEnd(8);
        long v = source.getLong(cur);
        cur += 8;
        return v;
    }

    @Override
    protected float _readFloat() throws EOFException, IOException {
        checkEnd(4);
        float v = source.getFloat(cur);
        cur += 4;
        return v;
    }

    @Override
    protected double _readDouble() throws EOFException, IOException {
        checkEnd(8);
        double v = source.getDouble(cur);
        cur += 8;
        return v;
    }

    @Override
    public int read() throws IOException {
        if (cur >= endIndex) {
            return -1;
        }
        int v = source.getUnsignedByte(cur);
        cur += 1;
        return v;
    }

    @Override
    public byte readByte() throws EOFException, IOException {
        return _readByte();
    }

    @Override
    public short readShort() throws EOFException, IOException {
        return _readShort();
    }

    @Override
    public char readChar() throws EOFException, IOException {
        return _readChar();
    }

    @Override
    public int readMedium() throws EOFException, IOException {
        return _readMedium();
    }

    @Override
    public int readInt() throws EOFException, IOException {
        return _readInt();
    }

    @Override
    public long readLong() throws EOFException, IOException {
        return _readLong();
    }

    @Override
    public float readFloat() throws EOFException, IOException {
        return _readFloat();
    }

    @Override
    public double readDouble() throws EOFException, IOException {
        return _readDouble();
    }

    @Override
    public long readPrimitive(int bytesNum) throws IllegalArgumentException, EOFException, IOException {
        checkEnd(bytesNum);
        long v = source.getPrimitive(cur, bytesNum);
        cur += bytesNum;
        return v;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        Checker.checkRangeIndexes(off, off + len, b.length);
        int actual = (int)Math.min(len, endIndex - cur);
        source.copy(cur, Binary.wrap(b), off, actual);
        cur += actual;
        return actual;
    }

    @Override
    public long read(Binary binary, long offset, long length)
            throws NullPointerException, IndexOutOfBoundsException, IOException {
        Checker.checkRangeIndexes(offset, offset + length, binary.length());
        long actual = Math.min(length, endIndex - cur);
        source.copy(cur, binary, offset, actual);
        cur += actual;
        return actual;
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
