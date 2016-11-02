package com.cogician.quicker.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.Function;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.cogician.quicker.Buildable;
import com.cogician.quicker.Checker;
import com.cogician.quicker.Quicker;
import com.cogician.quicker.Uniforms;
import com.cogician.quicker.util.reflect.ReflectionQuicker;

/**
 * Static quick utility class provides static methods for {@linkplain Object#toString()}.
 *
 * @author Fred Suvn
 * @version 0.0.0, 2015-10-10 14:17:12
 * @since 0.0.0
 */
public class ToStringQuicker {
	
	public static String join(String[] array, String separator){
		if (array.length == 0){
			return Consts.emptyString();
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < array.length - 1; i++){
			sb.append(array[i]);
			sb.append(separator);
		}
		sb.append(array[array.length - 1]);
		return sb.toString();
	}
	
	private static final Object NULL = new Object();
	
	public static String join(Iterator<?> iterator, String separator){
		StringBuilder sb = new StringBuilder();
		try{
			sb.append(String.valueOf(iterator.next()));
		} catch (NoSuchElementException e){
			return Consts.emptyString();
		}
		while(iterator.hasNext()){
			sb.append(separator);
			sb.append(String.valueOf(iterator.next()));
		}
		return sb.toString();
	}

    /**
     * <p>
     * Joins given array, using config of {@linkplain ToStringConfig#VALUE_OF}. If given array is null, return an empty
     * string.
     * </p>
     * 
     * @param <T>
     *            type of array
     * @param array
     *            given array
     * @param config
     *            specified config
     * @return string after joining
     * @see ToStringConfig
     * @since 0.0.0
     */
    public static <T> String join(@Nullable T[] array) {
        return join(array, null);
    }

    /**
     * <p>
     * Joins given array with specified config, using {@linkplain #toString(Object, ToStringConfig)} to toString each
     * element. If given array is null, return an empty string. If given config is null, use
     * {@linkplain ToStringConfig#VALUE_OF}.
     * </p>
     * 
     * @param <T>
     *            type of array
     * @param array
     *            given array
     * @param config
     *            specified config
     * @return string after joining
     * @see ToStringConfig
     * @since 0.0.0
     */
    public static <T> String join(@Nullable T[] array, @Nullable ToStringConfig config) {
        if (null == array) {
            return Consts.emptyString();
        }
        ToStringConfig conf = config == null ? ToStringConfig.VALUE_OF : config;
        StringBuilder sb = new StringBuilder();
        boolean[] atLeastOne = {false};
        Quicker.each(array, str -> {
            if (null != str || !conf.nullIgnored) {
                sb.append(toString(str, config)).append(conf.getJoinSeparator());
                atLeastOne[0] = true;
            }
        });
        if (atLeastOne[0]) {
            sb.delete(sb.length() - conf.getJoinSeparator().length(), sb.length());
        }
        return sb.toString();
    }

    /**
     * <p>
     * Joins given iterator, using config of {@linkplain ToStringConfig#VALUE_OF}. If given iterator is null, return an
     * empty string.
     * </p>
     * 
     * @param iterator
     *            given iterator
     * @return string after joining
     * @see ToStringConfig
     * @since 0.0.0
     */
    public static String join(@Nullable Iterator<?> iterator) {
        return join(iterator, (ToStringConfig)null);
    }

    /**
     * <p>
     * Joins given iterator with specified config, using {@linkplain #toString(Object, ToStringConfig)} to toString each
     * element. If given iterator is null, return an empty string. If given config is null, use
     * {@linkplain ToStringConfig#VALUE_OF}.
     * </p>
     * 
     * @param iterator
     *            given iterator
     * @param config
     *            specified config
     * @return string after joining
     * @see ToStringConfig
     * @since 0.0.0
     */
    public static String join(@Nullable Iterator<?> iterator, @Nullable ToStringConfig config) {
        if (null == iterator) {
            return Consts.emptyString();
        }
        ToStringConfig conf = config == null ? ToStringConfig.VALUE_OF : config;
        StringBuilder sb = new StringBuilder();
        boolean[] atLeastOne = {false};
        Quicker.each(iterator, str -> {
            if (null != str || !conf.nullIgnored) {
                if (str instanceof FieldToString) {
                    sb.append(str.toString()).append(conf.getJoinSeparator());
                } else {
                    sb.append(toString(str, conf)).append(conf.getJoinSeparator());
                }
                atLeastOne[0] = true;
            }
        });
        if (atLeastOne[0]) {
            sb.delete(sb.length() - conf.getJoinSeparator().length(), sb.length());
        }
        return sb.toString();
    }

