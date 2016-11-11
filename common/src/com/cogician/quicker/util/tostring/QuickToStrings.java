package com.cogician.quicker.util.tostring;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.cogician.quicker.Buildable;
import com.cogician.quicker.Checker;
import com.cogician.quicker.Quicker;
import com.cogician.quicker.QuickerUniform;
import com.cogician.quicker.util.Consts;
import com.cogician.quicker.util.QuickArrays;
import com.cogician.quicker.util.QuickCollections;
import com.cogician.quicker.util.reflect.QuickReflections;

/**
 * Static quick utility class provides static methods for {@linkplain Object#toString()}.
 *
 * @author Fred Suvn
 * @version 0.0.0, 2015-10-10 14:17:12
 * @since 0.0.0
 */
public class QuickToStrings {

    //public static 

    /**
     * <p>
     * Builder for {@linkplain ReflectionQuickToString}.
     * </p>
     *
     * @author Fred Suvn
     * @version 0.0.0, 2016-11-11T10:54:45+08:00
     * @since 0.0.0, 2016-11-11T10:54:45+08:00
     */
    public static class ReflectionQuickToStringBuilder implements Buildable<ReflectionQuickToString> {

        private String prefix = "{";
        private String suffix = "}";
        private String indicator = "=";
        private String separator = ",";
        private Class<?> upTo = null;
        private boolean deep = false;

        private Set<String> inclusive = null;
        private Set<String> exclusive = null;

        private Function<Object, String> signature = null;
        private BiFunction<Field, Object, String> special = null;

        /**
         * <p>
         * Constructs with default configurations.
         * </p>
         * 
         * @since 0.0.0
         */
        public ReflectionQuickToStringBuilder() {

        }

        /**
         * <p>
         * Sets signature function.
         * </p>
         * 
         * @param signature
         *            signature function
         * @return this builder
         * @since 0.0.0
         */
        public ReflectionQuickToStringBuilder setSignature(@Nullable Function<Object, String> signature) {
            this.signature = signature;
            return this;
        }

        /**
         * <p>
         * Sets prefix of reflected fields string, maybe null and null will be considered as empty.
         * </p>
         * 
         * @param prefix
         *            prefix of reflected fields string
         * @return this builder
         * @since 0.0.0
         */
        public ReflectionQuickToStringBuilder setPrefix(@Nullable String prefix) {
            this.prefix = Quicker.nonnull(prefix);
            return this;
        }

        /**
         * <p>
         * Sets suffix of reflected fields string, maybe null and null will be considered as empty.
         * </p>
         * 
         * @param suffix
         *            suffix of reflected fields string
         * @return this builder
         * @since 0.0.0
         */
        public ReflectionQuickToStringBuilder setSuffix(String suffix) {
            this.suffix = Quicker.nonnull(suffix);
            return this;
        }

        /**
         * <p>
         * Sets field value indicator of reflected fields string, maybe null and null will be considered as empty.
         * </p>
         * 
         * @param indicator
         *            field value indicator of reflected fields string
         * @return this builder
         * @since 0.0.0
         */
        public ReflectionQuickToStringBuilder setIndicator(String indicator) {
            this.indicator = Quicker.nonnull(indicator);
            return this;
        }

        /**
         * <p>
         * Sets field separator of reflected fields string, maybe null and null will be considered as empty.
         * </p>
         * 
         * @param separator
         *            field separator of reflected fields string
         * @return this builder
         * @since 0.0.0
         */
        public ReflectionQuickToStringBuilder setSeparator(String separator) {
            this.separator = Quicker.nonnull(separator);
            return this;
        }

        /**
         * <p>
         * Sets last and inclusive reflected parent class which this toString reach to.
         * </p>
         * 
         * @param upTo
         *            last and inclusive reflected parent class which this toString reach to
         * @return this builder
         * @since 0.0.0
         */
        public ReflectionQuickToStringBuilder setUpTo(@Nullable Class<?> upTo) {
            this.upTo = upTo;
            return this;
        }

        /**
         * <p>
         * Sets whether a field will be reflected to string by this toString if it doesn't override toString method upon
         * Object class.
         * </p>
         * 
         * @param deep
         *            whether a field will be reflected to string by this toString if it doesn't override toString
         *            method upon Object class
         * @return this builder
         * @since 0.0.0
         */
        public ReflectionQuickToStringBuilder setDeep(boolean deep) {
            this.deep = deep;
            return this;
        }

        /**
         * <p>
         * Sets inclusive filed names which will be reflected to string, maybe null if all fields reflected.
         * </p>
         * 
         * @param inclusive
         *            inclusive filed names which will be reflected to string
         * @return this builder
         * @since 0.0.0
         */
        public ReflectionQuickToStringBuilder setInclusive(@Nullable Set<String> inclusive) {
            this.inclusive = inclusive;
            return this;
        }

