package com.cogician.quicker.util;

import java.util.Collections;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * <p>
 * Common constants. All constants of this class are written on class, no configuration, and that distinguishes this
 * class from {@linkplain QuickerArguments}.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-08-05T20:40:39+08:00
 * @since 0.0.0, 2016-08-05T20:40:39+08:00
 */
public class Consts {

    /**
     * <p>
     * Empty object array.
     * </p>
     * 
     * @since 0.0.0
     */
    public static final Object[] EMPTY_OBJECT_ARRAY = {};

    /**
     * <p>
     * Empty string array.
     * </p>
     * 
     * @since 0.0.0
     */
    public static final String[] EMPTY_STRING_ARRAY = {};

    /**
     * <p>
     * Empty class array.
     * </p>
     * 
     * @since 0.0.0
     */
    public static final Class<?>[] EMPTY_CLASS_ARRAY = {};

    /**
     * <p>
     * Returns an empty ojbect array. Commonly this method is used to toArray a list:
     * 
     * <pre>
     * Object[] toArray = list.toArray(Consts.emptyObjectArray());
     * </pre>
     * 
     * Or represents a non-argument flag in reflection.
     * </p>
     * 
     * @return an empty ojbect array
     * @since 0.0.0
     */
    public final static Object[] emptyObjectArray() {
        return EMPTY_OBJECT_ARRAY;
    }

    /**
     * <p>
     * Returns an empty string array. Commonly this method is used to toArray a list:
     * 
     * <pre>
     * String[] toArray = list.toArray(Consts.emptyStringArray());
     * </pre>
     * </p>
     * 
     * @return an empty string array
     * @since 0.0.0
     */
    public final static String[] emptyStringArray() {
        return EMPTY_STRING_ARRAY;
    }

    /**
     * <p>
     * Returns an empty class array. Commonly this method is used to toArray a list:
     * 
     * <pre>
     * Class<?>[] toArray = list.toArray(Consts.emptyClassArray());
     * </pre>
     * 
     * Or represents a non-parameter flag in reflection.
     * </p>
     * 
     * @return an empty class array
     * @since 0.0.0
     */
    public final static Class<?>[] emptyClassArray() {
        return EMPTY_CLASS_ARRAY;
    }

    /**
     * <p>
     * Returns an empty string.
     * </p>
     * 
     * @return an empty string
     * @since 0.0.0
     */
    public final static String emptyString() {
        return "";
    }

    /**
     * <p>
     * Returns an empty iterator.
     * </p>
     * 
     * @param <E>
     *            component type
     * @return an empty iterator
     * @since 0.0.0
     */
    public final static <E> Iterator<E> emptyIterator() {
        return Collections.emptyIterator();
    }

    /**
     * <p>
     * Returns an empty spliterator reports {@linkplain Spliterator#SIZED} and {@linkplain Spliterator#SUBSIZED}.
     * </p>
     * <p>
     * This method is same as {@linkplain Spliterators#emptySpliterator()}.
     * </p>
     * 
     * @param <E>
     *            component type
     * @return an empty spliterator
     * @since 0.0.0
     */
    public final static <E> Spliterator<E> emptySpliterator() {
        return Spliterators.emptySpliterator();
    }

    /**
     * <p>
     * Returns an empty iterable.
     * </p>
     * 
     * @param <E>
     *            component type
     * @return an empty iterable
     * @since 0.0.0
     */
    public final static <E> Iterable<E> emptyIterable() {
        return Consts::emptyIterator;
    }

    /**
     * <p>
     * Returns an empty sequential stream.
     * </p>
     * 
     * @param <E>
     *            component type
     * @return an empty sequential stream
     * @since 0.0.0
     */
    public final static <E> Stream<E> emptyStream() {
        return Stream.empty();
    }

    /**
     * <p>
     * Returns an empty {@linkplain Consumer}. Returned consumer do nothing.
     * </p>
     * 
     * @param <E>
     *            component type
     * @return an empty {@linkplain Consumer}
     * @since 0.0.0
     */
    public final static <E> Consumer<E> emptyConsumer() {
        return (e) -> {
        };
    }
}
