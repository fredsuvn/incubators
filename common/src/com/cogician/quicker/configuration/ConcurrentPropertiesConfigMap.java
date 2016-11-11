package com.cogician.quicker.configuration;

import java.io.InputStream;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.concurrent.ThreadSafe;

import com.cogician.quicker.ReadException;
import com.cogician.quicker.struct.QuickValue;
import com.cogician.quicker.util.placeholder.QuickPlaceholderResolver;
import com.sun.istack.internal.Nullable;

/**
 * <p>
 * This class is concurrent version of {@linkplain PropertiesConfigMap}. It is thread-safe.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-08-18T16:14:15+08:00
 * @since 0.0.0, 2016-08-18T16:14:15+08:00
 */
@ThreadSafe
public class ConcurrentPropertiesConfigMap extends PropertiesConfigMap {

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
    public ConcurrentPropertiesConfigMap(URI uri) throws NullPointerException, ReadException {
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
    public ConcurrentPropertiesConfigMap(String fileName)
            throws NullPointerException, ReadException, IllegalArgumentException {
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
    public ConcurrentPropertiesConfigMap(InputStream in) throws NullPointerException, ReadException {
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
    public ConcurrentPropertiesConfigMap(URI uri, @Nullable QuickPlaceholderResolver resolver)
            throws NullPointerException, ReadException {
        super(uri, resolver);
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
    public ConcurrentPropertiesConfigMap(String fileName, @Nullable QuickPlaceholderResolver resolver)
            throws NullPointerException, ReadException, IllegalArgumentException {
        super(fileName, resolver);
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
    public ConcurrentPropertiesConfigMap(InputStream in, @Nullable QuickPlaceholderResolver resolver)
            throws NullPointerException, ReadException {
        super(in, resolver);
    }

    @Override
    protected Map<String, QuickValue<?>> createContainer() {
        return new ConcurrentHashMap<>();
    }
}
