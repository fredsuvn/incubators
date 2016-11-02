package com.cogician.quicker.struct;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import java.util.function.Function;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.cogician.quicker.Checker;
import com.cogician.quicker.Quicker;
import com.cogician.quicker.util.lexer.ParsingException;

/**
 * <p>
 * A wrapper which is convenient to convert wrapped value into different type such as String, Integer or others.
 * </p>
 *
 * @param <T>
 *            type of wrapped object
 * 
 * @author Fred Suvn
 * @version 0.0.0, 2016-04-14T11:08:32+08:00
 * @since 0.0.0, 2016-04-14T11:08:32+08:00
 */
@Immutable
public interface ValueOf<T> extends ValueWrapper<T> {

    /**
     * <p>
     * Returns a {@code ValueOf} with given value.
     * </p>
     * 
     * @param value
     *            given value
     * @return an instance of ValueOf with given value
     * @since 0.0.0
     */
    public static <T> ValueOf<T> wrap(@Nullable T value) {
        return Quicker.require(value, NullValueOf::singleInstance, e -> new CachedValueOf<>(e));
    }

    /**
     * <p>
     * Returns original wrapped value.
     * </p>
     * 
     * @return original wrapped value
     * @since 0.0.0
     */
    public @Nullable T getValue();

    /**
     * <p>
     * Returns {@linkplain String#valueOf(Object)} of wrapped object.
     * </p>
     * 
     * @return {@linkplain String#valueOf(Object)} of wrapped object
     * @since 0.0.0
     */
    public @Nullable String asString();

    /**
     * <p>
     * Returns boolean value of wrapped object. If wrapped object is null, return false.
     * </p>
     * 
     * @return boolean value of wrapped object
     * @since 0.0.0
     */
    public boolean asBoolean();

    /**
     * <p>
     * Returns byte value of wrapped object, supporting arithmetic expression. If wrapped object is null, return 0.
     * </p>
     * 
     * @return byte value of wrapped object
     * @throws NumberFormatException
     *             if wrapped object cannot as byte
     * @throws ParsingException
     *             if failed to parse wrapped object
     * @since 0.0.0
     */
    public byte asByte() throws NumberFormatException, ParsingException;

    /**
     * <p>
     * Returns short value of wrapped object, supporting arithmetic expression. If wrapped object is null, return 0.
     * </p>
     * 
     * @return short value of wrapped object
     * @throws NumberFormatException
     *             if wrapped object cannot as short
     * @throws ParsingException
     *             if failed to parse wrapped object
     * @since 0.0.0
     */
    public short asShort() throws NumberFormatException, ParsingException;

    /**
     * <p>
     * Returns char value of wrapped object. This method uses {@linkplain #asString()} and returns first char of the
     * result. If the result is null or empty, return 0.
     * </p>
     * 
     * @return char value of wrapped object
     * @since 0.0.0
     */
    public char asChar();

    /**
     * <p>
     * Returns int value of wrapped object, supporting arithmetic expression. If wrapped object is null, return 0.
     * </p>
     * 
     * @return int value of wrapped object
     * @throws NumberFormatException
     *             if wrapped object cannot as int
     * @throws ParsingException
     *             if failed to parse wrapped object
     * @since 0.0.0
     */
    public int asInt() throws NumberFormatException, ParsingException;

    /**
     * <p>
     * Returns float value of wrapped object, supporting arithmetic expression. If wrapped object is null, return 0.
     * </p>
     * 
     * @return float value of wrapped object
     * @throws NumberFormatException
     *             if wrapped object cannot as float
     * @throws ParsingException
     *             if failed to parse wrapped object
     * @since 0.0.0
     */
    public float asFloat() throws NumberFormatException, ParsingException;

    /**
     * <p>
     * Returns long value of wrapped object, supporting arithmetic expression. If wrapped object is null, return 0.
     * </p>
     * 
     * @return long value of wrapped object
     * @throws NumberFormatException
     *             if wrapped object cannot as long
     * @throws ParsingException
     *             if failed to parse wrapped object
     * @since 0.0.0
     */
    public long asLong() throws NumberFormatException, ParsingException;

