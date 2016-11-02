/**
 * 
 */
package com.cogician.quicker.util.reflect;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;

import javax.annotation.Nullable;

import com.cogician.quicker.Checker;
import com.cogician.quicker.Quicker;
import com.cogician.quicker.struct.BoxOf;
import com.cogician.quicker.util.QuiCollections;
import com.cogician.quicker.util.Consts;
import com.cogician.quicker.util.ToStringQuicker;

/**
 * <p>
 * Static quick utility class provides simple methods for common reflection.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-01-02 23:26:11
 * @since 0.0.0
 */
public class ReflectionQuicker {

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
     * Returns an iterator of class inheritance tree of given class from given class to given up-to class, in
     * sub-to-upto order. If up-to class is null, it will be considered as up to {@linkplain Object} class.
     * </p>
     * 
     * @param cls
     *            given class
     * @param upTo
     *            given up-to class
     * @return an iterator of class inheritance tree of given class
     * @throws NullPointerException
     *             if given class is null
     * @since 0.0.0
     */
    public static Iterator<Class<?>> iteratorOfClassInheritance(Class<?> cls, @Nullable Class<?> upTo)
            throws NullPointerException {
        return new SuperclassIterator(Quicker.require(cls), upTo);
    }

    /**
     * <p>
     * Returns an iterator of class inheritance tree of given class from given class to {@linkplain Object} class, in
     * sub-to-Object order.
     * </p>
     * 
     * @param cls
     *            given class
     * @return an iterator of class inheritance tree of given class
     * @throws NullPointerException
     *             if given class is null
     * @since 0.0.0
     */
    public static Iterator<Class<?>> iteratorOfClassInheritance(Class<?> cls) throws NullPointerException {
        return iteratorOfClassInheritance(cls, null);
    }

    /**
     * <p>
     * Returns an iterator of interface inheritance tree of given class from given class to last root interface, in
     * sub-to-root order.
     * </p>
     * 
     * @param cls
     *            given class
     * @return an iterator of interface inheritance tree of given class
     * @throws NullPointerException
     *             if given class is null
     * @since 0.0.0
     */
    public static Iterator<Class<?>> iteratorOfInterfaceInheritance(Class<?> cls) throws NullPointerException {
        return new InterfaceIterator(cls);
    }

    /**
     * <p>
     * Returns an iterator which consists of all elements of result of given action from given class and its
     * superclasses up to given up-to class, in sub-to-upTo order. If up-to class is null, it will be considered as up
     * to {@linkplain Object} class.
     * </p>
     * 
     * @param cls
     *            given class
     * @param action
     *            given action
     * @param upTo
     *            given up-to class
     * @return an iterator which consists of all elements of result of given action
     * @throws NullPointerException
     *             if given class or action is null
     * @since 0.0.0
     */
    public static <E> Iterator<E> iteratorOf(Class<?> cls, Function<Class<?>, E[]> action, @Nullable Class<?> upTo)
            throws NullPointerException {
        return new MemberIterator<E>(Quicker.require(cls), Quicker.require(action), upTo);
    }

    /**
     * <p>
     * Returns an iterator which consists of all elements of result of given action from given class and its
     * superclasses.
     * </p>
     * 
     * @param cls
     *            given class
     * @param action
     *            given action
     * @return an iterator which consists of all elements of result of given action
     * @throws NullPointerException
     *             if given class or action is null
     * @since 0.0.0
     */
    public static <E> Iterator<E> iteratorOf(Class<?> cls, Function<Class<?>, E[]> action) throws NullPointerException {
        return iteratorOf(cls, action, null);
    }

    /**
     * <p>
     * Returns an iterator which consists of all {@linkplain Member}s of given class and its superclasses (for each
     * class includes constructors, fields and methods and in that order) up to given up-to class, in sub-to-upTo order.
     * If up-to class is null, it will be considered as up to {@linkplain Object} class.
     * </p>
     * 
     * @param cls
     *            given class
     * @param upTo
     *            given up-to class
     * @return an iterator which consists of all {@linkplain Member}s
     * @throws NullPointerException
     *             if given class is null
     * @since 0.0.0
     */
    public static Iterator<Member> iteratorOfMembers(Class<?> cls, Class<?> upTo) throws NullPointerException {
        return iteratorOf(Quicker.require(cls), c -> {
            Constructor<?>[] cs = c.getDeclaredConstructors();
            Field[] fs = c.getDeclaredFields();
            Method[] ms = c.getDeclaredMethods();
            return QuiCollections.concat(Member.class, cs, fs, ms);
        }, upTo);
    }

    /**
     * <p>
     * Returns an iterator which consists of all {@linkplain Member}s of given class and its superclasses (for each
     * class includes constructors, fields and methods and in that order).
     * </p>
     * 
     * @param cls
     *            given class
     * @return an iterator which consists of all {@linkplain Member}s
     * @throws NullPointerException
     *             if given class is null
     * @since 0.0.0
     */
    public static Iterator<Member> iteratorOfMembers(Class<?> cls) throws NullPointerException {
        return iteratorOfMembers(cls, null);
    }

