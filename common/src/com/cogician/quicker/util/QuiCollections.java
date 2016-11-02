package com.cogician.quicker.util;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.RandomAccess;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import com.cogician.quicker.Checker;
import com.cogician.quicker.OutOfBoundsException;
import com.cogician.quicker.Quicker;

/**
 * <p>
 * Static quick utility class provides static methods for collection and array.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2015-12-21 08:57:16
 * @since 0.0.0
 */
public class QuiCollections {
    
    //public static <E> Collection<E> collection()
    
    /**
     * <p>
     * Returns an iterator combines given iterators. If given iterators is null, return an empty iterator.
     * </p>
     * 
     * @param <E>
     *            component type
     * @param iterators
     *            given iterators
     * @return iterator combines given iterators
     * @since 0.0.0
     */
    @SafeVarargs
    public static <E> Iterator<E> iterator(@Nullable Iterator<? extends E>... iterators) {
        if (Checker.isEmpty(iterators)) {
            return Consts.emptyIterator();
        }
        return new Iterator<E>() {

            private int i = 0;

            @Override
            public boolean hasNext() {
                while (i < iterators.length && !iterators[i].hasNext()) {
                    i++;
                }
                if (i == iterators.length) {
                    return false;
                }
                return true;

            }

            @Override
            public E next() {
                while (i < iterators.length && !iterators[i].hasNext()) {
                    i++;
                }
                if (i == iterators.length) {
                    throw new NoSuchElementException();
                }
                return iterators[i].next();

            }

            @Override
            public void remove() {
                if (i >= iterators.length) {
                    throw new IllegalStateException();
                }
                iterators[i].remove();
            }
        };
    }

    /**
     * <p>
     * Returns an spliterator combines given spliterators. If given spliterators is null, return an empty spliterator.
     * </p>
     * <p>
     * Given spliterators are considered can be partitioned. Returned spliterator's characteristics is the result of
     * "AND operation" of all given spliterators'characteristics. And the same as estimating size, returned
     * spliterator's estimated size is sum of all, but if any one's estimated size is {@linkplain Long#MAX_VALUE},
     * returned spliterator's will be set {@linkplain Long#MAX_VALUE}.
     * </p>
     * 
     * @param spliterators
     *            given spliterators
     * @return spliterator combines given spliterators
     * @since 0.0.0
     */
    @SafeVarargs
    public static <E> Spliterator<E> spliterator(@Nullable Spliterator<? extends E>... spliterators) {
        if (Checker.isEmpty(spliterators)) {
            return Consts.emptySpliterator();
        }
        return new Spliterator<E>() {

            private int i = 0;

            private final long length;

            private final int characteristics;

            {
                long length = 0;
                for (int i = 0; i < spliterators.length; i++) {
                    if (spliterators[i].estimateSize() == Long.MAX_VALUE) {
                        length = Long.MAX_VALUE;
                        break;
                    }
                    length += spliterators[i].estimateSize();
                }
                this.length = length;

                int characteristics = 0;
                for (int i = 0; i < spliterators.length; i++) {
                    length &= spliterators[i].characteristics();
                }
                this.characteristics = characteristics;
            }

            @Override
            public boolean tryAdvance(Consumer<? super E> action) {
                while (i < spliterators.length && !spliterators[i].tryAdvance(action)) {
                    i++;
                }
                if (i == spliterators.length) {
                    return false;
                }
                return true;
            }

            @Override
            @SuppressWarnings("unchecked")
            public Spliterator<E> trySplit() {
                Spliterator<E> split = null;
                while (i < spliterators.length && (split = (Spliterator<E>)spliterators[i].trySplit()) != null) {
                    i++;
                }
                if (i == spliterators.length) {
                    return null;
                }
                return split;
            }

            @Override
            public long estimateSize() {
                return length;
            }

            @Override
            public int characteristics() {
                return characteristics;
            }
        };
    }

    /**
     * <p>
     * Adds given array into given collection then returns given collection. If given collection is null, return null.
     * </p>
     *
     * @param <E>
     *            type of element
     * @param <T>
     *            type of collection
     * @param collection
     *            given collection
     * @param array
     *            given array
     * @return given collection after adding
     * @throws UnsupportedOperationException
     *             if write operation of given collection is unsupported
     * @since 0.0.0
     */
    public static @Nullable <E, T extends Collection<? super E>> T addAll(@Nullable T collection, @Nullable E[] array)
            throws UnsupportedOperationException {
        if (collection != null && Checker.isNotEmpty(array)) {
            Collections.addAll(collection, array);
        }
        return collection;
    }

    /**
     * <p>
     * Adds given iterator into given collection then returns given collection. If given collection is null, return
     * null.
     * </p>
     *
     * @param <E>
     *            type of element
     * @param <T>
     *            type of collection
     * @param collection
     *            given collection
     * @param iterator
     *            given iterator
     * @return given collection after adding
     * @throws UnsupportedOperationException
     *             if write operation of given collection is unsupported
     * @since 0.0.0
     */
    public static @Nullable <E, T extends Collection<E>> T addAll(@Nullable T collection,
            @Nullable Iterator<? extends E> iterator) throws UnsupportedOperationException {
        if (collection != null) {
            Quicker.each(iterator, e -> {
                collection.add(e);
            });
        }
        return collection;
    }

    /**
     * <p>
     * Adds given iterable into given collection then returns given collection. If given collection is null, return
     * null.
     * </p>
     *
     * @param <E>
     *            type of element
     * @param <T>
     *            type of collection
     * @param collection
     *            given collection
     * @param iterable
     *            given iterable
     * @return given collection after adding
     * @throws UnsupportedOperationException
     *             if write operation of given collection is unsupported
     * @since 0.0.0
     */
    public static @Nullable <E, T extends Collection<E>> T addAll(@Nullable T collection,
            @Nullable Iterable<? extends E> iterable) throws UnsupportedOperationException {
        if (iterable != null) {
            return addAll(collection, iterable.iterator());
        }
        return collection;
    }