    /**
     * <p>
     * Joins given iterable, using config of {@linkplain ToStringConfig#VALUE_OF}. If given iterable is null, return an
     * empty string.
     * </p>
     * 
     * @param iterable
     *            given iterable
     * @param config
     *            specified config
     * @return string after joining
     * @see ToStringConfig
     * @since 0.0.0
     */
    public static String join(@Nullable Iterable<?> iterable) {
        return join(iterable, null);
    }

    /**
     * <p>
     * Joins given iterable with specified config, using {@linkplain #toString(Object, ToStringConfig)} to toString each
     * element. If given iterable is null, return an empty string. If given config is null, use
     * {@linkplain ToStringConfig#VALUE_OF}.
     * </p>
     * 
     * @param iterable
     *            given iterable
     * @param config
     *            specified config
     * @return string after joining
     * @see ToStringConfig
     * @since 0.0.0
     */
    public static String join(@Nullable Iterable<?> iterable, @Nullable ToStringConfig config) {
        if (null == iterable) {
            return Consts.emptyString();
        }
        return join(iterable.iterator(), config);
    }

    /**
     * <p>
     * ToStrings given object with config {@linkplain ToStringConfig#VALUE_OF}. If given object is null, using valueOf
     * function of the config.
     * </p>
     * 
     * @param obj
     *            given object
     * @return a string after converting
     * @see ToStringConfig
     * @since 0.0.0
     */
    public static String toString(@Nullable Object obj) {
        return toString(obj, null);
    }

    /**
     * <p>
     * ToStrings given object with specified config. If given object is null, using valueOf function of given config. If
     * given config is null, use {@linkplain ToStringConfig#VALUE_OF}.
     * </p>
     * 
     * @param obj
     *            given object
     * @param config
     *            given config
     * @return a string after converting
     * @see ToStringConfig
     * @since 0.0.0
     */
    public static String toString(@Nullable Object obj, @Nullable ToStringConfig config) {
        ToStringConfig conf = config == null ? ToStringConfig.VALUE_OF : config;
        if (null == obj) {
            return conf.getValueOfFunction().apply(obj);
        }
        Class<?> type = obj.getClass();
        if (type.isArray()) {
            return arrayReflectToString(obj, conf);
        } else if (conf.getPolicy() == ToStringPolicy.VALUE_OF
                || (conf.getPolicy() == ToStringPolicy.AUTO && isToStringOverridden(type, conf.getUpTo()))) {
            return conf.getValueOfFunction().apply(obj);
        } else {
            return objectReflectToString(obj, conf);
        }
    }

    /**
     * <p>
     * ToStrings given object with config {@linkplain ToStringConfig#AUTO}, but the different from
     * {@linkplain #toString(Object)} is it force to use reflection to toString itself, regardless of policy setting of
     * specified config. The policy setting of specified config takes effect in its fields when toStrings its fields
     * recursively. If given object is null, using valueOf function of the config.
     * </p>
     * 
     * @param obj
     *            given object
     * @return a string after converting
     * @see ToStringConfig
     * @since 0.0.0
     */
    public static String reflectToString(@Nullable Object obj) {
        return reflectToString(obj, null);
    }

    /**
     * <p>
     * ToStrings given object with specified config, but the different from
     * {@linkplain #toString(Object, ToStringConfig)} is it force to use reflection to toString itself, regardless of
     * policy setting of specified config. The policy setting of specified config takes effect in its fields when
     * toStrings its fields recursively. If given object is null, using valueOf function of given config. If given
     * config is null, use {@linkplain ToStringConfig#AUTO}.
     * </p>
     * 
     * @param obj
     *            given object
     * @param config
     *            given config
     * @return a string after converting
     * @see ToStringConfig
     * @since 0.0.0
     */
    public static String reflectToString(@Nullable Object obj, @Nullable ToStringConfig config) {
        ToStringConfig conf = config == null ? ToStringConfig.AUTO : config;
        if (null == obj) {
            return conf.getValueOfFunction().apply(obj);
        }
        Class<?> type = obj.getClass();
        if (type.isArray()) {
            return arrayReflectToString(obj, conf);
        } else {
            return objectReflectToString(obj, conf);
        }
    }