    /**
     * <p>
     * Returns an iterator which consists of all fields of given class and its superclasses up to given up-to class, in
     * sub-to-upTo order. If up-to class is null, it will be considiered as up to {@linkplain Object} class.
     * </p>
     * 
     * @param cls
     *            given class
     * @param upTo
     *            given up-to class
     * @return an iterator which consists of all fields
     * @throws NullPointerException
     *             if given class is null
     * @since 0.0.0
     */
    public static Iterator<Field> iteratorOfFields(Class<?> cls, Class<?> upTo) throws NullPointerException {
        return iteratorOf(Quicker.require(cls), c -> c.getDeclaredFields(), upTo);
    }

    /**
     * <p>
     * Returns an iterator which consists of all fields of given class and its superclasses.
     * </p>
     * 
     * @param cls
     *            given class
     * @return an iterator which consists of all fields
     * @throws NullPointerException
     *             if given class is null
     * @since 0.0.0
     */
    public static Iterator<Field> iteratorOfFields(Class<?> cls) throws NullPointerException {
        return iteratorOfFields(cls, null);
    }

    /**
     * <p>
     * Returns an iterator which consists of all method of given class and its superclasses up to given up-to class, in
     * sub-to-upTo order. If up-to class is null, it will be considiered as up to {@linkplain Object} class.
     * </p>
     * 
     * @param cls
     *            given class
     * @param upTo
     *            given up-to class
     * @return an iterator which consists of all method
     * @throws NullPointerException
     *             if given class is null
     * @since 0.0.0
     */
    public static Iterator<Method> iteratorOfMethods(Class<?> cls, Class<?> upTo) throws NullPointerException {
        return iteratorOf(Quicker.require(cls), c -> c.getDeclaredMethods(), upTo);
    }

    /**
     * <p>
     * Returns an iterator which consists of all method of given class and its superclasses.
     * </p>
     * 
     * @param cls
     *            given class
     * @return an iterator which consists of all method
     * @throws NullPointerException
     *             if given class is null
     * @since 0.0.0
     */
    public static Iterator<Method> iteratorOfMethods(Class<?> cls) throws NullPointerException {
        return iteratorOfMethods(cls, null);
    }

    /**
     * <p>
     * Returns an iterator which consists of all constructors of given class and its superclasses up to given up-to
     * class, in sub-to-upTo order. If up-to class is null, it will be considiered as up to {@linkplain Object} class.
     * </p>
     * 
     * @param cls
     *            given class
     * @param upTo
     *            given up-to class
     * @return an iterator which consists of all constructors
     * @throws NullPointerException
     *             if given class is null
     * @since 0.0.0
     */
    public static Iterator<Constructor<?>> iteratorOfConstructors(Class<?> cls, Class<?> upTo)
            throws NullPointerException {
        return iteratorOf(Quicker.require(cls), c -> c.getDeclaredConstructors(), upTo);
    }

    /**
     * <p>
     * Returns an iterator which consists of all constructors of given class and its superclasses.
     * </p>
     * 
     * @param cls
     *            given class
     * @return an iterator which consists of all constructors
     * @throws NullPointerException
     *             if given class is null
     * @since 0.0.0
     */
    public static Iterator<Constructor<?>> iteratorOfConstructors(Class<?> cls) throws NullPointerException {
        return iteratorOfConstructors(cls, null);
    }

    /**
     * <p>
     * Returns an iterator which consists of all inner classes of given class and its superclasses up to given up-to
     * class, in sub-to-upTo order. If up-to class is null, it will be considiered as up to {@linkplain Object} class.
     * </p>
     * 
     * @param cls
     *            given class
     * @param upTo
     *            given up-to class
     * @return an iterator which consists of all inner classes
     * @throws NullPointerException
     *             if given class is null
     * @since 0.0.0
     */
    public static Iterator<Class<?>> iteratorOfInnerClasses(Class<?> cls, Class<?> upTo) throws NullPointerException {
        return iteratorOf(Quicker.require(cls), c -> c.getDeclaredClasses(), upTo);
    }

    /**
     * <p>
     * Returns an iterator which consists of all inner classes of given class and its superclasses.
     * </p>
     * 
     * @param cls
     *            given class
     * @return an iterator which consists of all inner classes
     * @throws NullPointerException
     *             if given class is null
     * @since 0.0.0
     */
    public static Iterator<Class<?>> iteratorOfInnerClasses(Class<?> cls) throws NullPointerException {
        return iteratorOfInnerClasses(cls, null);
    }