        /**
         * <p>
         * Sets exclusive filed names which will be reflected to string.
         * </p>
         * 
         * @param exclusive
         *            exclusive filed names which will be reflected to string
         * @return this builder
         * @since 0.0.0
         */
        public ReflectionQuickToStringBuilder setExclusive(@Nullable Set<String> exclusive) {
            this.exclusive = exclusive;
            return this;
        }

        /**
         * <p>
         * Sets special toString tool to build string for each reflected field.
         * </p>
         * 
         * @param special
         *            special toString tool to build string for each reflected field
         * @return this builder
         * @since 0.0.0
         */
        public ReflectionQuickToStringBuilder setSpecial(@Nullable BiFunction<Field, Object, String> special) {
            this.special = special;
            return this;
        }

        /**
         * <p>
         * Builds toString according to this configurations.
         * </p>
         * 
         * @return toString according to this configurations
         * @since 0.0.0
         */
        @Override
        public ReflectionQuickToString build() {
            return new ReflectionQuickToString() {

                private String prefix = ReflectionQuickToStringBuilder.this.prefix;
                private String suffix = ReflectionQuickToStringBuilder.this.suffix;
                private String indicator = ReflectionQuickToStringBuilder.this.indicator;
                private String separator = ReflectionQuickToStringBuilder.this.separator;
                private Class<?> upTo = ReflectionQuickToStringBuilder.this.upTo;
                private boolean deep = ReflectionQuickToStringBuilder.this.deep;

                private Set<String> inclusive = ReflectionQuickToStringBuilder.this.inclusive;
                private Set<String> exclusive = ReflectionQuickToStringBuilder.this.exclusive;

                private Function<Object, String> signature = ReflectionQuickToStringBuilder.this.signature;
                private BiFunction<Field, Object, String> special = ReflectionQuickToStringBuilder.this.special;

                @Override
                public boolean isDeep() {
                    return deep;
                }

                @Override
                public @Nullable Class<?> getUpTo() {
                    return upTo;
                }

                @Override
                public String getSuffix() {
                    return suffix;
                }

                @Override
                public @Nullable BiFunction<Field, Object, String> getSpecial() {
                    return special;
                }

                @Override
                public @Nullable Function<Object, String> getSignature() {
                    return signature;
                }

                @Override
                public String getSeparator() {
                    return separator;
                }

                @Override
                public String getPrefix() {
                    return prefix;
                }

                @Override
                public String getIndicator() {
                    return indicator;
                }

                @Override
                public @Nullable Set<String> getInclusiveField() {
                    return inclusive;
                }

                @Override
                public @Nullable Set<String> getExclusiveField() {
                    return exclusive;
                }

                @Override
                public String toString(Object obj) {
                    if (obj == null) {
                        return String.valueOf(obj);
                    }
                    StringBuilder sb = new StringBuilder();
                    Class<?> objCls = obj.getClass();
                    sb.append(getSignature() == null ? objCls.getName() : getSignature().apply(obj));
                    sb.append(getPrefix());
                    List<Field> reflected = new ArrayList<>();
                    Quicker.flow(QuickReflections.getClassInheritanceTree(objCls, getUpTo())).each(cls -> {
                        Field[] fs = cls.getDeclaredFields();
                        Set<String> in = getInclusiveField();
                        if (Checker.isNotEmpty(in)) {
                            for (int i = 0; i < fs.length; i++) {
                                if (in.contains(fs[i].getName())) {
                                    reflected.add(fs[i]);
                                }
                            }
                        } else {
                            reflected.addAll(Arrays.asList(fs));
                        }
                    });

                    List<Field> list = QuickCollections.distinct(reflected);
                    Set<String> ex = Quicker.nonnull(getExclusiveField());
                    BiFunction<Field, Object, String> special = getSpecial();
                    if (special == null) {
                        special = (f, o) -> {
                            Object value = null;
                            try {
                                value = f.get(o);
                            } catch (Exception e) {
                            }
                            if (isDeep() && !isOverrideToString(f)) {
                                return this.toString(value);
                            } else {
                                return String.valueOf(value);
                            }
                        };
                    }
                    List<String> joined = new ArrayList<>();
                    for (int i = 0; i < list.size();) {
                        Field f = list.get(i);
                        if (!ex.contains(f.getName())) {
                            StringBuilder fieldStr = new StringBuilder();
                            fieldStr.append(f.getName()).append(getSeparator());
                            fieldStr.append(special.apply(f, obj));
                            joined.add(fieldStr.toString());
                        }
                    }
                    sb.append(QuickCollections.join(joined, getSeparator()));

                    sb.append(getSuffix());
                    return sb.toString();
                }

                private boolean isOverrideToString(Field f) {
                    Class<?> cls = f.getClass();
                    if (cls.isPrimitive()) {
                        return true;
                    }
                    Method m = QuickReflections.getMethod(cls, "toString", Consts.emptyClassArray());
                    Class<?> toStringCls = m.getClass();
                    Class<?> upTo = getUpTo() == null ? Object.class : getUpTo();
                    return upTo.isAssignableFrom(toStringCls);
                }
            };
        }
    }

