package com.cogician.quicker.util.tostring;

import javax.annotation.Nullable;

/**
 * Static quick utility class provides static methods for {@linkplain Object#toString()}. See {@linkplain QuickToString}
 * , {@linkplain ObjectQuickToStringStyle} and {@linkplain ArrayQuickToStringStyle}.
 *
 * @author Fred Suvn
 * @version 0.0.0, 2015-10-10 14:17:12
 * @since 0.0.0
 * @see QuickToString
 * @see ObjectQuickToStringStyle
 * @see ArrayQuickToStringStyle
 */
public class QuickToStrings {

    /**
     * <p>
     * Default {@linkplain QuickToString} instance without deep reflection.
     * </p>
     * 
     * @since 0.0.0
     */
    public static final QuickToString DEFAULT_NO_DEEP = new QuickToStringBuilder().build();

    /**
     * <p>
     * Default {@linkplain QuickToString} instance with deep reflection.
     * </p>
     * 
     * @since 0.0.0
     */
    public static final QuickToString DEFAULT_DEEP;

    static {
        QuickToStringBuilder builder = new QuickToStringBuilder();
        builder.objectStyle().setDeep(true);
        builder.arrayStyle().setDeep(true);
        DEFAULT_DEEP = builder.build();
    }

    /**
     * <p>
     * Use reflection to build a string represent given object by default style.
     * </p>
     * 
     * @param obj
     *            given object
     * @return a string represents given object by default style
     * @since 0.0.0
     * @see #DEFAULT_NO_DEEP
     */
    public static String reflectToString(@Nullable Object obj) {
        return reflectToString(obj, false);
    }

    /**
     * <p>
     * Use deep reflection to build a string represent given object by default reflection style.
     * </p>
     * 
     * @param obj
     *            given object
     * @return a string represents given object by default deep reflection style
     * @since 0.0.0
     * @see #DEFAULT_NO_DEEP
     */
    public static String reflectToString(@Nullable Object obj, boolean deep) {
        return deep ? DEFAULT_DEEP.toString(obj) : DEFAULT_NO_DEEP.toString(obj);
    }

    private static final ObjectQuickToStringStyle MULTI_LINE_OBJECT_STYLE;
    private static final ArrayQuickToStringStyle MULTI_LINE_ARRAY_STYLE;

    static {
        MULTI_LINE_OBJECT_STYLE = new ObjectQuickToStringStyle();
        MULTI_LINE_OBJECT_STYLE.setPrefix(MULTI_LINE_OBJECT_STYLE.getPrefix() + System.lineSeparator());
        MULTI_LINE_OBJECT_STYLE.setSeparator(MULTI_LINE_OBJECT_STYLE.getSeparator() + System.lineSeparator());
        MULTI_LINE_OBJECT_STYLE.setSuffix(System.lineSeparator() + MULTI_LINE_OBJECT_STYLE.getSuffix());

        MULTI_LINE_ARRAY_STYLE = new ArrayQuickToStringStyle();
        MULTI_LINE_ARRAY_STYLE.setPrefix(MULTI_LINE_ARRAY_STYLE.getPrefix() + System.lineSeparator());
        MULTI_LINE_ARRAY_STYLE.setSeparator(MULTI_LINE_ARRAY_STYLE.getSeparator() + System.lineSeparator());
        MULTI_LINE_ARRAY_STYLE.setSuffix(System.lineSeparator() + MULTI_LINE_ARRAY_STYLE.getSuffix());
    }

    /**
     * <p>
     * Returns a multi-line object style.
     * </p>
     * 
     * @return multi-line object style
     * @since 0.0.0
     */
    public static ObjectQuickToStringStyle multiLineObjectStyle() {
        return MULTI_LINE_OBJECT_STYLE.clone();
    }

    /**
     * <p>
     * Returns a multi-line array style.
     * </p>
     * 
     * @return multi-line array style
     * @since 0.0.0
     */
    public static ArrayQuickToStringStyle multiLineArrayStyle() {
        return MULTI_LINE_ARRAY_STYLE.clone();
    }
}
