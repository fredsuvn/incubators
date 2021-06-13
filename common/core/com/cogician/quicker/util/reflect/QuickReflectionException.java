package com.cogician.quicker.util.reflect;

import javax.annotation.Nullable;

import com.cogician.quicker.CommonRuntimeException;

/**
 * <p>
 * An runtime exception represents a reflection problem.
 * </p>
 * <p>
 * Each instance of this exception has a code to indicate type of exception and possible reason why the exception
 * occurs.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2015-06-30 14:52:47
 * @since 0.0.0
 */
public class QuickReflectionException extends CommonRuntimeException {

    /**
     * <P>
     * Exception code: reflected member is not found.
     * </p>
     * 
     * @since 0.0.0
     */
    public final static int CODE_NOT_FOUND = 0;

    /**
     * <P>
     * Exception code: access security problem.
     * </p>
     * 
     * @since 0.0.0
     */
    public final static int CODE_ACCESS_SECURITY = 1;

    /**
     * <P>
     * Exception code: other problem.
     * </p>
     * 
     * @since 0.0.0
     */
    public final static int CODE_OTHER = Integer.MAX_VALUE;

    /**
     * Serial UID.
     * 
     * @since 0.0.0
     */
    private static final long serialVersionUID = 1L;

    private int code;

    /**
     * <p>
     * Constructs with given cause.
     * </p>
     *
     * @param cause
     *            given cause
     * @since 0.0.0
     */
    public QuickReflectionException(final Throwable cause) {
        this(null, cause, assignCode(cause));
    }

    /**
     * <p>
     * Constructs with given detail message.
     * </p>
     *
     * @param message
     *            given detail message
     * @since 0.0.0
     */
    public QuickReflectionException(final String message) {
        this(message, null, assignCode(null));
    }

    /**
     * <p>
     * Constructs with given detail message and given cause.
     * </p>
     *
     * @param message
     *            given detail message
     * @param cause
     *            given cause
     * @since 0.0.0
     */
    public QuickReflectionException(final String message, final Throwable cause) {
        this(message, cause, assignCode(cause));
    }

    /**
     * <p>
     * Constructs with given cause and exception code.
     * </p>
     *
     * @param cause
     *            given cause
     * @param code
     *            exception code
     * @since 0.0.0
     */
    public QuickReflectionException(final Throwable cause, int code) {
        this(null, cause, code);
    }

    /**
     * <p>
     * Constructs with given detail message and exception code.
     * </p>
     *
     * @param message
     *            given detail message
     * @param code
     *            exception code
     * @since 0.0.0
     */
    public QuickReflectionException(final String message, int code) {
        this(message, null, code);
    }

    /**
     * <p>
     * Constructs with given detail message, given cause and exception code.
     * </p>
     *
     * @param message
     *            given detail message
     * @param cause
     *            given cause
     * @param code
     *            exception code
     * @since 0.0.0
     */
    public QuickReflectionException(final String message, final Throwable cause, int code) {
        super(message, cause);
        setCode(code);
    }

    private void setCode(int code) {
        this.code = code;
    }

    private static int assignCode(@Nullable Throwable cause) {
        if (cause == null) {
            return CODE_OTHER;
        } else if (cause instanceof NoSuchMethodException || cause instanceof NoSuchFieldException
                || cause instanceof ClassNotFoundException) {
            return CODE_NOT_FOUND;
        } else if (cause instanceof SecurityException || cause instanceof IllegalAccessException) {
            return CODE_ACCESS_SECURITY;
        } else {
            return CODE_OTHER;
        }
    }

    /**
     * <p>
     * Returns exception code.
     * </p>
     * 
     * @return exception code
     * @since 0.0.0
     */
    public int getCode() {
        return this.code;
    }

    /**
     * <p>
     * Returns whether this exception occurs because reflected member is not found.
     * </p>
     * 
     * @return whether this exception occurs because reflected member is not found
     * @since 0.0.0
     */
    public boolean isNotFound() {
        return getCode() == CODE_NOT_FOUND;
    }

    /**
     * <p>
     * Returns whether this exception occurs because of access security problem.
     * </p>
     * 
     * @return whether this exception occurs because of access security problem
     * @since 0.0.0
     */
    public boolean isSecurityProblem() {
        return getCode() == CODE_ACCESS_SECURITY;
    }
}
