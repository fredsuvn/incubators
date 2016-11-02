package com.cogician.quicker.util.lexer;

import com.cogician.quicker.CommonRuntimeException;

/**
 * <p>
 * Parsing exception thrown when a parsing problem occurs.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-01-28T10:56:34+08:00
 * @since 0.0.0, 2016-01-28T10:56:34+08:00
 */
public class ParsingException extends CommonRuntimeException {

    /**
     * Serial ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * <p>
     * Constructs a parsing exception with no detail message.
     * </p>
     * 
     * @since 0.0.0
     */
    public ParsingException() {
        super();
    }

    /**
     * <p>
     * Constructs a parsing exception with detail message.
     * </p>
     *
     * @param message
     *            detail message
     * @since 0.0.0
     */
    public ParsingException(final String message) {
        super(message);
    }

    /**
     * <p>
     * Constructs a parsing exception with cause.
     * </p>
     *
     * @param cause
     *            cause of this exception
     * @since 0.0.0
     */
    public ParsingException(final Throwable cause) {
        super(cause);
    }

    /**
     * <p>
     * Constructs a parsing exception with detail message and cause.
     * </p>
     *
     * @param message
     *            detail message
     * @param cause
     *            cause of this exception
     * @since 0.0.0
     */
    public ParsingException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * <p>
     * Constructs a parsing exception with detail message and index of illegal character.
     * </p>
     *
     * @param message
     *            detail message
     * @param index
     *            index of illegal character
     * @since 0.0.0
     */
    public ParsingException(String message, int index) {
        super("Illegal character \"" + message.charAt(index) + "\" at index " + index + " in the string " + message);
    }
}
