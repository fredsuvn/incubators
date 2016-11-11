package com.cogician.quicker;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.annotation.Nonnegative;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.cogician.quicker.configuration.QConfiguration;
import com.cogician.quicker.function.QuickAction;
import com.cogician.quicker.function.QuickConsumer;
import com.cogician.quicker.function.QuickPredicate;
import com.cogician.quicker.function.PredicateQuickAction;
import com.cogician.quicker.log.QLogger;
import com.cogician.quicker.util.Consts;
import com.cogician.quicker.util.calculator.Calculator;
import com.cogician.quicker.util.lexer.ParsingException;

/**
 * <p>
 * Quicker class is a quick, convenient static tool. It is spiritual leader of this lib.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-02-18T17:23:38+08:00
 * @since 0.0.0, 2016-02-18T17:23:38+08:00
 */
public class Quicker {

    /**
     * <p>
     * Returns default logger of this library. It's config in config.properties.
     * </p>
     * 
     * @return default logger
     * @since 0.0.0
     */
    private static QLogger log() {
        return QuickerProperties.LOG;
    }

    // private static final QConfiguration properties = QuickerProperties.getProperties();

    // private static final int LOGGING_LEVEL = properties.getInt("log.level");

    // /**
    // * <p>
    // * Logs specified info. The log level is configured in config.properties.
    // * </p>
    // *
    // * @param info
    // * specified info
    // * @since 0.0.0
    // */
    // public static void log(String info) {
    // log().log(LOGGING_LEVEL, info);
    // }

    private static final ThreadLocal<ThreadLocalValues> threadLocal = ThreadLocal
            .withInitial(() -> new ThreadLocalValues());

    /**
     * <p>
     * Puts specified value with the specified key in current thread. If current thread previously contained a mapping
     * for the key, the old value is replaced by the specified value.
     * </p>
     * 
     * @param key
     *            specified key
     * @param value
     *            specified value
     * @return the previous value associated with key, or null if there was no mapping for key
     * @since 0.0.0
     */
    public static @Nullable Object put(@Nullable Object key, @Nullable Object value) {
        return threadLocal.get().put(key, value);
    }

    /**
     * <p>
     * Returns the value to which the specified key is mapped in current thread, or null if current thread contains no
     * mapping for the key.
     * </p>
     * 
     * @param key
     *            specified key
     * @return the value to which the specified key is mapped in current thread, or null if current thread contains no
     *         mapping for the key
     * @since 0.0.0
     */
    public static @Nullable Object get(@Nullable Object key) {
        return threadLocal.get().get(key);
    }

    /**
     * <p>
     * Removes the mapping for specified key from current thread if it is present.
     * </p>
     * 
     * @param key
     *            specified key
     * @return the previous value associated with key, or null if there was no mapping for key
     * @since 0.0.0
     */
    public static @Nullable Object remove(@Nullable Object key) {
        return threadLocal.get().remove(key);
    }

    /**
     * <p>
     * Removes all of the mappings from current thread.
     * </p>
     * 
     * @since 0.0.0
     */
    public static void clear() {
        threadLocal.get().clear();
    }

    /**
     * <p>
     * Returns all thread local parameters as a map.
     * </p>
     * 
     * @return all thread local parameters as a map
     * @since 0.0.0
     */
    public static Map<Object, Object> threadLocal() {
        return threadLocal.get().map();
    }

    /**
     * <p>
     * Returns milliseconds of difference from now to last calling of this method in current thread.
     * </p>
     * <p>
     * For example:
     * 
     * <pre>
     * Quicker.clockMillis();
     * do something...
     * System.out.println(Quicker.clockMillis());
     * </pre>
     * 
     * Above case is equivalent to:
     * 
     * <pre>
     * long l1 = System.currentTimeMillis();
     * do something...
     * long l2 = System.currentTimeMillis();
     * System.out.println(l2 - l1);
     * </pre>
     * 
     * </p>
     * 
     * @return milliseconds of difference from now to last calling of this method in current thread
     * @since 0.0.0
     */
    public static long clockMillis() {
        long l = System.currentTimeMillis();
        long result = l - threadLocal.get().getMillis();
        threadLocal.get().setMillis(l);
        return result;
    }

    /**
     * <p>
     * Returns nanoseconds of difference from now to last calling of this method in current thread.
     * </p>
     * <p>
     * For example:
     * 
     * <pre>
     * Quicker.clockNano();
     * do something...
     * System.out.println(Quicker.clockNano());
     * </pre>
     * 
     * Above case is equivalent to:
     * 
     * <pre>
     * long l1 = System.nanoTime();
     * do something...
     * long l2 = System.nanoTime();
     * System.out.println(l2 - l1);
     * </pre>
     * 
     * </p>
     * 
     * @return nanoseconds of difference from now to last calling of this method in current thread
     * @since 0.0.0
     */
    public static long clockNano() {
        long l = System.nanoTime();
        long result = l - threadLocal.get().getNano();
        threadLocal.get().setNano(l);
        return result;
    }

    /**
     * <p>
     * Returns milliseconds of difference from now to last calling of this method with specified key in current thread.
     * </p>
     * <p>
     * For example:
     * 
     * <pre>
     * Quicker.clockMillis("clock0");
     * Quicker.clockMillis("clock1");
     * Quicker.clockMillis("clock2");
     * do something...
     * System.out.println(Quicker.clockMillis("clock1"));
     * </pre>
     * 
     * Above case is equivalent to:
     * 
     * <pre>
     * long clock0 = System.currentTimeMillis();
     * long clock1 = System.currentTimeMillis();
     * long clock2 = System.currentTimeMillis();
     * do something...
     * long clock1_end = System.currentTimeMillis();
     * System.out.println(clock1_end - clock1);
     * </pre>
     * 
     * </p>
     * 
     * @param key
     *            specified key
     * @return milliseconds of difference from now to last calling of this method with specified key in current thread
     * @since 0.0.0
     */
    public static long clockMillis(Object key) {
        long l = System.currentTimeMillis();
        key = hash(key) + "millis";
        long result = l - (Long)threadLocal.get().getInternal(key);
        threadLocal.get().putInternal(key, l);
        return result;
    }

    /**
     * <p>
     * Returns nanoseconds of difference from now to last calling of this method with specified key in current thread.
     * </p>
     * <p>
     * For example:
     * 
     * <pre>
     * Quicker.clockNano("clock0");
     * Quicker.clockNano("clock1");
     * Quicker.clockNano("clock2");
     * do something...
     * System.out.println(Quicker.clockNano("clock1"));
     * </pre>
     * 
     * Above case is equivalent to:
     * 
     * <pre>
     * long clock0 = System.nanoTime();
     * long clock1 = System.nanoTime();
     * long clock2 = System.nanoTime();
     * do something...
     * long clock1_end = System.nanoTime();
     * System.out.println(clock1_end - clock1);
     * </pre>
     * 
     * </p>
     * 
     * @param key
     *            specified key
     * @return nanoseconds of difference from now to last calling of this method with specified key in current thread
     * @since 0.0.0
     */
    public static long clockNano(Object key) {
        long l = System.nanoTime();
        key = hash(key) + "nano";
        long result = l - (Long)threadLocal.get().getInternal(key);
        threadLocal.get().putInternal(key, l);
        return result;
    }

