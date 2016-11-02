package com.cogician.quicker.sql;

/**
 * Thrown if simple sql error occurs.
 *
 * @author Fred Suvn
 * @version 0.0.0, 2015-06-30 17:12:58
 */
public class SimpleSQLException extends RuntimeException {
    /**
     * Serial ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs with no parameter.
     */
    public SimpleSQLException() {
        super();
    }

    /**
     * Constructs with given cause.
     *
     * @param cause
     *            given cause
     */
    public SimpleSQLException(final Throwable cause) {
        super(cause);
    }

    /**
     * Constructs with given detail message.
     *
     * @param message
     *            given detail message
     */
    public SimpleSQLException(final String message) {
        super(message);
    }

    /**
     * Constructs with given detail message and given cause.
     *
     * @param message
     *            given detail message
     * @param cause
     *            given cause
     */
    public SimpleSQLException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
