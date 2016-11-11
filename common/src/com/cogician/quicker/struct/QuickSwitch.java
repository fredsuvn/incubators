package com.cogician.quicker.struct;

import java.util.List;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;

import com.cogician.quicker.annotation.Whisper;

/**
 * <p>
 * This class represents a switch or if-else statement.
 * </p>
 * <p>
 * For example, a switch statement
 * 
 * <pre>
 * switch (num) {
 *     case 1:
 *         System.out.println("this is 1.");
 *         break;
 *     case 2:
 *         System.out.println("this is 2.");
 *         break;
 *     case 3:
 *         System.out.println("this is 3.");
 *         break;
 *     default:
 *         System.out.println("this is a number.");
 * }
 * </pre>
 * 
 * or an if-else statement:
 * 
 * <pre>
 * if (num == 1) {
 *     System.out.println("this is 1.");
 * } else if (num == 2) {
 *     System.out.println("this is 2.");
 * } else if (num == 3) {
 *     System.out.println("this is 3.");
 * } else {
 *     System.out.println("this is a number.");
 * }
 * </pre>
 * 
 * can also write:
 * 
 * <pre>
 * QuickSwitch{@code <}Integer{@code >} quikSwitch = new QuickSwitch{@code <}Integer{@code >}(Arrays.asList(
 *     new QuickCase{@code <}Integer{@code >}(
 *         i -{@code >} i == 1, i -{@code >} System.out.println("this is 1.")),
 *     new QuickCase{@code <}Integer{@code >}(
 *         i -{@code >} i == 2, i -{@code >} System.out.println("this is 2.")),
 *     new QuickCase{@code <}Integer{@code >}(
 *         i -{@code >} i == 3, i -{@code >} System.out.println("this is 3."))),
 *     new QuickCase{@code <}Integer{@code >}(
 *         null, i -{@code >} System.out.println("this is a number.")));
 *         
 * quikSwitch.perform(num);
 * </pre>
 * </p>
 * <p>
 * Each {@linkplain QuickCase} represents a case label or if-body. QuickSwitch first test each case, if succeeds, switch
 * will perform its action. If the action return true, switch will go to next case, or break if false. QuickSwitch may
 * has a default case at last which is used without testing if the switch doesn't break.
 * </p>
 * <p>
 * QuickSwitch itself is thread-safe.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-07-31T14:23:20+08:00
 * @since 0.0.0, 2016-07-31T14:23:20+08:00
 * @see QuickCase
 */
@Immutable
@ThreadSafe
public class QuickSwitch<T> {

    private static final QuickCase<?>[] TYPE = new QuickCase[0];

    @SuppressWarnings("unchecked")
    private static final <T> QuickCase<T>[] getArray(List<QuickCase<T>> caseList) {
        return caseList.toArray((QuickCase<T>[])TYPE);
    }

    private final QuickCase<T>[] cases;

    private final QuickCase<T> defaultCase;

    /**
     * <p>
     * Constructs a switch with specified case list and default case. specified case list still can be used after this
     * calling.
     * </p>
     * 
     * @param cases
     *            specified cases
     * @param defaultCase
     *            specified default case
     * @since 0.0.0
     */
    public QuickSwitch(QuickCase<T>[] cases, @Nullable QuickCase<T> defaultCase) {
        this.cases = cases;
        this.defaultCase = defaultCase;
    }

    /**
     * <p>
     * Constructs a switch with specified case list and default case.
     * </p>
     * 
     * @param caseList
     *            specified case list
     * @param defaultCase
     *            specified default case
     * @since 0.0.0
     */
    public QuickSwitch(List<QuickCase<T>> caseList, @Nullable QuickCase<T> defaultCase) {
        this(getArray(caseList), defaultCase);
    }

    /**
     * <p>
     * Returns cases number of this switch.
     * </p>
     * 
     * @return cases number of this switch
     * @since 0.0.0
     */
    public int getCaseNumber() {
        return cases.length;
    }

    /**
     * <p>
     * Performs this switch with given argument.
     * </p>
     * 
     * @param arg
     *            given argument
     * @since 0.0.0
     */
    public void perform(T arg) {
        perform0(arg);
    }

    /**
     * <p>
     * Performs this switch with specified performance mode and argument.
     * </p>
     * 
     * @param mode
     *            specified performance mode
     * @param arg
     *            specified argument
     * @since 0.0.0
     */
    @Whisper("Actually, mode is useless..")
    public void perform(PerformMode mode, T arg) {
        switch (mode) {
            case ORDERED:
            case UNORDERED:
            default: {
                perform0(arg);
            }
        }
    }

    private void perform0(T arg) {
        for (int i = 0; i < cases.length; i++) {
            QuickCase<T> quickCase = cases[i];
            if (quickCase.test(arg)) {
                if (!quickCase.perform(arg)) {
                    return;
                }
            }
        }
        if (defaultCase != null) {
            defaultCase.perform(arg);
        }
    }

    /**
     * <p>
     * Performance mode of quick switch, ordered or unordered.
     * </p>
     *
     * @author Fred Suvn
     * @version 0.0.0, 2016-11-08T17:19:13+08:00
     * @since 0.0.0, 2016-11-08T17:19:13+08:00
     */
    public static enum PerformMode {

        /**
         * <p>
         * Ordered mode.
         * </p>
         * 
         * @since 0.0.0
         */
        ORDERED,

        /**
         * <p>
         * Unordered mode.
         * </p>
         * 
         * @since 0.0.0
         */
        UNORDERED
    }
}
