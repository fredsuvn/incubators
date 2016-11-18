package com.cogician.quicker.util;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import com.cogician.quicker.Checker;
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
public class QuickCollections {

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
            Quicker.flow(iterator).each(e -> {
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
            // collection.addAll(stream.collect(Collectors.toSet()));
            stream.forEachOrdered(e -> collection.add(e));
        }
        return collection;
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
    public static @Nullable <E> List<? super E> addAll(@Nullable List<? super E> list, int index, @Nullable E[] array)
            throws UnsupportedOperationException {
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
            list.addAll(index, Quicker.flow(iterator).toList());
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
    public static @Nullable <E> List<E> addAll(@Nullable List<E> list, int index,
            @Nullable Iterable<? extends E> iterable) throws UnsupportedOperationException {
        if (list != null) {
            list.addAll(index, Quicker.flow(iterable).toList());
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
    public static @Nullable <E> List<E> addAll(@Nullable List<E> list, int index, @Nullable Stream<? extends E> stream)
            throws UnsupportedOperationException {
        if (list != null) {
            list.addAll(index, Quicker.flow(stream).toList());
        }
        return list;
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
    public static @Nullable <E, T extends Collection<? super E>> T addDistinct(@Nullable T collection,
            @Nullable E[] array) throws UnsupportedOperationException {
        if (collection != null && Checker.isNotEmpty(array)) {
            Quicker.flow(array).each(e -> {
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
            Quicker.flow(iterator).each(e -> {
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
        if (collection != null && stream != null) {
            stream.forEachOrdered(e -> {
                if (!collection.contains(e)) {
                    collection.add(e);
                }
            });
        }
        return collection;
    }

    /**
     * <p>
     * Returns number of equal elements at corresponding index for given two arrays.
     * </p>
     *
     * @param list1
     *            first list
     * @param list2
     *            second list
     * @return number of equal elements at corresponding index for given two arrays
     * @since 0.0.0
     */
    public static int similarity(@Nullable List<?> list1, @Nullable List<?> list2) {
        if (Checker.isEmpty(list1) || Checker.isEmpty(list2)) {
            return 0;
        }
        return QuickArrays.similarity(list1.toArray(), list2.toArray());
    }

    /**
     * <p>
     * Returns number of equal elements at corresponding index for each given arrays.
     * </p>
     *
     * @param arrays
     *            given arrays
     * @return number of equal elements at corresponding index for each given arrays
     * @since 0.0.0
     */
    public static int similarity(@Nullable List<?>... lists) {
        if (Checker.isEmpty(lists)) {
            return 0;
        }
        List<List<?>> tmp = new ArrayList<>(Arrays.asList(lists));
        List<Object> comparator = new ArrayList<>();
        int result = 0;
        int count = 0;
        LOOP: while (tmp.size() > 1) {
            for (int i = 0; i < tmp.size();) {
                List<?> list = tmp.get(i);
                if (count >= list.size()) {
                    tmp.remove(i);
                } else {
                    comparator.add(list.get(count));
                    i++;
                }
            }
            if (comparator.size() > 1) {
                Object o = comparator.get(0);
                for (int i = 1; i < comparator.size(); i++) {
                    if (!Checker.isEqual(o, comparator.get(i))) {
                        comparator.clear();
                        continue LOOP;
                    }
                }
                result++;
                comparator.clear();
            }
        }
        return result;
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
        @SuppressWarnings("unchecked")
        E[] array = (E[])Array.newInstance(type, 0);
        Checker.checkNull(type);
        if (Checker.isEmpty(list)) {
            return array;
        }
        return list.toArray(array);
    }

    /**
     * <p>
     * Joins given iterable with specified separator.
     * </p>
     * 
     * @param iterable
     *            given iterable
     * @param separator
     *            specified separator
     * @return string after joining
     * @since 0.0.0
     */
    public static String join(Iterable<?> iterable, String separator) {
        StringBuilder sb = new StringBuilder();
        Iterator<?> iterator = iterable.iterator();
        try {
            sb.append(String.valueOf(iterator.next()));
        } catch (NoSuchElementException e) {
            return Consts.emptyString();
        }
        while (iterator.hasNext()) {
            sb.append(separator);
            sb.append(String.valueOf(iterator.next()));
        }
        return sb.toString();
    }

    /**
     * <p>
     * Joins given iterable with comma ",".
     * </p>
     * 
     * @param iterable
     *            given iterable
     * @return string after joining
     * @since 0.0.0
     */
    public static String join(Iterable<?> iterable) {
        return join(iterable, ",");
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
            if (QuickMaths.isOdd(array.length)) {
                map.put(array[array.length - 1], null);
            }
        }
        return map;
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
     * Returns a list of which elements come from specified function. Returned list is always empty.
     * </p>
     * 
     * @param<E> type
     *               of elements
     * 
     * @param function
     *            specified function
     * @return a list of which elements come from specified function
     * @since 0.0.0
     */
    public static <E> List<E> functionalList(Function<Integer, ? extends E> function) {
        return functionalList(function, 0);
    }

    /**
     * <p>
     * Returns a list of which elements come from specified function. Size of list is specified and fixed.
     * </p>
     * 
     * @param<E> type
     *               of elements
     * 
     * @param function
     *            specified function
     * @param size
     *            specified size
     * @return a list of which elements come from specified function
     * @since 0.0.0
     */
    public static <E> List<E> functionalList(Function<Integer, ? extends E> function, int size) {
        return new FunctionalList<>(function, size);
    }

    /**
     * <p>
     * Returns a map of which elements come from specified function. Returned map is always empty.
     * </p>
     * 
     * @param<E> type
     *               of elements
     * 
     * @param function
     *            specified function
     * @return a map of which elements come from specified function
     * @since 0.0.0
     */
    public static <K, V> Map<K, V> functionalMap(Function<Object, ? extends V> function) {
        return functionalMap(function, 0);
    }

    /**
     * <p>
     * Returns a map of which elements come from specified function. Size of map is specified and fixed.
     * </p>
     * 
     * @param<E> type
     *               of elements
     * 
     * @param function
     *            specified function
     * @param size
     *            specified size
     * @return a map of which elements come from specified function
     * @since 0.0.0
     */
    public static <K, V> Map<K, V> functionalMap(Function<Object, ? extends V> function, int size) {
        return new FunctionalMap<>(function, size);
    }

    /**
     * <p>
     * Returns a list refuses new null elements according to given list. Returned list extends all performance and
     * features of given list. Returned list is serializable, and if given list implements {@linkplain RandomAccess},
     * returned list still implement.
     * </p>
     * @param<E> type
     *               of elements
     * 
     * @param function
     *            specified function
     * @return a list of which elements come from specified function
     * @since 0.0.0
     */
    public static <E> List<E> elementsNonnullList(List<E> list) {
        return list instanceof RandomAccess ? new RandomAccessElementsNonnullList<>(list)
                : new ElementsNonnullList<>(list);
    }

    /**
     * <p>
     * A type of list of which elements come from a function. Size of this list is fixed.
     * </p>
     * 
     * @param <K>
     *            type of map keys
     * @param <V>
     *            type of map values
     * 
     * @author Fred Suvn
     * @version 0.0.0, 2016-11-08T10:26:56+08:00
     * @since 0.0.0, 2016-11-08T10:26:56+08:00
     */
    private static class FunctionalList<E> implements List<E> {

        private final Function<Integer, ? extends E> function;

        private final int size;

        protected FunctionalList(Function<Integer, ? extends E> function, int size) {
            this.function = function;
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
            return true;
        }

        @Override
        public Iterator<E> iterator() {
            return new FunctionalIterator();
        }

        @Override
        public Object[] toArray() {
            return new Object[0];
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> T[] toArray(T[] a) {
            return (T[])Array.newInstance(a.getClass().getComponentType(), 0);
        }

        @Override
        public boolean add(E e) {
            return true;
        }

        @Override
        public boolean remove(Object o) {
            return true;
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            return true;
        }

        @Override
        public boolean addAll(Collection<? extends E> c) {
            return true;
        }

        @Override
        public boolean addAll(int index, Collection<? extends E> c) {
            return true;
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            return true;
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            return true;
        }

        @Override
        public void clear() {
        }

        @Override
        public E get(int index) {
            return function.apply(index);
        }

        @Override
        public E set(int index, E element) {
            return null;
        }

        @Override
        public void add(int index, E element) {
        }

        @Override
        public E remove(int index) {
            return null;
        }

        @Override
        public int indexOf(Object o) {
            return 0;
        }

        @Override
        public int lastIndexOf(Object o) {
            return 0;
        }

        @Override
        public ListIterator<E> listIterator() {
            return listIterator(0);
        }

        @Override
        public ListIterator<E> listIterator(int index) {
            return new FunctionalListIterator(index);
        }

        @Override
        public List<E> subList(int fromIndex, int toIndex) {
            return this;
        }

        private class FunctionalIterator implements Iterator<E> {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public E next() {
                return get(index++);
            }

        }

        private class FunctionalListIterator implements ListIterator<E> {

            private int index;

            private FunctionalListIterator(int index) {
                this.index = index;
            }

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public E next() {
                return get(index++);
            }

            @Override
            public boolean hasPrevious() {
                return index > 1;
            }

            @Override
            public E previous() {
                return get(--index);
            }

            @Override
            public int nextIndex() {
                return index;
            }

            @Override
            public int previousIndex() {
                return index - 1;
            }

            @Override
            public void remove() {
            }

            @Override
            public void set(E e) {
            }

            @Override
            public void add(E e) {
            }

        }
    }

    /**
     * <p>
     * A type of map of which elements come from a function. Size of this map is fixed.
     * </p>
     * 
     * @param <K>
     *            type of map keys
     * @param <V>
     *            type of map values
     * @author Fred Suvn
     * @version 0.0.0, 2016-11-08T10:57:59+08:00
     * @since 0.0.0, 2016-11-08T10:57:59+08:00
     */
    private static class FunctionalMap<K, V> implements Map<K, V> {

        private final Function<Object, ? extends V> function;

        private final int size;

        protected FunctionalMap(Function<Object, ? extends V> function, int size) {
            this.function = function;
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
        public boolean containsKey(Object key) {
            return true;
        }

        @Override
        public boolean containsValue(Object value) {
            return true;
        }

        @Override
        public V get(Object key) {
            return function.apply(key);
        }

        @Override
        public V put(K key, V value) {
            return null;
        }

        @Override
        public V remove(Object key) {
            return null;
        }

        @Override
        public void putAll(Map<? extends K, ? extends V> m) {
        }

        @Override
        public void clear() {
        }

        @Override
        public Set<K> keySet() {
            return Collections.emptySet();
        }

        @Override
        public Collection<V> values() {
            return Collections.emptySet();
        }

        @Override
        public Set<java.util.Map.Entry<K, V>> entrySet() {
            return Collections.emptySet();
        }

    }

    /**
     * <p>
     * List refuses null elements.
     * </p>
     *
     * @author Fred Suvn
     * @version 0.0.0, 2016-11-09T10:41:37+08:00
     * @since 0.0.0, 2016-11-09T10:41:37+08:00
     */
    private static class ElementsNonnullList<E> implements List<E>, Serializable {

        private static final long serialVersionUID = 0L;

        private final List<E> list;

        protected ElementsNonnullList(List<E> list) {
            this.list = list;
        }

        @Override
        public void forEach(Consumer<? super E> action) {
            list.forEach(action);
        }

        @Override
        public int size() {
            return list.size();
        }

        @Override
        public boolean isEmpty() {
            return list.isEmpty();
        }

        @Override
        public boolean contains(Object o) {
            return list.contains(o);
        }

        @Override
        public Iterator<E> iterator() {
            return list.iterator();
        }

        @Override
        public Object[] toArray() {
            return list.toArray();
        }

        @Override
        public <T> T[] toArray(T[] a) {
            return list.toArray(a);
        }

        @Override
        public boolean add(E e) {
            return list.add(e);
        }

        @Override
        public boolean remove(Object o) {
            return list.remove(o);
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            return list.containsAll(c);
        }

        @Override
        public boolean addAll(Collection<? extends E> c) {
            Checker.checkNullElement(c);
            return list.addAll(c);
        }

        @Override
        public boolean addAll(int index, Collection<? extends E> c) {
            Checker.checkNullElement(c);
            return list.addAll(index, c);
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            return list.removeAll(c);
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            return list.retainAll(c);
        }

        @Override
        public void replaceAll(UnaryOperator<E> operator) {
            list.replaceAll(operator);
        }

        @Override
        public boolean removeIf(Predicate<? super E> filter) {
            return list.removeIf(filter);
        }

        @Override
        public void sort(Comparator<? super E> c) {
            list.sort(c);
        }

        @Override
        public void clear() {
            list.clear();
        }

        @Override
        public boolean equals(Object o) {
            return list.equals(o);
        }

        @Override
        public int hashCode() {
            return list.hashCode();
        }

        @Override
        public E get(int index) {
            return list.get(index);
        }

        @Override
        public E set(int index, E element) {
            Checker.checkNull(element);
            return list.set(index, element);
        }

        @Override
        public void add(int index, E element) {
            Checker.checkNull(element);
            list.add(index, element);
        }

        @Override
        public Stream<E> stream() {
            return list.stream();
        }

        @Override
        public E remove(int index) {
            return list.remove(index);
        }

        @Override
        public Stream<E> parallelStream() {
            return list.parallelStream();
        }

        @Override
        public int indexOf(Object o) {
            return list.indexOf(o);
        }

        @Override
        public int lastIndexOf(Object o) {
            return list.lastIndexOf(o);
        }

        @Override
        public ListIterator<E> listIterator() {
            return list.listIterator();
        }

        @Override
        public ListIterator<E> listIterator(int index) {
            return list.listIterator(index);
        }

        @Override
        public List<E> subList(int fromIndex, int toIndex) {
            return list.subList(fromIndex, toIndex);
        }

        @Override
        public Spliterator<E> spliterator() {
            return list.spliterator();
        }

        @Override
        public String toString() {
            return list.toString();
        }
    }

    /**
     * <p>
     * {@linkplain ElementsNonnullList} with {@linkplain RandomAccess}.
     * </p>
     *
     * @author Fred Suvn
     * @version 0.0.0, 2016-11-09T10:50:39+08:00
     * @since 0.0.0, 2016-11-09T10:50:39+08:00
     */
    private static class RandomAccessElementsNonnullList<E> extends ElementsNonnullList<E> implements RandomAccess {

        private static final long serialVersionUID = 1L;

        protected RandomAccessElementsNonnullList(List<E> list) {
            super(list);
        }
    }
}
