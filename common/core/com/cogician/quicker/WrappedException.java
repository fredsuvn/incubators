/*
 * File: WrappedException.java
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-02-14T20:13:22+08:00
 * @since 0.0.0, 2016-02-14T20:13:22+08:00
 */

package com.cogician.quicker;

import java.util.Objects;

/**
 * <p>
 * A runtime exception used to wrap other throwable. This exception itself has no representation. It is commonly used in
 * the situation that need throw an un-runtime exception, wrapping the un-runtime exception and throwing. Use
 * {@linkplain #getWrapped()} to get wrapped throwable.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-02-14T20:13:22+08:00
 * @since 0.0.0, 2016-02-14T20:13:22+08:00
 */
public class WrappedException extends CommonRuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * <p>
     * Wrapped throwable, not null.
     * </p>
     * 
     * @since 0.0.0
     */
    private final Throwable wrapped;

    /**
     * <p>
     * Constructs with wrapped throwable. Use {@linkplain #getWrapped()} to get wrapped throwable.
     * </p>
     * 
     * @param wrapped
     *            wrapped throwable
     * @throws NullPointerException
     *             if wrapped throwable is null
     * @since 0.0.0
     */
    public WrappedException(Throwable wrapped) throws NullPointerException {
        super("Wrapped Throwable: " + Objects.requireNonNull(wrapped));
        this.wrapped = wrapped;
    }

    /**
     * <p>
     * Returns wrapped throwable.
     * </p>
     * 
     * @return wrapped throwable
     * @since 0.0.0
     */
    public Throwable getWrapped() {
        return wrapped;
    }
}
