package com.cogician.quicker.function;

import java.util.function.Consumer;

import com.cogician.quicker.Checker;

/**
 * <p>
 * Specialization of {@linkplain Consumer} with 2 arguments: a long and an object.
 * </p>
 * <p>
 * This functional interface is designed to operate in a for-each loop, the long argument as index, the object as each
 * element. This functional interface is expected to operate via side-effects.
 * </p>
 * 
 * @param <E>
 *            type of each element
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-05-05T19:08:21+08:00
 * @since 0.0.0, 2016-05-05T19:08:21+08:00
 */
@FunctionalInterface
public interface EachConsumer<E> {

    /**
     * <p>
     * Functional method of this interface, called in each loop.
     * </p>
     * 
     * @param index
     *            index of current element start from 0
     * @param element
     *            current element
     * @since 0.0.0
     */
    public void accept(long index, E element);

    /**
     * <p>
     * Returns a composed EachConsumer that performs, in sequence, this operation followed by the {@code after}
     * operation. If performing either operation throws an exception, it is relayed to the caller of the composed
     * operation. If performing this operation throws an exception, the {@code after} operation will not be performed.
     * </p>
     *
     * @param after
     *            the operation to perform after this operation
     * @return a composed EachConsumer that performs in sequence this operation followed by the {@code after} operation
     * @throws NullPointerException
     *             if {@code after} is null
     * @since 0.0.0
     */
    default EachConsumer<E> andThen(EachConsumer<? super E> after) throws NullPointerException {
        Checker.checkNull(after);
        return (i, e) -> {
            accept(i, e);
            after.accept(i, e);
        };
    }
}
