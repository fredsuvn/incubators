/*
 * File: DefaultExceptionExtension.java
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-02-14T21:32:01+08:00
 * @since 0.0.0, 2016-02-14T21:32:01+08:00
 */

package com.cogician.quicker;

import java.io.OutputStream;
import java.io.Writer;
import java.util.List;

import javax.annotation.Nullable;

import com.cogician.quicker.util.ThrowableQuicker;

/**
 * <p>
 * Common throwable extension, an interface only provides default methods to extend {@linkplain Throwable}. This
 * interface is used in and assume that subtype of this interface is also subtype of {@linkplain Throwable}, then the
 * default methods of this interface can normally perform. If not, performance of default methods is unpredictable.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-02-14T21:32:01+08:00
 * @since 0.0.0, 2016-02-14T21:32:01+08:00
 */
public interface CommonThrowableExtension {

    /**
     * <p>
     * Gets stack trace info of this instance as a string, the content of info is same as printed of
     * {@linkplain #printStackTrace()}.
     * </p>
     *
     * @return stack trace info as string
     * @since 0.0.0
     */
    default String getStackTraceAsString() {
        return ThrowableQuicker.getStackTrace((Throwable)this);
    }

    /**
     * <p>
     * Prints stack trace info of this instance into the specified output stream, the content of info is same as printed
     * of {@linkplain #printStackTrace()}.
     * </p>
     *
     * @param out
     *            specified output stream, not null
     * @throws NullPointerException
     *             if specified output stream is null
     * @since 0.0.0
     */
    default void printStackTrace(final OutputStream out) throws NullPointerException {
        ThrowableQuicker.printStackTrace((Throwable)this, out);
    }

    /**
     * <p>
     * Prints stack trace info of this instance into the specified writer, the content of info is same as printed of
     * {@linkplain #printStackTrace()}.
     * </p>
     *
     * @param writer
     *            specified writer, not null
     * @throws NullPointerException
     *             if specified writer is null
     * @since 0.0.0
     */
    default void printStackTrace(final Writer writer) throws NullPointerException {
        ThrowableQuicker.printStackTrace((Throwable)this, writer);
    }

    /**
     * <p>
     * Gets cause chain of this instance as an array. The 0th element of returned list is this instance, the 1st is
     * {@linkplain #getCause()} from 0th, the 2nd is {@linkplain #getCause()} from 1st, and so on.
     * </p>
     *
     * @return cause chain of this instance as an array, not null
     * @since 0.0.0
     */
    default Throwable[] getCauseChain() {
        return ThrowableQuicker.getCauseChain((Throwable)this);
    }

    /**
     * <p>
     * Gets cause chain of this instance as a list. The 0th element of returned list is this instance, the 1st is
     * {@linkplain #getCause()} from 0th, the 2nd is {@linkplain #getCause()} from 1st, and so on.
     * </p>
     *
     * @return cause chain of this instance as a list, not null
     * @since 0.0.0
     */
    default List<Throwable> getCauseChainList() {
        return ThrowableQuicker.getCauseChainList((Throwable)this);
    }

    /**
     * <p>
     * Uses {@linkplain #getCause()} to get root cause of this instance. It may be return null if there is no cause
     * which was caused by this instance -- possibly this instance is the root cause, or other unknown problem.
     * </p>
     *
     * @return root cause of this instance, may be null
     * @since 0.0.0
     */
    default @Nullable Throwable getRootCause() {
        return ThrowableQuicker.getRootCause((Throwable)this);
    }
}
