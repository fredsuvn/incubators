package com.cogician.quicker;

import java.nio.Buffer;
import java.util.Collection;
import java.util.IllegalFormatException;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import com.cogician.quicker.binary.Binary;

/**
 * <p>
 * Checker is a simple, quick and thread-safe static tool for checking arguments. It is convenient to write clear code
 * for checking null, empty, index, positive or other common checking.
 * </p>
 * <p>
 * Checker is aimed at using minimal codes to check common problem <b>without redundancy</b>. It is not recommended that
 * adding redundant description into a common, obvious and without-saying checking such as:
 * 
 * <pre>
 * public void do(Object obj){
 *     Checker.checkNull(obj, "Passed object cannot be null!");
 *     ...more
 * }
 * 
 * If it is not in a special circumstance, "Passed object cannot be null!"
 * is a redundant description, just using:
 * 
 * public void do(Object obj){
 *     Checker.checkNull(obj);
 *     ...more
 * }
 * </pre>
 * 
 * </p>
 * <p>
 * Some methods include varargs, auto-boxing and varargs array creation happening when they were called. These method
 * may have extra performance cost (and they are not convenient to use frequently) so it is not recommended to use these
 * methods in frequently-called places.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2015-07-01 13:48:02
 * @since 0.0.0
 */
public class Checker {

    /**
     * <p>
     * Empty arguments array. Commonly used for empty varargs.
     * </p>
     * 
     * @since 0.0.0
     */
    public static final Object[] EMPTY_VARARGS = {};

    /**
     * <p>
     * Description for passing empty argument.
     * </p>
     * 
     * @since 0.0.0
     */
    private static final String PASSED_EMPTY = "Given %s is empty.";

    /**
     * <p>
     * Description for passing null element or value in a container.
     * </p>
     * 
     * @since 0.0.0
     */
    private static final String PASSED_NULL_IN_CONTAINER = "Exists null element or value in given %s.";

    /**
     * <p>
     * Description for checking positive (0 exclusive).
     * </p>
     * 
     * @since 0.0.0
     */
    private static final String CHECK_POSITIVE = "Number should be positive: %s.";

    /**
     * <p>
     * Description for checking positive (0 inclusive).
     * </p>
     * 
     * @since 0.0.0
     */
    private static final String CHECK_POSITIVE_OR_0 = "Number should be positive or 0: %s.";

    /**
     * <p>
     * Description for checking length. A legal length should be >= 0.
     * </p>
     * 
     * @since 0.0.0
     */
    private static final String CHECK_LENGTH = "Illegal length: %s.";

    /**
     * <p>
     * Description for checking range out of order: startIndex > endIndex.
     * </p>
     * 
     * @since 0.0.0
     */
    private static final String RANGE_INDEXES_OUT_OF_ORDER = "Range indexes out of order: startIndex/from > endIndex/to.";

    /**
     * <p>
     * Description for passing range out of bounds.
     * </p>
     * 
     * @since 0.0.0
     */
    private static final String RANGE_OUT_OF_BOUNDS = "Out of bounds, startIndex/from: %s, endIndex/to: %s, size: %s.";

    /**
     * <p>
     * Description for checking bounds out of order: start/from > end/to.
     * </p>
     * 
     * @since 0.0.0
     */
    private static final String BOUNDS_OUT_OF_ORDER = "Bounds out of order: start/from > end/to.";

    /**
     * <p>
     * Description for checking whether objects are equal.
     * </p>
     * 
     * @since 0.0.0
     */
    private static final String CHECK_EQUAL = "Given objects are not equal.";

    /**
     * <p>
     * Description for checking whether objects are distinct.
     * </p>
     * 
     * @since 0.0.0
     */
    private static final String CHECK_DISTINCT = "Given objects are not distinct.";

    /**
     * <p>
     * Description for checking whether passed object is an array.
     * </p>
     * 
     * @since 0.0.0
     */
    private static final String CHECK_ARRAY = "Given object is not an array.";

    /**
     * <p>
     * Empty constructor for some reflection framework which at least need a non-parameter constructor.
     * </p>
     * 
     * @since 0.0.0
     */
    public Checker() {

    }

