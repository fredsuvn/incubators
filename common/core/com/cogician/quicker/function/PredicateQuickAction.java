package com.cogician.quicker.function;

import com.cogician.quicker.Checker;

/**
 * <p>
 * A functional interface with 1 argument: a long, and a boolean result. This functional interface is designed to
 * operate in a loop. The argument is used number of times this action was performed, start from 0.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-06-13T21:09:49+08:00
 * @since 0.0.0, 2016-06-13T21:09:49+08:00
 */
@FunctionalInterface
public interface PredicateQuickAction {

    /**
     * <p>
     * Performs this functional method. Returns true if success, else false.
     * </p>
     * <p>
     * Returned value mostly means continues the loop if true, or breaks if false.
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
     * Returns a composed PredicateQuickAction that represents a short-circuiting logical AND of given {@code other} and
     * this PredicateQuickAction.
     * </p>
     *
     * @param other
     *            given PredicateQuickAction
     * @return a composed PredicateQuickAction that represents a short-circuiting logical AND of given {@code other} and
     *         this PredicateQuickAction
     * @throws NullPointerException
     *             if {@code other} is null
     */
    default PredicateQuickAction and(PredicateQuickAction other) throws NullPointerException {
        Checker.checkNull(other);
        return (i) -> perform(i) && other.perform(i);
    }

    /**
     * <p>
     * Returns a PredicateQuickAction that represents the logical negation of this PredicateQuickAction.
     * </p>
     *
     * @return a PredicateQuickAction that represents the logical negation of this PredicateQuickAction
     */
    default PredicateQuickAction negate() {
        return (i) -> !perform(i);
    }

    /**
     * <p>
     * Returns a composed PredicateQuickAction that represents a short-circuiting logical OR of given {@code other} and
     * this PredicateQuickAction.
     * </p>
     *
     * @param other
     *            given PredicateQuickAction
     * @return a composed PredicateQuickAction that represents a short-circuiting logical OR of given {@code other} and
     *         this PredicateQuickAction
     * @throws NullPointerException
     *             if {@code other} is null
     */
    default PredicateQuickAction or(PredicateQuickAction other) throws NullPointerException {
        Checker.checkNull(other);
        return (i) -> perform(i) || other.perform(i);
    }
}
