package com.cogician.quicker.configuration;

import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

import javax.annotation.Nullable;

import com.cogician.quicker.ReadException;
import com.cogician.quicker.QuickerUniform;
import com.cogician.quicker.WriteException;
import com.cogician.quicker.expression.QuickExpressionParser;
import com.cogician.quicker.struct.QuickValue;

/**
 * <p>
 * This class represents a configuration map.
 * </p>
 * <h2>Key-value pairs</h2>
 * <p>
 * The configurations are key-value pairs, and the keys must be distinct. The key consist of one or more "word" split by
 * dot character ("."). For example:
 * 
 * <pre>
 * key1.key2
 * </pre>
 * 
 * </p>
 * <h2>Sub-config</h2>
 * <p>
 * Dot characters split keys into words. If some of keys have same prefix word (one or more), these key-pairs may be
 * considered as sub-config. For example:
 * 
 * <pre>
 * key1.key2.key3 = 1
 * key1.key2.key4 = 2
 * key1.key5 = 3
 * key2 = 4
 * </pre>
 * 
 * In above example, sub-package "key1.key2" which has two elements, sub-package "key1" which has 3 elements can be
 * gotton by {@linkplain #getConfig(String)}:
 * 
 * <pre>
 * getConfig("key1.key2"); // Returned config includes key3, key4
 * getConfig("key1"); // Returned config includes key2.key3, key2.key4, key5
 * </pre>
 * 
 * </p>
 * <h2>Placeholder</h2>
 * <p>
 * Default implementation of config map is {@linkplain PropertiesConfigMap} and
 * {@linkplain ConcurrentPropertiesConfigMap}, both supporting placeholder resolver. See
 * {@linkplain QuickExpressionParser}.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-04-14T16:46:29+08:00
 * @since 0.0.0, 2016-04-14T16:46:29+08:00
 * @see PropertiesConfigMap
 * @see ConcurrentPropertiesConfigMap
 * @see QuickExpressionParser
 */
public interface QConfiguration extends Map<String, QuickValue<?>> {

    /**
     * <p>
     * Returns config map loaded from file of given file name.
     * </p>
     * 
     * @param fileName
     *            given file name
     * @return config map loaded from file of given file name
     * @throws NullPointerException
     *             if given file name is null
     * @throws ReadException
     *             if any problem occurs when reading
     * @throws IllegalArgumentException
     *             if given file name is illegal
     * @since 0.0.0
     */
    public static QConfiguration loadFrom(String fileName)
            throws NullPointerException, ReadException, IllegalArgumentException {
        return new PropertiesConfigMap(fileName);
    }

    /**
     * <p>
     * Returns config map loaded from given input stream. Returned config map's {@linkplain #refresh()} is invalid.
     * </p>
     * 
     * @param in
     *            given input stream
     * @return config map loaded from given input stream
     * @throws NullPointerException
     *             if given input stream is null
     * @throws ReadException
     *             if any problem occurs when reading
     * @since 0.0.0
     */
    public static QConfiguration loadFrom(InputStream in) throws NullPointerException, ReadException {
        return new PropertiesConfigMap(in);
    }

    /**
     * <p>
     * Returns value of specified key. Return null if not found.
     * </p>
     * 
     * @param key
     *            specified key
     * @return value of specified key
     * @throws NullPointerException
     *             if specified key is null
     * @since 0.0.0
     */
    default @Nullable String getString(String key) throws NullPointerException {
        QuickValue<?> value = get(key);
        if (null != value && value.isPresent()) {
            return get(key).asString();
        }
        return null;
    }

    /**
     * <p>
     * Returns int value of specified key. Return {@linkplain QuickerUniform#INVALID_CODE} if not found.
     * </p>
     * 
     * @param key
     *            specified key
     * @return int value of specified key
     * @throws NullPointerException
     *             if specified key is null
     * @throws NumberFormatException
     *             if the value doesn't parse
     * @since 0.0.0
     */
    default @Nullable int getInt(String key) throws NullPointerException, NumberFormatException {
        QuickValue<?> value = get(key);
        if (null != value && value.isPresent()) {
            return get(key).asInt();
        }
        return QuickerUniform.INVALID_CODE;
    }

    /**
     * <p>
     * Returns float value of specified key. Return {@linkplain QuickerUniform#INVALID_CODE} if not found.
     * </p>
     * 
     * @param key
     *            specified key
     * @return float value of specified key
     * @throws NullPointerException
     *             if specified key is null
     * @throws NumberFormatException
     *             if the value doesn't parse
     * @since 0.0.0
     */
    default @Nullable float getFloat(String key) throws NullPointerException, NumberFormatException {
        QuickValue<?> value = get(key);
        if (null != value && value.isPresent()) {
            return get(key).asFloat();
        }
        return QuickerUniform.INVALID_CODE;
    }

