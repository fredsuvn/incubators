package com.cogician.quicker;

/**
 * <p>
 * Common exception, extended exception of {@linkplain Exception}. It is recommended that use this class instead of
 * {@linkplain Exception}.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2015-10-10 14:56:50
 * @since 0.0.0
 */
public class CommonException extends Exception implements CommonThrowableExtension {

    /**
     * Serial version UID.
     * 
     * @since 0.0.0
     */
    private static final long serialVersionUID = 1L;

    /**
     * <p>
     * Constructs a common exception with no detail message.
     * </p>
     * 
     * @since 0.0.0
     */
    public CommonException() {
        super();
    }

    /**
     * <p>
     * Constructs a common exception with detail message.
     * </p>
     *
     * @param message
     *            detail message
     * @since 0.0.0
     */
    public CommonException(final String message) {
        super(message);
    }

    /**
     * <p>
     * Constructs a common exception with cause.
     * </p>
     *
     * @param cause
     *            cause of this exception
     * @since 0.0.0
     */
    public CommonException(final Throwable cause) {
        super(cause);
    }

    /**
     * <p>
     * Constructs a common exception with detail message and cause.
     * </p>
     *
     * @param message
     *            detail message
     * @param cause
     *            cause of this exception
     * @since 0.0.0
     */
    public CommonException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