    /**
     * <p>
     * Calculates arithmetic expression.
     * </p>
     * <p>
     * For example:
     * 
     * <pre>
     * Quicker.calculate("(1 + 2) * 3 % 4 + max(max(-5, 6.5, 100), max(3, 555, 8,), 88)*(2)");
     * </pre>
     * 
     * Result of above codes is 1111. More detail see {@linkplain Calculator}.
     * </p>
     * 
     * @param expr
     *            arithmetic expression, not null
     * @return result of the expression, not null
     * @throws ParsingException
     *             if any problem occurs when parsing
     * @since 0.0.0
     */
    public static BigDecimal calculate(String expr) throws ParsingException {
        return threadLocal.get().getCalculator().calculate(expr);
    }

    /**
     * <p>
     * Returns given required object if it is not null, or throws a {@linkplain NullPointerException} if it is null.
     * </p>
     * 
     * @param <T>
     *            type of required object
     * @param required
     *            given required object
     * @return given required object if it is not null
     * @throws NullPointerException
     *             if given required object is null
     * @since 0.0.0
     */
    public static <T> T require(T required) throws NullPointerException {
        Checker.checkNull(required);
        return required;
    }

    /**
     * <p>
     * Returns given integer if given integer greater than 0.
     * </p>
     * 
     * @param i
     *            given integer
     * @return given integer if given integer greater than 0
     * @throws IllegalArgumentException
     *             if given integer <= 0
     * @since 0.0.0
     */
    public static int requirePositive(int i) throws IllegalArgumentException {
        Checker.checkPositive(i);
        return i;
    }

    /**
     * <p>
     * Returns given long integer if given long integer greater than 0.
     * </p>
     * 
     * @param i
     *            given long integer
     * @return given long integer if given long integer greater than 0
     * @throws IllegalArgumentException
     *             if given long integer <= 0
     * @since 0.0.0
     */
    public static long requirePositive(long i) throws IllegalArgumentException {
        Checker.checkPositive(i);
        return i;
    }

    /**
     * <p>
     * Returns given integer if given integer is nonnegative.
     * </p>
     * 
     * @param i
     *            given integer
     * @return given integer if given integer is nonnegative
     * @throws IllegalArgumentException
     *             if given integer < 0
     * @since 0.0.0
     */
    public static int requireNonnegative(int i) throws IllegalArgumentException {
        Checker.checkNonnegative(i);
        return i;
    }

    /**
     * <p>
     * Returns given long integer if given long integer is nonnegative.
     * </p>
     * 
     * @param i
     *            given long integer
     * @return given long integer if given long integer is nonnegative
     * @throws IllegalArgumentException
     *             if given long integer < 0
     * @since 0.0.0
     */
    public static long requireNonnegative(long i) throws IllegalArgumentException {
        Checker.checkNonnegative(i);
        return i;
    }

    /**
     * <p>
     * Returns given required object if it is not null, or {@code ifNull}.get() if it is null.
     * </p>
     * 
     * @param <T>
     *            type of required object
     * @param required
     *            given required object
     * @param ifNull
     *            supplier if given object is null
     * @return given required object if it is not null, or {@code ifNull}.get() if it is null
     * @throws NullPointerException
     *             if given required object and {@code ifNull} (or its get()) are both null
     * @since 0.0.0
     */
    public static <T> T require(@Nullable T required, Supplier<? extends T> ifNull) throws NullPointerException {
        return required == null ? require(ifNull.get()) : required;
    }

    /**
     * <p>
     * If given string is non-null and non-empty, return itself; else throw an exception.
     * </p>
     * 
     * @param str
     *            given string
     * @return given string itself
     * @throws NullPointerException
     *             if given string is null
     * @throws IllegalArgumentException
     *             if given string is empty
     * @since 0.0.0
     */
    public static String requireNonEmpty(@Nullable String str) throws NullPointerException, IllegalArgumentException {
        Checker.checkEmpty(str);
        return str;
    }

    /**
     * <p>
     * Upper casts given iterator. If given iterator is null, return null.
     * </p>
     * 
     * @param <E>
     *            component type
     * @param iterator
     *            given iterator
     * @return itself after casting
     * @since 0.0.0
     */
    public static final @Nullable <E> Iterator<E> upperCast(@Nullable Iterator<? extends E> iterator) {
        @SuppressWarnings("unchecked")
        Iterator<E> it = (Iterator<E>)iterator;
        return it;
    }

    /**
     * <p>
     * Upper casts given spliterator. If given spliterator is null, return null.
     * </p>
     * 
     * @param <E>
     *            component type
     * @param spliterator
     *            given spliterator
     * @return itself after casting
     * @since 0.0.0
     */
    public static final <E> Spliterator<E> upperCast(Spliterator<? extends E> spliterator) {
        @SuppressWarnings("unchecked")
        Spliterator<E> sp = (Spliterator<E>)spliterator;
        return sp;
    }

    /**
     * <p>
     * Upper casts given iterable. If given iterable is null, return null.
     * </p>
     * 
     * @param <E>
     *            component type
     * @param iterable
     *            given iterable
     * @return itself after casting
     * @since 0.0.0
     */
    public static final <E> Iterable<E> upperCast(Iterable<? extends E> iterable) {
        @SuppressWarnings("unchecked")
        Iterable<E> it = (Iterable<E>)iterable;
        return it;
    }

    /**
     * <p>
     * Upper casts given stream. If given stream is null, return null.
     * </p>
     * 
     * @param <E>
     *            component type
     * @param stream
     *            given stream
     * @return itself after casting
     * @since 0.0.0
     */
    public static final <E> Stream<E> upperCast(Stream<? extends E> stream) {
        @SuppressWarnings("unchecked")
        Stream<E> st = (Stream<E>)stream;
        return st;
    }

    /**
     * <p>
     * Upper casts given supplier. If given supplier is null, return null.
     * </p>
     * 
     * @param <E>
     *            component type
     * @param supplier
     *            given supplier
     * @return itself after casting
     * @since 0.0.0
     */
    public static final @Nullable <E> Supplier<E> upperCast(@Nullable Supplier<? extends E> supplier) {
        @SuppressWarnings("unchecked")
        Supplier<E> sp = (Supplier<E>)supplier;
        return sp;
    }

    /**
     * <p>
     * Returns a non-null string. If given string is null, return an empty string; else return itself.
     * </p>
     * 
     * @param str
     *            given string
     * @return a non-null string
     * @since 0.0.0
     */
    public static String nonnull(@Nullable String str) {
        return require(str, Consts::emptyString);
    }

    /**
     * <p>
     * Returns a non-null iterator. If given iterator is null, return an empty iterator; else return itself.
     * </p>
     * 
     * @param <E>
     *            component type of iterator
     * @param iterator
     *            given iterator
     * @return a non-null iterator
     * @since 0.0.0
     */
    public static <E> Iterator<E> nonnull(@Nullable Iterator<? extends E> iterator) {
        return require(upperCast(iterator), Consts::emptyIterator);
    }

