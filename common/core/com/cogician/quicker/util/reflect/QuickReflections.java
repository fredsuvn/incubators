/**
 * 
 */
package com.cogician.quicker.util.reflect;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.annotation.Nullable;

import com.cogician.quicker.Checker;
import com.cogician.quicker.Quicker;
import com.cogician.quicker.util.Consts;
import com.cogician.quicker.util.QuickArrays;

/**
 * <p>
 * Static quick utility class provides simple methods for common reflection.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-01-02 23:26:11
 * @since 0.0.0
 */
public class QuickReflections {

    private static final Class<?>[] nonnull(@Nullable Class<?>[] parameterTypes) {
        return parameterTypes == null ? Consts.emptyClassArray() : parameterTypes;
    }

    /**
     * <p>
     * Sets the accessible flag for this object to <b>true</b> and returns.
     * </p>
     * 
     * @param t
     *            given accessible object
     * @return given accessible object
     * @throws NullPointerException
     *             if given accessible object is null
     * @since 0.0.0
     */
    public static <T extends AccessibleObject> T accessible(T t) throws NullPointerException {
        if (!Quicker.require(t).isAccessible()) {
            t.setAccessible(true);
        }
        return t;
    }

    /**
     * <p>
     * Returns class inheritance tree of specified class up to specified last class. If specified last class is null, it
     * will up to Object class.
     * </p>
     * 
     * @param cls
     *            specified class
     * @param upTo
     *            specified last class
     * @return class inheritance tree of specified class up to specified last class
     * @since 0.0.0
     */
    public static Class<?>[] getClassInheritanceTree(Class<?> cls, @Nullable Class<?> upTo) {
        if (upTo != null) {
            if (!upTo.isAssignableFrom(cls)) {
                throw new IllegalArgumentException("upto class should same or super class of specified class.");
            }
            if (upTo.isPrimitive()) {
                throw new IllegalArgumentException("upto class should not be primitive.");
            }
        }
        return Quicker.flow(new SuperclassIterator(cls, upTo)).toArray(new Class<?>[0]);
    }

    /**
     * <p>
     * Returns class inheritance tree of specified class up to Object class.
     * </p>
     * 
     * @param cls
     *            specified class
     * @return class inheritance tree of specified class up to Object class
     * @since 0.0.0
     */
    public static Class<?>[] getClassInheritanceTree(Class<?> cls) {
        return getClassInheritanceTree(cls, null);
    }

    /**
     * <p>
     * Get all members of given class in constructors, fields, methods order.
     * </p>
     * 
     * @param cls
     *            given class
     * @return all members of given class
     * @since 0.0.0
     */
    public static Member[] getMembers(Class<?> cls) {
        Constructor<?>[] cs = getConstructors(cls);
        Field[] fs = getFields(cls);
        Method[] ms = getMethods(cls);
        Member[] result = new Member[(int)QuickArrays.sumLength(cs, fs, ms)];
        return result;
    }

    /**
     * <p>
     * Get all fields of given class.
     * </p>
     * 
     * @param cls
     *            given class
     * @return all fields of given class
     * @since 0.0.0
     */
    public static Field[] getFields(Class<?> cls) {
        return QuickArrays.distinct(QuickArrays.concat(cls.getFields(), cls.getDeclaredFields()));
    }

    /**
     * <p>
     * Get all methods of given class.
     * </p>
     * 
     * @param cls
     *            given class
     * @return all methods of given class
     * @since 0.0.0
     */
    public static Method[] getMethods(Class<?> cls) {
        return QuickArrays.distinct(QuickArrays.concat(cls.getMethods(), cls.getDeclaredMethods()));
    }

    /**
     * <p>
     * Get all constructors of given class.
     * </p>
     * 
     * @param cls
     *            given class
     * @return all constructors of given class
     * @since 0.0.0
     */
    public static @Nullable Constructor<?>[] getConstructors(Class<?> cls) {
        return QuickArrays.distinct(QuickArrays.concat(cls.getConstructors(), cls.getDeclaredConstructors()));
    }

    /**
     * <p>
     * Gets field of given class by specified field name. Return null if not found.
     * </p>
     * 
     * @param cls
     *            given class
     * @param name
     *            specified field name
     * @return field or null if not found
     * @since 0.0.0
     */
    public static @Nullable Field getField(Class<?> cls, String name) {
        Field f = null;
        try {
            f = cls.getField(name);
        } catch (NoSuchFieldException e0) {
            try {
                f = cls.getDeclaredField(name);
            } catch (NoSuchFieldException e1) {
                return null;
            }
        }
        return f;
    }

