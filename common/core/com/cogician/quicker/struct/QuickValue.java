package com.cogician.quicker.struct;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import java.util.function.Function;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

/**
 * <p>
 * A wrapper holds a value. this class can conveniently convert wrapped value into different type such as String,
 * Integer or others.
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
public interface QuickValue<T> extends QuickWrapper<T> {

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
    public static <T> QuickValue<T> wrap(@Nullable T value) {
        return value == null ? Null.singleInstance() : new TypeCached<>(value);
    }

    /**
     * <p>
     * Returns wrapped value.
     * </p>
     * 
     * @return wrapped value
     * @since 0.0.0
     */
    public @Nullable T getValue();

    /**
     * <p>
     * Returns wrapped value as string.
     * </p>
     * 
     * @return wrapped value as string
     * @since 0.0.0
     */
    public @Nullable String asString();

    /**
     * <p>
     * Returns wrapped value as boolean.
     * </p>
     * 
     * @return wrapped value as boolean
     * @since 0.0.0
     */
    public boolean asBoolean();

    /**
     * <p>
     * Returns wrapped value as number.
     * </p>
     * 
     * @return wrapped value as number
     * @throws NumberFormatException
     *             if wrapped value cannot be converted to number
     * @since 0.0.0
     */
    public Number asNumber() throws NumberFormatException;

    /**
     * <p>
     * Returns wrapped value as byte.
     * </p>
     * 
     * @return wrapped value as byte
     * @throws NumberFormatException
     *             if wrapped value cannot be converted to byte
     * @since 0.0.0
     */
    public byte asByte() throws NumberFormatException;

    /**
     * <p>
     * Returns wrapped value as short.
     * </p>
     * 
     * @return wrapped value as short
     * @throws NumberFormatException
     *             if wrapped value cannot be converted to short
     * @since 0.0.0
     */
    public short asShort() throws NumberFormatException;

    /**
     * <p>
     * Returns wrapped value as char.
     * </p>
     * 
     * @return wrapped value as char
     * @throws NumberFormatException
     *             if wrapped value cannot be converted to char
     * @since 0.0.0
     */
    public char asChar();

    /**
     * <p>
     * Returns wrapped value as int.
     * </p>
     * 
     * @return wrapped value as int
     * @throws NumberFormatException
     *             if wrapped value cannot be converted to int
     * @since 0.0.0
     */
    public int asInt() throws NumberFormatException;

    /**
     * <p>
     * Returns wrapped value as float.
     * </p>
     * 
     * @return wrapped value as float
     * @throws NumberFormatException
     *             if wrapped value cannot be converted to float
     * @since 0.0.0
     */
    public float asFloat() throws NumberFormatException;

    /**
     * <p>
     * Returns wrapped value as long.
     * </p>
     * 
     * @return wrapped value as long
     * @throws NumberFormatException
     *             if wrapped value cannot be converted to long
     * @since 0.0.0
     */
    public long asLong() throws NumberFormatException;

    /**
     * <p>
     * Returns wrapped value as double.
     * </p>
     * 
     * @return wrapped value as double
     * @throws NumberFormatException
     *             if wrapped value cannot be converted to double
     * @since 0.0.0
     */
    public double asDouble() throws NumberFormatException;

    /**
     * <p>
     * Returns wrapped value as BigInteger.
     * </p>
     * 
     * @return wrapped value as BigInteger
     * @throws NumberFormatException
     *             if wrapped value cannot be converted to BigInteger
     * @since 0.0.0
     */
    public @Nullable BigInteger asBigInteger() throws NumberFormatException;

    /**
     * <p>
     * Returns wrapped value as BigDecimal.
     * </p>
     * 
     * @return wrapped value as BigDecimal
     * @throws NumberFormatException
     *             if wrapped value cannot be converted to BigDecimal
     * @since 0.0.0
     */
    public @Nullable BigDecimal asBigDecimal() throws NumberFormatException;

    /**
     * <p>
     * Returns wrapped value by specified converter.
     * </p>
     * 
     * @param converter
     *            by specified converter
     * @return wrapped value by specified converter
     * @since 0.0.0
     */
    public @Nullable <R> R as(Function<? super T, ? extends R> converter);

    /**
     * <p>
     * Returns an {@linkplain Optional} contains value of this {@linkplain QuickValue}.
     * </p>
     * 
     * @return an {@linkplain Optional} contains value of this {@linkplain QuickValue}
     * @since 0.0.0
     */
    default Optional<T> toOptional() {
        return Optional.ofNullable(getValue());
    }

    static class TypeCached<T> implements QuickValue<T> {

        private final T wrapped;

        @Nullable
        private Object cache = null;

        private TypeCached(T wrapped) {
            this.wrapped = wrapped;
        }

        @Override
        public T getValue() {
            return wrapped;
        }

        @Override
        public String asString() {
            if (wrapped instanceof String) {
                return (String)wrapped;
            }
            if (!(cache instanceof String)) {
                cache = String.valueOf(getValue());
            }
            return (String)cache;
        }

        @Override
        public boolean asBoolean() {
            if (wrapped instanceof Boolean) {
                return (Boolean)wrapped;
            }
            if (!(cache instanceof Boolean)) {
                cache = Boolean.valueOf(asString());
            }
            return (Boolean)cache;
        }

        @Override
        public Number asNumber() throws NumberFormatException {
            if (wrapped instanceof Number) {
                return ((Number)wrapped);
            }
            if (!(cache instanceof Number)) {
                String str = String.valueOf(wrapped);
                cache = new BigDecimal(str);
            }
            return (Number)cache;
        }

        @Override
        public byte asByte() throws NumberFormatException {
            return asNumber().byteValue();
        }

        @Override
        public short asShort() throws NumberFormatException {
            return asNumber().shortValue();
        }

        @Override
        public char asChar() {
            return (char)asNumber().shortValue();
        }

        @Override
        public int asInt() throws NumberFormatException {
            return asNumber().intValue();
        }

        @Override
        public float asFloat() throws NumberFormatException {
            return asNumber().floatValue();
        }

        @Override
        public long asLong() throws NumberFormatException {
            return asNumber().longValue();
        }

        @Override
        public double asDouble() throws NumberFormatException {
            return asNumber().doubleValue();
        }

        @Override
        public BigInteger asBigInteger() throws NumberFormatException {
            if (wrapped instanceof BigInteger) {
                return (BigInteger)wrapped;
            }
            if (!(cache instanceof BigInteger)) {
                String str = asString();
                cache = new BigInteger(str);
            }
            return (BigInteger)cache;
        }

        @Override
        public BigDecimal asBigDecimal() throws NumberFormatException {
            if (wrapped instanceof BigDecimal) {
                return (BigDecimal)wrapped;
            }
            if (!(cache instanceof BigDecimal)) {
                String str = asString();
                cache = new BigDecimal(str);
            }
            return (BigDecimal)cache;
        }

        @Override
        public @Nullable <R> R as(Function<? super T, ? extends R> converter) {
            return converter.apply(getValue());
        }

        @Override
        public String toString() {
            return "QuickValue: " + asString() + ".";
        }
    }

    static class Null<T> implements QuickValue<T> {

        private static final Null<?> SINGLETON = new Null<>();

        @SuppressWarnings("unchecked")
        private static <T> Null<T> singleInstance() {
            return (Null<T>)SINGLETON;
        }

        private Null() {

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
        public Number asNumber() throws NumberFormatException {
            return null;
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
            return null;
        }

        @Override
        public BigDecimal asBigDecimal() throws NumberFormatException {
            return null;
        }

        @Override
        public @Nullable <R> R as(Function<? super T, ? extends R> converter) throws NullPointerException {
            return converter.apply(null);
        }

        @Override
        public String toString() {
            return "QuickValue: " + null + ".";
        }
    }
}
