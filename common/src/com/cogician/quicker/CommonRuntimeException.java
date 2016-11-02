package com.cogician.quicker;

/**
 * <p>
 * Common runtime exception, extended exception of {@linkplain RuntimeException}. It is recommended that use this class
 * instead of {@linkplain RuntimeException}.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2015-10-10 14:36:10
 * @since 0.0.0
 */
public class CommonRuntimeException extends RuntimeException implements CommonThrowableExtension {

    /**
     * Serial version UID.
     * 
     * @since 0.0.0
     */
    private static final long serialVersionUID = 1L;

    /**
     * <p>
     * Constructs a common runtime exception with no detail message.
     * </p>
     * 
     * @since 0.0.0
     */
    public CommonRuntimeException() {
        super();
    }

    /**
     * <p>
     * Constructs a common runtime exception with detail message.
     * </p>
     *
     * @param message
     *            detail message
     * @since 0.0.0
     */
    public CommonRuntimeException(final String message) {
        super(message);
    }

    /**
     * <p>
     * Constructs a common runtime exception with cause.
     * </p>
     *
     * @param cause
     *            cause of this exception
     * @since 0.0.0
     */
    public CommonRuntimeException(final Throwable cause) {
        super(cause);
    }

    /**
     * <p>
     * Constructs a common runtime exception with detail message and cause.
     * </p>
     *
     * @param message
     *            detail message
     * @param cause
     *            cause of this exception
     * @since 0.0.0
     */
    public CommonRuntimeException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
