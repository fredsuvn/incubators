package com.cogician.quicker.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.concurrent.ThreadSafe;

import com.cogician.quicker.Quicker;
import com.cogician.quicker.ReadException;
import com.cogician.quicker.WriteException;
import com.cogician.quicker.struct.ValueOf;
import com.cogician.quicker.util.PathQuicker;
import com.cogician.quicker.util.placeholder.QuickPlaceholderResolver;
import com.sun.istack.internal.Nullable;

/**
 * <p>
 * Using {@linkplain Properties} to implement {@linkplain QConfiguration}. This class will support placeholder for its value
 * if it is provided a non-null resolver.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-04-14T17:14:37+08:00
 * @since 0.0.0, 2016-04-14T17:14:37+08:00
 */
@ThreadSafe
public class PropertiesConfigMap implements QConfiguration {

    private static URI getURI(String path) throws IllegalArgumentException {
        try {
            return new URI(path);
        } catch (Throwable e) {
            throw new IllegalArgumentException(e);
        }
    }

    private final Properties prop = new Properties();

    private final URI uri;

    private final Map<String, ValueOf<?>> map = createContainer();

    private final QuickPlaceholderResolver resolver;

    /**
     * <p>
     * Constructs with given uri.
     * </p>
     * 
     * @param uri
     *            given uri
     * @throws NullPointerException
     *             if given url is null
     * @throws ReadException
     *             if any problem occurs when reading
     * @since 0.0.0
     */
    public PropertiesConfigMap(URI uri) throws NullPointerException, ReadException {
        this(uri, null);
    }

    /**
     * <p>
     * Constructs with given file name.
     * </p>
     * 
     * @param fileNamerl
     *            given file name
     * @throws NullPointerException
     *             if given file name is null
     * @throws ReadException
     *             if any problem occurs when reading
     * @throws IllegalArgumentException
     *             if given file name is illegal
     * @since 0.0.0
     */
    public PropertiesConfigMap(String fileName) throws NullPointerException, ReadException, IllegalArgumentException {
        this(fileName, null);
    }

    /**
     * <p>
     * Constructs with given input stream. In this constructor {@linkplain #refresh()} is invalid.
     * </p>
     * 
     * @param in
     *            given input stream
     * @throws NullPointerException
     *             if given input stream is null
     * @throws ReadException
     *             if any problem occurs when reading
     * @since 0.0.0
     */
    public PropertiesConfigMap(InputStream in) throws NullPointerException, ReadException {
        this(in, null);
    }

    /**
     * <p>
     * Constructs with given uri and specified placeholder resolver.
     * </p>
     * 
     * @param uri
     *            given uri
     * @param resolver
     *            specified placeholder resolver
     * @throws NullPointerException
     *             if given url is null
     * @throws ReadException
     *             if any problem occurs when reading
     * @since 0.0.0
     */
    public PropertiesConfigMap(URI uri, @Nullable QuickPlaceholderResolver resolver)
            throws NullPointerException, ReadException {
        this.uri = uri;
        this.resolver = resolver;
        load();
    }

    /**
     * <p>
     * Constructs with given file name and specified placeholder resolver.
     * </p>
     * 
     * @param fileNamerl
     *            given file name
     * @param resolver
     *            specified placeholder resolver
     * @throws NullPointerException
     *             if given file name is null
     * @throws ReadException
     *             if any problem occurs when reading
     * @throws IllegalArgumentException
     *             if given file name is illegal
     * @since 0.0.0
     */
    public PropertiesConfigMap(String fileName, @Nullable QuickPlaceholderResolver resolver)
            throws NullPointerException, ReadException, IllegalArgumentException {
        this(getURI(PathQuicker.toAbsolutePath(fileName)), resolver);
    }

    /**
     * <p>
     * Constructs with given input stream and specified placeholder resolver. In this constructor
     * {@linkplain #refresh()} is invalid.
     * </p>
     * 
     * @param in
     *            given input stream
     * @param resolver
     *            specified placeholder resolver
     * @throws NullPointerException
     *             if given input stream is null
     * @throws ReadException
     *             if any problem occurs when reading
     * @since 0.0.0
     */
    public PropertiesConfigMap(InputStream in, @Nullable QuickPlaceholderResolver resolver)
            throws NullPointerException, ReadException {
        this.uri = null;
        this.resolver = resolver;
        load(in);
    }

    private void load() {
        try {
            load(uri.toURL().openStream());
        } catch (Throwable t) {
            throw new ReadException(t);
        }
    }

