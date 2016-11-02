package com.cogician.quicker.util;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import com.cogician.quicker.Checker;

/**
 * <p>
 * Static quick utility class provides simple methods for throwable.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2015-10-10 15:56:41
 * @since 0.0.0
 */
public class ThrowableQuicker {

    /**
     * <p>
     * Gets stack trace info of specified throwable object as a string, the content of info is same as printed of
     * {@linkplain Throwable#printStackTrace()}.
     * </p>
     *
     * @param t
     *            specified throwable object, not null
     * @return stack trace info of specified throwable object as a string
     * @throws NullPointerException
     *             if specified throwable object is null
     * @since 0.0.0
     */
    public static String getStackTrace(final Throwable t) throws NullPointerException {
        Checker.checkNull(t);
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        pw.flush();
        return sw.getBuffer().toString();
    }

    /**
     * <p>
     * Prints stack trace info of specified throwable object into the specified output stream, the content of info is
     * same as printed of {@linkplain Throwable#printStackTrace()}.
     * </p>
     *
     * @param t
     *            specified throwable object, not null
     * @param out
     *            specified output stream, not null
     * @throws NullPointerException
     *             if specified throwable object and/or output stream is null
     * @since 0.0.0
     */
    public static void printStackTrace(final Throwable t, final OutputStream out) throws NullPointerException {
        Checker.checkNull(t);
        Checker.checkNull(out);
        final PrintStream ps = new PrintStream(out);
        t.printStackTrace(ps);
        ps.flush();
    }

    /**
     * <p>
     * Prints stack trace info of specified throwable object into the specified writer, the content of info is same as
     * printed of {@linkplain Throwable#printStackTrace()}.
     * </p>
     *
     * @param t
     *            specified throwable object, not null
     * @param writer
     *            specified writer, not null
     * @throws NullPointerException
     *             if specified throwable object and/or writer is null
     * @since 0.0.0
     */
    public static void printStackTrace(final Throwable t, final Writer writer) throws NullPointerException {
        Checker.checkNull(t);
        Checker.checkNull(writer);
        final PrintWriter pw = new PrintWriter(writer);
        t.printStackTrace(pw);
        pw.flush();
    }

    /**
     * <p>
     * Gets cause chain of specified throwable object as an array. The 0th element of returned list is specified
     * throwable object, the 1st is {@linkplain Throwable#getCause()} from 0th, the 2nd is
     * {@linkplain Throwable#getCause()} from 1st, and so on.
     * </p>
     *
     * @param t
     *            specified throwable object, not null
     * @return cause chain of specified throwable object as an array, not null
     * @throws NullPointerException
     *             if specified throwable object is null
     * @since 0.0.0
     */
    public static Throwable[] getCauseChain(final Throwable t) throws NullPointerException {
        final List<Throwable> chain = getCauseChainList(t);
        return chain.toArray(new Throwable[chain.size()]);
    }

    /**
     * <p>
     * Gets cause chain of specified throwable object as a list. The 0th element of returned list is specified throwable
     * object, the 1st is {@linkplain Throwable#getCause()} from 0th, the 2nd is {@linkplain Throwable#getCause()} from
     * 1st, and so on.
     * </p>
     *
     * @param t
     *            specified throwable object, not null
     * @return cause chain of specified throwable object as a list, not null
     * @throws NullPointerException
     *             if specified throwable object is null
     * @since 0.0.0
     */
    public static List<Throwable> getCauseChainList(final Throwable t) throws NullPointerException {
        Checker.checkNull(t);
        final List<Throwable> chain = new ArrayList<>();
        Throwable element = t;
        while (element != null) {
            chain.add(element);
            element = element.getCause();
        }
        return chain;
    }

    /**
     * <p>
     * Uses {@linkplain Throwable#getCause()} to get root cause of specified throwable object. It may be return null if
     * there is no cause which was caused by specified throwable object -- possibly specified throwable object is the
     * root cause, or other unknown problem.
     * </p>
     *
     * @param t
     *            specified throwable object, not null
     * @return root cause of specified throwable object, may be null
     * @throws NullPointerException
     *             if specified throwable object is null
     * @since 0.0.0
     */
    public static Throwable getRootCause(final Throwable t) throws NullPointerException {
        Throwable cause = t.getCause();
        if (cause != null) {
            Throwable temp = null;
            while ((temp = cause.getCause()) != null) {
                cause = temp;
            }
        }
        return cause;
    }

}