    /**
     * <p>
     * Adds given stream into given collection then returns given collection. If given collection is null, return null.
     * </p>
     *
     * @param <E>
     *            type of element
     * @param <T>
     *            type of collection
     * @param collection
     *            given collection
     * @param stream
     *            given stream
     * @return given collection after adding
     * @throws UnsupportedOperationException
     *             if write operation of given collection is unsupported
     * @since 0.0.0
     */
    public static @Nullable <E, T extends Collection<E>> T addAll(@Nullable T collection,
            @Nullable Stream<? extends E> stream) throws UnsupportedOperationException {
        if (collection != null && stream != null) {
            stream.forEachOrdered(e -> collection.add(e));
        }
        return collection;
    }
    
    /**
     * <p>
     * Adds given array into given list at specified index then returns given collection. If given list is null,
     * return null.
     * </p>
     *
     * @param <E>
     *            type of element
     * @param list
     *            given list
     * @param array
     *            given array
     * @return given list after adding
     * @throws UnsupportedOperationException
     *             if write operation of given list is unsupported
     * @since 0.0.0
     */
    public static @Nullable <E> List<? super E> addAll(@Nullable List<? super E> list, int index,
            @Nullable E[] array) throws UnsupportedOperationException {
        if (list != null && array != null) {
            list.addAll(index, Arrays.asList(array));
        }
        return list;
    }

    /**
     * <p>
     * Adds given iterator into given list at specified index then returns given collection. If given list is null,
     * return null.
     * </p>
     *
     * @param <E>
     *            type of element
     * @param list
     *            given list
     * @param iterator
     *            given iterator
     * @return given list after adding
     * @throws UnsupportedOperationException
     *             if write operation of given list is unsupported
     * @since 0.0.0
     */
    public static @Nullable <E> List<E> addAll(@Nullable List<E> list, int index,
            @Nullable Iterator<? extends E> iterator) throws UnsupportedOperationException {
        if (list != null) {
            //new HashS
            Collections.
            list.addAll(index, Quicker.iterable(iterator));
        }
        return list;
    }

    /**
     * <p>
     * Adds given iterable into given list at specified index then returns given collection. If given list is null,
     * return null.
     * </p>
     *
     * @param <E>
     *            type of element
     * @param list
     *            given list
     * @param iterable
     *            given iterable
     * @return given list after adding
     * @throws UnsupportedOperationException
     *             if write operation of given list is unsupported
     * @since 0.0.0
     */
    public static @Nullable <E> List<? super E> addAll(@Nullable List<? super E> list, int index,
            @Nullable Iterable<? extends E> iterable) throws UnsupportedOperationException {
        if (list != null && Checker.isNotEmpty(iterable)) {
            list.addAll(index, Arrays.asList(iterable));
        }
        return list;
    }

    /**
     * <p>
     * Adds given stream into given list at specified index then returns given collection. If given list is null, return
     * null.
     * </p>
     *
     * @param <E>
     *            type of element
     * @param list
     *            given list
     * @param stream
     *            given stream
     * @return given list after adding
     * @throws UnsupportedOperationException
     *             if write operation of given list is unsupported
     * @since 0.0.0
     */
    public static @Nullable <E> List<? super E> addAll(@Nullable List<? super E> list, int index,
            @Nullable Stream<? extends E> stream) throws UnsupportedOperationException {
        if (list != null && Checker.isNotEmpty(stream)) {
            list.addAll(index, Arrays.asList(stream));
        }
        return list;
    }

    /**
     * <p>
     * Adds given array into given list at specified index then returns given collection. If given list is null, return
     * null.
     * </p>
     *
     * @param <E>
     *            type of element
     * @param list
     *            given list
     * @param array
     *            given array
     * @return given list after adding
     * @throws UnsupportedOperationException
     *             if write operation of given list is unsupported
     * @since 0.0.0
     */
    public static @Nullable <E> List<? super E> addAll(@Nullable List<? super E> list, int index, @Nullable E array)
            throws UnsupportedOperationException {
        if (list != null && Checker.isNotEmpty(array)) {
            list.addAll(index, Arrays.asList(array));
        }
        return list;
    }

    /**
     * <p>
     * Adds given element into given collection specified times then returns given collection. If given collection is
     * null, return null. If the number of times is negative, the action will be performed infinitely.
     * </p>
     *
     * @param <E>
     *            type of element
     * @param <T>
     *            type of collection
     * @param collection
     *            given collection
     * @param element
     *            given element
     * @param times
     *            number of specified times
     * @return given collection after adding
     * @throws UnsupportedOperationException
     *             if write operation of given collection is unsupported
     * @since 0.0.0
     */
    public static @Nullable <E, T extends Collection<? super E>> T addAll(@Nullable T collection, @Nullable E element,
            int times) throws UnsupportedOperationException {
        if (collection != null) {
            return addAll(collection, multipleSingletonElementList(element, times));
        }
        return collection;
    }

    /**
     * <p>
     * Removes duplications in given array to make sure each element is only, then returns array after removing. This
     * method guarantees the index order of elements is same as original. If given array is null, return null.
     * </p>
     * 
     * @param <E>
     *            type of element
     * @param array
     *            given array
     * @return new array after this operation
     * @since 0.0.0
     */
    public static @Nullable <E> E[] distinct(@Nullable E[] array) {
        if (array == null) {
            return null;
        }
        List<E> dis = distinct(addAll(new ArrayList<>(), array));
        @SuppressWarnings("unchecked")
        E[] newIs = (E[])Array.newInstance(array.getClass().getComponentType(), dis.size());
        return dis.toArray(newIs);
    }