    /**
     * <p>
     * Checks whether given expression is true. If not, an {@linkplain IllegalArgumentException} with no detail message
     * will be thrown.
     * </p>
     * 
     * @param expr
     *            given expression
     * @throws IllegalArgumentException
     *             if given expression is false
     * @since 0.0.0
     */
    public static void check(boolean expr) throws IllegalArgumentException {
        if (!expr) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * <p>
     * Checks whether given expression is true. If not, given throwable will be thrown. If given throwable is not
     * instance of RuntimeException, throw a {@linkplain WrappedException} wraps given throwable; else given throwable
     * will be directly throw.
     * </p>
     * <p>
     * This method do nothing if given throwable is null.
     * </p>
     * 
     * @param expr
     *            given expression
     * @param throwable
     *            given throwable
     * @throws RuntimeException
     *             if given expression is false and given throwable is instance of RuntimeException
     * @throws WrappedException
     *             if given expression is false and given throwable is not instance of RuntimeException
     * @since 0.0.0
     */
    public static void check(boolean expr, @Nullable Throwable throwable) throws RuntimeException, WrappedException {
        if (!expr) {
            if (throwable instanceof RuntimeException) {
                throw (RuntimeException)throwable;
            } else {
                throw new WrappedException(throwable);
            }
        }
    }

    /**
     * <p>
     * Checks whether given expression is true. If not, an {@linkplain IllegalArgumentException} with given message will
     * be thrown.
     * </p>
     * 
     * @param expr
     *            given expression
     * @param msg
     *            given message
     * @throws IllegalArgumentException
     *             if given expression is false
     * @since 0.0.0
     */
    public static void check(boolean expr, @Nullable String msg) throws IllegalArgumentException {
        if (!expr) {
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * <p>
     * Checks whether given expression is true. If not, an {@linkplain IllegalArgumentException} with given formatted
     * message will be thrown.
     * </p>
     * 
     * @param expr
     *            given expression
     * @param msg
     *            given formatted message
     * @param msgArgs
     *            arguments of given formatted message
     * @throws IllegalFormatException
     *             if formatting failed
     * @throws IllegalArgumentException
     *             if given expression is false
     * @since 0.0.0
     */
    public static void check(boolean expr, String msg, Object... msgArgs)
            throws IllegalFormatException, IllegalArgumentException {
        if (!expr) {
            throw new IllegalArgumentException(QuickerUniform.format(msg, msgArgs));
        }
    }

    /**
     * <p>
     * Checks whether given object is null. If it is, a {@linkplain NullPointerException} with no detail message will be
     * thrown.
     * </p>
     * 
     * @param obj
     *            given object
     * @throws NullPointerException
     *             if given object is null
     * @since 0.0.0
     */
    public static final void checkNull(Object obj) throws NullPointerException {
        // if (obj == null) {
        // throw new NullPointerException();
        // }
        obj.getClass();
    }

    /**
     * <p>
     * Checks whether given object is null. If it is, a {@linkplain NullPointerException} with given message will be
     * thrown.
     * </p>
     * 
     * @param obj
     *            given object
     * @param msg
     *            given message
     * @throws NullPointerException
     *             if given object is null
     * @since 0.0.0
     */
    public static void checkNull(Object obj, @Nullable String msg) throws NullPointerException {
        if (obj == null) {
            throw new NullPointerException(msg);
        }
    }

    /**
     * <p>
     * Checks whether given object is null. If it is, a {@linkplain NullPointerException} with given formatted message
     * will be thrown.
     * </p>
     * 
     * @param obj
     *            given object
     * @param msg
     *            given formatted message
     * @param msgArgs
     *            arguments of given formatted message
     * @throws IllegalFormatException
     *             if formatting failed
     * @throws NullPointerException
     *             if given object is null
     * @since 0.0.0
     */
    public static void checkNull(Object obj, String msg, Object... msgArgs)
            throws IllegalFormatException, NullPointerException {
        if (obj == null) {
            throw new NullPointerException(QuickerUniform.format(msg, msgArgs));
        }
    }

    /**
     * <p>
     * Throws if given predication returns true with given object. It is used as:
     * 
     * <pre>
     * Checkor.checkTrue(map, Checkor::hasNullValue, "Description!", Checkor.EMPTY_VARARGS);
     * </pre>
     * 
     * Above will check whether given map has null value (see {@linkplain #hasNullValue(Map)}). By that analogy, if
     * given predication returns true, an {@linkplain IllegalArgumentException} with given formatted message will be
     * thrown.
     * </p>
     * 
     * @param <T>
     *            type of given object
     * @param obj
     *            given object to be checked, not null
     * @param isTrue
     *            given predication, not null
     * @param msg
     *            given formatted message
     * @param msgArgs
     *            arguments of given formatted message
     * @throws NullPointerException
     *             if given object or predication is null
     * @throws IllegalFormatException
     *             if formatting failed
     * @throws IllegalArgumentException
     *             if given predication returns true
     * @since 0.0.0
     */
    public static <T> void throwIfTrue(T obj, Predicate<T> isTrue, String msg, Object... msgArgs)
            throws NullPointerException, IllegalFormatException, IllegalArgumentException {
        checkNull(obj);
        checkNull(isTrue);
        if (isTrue.test(obj)) {
            throw new IllegalArgumentException(QuickerUniform.format(msg, msgArgs));
        }
    }

    /**
     * <p>
     * Throws if given predication returns false with given object. It is used as:
     * 
     * <pre>
     * Checkor.checkFalse(string, Checkor::isNotEmpty, "Description!", Checkor.EMPTY_VARARGS);
     * </pre>
     * 
     * Above will check whether given string is null or empty (see {@linkplain #isNotEmpty(String)}). By that analogy,
     * if given predication returns false, an {@linkplain IllegalArgumentException} with given formatted message will be
     * thrown.
     * </p>
     * 
     * @param <T>
     *            type of given object
     * @param obj
     *            given object to be checked, not null
     * @param isFalse
     *            given predication, not null
     * @param msg
     *            given formatted message
     * @param msgArgs
     *            arguments of given formatted message
     * @throws NullPointerException
     *             if given object or predication is null
     * @throws IllegalFormatException
     *             if formatting failed
     * @throws IllegalArgumentException
     *             if given predication returns true
     * @since 0.0.0
     */
    public static <T> void throwIfFalse(T obj, Predicate<T> isFalse, String msg, Object... msgArgs) {
        checkNull(obj);
        checkNull(isFalse);
        if (!isFalse.test(obj)) {
            throw new IllegalArgumentException(QuickerUniform.format(msg, msgArgs));
        }
    }

    /**
     * <p>
     * Returns whether there exists any null element in given array.
     * </p>
     * 
     * @param <E>
     *            component type of given array
     * @param array
     *            given array, not null
     * @return whether there exist any null element in given array
     * @throws NullPointerException
     *             if given array is null
     * @since 0.0.0
     */
    public static <E> boolean hasNullElement(E[] array) throws NullPointerException {
        checkNull(array);
        for (int i = 0; i < array.length; i++) {
            if (array[i] == null) {
                return true;
            }
        }
        return false;
    }

    /**
     * <p>
     * Returns whether there exists any null element in given iterable.
     * </p>
     * 
     * @param iterable
     *            given iterable, not null
     * @return whether there exist any null element in given iterable
     * @throws NullPointerException
     *             if given iterable is null
     * @since 0.0.0
     */
    public static boolean hasNullElement(Iterable<?> iterable) throws NullPointerException {
        checkNull(iterable);
        Iterator<?> iterator = iterable.iterator();
        while (iterator.hasNext()) {
            if (iterator.next() == null) {
                return true;
            }
        }
        return false;
    }

    /**
     * <p>
     * Returns whether there exists any null value in given map.
     * </p>
     * 
     * @param map
     *            given map, not null
     * @return whether there exist any null value in given map
     * @throws NullPointerException
     *             if given map is null
     * @since 0.0.0
     */
    public static boolean hasNullValue(Map<?, ?> map) throws NullPointerException {
        checkNull(map);
        Iterator<?> iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            if (map.get(iterator.next()) == null) {
                return true;
            }
        }
        return false;
    }

    /**
     * <p>
     * Checks whether there exists any null element in given array. If there exists, an
     * {@linkplain IllegalArgumentException} with message {@value #CHECK_NULL_ELEMENT_OR_VALUE} will be thrown.
     * </p>
     * 
     * @param <E>
     *            component type of given array
     * @param array
     *            given array, not null
     * @throws NullPointerException
     *             if given array or any element of array is null
     * @since 0.0.0
     */
    public static <E> void checkNullElement(E[] array) throws NullPointerException {
        if (hasNullElement(array)) {
            throw new NullPointerException(QuickerUniform.format(PASSED_NULL_IN_CONTAINER, "array"));
        }
    }

    /**
     * <p>
     * Checks whether there exists any null element in given iterable. If there exists, an
     * {@linkplain IllegalArgumentException} with message {@value #CHECK_NULL_ELEMENT_OR_VALUE} will be thrown.
     * </p>
     * 
     * @param iterable
     *            given iterable, not null
     * @throws NullPointerException
     *             if given iterable or any element of iterable is null
     * @since 0.0.0
     */
    public static void checkNullElement(Iterable<?> iterable) throws NullPointerException {
        if (hasNullElement(iterable)) {
            throw new NullPointerException(QuickerUniform.format(PASSED_NULL_IN_CONTAINER, "iterable"));
        }
    }

    /**
     * <p>
     * Checks whether there exists any null value in given map. If there exists, an
     * {@linkplain IllegalArgumentException} with message {@value #CHECK_NULL_VALUE} will be thrown.
     * </p>
     * 
     * @param map
     *            given map, not null
     * @throws NullPointerException
     *             if given map or any value of map is null
     * @since 0.0.0
     */
    public static void checkNullValue(Map<?, ?> map) throws NullPointerException {
        if (hasNullValue(map)) {
            throw new NullPointerException(QuickerUniform.format(PASSED_NULL_IN_CONTAINER, "map"));
        }
    }

    /**
     * <p>
     * Checks whether given number is positive (0 exclusive).
     * </p>
     * 
     * @param n
     *            given number
     * @throws IllegalArgumentException
     *             if given number is not positive
     * @since 0.0.0
     */
    public static void checkPositive(int n) throws IllegalArgumentException {
        if (n <= 0) {
            throw new IllegalArgumentException(QuickerUniform.format(CHECK_POSITIVE, n));
        }
    }

    /**
     * <p>
     * Checks whether given number is positive (0 exclusive).
     * </p>
     * 
     * @param n
     *            given number
     * @throws IllegalArgumentException
     *             if given number is not positive
     * @since 0.0.0
     */
    public static void checkPositive(long n) throws IllegalArgumentException {
        if (n <= 0) {
            throw new IllegalArgumentException(QuickerUniform.format(CHECK_POSITIVE, n));
        }
    }

    /**
     * <p>
     * Checks whether given number is positive (0 inclusive).
     * </p>
     * 
     * @param n
     *            given number
     * @throws IllegalArgumentException
     *             if given number is not positive or 0
     * @since 0.0.0
     */
    public static void checkNonnegative(int n) throws IllegalArgumentException {
        if (n < 0) {
            throw new IllegalArgumentException(QuickerUniform.format(CHECK_POSITIVE_OR_0, n));
        }
    }

    /**
     * <p>
     * Checks whether given number is positive (0 inclusive).
     * </p>
     * 
     * @param n
     *            given number
     * @throws IllegalArgumentException
     *             if given number is not positive or 0
     * @since 0.0.0
     */
    public static void checkNonnegative(long n) throws IllegalArgumentException {
        if (n < 0) {
            throw new IllegalArgumentException(QuickerUniform.format(CHECK_POSITIVE_OR_0, n));
        }
    }

    /**
     * <p>
     * Checks whether given number is an legal length, an legal length being 0 or positive.
     * </p>
     * 
     * @param l
     *            given number
     * @throws IllegalArgumentException
     *             if given number is not an legal length
     * @since 0.0.0
     */
    public static void checkLength(int l) throws IllegalArgumentException {
        if (l < 0) {
            throw new IllegalArgumentException(QuickerUniform.format(CHECK_LENGTH, l));
        }
    }

    /**
     * <p>
     * Checks whether given number is an legal length, an legal length being 0 or positive.
     * </p>
     * 
     * @param l
     *            given number
     * @throws IllegalArgumentException
     *             if given number is not an legal length
     * @since 0.0.0
     */
    public static void checkLength(long l) throws IllegalArgumentException {
        if (l < 0) {
            throw new IllegalArgumentException(QuickerUniform.format(CHECK_LENGTH, l));
        }
    }

    /**
     * <p>
     * Checks whether given index is in bounds of given array.
     * </p>
     * 
     * @param index
     *            given index
     * @param array
     *            given array
     * @throws IndexOutOfBoundsException
     *             if given index is out of bounds
     * @since 0.0.0
     */
    public static void checkIndex(int index, @Nullable boolean[] array) throws IndexOutOfBoundsException {
        if (index < 0 || isEmpty(array) || index >= array.length) {
            throw new IndexOutOfBoundsException(String.valueOf(index));
        }
    }

    /**
     * <p>
     * Checks whether given index is in bounds of given array.
     * </p>
     * 
     * @param index
     *            given index
     * @param array
     *            given array
     * @throws IndexOutOfBoundsException
     *             if given index is out of bounds
     * @since 0.0.0
     */
    public static void checkIndex(int index, @Nullable byte[] array) throws IndexOutOfBoundsException {
        if (index < 0 || isEmpty(array) || index >= array.length) {
            throw new IndexOutOfBoundsException(String.valueOf(index));
        }
    }

    /**
     * <p>
     * Checks whether given index is in bounds of given array.
     * </p>
     * 
     * @param index
     *            given index
     * @param array
     *            given array
     * @throws IndexOutOfBoundsException
     *             if given index is out of bounds
     * @since 0.0.0
     */
    public static void checkIndex(int index, @Nullable short[] array) throws IndexOutOfBoundsException {
        if (index < 0 || isEmpty(array) || index >= array.length) {
            throw new IndexOutOfBoundsException(String.valueOf(index));
        }
    }

    /**
     * <p>
     * Checks whether given index is in bounds of given array.
     * </p>
     * 
     * @param index
     *            given index
     * @param array
     *            given array
     * @throws IndexOutOfBoundsException
     *             if given index is out of bounds
     * @since 0.0.0
     */
    public static void checkIndex(int index, @Nullable char[] array) throws IndexOutOfBoundsException {
        if (index < 0 || isEmpty(array) || index >= array.length) {
            throw new IndexOutOfBoundsException(String.valueOf(index));
        }
    }

    /**
     * <p>
     * Checks whether given index is in bounds of given array.
     * </p>
     * 
     * @param index
     *            given index
     * @param array
     *            given array
     * @throws IndexOutOfBoundsException
     *             if given index is out of bounds
     * @since 0.0.0
     */
    public static void checkIndex(int index, @Nullable int[] array) throws IndexOutOfBoundsException {
        if (index < 0 || isEmpty(array) || index >= array.length) {
            throw new IndexOutOfBoundsException(String.valueOf(index));
        }
    }

    /**
     * <p>
     * Checks whether given index is in bounds of given array.
     * </p>
     * 
     * @param index
     *            given index
     * @param array
     *            given array
     * @throws IndexOutOfBoundsException
     *             if given index is out of bounds
     * @since 0.0.0
     */
    public static void checkIndex(int index, @Nullable float[] array) throws IndexOutOfBoundsException {
        if (index < 0 || isEmpty(array) || index >= array.length) {
            throw new IndexOutOfBoundsException(String.valueOf(index));
        }
    }

    /**
     * <p>
     * Checks whether given index is in bounds of given array.
     * </p>
     * 
     * @param index
     *            given index
     * @param array
     *            given array
     * @throws IndexOutOfBoundsException
     *             if given index is out of bounds
     * @since 0.0.0
     */
    public static void checkIndex(int index, @Nullable long[] array) throws IndexOutOfBoundsException {
        if (index < 0 || isEmpty(array) || index >= array.length) {
            throw new IndexOutOfBoundsException(String.valueOf(index));
        }
    }

    /**
     * <p>
     * Checks whether given index is in bounds of given array.
     * </p>
     * 
     * @param index
     *            given index
     * @param array
     *            given array
     * @throws IndexOutOfBoundsException
     *             if given index is out of bounds
     * @since 0.0.0
     */
    public static void checkIndex(int index, @Nullable double[] array) throws IndexOutOfBoundsException {
        if (index < 0 || isEmpty(array) || index >= array.length) {
            throw new IndexOutOfBoundsException(String.valueOf(index));
        }
    }

    /**
     * <p>
     * Checks whether given index is in bounds of given array.
     * </p>
     * 
     * @param index
     *            given index
     * @param array
     *            given array
     * @throws IndexOutOfBoundsException
     *             if given index is out of bounds
     * @since 0.0.0
     */
    public static <E> void checkIndex(int index, @Nullable E[] array) throws IndexOutOfBoundsException {
        if (index < 0 || isEmpty(array) || index >= array.length) {
            throw new IndexOutOfBoundsException(String.valueOf(index));
        }
    }

    /**
     * <p>
     * Checks whether given index is in bounds of given collection.
     * </p>
     * 
     * @param index
     *            given index
     * @param collection
     *            given collection
     * @throws IndexOutOfBoundsException
     *             if given index is out of bounds
     * @since 0.0.0
     */
    public static void checkIndex(int index, @Nullable Collection<?> collection) throws IndexOutOfBoundsException {
        if (index < 0 || isEmpty(collection) || index >= collection.size()) {
            throw new IndexOutOfBoundsException(String.valueOf(index));
        }
    }

    /**
     * <p>
     * Checks whether given index is in bounds of given buffer (the upper bound uses {@linkplain Buffer#limit()}).
     * </p>
     * 
     * @param index
     *            given index
     * @param buffer
     *            given buffer
     * @throws IndexOutOfBoundsException
     *             if given index is out of bounds
     * @since 0.0.0
     */
    public static void checkIndex(int index, @Nullable Buffer buffer) throws IndexOutOfBoundsException {
        if (index < 0 || isEmpty(buffer) || index >= buffer.limit()) {
            throw new IndexOutOfBoundsException(String.valueOf(index));
        }
    }

    /**
     * <p>
     * Checks whether given index is in bounds of given size (0 <= index < size).
     * </p>
     * 
     * @param index
     *            given index
     * @param size
     *            given size
     * @throws IndexOutOfBoundsException
     *             if given index is out of bounds
     * @since 0.0.0
     */
    public static void checkIndex(int index, int size) throws IndexOutOfBoundsException {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(String.valueOf(index));
        }
    }

    /**
     * <p>
     * Checks whether given index is in bounds of given size (0 <= index < size).
     * </p>
     * 
     * @param index
     *            given index
     * @param size
     *            given size
     * @throws IndexOutOfBoundsException
     *             if given index is out of bounds
     * @since 0.0.0
     */
    public static void checkIndex(long index, long size) throws IndexOutOfBoundsException {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(String.valueOf(index));
        }
    }

