package com.cogician.quicker;

/**
 * <p>
 * Write exception may be thrown when a problem of write operation occurs.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-08-02T12:49:54+08:00
 * @since 0.0.0, 2016-08-02T12:49:54+08:00
 */
public class WriteException extends CommonRuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * <p>
     * Constructs a write exception with no detail message.
     * </p>
     * 
     * @since 0.0.0
     */
    public WriteException() {
        super();
    }

    /**
     * <p>
     * Constructs a write exception with detail message.
     * </p>
     *
     * @param message
     *            detail message
     * @since 0.0.0
     */
    public WriteException(final String message) {
        super(message);
    }

    /**
     * <p>
     * Constructs a write exception with cause.
     * </p>
     *
     * @param cause
     *            cause of this exception
     * @since 0.0.0
     */
    public WriteException(final Throwable cause) {
        super(cause);
    }

    /**
     * <p>
     * Constructs a write exception with detail message and cause.
     * </p>
     *
     * @param message
     *            detail message
     * @param cause
     *            cause of this exception
     * @since 0.0.0
     */
    public WriteException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