    /**
     * <p>
     * Removes duplications in given collection to make sure each element is only, then returns given collection. This
     * method doesn't guarantee the order of elements is same as original. If given collection is null, return null.
     * </p>
     * 
     * @param <E>
     *            type of element
     * @param <T>
     *            type of collection
     * @param collection
     *            given collection
     * @return given collection after this operation
     * @throws UnsupportedOperationException
     *             if write operation of given collection is unsupported
     * @since 0.0.0
     */
    public static @Nullable <E, T extends Collection<E>> T distinct(@Nullable T collection)
            throws UnsupportedOperationException {
        if (collection != null) {
            Set<E> set = new HashSet<>();
            set.addAll(collection);
            collection.clear();
            set.forEach(e -> {
                if (!collection.contains(e)) {
                    collection.add(e);
                }
            });
        }
        return collection;
    }

    /**
     * <p>
     * Removes duplications in given list to make sure each element is only, then returns given list. This method
     * guarantees the index order of elements is same as original. If given list is null, return null.
     * </p>
     * 
     * @param <E>
     *            type of element
     * @param list
     *            given list
     * @return given list after this operation
     * @throws UnsupportedOperationException
     *             if write operation of given list is unsupported
     * @since 0.0.0
     */
    public static @Nullable <E> List<E> distinct(@Nullable List<E> list) throws UnsupportedOperationException {
        if (list != null) {
            List<E> tmp = addAll(new LinkedList<>(), list);
            list.clear();
            tmp.forEach(e -> {
                if (!list.contains(e)) {
                    list.add(e);
                }
            });
        }
        return list;
    }

    /**
     * <p>
     * Adds distinct elements of given array into given collection then returns given collection. That's means each
     * added element should be distinct from any other element in finally collection. If given collection is null,
     * return null.
     * </p>
     *
     * @param <E>
     *            type of element
     * @param <T>
     *            type of collection
     * @param collection
     *            given collection
     * @param array
     *            given array
     * @return given collection after adding
     * @throws UnsupportedOperationException
     *             if write operation of given collection is unsupported
     * @since 0.0.0
     */
    public static @Nullable <E, T extends Collection<E>> T addDistinct(@Nullable T collection, @Nullable E[] array)
            throws UnsupportedOperationException {
        if (collection != null && Checker.isNotEmpty(array)) {
            Quicker.each(array, e -> {
                if (!collection.contains(e)) {
                    collection.add(e);
                }
            });
        }
        return collection;
    }

    /**
     * <p>
     * Adds distinct elements of given iterator into given collection then returns given collection. That's means each
     * added element should be distinct from any other element in finally collection. If given collection is null,
     * return null.
     * </p>
     *
     * @param <E>
     *            type of element
     * @param <T>
     *            type of collection
     * @param collection
     *            given collection
     * @param iterator
     *            given iterator
     * @return given collection after adding
     * @throws UnsupportedOperationException
     *             if write operation of given collection is unsupported
     * @since 0.0.0
     */
    public static @Nullable <E, T extends Collection<E>> T addDistinct(@Nullable T collection,
            @Nullable Iterator<? extends E> iterator) throws UnsupportedOperationException {
        if (collection != null) {
            Quicker.each(iterator, e -> {
                if (!collection.contains(e)) {
                    collection.add(e);
                }
            });
        }
        return collection;
    }

    /**
     * <p>
     * Adds distinct elements of given iterable into given collection then returns given collection. That's means each
     * added element should be distinct from any other element in finally collection. If given collection is null,
     * return null.
     * </p>
     *
     * @param <E>
     *            type of element
     * @param <T>
     *            type of collection
     * @param collection
     *            given collection
     * @param iterable
     *            given iterable
     * @return given collection after adding
     * @throws UnsupportedOperationException
     *             if write operation of given collection is unsupported
     * @since 0.0.0
     */
    public static @Nullable <E, T extends Collection<E>> T addDistinct(@Nullable T collection,
            @Nullable Iterable<? extends E> iterable) throws UnsupportedOperationException {
        if (iterable != null) {
            return addDistinct(collection, iterable.iterator());
        }
        return collection;
    }

    /**
     * <p>
     * Adds distinct elements of given stream into given collection then returns given collection. That's means each
     * added element should be distinct from any other element in finally collection. If given collection is null,
     * return null.
     * </p>
     *
     * @param <E>
     *            type of element
     * @param <T>
     *            type of collection
     * @param collection
     *            given collection
     * @param stream
     *            given stream
     * @return given collection after adding
     * @throws UnsupportedOperationException
     *             if write operation of given collection is unsupported
     * @since 0.0.0
     */
    public static @Nullable <E, T extends Collection<E>> T addDistinct(@Nullable T collection,
            @Nullable Stream<? extends E> stream) throws UnsupportedOperationException {
        if (stream != null) {
            return addDistinct(collection, stream.iterator());
        }
        return collection;
    }

    /**
     * <p>
     * Converts given list to an array and returns. If given list is null, return an empty object array. If given list
     * is null, return an empty array.
     * </p>
     *
     * @param <E>
     *            component type of list
     * @param list
     *            given list
     * @param type
     *            component type of given list
     * @return an array converted by given list
     * @throws NullPointerException
     *             if given type is null
     * @since 0.0.0
     */
    public static <E> E[] asArray(@Nullable List<? extends E> list, Class<E> type) throws NullPointerException {
        Checker.checkNull(type);
        if (Checker.isEmpty(list)) {
            @SuppressWarnings("unchecked")
            E[] array = (E[])Array.newInstance(type, 0);
            return array;
        }
        @SuppressWarnings("unchecked")
        E[] array = (E[])Array.newInstance(type, list.size());
        list.toArray(array);
        return array;
    }