    /**
     * <p>
     * Get public or declared methods from given class.
     * </p>
     * 
     * @param cls
     *            given class
     * @return public or declared methods of given class
     * @throws NullPointerException
     *             if given class is null
     * @throws ReflectionException
     *             if any problem occurs
     * @since 0.0.0
     */
    public static @Nullable Method[] getMethods(Class<?> cls) throws NullPointerException, ReflectionException {
        return QuiCollections.concatDistinct(Method.class, Quicker.require(cls).getMethods(),
                cls.getDeclaredMethods());
    }

    /**
     * <p>
     * Using {@linkplain Class#getMethod(String, Class...)} to get method from given class. Return null if method not
     * found.
     * </p>
     * 
     * @param cls
     *            given class
     * @param name
     *            method name
     * @param parameterTypes
     *            parameter types of method, null or empty if no parameter
     * @return method of given class or null if not found
     * @throws NullPointerException
     *             if given class or name is null
     * @throws ReflectionException
     *             if any problem occurs
     * @since 0.0.0
     */
    public static @Nullable Method getPublicMethod(Class<?> cls, String name, @Nullable Class<?>[] parameterTypes)
            throws NullPointerException, ReflectionException {
        try {
            return Quicker.require(cls).getMethod(Quicker.require(name), parameterTypes);
        } catch (NoSuchMethodException e) {
            return null;
        } catch (SecurityException e) {
            throw new ReflectionException(e);
        }
    }

    /**
     * <p>
     * Using {@linkplain Class#getDeclaredMethod(String, Class...)} to get method from given class. Return null if
     * method not found.
     * </p>
     * 
     * @param cls
     *            given class
     * @param name
     *            method name
     * @param parameterTypes
     *            parameter types of method, null or empty if no parameter
     * @return method of given class or null if not found
     * @throws NullPointerException
     *             if given class or name is null
     * @throws ReflectionException
     *             if any problem occurs
     * @since 0.0.0
     */
    public static @Nullable Method getDeclaredMethod(Class<?> cls, String name, @Nullable Class<?>[] parameterTypes)
            throws NullPointerException, ReflectionException {
        try {
            return Quicker.require(cls).getDeclaredMethod(Quicker.require(name), parameterTypes);
        } catch (NoSuchMethodException e) {
            return null;
        } catch (SecurityException e) {
            throw new ReflectionException(e);
        }

    }

    /**
     * <p>
     * Get public or declared method from given class. Return null if method not found.
     * </p>
     * 
     * @param cls
     *            given class
     * @param name
     *            method name
     * @param parameterTypes
     *            parameter types of method, null or empty if no parameter
     * @return method of given class or null if not found
     * @throws NullPointerException
     *             if given class or name is null
     * @throws ReflectionException
     *             if any problem occurs
     * @since 0.0.0
     */
    public static @Nullable Method getMethod(Class<?> cls, String name, @Nullable Class<?>[] parameterTypes)
            throws NullPointerException, ReflectionException {
        Method m = getPublicMethod(cls, name, parameterTypes);
        return m == null ? getDeclaredMethod(cls, name, parameterTypes) : m;
    }

    /**
     * <p>
     * Searches method from given class. This method will speculate and contrast parameter types according to given
     * runtime argument objects to return most confirm method. Return null if method not matched.
     * </p>
     * 
     * @param cls
     *            given class
     * @param name
     *            method name
     * @param args
     *            given runtime argument objects
     * @return method of given class or null if not matched
     * @throws NullPointerException
     *             if given class or name is null
     * @throws ReflectionException
     *             if any problem occurs
     * @since 0.0.0
     */
    public static @Nullable Method searchMethod(Class<?> cls, String name, @Nullable Object[] args)
            throws NullPointerException, ReflectionException {
        if (args == null || args.length == 0) {
            return searchMethod(cls, name, Consts.emptyObjectArray());
        }
        if (!Checker.hasNullElement(args)) {
            return getMethod(cls, name, QuiCollections.convert(args, Class.class, o -> o.getClass()));
        }
        Checker.checkNull(cls);
        Checker.checkNull(name);
        Class<?>[] parameterTypes = QuiCollections.convert(args, Class.class,
                obj -> Quicker.tryRequire(obj, null, o -> o.getClass()));
        BoxOf<Method> ret = new BoxOf<>();
        int[] similarity = {0};
        Quicker.each(getMethods(cls), m -> {
            if (m.getName().equals(name) && m.getParameterTypes().length == args.length) {
                Class<?>[] mTypes = m.getParameterTypes();
                int s = QuiCollections.similarity(parameterTypes, mTypes);
                if (s > similarity[0]) {
                    similarity[0] = s;
                    ret.set(m);
                }
            }
        });
        return ret.get();
    }

    /**
     * <p>
     * Gets public or declared fields from given class.
     * </p>
     * 
     * @param cls
     *            given class
     * @return public or declared fields of given class
     * @throws NullPointerException
     *             if given class is null
     * @throws ReflectionException
     *             if any problem occurs
     * @since 0.0.0
     */
    public static @Nullable Field[] getFields(Class<?> cls) throws NullPointerException, ReflectionException {
        return QuiCollections.concatDistinct(Field.class, Quicker.require(cls).getFields(), cls.getDeclaredFields());
    }

