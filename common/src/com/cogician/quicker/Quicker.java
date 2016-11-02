package com.cogician.quicker;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.annotation.Nonnegative;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.cogician.quicker.configuration.QConfiguration;
import com.cogician.quicker.function.Action;
import com.cogician.quicker.function.EachAction;
import com.cogician.quicker.function.EachConsumer;
import com.cogician.quicker.function.EachPredicate;
import com.cogician.quicker.function.EachPredicateAction;
import com.cogician.quicker.function.PredicateAction;
import com.cogician.quicker.log.QLogger;
import com.cogician.quicker.struct.ValueWrapper;
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

    private static final QConfiguration properties = QuickerProperties.getProperties();

    private static final int LOGGING_LEVEL = properties.getInt("log.level");

    /**
     * <p>
     * Logs specified info. The log level is configured in config.properties.
     * </p>
     * 
     * @param info
     *            specified info
     * @since 0.0.0
     */
    public static void log(String info) {
        log().log(LOGGING_LEVEL, info);
    }

    /**
     * Custom objects.
     */
    private static final ThreadLocal<Map<Object, Object>> currentLocal = ThreadLocal.withInitial(() -> new HashMap<>());

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
        return currentLocal.get().put(key, value);
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
        return currentLocal.get().get(key);
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
        return currentLocal.get().remove(key);
    }

    /**
     * <p>
     * Removes all of the mappings from current thread.
     * </p>
     * 
     * @since 0.0.0
     */
    public static void clear() {
        currentLocal.get().clear();
    }

    /**
     * Cache size for common quick function.
     */
    private static final int QUICK_CACHE = 10;

    /**
     * Array to store common quick function.
     */
    private static final ThreadLocal<Object[]> quickLocal = ThreadLocal.withInitial(() -> new Object[QUICK_CACHE]);

    private static int localIndexCounter = 0;

    private static final int LOCAL_INDEX_CLOCKER = localIndexCounter++;

    private static final int CLOCKER_INDEX_MILLIS = 0;

    private static final int CLOCKER_INDEX_NANO = 1;

    private static long[] getClocker() {
        Object[] local = quickLocal.get();
        Object clocker = local[LOCAL_INDEX_CLOCKER];
        if (clocker == null) {
            clocker = new long[2];
            local[LOCAL_INDEX_CLOCKER] = clocker;
        }
        return (long[])clocker;
    }

    /**
     * <p>
     * Returns milliseconds difference between this calling and last calling in current thread. The first calling in
     * current thread will return 0.
     * </p>
     * <p>
     * For example:
     * 
     * <pre>
     * long l1 = Quicker.clockMillis();
     * do something...
     * long l2 = Quicker.clockMillis();
     * System.out.println(l1);
     * System.out.println(l2);
     * </pre>
     * 
     * In above codes, l1 will be printed into 0 (if it is the first calling in current thread), and l2 will be the
     * milliseconds cost of "do something". it is equivalent to:
     * 
     * <pre>
     * long l1 = System.currentTimeMillis();
     * do something...
     * long l2 = System.currentTimeMillis();
     * System.out.println(0);
     * System.out.println(l2 - l1);
     * </pre>
     * 
     * </p>
     * 
     * @since 0.0.0
     */
    public static long clockMillis() {
        long l = System.currentTimeMillis();
        long[] clock = getClocker();
        if (clock[CLOCKER_INDEX_MILLIS] == 0) {
            clock[CLOCKER_INDEX_MILLIS] = System.currentTimeMillis();
            return 0L;
        } else {
            long result = l - clock[CLOCKER_INDEX_MILLIS];
            clock[CLOCKER_INDEX_MILLIS] = System.currentTimeMillis();
            return result;
        }
    }

    /**
     * <p>
     * Returns nanoseconds difference between this calling and last calling in current thread. The first calling in
     * current thread will return 0.
     * </p>
     * <p>
     * For example:
     * 
     * <pre>
     * long l1 = Quicker.clockNano();
     * do something...
     * long l2 = Quicker.clockNano();
     * System.out.println(l1);
     * System.out.println(l2);
     * </pre>
     * 
     * In above codes, l1 will be printed into 0 (if it is the first calling in current thread), and l2 will be the
     * nanoseconds cost of "do something". it is equivalent to:
     * 
     * <pre>
     * long l1 = System.nanoTime();
     * do something...
     * long l2 = System.nanoTime();
     * System.out.println(0);
     * System.out.println(l2 - l1);
     * </pre>
     * 
     * </p>
     * 
     * @since 0.0.0
     */
    public static long clockNano() {
        long l = System.nanoTime();
        long[] clock = getClocker();
        if (clock[CLOCKER_INDEX_NANO] == 0) {
            clock[CLOCKER_INDEX_NANO] = System.nanoTime();
            return 0L;
        } else {
            long result = l - clock[CLOCKER_INDEX_NANO];
            clock[CLOCKER_INDEX_NANO] = System.nanoTime();
            return result;
        }
    }

    private static final int LOCAL_INDEX_CALCULATOR = localIndexCounter++;

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
        Object[] local = quickLocal.get();
        Object calculator = local[LOCAL_INDEX_CALCULATOR];
        if (calculator == null) {
            calculator = new Calculator();
            local[LOCAL_INDEX_CALCULATOR] = calculator;
        }
        return ((Calculator)calculator).calculate(expr);
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
     * Returns given integer if given integer > 0.
     * </p>
     * 
     * @param i
     *            given integer
     * @return given integer if given integer > 0
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
     * Returns given long integer if given long integer > 0.
     * </p>
     * 
     * @param i
     *            given long integer
     * @return given long integer if given long integer > 0
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
     * Returns given integer if given integer >= 0.
     * </p>
     * 
     * @param i
     *            given integer
     * @return given integer if given integer >= 0
     * @throws IllegalArgumentException
     *             if given integer < 0
     * @since 0.0.0
     */
    public static int requirePositiveOr0(int i) throws IllegalArgumentException {
        Checker.checkPositiveOr0(i);
        return i;
    }

    /**
     * <p>
     * Returns given long integer if given long integer >= 0.
     * </p>
     * 
     * @param i
     *            given long integer
     * @return given long integer if given long integer >= 0
     * @throws IllegalArgumentException
     *             if given long integer < 0
     * @since 0.0.0
     */
    public static long requirePositiveOr0(long i) throws IllegalArgumentException {
        Checker.checkPositiveOr0(i);
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
     *            supplier for null given object
     * @return given required object if it is not null, or {@code ifNull}.get() if it is null
     * @throws NullPointerException
     *             if given required object and {@code ifNull} (or its get()) are both null
     * @since 0.0.0
     */
    public static <T> T require(@Nullable T required, @Nullable Supplier<? extends T> ifNull)
            throws NullPointerException {
        return required == null ? require(require(ifNull).get()) : required;
    }

    /**
     * <p>
     * Returns required object associated by given object. If given object is null, return {@code ifNull}.get().
     * Otherwise return {@code ifNonNull}.apply(given). Both supplier and function cannot return a null.
     * </p>
     * 
     * @param <T>
     *            type of given object
     * @param <R>
     *            type of required object
     * @param given
     *            given object
     * @param ifNull
     *            required object supplier for null given object
     * @param ifNonnull
     *            required object function for non-null given object
     * @return {@code ifNull}.get() if given object is null, else {@code ifNonNull}.apply(given)
     * @throws NullPointerException
     *             if needed one of {@code ifNull} or {@code ifNonNull} is null
     * @since 0.0.0
     */
    public static <T, R> R require(@Nullable T given, @Nullable Supplier<? extends R> ifNull,
            @Nullable Function<? super T, ? extends R> ifNonnull) throws NullPointerException {
        return given == null ? require(require(ifNull).get()) : require(require(ifNonnull).apply(given));
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
     * Returns required object associated by given object. If given object and {@code ifNonNull} are null, return null.
     * Otherwise return {@code ifNonNull}.apply(given).
     * </p>
     * 
     * @param <T>
     *            type of given object
     * @param <R>
     *            type of required object
     * @param given
     *            given object
     * @param ifNonnull
     *            required object function for non-null given object
     * @return {@code ifNonNull}.apply(given) if given object and {@code ifNonNull} are non-null, else null
     * @since 0.0.0
     */
    public static @Nullable <T, R> R tryRequire(@Nullable T given,
            @Nullable Function<? super T, ? extends R> ifNonnull) {
        if (given == null || ifNonnull == null) {
            return null;
        }
        return ifNonnull.apply(given);
    }

    /**
     * <p>
     * Returns required object associated by given object. If given object is null, return {@code ifNull}.get().
     * Otherwise return {@code ifNonNull}.apply(given). This method is weaker than
     * {@linkplain #require(Object, Supplier, Function)} because {@code ifNull} and {@code ifNonNull} and their returns
     * are permitted to be null. If {@code ifNull} or {@code ifNonNull} is null, it will be seen as a supplier or
     * function which returns a null.
     * </p>
     * 
     * @param <T>
     *            type of given object
     * @param <R>
     *            type of required object
     * @param given
     *            given object
     * @param ifNull
     *            required object supplier for null given object
     * @param ifNonNull
     *            required object function for non-null given object
     * @return {@code ifNull}.get() if given object is null, else {@code ifNonNull}.apply(given)
     * @since 0.0.0
     */
    public static @Nullable <T, R> R tryRequire(@Nullable T given, @Nullable Supplier<? extends R> ifNull,
            @Nullable Function<? super T, ? extends R> ifNonNull) {
        return given == null ? (ifNull == null ? null : ifNull.get())
                : (ifNonNull == null ? null : ifNonNull.apply(given));
    }

    /**
     * <p>
     * Tries to return the non-null argument of given arguments in parameters order. If all arguments are null, return
     * null.
     * </p>
     * 
     * @param <T>
     *            type of given object
     * @param arg0
     *            first argument
     * @param arg1
     *            second argument
     * @return first non-null argument of given arguments or null if not found
     * @since 0.0.0
     */
    public static @Nullable <T> T tryRequire(@Nullable T arg0, @Nullable T arg1) {
        if (arg0 != null) {
            return arg0;
        }
        if (arg1 != null) {
            return arg1;
        }
        return null;
    }

    /**
     * <p>
     * Tries to return the non-null argument of given arguments in parameters order. If all arguments are null, return
     * null.
     * </p>
     * 
     * @param <T>
     *            type of given object
     * @param arg0
     *            first argument
     * @param arg1
     *            second argument
     * @param arg3
     *            third argument
     * @return first non-null argument of given arguments or null if not found
     * @since 0.0.0
     */
    public static @Nullable <T> T tryRequire(@Nullable T arg0, @Nullable T arg1, @Nullable T arg2) {
        if (arg0 != null) {
            return arg0;
        }
        if (arg1 != null) {
            return arg1;
        }
        if (arg2 != null) {
            return arg2;
        }
        return null;
    }

    /**
     * <p>
     * Tries to return the non-null argument of given arguments in parameters order. If all arguments are null, return
     * null.
     * </p>
     * 
     * @param <T>
     *            type of given object
     * @param args
     *            given arguments
     * @return first non-null argument of given arguments or null if not found
     * @since 0.0.0
     */
    @SafeVarargs
    public static @Nullable <T> T tryRequire(@Nullable T... args) {
        return each(args, t -> t == null).getLastValue();
    }

    /**
     * <p>
     * Tries to return the non-null argument of given argument suppliers in parameters order. If all suppliers or get()
     * of suppliers are null, return null.
     * </p>
     * 
     * @param <T>
     *            type of given object
     * @param s0
     *            first supplier
     * @param s1
     *            second supplier
     * @return first non-null argument of given argument suppliers or null if not found
     * @since 0.0.0
     */
    public static @Nullable <T> T tryRequire(@Nullable Supplier<? extends T> s0, @Nullable Supplier<? extends T> s1) {
        T ret = null;
        if (s0 != null) {
            ret = s0.get();
            if (ret != null) {
                return ret;
            }
        }
        if (s1 != null) {
            ret = s1.get();
            if (ret != null) {
                return ret;
            }
        }
        return null;
    }

    /**
     * <p>
     * Tries to return the non-null argument of given argument suppliers in parameters order. If all suppliers or get()
     * of suppliers are null, return null.
     * </p>
     * 
     * @param <T>
     *            type of given object
     * @param s0
     *            first supplier
     * @param s1
     *            second supplier
     * @param s2
     *            third supplier
     * @return first non-null argument of given argument suppliers or null if not found
     * @since 0.0.0
     */
    public static @Nullable <T> T tryRequire(@Nullable Supplier<? extends T> s0, @Nullable Supplier<? extends T> s1,
            @Nullable Supplier<? extends T> s2) {
        T ret = null;
        if (s0 != null) {
            ret = s0.get();
            if (ret != null) {
                return ret;
            }
        }
        if (s1 != null) {
            ret = s1.get();
            if (ret != null) {
                return ret;
            }
        }
        if (s2 != null) {
            ret = s2.get();
            if (ret != null) {
                return ret;
            }
        }
        return null;
    }

    /**
     * <p>
     * Tries to return the non-null argument of given argument suppliers in parameters order. If all suppliers or get()
     * of suppliers are null, return null.
     * </p>
     * 
     * @param <T>
     *            type of given object
     * @param args
     *            given argument suppliers
     * @return first non-null argument of given argument suppliers or null if not found
     * @since 0.0.0
     */
    @SafeVarargs
    public static @Nullable <T> T tryRequire(@Nullable Supplier<T>... args) {
        Supplier<T> st = each(args, s -> !(s != null && s.get() != null)).getLastValue();
        return tryRequire(st, sup -> sup.get());
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
     * Returns an iterator backed by given array. If given array is null, return an empty iterator.
     * </p>
     * 
     * @param <E>
     *            component type
     * @param array
     *            given array
     * @return an iterator backed by given array
     * @since 0.0.0
     */
    public static <E> Iterator<? super E> iterator(@Nullable E[] array) {
        return array == null ? Consts.emptyIterator() : Arrays.asList(array).iterator();
    }

    /**
     * <p>
     * Returns an iterator of which elements are backed and converted from given iterator and converter. If given
     * iterator is null, return an empty iterator.
     * </p>
     * 
     * @param <T>
     *            component type of given iterator
     * @param <R>
     *            component type of returned iterator
     * @param iterator
     *            given iterator
     * @param converter
     *            given converter
     * @return an iterator of which elements are converted from given iterator and converter
     * @throws NullPointerException
     *             if given converter is null when it is needed
     * @since 0.0.0
     */
    public static <T, R> Iterator<R> iterator(@Nullable Iterator<T> iterator,
            Function<? super T, ? extends R> converter) throws NullPointerException {
        if (iterator == null) {
            return Consts.emptyIterator();
        }
        Checker.checkNull(converter);
        return upperCast(stream(iterator).map(converter).iterator());
    }

    /**
     * <p>
     * Returns an iterable backed by given iterator. If given iterator is null, return an empty iterable.
     * </p>
     * 
     * @param <E>
     *            component type
     * @param spliterator
     *            given iterator
     * @return an iterable backed by given iterator
     * @since 0.0.0
     */
    public static <E> Iterator<E> iterator(@Nullable Spliterator<? extends E> spliterator) {
        return spliterator == null ? Consts.emptyIterator() : Spliterators.iterator(spliterator);
    }

    /**
     * <p>
     * Returns an endless iterator backed by given supplier. If given supplier is null, return an empty iterator.
     * </p>
     * 
     * @param <E>
     *            component type
     * @param supplier
     *            given supplier
     * @return an endless iterator backed by given supplier
     * @since 0.0.0
     */
    public static <E> Iterator<E> iterator(@Nullable Supplier<? extends E> supplier) {
        if (supplier == null) {
            return Consts.emptyIterator();
        }
        return upperCast(Stream.generate(supplier).iterator());
    }

    /**
     * <p>
     * Returns an iterator backed by given supplier with specified element count. If given supplier is null, return an
     * empty iterator.
     * </p>
     * 
     * @param <E>
     *            component type
     * @param supplier
     *            given supplier
     * @param count
     *            specified element count
     * @return an iterator backed by given supplier with specified element count
     * @since 0.0.0
     */
    public static <E> Iterator<E> iterator(@Nullable Supplier<? extends E> supplier, long count) {
        if (supplier == null) {
            return Consts.emptyIterator();
        }
        return upperCast(Stream.generate(supplier).limit(count).iterator());
    }

    /**
     * <p>
     * Returns a spliterator backed by given array. If given array is null, return an empty spliterator.
     * </p>
     * 
     * @param <E>
     *            component type
     * @param array
     *            given array
     * @return a spliterator backed by given array
     * @since 0.0.0
     */
    public static <E> Spliterator<E> spliterator(@Nullable E[] array) {
        return array == null ? Consts.emptySpliterator() : Arrays.spliterator(array);
    }

    /**
     * <p>
     * Returns an spliterator of which elements are backed and converted from given spliterator and converter. If given
     * spliterator is null, return an empty spliterator. If returned spliterator is sorted, its
     * {@linkplain Spliterator#getComparator()} will return null; else throw {@linkplain IllegalStateException}.
     * </p>
     * 
     * @param <T>
     *            component type of given spliterator
     * @param <R>
     *            component type of returned spliterator
     * @param spliterator
     *            given spliterator
     * @param converter
     *            given converter
     * @return an spliterator of which elements are converted from given spliterator and converter
     * @throws NullPointerException
     *             if given converter is null when it is needed
     * @since 0.0.0
     */
    public static <T, R> Spliterator<R> spliterator(@Nullable Spliterator<T> spliterator,
            Function<? super T, ? extends R> converter) throws NullPointerException {
        if (spliterator == null) {
            return Consts.emptySpliterator();
        }
        Checker.checkNull(converter);
        return upperCast(stream(spliterator).map(converter).spliterator());
    }

    /**
     * <p>
     * Returns a spliterator backed by given iterator. If given iterator is null, return an empty spliterator.
     * </p>
     * 
     * @param <E>
     *            component type
     * @param iterator
     *            given iterator
     * @return a spliterator backed by given iterator
     * @since 0.0.0
     */
    public static <E> Spliterator<E> spliterator(@Nullable Iterator<? extends E> iterator) {
        return iterator == null ? Consts.emptySpliterator() : Spliterators.spliteratorUnknownSize(iterator, 0);
    }

    /**
     * <p>
     * Returns an endless spliterator backed by given supplier. If given supplier is null, return an empty spliterator;
     * else the returned spliterator is endless.
     * </p>
     * 
     * @param <E>
     *            component type
     * @param supplier
     *            given supplier
     * @return an endless spliterator backed by given supplier
     * @since 0.0.0
     */
    public static <E> Spliterator<E> spliterator(@Nullable Supplier<? extends E> supplier) {
        return upperCast(Stream.generate(supplier).spliterator());
    }

    /**
     * <p>
     * Returns a spliterator backed by given supplier with specified element count. If given supplier is null, return an
     * empty spliterator; else the returned spliterator is endless.
     * </p>
     * 
     * @param <E>
     *            component type
     * @param supplier
     *            given supplier
     * @param count
     *            specified element count
     * @return a spliterator backed by given supplier with specified element count
     * @since 0.0.0
     */
    public static <E> Spliterator<E> spliterator(@Nullable Supplier<? extends E> supplier, long count) {
        return upperCast(Stream.generate(supplier).limit(count).spliterator());
    }

    /**
     * <p>
     * Returns an iterator of which elements are non-null elements from given iterator. If given iterator is null,
     * return an empty iterator.
     * </p>
     * 
     * @param <E>
     *            component type
     * @param iterator
     *            given iterator
     * @return an iterator of which elements are non-null elements from given iterator
     * @since 0.0.0
     */
    public static <E> Iterator<E> elementNonnull(@Nullable Iterator<? extends E> iterator) {
        if (null == iterator) {
            return Consts.emptyIterator();
        }
        return upperCast(stream(iterator).filter(e -> e != null).iterator());
    }

    /**
     * <p>
     * Returns an spliterator of which elements are non-null elements from given spliterator. If given spliterator is
     * null, return an empty spliterator.
     * </p>
     * 
     * @param <E>
     *            component type
     * @param spliterator
     *            given spliterator
     * @return an spliterator of which elements are non-null elements from given spliterator
     * @since 0.0.0
     */
    public static <E> Spliterator<E> elementNonnull(@Nullable Spliterator<? extends E> spliterator) {
        if (null == spliterator) {
            return Consts.emptySpliterator();
        }
        return spliterator(elementNonnull(iterator(spliterator)));
    }

    /**
     * <p>
     * Returns a iterable backed by given array. If given array is null, return an empty iterable.
     * </p>
     * 
     * @param <E>
     *            component type
     * @param array
     *            given array
     * @return a iterable backed by given array
     * @since 0.0.0
     */
    public static <E> Iterable<E> iterable(@Nullable E[] array) {
        return iterable(iterator(array));
    }

    /**
     * <p>
     * Returns an iterable backed by given iterator. If given iterator is null, return an empty iterable.
     * </p>
     * 
     * @param <E>
     *            component type
     * @param iterator
     *            given iterator
     * @return an iterable backed by given iterator
     * @since 0.0.0
     */
    public static <E> Iterable<E> iterable(@Nullable Iterator<? extends E> iterator) {
        return () -> nonnull(iterator);
    }

    /**
     * <p>
     * Returns an iterable backed by given spliterator. If given spliterator is null, return an empty iterable.
     * </p>
     * 
     * @param <E>
     *            component type
     * @param spliterator
     *            given spliterator
     * @return an iterable backed by given spliterator
     * @since 0.0.0
     */
    public static <E> Iterable<E> iterable(@Nullable Spliterator<? extends E> spliterator) {
        return () -> iterator(spliterator);
    }

    /**
     * <p>
     * Returns a stream backed by given array. If given array is null, return an empty stream.
     * </p>
     * 
     * @param <E>
     *            component type
     * @param array
     *            given array
     * @return a stream backed by given array
     * @since 0.0.0
     */
    public static <E> Stream<E> stream(@Nullable E[] array) {
        return StreamSupport.stream(spliterator(array), false);
    }

    /**
     * <p>
     * Returns a stream backed by given iterator. If given iterator is null, return an empty stream.
     * </p>
     * 
     * @param <E>
     *            component type
     * @param iterator
     *            given iterator
     * @return a stream backed by given iterator
     * @since 0.0.0
     */
    public static <E> Stream<E> stream(@Nullable Iterator<? extends E> iterator) {
        return StreamSupport.stream(spliterator(iterator), false);
    }

    /**
     * <p>
     * Returns a stream backed by given iterable. If given iterable is null, return an empty stream.
     * </p>
     * 
     * @param <E>
     *            component type
     * @param iterable
     *            given iterable
     * @return a stream backed by given iterator
     * @since 0.0.0
     */
    public static <E> Stream<E> stream(@Nullable Iterable<? extends E> iterable) {
        if (iterable == null) {
            return Stream.empty();
        }
        return StreamSupport.stream(spliterator(iterable.iterator()), false);
    }

    /**
     * <p>
     * Returns a stream backed by given spliterator. If given spliterator is null, return an empty stream.
     * </p>
     * 
     * @param <E>
     *            component type
     * @param spliterator
     *            given spliterator
     * @return a stream backed by given spliterator
     * @since 0.0.0
     */
    public static <E> Stream<E> stream(@Nullable Spliterator<? extends E> spliterator) {
        return StreamSupport.stream(nonnull(spliterator), false);
    }

    /**
     * <p>
     * Returns a stream backed by given spliterator. If given spliterator is null, return an empty stream.
     * </p>
     * 
     * @param <E>
     *            component type
     * @param spliterator
     *            given spliterator
     * @param parallel
     *            whether returned stream is parallel
     * @return a stream backed by given spliterator
     * @since 0.0.0
     */
    public static <E> Stream<E> stream(@Nullable Spliterator<? extends E> spliterator, boolean parallel) {
        return StreamSupport.stream(nonnull(spliterator), parallel);
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
     */
    public static <T> void doIfNonnull(@Nullable T obj, @Nullable Consumer<? super T> action) {
        if (obj != null && action != null) {
            action.accept(obj);
        }
    }

    /**
     * <p>
     * Returns a matcher with given regular expression and string to be matched.
     * </p>
     * 
     * @param regex
     *            given regular expression
     * @param matched
     *            given string to be matched
     * @return a matcher matched by given regular expression and string
     * @throws NullPointerException
     *             if given pattern or string is null
     * @throws PatternSyntaxException
     *             if given pattern's syntax is invalid
     * @since 0.0.0
     */
    public static Matcher match(String regex, String matched) throws NullPointerException, PatternSyntaxException {
        return match(Pattern.compile(require(regex)), matched);
    }

    /**
     * <p>
     * Returns a matcher with given pattern and string to be matched.
     * </p>
     * 
     * @param pattern
     *            given pattern
     * @param matched
     *            given string to be matched
     * @return a matcher matched by given pattern and string
     * @throws NullPointerException
     *             if given pattern or string is null
     * @since 0.0.0
     */
    public static Matcher match(Pattern pattern, String matched) throws NullPointerException {
        return pattern.matcher(require(matched));
    }

    /**
     * <p>
     * Returns whether given string can be completely-matched (all characters) by given regular expression.
     * </p>
     * 
     * @param regex
     *            given regular expression
     * @param matched
     *            given string to be matched
     * @return whether given string can be completely-matched (all characters) by given regular expression
     * @throws NullPointerException
     *             if given pattern or string is null
     * @throws PatternSyntaxException
     *             if given pattern's syntax is invalid
     * @since 0.0.0
     */
    public static boolean completelyMatch(String regex, String matched)
            throws NullPointerException, PatternSyntaxException {
        return completelyMatch(Pattern.compile(require(regex)), matched);
    }

    /**
     * <p>
     * Returns whether given string can be completely-matched (all characters) by given pattern.
     * </p>
     * 
     * @param pattern
     *            given pattern
     * @param matched
     *            given string to be matched
     * @return whether whole given string can be completely-matched by given pattern
     * @throws NullPointerException
     *             if given pattern or string is null
     * @since 0.0.0
     */
    public static boolean completelyMatch(Pattern pattern, String matched) throws NullPointerException {
        Matcher m = match(pattern, matched);
        if (m.find() && m.start() == 0 && m.end() == matched.length()) {
            return true;
        }
        return false;
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
     * Performs given action for each element of given array in index order.
     * </p>
     * 
     * @param <E>
     *            component type of given array
     * @param array
     *            given array
     * @param action
     *            given action
     * @since 0.0.0
     */
    public static <E> void each(@Nullable E[] array, @Nullable Consumer<? super E> action) {
        if (array == null || action == null) {
            return;
        }
        stream(array).forEachOrdered(action);
    }

    /**
     * <p>
     * Performs given action for each element of given array in index order.
     * </p>
     * 
     * @param <E>
     *            component type of given array
     * @param array
     *            given array
     * @param action
     *            given action
     * @since 0.0.0
     */
    public static <E> void each(@Nullable E[] array, @Nullable EachConsumer<? super E> action) {
        if (array == null || action == null) {
            return;
        }
        int[] count = {0};
        stream(array).forEachOrdered(e -> action.accept(count[0]++, e));
    }

    /**
     * <p>
     * Performs given action for each element of given iterator in encounter order.
     * </p>
     * 
     * @param <E>
     *            component type of given iterator
     * @param iterator
     *            given iterator
     * @param action
     *            given action
     * @since 0.0.0
     */
    public static <E> void each(@Nullable Iterator<E> iterator, @Nullable Consumer<? super E> action) {
        each(stream(iterator), action);
    }

    /**
     * <p>
     * Performs given action for each element of given iterator in encounter order.
     * </p>
     * 
     * @param <E>
     *            component type of given iterator
     * @param iterator
     *            given iterator
     * @param action
     *            given action
     * @since 0.0.0
     */
    public static <E> void each(@Nullable Iterator<E> iterator, @Nullable EachConsumer<? super E> action) {
        each(stream(iterator), action);
    }

    /**
     * <p>
     * Performs given action for each element of given spliterator in encounter order.
     * </p>
     * 
     * @param <E>
     *            component type of given spliterator
     * @param spliterator
     *            given spliterator
     * @param action
     *            given action
     * @since 0.0.0
     */
    public static <E> void each(@Nullable Spliterator<E> spliterator, @Nullable Consumer<? super E> action) {
        each(stream(spliterator), action);
    }

    /**
     * <p>
     * Performs given action for each element of given spliterator in encounter order.
     * </p>
     * 
     * @param <E>
     *            component type of given spliterator
     * @param spliterator
     *            given spliterator
     * @param action
     *            given action
     * @since 0.0.0
     */
    public static <E> void each(@Nullable Spliterator<E> spliterator, @Nullable EachConsumer<? super E> action) {
        each(stream(spliterator), action);
    }

    /**
     * <p>
     * Performs given action for each element of given iterable in encounter order.
     * </p>
     * 
     * @param <E>
     *            component type of given iterable
     * @param iterable
     *            given iterable
     * @param action
     *            given action
     * @return actual number of performed times
     * @since 0.0.0
     */
    public static <E> void each(@Nullable Iterable<E> iterable, @Nullable Consumer<? super E> action) {
        each(stream(iterable.iterator()), action);
    }

    /**
     * <p>
     * Performs given action for each element of given iterable in encounter order.
     * </p>
     * 
     * @param <E>
     *            component type of given iterable
     * @param iterable
     *            given iterable
     * @param action
     *            given action
     * @since 0.0.0
     */
    public static <E> void each(@Nullable Iterable<E> iterable, @Nullable EachConsumer<? super E> action) {
        each(stream(iterable.iterator()), action);
    }

    /**
     * <p>
     * Performs given action for each element of given stream in encounter order.
     * </p>
     * 
     * @param <E>
     *            component type of given stream
     * @param stream
     *            given stream
     * @param action
     *            given action
     * @since 0.0.0
     */
    public static <E> void each(@Nullable Stream<E> stream, @Nullable Consumer<? super E> action) {
        if (stream == null || action == null) {
            return;
        }
        stream.forEach(e -> action.accept(e));
    }

    /**
     * <p>
     * Performs given action for each element of given stream in encounter order.
     * </p>
     * 
     * @param <E>
     *            component type of given stream
     * @param stream
     *            given stream
     * @param action
     *            given action
     * @since 0.0.0
     */
    public static <E> void each(@Nullable Stream<E> stream, @Nullable EachConsumer<? super E> action)
            throws NullPointerException {
        if (stream == null || action == null) {
            return;
        }
        long counter[] = {0};
        stream.forEachOrdered(e -> action.accept(counter[0]++, e));
    }

    /**
     * <p>
     * Performs given action for each entry of given map in encounter order.
     * </p>
     * 
     * @param <K>
     *            type of map keys
     * @param <K>
     *            type of map values
     * @param map
     *            given map
     * @param action
     *            given action
     * @since 0.0.0
     */
    public static <K, V> void each(@Nullable Map<K, V> map, @Nullable Consumer<? super Entry<K, V>> action)
            throws NullPointerException {
        each(stream(map.entrySet().iterator()), action);
    }

    /**
     * <p>
     * Performs given action for each entry of given map in encounter order.
     * </p>
     * 
     * @param <K>
     *            type of map keys
     * @param <K>
     *            type of map values
     * @param map
     *            given map
     * @param action
     *            given action
     * @since 0.0.0
     */
    public static <K, V> void each(@Nullable Map<K, V> map, @Nullable EachConsumer<? super Entry<K, V>> action)
            throws NullPointerException {
        each(stream(map.entrySet().iterator()), action);
    }

    /**
     * <p>
     * Performs given predication for each element of given array in index order. Once the predication returns false,
     * the performing will be broken and a result contains current element and its index will be returned.
     * </p>
     * <p>
     * If given array or predication is null, or predication returns true for all elements, a not-found result will be
     * returned.
     * </p>
     * 
     * @param <E>
     *            component type of given array
     * @param array
     *            given array
     * @param predication
     *            given predication
     * @return result contains current element and its index
     * @since 0.0.0
     */
    public static <E> EachResult<E> each(@Nullable E[] array, @Nullable Predicate<? super E> predication) {
        if (array == null || predication == null) {
            return EachResult.empty();
        }
        for (int i = 0; i < array.length; i++) {
            if (!predication.test(array[i])) {
                return new EachResult<>(i, array[i]);
            }
        }
        return new EachResult<>(array.length - 1, true);
    }

    /**
     * <p>
     * Performs given predication for each element of given array in index order. Once the predication returns false,
     * the performing will be broken and a result contains current element and its index will be returned.
     * </p>
     * <p>
     * If given array or predication is null, or predication returns true for all elements, a not-found result will be
     * returned.
     * </p>
     * 
     * @param <E>
     *            component type of given array
     * @param array
     *            given array
     * @param predication
     *            given predication
     * @return result contains current element and its index
     * @since 0.0.0
     */
    public static <E> EachResult<E> each(@Nullable E[] array, @Nullable EachPredicate<? super E> predication) {
        if (array == null || predication == null) {
            return EachResult.empty();
        }
        for (int i = 0; i < array.length; i++) {
            if (!predication.test(i, array[i])) {
                return new EachResult<>(i, array[i]);
            }
        }
        return new EachResult<>(array.length - 1, true);
    }

    /**
     * <p>
     * Performs given predication for each element of given iterator in encounter order. Once the predication returns
     * false, the performing will be broken and a result contains current element and its index will be returned.
     * </p>
     * <p>
     * If given iterator or predication is null, or predication returns true for all elements, a not-found result will
     * be returned.
     * </p>
     * 
     * @param <E>
     *            component type of given iterator
     * @param iterator
     *            given iterator
     * @param predication
     *            given predication
     * @return result contains current element and its index
     * @since 0.0.0
     */
    public static <E> EachResult<E> each(@Nullable Iterator<E> iterator, @Nullable Predicate<? super E> predication) {
        return each(iterable(iterator), predication);
    }

    /**
     * <p>
     * Performs given predication for each element of given iterator in encounter order. Once the predication returns
     * false, the performing will be broken and a result contains current element and its index will be returned.
     * </p>
     * <p>
     * If given iterator or predication is null, or predication returns true for all elements, a not-found result will
     * be returned.
     * </p>
     * 
     * @param <E>
     *            component type of given iterator
     * @param iterator
     *            given iterator
     * @param predication
     *            given predication
     * @return result contains current element and its index
     * @since 0.0.0
     */
    public static <E> EachResult<E> each(@Nullable Iterator<E> iterator,
            @Nullable EachPredicate<? super E> predication) {
        return each(iterable(iterator), predication);
    }

    /**
     * <p>
     * Performs given predication for each element of given spliterator in encounter order. Once the predication returns
     * false, the performing will be broken and a result contains current element and its index will be returned.
     * </p>
     * <p>
     * If given spliterator or predication is null, or predication returns true for all elements, a not-found result
     * will be returned.
     * </p>
     * 
     * @param <E>
     *            component type of given spliterator
     * @param spliterator
     *            given spliterator
     * @param predication
     *            given predication
     * @return result contains current element and its index
     * @since 0.0.0
     */
    public static <E> EachResult<E> each(@Nullable Spliterator<E> spliterator,
            @Nullable Predicate<? super E> predication) {
        return each(iterable(spliterator), predication);
    }

    /**
     * <p>
     * Performs given predication for each element of given spliterator in encounter order. Once the predication returns
     * false, the performing will be broken and a result contains current element and its index will be returned.
     * </p>
     * <p>
     * If given spliterator or predication is null, or predication returns true for all elements, a not-found result
     * will be returned.
     * </p>
     * 
     * @param <E>
     *            component type of given spliterator
     * @param spliterator
     *            given spliterator
     * @param predication
     *            given predication
     * @return result contains current element and its index
     * @since 0.0.0
     */
    public static <E> EachResult<E> each(@Nullable Spliterator<E> spliterator,
            @Nullable EachPredicate<? super E> predication) {
        return each(iterable(spliterator), predication);
    }

    /**
     * <p>
     * Performs given predication for each element of given iterable in encounter order. Once the predication returns
     * false, the performing will be broken and a result contains current element and its index will be returned.
     * </p>
     * <p>
     * If given iterable or predication is null, or predication returns true for all elements, a not-found result will
     * be returned.
     * </p>
     * 
     * @param <E>
     *            component type of given iterable
     * @param iterable
     *            given iterable
     * @param predication
     *            given predication
     * @return result contains current element and its index
     * @since 0.0.0
     */
    public static <E> EachResult<E> each(@Nullable Iterable<E> iterable, @Nullable Predicate<? super E> predication) {
        if (iterable == null || predication == null) {
            return EachResult.empty();
        }
        long count = 0;
        Iterator<E> iterator = iterable.iterator();
        while (iterator.hasNext()) {
            E e = iterator.next();
            if (!predication.test(e)) {
                return new EachResult<E>(count, e);
            }
            count++;
        }
        return new EachResult<E>(count - 1, true);
    }

    /**
     * <p>
     * Performs given predication for each element of given iterable in encounter order. Once the predication returns
     * false, the performing will be broken and a result contains current element and its index will be returned.
     * </p>
     * <p>
     * If given iterable or predication is null, or predication returns true for all elements, a not-found result will
     * be returned.
     * </p>
     * 
     * @param <E>
     *            component type of given iterable
     * @param iterable
     *            given iterable
     * @param predication
     *            given predication
     * @return result contains current element and its index
     * @since 0.0.0
     */
    public static <E> EachResult<E> each(@Nullable Iterable<E> iterable,
            @Nullable EachPredicate<? super E> predication) {
        if (iterable == null || predication == null) {
            return EachResult.empty();
        }
        long count = 0;
        Iterator<E> iterator = iterable.iterator();
        while (iterator.hasNext()) {
            E e = iterator.next();
            if (!predication.test(count, e)) {
                return new EachResult<E>(count, e);
            }
            count++;
        }
        return new EachResult<E>(count - 1, true);
    }

    /**
     * <p>
     * Performs given predication for each element of given stream in encounter order. Once the predication returns
     * false, the performing will be broken and a result contains current element and its index will be returned.
     * </p>
     * <p>
     * If given stream or predication is null, or predication returns true for all elements, a not-found result will be
     * returned.
     * </p>
     * 
     * @param <E>
     *            component type of given stream
     * @param stream
     *            given stream
     * @param predication
     *            given predication
     * @return result contains current element and its index
     * @since 0.0.0
     */
    public static <E> EachResult<E> each(@Nullable Stream<E> stream,
            @Nullable Predicate<? super E> predication) {
        if (stream == null || predication == null) {
            return EachResult.empty();
        }
        if (stream.isParallel()){
            return null;
        } else {
            return each(stream.iterator(), predication);
        }
    }

    /**
     * <p>
     * Performs given predication for each element of given stream in encounter order. Once the predication returns
     * false, the performing will be broken and a result contains current element and its index will be returned.
     * </p>
     * <p>
     * If given stream or predication is null, or predication returns true for all elements, a not-found result will be
     * returned.
     * </p>
     * 
     * @param <E>
     *            component type of given stream
     * @param stream
     *            given stream
     * @param predication
     *            given predication
     * @return result contains current element and its index
     * @since 0.0.0
     */
    public static <E> EachResult<E> each(@Nullable Stream<E> stream, @Nullable EachPredicate<? super E> predication) {
        if (stream == null || predication == null) {
            return EachResult.empty();
        }
        if (stream.isParallel()){
            return null;
        } else {
            return each(stream.iterator(), predication);
        }
    }

    /**
     * <p>
     * Performs given predication for each entry of given map in encounter order. Once the predication returns false,
     * the performing will be broken and a result contains current entry and its index will be returned.
     * </p>
     * <p>
     * If given map or predication is null, or predication returns true for all entries, a not-found result will be
     * returned.
     * </p>
     * 
     * @param <K>
     *            type of map keys
     * @param <K>
     *            type of map values
     * @param map
     *            given map
     * @param predication
     *            given predication
     * @return result contains current entry and its index
     * @since 0.0.0
     */
    public static <K, V> EachResult<Entry<K, V>> each(@Nullable Map<K, V> map,
            @Nullable Predicate<? super Entry<K, V>> predication) {
        return each(map.entrySet(), predication);
    }

    /**
     * <p>
     * Performs given predication for each entry of given map in encounter order. Once the predication returns false,
     * the performing will be broken and a result contains current entry and its index will be returned.
     * </p>
     * <p>
     * If given map or predication is null, or predication returns true for all entries, a not-found result will be
     * returned.
     * </p>
     * 
     * @param <K>
     *            type of map keys
     * @param <K>
     *            type of map values
     * @param map
     *            given map
     * @param predication
     *            given predication
     * @return result contains current entry and its index
     * @since 0.0.0
     */
    public static <K, V> EachResult<Entry<K, V>> each(@Nullable Map<K, V> map,
            @Nullable EachPredicate<? super Entry<K, V>> predication) {
        if (map == null || predication == null) {
            return EachResult.empty();
        }
        long[] counter = {0};
        return each(map, (e) -> {
            return predication.test(counter[0]++, e);
        });
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
    public static void each(long times, @Nullable Action action) {
        if (times == 0 || action == null) {
            return;
        }
        Stream<Object> stream = Stream.generate(() -> (Object)null);
        if (times < 0) {
            stream.forEachOrdered(e -> action.perform());
        } else {
            stream.limit(times).forEachOrdered(e -> action.perform());
        }
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
    public static void each(long times, @Nullable EachAction action) {
        if (times == 0 || action == null) {
            return;
        }
        long[] count = {0};
        Stream<Object> stream = Stream.generate(() -> (Object)null);
        if (times < 0) {
            stream.forEachOrdered(e -> action.perform(count[0]++));
        } else {
            stream.limit(times).forEachOrdered(e -> action.perform(count[0]++));
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
    public static long each(long times, @Nullable PredicateAction action) {
        return each(times, (i) -> {
            return action.perform();
        });
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
    public static long each(long times, @Nullable EachPredicateAction action) {
        if (times == 0 || action == null) {
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
     * Class represents result of each methods in {@linkplain Quicker}.
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

        private static final EachResult<Object> EMPTY = new EachResult<>(Uniforms.INVALID_CODE, null);

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

        @Nullable
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
         * Returns index of this result. If returned index is {@linkplain Uniforms#INVALID_CODE}, it means there is no
         * result found.
         * </p>
         * 
         * @return index of this result
         * @since 0.0.0
         */
        public long getIndex() {
            return index;
        }

        /**
         * <p>
         * Returns index of this result as int. If the index is > {@linkplain Integer#MAX_VALUE}, return
         * {@linkplain Integer#MAX_VALUE}. If returned index is {@linkplain Uniforms#INVALID_CODE}, it means there is no
         * result found.
         * </p>
         * 
         * @return index of this result as int
         * @since 0.0.0
         */
        public int getIndexAsInt() {
            return index > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int)index;
        }

        /**
         * <p>
         * Returns last element which cause the each operation broken. It maybe return null if the each operation
         * complete.
         * </p>
         * 
         * @return last element which cause the each operation broken
         * @since 0.0.0
         */
        public @Nullable E getLastValue() {
            return value;
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

    /**
     * <p>
     * Traverse and returns count of all elements of given iterator. Given iterator will be used up after traversing.
     * </p>
     * 
     * @param iterator
     *            given iterator
     * @return count of all elements of given iterator
     * @since 0.0.0
     */
    public static long countElements(@Nullable Iterator<?> iterator) {
        return iterator == null ? 0 : stream(iterator).count();
    }

    /**
     * <p>
     * Traverse and returns count of all elements of given spliterator. Given spliterator will be used up after
     * traversing.
     * </p>
     * 
     * @param spliterator
     *            given spliterator
     * @return count of all elements of given spliterator
     * @since 0.0.0
     */
    public static long countElements(@Nullable Spliterator<?> spliterator) {
        return spliterator == null ? 0 : stream(spliterator).count();
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
    public static String capitalizeInitials(String str) throws NullPointerException {
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
        Quicker.each(length, () -> {
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
}
