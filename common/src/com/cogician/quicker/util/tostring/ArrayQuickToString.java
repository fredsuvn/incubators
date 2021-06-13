package com.cogician.quicker.util.tostring;

import javax.annotation.Nullable;

/**
 * <p>
 * This class builds string for specified array. It follows the style like:
 * 
 * <pre>
 * [element0,element1]
 * </pre>
 * 
 * The "[" and "]" are prefix and suffix; "," is element separator. They are not constant but can be configured by its
 * builder.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-11-11T09:27:04+08:00
 * @since 0.0.0, 2016-11-11T09:27:04+08:00
 */
public interface ArrayQuickToString extends QuickToString {

    /**
     * <p>
     * Returns prefix of array string.
     * </p>
     * 
     * @return prefix of array string
     * @since 0.0.0
     */
    public String getPrefix();

    /**
     * <p>
     * Returns suffix of array string.
     * </p>
     * 
     * @return suffix of array string
     * @since 0.0.0
     */
    public String getSuffix();

    /**
     * <p>
     * Returns element separator of fields string.
     * </p>
     * 
     * @return element separator of fields string
     * @since 0.0.0
     */
    public String getSeparator();

    /**
     * <p>
     * Returns whether an element will be toString by this toString if it is an array.
     * </p>
     * 
     * @return whether an element will be toString by this toString if it is an array
     * @since 0.0.0
     */
    public boolean isDeep();

    /**
     * <p>
     * Returns special toString tool to build string for each element. Return null if there exists not.
     * </p>
     * 
     * @return special toString tool to build string for each element
     * @since 0.0.0
     */
    public @Nullable QuickToString getSpecial();
}
