package com.cogician.quicker;

/**
 * <p>
 * Close exception may be thrown when a problem of close operation occurs.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-08-16T12:12:04+08:00
 * @since 0.0.0, 2016-08-16T12:12:04+08:00
 */
public class CloseException extends CommonRuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * <p>
     * Constructs a read exception with no detail message.
     * </p>
     * 
     * @since 0.0.0
     */
    public CloseException() {
        super();
    }

    /**
     * <p>
     * Constructs a read exception with detail message.
     * </p>
     *
     * @param message
     *            detail message
     * @since 0.0.0
     */
    public CloseException(final String message) {
        super(message);
    }

    /**
     * <p>
     * Constructs a read exception with cause.
     * </p>
     *
     * @param cause
     *            cause of this exception
     * @since 0.0.0
     */
    public CloseException(final Throwable cause) {
        super(cause);
    }

    /**
     * <p>
     * Constructs a read exception with detail message and cause.
     * </p>
     *
     * @param message
     *            detail message
     * @param cause
     *            cause of this exception
     * @since 0.0.0
     */
    public CloseException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