    private static boolean isToStringOverridden(Class<?> cls, Class<?> upTo) {
        return Quicker.each(ReflectionQuicker.iteratorOfClassInheritance(cls, upTo), c -> {
            return !ReflectionQuicker.getPublicMethod(c, "toString", Consts.emptyClassArray()).getDeclaringClass()
                    .equals(cls);
        }).isComplete();
    }

    private static FieldToString fieldToString(Object obj, Field f, ToStringConfig config) {
        return new FieldToString(f, obj, config);
    }

    private static String arrayReflectToString(Object obj, ToStringConfig config) {
        return config.getArrayPrefix() + join(QuiCollections.asObjectArray(obj), config) + config.getArraySuffix();
    }

    private static String objectReflectToString(Object obj, ToStringConfig config) {
        Class<?> type = obj.getClass();
        Set<String> filter = config.getFieldNames();
        if (Checker.isEmpty(filter)) {
            if (config.staticIncluded()) {
                return type.getName() + config.getObjectPrefix()
                        + join(Quicker.iterator(ReflectionQuicker.iteratorOfFields(type, config.getUpTo()),
                                f -> fieldToString(obj, f, config)), config)
                        + config.getObjectSuffix();
            } else {
                return type.getName() + config.getObjectPrefix() + join(
                        Quicker.elementNonnull(Quicker.iterator(
                                ReflectionQuicker.iteratorOfFields(type, config.getUpTo()),
                                f -> Modifier.isStatic(f.getModifiers()) ? null : fieldToString(obj, f, config))),
                        config) + config.getObjectSuffix();
            }
        } else {
            return type.getName() + config.getObjectPrefix()
                    + join(Quicker
                            .elementNonnull(
                                    Quicker.iterator(ReflectionQuicker.iteratorOfFields(type, config.getUpTo()),
                                            f -> filter.contains(f.getName()) && (Modifier.isStatic(f.getModifiers())
                                                    ? config.staticIncluded() : false) ? null
                                                            : fieldToString(obj, f, config))),
                            config)
                    + config.getObjectSuffix();
        }
    }

    private static class FieldToString {
        private Field field;
        private Object object;
        private ToStringConfig config;

        private FieldToString(Field field, Object clazz, ToStringConfig config) {
            this.field = field;
            this.object = clazz;
            this.config = config;
        }

        public String toString() {
            Object valueObj = ReflectionQuicker.getFieldValue(field, object);
            String value = ToStringQuicker.toString(valueObj, config);
            return field.getName() + config.getFieldValueIndicator() + value;
        }
    }

    /**
     * <p>
     * This class represents a set of configurations for toString operation by reflection.
     * </p>
     * <h2>Style</h2>
     * <p>
     * Style of toString is a joined string appends a pair of affix like following if the object is a non-array object:
     * 
     * <pre>
     * com.cogician.MyObj{field0 = "string", field1 = com.cogician.Another{field0 = "another"}}
     * </pre>
     * 
     * Or below if the object is an array:
     * 
     * <pre>
     * [element0, element1, [element20, element21, element22], element3]
     * </pre>
     *
     * </p>
     * <p>
     * In the examples, "{", "}", "[" and "]" is prefix and suffix for object and array; " = " is value indicator for
     * each field; ", " is join separator; and these tokens are also default. All these tokens can be specified by this
     * config class.
     * </p>
     * <h2>Policy</h2>
     * <p>
     * There are two ways to toString an object: one is using valueOf function (default is
     * {@linkplain Builder#DEFAULT_VALUE_OF_FUNCTION}); the other is using reflection (fields included recursively) like
     * above examples.
     * </p>
     * <p>
     * This config class may specify a policy about how to toString an object. See {@linkplain ToStringPolicy}.
     * </p>
     * <h2>Builder</h2>
     * <p>
     * Using following codes to build a ToStringConfig:
     * 
     * <pre>
     * ToStringConfig config = new ToStringConfig.Builder().setXxx()...build();
     * </pre>
     * 
     * If the build is set nothing, it will build a config with default arguments.
     * </p>
     *
     * @author Fred Suvn
     * @version 0.0.0, 2016-05-25T13:21:12+08:00
     * @since 0.0.0, 2016-05-25T13:21:12+08:00
     */
    @Immutable
    public static class ToStringConfig {

