package com.cogician.quicker;

/**
 * This interface represents an adapter.
 *
 * @author Fred Suvn
 * @version 0.0.0, 2015-06-30 14:45:00
 * @since 0.0.0
 */
public interface Adapter<T> {
    /**
     * Returns the adaptee of this adapter, the adaptee is the object which is
     * adapted.
     *
     * @return the adaptee of this adapter
     */
    public T adaptee();
}