    /**
     * <p>
     * Returns getter method (getXxx()) of specified field name. Return null if not found.
     * </p>
     * 
     * @param cls
     *            given class
     * @param name
     *            given field name, not empty
     * @return getter method or null if not found
     * @since 0.0.0
     */
    public static @Nullable Method getter(Class<?> cls, String name) {
        return getter(cls, name, false);
    }

    /**
     * <p>
     * Returns getter method (getXxx() or isXxx()) of specified field name. Return null if not found.
     * </p>
     * 
     * @param cls
     *            given class
     * @param name
     *            given field name, not empty
     * @param supportIs
     *            true to support isXxx(), default only getXxx()
     * @return getter method or null if not found
     * @since 0.0.0
     */
    public static @Nullable Method getter(Class<?> cls, String name, boolean supportIs) {
        Method m = getMethod(cls, "get" + Quicker.capitalize(name), Consts.emptyClassArray());
        if (m == null) {
            return getMethod(cls, "is" + Quicker.capitalize(name), Consts.emptyClassArray());
        }
        return m;
    }

    /**
     * <p>
     * Returns setter method (setXxx()) of given class, field name and type of target to be set.
     * </p>
     * 
     * @param cls
     *            given class
     * @param name
     *            given field name, not empty
     * @param type
     *            type of target to be set
     * @return setter method
     * @since 0.0.0
     */
    public static @Nullable Method setter(Class<?> cls, String name, Class<?> type) {
        return getMethod(cls, "set" + Quicker.capitalize(name), type);
    }

    /**
     * <p>
     * Returns value of specified filed regardless of its access control.
     * </p>
     * 
     * @param inst
     *            object of specified filed
     * @param name
     *            specified field name, not empty
     * @return value of specified field
     * @throws QuickReflectionException
     *             if not found or other reflection problem occurs
     * @since 0.0.0
     */
    public static @Nullable Object getValue(Object inst, String name) throws QuickReflectionException {
        return getValue(inst.getClass(), inst, name, false);
    }

    /**
     * <p>
     * Returns value of specified filed regardless of its access control.
     * </p>
     * 
     * @param cls
     *            class of specified filed
     * @param name
     *            specified field name, not empty
     * @return value of specified field
     * @throws QuickReflectionException
     *             if not found or other reflection problem occurs
     * @since 0.0.0
     */
    public static @Nullable Object getValue(Class<?> cls, String name) throws QuickReflectionException {
        return getValue(cls, null, name, false);
    }

    /**
     * <p>
     * Returns value of specified filed regardless of its access control. If {@code supportGetter} specifies true, then
     * if the field is not found, it will invoke its getter method.
     * </p>
     * 
     * @param cls
     *            class of specified filed
     * @param inst
     *            object of specified filed
     * @param name
     *            specified field name, not empty
     * @param supportGetter
     *            true to support invoking getter method if not found
     * @return value of specified field
     * @throws QuickReflectionException
     *             if not found or other reflection problem occurs
     * @since 0.0.0
     */
    public static @Nullable Object getValue(Class<?> cls, @Nullable Object inst, String name, boolean supportGetter)
            throws QuickReflectionException {
        Field f = getField(cls, name);
        if (f == null) {
            if (!supportGetter) {
                throw new QuickReflectionException(new NoSuchFieldException(name));
            }
            Method m = getter(cls, name, true);
            if (m == null) {
                throw new QuickReflectionException(new NoSuchFieldException(name));
            }
            return null;
        } else {
            try {
                return accessible(f).get(inst);
            } catch (Exception e) {
                throw new QuickReflectionException(e);
            }
        }
    }

    /**
     * <p>
     * Gets value of specified field with specified object.
     * </p>
     * 
     * @param field
     *            specified field
     * @param obj
     *            specified object
     * @return value of specified field with specified object
     * @throws QuickReflectionException
     *             if any reflection problem occurs
     * @since 0.0.0
     */
    public static @Nullable Object getValue(Field field, Object obj) throws QuickReflectionException {
        try {
            return accessible(field).get(obj);
        } catch (Exception e) {
            throw new QuickReflectionException(e);
        }
    }

    /**
     * <p>
     * sets value of specified filed regardless of its access control.
     * </p>
     * 
     * @param inst
     *            object of specified filed
     * @param name
     *            specified field name, not empty
     * @param value
     *            value to be set
     * @throws QuickReflectionException
     *             if not found or other reflection problem occurs
     * @since 0.0.0
     */
    public static void setValue(Object inst, String name, @Nullable Object value) throws QuickReflectionException {
        setValue(inst.getClass(), inst, name, value, false);
    }

    /**
     * <p>
     * Sets value of specified filed regardless of its access control.
     * </p>
     * 
     * @param cls
     *            class of specified filed
     * @param name
     *            specified field name, not empty
     * @param value
     *            value to be set
     * @throws QuickReflectionException
     *             if not found or other reflection problem occurs
     * @since 0.0.0
     */
    public static void setValue(Class<?> cls, String name, @Nullable Object value) throws QuickReflectionException {
        setValue(cls, null, name, value, false);
    }