    /**
     * <p>
     * Returns double value of wrapped object, supporting arithmetic expression. If wrapped object is null, return 0.
     * </p>
     * 
     * @return double value of wrapped object
     * @throws NumberFormatException
     *             if wrapped object cannot as double
     * @throws ParsingException
     *             if failed to parse wrapped object
     * @since 0.0.0
     */
    public double asDouble() throws NumberFormatException, ParsingException;

    /**
     * <p>
     * Returns big integer value of wrapped object, supporting arithmetic expression. If wrapped object is null, return
     * 0.
     * </p>
     * 
     * @return big integer value of wrapped object
     * @throws NumberFormatException
     *             if wrapped object cannot as big integer
     * @throws ParsingException
     *             if failed to parse wrapped object
     * @since 0.0.0
     */
    public @Nullable BigInteger asBigInteger() throws NumberFormatException, ParsingException;

    /**
     * <p>
     * Returns decimal value of wrapped object, supporting arithmetic expression. If wrapped object is null, return 0.
     * </p>
     * 
     * @return decimal value of wrapped object
     * @throws NumberFormatException
     *             if wrapped object cannot as decimal
     * @throws ParsingException
     *             if failed to parse wrapped object
     * @since 0.0.0
     */
    public @Nullable BigDecimal asBigDecimal() throws NumberFormatException, ParsingException;

    /**
     * <p>
     * Returns converted value of wrapped object by given converter. If given converter is null, an
     * {@linkplain NullPointerException} thrown.
     * </p>
     * 
     * @param converter
     *            given converter
     * @return converted value of wrapped object
     * @throws NullPointerException
     *             if given converter is null
     * @since 0.0.0
     */
    public @Nullable <R> R as(Function<? super T, ? extends R> converter) throws NullPointerException;

    /**
     * <p>
     * Returns an {@linkplain Optional} contains value of this {@linkplain ValueOf}.
     * </p>
     * 
     * @return an {@linkplain Optional} contains value of this {@linkplain ValueOf}
     * @since 0.0.0
     */
    default Optional<T> toOptional() {
        return Optional.ofNullable(getValue());
    }

    static class CachedValueOf<T> implements ValueOf<T> {

        private final T wrapped;

        @Nullable
        private Object cache = null;

        private CachedValueOf(T wrapped) {
            this.wrapped = wrapped;
        }

        @Override
        public T getValue() {
            return wrapped;
        }

        @Override
        public String asString() {
            if (!(cache instanceof String) || cache == null) {
                cache = String.valueOf(getValue());
            }
            return (String)cache;
        }

        @Override
        public boolean asBoolean() {
            if (!(cache instanceof Boolean) || cache == null) {
                cache = Boolean.valueOf(asString());
            }
            return (Boolean)cache;
        }

        @Override
        public byte asByte() throws NumberFormatException, ParsingException {
            if (!(cache instanceof Byte) || cache == null) {
                String str = asString();
                if (!Checker.isNumeric(str)) {
                    cache = Quicker.calculate(str).byteValue();
                } else {
                    cache = Byte.valueOf(str);
                }
            }
            return (Byte)cache;
        }

        @Override
        public short asShort() throws NumberFormatException, ParsingException {
            if (!(cache instanceof Short) || cache == null) {
                String str = asString();
                if (!Checker.isNumeric(str)) {
                    cache = Quicker.calculate(str).shortValue();
                } else {
                    cache = Short.valueOf(str);
                }
            }
            return (Short)cache;
        }

        @Override
        public char asChar() {
            if (!(cache instanceof Character) || cache == null) {
                String str = asString();
                if (Checker.isEmpty(str)) {
                    return 0;
                } else {
                    cache = Character.valueOf(str.charAt(0));
                }
            }
            return (Character)cache;
        }

