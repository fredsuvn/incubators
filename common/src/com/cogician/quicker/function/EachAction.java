package com.cogician.quicker.function;

import com.cogician.quicker.Checker;

/**
 * <p>
 * Specialization of {@linkplain Action} with 1 argument: a long. This functional interface is designed to operate in a
 * loop. The argument is used to record number of times this action was performed, start from 0.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-06-13T20:56:44+08:00
 * @since 0.0.0, 2016-06-13T20:56:44+08:00
 */
@FunctionalInterface
public interface EachAction {

    /**
     * <p>
     * Performs this functional method.
     * </p>
     * 
     * @param index
     *            current index
     * @since 0.0.0
     */
    public void perform(long index);

    /**
     * <p>
     * Returns a composed EachAction that performs, in sequence, this operation followed by the {@code after} operation.
     * </p>
     * 
     * @param after
     *            the operation to perform after this operation
     * @return a composed EachAction that performs in sequence this operation followed by the {@code after} operation
     * @throws NullPointerException
     *             if {@code after} is null
     * @since 0.0.0
     */
    default EachAction andThen(EachAction after) throws NullPointerException {
        Checker.checkNull(after);
        return (i) -> {
            perform(i);
            after.perform(i);
        };
    }
}