    /**
     * <p>
     * Sets value of specified filed regardless of its access control. If {@code supportGetter} specifies true, then if
     * the field is not found, it will invoke its setter method.
     * </p>
     * 
     * @param cls
     *            class of specified filed
     * @param inst
     *            object of specified filed
     * @param name
     *            specified field name, not empty
     * @param value
     *            value to be set
     * @param supportGetter
     *            true to support invoking getter method if not found
     * @throws QuickReflectionException
     *             if not found or other reflection problem occurs
     * @since 0.0.0
     */
    public static void setValue(Class<?> cls, @Nullable Object inst, String name, @Nullable Object value,
            boolean supportGetter) throws QuickReflectionException {
        Field f = getField(cls, name);
        if (f == null) {
            if (!supportGetter) {
                throw new QuickReflectionException(new NoSuchFieldException(name));
            }
            Method m = getter(cls, name, true);
            if (m == null) {
                throw new QuickReflectionException(new NoSuchFieldException(name));
            }
            // return null;
        } else {
            try {
                accessible(f).set(inst, value);
            } catch (Exception e) {
                throw new QuickReflectionException(e);
            }
        }
    }

    /**
     * <p>
     * Sets specified value into specified field with specified object.
     * </p>
     * 
     * @param field
     *            specified field
     * @param obj
     *            specified object
     * @param value
     *            specified value
     * @return value of specified field with specified object
     * @throws QuickReflectionException
     *             if any reflection problem occurs
     * @since 0.0.0
     */
    public static void setValue(Field field, Object obj, Object value) throws QuickReflectionException {
        try {
            accessible(field).set(obj, value);
        } catch (Exception e) {
            throw new QuickReflectionException(e);
        }
    }

    /**
     * <p>
     * Gets method of given class by specified method name and parameter types. Return null if not found.
     * </p>
     * 
     * @param cls
     *            given class
     * @param name
     *            specified method name
     * @param parameterTypes
     *            specified parameter types of method
     * @return method or null if not found
     * @since 0.0.0
     */
    public static @Nullable Method getMethod(Class<?> cls, String name, @Nullable Class<?>... parameterTypes) {
        Method m = null;
        parameterTypes = nonnull(parameterTypes);
        try {
            m = cls.getMethod(name, parameterTypes);
        } catch (NoSuchMethodException e0) {
            try {
                m = cls.getDeclaredMethod(name, parameterTypes);
            } catch (NoSuchMethodException e1) {
                return null;
            }
        }
        return m;
    }

    /**
     * <p>
     * Searches method from given class with specified method name and runtime argument objects. Return null if method
     * not matched.
     * </p>
     * 
     * @param cls
     *            given class
     * @param name
     *            specified method name
     * @param args
     *            given runtime argument objects
     * @return method or null if not matched
     * @since 0.0.0
     */
    public static @Nullable Method searchMethod(Class<?> cls, String name, @Nullable Object... args) {
        if (Checker.isEmpty(args)) {
            return getMethod(cls, name, Consts.emptyClassArray());
        }
        if (!Checker.hasNullElement(args)) {
            return getMethod(cls, name, QuickArrays.convert(args, Class.class, o -> o.getClass()));
        }
        Class<?>[] parameterTypes = QuickArrays.convert(args, Class.class, o -> o == null ? null : o.getClass());
        Method[] ms = getMethods(cls);
        Method result = null;
        int similarity = 0;
        for (int i = 0; i < ms.length; i++) {
            if (ms[i].getParameterCount() == parameterTypes.length) {
                if (result == null) {
                    result = ms[i];
                    similarity = QuickArrays.similarity(result.getParameterTypes(), parameterTypes);
                } else {
                    int newSimilarity = QuickArrays.similarity(ms[i].getParameterTypes(), parameterTypes);
                    if (newSimilarity > similarity) {
                        result = ms[i];
                    }
                }
            }
        }
        return result;
    }

    /**
     * <p>
     * Invokes given method regardless of its access control.
     * </p>
     * 
     * @param method
     *            given method
     * @param inst
     *            instance of class where given method belong, null if method is static
     * @param args
     *            arguments of method
     * @return result of invoking
     * @throws QuickReflectionException
     *             if any problem occurs
     * @since 0.0.0
     */
    public static Object invoke(Method method, @Nullable Object inst, @Nullable Object... args)
            throws QuickReflectionException {
        try {
            return accessible(method).invoke(inst, args);
        } catch (Throwable e) {
            throw new QuickReflectionException(e);
        }
    }