        @Override
        public int asInt() throws NumberFormatException, ParsingException {
            if (!(cache instanceof Integer) || cache == null) {
                String str = asString();
                if (!Checker.isNumeric(str)) {
                    cache = Quicker.calculate(str).intValue();
                } else {
                    cache = Integer.valueOf(str);
                }
            }
            return (Integer)cache;
        }

        @Override
        public float asFloat() throws NumberFormatException, ParsingException {
            if (!(cache instanceof Float) || cache == null) {
                String str = asString();
                if (!Checker.isNumeric(str)) {
                    cache = Quicker.calculate(str).floatValue();
                } else {
                    cache = Float.valueOf(str);
                }
            }
            return (Float)cache;
        }

        @Override
        public long asLong() throws NumberFormatException, ParsingException {
            if (!(cache instanceof Long) || cache == null) {
                String str = asString();
                if (!Checker.isNumeric(str)) {
                    cache = Quicker.calculate(str).longValue();
                } else {
                    cache = Long.valueOf(str);
                }
            }
            return (Long)cache;
        }

        @Override
        public double asDouble() throws NumberFormatException, ParsingException {
            if (!(cache instanceof Double) || cache == null) {
                String str = asString();
                if (!Checker.isNumeric(str)) {
                    cache = Quicker.calculate(str).doubleValue();
                } else {
                    cache = Double.valueOf(str);
                }
            }
            return (Double)cache;
        }

        @Override
        public BigInteger asBigInteger() throws NumberFormatException, ParsingException {
            if (!(cache instanceof BigInteger) || cache == null) {
                String str = asString();
                if (!Checker.isNumeric(str)) {
                    cache = Quicker.calculate(str).toBigInteger();
                } else {
                    cache = new BigInteger(str);
                }
            }
            return (BigInteger)cache;
        }

        @Override
        public BigDecimal asBigDecimal() throws NumberFormatException, ParsingException {
            if (!(cache instanceof BigDecimal) || cache == null) {
                String str = asString();
                if (!Checker.isNumeric(str)) {
                    cache = Quicker.calculate(str);
                } else {
                    cache = new BigDecimal(str);
                }
            }
            return (BigDecimal)cache;
        }

        @Override
        public @Nullable <R> R as(Function<? super T, ? extends R> converter) throws NullPointerException {
            return Quicker.require(converter).apply(getValue());
        }

        @Override
        public String toString() {
            return getClass() + "{value=" + getValue() + "}";
        }
    }

    static class NullValueOf<T> implements ValueOf<T> {

        private static final NullValueOf<Object> SINGLETON = new NullValueOf<>();

        @SuppressWarnings("unchecked")
        private static <T> NullValueOf<T> singleInstance() {
            return (NullValueOf<T>)SINGLETON;
        }

        private NullValueOf() {

        }

        @Override
        public T getValue() {
            return null;
        }

        @Override
        public String asString() {
            return null;
        }

        @Override
        public boolean asBoolean() {
            return false;
        }

        @Override
        public byte asByte() throws NumberFormatException {
            return 0;
        }

        @Override
        public short asShort() throws NumberFormatException {
            return 0;
        }

        @Override
        public char asChar() {
            return 0;
        }

        @Override
        public int asInt() throws NumberFormatException {
            return 0;
        }

        @Override
        public float asFloat() throws NumberFormatException {
            return 0;
        }

        @Override
        public long asLong() throws NumberFormatException {
            return 0;
        }

        @Override
        public double asDouble() throws NumberFormatException {
            return 0;
        }

        @Override
        public BigInteger asBigInteger() throws NumberFormatException {
            return BigInteger.ZERO;
        }

        @Override
        public BigDecimal asBigDecimal() throws NumberFormatException {
            return BigDecimal.ZERO;
        }

        @Override
        public @Nullable <R> R as(Function<? super T, ? extends R> converter) throws NullPointerException {
            return Quicker.require(converter).apply(null);
        }

        @Override
        public String toString() {
            return getClass() + "{value=" + null + "}";
        }
    }
}