        /**
         * <p>
         * Default toString join separator.
         * </p>
         * 
         * @since 0.0.0
         */
        public static final String DEFAULT_JOIN_SEPARATOR = ",";

        /**
         * <p>
         * Default toString object prefix.
         * </p>
         * 
         * @since 0.0.0
         */
        public static final String DEFAULT_OBJECT_PREFIX = "{";

        /**
         * <p>
         * Default toString object suffix.
         * </p>
         * 
         * @since 0.0.0
         */
        public static final String DEFAULT_OBJECT_SUFFIX = "}";

        /**
         * <p>
         * Default toString object field indicator.
         * </p>
         * 
         * @since 0.0.0
         */
        public static final String DEFAULT_OBJECT_FIELD_VALUE_INDICATOR = "=";

        /**
         * <p>
         * Default toString array prefix separator.
         * </p>
         * 
         * @since 0.0.0
         */
        public static final String DEFAULT_ARRAY_PREFIX = "[";

        /**
         * <p>
         * Default toString array suffix.
         * </p>
         * 
         * @since 0.0.0
         */
        public static final String DEFAULT_ARRAY_SUFFIX = "]";

        /**
         * <p>
         * Default "valueOf" function to toString an object:
         * <ol>
         * <li>If the class is String, return "string.valueOf(strObj)" (quotations included);</li>
         * <li>Eles return string.valueOf(obj);</li>
         * </ol>
         * </p>
         * 
         * @since 0.0.0
         */
        public static final Function<? super Object, String> DEFAULT_VALUE_OF_FUNCTION = o -> {
            if (o == null || !(o instanceof String)) {
                return String.valueOf(o);
            } else {
                return Quicker.stringize(String.valueOf(o));
            }
        };

        /**
         * <p>
         * A toString config using {@linkplain ToStringPolicy#VALUE_OF} policy, style of {@linkplain Uniforms}, and
         * other default options of {@linkplain Builder}.
         * </p>
         * 
         * @since 0.0.0
         */
        public static final ToStringConfig VALUE_OF = new Builder().build();

        /**
         * <p>
         * A toString config using {@linkplain ToStringPolicy#REFLECTION} policy, style of {@linkplain Uniforms}, and
         * other default options of {@linkplain Builder}.
         * </p>
         * 
         * @since 0.0.0
         */
        public static final ToStringConfig REFLECTION = new Builder().setPolicy(ToStringPolicy.REFLECTION).build();

        /**
         * <p>
         * A toString config using {@linkplain ToStringPolicy#AUTO} policy, style of {@linkplain Uniforms}, and other
         * default options of {@linkplain Builder}.
         * </p>
         * 
         * @since 0.0.0
         */
        public static final ToStringConfig AUTO = new Builder().setPolicy(ToStringPolicy.AUTO).build();

        private String objectPrefix;
        private String objectSuffix;
        private String fieldValueIndicator;
        private String arrayPrefix;
        private String arraySuffix;
        private String joinSeparator;
        private ToStringPolicy policy;

        private Function<? super Object, String> valueOfFunction;

        @Nullable
        private Class<?> upTo;
        @Nullable
        private Set<String> fieldNames;

        private boolean nullIgnored;
        private boolean staticIncluded;

        private ToStringConfig(Builder builder) {
            this.objectPrefix = builder.objectPrefix;
            this.objectSuffix = builder.objectSuffix;
            this.fieldValueIndicator = builder.objectFieldValueIndicator;
            this.arrayPrefix = builder.arrayPrefix;
            this.arraySuffix = builder.arraySuffix;
            this.joinSeparator = builder.joinSeparator;
            this.policy = builder.policy;
            this.valueOfFunction = builder.valueOfFunction;
            this.upTo = builder.upTo;
            Set<String> names = null;
            if (builder.fieldNames != null) {
                names = Collections.unmodifiableSet(this.fieldNames);
            }
            this.fieldNames = names;
            this.nullIgnored = builder.nullIgnored;
            this.staticIncluded = builder.staticIncluded;
        }

