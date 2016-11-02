package com.cogician.quicker.struct;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.cogician.quicker.Checker;

/**
 * <p>
 * A base value wrapper contains a value. If the value is present, {@code isPresent()} will return {@code true} and
 * {@code getValue()} will return the value, otherwise they will return {@code false} and {@code null}.
 * </p>
 * 
 * @param <T>
 *            type of wrapped value
 * 
 * @author Fred Suvn
 * @version 0.0.0, 2016-06-28T10:39:30+08:00
 * @since 0.0.0, 2016-06-28T10:39:30+08:00
 */
@Immutable
public interface ValueWrapper<T> {

    /**
     * <p>
     * Returns value if the value is present in this wrapper, otherwise returns null.
     * </p>
     * 
     * @return value held by this wrapper
     * @since 0.0.0
     */
    public @Nullable T getValue();

    /**
     * <p>
     * Returns {@code true} if there is a value present, otherwise {@code false}.
     * </p>
     *
     * @return {@code true} if there is a value present, otherwise {@code false}
     */
    default boolean isPresent() {
        return getValue() != null;
    }

    /**
     * <p>
     * Returns whether value of given another value wrapper and this wrapper is equal. If given another value wrapper is
     * null, return false.
     * </p>
     * 
     * @param another
     *            given another value wrapper
     * @return
     * @since 0.0.0
     */
    default boolean valueEqual(@Nullable ValueWrapper<? extends T> another) {
        return another == null ? false : Checker.isEqual(getValue(), another.getValue());
    }
}