    /**
     * <p>
     * Returns number of element-pair which at same index of given two arrays, using {@linkplain Object#equals(Object)}
     * method. If two elements are both null, it will be seen as a same pair.
     * </p>
     *
     * @param a1
     *            first given array
     * @param a2
     *            second given array
     * @return number of element-pair which at same index of given two arrays
     * @since 0.0.0
     */
    public static int similarity(@Nullable Object[] a1, @Nullable Object[] a2) {
        if (a1 == null || a2 == null) {
            return 0;
        }
        int length = Math.min(a1.length, a2.length);
        int result = 0;
        for (int i = 0; i < length; i++) {
            if (Checker.isEqual(a1[i], a2[i])) {
                result++;
            }
        }
        return result;
    }

    /**
     * <p>
     * Puts elements of given array into given {@linkplain Map} and returns. Even index of array are keys, and next odd
     * index is value of the key. For example:
     *
     * <pre>
     * T[] array = {t0, t1, t2, t3...};
     * </pre>
     *
     * Puts them into map:
     *
     * <pre>
     * map.put(t0, t1);
     * map.put(t2, t3);
     * </pre>
     *
     * If length of array is odd, the last value will be seen as null:
     *
     * <pre>
     * map.put(last, null);
     * </pre>
     * </p>
     * <p>
     * If given map is null, return null.
     * </p>
     *
     * @param <T>
     *            component type of array and map
     * @param map
     *            given map
     * @param array
     *            given array
     * @return given map after putting
     * @throws UnsupportedOperationException
     *             if write operation of given map is unsupported
     * @since 0.0.0
     */
    public static @Nullable <T> Map<T, T> putMap(@Nullable Map<T, T> map, @Nullable T[] array)
            throws UnsupportedOperationException {
        if (map != null && Checker.isNotEmpty(array)) {
            for (int i = 0; i < array.length / 2; i++) {
                int index = i * 2;
                map.put(array[index], array[index + 1]);
            }
            if (MathQuicker.isOdd(array.length)) {
                map.put(array[array.length - 1], null);
            }
        }
        return map;
    }

    /**
     * <p>
     * Converts given original array into new array of given new type. Each element of original array will be converted
     * by given converter and put into returned new array at corresponding index.
     * </p>
     * <p>
     * If given original array is null, the converter will be ignored and an empty array returned.
     * </p>
     *
     * @param <T>
     *            type of elements of given array
     * @param <R>
     *            type of elements of result array
     * @param original
     *            given original array
     * @param newType
     *            given new type
     * @param converter
     *            given converter
     * @return a new array of which elements are converted by given array
     * @throws NullPointerException
     *             if new type is null or converter is null when it is needed
     * @since 0.0.0
     */
    public static <T, R> R[] convert(@Nullable T[] original, Class<?> newType,
            @Nullable Function<? super T, ? extends R> converter) throws NullPointerException {
        Checker.checkNull(newType);
        if (Checker.isEmpty(original)) {
            @SuppressWarnings("unchecked")
            R[] ret = (R[])Array.newInstance(newType, 0);
            return ret;
        }
        @SuppressWarnings("unchecked")
        R[] ret = (R[])Array.newInstance(newType, original.length);
        Quicker.each(original, (i, t) -> {
            ret[(int)i] = converter.apply(t);
        });
        return ret;
    }

    /**
     * <p>
     * Converts each elements of given original collection, and add the elements after converting into given result
     * collection. If given result collection or orignal collection is null, return null.
     * </p>
     * 
     * @param <R>
     *            type of result element
     * @param <CR>
     *            type of result collection
     * @param <T>
     *            type of original element
     * @param <CT>
     *            type of original collection
     * @param result
     *            given result collection
     * @param original
     *            given original collection
     * @param converter
     *            given converter
     * @return given result collection after converting and adding
     * @throws NullPointerException
     *             if given converter is null when it is needed
     * @throws UnsupportedOperationException
     *             if write operation of given result collection is unsupported
     * @since 0.0.0
     */
    public static <T, R, CT extends Collection<T>, CR extends Collection<R>> CR convert(@Nullable CR result,
            @Nullable CT original, Function<? super T, ? extends R> converter)
            throws NullPointerException, UnsupportedOperationException {
        if (result != null && Checker.isNotEmpty(original)) {
            Checker.checkNull(converter);
            original.forEach(e -> {
                result.add(converter.apply(e));
            });
        }
        return result;
    }

    /**
     * <p>
     * Maps given array into given map. Elements of given array will be keys of given map, and the values will be
     * generated by given converter, which uses the keys to convert. If given map is null, return null.
     * </p>
     *
     * @param <T>
     *            type of keys of given map
     * @param <U>
     *            type of values of given map
     * @param map
     *            given map
     * @param array
     *            given array
     * @param converter
     *            given converter
     * @return given map after putting
     * @throws NullPointerException
     *             if given converter is null when it is needed
     * @throws UnsupportedOperationException
     *             if write operation of given map is unsupported
     * @since 0.0.0
     */
    public static <T, U> Map<T, U> map(@Nullable Map<T, U> map, @Nullable T[] array,
            @Nullable Function<? super T, ? extends U> converter)
            throws NullPointerException, UnsupportedOperationException {
        if (map != null && Checker.isNotEmpty(array)) {
            Checker.checkNull(converter);
            for (int i = 0; i < array.length; i++) {
                map.put(array[i], converter.apply(array[i]));
            }
        }
        return map;
    }