        /**
         * <p>
         * Returns prefix for non-array object.
         * </p>
         * 
         * @return prefix for non-array object
         * @since 0.0.0
         */
        public String getObjectPrefix() {
            return objectPrefix;
        }

        /**
         * <p>
         * Returns suffix for non-array object.
         * </p>
         * 
         * @return suffix for non-array object
         * @since 0.0.0
         */
        public String getObjectSuffix() {
            return objectSuffix;
        }

        /**
         * <p>
         * Returns field value indicator for non-array object.
         * </p>
         * 
         * @return field value indicator for non-array object
         * @since 0.0.0
         */
        public String getFieldValueIndicator() {
            return fieldValueIndicator;
        }

        /**
         * <p>
         * Returns prefix for array object.
         * </p>
         * 
         * @return prefix for array object
         * @since 0.0.0
         */
        public String getArrayPrefix() {
            return arrayPrefix;
        }

        /**
         * <p>
         * Returns suffix for array object.
         * </p>
         * 
         * @return suffix for array object
         * @since 0.0.0
         */
        public String getArraySuffix() {
            return arraySuffix;
        }

        /**
         * <p>
         * Returns join separator.
         * </p>
         * 
         * @return join separator
         * @since 0.0.0
         */
        public String getJoinSeparator() {
            return joinSeparator;
        }

        /**
         * <p>
         * Returns policy of toString.
         * </p>
         * 
         * @return policy of toString
         * @since 0.0.0
         */
        public ToStringPolicy getPolicy() {
            return policy;
        }

        /**
         * <p>
         * Returns valueOf function.
         * </p>
         * 
         * @return valueOf function
         * @since 0.0.0
         */
        public Function<? super Object, String> getValueOfFunction() {
            return valueOfFunction;
        }

        /**
         * <p>
         * Returns last superclass for reflecting.
         * </p>
         * 
         * @return last superclass for reflecting
         * @since 0.0.0
         */
        public @Nullable Class<?> getUpTo() {
            return upTo;
        }

        /**
         * <p>
         * Returns specified fields names which are need to reflect.
         * </p>
         * 
         * @return specified fields names which are need to reflect
         * @since 0.0.0
         */
        public @Nullable Set<String> getFieldNames() {
            return fieldNames;
        }

        /**
         * <p>
         * Returns whether null elements are joined to build toString.
         * </p>
         * 
         * @return whether null elements are joined to build toString
         * @since 0.0.0
         */
        public boolean nullIgnored() {
            return nullIgnored;
        }

        /**
         * <p>
         * Returns whether static fields is included to build toString.
         * </p>
         * 
         * @return whether static fields is included to build toString
         * @since 0.0.0
         */
        public boolean staticIncluded() {
            return staticIncluded;
        }

        /**
         * <p>
         * Builder of {@linkplain ToStringConfig}. Default builder will build result same as
         * {@linkplain ToStringConfig#VALUE_OF}.
         * </p>
         *
         * @author Fred Suvn
         * @version 0.0.0, 2016-05-21T22:27:52+08:00
         * @since 0.0.0, 2016-05-21T22:27:52+08:00
         * @see ToStringConfig
         */
        public static class Builder implements Buildable<ToStringConfig> {

            /**
             * <p>
             * Returns a config of which style of prefixes, suffixes and join separator of are appended given addition
             * on given config's.
             * </p>
             * 
             * @param config
             *            given config
             * @param addition
             *            given addition
             * @return a config of which all prefixes, suffixes and join separator of are appended given addition on
             *         given config's
             * @throws NullPointerException
             *             if given config is null
             * @since 0.0.0
             */
            public static ToStringConfig addStyleSuffix(ToStringConfig config, @Nullable String addition)
                    throws NullPointerException {
                Builder builder = new Builder().copyFrom(Quicker.require(config));
                builder.setStyle(config.getObjectPrefix() + addition, config.getObjectSuffix() + addition,
                        config.getFieldValueIndicator(), config.getArrayPrefix() + addition,
                        config.getArraySuffix() + addition, config.getJoinSeparator() + addition);
                return builder.build();
            }

