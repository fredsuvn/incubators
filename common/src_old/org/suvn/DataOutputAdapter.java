package com.cogician.quicker;

import com.cogician.quicker.util.reflect.ReflectionException;

/**
 * This class is an adapter to write data block, the type may be an output
 * stream or other object which can write data.
 *
 * @author Fred Suvn
 * @version 0.0.0, 2015-06-30 14:16:54
 */
public interface DataOutputAdapter<T> extends Adapter<T> {
    /**
     * Writes special data block in to special generic type of instance, the
     * actual written length in bits will be returned.
     *
     * @param data
     *            special data block, not null
     * @return the actual written length in bits will be returned
     * @throws NullPointerException
     *             thrown if special block is null
     * @throws ReflectionException
     *             thrown if a data error occurs
     */
    public long write(Bitsss data);
}
