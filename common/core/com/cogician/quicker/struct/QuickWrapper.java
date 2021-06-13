package com.cogician.quicker.struct;

import javax.annotation.Nullable;

import com.cogician.quicker.Checker;

/**
 * <p>
 * A wrapper holds a value.
 * </p>
 * 
 * @param <T>
 *            type of wrapped value
 * 
 * @author Fred Suvn
 * @version 0.0.0, 2016-06-28T10:39:30+08:00
 * @since 0.0.0, 2016-06-28T10:39:30+08:00
 */
public interface QuickWrapper<T> {

    /**
     * <p>
     * Returns holden value if it is present, or null.
     * </p>
     * 
     * @return value holden value
     * @since 0.0.0
     */
    public @Nullable T getValue();

    /**
     * <p>
     * Returns whether this instance holds a null value.
     * </p>
     * 
     * @return whether this instance holds a null value
     * @since 0.0.0
     */
    default boolean isEmpty() {
        return getValue() == null;
    }

    /**
     * <p>
     * Returns whether value of given instance and this instance are equal.
     * </p>
     * 
     * @param inst
     *            given instance
     * @return whether value of given instance and this instance are equal
     * @since 0.0.0
     */
    default boolean valueEqual(QuickWrapper<? extends T> inst) {
        return Checker.isEqual(getValue(), inst.getValue());
    }
}