    /**
     * <p>
     * Returns a non-null spliterator. If given spliterator is null, return an empty spliterator; else return itself.
     * </p>
     * 
     * @param <E>
     *            component type of spliterator
     * @param spliterator
     *            given spliterator
     * @return a non-null iterator
     * @since 0.0.0
     */
    public static <E> Spliterator<E> nonnull(@Nullable Spliterator<? extends E> spliterator) {
        return require(upperCast(spliterator), Consts::emptySpliterator);
    }

    /**
     * <p>
     * Returns a non-null iterable. If given iterable is null, return an empty iterable; else return itself.
     * </p>
     * 
     * @param <E>
     *            component type of iterable
     * @param iterable
     *            given iterable
     * @return a non-null iterable
     * @since 0.0.0
     */
    public static <E> Iterable<E> nonnull(@Nullable Iterable<? extends E> iterable) {
        return require(upperCast(iterable), Consts::emptyIterable);
    }

    /**
     * <p>
     * Returns a non-null stream. If given stream is null, return an empty stream; else return itself.
     * </p>
     * 
     * @param <E>
     *            component type of stream
     * @param stream
     *            given stream
     * @return a non-null stream
     * @since 0.0.0
     */
    public static <E> Stream<E> nonnull(@Nullable Stream<? extends E> stream) {
        return require(upperCast(stream), Consts::emptyStream);
    }

    /**
     * <p>
     * Returns a non-null list. If given list is null, return an empty serializable list (immutable); else return
     * itself.
     * </p>
     * 
     * @param <E>
     *            component type of list
     * @param list
     *            given list
     * @return a non-null list
     * @since 0.0.0
     */
    public static <E> List<E> nonnull(@Nullable List<E> list) {
        return require(list, Collections::emptyList);
    }

    /**
     * <p>
     * Returns a non-null set. If given set is null, return an empty serializable set (immutable); else return itself.
     * </p>
     * 
     * @param <E>
     *            component type of set
     * @param set
     *            given set
     * @return a non-null set
     * @since 0.0.0
     */
    public static <E> Set<E> nonnull(@Nullable Set<E> set) {
        return require(set, Collections::emptySet);
    }

    /**
     * <p>
     * Returns a non-null map. If given map is null, return an empty serializable map (immutable); else return itself.
     * </p>
     * 
     * @param <K>
     *            type of map keys
     * @param <V>
     *            type of map values
     * @param map
     *            given map
     * @return a non-null map
     * @since 0.0.0
     */
    public static <K, V> Map<K, V> nonnull(@Nullable Map<K, V> map) {
        return require(map, Collections::emptyMap);
    }

    /**
     * <p>
     * Returns a {@linkplain Flow} object by given array. Returned flow object is convenient for each operation and to
     * convert into other flow-able type such as {@linkplain Collection}, {@linkplain Stream} or {@linkplain Supplier}.
     * </p>
     * 
     * @param <E>
     *            type of component element
     * @param array
     *            given array
     * @return a {@linkplain Flow} object
     * @since 0.0.0
     * @see Flow
     */
    public static <E> Flow<E> flow(@Nullable E[] array) {
        return array == null ? Flow.empty() : new Flow<E>() {

            @Override
            public java.util.Spliterator<E> toSpliterator() {
                return Arrays.spliterator(array);
            }
        };
    }

    /**
     * <p>
     * Returns a {@linkplain Flow} object by given iterator. Returned flow object is convenient for each operation and
     * to convert into other flow-able type such as {@linkplain Collection}, {@linkplain Stream} or
     * {@linkplain Supplier}.
     * </p>
     * 
     * @param <E>
     *            type of component element
     * 
     * @param iterator
     *            given iterator
     * @return a {@linkplain Flow} object
     * @since 0.0.0
     * @see Flow
     */
    public static <E> Flow<E> flow(@Nullable Iterator<? extends E> iterator) {
        return iterator == null ? Flow.empty() : new Flow<E>() {

            @Override
            public Iterator<E> toIterator() {
                return upperCast(iterator);
            }

            @Override
            public Spliterator<E> toSpliterator() {
                return Spliterators.spliteratorUnknownSize(toIterator(), Spliterator.ORDERED);
            }
        };
    }

    /**
     * <p>
     * Returns a {@linkplain Flow} object by given iterable. Returned flow object is convenient for each operation and
     * to convert into other flow-able type such as {@linkplain Collection}, {@linkplain Stream} or
     * {@linkplain Supplier}.
     * </p>
     * 
     * @param <E>
     *            type of component element
     * 
     * @param iterable
     *            given iterable
     * @return a {@linkplain Flow} object
     * @since 0.0.0
     * @see Flow
     */
    public static <E> Flow<E> flow(@Nullable Iterable<? extends E> iterable) {
        return iterable == null ? Flow.empty() : new Flow<E>() {

            @Override
            public Iterable<E> toIterable() {
                return upperCast(iterable);
            }

            @Override
            public Spliterator<E> toSpliterator() {
                return Spliterators.spliteratorUnknownSize(toIterator(), Spliterator.ORDERED);
            }

        };
    }

    /**
     * <p>
     * Returns a {@linkplain Flow} object by given spliterator. Returned flow object is convenient for each operation
     * and to convert into other flow-able type such as {@linkplain Collection}, {@linkplain Stream} or
     * {@linkplain Supplier}.
     * </p>
     * 
     * @param <E>
     *            type of component element
     * 
     * @param spliterator
     *            given spliterator
     * @return a {@linkplain Flow} object
     * @since 0.0.0
     * @see Flow
     */
    public static <E> Flow<E> flow(@Nullable Spliterator<? extends E> spliterator) {
        return spliterator == null ? Flow.empty() : new Flow<E>() {

            @Override
            public java.util.Spliterator<E> toSpliterator() {
                return upperCast(spliterator);
            }
        };
    }

    /**
     * <p>
     * Returns a {@linkplain Flow} object by given stream. Returned flow object is convenient for each operation and to
     * convert into other flow-able type such as {@linkplain Collection}, {@linkplain Stream} or {@linkplain Supplier}.
     * </p>
     * 
     * @param <E>
     *            type of component element
     * 
     * @param stream
     *            given stream
     * @return a {@linkplain Flow} object
     * @since 0.0.0
     * @see Flow
     */
    public static <E> Flow<E> flow(@Nullable Stream<? extends E> stream) {
        return stream == null ? Flow.empty() : new Flow<E>() {

            @Override
            public java.util.Spliterator<E> toSpliterator() {
                return upperCast(stream.spliterator());
            }

            @Override
            public Stream<E> toStream(boolean parallel) {
                return upperCast(parallel ? stream.parallel() : stream);
            }
        };
    }

