package com.cogician.quicker.binary;

import java.io.IOException;

import com.cogician.quicker.CommonRuntimeException;

/**
 * <p>
 * This exception like {@linkplain IOException}, it may be thrown if any problem occurs when doing a binary operation.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-08-24T09:32:32+08:00
 * @since 0.0.0, 2016-08-24T09:32:32+08:00
 */
public class BinaryException extends CommonRuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * <p>
     * Constructs a binary exception with no detail message.
     * </p>
     * 
     * @since 0.0.0
     */
    public BinaryException() {
        super();
    }

    /**
     * <p>
     * Constructs a binary exception with detail message.
     * </p>
     *
     * @param message
     *            detail message
     * @since 0.0.0
     */
    public BinaryException(final String message) {
        super(message);
    }

    /**
     * <p>
     * Constructs a binary exception with cause.
     * </p>
     *
     * @param cause
     *            cause of this exception
     * @since 0.0.0
     */
    public BinaryException(final Throwable cause) {
        super(cause);
    }

    /**
     * <p>
     * Constructs a binary exception with detail message and cause.
     * </p>
     *
     * @param message
     *            detail message
     * @param cause
     *            cause of this exception
     * @since 0.0.0
     */
    public BinaryException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
