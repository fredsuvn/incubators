package com.cogician.quicker.struct;

import java.util.function.Consumer;
import java.util.function.Predicate;

import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import com.cogician.quicker.Checker;

/**
 * <p>
 * Case represents a switch case label or if-body in a {@linkplain Switch}.
 * </p>
 * <p>
 * A case has a tester and a action. Tester is used to test whether given argument is that meets the condition, action
 * is used to perform with given argument.
 * </p>
 * <p>
 * Case hold a case constant to test this constant and given argument. Default test just uses
 * {@linkplain Checker#equals(Object)} to test. Custom tester may test more complexly, even the constant may be null but
 * the testing is conceptual.
 * </p>
 * <p>
 * Action of case returns true to non-break this switch, and next cases will not test passed argument. Or returns false
 * to break this switch. Default action is an empty false-returned action.
 * </p>
 * <p>
 * Case is thread-safe.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-07-30T20:52:58+08:00
 * @since 0.0.0, 2016-07-30T20:52:58+08:00
 * @see Switch
 */
@ThreadSafe
public class Case<T> {

    private static final Case<?> USELESS = new Case<>(null, t -> false, t -> true);

    /**
     * <p>
     * Returns a useless case like:
     * 
     * <pre>
     * new Case<>(null, t -> false, t -> true);
     * </pre>
     * </p>
     * 
     * @return
     * @since 0.0.0
     */
    public static final <T> Case<T> getUseless() {
        @SuppressWarnings("unchecked")
        Case<T> result = (Case<T>)USELESS;
        return result;
    }

    private static final <T> Predicate<T> returnFalse(@Nullable Consumer<T> action) {
        return action == null ? t -> false : t -> {
            action.accept(t);
            return false;
        };
    }

    @Nullable
    private final T constant;

    private final Predicate<? super T> tester;

    private final Predicate<? super T> action;

    /**
     * <p>
     * Constructs a switch case with given case constant, default tester and action.
     * </p>
     * 
     * @param caseConstant
     *            given case constant
     * @since 0.0.0
     */
    public Case(@Nullable T caseConstant) {
        this(caseConstant, null, (Predicate<T>)null);
    }

    /**
     * <p>
     * Constructs a switch case with given case constant, given case action and default tester. Actual action will
     * return false after given action performed. If given case action is null, it will set default instead.
     * </p>
     * 
     * @param caseConstant
     *            given case constant
     * @param action
     *            given case action
     * @since 0.0.0
     */
    public Case(@Nullable T caseConstant, @Nullable Consumer<? super T> action) {
        this(caseConstant, null, action);
    }

    /**
     * <p>
     * Constructs a switch case with given case constant, given case action and default tester. If given case action is
     * null, it will set default instead.
     * </p>
     * 
     * @param caseConstant
     *            given case constant
     * @param action
     *            given case action
     * @since 0.0.0
     */
    public Case(@Nullable T caseConstant, @Nullable Predicate<? super T> action) {
        this(caseConstant, null, action);
    }

    /**
     * <p>
     * Constructs a switch case with given case constant, case tester and case action. Actual action will return false
     * after given action performed. If given tester or action is null, it will set default instead.
     * </p>
     * 
     * @param caseConstant
     *            given case constant
     * @param tester
     *            given case tester
     * @param action
     *            given case action
     * @since 0.0.0
     */
    public Case(@Nullable T caseConstant, @Nullable Predicate<? super T> tester, @Nullable Consumer<? super T> action) {
        this(caseConstant, tester, returnFalse(action));
    }

    /**
     * <p>
     * Constructs a switch case with given case constant, case tester and case action. If given tester or action is
     * null, it will set default instead.
     * </p>
     * 
     * @param caseConstant
     *            given case constant
     * @param tester
     *            given case tester
     * @param action
     *            given case action
     * @since 0.0.0
     */
    public Case(@Nullable T caseConstant, @Nullable Predicate<? super T> tester,
            @Nullable Predicate<? super T> action) {
        this.constant = caseConstant;
        this.tester = tester == null ? t -> Checker.isEqual(getCaseConstant(), t) : tester;
        this.action = action == null ? t -> false : action;
    }

    /**
     * <p>
     * Returns case constant.
     * </p>
     * 
     * @return case constant
     * @since 0.0.0
     */
    public @Nullable T getCaseConstant() {
        return constant;
    }

    /**
     * <p>
     * Tests whether given argument matches the condition to enter this switch case.
     * </p>
     * 
     * @param arg
     *            whether given argument matches the condition to enter this switch case
     * @return true if it matches
     * @since 0.0.0
     */
    public boolean test(@Nullable T arg) {
        return tester.test(arg);
    }

    /**
     * <p>
     * Performs action of this switch case with given argument.
     * </p>
     * 
     * @param arg
     *            given argument
     * @return true if continues to next switch case, or false if breaks this switch
     * @since 0.0.0
     */
    public boolean perform(@Nullable T arg) {
        return action.test(arg);
    }
}
