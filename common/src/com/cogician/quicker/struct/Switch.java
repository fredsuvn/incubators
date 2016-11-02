package com.cogician.quicker.struct;

import java.util.Collection;
import java.util.stream.Stream;

import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import com.cogician.quicker.Quicker;

/**
 * <p>
 * This class represents a bigger and more flexible switch and if-else statement.
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
 * Switch{@code <}Integer{@code >} s = new SwitchBuilder{@code <}Integer{@code >}().addCase(new Case{@code <}Integer{@code >}(1, i -> {
 *     System.out.println("this is 1.");
 * })).addCase(new Case{@code <}Integer{@code >}(2, i -> {
 *     System.out.println("this is 2.");
 * })).addCase(new Case{@code <}Integer{@code >}(3, i -> {
 *     System.out.println("this is 3.");
 * })).setDefaultCase(new Case{@code <}Integer{@code >}(null, i -> {
 *     System.out.println("this is a number.");
 * })).build();
 * s.perform(num);
 * </pre>
 * </p>
 * <p>
 * Each {@linkplain Case} represents a case label or if-body in switch or if-else statement but it is more flexible,
 * seeing {@linkplain Case} to more detail. There are two mode to iterate each switch case: {@linkplain #ORDER} and
 * {@linkplain CYCLE}. Order mode always iterate from first switch case to end. For cycle mode, at the beginning it
 * iterates from first switch case, and it will record last broken case, and iterates from recorded case next time (all
 * cases in a cycle linked list).
 * </p>
 * <p>
 * Note if a switch case's action returns true, that means this case is not break, that means next cases will not test
 * passed argument until a case action returns false to break this switch. Default case never test the passed argument.
 * </p>
 * <p>
 * Switch is thread-safe.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-07-31T14:23:20+08:00
 * @since 0.0.0, 2016-07-31T14:23:20+08:00
 * @see Case
 */
@ThreadSafe
public class Switch<T> {

    /**
     * <p>
     * Order perform mode. In this mode, this switch will test given argument from head case to tail case in adding
     * order.
     * </p>
     * 
     * @since 0.0.0
     */
    public static final int ORDER = 0;

    /**
     * <p>
     * Cycle perform mode. In this mode, this switch will record last broken case and start to test from last recorded
     * case in cycle order next time until all cases has been tested.
     * </p>
     * 
     * @since 0.0.0
     */
    public static final int CYCLE = 1;

    private final LinkedNode<Case<T>> head;

    @Nullable
    private final Case<T> defaultCase;

    private final ThreadLocal<LinkedNode<Case<T>>> cur;

    private final int caseNum;

    /**
     * <p>
     * Constructs a switch used by {@linkplain SwitchBuilder}. Given head case links all cases, and it has at least one
     * useless case (so the actual cases number should be subtract one from number of this linked list).
     * </p>
     * 
     * @param head
     *            head case
     * @param defaultCase
     *            default case;
     * @since 0.0.0
     */
    Switch(LinkedNode<Case<T>> head, Case<T> defaultCase) {
        this.head = head;
        this.defaultCase = defaultCase;
        this.cur = ThreadLocal.withInitial(() -> head);
        int[] count = {0};
        Quicker.each(head.iteratorInNext(), c -> {
            count[0]++;
        });
        this.caseNum = count[0];
    }

    /**
     * <p>
     * Returns number of this switch cases.
     * </p>
     * 
     * @return number of this switch cases
     * @since 0.0.0
     */
    public int getCaseNumber() {
        return caseNum - 1;
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
        performInOrder(arg);
    }

    /**
     * <p>
     * Performs this switch with given argument in specified mode.
     * </p>
     * 
     * @param mode
     *            specified mode
     * @param arg
     *            given argument
     * @since 0.0.0
     */
    public void perform(int mode, T arg) {
        switch (mode) {
            case ORDER: {
                performInOrder(arg);
                break;
            }
            case CYCLE: {
                performInCycle(arg);
                break;
            }
            default: {
                performInOrder(arg);
            }
        }
    }

    /**
     * <p>
     * Performs this switch with each given arguments.
     * </p>
     * 
     * @param args
     *            given arguments
     * @since 0.0.0
     */
    public void performEach(Collection<T> args) {
        Quicker.each(args, a -> {
            perform(a);
        });
    }

    /**
     * <p>
     * Performs this switch with each given arguments in specified mode.
     * </p>
     * 
     * @param mode
     *            specified mode
     * @param args
     *            given arguments
     * @since 0.0.0
     */
    public void performEach(int mode, Collection<T> args) {
        Quicker.each(args, a -> {
            perform(mode, a);
        });
    }

    /**
     * <p>
     * Performs this switch with each given arguments.
     * </p>
     * 
     * @param args
     *            given arguments
     * @since 0.0.0
     */
    public void performEach(Stream<T> args) {
        Quicker.each(args, a -> {
            perform(a);
        });
    }

    /**
     * <p>
     * Performs this switch with each given arguments in specified mode.
     * </p>
     * 
     * @param mode
     *            specified mode
     * @param args
     *            given arguments
     * @since 0.0.0
     */
    public void performEach(int mode, Stream<T> args) {
        Quicker.each(args, a -> {
            perform(mode, a);
        });
    }

    private void performInOrder(T arg) {
        boolean found = false;
        LinkedNode<Case<T>> switchCase = head.getNext();
        while (switchCase != head) {
            Case<T> sc = switchCase.getValue();
            switchCase = switchCase.getNext();
            if (found) {
                if (!sc.perform(arg)) {
                    break;
                }
            } else if (sc.test(arg)) {
                found = true;
                if (sc.perform(arg)) {
                    continue;
                } else {
                    break;
                }
            }
        }
        if (!found) {
            defaultCase.perform(arg);
        }
    }

    private void performInCycle(T arg) {
        boolean found = false;
        int count = 0;
        LinkedNode<Case<T>> curr = cur.get();
        while (count++ < getCaseNumber() + 1) {
            Case<T> sc = curr.getValue();
            curr = curr.getNext();
            if (found) {
                if (!sc.perform(arg)) {
                    break;
                }
            } else if (sc.test(arg)) {
                found = true;
                if (sc.perform(arg)) {
                    continue;
                } else {
                    break;
                }
            }
        }
        if (!found) {
            defaultCase.perform(arg);
        }
    }
}