    /**
     * <p>
     * Returns long value of specified key. Return {@linkplain QuickerUniform#INVALID_CODE} if not found.
     * </p>
     * 
     * @param key
     *            specified key
     * @return long value of specified key
     * @throws NullPointerException
     *             if specified key is null
     * @throws NumberFormatException
     *             if the value doesn't parse
     * @since 0.0.0
     */
    default @Nullable long getLong(String key) throws NullPointerException, NumberFormatException {
        QuickValue<?> value = get(key);
        if (null != value && value.isPresent()) {
            return get(key).asLong();
        }
        return QuickerUniform.INVALID_CODE;
    }

    /**
     * <p>
     * Returns double value of specified key. Return {@linkplain QuickerUniform#INVALID_CODE} if not found.
     * </p>
     * 
     * @param key
     *            specified key
     * @return double value of specified key
     * @throws NullPointerException
     *             if specified key is null
     * @throws NumberFormatException
     *             if the value doesn't parse
     * @since 0.0.0
     */
    default @Nullable double getDouble(String key) throws NullPointerException, NumberFormatException {
        QuickValue<?> value = get(key);
        if (null != value && value.isPresent()) {
            return get(key).asDouble();
        }
        return QuickerUniform.INVALID_CODE;
    }

    /**
     * <p>
     * Returns big int value of specified key. Return null if not found.
     * </p>
     * 
     * @param key
     *            specified key
     * @return big int value of specified key
     * @throws NullPointerException
     *             if specified key is null
     * @throws NumberFormatException
     *             if the value doesn't parse
     * @since 0.0.0
     */
    default @Nullable BigInteger getBigInteger(String key) throws NullPointerException, NumberFormatException {
        QuickValue<?> value = get(key);
        if (null != value && value.isPresent()) {
            return get(key).asBigInteger();
        }
        return QuickerUniform.INVALID_CODE_BIG_INT;
    }

    /**
     * <p>
     * Returns big decimal value of specified key. Return null if not found.
     * </p>
     * 
     * @param key
     *            specified key
     * @return big decimal value of specified key
     * @throws NullPointerException
     *             if specified key is null
     * @throws NumberFormatException
     *             if the value doesn't parse
     * @since 0.0.0
     */
    default @Nullable BigDecimal getBigDecimal(String key) throws NullPointerException, NumberFormatException {
        QuickValue<?> value = get(key);
        if (null != value && value.isPresent()) {
            return get(key).asBigDecimal();
        }
        return QuickerUniform.INVALID_CODE_BIG_DEC;
    }

    /**
     * <p>
     * Returns read-only sub-config of specified key head.
     * </p>
     * 
     * @param key
     *            specified key head
     * @return read only sub-config value of specified key head
     * @throws NullPointerException
     *             if specified key head is null
     * @since 0.0.0
     */
    QConfiguration getConfig(String key) throws NullPointerException;

    /**
     * <p>
     * Sets value of specified key.
     * </p>
     * 
     * @param key
     *            specified key
     * @param value
     *            value of specified key
     * @throws NullPointerException
     *             if specified key is null
     * @throws UnsupportedOperationException
     *             if current instance doesn't support this operation
     * @since 0.0.0
     */
    default void set(String key, @Nullable String value) throws NullPointerException, UnsupportedOperationException {
        put(key, QuickValue.wrap(value));
    }

    /**
     * <p>
     * Sets int value of specified key.
     * </p>
     * 
     * @param key
     *            specified key
     * @param value
     *            int value of specified key
     * @throws NullPointerException
     *             if specified key is null
     * @throws UnsupportedOperationException
     *             if current instance doesn't support this operation
     * @since 0.0.0
     */
    default void set(String key, int value) throws NullPointerException, UnsupportedOperationException {
        put(key, QuickValue.wrap(value));
    }

    /**
     * <p>
     * Sets float value of specified key.
     * </p>
     * 
     * @param key
     *            specified key
     * @param value
     *            float value of specified key
     * @throws NullPointerException
     *             if specified key is null
     * @throws UnsupportedOperationException
     *             if current instance doesn't support this operation
     * @since 0.0.0
     */
    default void set(String key, float value) throws NullPointerException, UnsupportedOperationException {
        put(key, QuickValue.wrap(value));
    }

