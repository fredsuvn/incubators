/*
 * File: ForEachPredicate.java
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-02-10T16:39:44+08:00
 * @since 0.0.0, 2016-02-10T16:39:44+08:00
 */

package com.cogician.quicker.function;

import java.util.function.Predicate;

import com.cogician.quicker.Checker;

/**
 * <p>
 * Specialization of {@linkplain Predicate} with 2 arguments: a long and an object.
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
 * @version 0.0.0, 2016-02-10T16:39:44+08:00
 * @since 0.0.0, 2016-02-10T16:39:44+08:00
 */
@FunctionalInterface
public interface QuickPredicate<E> {

    /**
     * <p>
     * Functional method of this interface, called in each loop.
     * </p>
     * 
     * @param index
     *            index of current element start from 0
     * @param element
     *            current element
     * @return result of each calling
     * @since 0.0.0
     * @see QuickPredicate
     */
    public boolean test(long index, E element);

    /**
     * <p>
     * Returns a composed EachPredicate that represents a short-circuiting logical AND of given {@code other} and this
     * predicate.
     * </p>
     *
     * @param other
     *            given EachPredicate
     * @return a composed EachPredicate that represents a short-circuiting logical AND of given {@code other} and this
     *         predicate
     * @throws NullPointerException
     *             if {@code other} is null
     */
    default QuickPredicate<E> and(QuickPredicate<? super E> other) throws NullPointerException {
        Checker.checkNull(other);
        return (i, e) -> test(i, e) && other.test(i, e);
    }

    /**
     * <p>
     * Returns a EachPredicate that represents the logical negation of this predicate.
     * </p>
     *
     * @return a EachPredicate that represents the logical negation of this predicate
     */
    default QuickPredicate<E> negate() {
        return (i, e) -> !test(i, e);
    }

    /**
     * <p>
     * Returns a composed EachPredicate that represents a short-circuiting logical OR of given {@code other} and this
     * predicate.
     * </p>
     *
     * @param other
     *            given EachPredicate
     * @return a composed EachPredicate that represents a short-circuiting logical OR of given {@code other} and this
     *         predicate
     * @throws NullPointerException
     *             if {@code other} is null
     */
    default QuickPredicate<E> or(QuickPredicate<? super E> other) throws NullPointerException {
        Checker.checkNull(other);
        return (i, e) -> test(i, e) || other.test(i, e);
    }
}