            private String objectPrefix = DEFAULT_OBJECT_PREFIX;
            private String objectSuffix = DEFAULT_OBJECT_SUFFIX;
            private String objectFieldValueIndicator = DEFAULT_OBJECT_FIELD_VALUE_INDICATOR;
            private String arrayPrefix = DEFAULT_ARRAY_PREFIX;
            private String arraySuffix = DEFAULT_ARRAY_SUFFIX;
            private String joinSeparator = DEFAULT_JOIN_SEPARATOR;
            private ToStringPolicy policy = ToStringPolicy.VALUE_OF;
            private Function<? super Object, String> valueOfFunction = DEFAULT_VALUE_OF_FUNCTION;
            @Nullable
            private Class<?> upTo = null;
            @Nullable
            private Set<String> fieldNames = null;
            private boolean nullIgnored = false;
            private boolean staticIncluded = false;

            /**
             * <p>
             * Constructs an instance with default configs.
             * </p>
             * 
             * @since 0.0.0
             */
            public Builder() {
            }

            /**
             * <p>
             * Sets prefix for non-array object. Null will be considered as empty.
             * </p>
             * 
             * @param objectPrefix
             *            prefix for non-array object
             * @return this builder
             * @since 0.0.0
             */
            public Builder setObjectPrefix(@Nullable String objectPrefix) {
                this.objectPrefix = Quicker.nonnull(objectPrefix);
                return this;
            }

            /**
             * <p>
             * Sets suffix for non-array object. Null will be considered as empty.
             * </p>
             * 
             * @param objectSuffix
             *            suffix for non-array object
             * @return this builder
             * @since 0.0.0
             */
            public Builder setObjectSuffix(@Nullable String objectSuffix) {
                this.objectSuffix = Quicker.nonnull(objectSuffix);
                return this;
            }

            /**
             * <p>
             * Sets field value indicator for non-array object. Null will be considered as empty.
             * </p>
             * 
             * @param objectFieldValueIndicator
             *            field value indicator for non-array object
             * @return this builder
             * @since 0.0.0
             */
            public Builder setObjectFieldValueIndicator(@Nullable String objectFieldValueIndicator) {
                this.objectFieldValueIndicator = Quicker.nonnull(objectFieldValueIndicator);
                return this;
            }

            /**
             * <p>
             * Sets prefix for array object. Null will be considered as empty.
             * </p>
             * 
             * @param arrayPrefix
             *            prefix for array object
             * @return this builder
             * @since 0.0.0
             */
            public Builder setArrayPrefix(@Nullable String arrayPrefix) {
                this.arrayPrefix = Quicker.nonnull(arrayPrefix);
                return this;
            }

            /**
             * <p>
             * Sets suffix for array object. Null will be considered as empty.
             * </p>
             * 
             * @param arraySuffix
             *            suffix for array object
             * @return this builder
             * @since 0.0.0
             */
            public Builder setArraySuffix(@Nullable String arraySuffix) {
                this.arraySuffix = Quicker.nonnull(arraySuffix);
                return this;
            }

            /**
             * <p>
             * Sets join separator. Null will be considered as empty.
             * </p>
             * 
             * @param joinSeparator
             *            join separator
             * @return this builder
             * @since 0.0.0
             */
            public Builder setJoinSeparator(@Nullable String joinSeparator) {
                this.joinSeparator = Quicker.nonnull(joinSeparator);
                return this;
            }

            /**
             * <p>
             * Sets basic delimiters includes: prefix, suffix and field value indicator for non-array object. Null will
             * be considered as empty.
             * </p>
             * 
             * @param objectPrefix
             *            prefix for non-array object
             * @param objectSuffix
             *            suffix for non-array object
             * @param objectFieldValueIndicator
             *            field value indicator for non-array object
             * @return this builder
             * @since 0.0.0
             */
            public Builder setObjectStyle(@Nullable String objectPrefix, @Nullable String objectSuffix,
                    @Nullable String objectFieldValueIndicator) {
                setObjectPrefix(objectPrefix);
                setObjectSuffix(objectSuffix);
                setObjectFieldValueIndicator(objectFieldValueIndicator);
                return this;
            }