    /**
     * <p>
     * Using {@linkplain Class#getField(String)} to get field from given class. Return null if field not found.
     * </p>
     * 
     * @param cls
     *            given class
     * @param name
     *            field name
     * @return field of given class or null if not found
     * @throws NullPointerException
     *             if given class or name is null
     * @throws ReflectionException
     *             if any problem occurs
     * @since 0.0.0
     */
    public static Field getPublicField(Class<?> cls, String name) throws NullPointerException, ReflectionException {
        try {
            return Quicker.require(cls).getField(Quicker.require(name));
        } catch (NoSuchFieldException e) {
            return null;
        } catch (SecurityException e) {
            throw new ReflectionException(e);
        }
    }

    /**
     * <p>
     * Using {@linkplain Class#getDeclaredField(String)} to get field from given class. Return null if field not found.
     * </p>
     * 
     * @param cls
     *            given class
     * @param name
     *            field name
     * @return field of given class or null if not found
     * @throws NullPointerException
     *             if given class or name is null
     * @throws ReflectionException
     *             if any problem occurs
     * @since 0.0.0
     */
    public static Field getDeclaredField(Class<?> cls, String name) throws NullPointerException, ReflectionException {
        try {
            return Quicker.require(cls).getDeclaredField(Quicker.require(name));
        } catch (NoSuchFieldException e) {
            return null;
        } catch (SecurityException e) {
            throw new ReflectionException(e);
        }
    }

    /**
     * <p>
     * Gets public or declared field from given class. Return null if field not found.
     * </p>
     * 
     * @param cls
     *            given class
     * @param name
     *            field name
     * @return field of given class or null if not found
     * @throws NullPointerException
     *             if given class or name is null
     * @throws ReflectionException
     *             if any problem occurs
     * @since 0.0.0
     */
    public static @Nullable Field getField(Class<?> cls, String name) throws NullPointerException, ReflectionException {
        Field f = getPublicField(cls, name);
        return f == null ? getDeclaredField(cls, name) : f;
    }

    /**
     * <p>
     * Get public or declared constructors from given class.
     * </p>
     * 
     * @param cls
     *            given class
     * @return public or declared constructors of given class
     * @throws NullPointerException
     *             if given class is null
     * @throws ReflectionException
     *             if any problem occurs
     * @since 0.0.0
     */
    public static @Nullable Constructor<?>[] getConstructors(Class<?> cls)
            throws NullPointerException, ReflectionException {
        return QuiCollections.concatDistinct(Constructor.class, Quicker.require(cls).getConstructors(),
                cls.getDeclaredConstructors());
    }

    /**
     * <p>
     * Using {@linkplain Class#getConstructor(Class...)} to get constructor from given class. Return null if constructor
     * not found.
     * </p>
     * 
     * @param cls
     *            given class
     * @param parameterTypes
     *            parameter types of constructor, null or empty if no parameter
     * @return constructor of given class or null if not found
     * @throws NullPointerException
     *             if given class is null
     * @throws ReflectionException
     *             if any problem occurs
     * @since 0.0.0
     */
    public static @Nullable Constructor<?> getPublicConstructor(Class<?> cls, @Nullable Class<?>[] parameterTypes)
            throws NullPointerException, ReflectionException {
        try {
            return Quicker.require(cls).getConstructor(parameterTypes);
        } catch (NoSuchMethodException e) {
            return null;
        } catch (SecurityException e) {
            throw new ReflectionException(e);
        }
    }

    /**
     * <p>
     * Using {@linkplain Class#getDeclaredConstructor(Class...)} to get constructor from given class. Return null if
     * constructor not found.
     * </p>
     * 
     * @param cls
     *            given class
     * @param parameterTypes
     *            parameter types of constructor, null or empty if no parameter
     * @return constructor of given class or null if not found
     * @throws NullPointerException
     *             if given class is null
     * @throws ReflectionException
     *             if any problem occurs
     * @since 0.0.0
     */
    public static @Nullable Constructor<?> getDeclaredConstructor(Class<?> cls, @Nullable Class<?>[] parameterTypes)
            throws NullPointerException, ReflectionException {
        try {
            return Quicker.require(cls).getDeclaredConstructor(parameterTypes);
        } catch (NoSuchMethodException e) {
            return null;
        } catch (SecurityException e) {
            throw new ReflectionException(e);
        }
    }

    /**
     * <p>
     * Get public or declared constructor from given class. Return null if constructor not found.
     * </p>
     * 
     * @param cls
     *            given class
     * @param parameterTypes
     *            parameter types of constructor, null or empty if no parameter
     * @return public or declared constructor of given class
     * @throws NullPointerException
     *             if given class is null
     * @throws ReflectionException
     *             if any problem occurs
     * @since 0.0.0
     */
    public static @Nullable Constructor<?> getConstructor(Class<?> cls, @Nullable Class<?>[] parameterTypes)
            throws NullPointerException, ReflectionException {
        Constructor<?> c = getPublicConstructor(cls, parameterTypes);
        return c == null ? getDeclaredConstructor(cls, parameterTypes) : c;
    }