    /**
     * <p>
     * Sets long value of specified key.
     * </p>
     * 
     * @param key
     *            specified key
     * @param value
     *            long value of specified key
     * @throws NullPointerException
     *             if specified key is null
     * @throws UnsupportedOperationException
     *             if current instance doesn't support this operation
     * @since 0.0.0
     */
    default void set(String key, long value) throws NullPointerException, UnsupportedOperationException {
        put(key, QuickValue.wrap(value));
    }

    /**
     * <p>
     * Sets double value of specified key.
     * </p>
     * 
     * @param key
     *            specified key
     * @param value
     *            double value of specified key
     * @throws NullPointerException
     *             if specified key is null
     * @throws UnsupportedOperationException
     *             if current instance doesn't support this operation
     * @since 0.0.0
     */
    default void set(String key, double value) throws NullPointerException, UnsupportedOperationException {
        put(key, QuickValue.wrap(value));
    }

    /**
     * <p>
     * Sets big int value of specified key.
     * </p>
     * 
     * @param key
     *            specified key
     * @param value
     *            big int value of specified key
     * @throws NullPointerException
     *             if specified key is null
     * @throws UnsupportedOperationException
     *             if current instance doesn't support this operation
     * @since 0.0.0
     */
    default void set(String key, @Nullable BigInteger value)
            throws NullPointerException, UnsupportedOperationException {
        put(key, QuickValue.wrap(value));
    }

    /**
     * <p>
     * Sets big decimal value of specified key.
     * </p>
     * 
     * @param key
     *            specified key
     * @param value
     *            big decimal value of specified key
     * @throws NullPointerException
     *             if specified key is null
     * @throws UnsupportedOperationException
     *             if current instance doesn't support this operation
     * @since 0.0.0
     */
    default void set(String key, @Nullable BigDecimal value)
            throws NullPointerException, UnsupportedOperationException {
        put(key, QuickValue.wrap(value));
    }

    /**
     * <p>
     * Sets config of specified key head.
     * </p>
     * 
     * @param key
     *            specified key head
     * @param config
     *            config of specified key head
     * @throws NullPointerException
     *             if specified key head is null
     * @throws UnsupportedOperationException
     *             if current instance doesn't support this operation
     * @since 0.0.0
     */
    default void set(String key, @Nullable QConfiguration config)
            throws NullPointerException, UnsupportedOperationException {
        if (null != config) {
            config.forEach((k, v) -> {
                put(key + "." + k, v);
            });
        }
    }

    /**
     * <p>
     * Removes value of specified key.
     * </p>
     * 
     * @param key
     *            specified key
     * @throws NullPointerException
     *             if specified key is null
     * @throws UnsupportedOperationException
     *             if current instance doesn't support this operation
     * @since 0.0.0
     */
    default void remove(String key) throws NullPointerException, UnsupportedOperationException {
        remove(key);
    }

    /**
     * <p>
     * Removes config of specified key head.
     * </p>
     * 
     * @param key
     *            specified key head
     * @throws NullPointerException
     *             if specified key head is null
     * @throws UnsupportedOperationException
     *             if current instance doesn't support this operation
     * @since 0.0.0
     */
    default void removeAll(String key) throws NullPointerException, UnsupportedOperationException {
        this.forEach((str, obj) -> {
            if (str.indexOf(key + ".") == 0) {
                remove(str);
            }
        });
    }

    /**
     * <p>
     * Returns a config map contains all properties of current instance but read-only.
     * </p>
     * 
     * @return a config map contains all properties of current instance but read-only
     * @since 0.0.0
     */
    public QConfiguration toReadOnly();

    /**
     * <p>
     * Refreshes this config.
     * </p>
     * 
     * @throws UnsupportedOperationException
     *             if current instance doesn't support this operation
     * @since 0.0.0
     */
    public void refresh() throws UnsupportedOperationException;

    /**
     * <p>
     * Saves this config to default position.
     * </p>
     * 
     * @throws UnsupportedOperationException
     *             if current instance doesn't support this operation
     * @throws WriteException
     *             if any problem occurs when written
     * @since 0.0.0
     */
    public void save() throws UnsupportedOperationException, WriteException;

    /**
     * <p>
     * Saves this config to specified position.
     * </p>
     * 
     * @param out
     *            specified position
     * @throws UnsupportedOperationException
     *             if current instance doesn't support this operation
     * @throws WriteException
     *             if any problem occurs when written
     * @since 0.0.0
     */
    public void save(OutputStream out) throws UnsupportedOperationException, WriteException;
}