            /**
             * <p>
             * Sets basic delimiters includes: prefix and suffix for array object. Null will be considered as empty.
             * </p>
             * 
             * @param arrayPrefix
             *            prefix for array object
             * @param arraySuffix
             *            suffix for array object
             * @return this builder
             * @since 0.0.0
             */
            public Builder setArrayStyle(@Nullable String arrayPrefix, @Nullable String arraySuffix) {
                setArrayPrefix(arrayPrefix);
                setArraySuffix(arraySuffix);
                return this;
            }

            /**
             * <p>
             * Sets basic delimiters includes: prefix, suffix, field value indicator and separator for non-array or
             * array object. Null will be considered as empty.
             * </p>
             * 
             * @param objectPrefix
             *            prefix for non-array object
             * @param objectSuffix
             *            suffix for non-array object
             * @param objectFieldValueIndicator
             *            field value indicator for non-array object
             * @param objectSeparator
             *            separator for non-array object
             * @param arrayPrefix
             *            prefix for array object
             * @param arraySuffix
             *            suffix for array object
             * @param arraySeparator
             *            separator for array object
             * @return this builder
             * @since 0.0.0
             */
            public Builder setStyle(@Nullable String objectPrefix, @Nullable String objectSuffix,
                    @Nullable String objectFieldValueIndicator, @Nullable String arrayPrefix,
                    @Nullable String arraySuffix, @Nullable String joinSeparator) {
                setObjectPrefix(objectPrefix);
                setObjectSuffix(objectSuffix);
                setObjectFieldValueIndicator(objectFieldValueIndicator);
                setArrayPrefix(arrayPrefix);
                setArraySuffix(arraySuffix);
                setJoinSeparator(joinSeparator);
                return this;
            }

            /**
             * <p>
             * Sets toString policy. If given policy is null, it will be considered as {@linkplain ToStringPolicy#AUTO}.
             * </p>
             * 
             * @param policy
             *            given policy
             * @return this builder
             * @since 0.0.0
             */
            public Builder setPolicy(@Nullable ToStringPolicy policy) {
                this.policy = Quicker.require(policy, () -> ToStringPolicy.AUTO);
                return this;
            }

            /**
             * <p>
             * Sets valueOf function. If given valueOf function is null, set {@linkplain #DEFAULT_VALUE_OF_FUNCTION}.
             * </p>
             * 
             * @param valueOf
             *            given valueOf function
             * @return this builder
             * @since 0.0.0
             */
            public Builder setValueOfFunction(@Nullable Function<? super Object, String> valueOfFunction) {
                this.valueOfFunction = Quicker.require(valueOfFunction, () -> DEFAULT_VALUE_OF_FUNCTION);
                return this;
            }

            /**
             * <p>
             * Sets up-to superclass (inclusive) in toString reflection. If given up-to superclass is null, it will
             * considered as Object class. See {@linkplain ToStringConfig}.
             * </p>
             * 
             * @param upTo
             *            up-to superclass (inclusive) in toString reflection
             * @return this builder
             * @since 0.0.0
             * @see ToStringConfig
             */
            public Builder setUpTo(@Nullable Class<?> upTo) {
                this.upTo = upTo;
                return this;
            }

            /**
             * <p>
             * Sets field names indicates which fields should be toString-ed by reflection. If set this option, only
             * fields of which names in this set may be reflected to string; otherwise, all fields will be reflected.
             * This option only works for non-array object.
             * </p>
             * 
             * @param fieldNames
             *            names of fields to be reflected
             * @return this builder
             * @since 0.0.0
             */
            public Builder setFieldNames(@Nullable Set<String> fieldNames) {
                this.fieldNames = fieldNames;
                return this;
            }

            /**
             * <p>
             * Sets field names indicates which fields should be toString-ed by reflection. If set this option, only
             * fields of which names are set may be reflected to string; otherwise, all fields will be reflected. This
             * option only works for non-array object.
             * </p>
             * 
             * @param fieldNames
             *            names of fields to be reflected
             * @return this builder
             * @throws NullPointerException
             *             if any of given field name is null
             * @throws IllegalArgumentException
             *             if any of given field name is empty
             * @since 0.0.0
             */
            public Builder setFieldNames(String... fieldNames) throws NullPointerException, IllegalArgumentException {
                if (Checker.isNotEmpty(fieldNames)) {
                    Quicker.each(fieldNames, f -> {
                        appandField(f);
                    });
                }
                return this;
            }