    /**
     * <p>
     * Invokes method with specified class, instance, method name, parameter types and arguments, regardless of its
     * access control.
     * </p>
     * 
     * @param cls
     *            specified class
     * @param inst
     *            specified instance, maybe null if the method is static
     * @param name
     *            given method name
     * @param parameterTypes
     *            given parameter types
     * @param args
     *            given runtime arguments
     * @return result of invoke
     * @throws QuickReflectionException
     *             if not found or other reflection problem occurs
     * @since 0.0.0
     */
    public static @Nullable Object invoke(Class<?> cls, @Nullable Object inst, String name,
            @Nullable Class<?>[] parameterTypes, @Nullable Object[] args) throws QuickReflectionException {
        Method m = getMethod(cls, name, parameterTypes);
        if (m == null) {
            throw new QuickReflectionException(new NoSuchMethodException(buildMethodString(cls, name, parameterTypes)));
        }
        return invoke(cls, name, args);
    }

    /**
     * <p>
     * Invokes method with specified class, instance, method name and arguments, regardless of its access control.
     * </p>
     * 
     * @param cls
     *            given class
     * @param inst
     *            specified instance, maybe null if the method is static
     * @param name
     *            given method name
     * @param args
     *            given runtime arguments
     * @return result of invoke
     * @throws QuickReflectionException
     *             if not found or other reflection problem occurs
     * @since 0.0.0
     */
    public static @Nullable Object invoke(Class<?> cls, @Nullable Object inst, String name, @Nullable Object[] args)
            throws QuickReflectionException {
        Method m = searchMethod(cls, name, args);
        if (m == null) {
            throw new QuickReflectionException(new NoSuchMethodException(buildMethodString(cls, name,
                    QuickArrays.convert(args, Class.class, o -> o == null ? null : o.getClass()))));
        }
        return invoke(cls, name, args);
    }

    private static String buildMethodString(Class<?> cls, String name, @Nullable Class<?>[] parameterTypes) {
        return cls.getName() + "." + name + "(" + QuickArrays.join(nonnull(parameterTypes)) + ")";
    }

    /**
     * <p>
     * Invokes method with specified instance, method name and arguments, regardless of its access control.
     * </p>
     * 
     * @param inst
     *            specified instance
     * @param name
     *            given method name
     * @param args
     *            given runtime arguments
     * @return result of invoke
     * @throws QuickReflectionException
     *             if not found or other reflection problem occurs
     * @since 0.0.0
     */
    public static Object invoke(Object inst, String name, @Nullable Object... args) throws QuickReflectionException {
        return invoke(inst.getClass(), inst, name, args);
    }

    /**
     * <p>
     * Invokes method with specified class, method name and arguments, regardless of its access control.
     * </p>
     * 
     * @param cls
     *            specified class
     * @param name
     *            given method name
     * @param args
     *            given runtime arguments
     * @return result of invoke
     * @throws QuickReflectionException
     *             if not found or other reflection problem occurs
     * @since 0.0.0
     */
    public static Object invoke(Class<?> cls, String name, @Nullable Object... args) throws QuickReflectionException {
        return invoke(cls, null, name, args);
    }

    /**
     * <p>
     * Gets constructor of given class by specified parameter types. Return null if not found.
     * </p>
     * 
     * @param cls
     *            given class
     * @param parameterTypess
     *            specified parameter types of method
     * @return constructor or null if not found
     * @since 0.0.0
     */
    public static @Nullable Constructor<?> getConstructor(Class<?> cls, @Nullable Class<?>... parameterTypes) {
        Constructor<?> c = null;
        try {
            c = cls.getConstructor(parameterTypes);
        } catch (NoSuchMethodException e0) {
            try {
                c = cls.getDeclaredConstructor(parameterTypes);
            } catch (NoSuchMethodException e1) {
                return null;
            }
        }
        return c;
    }

    /**
     * <p>
     * Gets class of specified name. Return null if not found.
     * </p>
     * 
     * @param name
     *            specified class name
     * @return class of specified name
     * @since 0.0.0
     */
    public static @Nullable Class<?> getClass(String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            throw new QuickReflectionException(e);
        }
    }

    static class SuperclassIterator implements Iterator<Class<?>> {

        private Class<?> cls;

        @Nullable
        private final Class<?> upTo;

        SuperclassIterator(Class<?> cls, @Nullable Class<?> upTo) {
            this.cls = cls;
            this.upTo = upTo;
        }

        @Override
        public boolean hasNext() {
            return cls != null;
        }

        @Override
        public Class<?> next() {
            if (cls == null) {
                throw new NoSuchElementException();
            }
            Class<?> ret = cls;
            if (cls.equals(upTo) || cls.equals(Object.class)) {
                cls = null;
            } else {
                cls = cls.getSuperclass();
            }
            return ret;
        }
    }

}
