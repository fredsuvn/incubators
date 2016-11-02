package com.cogician.quicker.function;

import com.cogician.quicker.Checker;

/**
 * <p>
 * A functional interface without any argument and returned value. {@linkplain #perform()} is its functional method.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-06-13T20:37:58+08:00
 * @since 0.0.0, 2016-06-13T20:37:58+08:00
 */
@FunctionalInterface
public interface Action {

    /**
     * <p>
     * Performs this functional method.
     * </p>
     * 
     * @since 0.0.0
     */
    public void perform();

    /**
     * <p>
     * Returns a composed Action that performs, in sequence, this operation followed by the {@code after} operation.
     * </p>
     * 
     * @param after
     *            the operation to perform after this operation
     * @return a composed Action that performs in sequence this operation followed by the {@code after} operation
     * @throws NullPointerException
     *             if {@code after} is null
     * @since 0.0.0
     */
    default Action andThen(Action after) throws NullPointerException {
        Checker.checkNull(after);
        return () -> {
            perform();
            after.perform();
        };
    }
}