            /**
             * <p>
             * Appends field name indicates which fields should be toString-ed by reflection. If set this option, only
             * fields of which names are set may be reflected to string; otherwise, all fields will be reflected. This
             * option only works for non-array object.
             * </p>
             * 
             * @param fieldName
             *            given field name
             * @return this builder
             * @throws NullPointerException
             *             if given field name is null
             * @throws IllegalArgumentException
             *             if given field name is empty
             * @since 0.0.0
             */
            public Builder appandField(String fieldName) throws NullPointerException, IllegalArgumentException {
                fieldName = Quicker.requireNonEmpty(fieldName);
                this.fieldNames = Quicker.require(this.fieldNames, () -> new HashSet<String>());
                this.fieldNames.add(fieldName);
                return this;
            }

            /**
             * <p>
             * Removes field name indicates which fields should be toString-ed by reflection. If field names are set ,
             * only fields of which names are set may be reflected to string; otherwise, all fields will be reflected.
             * This option only works for non-array object.
             * </p>
             * 
             * @param fieldName
             *            given field name
             * @return this builder
             * @throws NullPointerException
             *             if given field name is null
             * @throws IllegalArgumentException
             *             if given field name is empty
             * @since 0.0.0
             */
            public Builder removeField(String fieldName) throws NullPointerException, IllegalArgumentException {
                if (this.fieldNames != null) {
                    this.fieldNames.remove(Quicker.requireNonEmpty(fieldName));
                }
                return this;
            }

            /**
             * <p>
             * Sets whether null elements are joined to build toString. Default is false;
             * </p>
             * 
             * @param nullIgnored
             *            whether null elements are joined to build toString
             * @return this builder
             * @since 0.0.0
             */
            public Builder setNullIgnored(boolean nullIgnored) {
                this.nullIgnored = nullIgnored;
                return this;
            }

            /**
             * <p>
             * Sets whether static fields is included to build toString. Default is false.
             * </p>
             * 
             * @param included
             *            whether static fields is included to build toString
             * @return this builder
             * @since 0.0.0
             */
            public Builder setStaticIncluded(boolean included) {
                this.staticIncluded = included;
                return this;
            }

            /**
             * <p>
             * Sets options copied from given config.
             * </p>
             * 
             * @param config
             *            given config
             * @return this builder
             * @throws NullPointerException
             *             if given config is null
             * @since 0.0.0
             */
            public Builder copyFrom(ToStringConfig config) throws NullPointerException {
                setStyle(config.objectPrefix, config.objectSuffix, config.fieldValueIndicator, config.arrayPrefix,
                        config.arraySuffix, config.joinSeparator);
                setValueOfFunction(config.valueOfFunction);
                setUpTo(config.upTo);
                setFieldNames(config.fieldNames);
                return this;
            }

            @Override
            public ToStringConfig build() {
                return new ToStringConfig(this);
            }
        }
    }

    /**
     * <p>
     * ToString policy: {@linkplain ToStringPolicy#AUTO}, {@linkplain ToStringPolicy#VALUE_OF},
     * {@linkplain ToStringPolicy#REFLECTION}.
     * </p>
     *
     * @author Fred Suvn
     * @version 0.0.0, 2016-08-11T10:16:30+08:00
     * @since 0.0.0, 2016-08-11T10:16:30+08:00
     */
    public static enum ToStringPolicy {

        /**
         * <p>
         * If object's toString method is overridden from specified up-to class (or Object if no specifying), use
         * valueOf function; else use reflection to toString.
         * </p>
         * 
         * @since 0.0.0
         */
        AUTO,

        /**
         * <p>
         * Always use valueOf function to toString.
         * </p>
         * 
         * @since 0.0.0
         */
        VALUE_OF,

        /**
         * <p>
         * Always use reflection to toString.
         * </p>
         * 
         * @since 0.0.0
         */
        REFLECTION,
    }

}
