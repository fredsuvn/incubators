package com.cogician.quicker.function;

import com.cogician.quicker.Checker;

/**
 * <p>
 * Specialization of {@linkplain EachPredicateAction} with 1 argument: a long. This functional interface is designed to
 * operate in a loop. The argument is used to record number of times this action was performed, start from 0.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-06-13T21:09:49+08:00
 * @since 0.0.0, 2016-06-13T21:09:49+08:00
 */
@FunctionalInterface
public interface EachPredicateAction {
    /**
     * <p>
     * Performs this functional method. Returns true if success, else false.
     * </p>
     * 
     * @param index
     *            current index
     * @return true if success, else false
     * @since 0.0.0
     */
    public boolean perform(long index);

    /**
     * <p>
     * Returns a composed EachPredicateAction that represents a short-circuiting logical AND of given {@code other} and
     * this EachPredicateAction.
     * </p>
     *
     * @param other
     *            given EachPredicateAction
     * @return a composed EachPredicateAction that represents a short-circuiting logical AND of given {@code other} and
     *         this EachPredicateAction
     * @throws NullPointerException
     *             if {@code other} is null
     */
    default EachPredicateAction and(EachPredicateAction other) throws NullPointerException {
        Checker.checkNull(other);
        return (i) -> perform(i) && other.perform(i);
    }

    /**
     * <p>
     * Returns a EachPredicateAction that represents the logical negation of this EachPredicateAction.
     * </p>
     *
     * @return a EachPredicateAction that represents the logical negation of this EachPredicateAction
     */
    default EachPredicateAction negate() {
        return (i) -> !perform(i);
    }

    /**
     * <p>
     * Returns a composed EachPredicateAction that represents a short-circuiting logical OR of given {@code other} and
     * this EachPredicateAction.
     * </p>
     *
     * @param other
     *            given EachPredicateAction
     * @return a composed EachPredicateAction that represents a short-circuiting logical OR of given {@code other} and
     *         this EachPredicateAction
     * @throws NullPointerException
     *             if {@code other} is null
     */
    default EachPredicateAction or(EachPredicateAction other) throws NullPointerException {
        Checker.checkNull(other);
        return (i) -> perform(i) || other.perform(i);
    }
}
