package com.cogician.quicker.struct;

/**
 * <p>
 * This class represents simple and fast temporary data with two int type field, it commonly express a position in an
 * primitive array with special index and bit offset of element of the index.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2015-05-23 14:46:40
 */
public class ArrayPos {
    /**
     * Commonly is index of a array, but can also be a value represents other.
     */
    public int index;

    /**
     * Commonly is bit offset of a index of a array, but can also be a value represents other.
     */
    public int offset;

    /**
     * Constructs with all field set 0.
     */
    public ArrayPos() {
        index = 0;
        offset = 0;
    }

    /**
     * Constructs with index and offset.
     *
     * @param index
     *            commonly is index of a array, but can also be a value represents other
     * @param offset
     *            commonly is bit offset of a index of a array, but can also be a value represents other
     */
    public ArrayPos(final int index, final int offset) {
        this.index = index;
        this.offset = offset;
    }
}
