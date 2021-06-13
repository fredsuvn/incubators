package com.cogician.quicker;

/**
 * <p>
 * How do you turn this on!
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-07-26T15:24:38+08:00
 * @since 0.0.0, 2016-07-26T15:24:38+08:00
 */
public class WTFException extends CommonRuntimeException {

    private static final long serialVersionUID = 1L;
    
    public static final String HOW_DO_YOU_TURN_THIS_ON = "How do you turn this on!";

    /**
     * <p>
     * Constructs a WTFException with no detail message.
     * </p>
     * 
     * @since 0.0.0
     */
    public WTFException() {
        super();
    }

    /**
     * <p>
     * Constructs a WTFException with detail message.
     * </p>
     *
     * @param message
     *            detail message
     * @since 0.0.0
     */
    public WTFException(final String message) {
        super(message);
    }

    /**
     * <p>
     * Constructs a WTFException with cause.
     * </p>
     *
     * @param cause
     *            cause of this exception
     * @since 0.0.0
     */
    public WTFException(final Throwable cause) {
        super(cause);
    }

    /**
     * <p>
     * Constructs a WTFException with detail message and cause.
     * </p>
     *
     * @param message
     *            detail message
     * @param cause
     *            cause of this exception
     * @since 0.0.0
     */
    public WTFException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
