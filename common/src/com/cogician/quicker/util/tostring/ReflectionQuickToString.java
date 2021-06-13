package com.cogician.quicker.util.tostring;

import java.lang.reflect.Field;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

import javax.annotation.Nullable;

/**
 * <p>
 * This class uses reflection to build string for specified object. It follows the style like:
 * 
 * <pre>
 * class signature{filed1=value1,file2=value2}
 * </pre>
 * 
 * Class signature is a signature to describe specified class, commonly be the class name; the "{" and "}" are prefix
 * and suffix; "=" is filed value indicator; "," is filed separator. They are not constant but can be configured by its
 * builder. The reflected fields may include parent classes of its inherited tree up to a specifiable class.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-11-11T09:26:43+08:00
 * @since 0.0.0, 2016-11-11T09:26:43+08:00
 */
public interface ReflectionQuickToString extends QuickToString {

    /**
     * <p>
     * Returns signature of specified object. May be null if just uses class name.
     * </p>
     * 
     * @return function to get signature of specified object
     * @since 0.0.0
     */
    public @Nullable Function<Object, String> getSignature();

    /**
     * <p>
     * Returns prefix of reflected fields string.
     * </p>
     * 
     * @return prefix of reflected fields string
     * @since 0.0.0
     */
    public String getPrefix();

    /**
     * <p>
     * Returns suffix of reflected fields string.
     * </p>
     * 
     * @return suffix of reflected fields string
     * @since 0.0.0
     */
    public String getSuffix();

    /**
     * <p>
     * Returns field value indicator of reflected fields string.
     * </p>
     * 
     * @return field value indicator of reflected fields string
     * @since 0.0.0
     */
    public String getIndicator();

    /**
     * <p>
     * Returns field separator of reflected fields string.
     * </p>
     * 
     * @return field separator of reflected fields string
     * @since 0.0.0
     */
    public String getSeparator();

    /**
     * <p>
     * Returns last and inclusive reflected parent class which this toString reach to.
     * </p>
     * 
     * @return last and inclusive reflected parent class which this toString reach to
     * @since 0.0.0
     */
    public Class<?> getUpTo();

    /**
     * <p>
     * Returns inclusive filed names which will be reflected to string. return null if there is no specified and all
     * fields will be reflected.
     * </p>
     * 
     * @return inclusive filed names which will be reflected to String
     * @since 0.0.0
     */
    public @Nullable Set<String> getInclusiveField();

    /**
     * <p>
     * Returns exclusive filed names which will not be reflected to string. return null if there is no specified.
     * </p>
     * 
     * @return exclusive filed names which will not be reflected to String
     * @since 0.0.0
     */
    public @Nullable Set<String> getExclusiveField();

    /**
     * <p>
     * Returns whether a field will be reflected to string by this toString if it doesn't override toString method upon
     * Object class.
     * </p>
     * 
     * @return whether a field will be reflected to string by this toString if it doesn't override toString method upon
     *         Object class
     * @since 0.0.0
     */
    public boolean isDeep();

    /**
     * <p>
     * Returns special toString tool to build string for each reflected field. Return null if there exists not.
     * </p>
     * 
     * @return special toString tool to build string for each reflected field
     * @since 0.0.0
     */
    public @Nullable BiFunction<Field, Object, String> getSpecial();
}
