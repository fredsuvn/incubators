package com.cogician.quicker;

/**
 * <p>
 * This runtime exception can be thrown when a out of bounds occured in any possible place.
 * </p>
 * <p>
 * A stream also can throw this exception when reads/writes out of end of stream.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2015-01-15 10:15:19
 * @since 0.0.0
 */
public class OutOfBoundsException extends CommonRuntimeException {

    /**
     * Serial version ID.
     * 
     * @since 0.0.0
     */
    private static final long serialVersionUID = 0L;

    /**
     * Constructs an empty instance with no clear cause.
     *
     * @since 0.0.0
     */
    public OutOfBoundsException() {
        super("Out of bounds.");
    }

    /**
     * Constructs an instance with detail position where out of bounds.
     *
     * @param pos
     *            detail position
     * @since 0.0.0
     */
    public OutOfBoundsException(final int pos) {
        super("Out of bounds: " + pos + ".");
    }

    /**
     * Constructs an instance with detail position where out of bounds.
     *
     * @param pos
     *            detail position
     * @since 0.0.0
     */
    public OutOfBoundsException(final long pos) {
        super("Out of bounds: " + pos + ".");
    }

    /**
     * Constructs an instance with detail position where out of bounds.
     *
     * @param pos
     *            detail position
     * @since 0.0.0
     */
    public OutOfBoundsException(final float pos) {
        super("Out of bounds: " + pos + ".");
    }

    /**
     * Constructs an instance with detail position where out of bounds.
     *
     * @param pos
     *            detail position
     * @since 0.0.0
     */
    public OutOfBoundsException(final double pos) {
        super("Out of bounds: " + pos + ".");
    }

    /**
     * Constructs an instance with detail position where out of bounds.
     *
     * @param pos
     *            detail position
     * @since 0.0.0
     */
    public OutOfBoundsException(final Number pos) {
        super("Out of bounds: " + pos + ".");
    }

    /**
     * Constructs an instance with detail message.
     *
     * @param message
     *            detail message
     * @since 0.0.0
     */
    public OutOfBoundsException(final String message) {
        super("Out of bounds: " + message);
    }

    /**
     * Constructs an instance with detail position where out of bounds and cause.
     *
     * @param pos
     *            detail position out of bounds
     * @param cause
     *            detail cause
     * @since 0.0.0
     */
    public OutOfBoundsException(final int pos, final Throwable cause) {
        super("Out of bounds: " + pos + ".", cause);
    }

    /**
     * Constructs an instance with detail position where out of bounds and cause.
     *
     * @param pos
     *            detail position out of bounds
     * @param cause
     *            detail cause
     * @since 0.0.0
     */
    public OutOfBoundsException(final long pos, final Throwable cause) {
        super("Out of bounds: " + pos + ".", cause);
    }

    /**
     * Constructs an instance with detail position where out of bounds and cause.
     *
     * @param pos
     *            detail position out of bounds
     * @param cause
     *            detail cause
     * @since 0.0.0
     */
    public OutOfBoundsException(final float pos, final Throwable cause) {
        super("Out of bounds: " + pos + ".", cause);
    }

    /**
     * Constructs an instance with detail position where out of bounds and cause.
     *
     * @param pos
     *            detail position out of bounds
     * @param cause
     *            detail cause
     * @since 0.0.0
     */
    public OutOfBoundsException(final double pos, final Throwable cause) {
        super("Out of bounds: " + pos + ".", cause);
    }

    /**
     * Constructs an instance with detail position where out of bounds and cause.
     *
     * @param pos
     *            detail position out of bounds
     * @param cause
     *            detail cause
     * @since 0.0.0
     */
    public OutOfBoundsException(final Number pos, final Throwable cause) {
        super("Out of bounds: " + pos + ".", cause);
    }

    /**
     * Constructs an instance with detail message and cause.
     *
     * @param message
     *            detail message
     * @param cause
     *            detail cause
     * @since 0.0.0
     */
    public OutOfBoundsException(final String message, final Throwable cause) {
        super("Out of bounds: " + message, cause);
    }
}