    /**
     * <p>
     * Returns a {@linkplain Flow} object by given supplier. Returned flow object is convenient for each operation and
     * to convert into other flow-able type such as {@linkplain Collection}, {@linkplain Stream} or
     * {@linkplain Supplier}.
     * </p>
     * 
     * @param <E>
     *            type of component element
     * 
     * @param supplier
     *            given supplier
     * @return a {@linkplain Flow} object
     * @since 0.0.0
     * @see Flow
     */
    public static <E> Flow<E> flow(@Nullable Supplier<? extends E> supplier) {
        return supplier == null ? Flow.empty() : new Flow<E>() {

            @Override
            public java.util.Spliterator<E> toSpliterator() {
                return toStream().spliterator();
            }

            @Override
            public Stream<E> toStream() {
                return Stream.generate(upperCast(supplier));
            }

            @Override
            public Stream<E> toStream(boolean parallel) {
                return parallel ? toStream().parallel() : toStream();
            }

            @Override
            public Supplier<E> toSupplier() throws NoSuchElementException {
                return upperCast(supplier);
            }
        };
    }

    /**
     * <p>
     * Returns a {@linkplain Flow} object by given map. Returned flow object is convenient for each operation and to
     * convert into other flow-able type such as {@linkplain Collection}, {@linkplain Stream} or {@linkplain Supplier}.
     * </p>
     * 
     * @param <K>
     *            type of map keys
     * @param <V>
     *            type of map values
     * 
     * @param map
     *            given map
     * @return a {@linkplain Flow} object
     * @since 0.0.0
     * @see Flow
     */
    public static <K, V> Flow<Entry<K, V>> flow(@Nullable Map<K, V> map) {
        return map == null ? Flow.empty() : new Flow<Entry<K, V>>() {

            @Override
            public Set<Entry<K, V>> toSet() {
                return map.entrySet();
            }

            @Override
            public java.util.Spliterator<Entry<K, V>> toSpliterator() {
                return toSet().spliterator();
            }
        };
    }

    /**
     * <p>
     * Concats given flows into one and returnd.
     * </p>
     * 
     * @param <E>
     *            type of component element
     * @param flows
     *            given flows
     * @return a flow after concatting
     * @since 0.0.0
     */
    @SafeVarargs
    public static <E> Flow<E> flow(@Nullable Flow<? extends E>... flows) {
        return flows == null ? Flow.empty() : new Flow<E>() {

            @Override
            public Spliterator<E> toSpliterator() {
                return toStream().spliterator();
            }

            @Override
            public Stream<E> toStream() {
                Stream<E> result = Stream.empty();
                for (int i = 0; i < flows.length; i++) {
                    if (flows[i] != null) {
                        result = Stream.concat(result, flows[i].toStream());
                    }
                }
                return result;
            }

            @Override
            public Stream<E> toStream(boolean parallel) {
                return parallel ? toStream().parallel() : toStream();
            }
        };
    }

    /**
     * <p>
     * Loop performs given action specified times. If the number of times is negative, the action will be performed
     * infinitely.
     * </p>
     * 
     * @param times
     *            number of specified times
     * @param action
     *            given action
     * @since 0.0.0
     */
    public static void each(long times, QuickAction action) {
        if (times == 0) {
            return;
        }
        if (times < 0) {
            while (true) {
                long count = 0;
                action.perform(count++);
            }
        } else {
            for (long i = 0; i < times; i++) {
                action.perform(i);
            }
        }
    }

    /**
     * <p>
     * Loop performs given action specified times. If the action returns false, it will break the loop. Actual
     * performance times will be returned. If given action is null, return 0.
     * </p>
     * <p>
     * If the number of times is negative, the action will try to perform infinitely till it returns false.
     * </p>
     * 
     * @param times
     *            number of specified times
     * @param action
     *            given action
     * @return actual performance times
     * @since 0.0.0
     */
    public static long each(long times, PredicateQuickAction action) {
        if (times == 0) {
            return 0;
        }
        if (times < 0) {
            long i = 0;
            while (true) {
                if (!action.perform(i)) {
                    return i;
                }
                i++;
            }
        } else {
            for (long i = 0; i < times; i++) {
                if (!action.perform(i)) {
                    return i;
                }
            }
        }
        return times;
    }

    /**
     * <p>
     * Does action if given object and action are not null.
     * </p>
     * 
     * @param <T>
     *            type of given object
     * @param obj
     *            given object
     * @param action
     *            given action
     * @since 0.0.0
     * @deprecated This method is ridiculous.
     */
    @Deprecated
    public static <T> void doIfNonnull(@Nullable T obj, @Nullable Consumer<? super T> action) {
        if (obj != null && action != null) {
            action.accept(obj);
        }
    }

    /**
     * <p>
     * Makes current thread sleep in given milliseconds. The sleeping can be interrupted by an
     * {@linkplain InterruptedException}. The actual sleeping milliseconds will be returned.
     * </p>
     * 
     * @param millis
     *            given milliseconds
     * @return actual sleeping milliseconds
     * @throws IllegalArgumentException
     *             if given milliseconds is negative
     * @since 0.0.0
     */
    public static long sleep(@Nonnegative long millis) throws IllegalArgumentException {
        try {
            clockMillis();
            Thread.sleep(millis);
            return clockMillis();
        } catch (InterruptedException e) {
            return clockMillis();
        }
    }

    /**
     * <p>
     * Forcibly makes current thread sleep in given milliseconds. The sleeping can <b>not</b> be interrupted by an
     * {@linkplain InterruptedException}. It will be sleeping until time up.
     * </p>
     * 
     * @param millis
     *            given milliseconds
     * @throws IllegalArgumentException
     *             if given milliseconds is negative
     * @since 0.0.0
     */
    public static void sleepForcibly(@Nonnegative long millis) throws IllegalArgumentException {
        try {
            clockMillis();
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            long l = clockMillis();
            if (l < millis) {
                sleepForcibly(millis - l);
            }
        }
    }

    /**
     * <p>
     * Counts and returns number of portions divided in {@code portion}. If {@code total} is not multiple of
     * {@code portion}, the remainder will be seeing as an extra one portion and added in returned value:
     * 
     * <pre>
     * return total % portion == 0 ? total / portion : total / portion + 1;
     * </pre>
     * 
     * </p>
     * 
     * @param total
     *            total value
     * @param portion
     *            value of each portion
     * @return number of portions divided in {@code portion}
     * @since 0.0.0
     */
    public static int countPortion(int total, int portion) {
        return total % portion == 0 ? total / portion : total / portion + 1;
    }

    /**
     * <p>
     * Counts and returns number of portions divided in {@code portion}. If {@code total} is not multiple of
     * {@code portion}, the remainder will be seeing as an extra one portion and added in returned value:
     * 
     * <pre>
     * return total % portion == 0 ? total / portion : total / portion + 1;
     * </pre>
     * 
     * </p>
     * 
     * @param total
     *            total value
     * @param portion
     *            value of each portion
     * @return number of portions divided in {@code portion}
     * @since 0.0.0
     */
    public static long countPortion(long total, long portion) {
        return total % portion == 0 ? total / portion : total / portion + 1;
    }

