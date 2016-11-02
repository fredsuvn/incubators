package com.cogician.quicker.binary.data.impl.primitivearray;

import com.cogician.quicker.annotation.Base;
import com.cogician.quicker.struct.ArrayPos;

/**
 * Base read write accessor of implementation of primitive type array of this
 * package.
 *
 * @author Fred Suvn
 * @version 0.0.0, 2015-06-01 09:00:32
 * @since 0.0.0
 * @deprecated Stop used this class indefinitely.
 */
@Base
@Deprecated
public interface BaseReadWrite {
    /**
     * Reads an int type at special position of primitive array.
     *
     * @param arrayPosInfo
     *            special position of primitive array
     * @return an int type
     */
    public int readInt(ArrayPos arrayPosInfo);

    /**
     * Reads a long type at special position of primitive array.
     *
     * @param arrayPosInfo
     *            special position of primitive array
     * @return a long type
     */
    public long readLong(ArrayPos arrayPosInfo);

    /**
     * Reads bits of special number as int type at special position of primitive
     * array. If the number of bits equals int type's, whole returned int type
     * is read value, else, read bits in low orders of int type ,high orders
     * will be filled 0.
     *
     * @param arrayPosInfo
     *            special position of primitive array
     * @param bits
     *            special number of bits, [1, 32]
     * @return bits of special number
     */
    public int readBitsAsInt(ArrayPos arrayPosInfo, int bits);

    /**
     * Reads bits of special number as long type at special position of
     * primitive array. If the number of bits equals long type's, whole returned
     * long type is read value, else, read bits in low orders of long type ,high
     * orders will be filled 0.
     *
     * @param arrayPosInfo
     *            special position of primitive array
     * @param bits
     *            special number of bits, [1, 64]
     * @return bits of special number
     */
    public long readBitsAsLong(ArrayPos arrayPosInfo, int bits);

    /**
     * Writes an int type at special position of primitive array.
     *
     * @param arrayPosInfo
     *            special position of primitive array
     * @param value
     *            int value
     */
    public void writeInt(ArrayPos arrayPosInfo, int value);

    /**
     * Writes a long type at special position of primitive array.
     *
     * @param arrayPosInfo
     *            special position of primitive array
     * @param value
     *            long value
     */
    public void writeLong(ArrayPos arrayPosInfo, long value);

    /**
     * Writes special bits of number at special position of primitive array in
     * order (left to right, high to low order).
     *
     * @param arrayPosInfo
     *            special position of primitive array
     * @param value
     *            bits value
     * @param bits
     *            special number of bits, [1, 32]
     */
    public void writeBitsInOrder(ArrayPos arrayPosInfo, int value, int bits);

    /**
     * Writes special bits of number at special position of primitive array in
     * order (left to right, high to low order).
     *
     * @param arrayPosInfo
     *            special position of primitive array
     * @param value
     *            bits value
     * @param bits
     *            special number of bits, [1, 64]
     */
    public void writeBitsInOrder(ArrayPos arrayPosInfo, long value, int bits);
}
