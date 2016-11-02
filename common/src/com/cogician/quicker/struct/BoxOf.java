package com.cogician.quicker.struct;

import javax.annotation.Nullable;

/**
 * <p>
 * A class to set and get a object. It is often used in lambda expression.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-05-18T16:51:58+08:00
 * @since 0.0.0, 2016-05-18T16:51:58+08:00
 */
public class BoxOf<T> {

    @Nullable
    private T value;

    /**
     * <p>
     * Constructs an empty instance.
     * </p>
     * 
     * @since 0.0.0
     */
    public BoxOf() {

    }

    /**
     * <p>
     * Constructs an instance with given value.
     * </p>
     * 
     * @param value
     *            given value
     * @since 0.0.0
     */
    public BoxOf(@Nullable T value) {
        set(value);
    }

    /**
     * <p>
     * Gets value.
     * </p>
     * 
     * @return value in the box
     * @since 0.0.0
     */
    public @Nullable T get() {
        return value;
    }

    /**
     * <p>
     * Sets given value.
     * </p>
     * 
     * @param value
     *            given value
     * @since 0.0.0
     */
    public void set(@Nullable T value) {
        this.value = value;
    }

    /**
     * <p>
     * Returns whether this box is empty, that means value of this box is null.
     * </p>
     * 
     * @return whether this box is empty
     * @since 0.0.0
     */
    public boolean isEmpty() {
        return value == null;
    }
}