    /**
     * <p>
     * Checks whether range specified by given indexes is in bounds of given size (0 <= startIndex <= endIndex <= size).
     * </p>
     * 
     * @param startIndex
     *            start index, inclusive
     * @param endIndex
     *            end index, exclusive
     * @param size
     *            total size
     * @throws IndexOutOfBoundsException
     *             if indexes out of bounds or startIndex > endIndex
     * @since 0.0.0
     */
    public static void checkRangeIndexes(int startIndex, int endIndex, int size) throws IndexOutOfBoundsException {
        if (startIndex > endIndex) {
            throw new IndexOutOfBoundsException(RANGE_INDEXES_OUT_OF_ORDER);
        }
        if (startIndex < 0 || endIndex > size) {
            throw new IndexOutOfBoundsException(QuickerUniform.format(RANGE_OUT_OF_BOUNDS, startIndex, endIndex, size));
        }
    }

    /**
     * <p>
     * Checks whether range specified by given indexes is in bounds of given size (0 <= startIndex <= endIndex <= size).
     * </p>
     * 
     * @param startIndex
     *            start index, inclusive
     * @param endIndex
     *            end index, exclusive
     * @param size
     *            total size
     * @throws IndexOutOfBoundsException
     *             if indexes out of bounds or startIndex > endIndex
     * @since 0.0.0
     */
    public static void checkRangeIndexes(long startIndex, long endIndex, long size) throws IndexOutOfBoundsException {
        if (startIndex > endIndex) {
            throw new IndexOutOfBoundsException(RANGE_INDEXES_OUT_OF_ORDER);
        }
        if (startIndex < 0 || endIndex > size) {
            throw new IndexOutOfBoundsException(QuickerUniform.format(RANGE_OUT_OF_BOUNDS, startIndex, endIndex, size));
        }
    }