    private void load(InputStream in) {
        try {
            in = uri.toURL().openStream();
            prop.load(in);
            map.clear();
            prop.forEach((k, v) -> {
                map.put(k.toString(), ValueOf.wrap(v));
            });
            prop.clear();
            in.close();
            map.forEach((k, v) -> {
                put(k, v);
            });
        } catch (Throwable t) {
            throw new ReadException(t);
        }
    }

    /**
     * <p>
     * Returns a map used to contain all the property-pairs. This method will only be called once.
     * </p>
     * 
     * @return a map used to contain all the property-pairs
     * @since 0.0.0
     */
    protected Map<String, ValueOf<?>> createContainer() {
        return new HashMap<String, ValueOf<?>>();
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(Quicker.require(key));
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public ValueOf<?> get(Object key) {
        return map.get(Quicker.require(key));
    }

    @Override
    public ValueOf<?> put(String key, ValueOf<?> value) {
        if (null != resolver) {
            String v = resolver.resolve(value.asString(), this);
            return map.put(Quicker.require(key), v == value.asString() ? value : ValueOf.wrap(v));
        }
        return map.put(Quicker.require(key), value);
    }

    @Override
    public ValueOf<?> remove(Object key) {
        return map.remove(Quicker.require(key));
    }

    @Override
    public void putAll(Map<? extends String, ? extends ValueOf<?>> m) {
        if (null != m) {
            m.forEach((k, v) -> {
                put(k, v);
            });
        }
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Set<String> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<ValueOf<?>> values() {
        return map.values();
    }

    @Override
    public Set<java.util.Map.Entry<String, ValueOf<?>>> entrySet() {
        return map.entrySet();
    }

    @Override
    public QConfiguration getConfig(String key) throws NullPointerException {
        String nonnullKey = Quicker.require(key);
        Map<String, ValueOf<?>> result = new ConcurrentHashMap<>();
        map.forEach((k, v) -> {
            if (k.startsWith(nonnullKey + ".")) {
                result.put(k, v);
            }
        });
        if (result.isEmpty()) {
            return new ReadOnlyConfigMap(Collections.emptyMap());
        }
        return new ReadOnlyConfigMap(result);
    }

    @Override
    public QConfiguration toReadOnly() {
        return new ReadOnlyConfigMap(map);
    }

    @Override
    public void refresh() throws UnsupportedOperationException {
        if (null != uri) {
            load();
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public void save() throws UnsupportedOperationException, WriteException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void save(OutputStream out) throws UnsupportedOperationException, WriteException {
        try {
            prop.store(out, null);
        } catch (IOException e) {
            throw new WriteException(e);
        }
    }

    private static class ReadOnlyConfigMap implements QConfiguration {

        private final Map<String, ValueOf<?>> map;

        private ReadOnlyConfigMap(Map<String, ValueOf<?>> map) {
            this.map = map;
        }

        @Override
        public int size() {
            return map.size();
        }

        @Override
        public boolean isEmpty() {
            return map.isEmpty();
        }

        @Override
        public boolean containsKey(Object key) {
            return map.containsKey(Quicker.require(key));
        }

        @Override
        public boolean containsValue(Object value) {
            return map.containsValue(value);
        }

        @Override
        public ValueOf<?> get(Object key) {
            return map.get(Quicker.require(key));
        }

        @Override
        public ValueOf<?> put(String key, ValueOf<?> value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ValueOf<?> remove(Object key) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void putAll(Map<? extends String, ? extends ValueOf<?>> m) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Set<String> keySet() {
            return map.keySet();
        }

        @Override
        public Collection<ValueOf<?>> values() {
            return map.values();
        }

        @Override
        public Set<java.util.Map.Entry<String, ValueOf<?>>> entrySet() {
            return map.entrySet();
        }

        @Override
        public QConfiguration getConfig(String key) throws NullPointerException {
            String nonnullKey = Quicker.require(key);
            Map<String, ValueOf<?>> result = new ConcurrentHashMap<>();
            map.forEach((k, v) -> {
                if (k.startsWith(nonnullKey + ".")) {
                    result.put(k, v);
                }
            });
            return new ReadOnlyConfigMap(result);
        }

        @Override
        public QConfiguration toReadOnly() {
            return this;
        }

        @Override
        public void refresh() throws UnsupportedOperationException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void save() throws UnsupportedOperationException, WriteException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void save(OutputStream out) throws UnsupportedOperationException, WriteException {
            try {
                new Properties().store(out, null);
            } catch (IOException e) {
                throw new WriteException(e);
            }
        }
    }
}
