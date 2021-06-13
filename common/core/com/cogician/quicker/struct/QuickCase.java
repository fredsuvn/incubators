package com.cogician.quicker.struct;

import java.util.function.Consumer;
import java.util.function.Predicate;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

/**
 * <p>
 * Case represents a switch case label or if-body in a {@linkplain QuickSwitch}.
 * </p>
 * <p>
 * A case has a tester and a action. Tester is used to test whether passed argument meets the condition, if it is,
 * action will be performed with given argument.
 * </p>
 * <p>
 * The action returns true to continue current switch (go to next case), or false to break this switch.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-07-30T20:52:58+08:00
 * @since 0.0.0, 2016-07-30T20:52:58+08:00
 * @see QuickSwitch
 */
@Immutable
public class QuickCase<T> {

    private static final <T> Predicate<T> returnFalse(@Nullable Consumer<T> action) {
        return action == null ? t -> false : t -> {
            action.accept(t);
            return false;
        };
    }

    private final Predicate<? super T> tester;

    private final Predicate<? super T> action;

    /**
     * <p>
     * Constructs with specified tester and action. The action will be considered as returning false after performing.
     * </p>
     * 
     * @param tester
     *            specified tester
     * @param action
     *            specified action
     * @since 0.0.0
     */
    public QuickCase(Predicate<? super T> tester, Consumer<? super T> action) {
        this.tester = tester;
        this.action = returnFalse(action);
    }

    /**
     * <p>
     * Constructs with specified tester and action.
     * </p>
     * 
     * @param tester
     *            specified tester
     * @param action
     *            specified action
     * @since 0.0.0
     */
    public QuickCase(Predicate<? super T> tester, Predicate<? super T> action) {
        this.tester = tester;
        this.action = action;
    }

    /**
     * <p>
     * Tests whether given argument meets the condition.
     * </p>
     * 
     * @param arg
     *            given argument
     * @return whether given argument meets the condition
     * @since 0.0.0
     */
    public boolean test(@Nullable T arg) {
        return tester.test(arg);
    }

    /**
     * <p>
     * Performs action with given argument.
     * </p>
     * 
     * @param arg
     *            given argument
     * @return true if continues to go to next case, or false to break this switch
     * @since 0.0.0
     */
    public boolean perform(@Nullable T arg) {
        return action.test(arg);
    }
}