    /**
     * <p>
     * Checks whether {@code from} <= {@code to}.
     * </p>
     * 
     * @param from
     *            from argument
     * @param to
     *            to argument
     * @throws IllegalArgumentException
     *             if {@code from} > {@code to}
     * @since 0.0.0
     */
    public static void checkBoundsOrder(int from, int to) throws IllegalArgumentException {
        if (from > to) {
            throw new IllegalArgumentException(BOUNDS_OUT_OF_ORDER);
        }
    }

    /**
     * <p>
     * Checks whether {@code from} <= {@code to}.
     * </p>
     * 
     * @param from
     *            from argument
     * @param to
     *            to argument
     * @throws IllegalArgumentException
     *             if {@code from} > {@code to}
     * @since 0.0.0
     */
    public static void checkBoundsOrder(long from, long to) throws IllegalArgumentException {
        if (from > to) {
            throw new IllegalArgumentException(BOUNDS_OUT_OF_ORDER);
        }
    }

    /**
     * <p>
     * Checks whether {@code from} <= {@code to}.
     * </p>
     * 
     * @param from
     *            from argument
     * @param to
     *            to argument
     * @throws IllegalArgumentException
     *             if {@code from} > {@code to}
     * @since 0.0.0
     */
    public static void checkBoundsOrder(float from, float to) throws IllegalArgumentException {
        if (from > to) {
            throw new IllegalArgumentException(BOUNDS_OUT_OF_ORDER);
        }
    }

    /**
     * <p>
     * Checks whether {@code from} <= {@code to}.
     * </p>
     * 
     * @param from
     *            from argument
     * @param to
     *            to argument
     * @throws IllegalArgumentException
     *             if {@code from} > {@code to}
     * @since 0.0.0
     */
    public static void checkBoundsOrder(double from, double to) throws IllegalArgumentException {
        if (from > to) {
            throw new IllegalArgumentException(BOUNDS_OUT_OF_ORDER);
        }
    }

