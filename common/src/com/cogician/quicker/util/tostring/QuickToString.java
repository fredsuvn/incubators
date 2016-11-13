package com.cogician.quicker.util.tostring;

/**
 * <p>
 * This class is used to build a string to represents specified object, like "toString" method.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-11-11T09:13:06+08:00
 * @since 0.0.0, 2016-11-11T09:13:06+08:00
 */
public interface QuickToString {

    /**
     * <p>
     * Builds a string to represents specified object
     * </p>
     * 
     * @param obj
     *            specified object
     * @return a string to represents specified object
     * @since 0.0.0
     */
    public String toString(Object obj);
}
