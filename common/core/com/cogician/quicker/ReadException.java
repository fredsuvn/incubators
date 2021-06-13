package com.cogician.quicker;

/**
 * <p>
 * Read exception may be thrown when a problem of read operation occurs.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-08-02T12:51:51+08:00
 * @since 0.0.0, 2016-08-02T12:51:51+08:00
 */
public class ReadException extends CommonRuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * <p>
     * Constructs a read exception with no detail message.
     * </p>
     * 
     * @since 0.0.0
     */
    public ReadException() {
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
    public ReadException(final String message) {
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
    public ReadException(final Throwable cause) {
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
    public ReadException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