    /**
     * <p>
     * Checks whether {@code size} is greater than {@code maxSize}. If it is, an {@linkplain OutOfBoundsException} with
     * specified message will be thrown.
     * </p>
     * <p>
     * This method is used in custom situation of out of bounds.
     * </p>
     * 
     * @param size
     *            given size
     * @param maxSize
     *            given max size
     * @param msg
     *            specified message
     * @throws OutOfBoundsException
     *             if {@code size} out of {@code maxSize}
     * @since 0.0.0
     */
    public static void checkBounds(int size, int maxSize, String msg) throws OutOfBoundsException {
        if (size > maxSize) {
            throw new OutOfBoundsException(msg);
        }
    }

    /**
     * <p>
     * Checks whether {@code size} is greater than {@code maxSize}. If it is, an {@linkplain OutOfBoundsException} with
     * specified message will be thrown.
     * </p>
     * <p>
     * This method is used in custom situation of out of bounds.
     * </p>
     * 
     * @param size
     *            given size
     * @param maxSize
     *            given max size
     * @param msg
     *            specified message
     * @throws OutOfBoundsException
     *             if {@code size} out of {@code maxSize}
     * @since 0.0.0
     */
    public static void checkBounds(long size, long maxSize, String msg) throws OutOfBoundsException {
        if (size > maxSize) {
            throw new OutOfBoundsException(msg);
        }
    }

    /**
     * <p>
     * Checks whether {@code size} is greater than {@code maxSize}. If it is, an {@linkplain OutOfBoundsException} will
     * be thrown.
     * </p>
     * 
     * @param size
     *            given size
     * @param maxSize
     *            given max size
     * @throws OutOfBoundsException
     *             if {@code size} out of {@code maxSize}
     * @since 0.0.0
     */
    public static void checkBounds(int size, int maxSize) throws OutOfBoundsException {
        if (size > maxSize) {
            throw new OutOfBoundsException(size);
        }
    }

    /**
     * <p>
     * Checks whether {@code size} is greater than {@code maxSize}. If it is, an {@linkplain OutOfBoundsException} will
     * be thrown.
     * </p>
     * 
     * @param size
     *            given size
     * @param maxSize
     *            given max size
     * @throws OutOfBoundsException
     *             if {@code size} out of {@code maxSize}
     * @since 0.0.0
     */
    public static void checkBounds(long size, long maxSize) throws OutOfBoundsException {
        if (size > maxSize) {
            throw new OutOfBoundsException(size);
        }
    }

    /**
     * <p>
     * Returns whether it is same type of given two objects.
     * </p>
     * <p>
     * Note if they are both null, return true.
     * </p>
     *
     * @param a
     *            first object
     * @param b
     *            second object
     * @return whether it is same type of given two objects
     * @since 0.0.0
     */
    public static boolean isSameType(final Object a, final Object b) {
        if (a == b) {
            return true;
        } else if (a == null && b == null) {
            return true;
        } else if (a == null || b == null) {
            return false;
        } else {
            return a.getClass().equals(b.getClass());
        }
    }

    /**
     * <p>
     * Returns whether given array is null or empty.
     * </p>
     * 
     * @param <E>
     *            component type of given array
     * @param array
     *            given array
     * @return whether given array is null or empty
     * @since 0.0.0
     */
    public static boolean isEmpty(final boolean[] array) {
        return array == null ? true : array.length == 0;
    }

    /**
     * <p>
     * Returns whether given array is null or empty.
     * </p>
     * 
     * @param <E>
     *            component type of given array
     * @param array
     *            given array
     * @return whether given array is null or empty
     * @since 0.0.0
     */
    public static boolean isEmpty(final byte[] array) {
        return array == null ? true : array.length == 0;
    }

    /**
     * <p>
     * Returns whether given array is null or empty.
     * </p>
     * 
     * @param <E>
     *            component type of given array
     * @param array
     *            given array
     * @return whether given array is null or empty
     * @since 0.0.0
     */
    public static boolean isEmpty(final short[] array) {
        return array == null ? true : array.length == 0;
    }

    /**
     * <p>
     * Returns whether given array is null or empty.
     * </p>
     * 
     * @param <E>
     *            component type of given array
     * @param array
     *            given array
     * @return whether given array is null or empty
     * @since 0.0.0
     */
    public static boolean isEmpty(final char[] array) {
        return array == null ? true : array.length == 0;
    }

    /**
     * <p>
     * Returns whether given array is null or empty.
     * </p>
     * 
     * @param <E>
     *            component type of given array
     * @param array
     *            given array
     * @return whether given array is null or empty
     * @since 0.0.0
     */
    public static boolean isEmpty(final int[] array) {
        return array == null ? true : array.length == 0;
    }

    /**
     * <p>
     * Returns whether given array is null or empty.
     * </p>
     * 
     * @param <E>
     *            component type of given array
     * @param array
     *            given array
     * @return whether given array is null or empty
     * @since 0.0.0
     */
    public static boolean isEmpty(final float[] array) {
        return array == null ? true : array.length == 0;
    }

    /**
     * <p>
     * Returns whether given array is null or empty.
     * </p>
     * 
     * @param <E>
     *            component type of given array
     * @param array
     *            given array
     * @return whether given array is null or empty
     * @since 0.0.0
     */
    public static boolean isEmpty(final long[] array) {
        return array == null ? true : array.length == 0;
    }

    /**
     * <p>
     * Returns whether given array is null or empty.
     * </p>
     * 
     * @param <E>
     *            component type of given array
     * @param array
     *            given array
     * @return whether given array is null or empty
     * @since 0.0.0
     */
    public static boolean isEmpty(final double[] array) {
        return array == null ? true : array.length == 0;
    }

    /**
     * <p>
     * Returns whether given array is null or empty.
     * </p>
     * 
     * @param <E>
     *            component type of given array
     * @param array
     *            given array
     * @return whether given array is null or empty
     * @since 0.0.0
     */
    public static <E> boolean isEmpty(final E[] array) {
        return array == null ? true : array.length == 0;
    }

    /**
     * <p>
     * Returns whether given char sequence is null or empty.
     * </p>
     *
     * @param str
     *            given char sequence
     * @return whether given char sequence is null or empty
     * @since 0.0.0
     */
    public static boolean isEmpty(final CharSequence str) {
        return str == null || str.length() == 0;
    }

