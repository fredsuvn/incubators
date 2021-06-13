package com.cogician.quicker.binary;

import java.util.NoSuchElementException;

import com.cogician.quicker.bigarray.ByteArray;

/**
 * <p>
 * Queue to store the byte.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-08-26T13:48:49+08:00
 * @since 0.0.0, 2016-08-26T13:48:49+08:00
 */
public class ByteQueue {

    private static final long BUFFER_SIZE = 8;

    private static final double GROWTH_FACTOR = 1.5;

    private ByteArray value;

    private long size;

    /**
     * <p>
     * Constructs an empty byte queue.
     * </p>
     * 
     * @since 0.0.0
     */
    public ByteQueue() {
        value = new ByteArray(BUFFER_SIZE);
        size = 0;
    }

    /**
     * <p>
     * Returns size of this byte queue;
     * </p>
     * 
     * @return
     * @since 0.0.0
     */
    public long size() {
        return size;
    }

    /**
     * <p>
     * Gets but doesn't remove first byte of this byte queue.
     * </p>
     * 
     * @return first byte
     * @throws NoSuchElementException
     *             if there is no element in this byte queue
     * @since 0.0.0
     */
    public byte getFirst() throws NoSuchElementException {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return value.get(0);
    }

    /**
     * <p>
     * Gets but doesn't remove last byte of this byte queue.
     * </p>
     * 
     * @return last byte
     * @throws NoSuchElementException
     *             if there is no element in this byte queue
     * @since 0.0.0
     */
    public byte getLast() throws NoSuchElementException {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return value.get(size - 1);
    }

    /**
     * <p>
     * Gets and removes first byte of this byte queue.
     * </p>
     * 
     * @return first byte
     * @throws NoSuchElementException
     *             if there is no element in this byte queue
     * @since 0.0.0
     */
    public byte pollFirst() throws NoSuchElementException {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        size--;
        byte v = value.get(0);
        ByteArray tmp = value.clone();
        tmp.copy(1, value, 0, size);
        return v;
    }

    /**
     * <p>
     * Gets and removes last byte of this byte queue.
     * </p>
     * 
     * @return last byte
     * @throws NoSuchElementException
     *             if there is no element in this byte queue
     * @since 0.0.0
     */
    public byte pollLast() throws NoSuchElementException {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return value.get((size--) - 1);
    }

    /**
     * <p>
     * Adds first byte into this byte queue.
     * </p>
     * 
     * @since 0.0.0
     */
    public void addFirst(byte v) throws NoSuchElementException {
        size++;
        if (size > value.length()) {
            ByteArray newValue = new ByteArray((long)(value.length() * GROWTH_FACTOR));
            value.copy(0, newValue, 1, size - 1);
            value = newValue;
        }
        value.set(0, v);
    }

    /**
     * <p>
     * Adds last byte into this byte queue.
     * </p>
     * 
     * @since 0.0.0
     */
    public void addLast(byte v) throws NoSuchElementException {
        size++;
        if (size > value.length()) {
            ByteArray newValue = new ByteArray((long)(value.length() * GROWTH_FACTOR));
            value.copy(newValue);
            value = newValue;
        }
        value.set(size - 1, v);
    }

    /**
     * <p>
     * Converts this byte queue into a new binary.
     * </p>
     * 
     * @return a new binary converted from this byte queue
     * @since 0.0.0
     */
    public Binary toBinary() {
        ByteArray result = new ByteArray(size);
        value.copy(result);
        return new HeapBinary(result, null);
    }

    /**
     * <p>
     * Converts this byte queue into a new byte array.
     * </p>
     * 
     * @return a new byte array converted from this byte queue
     * @since 0.0.0
     */
    public ByteArray toByteArray() {
        ByteArray result = new ByteArray(size);
        value.copy(result);
        return result;
    }

    /**
     * <p>
     * Converts this byte queue into a new byte array. If size of this queue is greater than Integer.MAX_VALUE, return
     * first Integer.MAX_VALUE elements.
     * </p>
     * 
     * @return a new byte array converted from this byte queue
     * @since 0.0.0
     */
    public byte[] toArray() {
        return toByteArray().toArray();
    }
}