    /**
     * <p>
     * Searches constructor from given class. This method will speculate and contrast parameter types according to given
     * runtime argument objects to return most confirm constructor. Return null if constructor not matched.
     * </p>
     * 
     * @param cls
     *            given class
     * @param args
     *            given runtime argument objects
     * @return constructor of given class or null if not matched
     * @throws NullPointerException
     *             if given class is null
     * @throws ReflectionException
     *             if any problem occurs
     * @since 0.0.0
     */
    public static @Nullable Constructor<?> searchConstructor(Class<?> cls, @Nullable Object[] args)
            throws NullPointerException, ReflectionException {
        if (args == null || args.length == 0) {
            return searchConstructor(cls, Consts.emptyObjectArray());
        }
        if (!Checker.hasNullElement(args)) {
            return getConstructor(cls, QuiCollections.convert(args, Class.class, o -> o.getClass()));
        }
        Checker.checkNull(cls);
        Class<?>[] parameterTypes = QuiCollections.convert(args, Class.class,
                obj -> Quicker.tryRequire(obj, null, o -> o.getClass()));
        BoxOf<Constructor<?>> ret = new BoxOf<>();
        int[] similarity = {0};
        Quicker.each(getConstructors(cls), c -> {
            if (c.getParameterTypes().length == args.length) {
                Class<?>[] mTypes = c.getParameterTypes();
                int s = QuiCollections.similarity(parameterTypes, mTypes);
                if (s > similarity[0]) {
                    similarity[0] = s;
                    ret.set(c);
                }
            }
        });
        return ret.get();
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
            throw new ReflectionException(e);
        }
    }

    /**
     * <p>
     * Returns getter method (getXxx()) of specified field name.
     * </p>
     * 
     * @param cls
     *            given class
     * @param name
     *            given field name, not empty
     * @return getter method
     * @throws NullPointerException
     *             if given class or name is null
     * @throws IllegalArgumentException
     *             if given name is empty
     * @throws ReflectionException
     *             if any problem occurs
     * @since 0.0.0
     */
    public static @Nullable Method getterOf(Class<?> cls, String name)
            throws NullPointerException, IllegalArgumentException, ReflectionException {
        Checker.checkEmpty(name);
        return getMethod(cls, "get" + Quicker.capitalizeInitials(name), null);
    }

    /**
     * <p>
     * Returns boolean-getter method (isXxx()) of specified field name.
     * </p>
     * 
     * @param cls
     *            given class
     * @param name
     *            given field name, not empty
     * @return boolean-getter method
     * @throws NullPointerException
     *             if given class or name is null
     * @throws IllegalArgumentException
     *             if given name is empty
     * @throws ReflectionException
     *             if any problem occurs
     * @since 0.0.0
     */
    public static @Nullable Method isserOf(Class<?> cls, String name)
            throws NullPointerException, IllegalArgumentException, ReflectionException {
        Checker.checkEmpty(name);
        return getMethod(cls, "is" + Quicker.capitalizeInitials(name), null);
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
     * @throws NullPointerException
     *             if given class, name or target type is null
     * @throws IllegalArgumentException
     *             if given name is empty
     * @throws ReflectionException
     *             if any problem occurs
     * @since 0.0.0
     */
    public static @Nullable Method setterOf(Class<?> cls, String name, Class<?> type)
            throws NullPointerException, IllegalArgumentException, ReflectionException {
        Checker.checkEmpty(name);
        return getMethod(cls, "set" + Quicker.capitalizeInitials(name), new Class<?>[]{Quicker.require(type)});
    }

    /**
     * <p>
     * Gets value of given field. Field will set accessible if it is not accessible.
     * </p>
     * 
     * @param field
     *            given field
     * @param inst
     *            instance of class where given field belong, null if field is static
     * @return value of given field
     * @throws NullPointerException
     *             if given field is null
     * @throws ReflectionException
     *             if any problem occurs
     * @since 0.0.0
     */
    public static Object getFieldValue(Field field, @Nullable Object inst)
            throws NullPointerException, ReflectionException {
        try {
            return accessible(field).get(inst);
        } catch (Throwable e) {
            throw new ReflectionException(e);
        }
    }

    /**
     * <p>
     * Sets value of given field. Field will set accessible if it is not accessible.
     * </p>
     * 
     * @param field
     *            given field
     * @param inst
     *            instance of class where given field belong, null if field is static
     * @param value
     *            value to be set
     * @throws NullPointerException
     *             if given field is null
     * @throws ReflectionException
     *             if any problem occurs
     * @since 0.0.0
     */
    public static void setFieldValue(Field field, @Nullable Object inst, @Nullable Object value)
            throws NullPointerException, ReflectionException {
        try {
            accessible(field).set(inst, value);
        } catch (Throwable e) {
            throw new ReflectionException(e);
        }
    }

    /**
     * <p>
     * Returns value of field of given field name. This method finds field by {@linkplain #getField(Class, String)},
     * {@linkplain #getterOf(Class, String)} or {@linkplain #isserOf(Class, String)}. Field will set accessible if it is
     * not accessible.
     * </p>
     * <p>
     * Note if not found, a {@linkplain ReflectionException} will be thrown.
     * </p>
     * 
     * @param cls
     *            given class
     * @param inst
     *            instance of given class, maybe null if the field is static
     * @param name
     *            given field name, not empty
     * @return value of field of given field name
     * @throws NullPointerException
     *             if given class or name is null
     * @throws ReflectionException
     *             if field or its getter not found or other problem occurs
     * @since 0.0.0
     */
    public static @Nullable Object getFieldValue(Class<?> cls, @Nullable Object inst, String name)
            throws NullPointerException, ReflectionException {
        Field f = getField(cls, name);
        if (f == null) {
            Method m = Quicker.tryRequire(() -> getterOf(cls, name), () -> isserOf(cls, name));
            return Quicker.tryRequire(m, () -> {
                throw new ReflectionException("No such field or getter of field: " + name + ".",
                        ReflectionException.CODE_NOT_FOUND);
            }, method -> {
                return invoke(accessible(m), inst, Consts.emptyObjectArray());
            });
        } else {
            return getFieldValue(accessible(f), inst);
        }
    }

    /**
     * <p>
     * Sets value of field of given field name. This method finds field by {@linkplain #getField(Class, String)} or
     * {@linkplain #setterOf(Class, String, Class)}. Field will set accessible if it is not accessible.
     * </p>
     * <p>
     * Note if not found, a {@linkplain ReflectionException} will be thrown.
     * </p>
     * 
     * @param cls
     *            given class
     * @param inst
     *            instance of given class, maybe null if the field is static
     * @param name
     *            given field name, not empty
     * @param value
     *            value to be set
     * @throws NullPointerException
     *             if given class or name is null
     * @throws ReflectionException
     *             if field or its getter not found or other problem occurs
     * @since 0.0.0
     */
    public static void setFieldValue(Class<?> cls, @Nullable Object inst, String name, Object value)
            throws NullPointerException, ReflectionException {
        Field f = getField(cls, name);
        if (f == null) {
            Method m = searchMethod(cls, "set" + Quicker.capitalizeInitials(name), new Object[]{value});
            Quicker.tryRequire(m, () -> {
                throw new ReflectionException("No such field or setter of field: " + name + ".",
                        ReflectionException.CODE_NOT_FOUND);
            }, method -> {
                return invoke(accessible(m), inst, value);
            });
        } else {
            setFieldValue(accessible(f), inst, value);
        }
    }

    /**
     * <p>
     * Returns value of field of given field name for given object. This method finds field by
     * {@linkplain #getField(Class, String)}, {@linkplain #getterOf(Class, String)} or
     * {@linkplain #isserOf(Class, String)}. Field will set accessible if it is not accessible.
     * </p>
     * <p>
     * Note if not found, a {@linkplain ReflectionException} will be thrown.
     * </p>
     * 
     * @param obj
     *            given object
     * @param name
     *            given field name, not empty
     * @return value of field of given field name
     * @throws NullPointerException
     *             if given object or name is null
     * @throws ReflectionException
     *             if field or its getter not found or other problem occurs
     * @since 0.0.0
     */
    public static Object getFieldValue(Object obj, String name) throws NullPointerException, ReflectionException {
        return getFieldValue(Quicker.require(obj).getClass(), obj, name);
    }

    /**
     * <p>
     * Sets value of field of given field name for given object. This method finds field by
     * {@linkplain #getField(Class, String)} or {@linkplain #setterOf(Class, String, Class)}. Field will set accessible
     * if it is not accessible.
     * </p>
     * <p>
     * Note if not found, a {@linkplain ReflectionException} will be thrown.
     * </p>
     * 
     * @param obj
     *            given object
     * @param name
     *            given field name, not empty
     * @param value
     *            value to be set
     * @throws NullPointerException
     *             if given object or name is null
     * @throws ReflectionException
     *             if field or its getter not found or other problem occurs
     * @since 0.0.0
     */
    public static void setFieldValue(Object obj, String name, Object value)
            throws NullPointerException, ReflectionException {
        setFieldValue(Quicker.require(obj).getClass(), obj, name, value);
    }

    /**
     * <p>
     * Invokes given method. Method will set accessible if it is not accessible.
     * </p>
     * 
     * @param method
     *            given method
     * @param inst
     *            instance of class where given method belong, null if method is static
     * @param args
     *            arguments of method
     * @return result of invoking
     * @throws NullPointerException
     *             if given method is null
     * @throws ReflectionException
     *             if any problem occurs
     * @since 0.0.0
     */
    public static Object invoke(Method method, @Nullable Object inst, @Nullable Object... args)
            throws NullPointerException, ReflectionException {
        try {
            return accessible(Quicker.require(method)).invoke(inst, args);
        } catch (Throwable e) {
            throw new ReflectionException(e);
        }
    }

    /**
     * <p>
     * Invokes specified method of given name and parameter types. Method will set accessible if it is not accessible.
     * </p>
     * <p>
     * Note if not found, a {@linkplain ReflectionException} will be thrown.
     * </p>
     * 
     * @param cls
     *            given class
     * @param inst
     *            instance of given class, maybe null if the method is static
     * @param name
     *            given method name
     * @param parameterTypes
     *            given parameter types
     * @param args
     *            given runtime arguments
     * @return result of invoke
     * @throws NullPointerException
     *             if given class or name is null
     * @throws ReflectionException
     *             if method not found or other problem occurs
     * @since 0.0.0
     */
    public static Object invoke(Class<?> cls, @Nullable Object inst, String name, @Nullable Class<?>[] parameterTypes,
            @Nullable Object[] args) throws NullPointerException, ReflectionException {
        return Quicker.tryRequire(accessible(getMethod(cls, name, parameterTypes)), () -> {
            throw new ReflectionException(toMethodString(name, parameterTypes), ReflectionException.CODE_NOT_FOUND);
        }, m -> {
            return invoke(accessible(m), inst, args);
        });
    }

    private static String toMethodString(String name, Class<?>[] parameterTypes) {
        return "No such method: " + name + "(" + ToStringQuicker.join(parameterTypes) + ").";
    }

    private static String toConstructorString(String name, Class<?>[] parameterTypes) {
        return "No such Constructor: " + name + "(" + ToStringQuicker.join(parameterTypes) + ").";
    }

    /**
     * <p>
     * Invokes specified method of given name and given arguments. Method will set accessible if it is not accessible.
     * </p>
     * <p>
     * This method will speculate and contrast parameter types according to given runtime argument objects to return
     * most confirm method. Return null if method not found.
     * </p>
     * <p>
     * Note if not found, a {@linkplain ReflectionException} will be thrown.
     * </p>
     * 
     * @param cls
     *            given class
     * @param inst
     *            instance of given class, maybe null if the method is static
     * @param name
     *            given method name
     * @param args
     *            given runtime arguments
     * @return result of invoke
     * @throws NullPointerException
     *             if given class or name is null
     * @throws ReflectionException
     *             if method not found or other problem occurs
     * @since 0.0.0
     */
    public static Object invoke(Class<?> cls, @Nullable Object inst, String name, @Nullable Object... args)
            throws NullPointerException, ReflectionException {
        return Quicker.tryRequire(accessible(searchMethod(cls, name, args)), () -> {
            throw new ReflectionException(
                    toMethodString(name, QuiCollections.convert(args, Class.class, o -> o.getClass())),
                    ReflectionException.CODE_NOT_FOUND);
        }, m -> {
            return invoke(accessible(m), inst, args);
        });
    }

    /**
     * <p>
     * Invokes specified static method of given name and given arguments. Method will set accessible if it is not
     * accessible.
     * </p>
     * <p>
     * This method will speculate and contrast parameter types according to given runtime argument objects to return
     * most confirm method. Return null if method not found.
     * </p>
     * <p>
     * Note if not found, a {@linkplain ReflectionException} will be thrown.
     * </p>
     * 
     * @param cls
     *            given class
     * @param name
     *            given method name
     * @param args
     *            given runtime arguments
     * @return result of invoke
     * @throws NullPointerException
     *             if given class or name is null
     * @throws ReflectionException
     *             if method not found or other problem occurs
     * @since 0.0.0
     */
    public static Object invoke(Class<?> cls, String name, @Nullable Object... args)
            throws NullPointerException, ReflectionException {
        return invoke(cls, null, name, args);
    }

    /**
     * <p>
     * Invokes specified instance method of given name and given arguments. Method will set accessible if it is not
     * accessible.
     * </p>
     * <p>
     * This method will speculate and contrast parameter types according to given runtime argument objects to return
     * most confirm method. Return null if method not found.
     * </p>
     * <p>
     * Note if not found, a {@linkplain ReflectionException} will be thrown.
     * </p>
     * 
     * @param obj
     *            given object
     * @param name
     *            given method name
     * @param args
     *            given runtime arguments
     * @return result of invoke
     * @throws NullPointerException
     *             if given object or name is null
     * @throws ReflectionException
     *             if method not found or other problem occurs
     * @since 0.0.0
     */
    public static Object invoke(Object obj, String name, @Nullable Object... args)
            throws NullPointerException, ReflectionException {
        return invoke(Quicker.require(obj).getClass(), obj, name, args);
    }

    /**
     * <p>
     * Constructs a new instance. Constructor will set accessible if it is not accessible.
     * </p>
     * 
     * @param constructor
     *            given constructor
     * @param args
     *            given arguments of constructor
     * @return a new instance constructed with given constructor and arguments
     * @throws NullPointerException
     *             if given constructor is null
     * @throws ReflectionException
     *             if any problem occurs
     * @since 0.0.0
     */
    public static Object construct(Constructor<?> constructor, @Nullable Object... args)
            throws NullPointerException, ReflectionException {
        try {
            return accessible(Quicker.require(constructor)).newInstance(args);
        } catch (Throwable t) {
            throw new ReflectionException(t);
        }
    }

    /**
     * <p>
     * Constructs a new instance with specified class name, parameter types and arguments. Constructor will set
     * accessible if it is not accessible.
     * </p>
     * 
     * @param className
     *            specified class name
     * @param parameterTypes
     *            parameter types
     * @param args
     *            arguments
     * @return a new instance constructed with given class name, parameter types and arguments
     * @throws NullPointerException
     *             if given class name is null
     * @throws ReflectionException
     *             if any problem occurs
     * @since 0.0.0
     */
    public static Object construct(String className, @Nullable Class<?>[] parameterTypes, @Nullable Object[] args)
            throws NullPointerException, ReflectionException {
        try {
            return Quicker.tryRequire(getConstructor(getClass(Quicker.require(className)), parameterTypes), () -> {
                throw new ReflectionException(toConstructorString(className, parameterTypes),
                        ReflectionException.CODE_NOT_FOUND);
            }, c -> construct(accessible(c), args));
        } catch (Throwable t) {
            throw new ReflectionException(t);
        }
    }

    /**
     * <p>
     * Constructs a new instance with specified class name and arguments. Constructor will set accessible if it is not
     * accessible.
     * </p>
     * 
     * @param className
     *            specified class name
     * @param args
     *            arguments
     * @return a new instance constructed with given class name and arguments
     * @throws NullPointerException
     *             if given class name is null
     * @throws ReflectionException
     *             if any problem occurs
     * @since 0.0.0
     */
    public static Object construct(String className, @Nullable Object[] args)
            throws NullPointerException, ReflectionException {
        try {
            return Quicker.tryRequire(searchConstructor(getClass(Quicker.require(className)), args), () -> {
                throw new ReflectionException(toConstructorString(className, Consts.emptyClassArray()),
                        ReflectionException.CODE_NOT_FOUND);
            }, c -> construct(accessible(c), args));
        } catch (Throwable t) {
            throw new ReflectionException(t);
        }
    }

    static class SuperclassIterator implements Iterator<Class<?>> {

        private Class<?> cur;

        @Nullable
        private final Class<?> upTo;

        SuperclassIterator(Class<?> cls, @Nullable Class<?> upTo) {
            this.cur = cls;
            this.upTo = upTo;
        }

        @Override
        public boolean hasNext() {
            return cur != null;
        }

        @Override
        public Class<?> next() {
            if (cur == null) {
                throw new NoSuchElementException();
            }
            Class<?> ret = cur;
            if (cur.equals(upTo)) {
                cur = null;
            } else {
                cur = cur.getSuperclass();
            }
            return ret;
        }
    }

    static class InterfaceIterator implements Iterator<Class<?>> {

        private int cur = 0;

        private final List<Class<?>> list = new ArrayList<>();

        InterfaceIterator(Class<?> cls) {
            QuiCollections.addAll(list, cls.getInterfaces());
        }

        @Override
        public boolean hasNext() {
            return cur < list.size();
        }

        @Override
        public Class<?> next() {
            if (cur >= list.size()) {
                throw new NoSuchElementException();
            }
            Class<?> ret = list.get(cur);
            Quicker.each(ret.getInterfaces(), i -> {
                if (!list.contains(i)) {
                    list.add(i);
                }
            });
            cur++;
            return ret;
        }

    }

    static class MemberIterator<E> implements Iterator<E> {
        private final LinkedList<E> linkedList = new LinkedList<>();
        private final SuperclassIterator clsIt;
        private final Function<Class<?>, E[]> action;

        MemberIterator(Class<?> cls, Function<Class<?>, E[]> action, @Nullable Class<?> upTo) {
            clsIt = new SuperclassIterator(cls, upTo);
            this.action = action;
            while (clsIt.hasNext() && linkedList.isEmpty()) {
                QuiCollections.addAll(linkedList, action.apply(clsIt.next()));
            }
        }

        @Override
        public boolean hasNext() {
            return !linkedList.isEmpty();
        }

        @Override
        public E next() {
            if (linkedList.isEmpty()) {
                throw new NoSuchElementException();
            }
            E ret = linkedList.pollFirst();
            while (linkedList.isEmpty() && clsIt.hasNext()) {
                Quicker.each(action.apply(clsIt.next()), e -> {
                    if (!linkedList.contains(e)) {
                        linkedList.addLast(e);
                    }
                });
            }
            return ret;
        }
    }
}