    /**
     * <p>
     * Builder for {@linkplain ArrayQuickToString}.
     * </p>
     *
     * @author Fred Suvn
     * @version 0.0.0, 2016-11-11T10:54:45+08:00
     * @since 0.0.0, 2016-11-11T10:54:45+08:00
     */
    public static class ArrayQuickToStringBuilder implements Buildable<ArrayQuickToString> {

        private String prefix = "[";
        private String suffix = "]";
        private String separator = ",";
        private boolean deep = false;

        private QuickToString special = null;

        /**
         * <p>
         * Constructs with default configurations.
         * </p>
         * 
         * @since 0.0.0
         */
        public ArrayQuickToStringBuilder() {

        }

        /**
         * <p>
         * Sets prefix of array string, maybe null and null will be considered as empty.
         * </p>
         * 
         * @param prefix
         *            prefix of array string
         * @return this builder
         * @since 0.0.0
         */
        public ArrayQuickToStringBuilder setPrefix(@Nullable String prefix) {
            this.prefix = Quicker.nonnull(prefix);
            return this;
        }

        /**
         * <p>
         * Sets suffix of array string, maybe null and null will be considered as empty.
         * </p>
         * 
         * @param suffix
         *            suffix of array string
         * @return this builder
         * @since 0.0.0
         */
        public ArrayQuickToStringBuilder setSuffix(String suffix) {
            this.suffix = Quicker.nonnull(suffix);
            return this;
        }

        /**
         * <p>
         * Sets element separator of array string, maybe null and null will be considered as empty.
         * </p>
         * 
         * @param separator
         *            element separator of array string
         * @return this builder
         * @since 0.0.0
         */
        public ArrayQuickToStringBuilder setSeparator(String separator) {
            this.separator = Quicker.nonnull(separator);
            return this;
        }

        /**
         * <p>
         * Sets whether an element will be toString by this toString if it is an array.
         * </p>
         * 
         * @param deep
         *            an element will be toString by this toString if it is an array
         * @return this builder
         * @since 0.0.0
         */
        public ArrayQuickToStringBuilder setDeep(boolean deep) {
            this.deep = deep;
            return this;
        }

        /**
         * <p>
         * Sets special toString tool to build string for each element.
         * </p>
         * 
         * @param special
         *            special toString tool to build string for each element
         * @return this builder
         * @since 0.0.0
         */
        public ArrayQuickToStringBuilder setSpecial(@Nullable QuickToString special) {
            this.special = special;
            return this;
        }

        /**
         * <p>
         * Builds toString according to this configurations.
         * </p>
         * 
         * @return toString according to this configurations
         * @since 0.0.0
         */
        @Override
        public ArrayQuickToString build() {
            return new ArrayQuickToString() {

                private String prefix = ArrayQuickToStringBuilder.this.prefix;
                private String suffix = ArrayQuickToStringBuilder.this.suffix;
                private String separator = ArrayQuickToStringBuilder.this.separator;
                private boolean deep = ArrayQuickToStringBuilder.this.deep;

                private QuickToString special = ArrayQuickToStringBuilder.this.special;

                @Override
                public boolean isDeep() {
                    return deep;
                }

                @Override
                public String getSuffix() {
                    return suffix;
                }

                @Override
                public QuickToString getSpecial() {
                    return special;
                }

                @Override
                public String getSeparator() {
                    return separator;
                }

                @Override
                public String getPrefix() {
                    return prefix;
                }

                @Override
                public String toString(Object array) {
                    if (array == null) {
                        return String.valueOf(array);
                    }
                    Checker.checkArray(array);
                    StringBuilder sb = new StringBuilder();
                    sb.append(getPrefix());
                    int length = Array.getLength(array);
                    List<String> joined = new ArrayList<>();
                    QuickToString special = getSpecial();
                    Function<Object, String> elementToString;
                    if (special == null) {
                        if (isDeep()) {
                            elementToString = o -> {
                                if (Checker.isArray(o)) {
                                    return toString(o);
                                } else {
                                    return String.valueOf(o);
                                }
                            };
                        } else {
                            elementToString = o -> String.valueOf(o);
                        }
                    } else {
                        elementToString = o -> special.toString(o);
                    }
                    for (int i = 0; i < length; i++) {
                        joined.add(elementToString.apply(Array.get(array, i)));
                    }
                    sb.append(QuickCollections.join(joined, getSeparator()));
                    sb.append(getSuffix());
                    return sb.toString();
                }
            };
        }
    }
}
