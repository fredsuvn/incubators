package com.cogician.quicker.binary;

import java.io.EOFException;

/**
 * <p>
 * Like {@linkplain EOFException} represents read or write operation over end of the file, stream or other binary
 * sources.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-08-22T14:02:27+08:00
 * @since 0.0.0, 2016-08-22T14:02:27+08:00
 */
public class EndOfBinaryException extends BinaryException {

    private static final long serialVersionUID = 1L;

    /**
     * <p>
     * Constructs an end of binary exception with no detail message.
     * </p>
     * 
     * @since 0.0.0
     */
    public EndOfBinaryException() {
        super();
    }

    /**
     * <p>
     * Constructs an end of binary exception with detail message.
     * </p>
     *
     * @param message
     *            detail message
     * @since 0.0.0
     */
    public EndOfBinaryException(final String message) {
        super(message);
    }

    /**
     * <p>
     * Constructs an end of binary exception with cause.
     * </p>
     *
     * @param cause
     *            cause of this exception
     * @since 0.0.0
     */
    public EndOfBinaryException(final Throwable cause) {
        super(cause);
    }

    /**
     * <p>
     * Constructs an end of binary exception with detail message and cause.
     * </p>
     *
     * @param message
     *            detail message
     * @param cause
     *            cause of this exception
     * @since 0.0.0
     */
    public EndOfBinaryException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
