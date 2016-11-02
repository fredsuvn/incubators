package com.cogician.quicker.struct;

import java.util.List;

import javax.annotation.Nullable;

import com.cogician.quicker.Buildable;
import com.cogician.quicker.Checker;
import com.cogician.quicker.Quicker;

/**
 * <p>
 * A builder to build {@linkplain Switch}.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-08-02T13:31:10+08:00
 * @since 0.0.0, 2016-08-02T13:31:10+08:00
 * @see Switch
 */
public class SwitchBuilder<T> implements Buildable<Switch<T>> {

    private final LinkedNode<Case<T>> head = new LinkedNode<>(Case.getUseless());

    private LinkedNode<Case<T>> tail = head.linkNext(head);

    @Nullable
    private Case<T> defaultCase = null;

    /**
     * <p>
     * Adds given switch case.
     * </p>
     * 
     * @param switchCase
     *            given switch case
     * @return this
     * @throws NullPointerException
     *             if given switch case is null
     * @since 0.0.0
     */
    public SwitchBuilder<T> addCase(Case<T> switchCase) throws NullPointerException {
        tail = tail.linkNextByValue(Quicker.require(switchCase));
        tail.linkNext(head);
        return this;
    }

    /**
     * <p>
     * Adds given switch cases.
     * </p>
     * 
     * @param switchCases
     *            given switch cases.
     * @return this
     * @throws NullPointerException
     *             if given switch cases or any element it is null
     * @since 0.0.0
     */
    public SwitchBuilder<T> addCases(@SuppressWarnings("unchecked") Case<T>... switchCases)
            throws NullPointerException {
        Checker.checkNullElement(switchCases);
        tail = tail.linkNextsByValues(switchCases);
        tail.linkNext(head);
        return this;
    }

    /**
     * <p>
     * Adds given switch cases.
     * </p>
     * 
     * @param switchCases
     *            given switch cases.
     * @return this
     * @throws NullPointerException
     *             if given switch cases or any element it is null
     * @since 0.0.0
     */
    public SwitchBuilder<T> addCases(List<Case<T>> switchCases) throws NullPointerException {
        Checker.checkNullElement(switchCases);
        tail = tail.linkNextsByValues(switchCases);
        tail.linkNext(head);
        return this;
    }

    /**
     * <p>
     * Sets default switch case.
     * </p>
     * 
     * @param defaultCase
     *            given switch case
     * @return this
     * @throws NullPointerException
     *             if given switch case is null
     * @since 0.0.0
     */
    public SwitchBuilder<T> setDefaultCase(Case<T> defaultCase) throws NullPointerException {
        this.defaultCase = defaultCase;
        return this;
    }

    /**
     * <p>
     * Builds {@linkplain Switch} according to given cases and mode.
     * </p>
     * 
     * @return {@linkplain Switch} according to given cases and mode
     * @since 0.0.0
     */
    @Override
    public Switch<T> build() {
        return new Switch<>(head, defaultCase == null ? Case.getUseless() : defaultCase);
    }
}
