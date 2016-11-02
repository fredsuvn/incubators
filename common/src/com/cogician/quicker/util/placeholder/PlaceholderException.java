package com.cogician.quicker.util.placeholder;

import com.cogician.quicker.CommonRuntimeException;
import com.cogician.quicker.Quicker;

/**
 * <p>
 * Represents a placeholder exception. It could be thrown when any problem about placeholder occurs.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-08-03T22:44:22+08:00
 * @since 0.0.0, 2016-08-03T22:44:22+08:00
 */
public class PlaceholderException extends CommonRuntimeException {

    private static final long serialVersionUID = 1L;

    private static String dealMessage(String message, String where, int index) {
        if (null == where || where.length() <= index) {
            return message + " At index: " + index + ".";
        }
        int row = 1;
        int col = 1;
        boolean isR = false;
        for (int i = 0; i < index; i++) {
            if ('\r' == where.charAt(i)) {
                row++;
                col = 1;
                isR = true;
            } else if (isR) {
                isR = false;
                if ('\n' != where.charAt(i)) {
                    col++;
                }
            } else if ('\n' == where.charAt(i)) {
                row++;
                col = 1;
            } else {
                col++;
            }
        }
        return message + " At index: " + index + ", row: " + row + ", column: " + col + "; "
                + Quicker.ellipsis(where.substring(index));
    }

    /**
     * <p>
     * Constructs a placeholder exception with no detail message.
     * </p>
     * 
     * @since 0.0.0
     */
    public PlaceholderException() {
        super();
    }

    /**
     * <p>
     * Constructs a placeholder exception with detail message.
     * </p>
     *
     * @param message
     *            detail message
     * @since 0.0.0
     */
    public PlaceholderException(final String message) {
        super(message);
    }

    /**
     * <p>
     * Constructs a placeholder exception with detail message and occurred index.
     * </p>
     *
     * @param message
     *            detail message
     * @param index
     *            occurred index
     * @since 0.0.0
     */
    public PlaceholderException(final String message, int index) {
        super(dealMessage(message, null, index));
    }

    /**
     * <p>
     * Constructs a placeholder exception with detail message, string where occured and occurred index.
     * </p>
     *
     * @param message
     *            detail message
     * @param where
     *            string where occured
     * @param index
     *            occurred index
     * @since 0.0.0
     */
    public PlaceholderException(final String message, String where, int index) {
        super(dealMessage(message, where, index));
    }

    /**
     * <p>
     * Constructs a placeholder exception with cause.
     * </p>
     *
     * @param cause
     *            cause of this exception
     * @since 0.0.0
     */
    public PlaceholderException(final Throwable cause) {
        super(cause);
    }

    /**
     * <p>
     * Constructs a placeholder exception with detail message and cause.
     * </p>
     *
     * @param message
     *            detail message
     * @param cause
     *            cause of this exception
     * @since 0.0.0
     */
    public PlaceholderException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * <p>
     * Constructs a placeholder exception with detail message, cause and occurred index.
     * </p>
     *
     * @param message
     *            detail message
     * @param cause
     *            cause of this exception
     * @param index
     *            occurred index
     * @since 0.0.0
     */
    public PlaceholderException(final String message, final Throwable cause, int index) {
        super(dealMessage(message, null, index), cause);
    }

    /**
     * <p>
     * Constructs a placeholder exception with detail message, cause, string where occured and occurred index.
     * </p>
     *
     * @param message
     *            detail message
     * @param cause
     *            cause of this exception
     * @param where
     *            string where occured
     * @param index
     *            occurred index
     * @since 0.0.0
     */
    public PlaceholderException(final String message, final Throwable cause, String where, int index) {
        super(dealMessage(message, where, index), cause);
    }
}