    /**
     * <p>
     * Capitalizes initials of given string.
     * </p>
     * 
     * @param str
     *            given string
     * @return string after capitalizing
     * @throws NullPointerException
     *             if given string is null
     * @since 0.0.0
     */
    public static String capitalize(String str) throws NullPointerException {
        char[] cs = require(str).toCharArray();
        if (cs.length > 0) {
            cs[0] = Character.toUpperCase(cs[0]);
        }
        return String.valueOf(cs);
    }

    /**
     * <p>
     * String-izes input string, appending " as prefix and suffix. For example:
     * 
     * <pre>
     * Quicker.stringize("abc");
     * will return: "abc" (original is abc)
     * </pre>
     * 
     * If input string is null, return null.
     * </p>
     * 
     * @param str
     *            input string
     * @return string-lized string
     * @since 0.0.0
     */
    public static @Nullable String stringize(@Nullable String str) {
        return null == str ? str : "\"" + str + "\"";
    }

    /**
     * <p>
     * Returns a string of whitespace of specified length.
     * </p>
     * 
     * @param length
     *            specified length, >= 0
     * @return a string of whitespace with specified length
     * @throws IllegalArgumentException
     *             if length < 0
     * @since 0.0.0
     */
    public static String blankString(int length) throws IllegalArgumentException {
        Checker.checkLength(length);
        StringBuilder sb = new StringBuilder();
        Quicker.each(length, i -> {
            sb.append(" ");
        });
        return sb.toString();
    }

    /**
     * <p>
     * Left pad or truncate given string. If given string is null, it will be seen as an empty string; if given pad
     * string is null or empty, it will be seen as a string consists of a whitespace. For examples:
     * 
     * <pre>
     * Quicker.leftPad(null, 3, null)   = "   "
     * Quicker.leftPad("", 3, "z")      = "zzz"
     * Quicker.leftPad("bat", 3, "yz")  = "bat"
     * Quicker.leftPad("bat", 5, "yz")  = "yzbat"
     * Quicker.leftPad("bat", 8, "yz")  = "yzyzybat"
     * Quicker.leftPad("bat", 1, "yz")  = "t"
     * Quicker.leftPad("bat", 5, null)  = "  bat"
     * Quicker.leftPad("bat", 5, "")    = "  bat"
     * </pre>
     * </p>
     * 
     * @param str
     *            given string
     * @param length
     *            given length of returned string, >= 0
     * @param padStr
     *            pad string
     * @return string after padding or truncating
     * @throws IllegalArgumentException
     *             if given length < 0
     * @since 0.0.0
     */
    public static String leftPad(@Nullable String str, int length, @Nullable String padStr)
            throws IllegalArgumentException {
        Checker.checkLength(length);
        str = require(str, Consts::emptyString);
        if (Checker.isEmpty(padStr)) {
            padStr = " ";
        }
        if (length > str.length()) {
            int padLength = length - str.length();
            StringBuilder sb = new StringBuilder();
            for (int i = 0, j = 0; i < padLength; i++, j++) {
                if (j >= padStr.length()) {
                    j = 0;
                }
                sb.append(padStr.charAt(j));
            }
            sb.append(str);
            return sb.toString();
        } else if (length == 0) {
            return Consts.emptyString();
        } else {
            return str.substring(str.length() - length);
        }
    }