    /**
     * <p>
     * Returns whether given collection is null or empty.
     * </p>
     *
     * @param collection
     *            given collection
     * @return whether given collection is null or empty
     * @since 0.0.0
     */
    public static boolean isEmpty(final Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * <p>
     * Returns whether given map is null or empty.
     * </p>
     *
     * @param map
     *            given map
     * @return whether given map is null or empty
     * @since 0.0.0
     */
    public static boolean isEmpty(final Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    /**
     * <p>
     * Returns whether given buffer is null or empty.
     * </p>
     *
     * @param buffer
     *            given buffer
     * @return whether given buffer is null or empty
     * @since 0.0.0
     */
    public static boolean isEmpty(final Buffer buffer) {
        return buffer == null ? true : !buffer.hasRemaining();
    }

    /**
     * <p>
     * Returns whether given binary is null or empty.
     * </p>
     *
     * @param binary
     *            given binary
     * @return whether given binary is null or empty
     * @since 0.0.0
     */
    public static boolean isEmpty(final Binary binary) {
        return binary == null ? true : binary.length() == 0;
    }

    /**
     * <p>
     * Returns whether given array is not null or empty.
     * </p>
     *
     * @param array
     *            given array
     * @return whether given array is not null or empty
     * @since 0.0.0
     */
    public static boolean isNotEmpty(boolean[] array) {
        return !isEmpty(array);
    }

    /**
     * <p>
     * Returns whether given array is not null or empty.
     * </p>
     *
     * @param array
     *            given array
     * @return whether given array is not null or empty
     * @since 0.0.0
     */
    public static boolean isNotEmpty(byte[] array) {
        return !isEmpty(array);
    }

    /**
     * <p>
     * Returns whether given array is not null or empty.
     * </p>
     *
     * @param array
     *            given array
     * @return whether given array is not null or empty
     * @since 0.0.0
     */
    public static boolean isNotEmpty(short[] array) {
        return !isEmpty(array);
    }

    /**
     * <p>
     * Returns whether given array is not null or empty.
     * </p>
     *
     * @param array
     *            given array
     * @return whether given array is not null or empty
     * @since 0.0.0
     */
    public static boolean isNotEmpty(char[] array) {
        return !isEmpty(array);
    }

    /**
     * <p>
     * Returns whether given array is not null or empty.
     * </p>
     *
     * @param array
     *            given array
     * @return whether given array is not null or empty
     * @since 0.0.0
     */
    public static boolean isNotEmpty(int[] array) {
        return !isEmpty(array);
    }

    /**
     * <p>
     * Returns whether given array is not null or empty.
     * </p>
     *
     * @param array
     *            given array
     * @return whether given array is not null or empty
     * @since 0.0.0
     */
    public static boolean isNotEmpty(float[] array) {
        return !isEmpty(array);
    }

    /**
     * <p>
     * Returns whether given array is not null or empty.
     * </p>
     *
     * @param array
     *            given array
     * @return whether given array is not null or empty
     * @since 0.0.0
     */
    public static boolean isNotEmpty(long[] array) {
        return !isEmpty(array);
    }

    /**
     * <p>
     * Returns whether given array is not null or empty.
     * </p>
     *
     * @param array
     *            given array
     * @return whether given array is not null or empty
     * @since 0.0.0
     */
    public static boolean isNotEmpty(double[] array) {
        return !isEmpty(array);
    }

    /**
     * <p>
     * Returns whether given array is not null or empty.
     * </p>
     *
     * @param array
     *            given array
     * @return whether given array is not null or empty
     * @since 0.0.0
     */
    public static <E> boolean isNotEmpty(E[] array) {
        return !isEmpty(array);
    }

    /**
     * <p>
     * Returns whether given char sequence is not null or empty.
     * </p>
     *
     * @param str
     *            given char sequence
     * @return whether given char sequence is not null or empty
     * @since 0.0.0
     */
    public static boolean isNotEmpty(final CharSequence str) {
        return !isEmpty(str);
    }

    /**
     * <p>
     * Returns whether given collection is not null or empty.
     * </p>
     *
     * @param collection
     *            given collection
     * @return whether given collection is not null or empty
     * @since 0.0.0
     */
    public static boolean isNotEmpty(final Collection<?> collection) {
        return !isEmpty(collection);
    }

    /**
     * <p>
     * Returns whether given map is not null or empty.
     * </p>
     *
     * @param map
     *            given map
     * @return whether given map is not null or empty
     * @since 0.0.0
     */
    public static boolean isNotEmpty(final Map<?, ?> map) {
        return !isEmpty(map);
    }

    /**
     * <p>
     * Returns whether given buffer is not null or empty.
     * </p>
     *
     * @param buffer
     *            given buffer
     * @return whether given buffer is not null or empty
     * @since 0.0.0
     */
    public static boolean isNotEmpty(final Buffer buffer) {
        return !isEmpty(buffer);
    }

    /**
     * <p>
     * Returns whether given binary is not null or empty.
     * </p>
     *
     * @param binary
     *            given binary
     * @return whether given binary is not null or empty
     * @since 0.0.0
     */
    public static boolean isNotEmpty(final Binary binary) {
        return !isEmpty(binary);
    }

    /**
     * <p>
     * Checks whether given array is null or empty. If it is, an {@linkplain NullPointerException} or
     * {@linkplain IllegalArgumentException} with message {@value #CHECK_EMPTY} will be thrown.
     * </p>
     * 
     * @param array
     *            given array
     * @throws NullPointerException
     *             if given array is null
     * @throws IllegalArgumentException
     *             if given array is empty
     * @since 0.0.0
     */
    public static void checkEmpty(final boolean[] array) throws NullPointerException, IllegalArgumentException {
        checkNull(array);
        if (isEmpty(array)) {
            throw new IllegalArgumentException(QuickerUniform.format(PASSED_EMPTY, "array"));
        }
    }

    /**
     * <p>
     * Checks whether given array is null or empty. If it is, an {@linkplain NullPointerException} or
     * {@linkplain IllegalArgumentException} with message {@value #CHECK_EMPTY} will be thrown.
     * </p>
     * 
     * @param array
     *            given array
     * @throws NullPointerException
     *             if given array is null
     * @throws IllegalArgumentException
     *             if given array is empty
     * @since 0.0.0
     */
    public static void checkEmpty(final byte[] array) throws NullPointerException, IllegalArgumentException {
        checkNull(array);
        if (isEmpty(array)) {
            throw new IllegalArgumentException(QuickerUniform.format(PASSED_EMPTY, "array"));
        }
    }

    /**
     * <p>
     * Checks whether given array is null or empty. If it is, an {@linkplain NullPointerException} or
     * {@linkplain IllegalArgumentException} with message {@value #CHECK_EMPTY} will be thrown.
     * </p>
     * 
     * @param array
     *            given array
     * @throws NullPointerException
     *             if given array is null
     * @throws IllegalArgumentException
     *             if given array is empty
     * @since 0.0.0
     */
    public static void checkEmpty(final short[] array) throws NullPointerException, IllegalArgumentException {
        checkNull(array);
        if (isEmpty(array)) {
            throw new IllegalArgumentException(QuickerUniform.format(PASSED_EMPTY, "array"));
        }
    }

    /**
     * <p>
     * Checks whether given array is null or empty. If it is, an {@linkplain NullPointerException} or
     * {@linkplain IllegalArgumentException} with message {@value #CHECK_EMPTY} will be thrown.
     * </p>
     * 
     * @param array
     *            given array
     * @throws NullPointerException
     *             if given array is null
     * @throws IllegalArgumentException
     *             if given array is empty
     * @since 0.0.0
     */
    public static void checkEmpty(final char[] array) throws NullPointerException, IllegalArgumentException {
        checkNull(array);
        if (isEmpty(array)) {
            throw new IllegalArgumentException(QuickerUniform.format(PASSED_EMPTY, "array"));
        }
    }

    /**
     * <p>
     * Checks whether given array is null or empty. If it is, an {@linkplain NullPointerException} or
     * {@linkplain IllegalArgumentException} with message {@value #CHECK_EMPTY} will be thrown.
     * </p>
     * 
     * @param array
     *            given array
     * @throws NullPointerException
     *             if given array is null
     * @throws IllegalArgumentException
     *             if given array is empty
     * @since 0.0.0
     */
    public static void checkEmpty(final int[] array) throws NullPointerException, IllegalArgumentException {
        checkNull(array);
        if (isEmpty(array)) {
            throw new IllegalArgumentException(QuickerUniform.format(PASSED_EMPTY, "array"));
        }
    }

    /**
     * <p>
     * Checks whether given array is null or empty. If it is, an {@linkplain NullPointerException} or
     * {@linkplain IllegalArgumentException} with message {@value #CHECK_EMPTY} will be thrown.
     * </p>
     * 
     * @param array
     *            given array
     * @throws NullPointerException
     *             if given array is null
     * @throws IllegalArgumentException
     *             if given array is empty
     * @since 0.0.0
     */
    public static void checkEmpty(final float[] array) throws NullPointerException, IllegalArgumentException {
        checkNull(array);
        if (isEmpty(array)) {
            throw new IllegalArgumentException(QuickerUniform.format(PASSED_EMPTY, "array"));
        }
    }

    /**
     * <p>
     * Checks whether given array is null or empty. If it is, an {@linkplain NullPointerException} or
     * {@linkplain IllegalArgumentException} with message {@value #CHECK_EMPTY} will be thrown.
     * </p>
     * 
     * @param array
     *            given array
     * @throws NullPointerException
     *             if given array is null
     * @throws IllegalArgumentException
     *             if given array is empty
     * @since 0.0.0
     */
    public static void checkEmpty(final long[] array) throws NullPointerException, IllegalArgumentException {
        checkNull(array);
        if (isEmpty(array)) {
            throw new IllegalArgumentException(QuickerUniform.format(PASSED_EMPTY, "array"));
        }
    }

    /**
     * <p>
     * Checks whether given array is null or empty. If it is, an {@linkplain NullPointerException} or
     * {@linkplain IllegalArgumentException} with message {@value #CHECK_EMPTY} will be thrown.
     * </p>
     * 
     * @param array
     *            given array
     * @throws NullPointerException
     *             if given array is null
     * @throws IllegalArgumentException
     *             if given array is empty
     * @since 0.0.0
     */
    public static void checkEmpty(final double[] array) throws NullPointerException, IllegalArgumentException {
        checkNull(array);
        if (isEmpty(array)) {
            throw new IllegalArgumentException(QuickerUniform.format(PASSED_EMPTY, "array"));
        }
    }

    /**
     * <p>
     * Checks whether given array is null or empty. If it is, an {@linkplain NullPointerException} or
     * {@linkplain IllegalArgumentException} with message {@value #CHECK_EMPTY} will be thrown.
     * </p>
     * 
     * @param <E>
     *            component type of array
     * @param array
     *            given array
     * @throws NullPointerException
     *             if given array is null
     * @throws IllegalArgumentException
     *             if given array is empty
     * @since 0.0.0
     */
    public static <E> void checkEmpty(final E[] array) throws NullPointerException, IllegalArgumentException {
        checkNull(array);
        if (isEmpty(array)) {
            throw new IllegalArgumentException(QuickerUniform.format(PASSED_EMPTY, "array"));
        }
    }

    /**
     * <p>
     * Checks whether given char sequence is null or empty. If it is, an {@linkplain NullPointerException} or
     * {@linkplain IllegalArgumentException} with message {@value #CHECK_EMPTY} will be thrown.
     * </p>
     * 
     * @param str
     *            given char sequence
     * @throws NullPointerException
     *             if given char sequence is null
     * @throws IllegalArgumentException
     *             if given char sequence is empty
     * @since 0.0.0
     */
    public static void checkEmpty(CharSequence str) throws NullPointerException, IllegalArgumentException {
        checkNull(str);
        if (isEmpty(str)) {
            throw new IllegalArgumentException(QuickerUniform.format(PASSED_EMPTY, "string"));
        }
    }

    /**
     * <p>
     * Checks whether given collection is null or empty. If it is, an {@linkplain NullPointerException} or
     * {@linkplain IllegalArgumentException} with message {@value #CHECK_EMPTY} will be thrown.
     * </p>
     * 
     * @param collection
     *            given collection
     * @throws NullPointerException
     *             if given collection is null
     * @throws IllegalArgumentException
     *             if given collection is empty
     * @since 0.0.0
     */
    public static void checkEmpty(final Collection<?> collection)
            throws NullPointerException, IllegalArgumentException {
        checkNull(collection);
        if (isEmpty(collection)) {
            throw new IllegalArgumentException(QuickerUniform.format(PASSED_EMPTY, "collection"));
        }
    }

    /**
     * <p>
     * Checks whether given map is null or empty. If it is, an {@linkplain NullPointerException} or
     * {@linkplain IllegalArgumentException} with message {@value #CHECK_EMPTY} will be thrown.
     * </p>
     * 
     * @param map
     *            given map
     * @throws NullPointerException
     *             if given map is null
     * @throws IllegalArgumentException
     *             if given map is empty
     * @since 0.0.0
     */
    public static void checkEmpty(final Map<?, ?> map) throws NullPointerException, IllegalArgumentException {
        checkNull(map);
        if (isEmpty(map)) {
            throw new IllegalArgumentException(QuickerUniform.format(PASSED_EMPTY, "map"));
        }
    }

    /**
     * <p>
     * Checks whether given buffer is null or empty. If it is, an {@linkplain NullPointerException} or
     * {@linkplain IllegalArgumentException} with message {@value #CHECK_EMPTY} will be thrown.
     * </p>
     * 
     * @param buffer
     *            given buffer
     * @throws NullPointerException
     *             if given buffer is null
     * @throws IllegalArgumentException
     *             if given buffer is empty
     * @since 0.0.0
     */
    public static void checkEmpty(final Buffer buffer) throws NullPointerException, IllegalArgumentException {
        checkNull(buffer);
        if (isEmpty(buffer)) {
            throw new IllegalArgumentException(QuickerUniform.format(PASSED_EMPTY, "buffer"));
        }
    }

    /**
     * <p>
     * Checks whether given binary is null or empty. If it is, an {@linkplain NullPointerException} or
     * {@linkplain IllegalArgumentException} with message {@value #CHECK_EMPTY} will be thrown.
     * </p>
     * 
     * @param binary
     *            given binary
     * @throws NullPointerException
     *             if given binary is null
     * @throws IllegalArgumentException
     *             if given binary is empty
     * @since 0.0.0
     */
    public static void checkEmpty(final Binary binary) throws NullPointerException, IllegalArgumentException {
        checkNull(binary);
        if (isEmpty(binary)) {
            throw new IllegalArgumentException(QuickerUniform.format(PASSED_EMPTY, "buffer"));
        }
    }

    /**
     * <p>
     * Returns whether given two objects are equal:
     * 
     * <pre>
     * o1 == o2 ? true : (o1 == null ? o2.equals(o1) : o1.equals(o2));
     * </pre>
     * </p>
     * 
     * @param o1
     *            first object
     * @param o2
     *            second object
     * @return whether given two objects are equal
     * @since 0.0.0
     */
    public static boolean isEqual(@Nullable Object o1, @Nullable Object o2) {
        return o1 == o2 ? true : (o1 == null ? o2.equals(o1) : o1.equals(o2));
    }

    /**
     * <p>
     * Returns whether all given objects are equal. Any two element should be:
     * 
     * <pre>
     * o1 == o2 ? true : (o1 == null ? o2.equals(o1) : o1.equals(o2));
     * </pre>
     * </p>
     * 
     * @param objs
     *            given objects
     * @return whether given objects are equal
     * @since 0.0.0
     */
    public static boolean isEqual(Object... objs) {
        for (int i = 0; i < objs.length - 1; i++) {
            if (!isEqual(objs[i], objs[i + 1])) {
                return false;
            }
        }
        return true;
    }

    /**
     * <p>
     * Returns whether given two objects are not equal:
     * 
     * <pre>
     * !{@linkplain #isEqual(Object, Object)}
     * </pre>
     * </p>
     * 
     * @param o1
     *            first object
     * @param o2
     *            second object
     * @return whether given two objects are not equal
     * @since 0.0.0
     */
    public static boolean isNotEqual(@Nullable Object o1, @Nullable Object o2) {
        return !isEqual(o1, o2);
    }

    /**
     * <p>
     * Returns whether given arguments are not-all-equal:
     * 
     * <pre>
     * !{@linkplain #isEqual(Object...)}
     * </pre>
     * </p>
     * <p>
     * Use {@linkplain #isDistinct(Object...)} if you want to check whether any two arguments are not equal.
     * </p>
     * 
     * @param objs
     *            given objects
     * @return whether given objects are not-all-equal
     * @since 0.0.0
     * @see Checker#isDistinct(Object...)
     */
    public static boolean isNotEqual(Object... objs) {
        return !isEqual(objs);
    }

    /**
     * <p>
     * Returns whether each given argument are distinct of others.
     * </p>
     * 
     * @param objs
     *            given objects
     * @return whether each given argument are distinct of others
     * @since 0.0.0
     */
    public static boolean isDistinct(Object... objs) {
        if (objs.length <= 1) {
            return true;
        }
        for (int i = 0; i < objs.length - 1; i++) {
            for (int j = 1; j < objs.length; j++) {
                if (isEqual(objs[i], objs[j])) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * <p>
     * Checks whether given two objects are equal:
     * 
     * <pre>
     * o1 == o2 ? true : (o1 == null ? o2.equals(o1) : o1.equals(o2));
     * </pre>
     * </p>
     * 
     * @param o1
     *            first object
     * @param o2
     *            second object
     * @throws IllegalArgumentException
     *             if given two objects are not equal
     * @since 0.0.0
     */
    public static void checkEqual(@Nullable Object o1, @Nullable Object o2) throws IllegalArgumentException {
        if (isNotEqual(o1, o2)) {
            throw new IllegalArgumentException(CHECK_EQUAL);
        }
    }

    /**
     * <p>
     * Checks whether given objects are equal. Any two element should be:
     * 
     * <pre>
     * o1 == o2 ? true : (o1 == null ? o2.equals(o1) : o1.equals(o2));
     * </pre>
     * 
     * .
     * </p>
     * 
     * @param objs
     *            given objects
     * @throws IllegalArgumentException
     *             if any two objects are not equal
     * @since 0.0.0
     */
    public static void checkEqual(Object... objs) {
        if (isNotEqual(objs)) {
            throw new IllegalArgumentException(CHECK_EQUAL);
        }
    }

    /**
     * <p>
     * Checks whether given each of objects is distinct.
     * </p>
     * 
     * @param objs
     *            given objects
     * @throws IllegalArgumentException
     *             if any two objects are equal
     * @since 0.0.0
     */
    public static void checkDistinct(Object... objs) {
        if (isDistinct(objs)) {
            throw new IllegalArgumentException(CHECK_DISTINCT);
        }
    }

    /**
     * <p>
     * Returns whether given char sequence only consists of digits. Using {@linkplain Character#isDigit(char)} to check.
     * </p>
     * 
     * @param str
     *            whether given char sequence only consists of digits
     * @return true if it is
     * @since 0.0.0
     */
    public static boolean isNumeric(@Nullable CharSequence str) {
        if (Checker.isEmpty(str)) {
            return false;
        }
        final int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if (Character.isDigit(str.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * <p>
     * Returns whether given two classes are belong to one type. If one of them is null, return false.
     * </p>
     * 
     * @param cls1
     *            first class
     * @param cls2
     *            second class
     * @return whether given two classes are belong to one type
     * @since 0.0.0
     */
    public static boolean classEqual(@Nullable Class<?> cls1, @Nullable Class<?> cls2) {
        if (cls1 == cls2) {
            return true;
        }
        if (cls1 != null && cls2 != null) {
            return cls1.getName().equals(cls2.getName());
        }
        return false;
    }

    /**
     * <p>
     * Returns whether given object is an array.
     * </p>
     * 
     * @param array
     *            given object
     * @return whether given object is an array
     * @since 0.0.0
     */
    public static boolean isArray(@Nullable Object array) {
        return array == null ? false : array.getClass().isArray();
    }

    /**
     * <p>
     * Checks whether given object is an array
     * </p>
     * 
     * @param array
     *            given object
     * @throws NullPointerException
     *             if given object is null
     * @throws IllegalArgumentException
     *             if given object is not an array
     * @since 0.0.0
     */
    public static void checkArray(Object array) throws NullPointerException, IllegalArgumentException {
        if (!isArray(array)) {
            throw new IllegalArgumentException(CHECK_ARRAY);
        }
    }
}
