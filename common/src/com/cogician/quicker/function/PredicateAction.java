package com.cogician.quicker.function;

import com.cogician.quicker.Checker;

/**
 * <p>
 * A functional interface without any argument. {@linkplain #perform()} is its functional method.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-06-13T21:02:37+08:00
 * @since 0.0.0, 2016-06-13T21:02:37+08:00
 */
@FunctionalInterface
public interface PredicateAction {

    /**
     * <p>
     * Performs this functional method. Returns true if success, else false.
     * </p>
     * 
     * @return true if success, else false
     * @since 0.0.0
     */
    public boolean perform();

    /**
     * <p>
     * Returns a composed PredicateAction that represents a short-circuiting logical AND of given {@code other} and this
     * PredicateAction.
     * </p>
     *
     * @param other
     *            given PredicateAction
     * @return a composed PredicateAction that represents a short-circuiting logical AND of given {@code other} and this
     *         PredicateAction
     * @throws NullPointerException
     *             if {@code other} is null
     */
    default PredicateAction and(PredicateAction other) throws NullPointerException {
        Checker.checkNull(other);
        return () -> perform() && other.perform();
    }

    /**
     * <p>
     * Returns a PredicateAction that represents the logical negation of this PredicateAction.
     * </p>
     *
     * @return a PredicateAction that represents the logical negation of this PredicateAction
     */
    default PredicateAction negate() {
        return () -> !perform();
    }

    /**
     * <p>
     * Returns a composed PredicateAction that represents a short-circuiting logical OR of given {@code other} and this
     * PredicateAction.
     * </p>
     *
     * @param other
     *            given PredicateAction
     * @return a composed PredicateAction that represents a short-circuiting logical OR of given {@code other} and this
     *         PredicateAction
     * @throws NullPointerException
     *             if {@code other} is null
     */
    default PredicateAction or(PredicateAction other) throws NullPointerException {
        Checker.checkNull(other);
        return () -> perform() || other.perform();
    }
}