    /**
     * <p>
     * Returns sum length of given arrays. Null array will be seen as an empty array. If number of sum is greater than
     * {@linkplain Integer#MAX_VALUE}, return {@linkplain Integer#MAX_VALUE}.
     * </p>
     *
     * @param arrays
     *            given arrays
     * @return sum length of given arrays
     * @throws IllegalArgumentException
     *             if there exists non-array object in given arrays
     * @since 0.0.0
     */
    public static int sumLength(@Nullable Object... arrays) throws IllegalArgumentException {
        if (Checker.isNotEmpty(arrays)) {
            long l = 0;
            for (int i = 0; i < arrays.length; i++) {
                if (arrays[i] != null) {
                    l += Array.getLength(arrays[i]);
                }
            }
            return l > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int)l;
        }
        return 0;
    }

    /**
     * <p>
     * Concats given arrays into a new array and returns. The specified type of returned array should be compatible. If
     * given arrays is null, return a new empty Object[].
     * </p>
     *
     * @param <E>
     *            specified type of returned array
     * @param type
     *            specified type of returned array
     * @param arrays
     *            given arrays
     * @return an array concats given arrays
     * @throws IllegalArgumentException
     *             if there exists non-array object in given arrays
     * @throws ClassCastException
     *             if type of returned array is incompatible
     * @since 0.0.0
     */
    @SuppressWarnings("unchecked")
    public static <E> E[] concat(Class<?> type, @Nullable Object... arrays)
            throws IllegalArgumentException, ClassCastException {
        if (arrays == null) {
            return (E[])Array.newInstance(Object.class, 0);
        }
        E[] result = (E[])Array.newInstance(type, sumLength(arrays));
        if (result.length != 0) {
            int[] index = {0};
            Quicker.each(arrays, a -> {
                if (a != null) {
                    if (a.getClass().isArray()) {
                        Quicker.each(asObjectArray(a), o -> {
                            E e = (E)o;
                            result[index[0]++] = e;
                        });
                    } else {
                        throw new IllegalArgumentException("There exists non-array object in given arrays.");
                    }
                }
            });
        }
        return result;
    }

    /**
     * <p>
     * Concats given arrays into a new array and returns, but each element should be distinct in returned array. The
     * specified type of returned array should be compatible. If given arrays is null, return a new empty Object[].
     * </p>
     *
     * @param <E>
     *            specified type of returned array
     * @param type
     *            specified type of returned array
     * @param arrays
     *            given arrays
     * @return an array concats given arrays
     * @throws IllegalArgumentException
     *             if there exists non-array object in given arrays
     * @throws ClassCastException
     *             if type of returned array is incompatible
     * @since 0.0.0
     */
    public static <E> E[] concatDistinct(Class<?> type, @Nullable Object... arrays)
            throws IllegalArgumentException, ClassCastException {
        if (Checker.isEmpty(arrays)) {
            @SuppressWarnings("unchecked")
            E[] result = (E[])Array.newInstance(Object.class, 0);
            return result;
        }
        @SuppressWarnings("unchecked")
        E[] resultType = (E[])Array.newInstance(type, 0);
        List<Object> result = new LinkedList<>();
        Quicker.each(arrays.length, array -> {
            addDistinct(result, asObjectArray(array));
        });
        return result.toArray(resultType);
    }

    /**
     * <p>
     * Converts primitive array into object[]. If given array is not a primitive array, return itself. If given array is
     * null, return a new empty Object[].
     * </p>
     *
     * @param array
     *            given primitive array
     * @return object[] converted by given primitive array
     * @throws IllegalArgumentException
     *             if given object is not an array
     * @since 0.0.0
     */
    public static Object[] asObjectArray(@Nullable Object array) throws IllegalArgumentException {
        if (array == null) {
            return new Object[0];
        }
        Checker.checkArray(array);
        if (array.getClass().getComponentType().isPrimitive()) {
            Object[] result = new Object[Array.getLength(array)];
            for (int i = 0; i < result.length; i++) {
                result[i] = Array.get(array, i);
            }
            return result;
        } else {
            return (Object[])array;
        }
    }

    /**
     * <p>
     * Returns a multiple-singleton-element list. This list has only one actual element which is given, but it simulate
     * a sized-list just like it has many duplicates of given element. That means, if one element of that list is
     * changed, all "duplicates" will be changed immediately.
     * </p>
     * 
     * @param <E>
     *            type of list element
     * @param element
     *            given element
     * @param size
     *            given size, >= 0
     * @return a multiple-singleton-element list
     * @throws IllegalArgumentException
     *             if given size is negative
     * @since 0.0.0
     */
    public static <E> List<E> multipleSingletonElementList(@Nullable E element, int size)
            throws IllegalArgumentException {
        Checker.checkPositiveOr0(size);
        return new MultipleSingletonElementList<E>(element, size);
    }

    static class MultipleSingletonElementList<E> implements List<E>, Serializable {

        private static final long serialVersionUID = 1L;

        private @Nullable E element;

        private int size;

        MultipleSingletonElementList(@Nullable E element, int size) {
            this.element = element;
            this.size = size;
        }

        @Override
        public int size() {
            return size;
        }

        @Override
        public boolean isEmpty() {
            return size() == 0;
        }

        @Override
        public boolean contains(Object o) {
            return !isEmpty() && Checker.isEqual(element, o);
        }

        @Override
        public Iterator<E> iterator() {
            return new Iterator<E>() {

                private int s = size();

                @Override
                public boolean hasNext() {
                    return s > 0;
                }

                @Override
                public E next() {
                    if (hasNext()) {
                        s--;
                        return element;
                    } else {
                        throw new NoSuchElementException();
                    }
                }
            };
        }

        @Override
        public Object[] toArray() {
            Object[] ret = new Object[size()];
            Arrays.fill(ret, element);
            return ret;
        }

        @Override
        public <T> T[] toArray(T[] a) {
            Checker.checkNull(a);
            if (a.length < size) {
                @SuppressWarnings("unchecked")
                T[] array = (T[])Array.newInstance(a.getClass().getComponentType(), size());
                Arrays.fill(array, element);
                return array;
            }
            Arrays.fill(a, 0, size(), element);
            if (a.length > size)
                a[size] = null;
            return a;
        }

        @Override
        public boolean add(E e) {
            Checker.checkEqual(element, e);
            if (size + 1 > 0) {
                size++;
                return true;
            }
            return false;
        }

        @Override
        public boolean remove(Object o) {
            if (!isEmpty() && Checker.isEqual(element, o)) {
                size--;
                return true;
            }
            return false;
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            Checker.checkNull(c);
            for (Object o : c) {
                if (Checker.isNotEqual(element, o)) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public boolean addAll(Collection<? extends E> c) {
            Checker.checkNull(c);
            if (!containsAll(c)) {
                throw new IllegalArgumentException("Exists unequal element in given collection.");
            }
            if (size + c.size() >= 0) {
                size += c.size();
                return true;
            }
            return false;
        }

        @Override
        public boolean addAll(int index, Collection<? extends E> c) {
            Checker.checkIndex(index, size());
            return addAll(c);
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            Checker.checkNull(c);
            for (Object o : c) {
                if (Checker.isEqual(element, o)) {
                    size = 0;
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            Checker.checkNull(c);
            for (Object o : c) {
                if (Checker.isEqual(element, o)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public void clear() {
            element = null;
            size = 0;
        }

        @Override
        public E get(int index) {
            Checker.checkIndex(index, size());
            return element;
        }

        @Override
        public E set(int index, E element) {
            Checker.checkIndex(index, size());
            Checker.checkEqual(this.element, element);
            return element;
        }

        @Override
        public void add(int index, E element) {
            set(index, element);
            size++;
        }

        @Override
        public E remove(int index) {
            Checker.checkIndex(index, size());
            size--;
            return element;
        }

        @Override
        public int indexOf(Object o) {
            if (contains(o)) {
                return 0;
            }
            return -1;
        }

        @Override
        public int lastIndexOf(Object o) {
            if (contains(o)) {
                return size() - 1;
            }
            return -1;
        }

        @Override
        public ListIterator<E> listIterator() {
            return listIterator(0);
        }

        @Override
        public ListIterator<E> listIterator(int index) {
            Checker.checkIndex(index, size());
            return new ListIterator<E>() {

                private int last = size() - 1 - index;

                private int cur = 0;

                @Override
                public boolean hasNext() {
                    return cur <= last;
                }

                @Override
                public E next() {
                    if (hasNext()) {
                        cur++;
                        return element;
                    } else {
                        throw new NoSuchElementException();
                    }
                }

                @Override
                public boolean hasPrevious() {
                    return cur > 0;
                }

                @Override
                public E previous() {
                    if (hasPrevious()) {
                        cur--;
                        return element;
                    } else {
                        throw new NoSuchElementException();
                    }
                }

                @Override
                public int nextIndex() {
                    return cur;
                }

                @Override
                public int previousIndex() {
                    return cur - 1;
                }

                @Override
                public void remove() {
                    throw new UnsupportedOperationException();
                }

                @Override
                public void set(E e) {
                    throw new UnsupportedOperationException();
                }

                @Override
                public void add(E e) {
                    throw new UnsupportedOperationException();
                }
            };
        }

        @Override
        public List<E> subList(int fromIndex, int toIndex) {
            Checker.checkRangeIndexes(fromIndex, toIndex, size());
            return new MultipleSingletonElementList<E>(element, toIndex - fromIndex);
        }
    }

    /**
     * <p>
     * Returns a map only owns at most one value, and any key (null inclusive) can get this value. If given value is
     * null, return an empty this type of map; else returned map have given value.
     * </p>
     * 
     * @param <V>
     *            type of map values
     * @param value
     *            given value
     * @return a map only owns at most one value
     * @since 0.0.0
     */
    public static <V> Map<?, V> multipleSingletonValueMap(@Nullable V value) {
        return new MultipleSingletonValueMap<>(value);
    }

    static class MultipleSingletonValueMap<V> implements Map<Object, V> {

        private V value = null;

        private boolean empty = true;

        MultipleSingletonValueMap(@Nullable V value) {
            this.value = value;
            this.empty = null == value;
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public boolean isEmpty() {
            return empty;
        }

        @Override
        public boolean containsKey(Object key) {
            return true;
        }

        @Override
        public boolean containsValue(Object value) {
            return Checker.isEqual(value, this.value);
        }

        @Override
        public V get(Object key) {
            return value;
        }

        @Override
        public V put(Object key, V value) {
            V old = isEmpty() ? null : this.value;
            this.value = value;
            empty = false;
            return old;
        }

        @Override
        public V remove(Object key) {
            V old = isEmpty() ? null : this.value;
            empty = true;
            return old;
        }

        @Override
        public void putAll(Map<? extends Object, ? extends V> m) {
            Checker.checkNull(m);
            if (m.size() > 1) {
                throw new UnsupportedOperationException("This map only put one value.");
            } else if (m.size() == 1) {
                m.forEach((k, v) -> {
                    put(k, v);
                });
            }
        }

        @Override
        public void clear() {
            empty = true;
            value = null;
        }

        @Override
        public Set<Object> keySet() {
            throw new UnsupportedOperationException("This map doesn't has any key.");
        }

        @Override
        public Collection<V> values() {
            return Collections.singleton(value);
        }

        @Override
        public Set<java.util.Map.Entry<Object, V>> entrySet() {
            throw new UnsupportedOperationException("This map doesn't has any key.");
        }
    }

    /**
     * <p>
     * Returns a map has keys but every keys are associated with only one value.
     * </p>
     * 
     * @param <K>
     *            type of map keys
     * @param <V>
     *            type of map values
     * @return a map has keys but every keys are associated with only one value
     * @since 0.0.0
     */
    public static <K, V> Map<K, V> multiKeysSingletonValueMap() {
        return new MultiKeysSingletonValueMap<>(null, null);
    }

    /**
     * <p>
     * Returns a map has specified keys but every keys are associated with only one value. Specified key set will be
     * reflected if returned map changed and vice-versa.
     * </p>
     * 
     * @param <K>
     *            type of map keys
     * @param <V>
     *            type of map value
     * @param keySet
     *            specified keys
     * @param value
     *            initial value
     * @return a map has keys but every keys are associated with only one value
     * @since 0.0.0
     */
    public static <K, V> Map<K, V> multiKeysSingletonValueMap(@Nullable Set<K> keySet, @Nullable V value) {
        return new MultiKeysSingletonValueMap<>(keySet, value);
    }

    static class MultiKeysSingletonValueMap<K, V> implements Map<K, V> {

        private Set<K> keySet;

        private V value;

        MultiKeysSingletonValueMap(Set<K> keySet, V value) {
            this.keySet = keySet;
            this.value = value;
        }

        private Set<K> forceKeySet() {
            return null == keySet ? Collections.emptySet() : keySet;
        }

        @Override
        public int size() {
            return forceKeySet().size();
        }

        @Override
        public boolean isEmpty() {
            return forceKeySet().isEmpty();
        }

        @Override
        public boolean containsKey(Object key) {
            return forceKeySet().contains(key);
        }

        @Override
        public boolean containsValue(Object value) {
            return Checker.isEqual(this.value, value);
        }

        @Override
        public V get(Object key) {
            return containsKey(key) ? value : null;
        }

        @Override
        public V put(K key, V value) {
            V old = this.value;
            if (null == keySet) {
                keySet = new HashSet<>();
            }
            keySet.add(key);
            this.value = value;
            return old;
        }

        @Override
        public V remove(Object key) {
            if (containsKey(key)) {
                keySet.remove(key);
                return value;
            }
            return value;
        }

        @Override
        public void putAll(Map<? extends K, ? extends V> m) {
            Checker.checkNull(m);
            m.forEach((k, v) -> {
                put(k, v);
            });
        }

        @Override
        public void clear() {
            if (null != keySet) {
                keySet.clear();
            }
            value = null;
        }

        @Override
        public Set<K> keySet() {
            if (null == keySet) {
                keySet = new HashSet<>();
            }
            return keySet;
        }

        @Override
        public Collection<V> values() {
            return Collections.singleton(value);
        }

        @Override
        public Set<java.util.Map.Entry<K, V>> entrySet() {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * <p>
     * Returns a size-limited collection backed by given collection. If specified limited size (max permitted size) is
     * negative, it will return original given collection. If given collection is serializable, returned collection is
     * also serializable.
     * </p>
     * <p>
     * Returned collection will throw an {@linkplain OutOfBoundsException} if size of it out of the limit.
     * </p>
     * 
     * @param <E>
     *            component type
     * @param collection
     *            given collection
     * @param limit
     *            max permitted size
     * @return a size-limited collection or given collection itself if limited size is negative
     * @throws NullPointerException
     *             if given collection is null
     * @since 0.0.0
     */
    public static <E> Collection<E> sizeLimitedCollection(Collection<E> collection, int limit)
            throws NullPointerException {
        Checker.checkNull(collection);
        return limit >= 0 ? new SizeLimitedCollection<E>(collection, limit) : collection;
    }

    /**
     * <p>
     * Returns a size-limited set backed by given set. If specified limited size (max permitted size) is negative, it
     * will return original given set. If given set is serializable, returned set is also serializable.
     * </p>
     * <p>
     * Returned set will throw an {@linkplain OutOfBoundsException} if size of it out of the limit.
     * </p>
     * 
     * @param <E>
     *            component type
     * @param set
     *            given set
     * @param limit
     *            max permitted size
     * @return a size-limited set or given set itself if limited size is negative
     * @throws NullPointerException
     *             if given set is null
     * @since 0.0.0
     */
    public static <E> Set<E> sizeLimitedSet(Set<E> set, int limit) throws NullPointerException {
        Checker.checkNull(set);
        return limit >= 0 ? new SizeLimitedSet<E>(set, limit) : set;
    }

    /**
     * <p>
     * Returns a size-limited list backed by given list. If specified limited size (max permitted size) is negative, it
     * will return original given list. If given list is serializable, returned list is also serializable. Similarly if
     * given list implements {@linkplain RandomAccess}, returned list is also implements.
     * </p>
     * <p>
     * Returned list will throw an {@linkplain OutOfBoundsException} if size of it out of the limit.
     * </p>
     * 
     * @param <E>
     *            component type
     * @param list
     *            given list
     * @param limit
     *            max permitted size
     * @return a size-limited list or given list itself if limited size is negative
     * @throws NullPointerException
     *             if given list is null
     * @since 0.0.0
     */
    public static <E> List<E> sizeLimitedList(List<E> list, int limit) throws NullPointerException {
        Checker.checkNull(list);
        return limit >= 0 ? (list instanceof RandomAccess ? new RandomAccessSizeLimitedList<>(list, limit)
                : new SizeLimitedList<>(list, limit)) : list;
    }

    /**
     * <p>
     * Returns a size-limited map backed by given map. If specified limited size (max permitted size) is negative, it
     * will return original given map. If given map is serializable, returned map is also serializable.
     * </p>
     * <p>
     * Returned map will throw an {@linkplain OutOfBoundsException} if size of it out of the limit.
     * </p>
     * 
     * @param <K>
     *            type of map key
     * @param <V>
     *            type of map value
     * @param map
     *            given map
     * @param limit
     *            max permitted size
     * @return a size-limited map or given map itself if limited size is negative
     * @throws NullPointerException
     *             if given map is null
     * @since 0.0.0
     */
    public static <K, V> Map<K, V> sizeLimitedMap(Map<K, V> map, int limit) throws NullPointerException {
        Checker.checkNull(map);
        return limit >= 0 ? new SizeLimitedMap<K, V>(map, limit) : map;
    }

    static class SourceWrapper<S> implements Serializable {

        private static final long serialVersionUID = 1L;

        protected final S source;

        SourceWrapper(S source) {
            this.source = source;
        }
    }

    static class SizeLimited<S> extends SourceWrapper<S> {

        private static final long serialVersionUID = 1L;

        protected final int limit;

        SizeLimited(S source, int limit) {
            super(source);
            this.limit = limit;
        }

        protected void checkLimit(int total) {
            if (total > limit || total < 0) {
                throw new OutOfBoundsException("Number of element out of limit: " + total + ".");
            }
        }

        @Override
        public boolean equals(Object o) {
            return source == o || source.equals(o);
        }

        @Override
        public int hashCode() {
            return source.hashCode();
        }
    }

    static class SizeLimitedContainer<E, S extends Collection<E>> extends SizeLimited<S> implements Collection<E> {

        private static final long serialVersionUID = 1L;

        SizeLimitedContainer(S source, int limit) {
            super(source, limit);
        }

        @Override
        public int size() {
            return source.size();
        }

        @Override
        public boolean isEmpty() {
            return source.isEmpty();
        }

        @Override
        public boolean contains(Object o) {
            return source.contains(o);
        }

        @Override
        public Iterator<E> iterator() {
            return source.iterator();
        }

        @Override
        public Object[] toArray() {
            return source.toArray();
        }

        @Override
        public <T> T[] toArray(T[] a) {
            return source.toArray(a);
        }

        @Override
        public boolean add(E e) {
            boolean ret = source.add(e);
            if (size() > limit) {
                remove(e);
                checkLimit(size());
            }
            return ret;
        }

        @Override
        public boolean addAll(Collection<? extends E> c) {
            boolean ret = false;
            for (E e : c) {
                ret |= add(e);
            }
            return ret;
        }

        @Override
        public boolean remove(Object o) {
            return source.remove(o);
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            return source.containsAll(c);
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            return source.removeAll(c);
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            return source.retainAll(c);
        }

        @Override
        public void clear() {
            source.clear();
        }
    }

    static class SizeLimitedCollection<E> extends SizeLimitedContainer<E, Collection<E>> implements Collection<E> {

        private static final long serialVersionUID = 1L;

        SizeLimitedCollection(Collection<E> source, int limit) {
            super(source, limit);
        }
    }

    static class SizeLimitedSet<E> extends SizeLimitedContainer<E, Set<E>> implements Set<E> {

        private static final long serialVersionUID = 1L;

        SizeLimitedSet(Set<E> source, int limit) {
            super(source, limit);
        }

        @Override
        public boolean add(E e) {
            if (!contains(e)) {
                checkLimit(size() + 1);
            }
            return source.add(e);
        }
    }

    static class SizeLimitedList<E> extends SizeLimitedContainer<E, List<E>> implements List<E> {

        private static final long serialVersionUID = 1L;

        SizeLimitedList(List<E> source, int limit) {
            super(source, limit);
        }

        @Override
        public boolean addAll(int index, Collection<? extends E> c) {
            checkLimit(size() + c.size());
            return source.addAll(index, c);
        }

        @Override
        public E get(int index) {
            return source.get(index);
        }

        @Override
        public E set(int index, E element) {
            return source.set(index, element);
        }

        @Override
        public void add(int index, E element) {
            checkLimit(size() + 1);
            source.add(index, element);
        }

        @Override
        public E remove(int index) {
            return source.remove(index);
        }

        @Override
        public int indexOf(Object o) {
            return source.indexOf(o);
        }

        @Override
        public int lastIndexOf(Object o) {
            return source.lastIndexOf(o);
        }

        @Override
        public ListIterator<E> listIterator() {
            return source.listIterator();
        }

        @Override
        public ListIterator<E> listIterator(int index) {
            return source.listIterator(index);
        }

        @Override
        public List<E> subList(int fromIndex, int toIndex) {
            return sizeLimitedList(source.subList(fromIndex, toIndex), limit);
        }

        @Override
        public boolean add(E e) {
            checkLimit(size() + 1);
            return source.add(e);
        }

        @Override
        public boolean addAll(Collection<? extends E> c) {
            checkLimit(size() + c.size());
            return source.addAll(c);
        }
    }

    static class RandomAccessSizeLimitedList<E> extends SizeLimitedList<E> implements RandomAccess {

        private static final long serialVersionUID = 1L;

        RandomAccessSizeLimitedList(List<E> source, int limit) {
            super(source, limit);
        }

    }

    static class SizeLimitedMap<K, V> extends SizeLimited<Map<K, V>> implements Map<K, V> {

        private static final long serialVersionUID = 1L;

        SizeLimitedMap(Map<K, V> source, int limit) {
            super(source, limit);
        }

        @Override
        public int size() {
            return source.size();
        }

        @Override
        public boolean isEmpty() {
            return source.isEmpty();
        }

        @Override
        public boolean containsKey(Object key) {
            return source.containsKey(key);
        }

        @Override
        public boolean containsValue(Object value) {
            return source.containsValue(value);
        }

        @Override
        public V get(Object key) {
            return source.get(key);
        }

        @Override
        public V put(K key, V value) {
            if (!containsKey(key)) {
                checkLimit(size() + 1);
            }
            return source.put(key, value);
        }

        @Override
        public V remove(Object key) {
            return source.remove(key);
        }

        @Override
        public void putAll(Map<? extends K, ? extends V> m) {
            int[] count = {0};
            m.entrySet().forEach(e -> {
                if (!containsKey(e.getKey())) {
                    count[0]++;
                }
            });
            checkLimit(size() + count[0]);
            source.putAll(m);
        }

        @Override
        public void clear() {
            source.clear();
        }

        @Override
        public Set<K> keySet() {
            return source.keySet();
        }

        @Override
        public Collection<V> values() {
            return source.values();
        }

        @Override
        public Set<java.util.Map.Entry<K, V>> entrySet() {
            return source.entrySet();
        }
    }
}