    /**
     * <p>
     * Right pad or truncate given string. If given string is null, it will be seen as an empty string; if given pad
     * string is null or empty, it will be seen as a string consists of a whitespace. For examples:
     * 
     * <pre>
     * Quicker.rightPad(null, 3, null)   = "   "
     * Quicker.rightPad("", 3, "z")      = "zzz"
     * Quicker.rightPad("bat", 3, "yz")  = "bat"
     * Quicker.rightPad("bat", 5, "yz")  = "batyz"
     * Quicker.rightPad("bat", 8, "yz")  = "batyzyzy"
     * Quicker.rightPad("bat", 1, "yz")  = "b"
     * Quicker.rightPad("bat", 5, null)  = "bat  "
     * Quicker.rightPad("bat", 5, "")    = "bat  "
     * </pre>
     * </p>
     * 
     * @param str
     *            given string
     * @param length
     *            given length of returned string, >= 0
     * @param padStr
     *            pad string
     * @return string after padding or truncating
     * @throws IllegalArgumentException
     *             if given length < 0
     * @since 0.0.0
     */
    public static String rightPad(@Nullable String str, int length, @Nullable String padStr)
            throws IllegalArgumentException {
        Checker.checkLength(length);
        str = require(str, Consts::emptyString);
        if (Checker.isEmpty(padStr)) {
            padStr = " ";
        }
        if (length > str.length()) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            int padLength = length - str.length();
            for (int i = 0, j = 0; i < padLength; i++, j++) {
                if (j >= padStr.length()) {
                    j = 0;
                }
                sb.append(padStr.charAt(j));
            }
            return sb.toString();
        } else if (length == 0) {
            return Consts.emptyString();
        } else {
            return str.substring(0, length);
        }
    }

    /**
     * <p>
     * Returns given value if given value >= given lower limit; else returns the lower limit like:
     * 
     * <pre>
     * return value < least ? least : value;
     * </pre>
     * </p>
     * 
     * @param value
     *            given value
     * @param least
     *            lower limit
     * @return value above given lower limit
     * @since 0.0.0
     */
    public static int atLeast(int value, int least) {
        return value < least ? least : value;
    }

    /**
     * <p>
     * Returns given value if given value >= given lower limit; else returns the lower limit like:
     * 
     * <pre>
     * return value < least ? least : value;
     * </pre>
     * </p>
     * 
     * @param value
     *            given value
     * @param least
     *            lower limit
     * @return value above given lower limit
     * @since 0.0.0
     */
    public static long atLeast(long value, long least) {
        return value < least ? least : value;
    }

    /**
     * <p>
     * Returns given value if given value >= given lower limit; else returns the lower limit like:
     * 
     * <pre>
     * return value < least ? least : value;
     * </pre>
     * </p>
     * 
     * @param value
     *            given value
     * @param least
     *            lower limit
     * @return value above given lower limit
     * @since 0.0.0
     */
    public static float atLeast(float value, float least) {
        return value < least ? least : value;
    }

    /**
     * <p>
     * Returns given value if given value >= given lower limit; else returns the lower limit like:
     * 
     * <pre>
     * return value < least ? least : value;
     * </pre>
     * </p>
     * 
     * @param value
     *            given value
     * @param least
     *            lower limit
     * @return value above given lower limit
     * @since 0.0.0
     */
    public static double atLeast(double value, double least) {
        return value < least ? least : value;
    }

    /**
     * <p>
     * Returns given value if given value <= given upper limit; else returns the upper limit like:
     * 
     * <pre>
     * return value > most ? most : value;
     * </pre>
     * </p>
     * 
     * @param value
     *            given value
     * @param most
     *            upper limit
     * @return value under given upper limit
     * @since 0.0.0
     */
    public static int atMost(int value, int most) {
        return value > most ? most : value;
    }

    /**
     * <p>
     * Returns given value if given value <= given upper limit; else returns the upper limit like:
     * 
     * <pre>
     * return value > most ? most : value;
     * </pre>
     * </p>
     * 
     * @param value
     *            given value
     * @param most
     *            upper limit
     * @return value under given upper limit
     * @since 0.0.0
     */
    public static long atMost(long value, long most) {
        return value > most ? most : value;
    }

    /**
     * <p>
     * Returns given value if given value <= given upper limit; else returns the upper limit like:
     * 
     * <pre>
     * return value > most ? most : value;
     * </pre>
     * </p>
     * 
     * @param value
     *            given value
     * @param most
     *            upper limit
     * @return value under given upper limit
     * @since 0.0.0
     */
    public static float atMost(float value, float most) {
        return value > most ? most : value;
    }

    /**
     * <p>
     * Returns given value if given value <= given upper limit; else returns the upper limit like:
     * 
     * <pre>
     * return value > most ? most : value;
     * </pre>
     * </p>
     * 
     * @param value
     *            given value
     * @param most
     *            upper limit
     * @return value under given upper limit
     * @since 0.0.0
     */
    public static double atMost(double value, double most) {
        return value > most ? most : value;
    }

    /**
     * <p>
     * Returns given value if given value in given bounds; else returns the lower limit or upper limit like:
     * 
     * <pre>
     * return value < from ? from : (value > to ? to : value);
     * </pre>
     * </p>
     * 
     * @param value
     *            given value
     * @param from
     *            lower limit of given bounds, inclusive
     * @param to
     *            upper limit of given bounds, inclusive
     * @return value in given bounds
     * @since 0.0.0
     */
    public static int inBounds(int value, int from, int to) {
        return value < from ? from : (value > to ? to : value);
    }

    /**
     * <p>
     * Returns given value if given value in given bounds; else returns the lower limit or upper limit like:
     * 
     * <pre>
     * return value < from ? from : (value > to ? to : value);
     * </pre>
     * </p>
     * 
     * @param value
     *            given value
     * @param from
     *            lower limit of given bounds, inclusive
     * @param to
     *            upper limit of given bounds, inclusive
     * @return value in given bounds
     * @since 0.0.0
     */
    public static long inBounds(long value, long from, long to) {
        return value < from ? from : (value > to ? to : value);
    }

    /**
     * <p>
     * Returns given value if given value in given bounds; else returns the lower limit or upper limit like:
     * 
     * <pre>
     * return value < from ? from : (value > to ? to : value);
     * </pre>
     * </p>
     * 
     * @param value
     *            given value
     * @param from
     *            lower limit of given bounds, inclusive
     * @param to
     *            upper limit of given bounds, inclusive
     * @return value in given bounds
     * @since 0.0.0
     */
    public static float inBounds(float value, float from, float to) {
        return value < from ? from : (value > to ? to : value);
    }

    /**
     * <p>
     * Returns given value if given value in given bounds; else returns the lower limit or upper limit like:
     * 
     * <pre>
     * return value < from ? from : (value > to ? to : value);
     * </pre>
     * </p>
     * 
     * @param value
     *            given value
     * @param from
     *            lower limit of given bounds, inclusive
     * @param to
     *            upper limit of given bounds, inclusive
     * @return value in given bounds
     * @since 0.0.0
     */
    public static double inBounds(double value, double from, double to) {
        return value < from ? from : (value > to ? to : value);
    }

    /**
     * <p>
     * Returns a string consists of substring of specified remainder long from head and a ellipsis. For example:
     * 
     * <pre>
     * ellipsis("abcdefg", 3);
     * will return "abc..."
     * </pre>
     * </p>
     * <p>
     * If given string is null, return "null". If length of given string is less than given remainder length, return
     * itself.
     * </p>
     * 
     * @param str
     *            given string
     * @param remainder
     *            given remainder length
     * @return a string consists of substring of specified remainder long from head and a ellipsis
     * @throws IllegalArgumentException
     *             if given remainder length < 0
     * @since 0.0.0
     */
    public static String ellipsis(@Nullable String str, int remainder) throws IllegalArgumentException {
        if (str == null) {
            return String.valueOf(str);
        }
        if (str.length() > remainder) {
            return str.substring(0, remainder) + "...";
        } else {
            return str;
        }
    }

    /**
     * <p>
     * Returns a string consists of substring of 3 characters long from head and a ellipsis. For example:
     * 
     * <pre>
     * ellipsis("abcdefg");
     * will return "abc..."
     * </pre>
     * </p>
     * <p>
     * If given string is null, return "null". If length of given string is less than 3, return itself.
     * </p>
     * 
     * @param str
     *            given string
     * @return a string consists of substring of 3 characters long from head and a ellipsis
     * @since 0.0.0
     */
    public static String ellipsis(@Nullable String str) {
        return ellipsis(str, 3);
    }

    private static Scanner scanner = null;

    /**
     * <p>
     * Scans string of a line of input from {@linkplain System#in}.
     * </p>
     * 
     * @return a string scanned from {@linkplain System#in}
     * @since 0.0.0
     */
    public static String scan() {
        return Quicker.require(scanner, () -> new Scanner(System.in)).nextLine();
    }

    private static final String USER_DIR = System.getProperty("user.dir");

    /**
     * <p>
     * Returns user's curent work path, same as:
     * 
     * <pre>
     * System.getProperty("user.dir");
     * </pre>
     * </p>
     * 
     * @return
     * @since 0.0.0
     */
    public static String getUserDir() {
        return USER_DIR;
    }

    /**
     * <p>
     * Returns resource of current application by specified path, including class path.
     * </p>
     * 
     * @param path
     *            specified path, including class path
     * @return resource of current application
     * @throws NullPointerException
     *             if specified path is null
     * @throws ReadException
     *             if any problem occurs when getting resource
     * @since 0.0.0
     */
    public static URI getResource(String path) throws NullPointerException, ReadException {
        URL url = Object.class.getResource(Quicker.require(path));
        try {
            return url.toURI();
        } catch (URISyntaxException e) {
            throw new ReadException(e);
        }
    }

    /**
     * <p>
     * Converts given object to boolean, same as:
     * 
     * <pre>
     * Boolean.parseBoolean(String.valueOf(obj));
     * </pre>
     * </p>
     * 
     * @param obj
     *            given object
     * @return boolean value of given object
     * @since 0.0.0
     */
    public static boolean toBoolean(Object obj) {
        return Boolean.parseBoolean(String.valueOf(obj));
    }

    /**
     * <p>
     * Converts given object to int, same as:
     * 
     * <pre>
     * Integer.parseInt(String.valueOf(obj));
     * </pre>
     * </p>
     * 
     * @param obj
     *            given object
     * @return int value of given object
     * @throws NumberFormatException
     *             if given object cannot be converted
     * @since 0.0.0
     */
    public static int toInt(Object obj) throws NumberFormatException {
        return Integer.parseInt(String.valueOf(obj));
    }

    /**
     * <p>
     * Converts given object to float, same as:
     * 
     * <pre>
     * Float.parseFloat(String.valueOf(obj));
     * </pre>
     * </p>
     * 
     * @param obj
     *            given object
     * @return float value of given object
     * @throws NumberFormatException
     *             if given object cannot be converted
     * @since 0.0.0
     */
    public static float toFloat(Object obj) throws NumberFormatException {
        return Float.parseFloat(String.valueOf(obj));
    }

    /**
     * <p>
     * Converts given object to long, same as:
     * 
     * <pre>
     * Long.parseLong(String.valueOf(obj));
     * </pre>
     * </p>
     * 
     * @param obj
     *            given object
     * @return long value of given object
     * @throws NumberFormatException
     *             if given object cannot be converted
     * @since 0.0.0
     */
    public static long toLong(Object obj) throws NumberFormatException {
        return Long.parseLong(String.valueOf(obj));
    }

    /**
     * <p>
     * Converts given object to double, same as:
     * 
     * <pre>
     * Double.parseDouble(String.valueOf(obj));
     * </pre>
     * </p>
     * 
     * @param obj
     *            given object
     * @return double value of given object
     * @throws NumberFormatException
     *             if given object cannot be converted
     * @since 0.0.0
     */
    public static double toDouble(Object obj) throws NumberFormatException {
        return Double.parseDouble(String.valueOf(obj));
    }

    /**
     * <p>
     * Converts given object to BigInteger, same as:
     * 
     * <pre>
     * new BigInteger(String.valueOf(obj));
     * </pre>
     * </p>
     * 
     * @param obj
     *            given object
     * @return BigInteger value of given object
     * @throws NumberFormatException
     *             if given object cannot be converted
     * @since 0.0.0
     */
    public static BigInteger toBigInteger(Object obj) throws NumberFormatException {
        return new BigInteger(String.valueOf(obj));
    }

    /**
     * <p>
     * Converts given object to BigDecimal, same as:
     * 
     * <pre>
     * new BigDecimal(String.valueOf(obj));
     * </pre>
     * </p>
     * 
     * @param obj
     *            given object
     * @return BigDecimal value of given object
     * @throws NumberFormatException
     *             if given object cannot be converted
     * @since 0.0.0
     */
    public static BigDecimal toBigDecimal(Object obj) throws NumberFormatException {
        return new BigDecimal(String.valueOf(obj));
    }

    /**
     * <p>
     * Returns default, original hash code of given object.
     * </p>
     * 
     * @param obj
     *            given object
     * @return default, original hash code of given object
     * @since 0.0.0
     */
    public static int hash(Object obj) {
        return System.identityHashCode(obj);
    }

    /**
     * <p>
     * Empty constructor for some reflection framework which need at least an empty constructor.
     * </p>
     * 
     * @since 0.0.0
     */
    public Quicker() {
    }

    public static void main(String[] args) {
        // System.out.println("Hello, welcome to use Quicker framework!");
        // System.out.println("What should I do for you?");
        // String input = scan();
        // System.out.println("Your input is: " + input);
    }

    /**
     * <p>
     * Thread local values.
     * </p>
     *
     * @author Fred Suvn
     * @version 0.0.0, 2016-11-07T14:59:47+08:00
     * @since 0.0.0, 2016-11-07T14:59:47+08:00
     */
    private static class ThreadLocalValues {

        private final Map<Object, Object> map = new HashMap<>();

        private final Map<Object, Object> internal = new HashMap<>();

        private long millis = 0L;

        private long nano = 0L;

        private Calculator calculator;

        private ThreadLocalValues() {

        }

        public @Nullable Object get(@Nullable Object key) {
            return map.get(key);
        }

        public @Nullable Object put(@Nullable Object key, @Nullable Object value) {
            return map.put(key, value);
        }

        public @Nullable Object remove(@Nullable Object key) {
            return map.remove(key);
        }

        public void clear() {
            map.clear();
        }

        public Map<Object, Object> map() {
            return map;
        }

        public @Nullable Object getInternal(@Nullable Object key) {
            return internal.get(key);
        }

        public @Nullable Object putInternal(@Nullable Object key, @Nullable Object value) {
            return internal.put(key, value);
        }

        public @Nullable Object removeInternal(@Nullable Object key) {
            return internal.remove(key);
        }

        public long getMillis() {
            return millis;
        }

        public void setMillis(long millis) {
            this.millis = millis;
        }

        public long getNano() {
            return nano;
        }

        public void setNano(long nano) {
            this.nano = nano;
        }

        public Calculator getCalculator() {
            return calculator = calculator == null ? new Calculator() : calculator;
        }
    }

    /**
     * <p>
     * Flow is a collection-like or stream-like class, represents a set of elements, ordered or disordered, limited or
     * unlimited. It provides a uniform interface for each operation, and is convenient to convert flow into another
     * type such as {@linkplain Collection}, {@linkplain Stream} or {@linkplain Supplier}. In most cases flow is
     * disposable except few special instance such as {@linkplain #EMPTY}.
     * </p>
     * <p>
     * Generally, object after converting and source converted object itself are linked, any operation will be reflected
     * each other.
     * </p>
     * 
     * @param <E>
     *            type of element
     * @author Fred Suvn
     * @version 0.0.0, 2016-11-04T14:23:05+08:00
     * @since 0.0.0, 2016-11-04T14:23:05+08:00
     */
    public static interface Flow<E> {

        /**
         * <p>
         * Returns an empty and immutable flow.
         * </p>
         * 
         * @return an empty and immutable flow
         * @since 0.0.0
         */
        public static <E> Flow<E> empty() {
            @SuppressWarnings("unchecked")
            Flow<E> result = (Flow<E>)EMPTY;
            return result;
        }

        /**
         * <P>
         * An empty and immutable flow.
         * </p>
         * 
         * @since 0.0.0
         */
        public static Flow<Object> EMPTY = new Flow<Object>() {

            @Override
            public Collection<Object> toCollection() {
                return toSet();
            }

            @Override
            public Set<Object> toSet() {
                return Collections.emptySet();
            }

            @Override
            public Iterable<Object> toIterable() {
                return toSet();
            }

            @Override
            public Iterator<Object> toIterator() {
                return Collections.emptyIterator();
            }

            @Override
            public List<Object> toList() {
                return Collections.emptyList();
            }

            @Override
            public Spliterator<Object> toSpliterator() {
                return Spliterators.emptySpliterator();
            }

            @Override
            public Stream<Object> toStream(boolean parallel) {
                return Stream.empty();
            }
        };

        /**
         * <p>
         * Converts into a {@linkplain Collection}.
         * </p>
         * 
         * @return converted {@linkplain Collection} after converting
         * @since 0.0.0
         */
        default Collection<E> toCollection() {
            return toSet();
        }

        /**
         * <p>
         * Converts into a {@linkplain Set}.
         * </p>
         * 
         * @return {@linkplain Set} after converting
         * @since 0.0.0
         */
        default Set<E> toSet() {
            return toStream().collect(Collectors.toSet());
        }

        /**
         * <p>
         * Converts into a {@linkplain Iterable}.
         * </p>
         * 
         * @return {@linkplain Iterable} after converting
         * @since 0.0.0
         */
        default Iterable<E> toIterable() {
            return toCollection();
        }

        /**
         * <p>
         * Converts into a {@linkplain Iterator}.
         * </p>
         * 
         * @return {@linkplain Iterator} after converting
         * @since 0.0.0
         */
        default Iterator<E> toIterator() {
            return toIterable().iterator();
        }

        /**
         * <p>
         * Converts into a {@linkplain List}. There are no guarantees on the type, mutability, serializability, or
         * thread-safety of the List returned.
         * </p>
         * 
         * @return {@linkplain List} after converting
         * @since 0.0.0
         */
        default List<E> toList() {
            return toStream().collect(Collectors.toList());
        }

        /**
         * <p>
         * Converts into a {@linkplain Spliterator}.
         * </p>
         * 
         * @return {@linkplain Spliterator} after converting
         * @since 0.0.0
         */
        public Spliterator<E> toSpliterator();

        /**
         * <p>
         * Converts into a un-parallel {@linkplain Stream}.
         * </p>
         * 
         * @return un-parallel {@linkplain Stream} after converting
         * @since 0.0.0
         */
        default Stream<E> toStream() {
            return toStream(false);
        }

        /**
         * <p>
         * Converts into a {@linkplain Stream}.
         * </p>
         * 
         * @param parallel
         *            whether returned stream is parallel
         * @return {@linkplain Stream} after converting
         * @since 0.0.0
         */
        default Stream<E> toStream(boolean parallel) {
            return StreamSupport.stream(toSpliterator(), parallel);
        }

        /**
         * <p>
         * Converts into a {@linkplain Supplier}. If the elements end, an {@linkplain NoSuchElementException} thrown.
         * </p>
         * 
         * @return {@linkplain Supplier} after converting
         * @throws NoSuchElementException
         *             if all elements ended
         * @since 0.0.0
         */
        default Supplier<E> toSupplier() throws NoSuchElementException {
            Iterator<E> it = toIterator();
            return () -> {
                if (it.hasNext()) {
                    return it.next();
                }
                throw new NoSuchElementException();
            };
        }

        /**
         * <p>
         * Converts into a array with given array. If this flow fits in the specified array, it is returned therein.
         * Otherwise, a new array is allocated with the runtime type of the specified array.
         * </p>
         * 
         * @param a
         *            specified array
         * @return an array contains the elements of this flow
         * @since 0.0.0
         */
        default E[] toArray(E[] a) {
            return toList().toArray(a);
        }

        /**
         * <p>
         * Performs given action for each element of this flow.
         * </p>
         * 
         * @param action
         *            given action
         * @since 0.0.0
         */
        default void each(Consumer<? super E> action) {
            toStream().forEach(action);
        }

        /**
         * <p>
         * Performs given action for each element of this flow.
         * </p>
         * 
         * @param action
         *            given action
         * @since 0.0.0
         */
        default void each(QuickConsumer<? super E> action) {
            long[] count = {0};
            each(e -> action.accept(count[0]++, e));
        }

        /**
         * <p>
         * Performs given predicate for each element of this flow. If the predicate returns false, the performance will
         * be broken and a result returned, else continue.
         * </p>
         * 
         * @param action
         *            given predicate
         * @return result of each operation
         * @since 0.0.0
         */
        default EachResult<E> each(Predicate<? super E> predicate) {
            Iterator<E> it = toIterator();
            long count = 0;
            while (it.hasNext()) {
                E value = it.next();
                if (predicate.test(value)) {
                    count++;
                } else {
                    return new EachResult<>(count, value);
                }
            }
            return new EachResult<>(count - 1, true);
        }

        /**
         * <p>
         * Performs given predicate for each element of this flow. If the predicate returns false, the performance will
         * be broken and a result returned, else continue.
         * </p>
         * 
         * @param action
         *            given predicate
         * @return result of each operation
         * @since 0.0.0
         */
        default EachResult<E> each(QuickPredicate<? super E> predicate) {
            Iterator<E> it = toIterator();
            long count = 0;
            while (it.hasNext()) {
                E value = it.next();
                if (predicate.test(count, value)) {
                    count++;
                } else {
                    return new EachResult<>(count, value);
                }
            }
            return new EachResult<>(count - 1, true);
        }

        /**
         * <p>
         * Returns a flow consisting of the results of applying the given function to the elements of this flow.
         * </p>
         * 
         * @param <R>
         *            the element type of the new flow
         * @param mapper
         *            function to apply elements
         * @return a flow consisting of the results of applying the given function to the elements of this flow
         * @since 0.0.0
         */
        default <R> Flow<R> map(Function<? super E, ? extends R> mapper) {
            return flow(toStream().map(mapper));
        }
    }

    /**
     * <p>
     * Class represents result of each methods in {@linkplain Flow}.
     * </p>
     *
     * @param <E>
     *            type of result object
     * 
     * @author Fred Suvn
     * @version 0.0.0, 2016-06-15T12:58:14+08:00
     * @since 0.0.0, 2016-06-15T12:58:14+08:00
     */
    @Immutable
    public static class EachResult<E> {

        private static final EachResult<Object> EMPTY = new EachResult<>(QuickerUniform.INVALID_CODE, null);

        /**
         * <p>
         * Returns an empty result.
         * </p>
         * 
         * @return an empty result
         * @since 0.0.0
         */
        public static <E> EachResult<E> empty() {
            @SuppressWarnings("unchecked")
            EachResult<E> result = (EachResult<E>)EMPTY;
            return result;
        }

        private long index;

        private E value;

        private boolean completeTraversal;

        private EachResult(long index, E value) {
            this.index = index;
            this.value = value;
            this.completeTraversal = false;
        }

        private EachResult(long index, boolean completeTraversal) {
            this.index = index;
            this.completeTraversal = completeTraversal;
            this.value = null;
        }

        /**
         * <p>
         * Returns index of last accessed element in the each method which produces this result. It will return -1 if no
         * element accessed.
         * </p>
         * 
         * @return index of last accessed element
         * @since 0.0.0
         */
        public long getIndex() {
            return index;
        }

        /**
         * <p>
         * Returns an int type of {@linkplain #getLastIndex()}. If the number is greater than
         * {@linkplain Integer#MAX_VALUE}, return {@linkplain Integer#MAX_VALUE}.
         * </p>
         * 
         * @return index of last accessed element as int
         * @since 0.0.0
         */
        public int getIndexAsInt() {
            return index > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int)index;
        }

        /**
         * <p>
         * Returns last accessed element in the each method which produces this result
         * </p>
         * 
         * @return last accessed element in the each method which produces this result
         * @since 0.0.0
         */
        public @Nullable E getValue() {
            return value;
        }

        /**
         * <p>
         * Returns number of processed elements.
         * </p>
         * 
         * @return number of processed elements
         * @since 0.0.0
         */
        public long getProcessNumber() {
            return getIndex() + 1;
        }

        /**
         * <p>
         * Returns whether the each operation of which result is represented by this instance is complete, that means
         * the each operation was never broken.
         * </p>
         * 
         * @return whether the each operation is complete
         * @since 0.0.0
         */
        public boolean isComplete() {
            return completeTraversal;
        }
    }
}
