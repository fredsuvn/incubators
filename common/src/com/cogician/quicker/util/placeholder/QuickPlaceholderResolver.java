package com.cogician.quicker.util.placeholder;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * <p>
 * Quick placeholder resolver is used to resolve a string which has placeholders such as:
 * 
 * <pre>
 * String str0 = "this is a ${0} string."
 * String str1 = "this is a ${name} string."
 * </pre>
 * 
 * Using following codes can resolve above string:
 * 
 * <pre>
 * PlaceholderResolver.getDefaultNestable().resolve(str0, "test");
 * </pre>
 * 
 * or
 * 
 * <pre>
 * Map<String, String> map = getMap();
 * map.put("name", "test");
 * PlaceholderResolver.getDefaultNestable().resolve(str1, map);
 * </pre>
 * 
 * Both can return the result string: "this is a test string.". For str0, ${0} specifies index 0 of arguments array. And
 * for str1, ${name} specifies the key "name" of the arguments.
 * </p>
 * <h2>Tokens of placeholder</h2>
 * <p>
 * Number of tokens of placeholder may be two -- a prefix and a suffix, or only one. In above example, "${" and "}" a
 * pair prefix and suffix tokens. The other example of one token is:
 * 
 * <pre>
 * "This is a :name string."
 * </pre>
 * 
 * In that example ":" is the token of placeholder.
 * </p>
 * <h2>Region delimiters and determines</h2>
 * <p>
 * Default implementation support region delimiter tokens, which can separate a region:
 * 
 * <pre>
 * This is a ${one} string&#60;[, and that is ${other} one]&#62.
 * </pre>
 * 
 * "&#60;[" and "]&#62" separate the region ", and that is ${other} one". Separated region may be ignored if there is no
 * value of ${other}. Or using determine token "?" to do this if separated region doesn't need ${other}:
 * 
 * <pre>
 * This is a ${one} string&#60;[${other}?, and that is the other one]&#62.
 * </pre>
 * 
 * In above example, if ${other} is existing, ", and that is the other one" will be printed, or it will be ignored.
 * Delimiters and determines are simple and useful in sql concatenation:
 * 
 * <pre>
 * select * from table t where 1 = 1
 * <[ and t.ID = :id ]>
 * <[ and t.NAME = :name ]>
 * <[ and t.GROUP = :group ]>
 * order by t.date
 * <[:id? ,t.ID ]>
 * <[:name? ,t.NAME ]>
 * <[:group? ,t.GROUP ]>
 * </pre>
 * </p>
 * <h2>Nesting and unnesting</h2>
 * <p>
 * Default implementation support nesting for two-tokens-placeholder and delimiters:
 * 
 * <pre>
 * this is a ${name0${name1}} string.
 * </pre>
 * 
 * In above example, it will resolve ${name1} first, if value of ${name1} is test, then it will resolve ${name0test}.
 * Same way from delimiters.
 * </p>
 * <p>
 * Note for unnesting placeholder resolver, a legal param name commonly only consists of numbers and letters.
 * </p>
 * <h2>Escape</h2>
 * <p>
 * Escape characters escape tokens of placeholders, region delimiters, determines and escape themselves. If an escape escape a
 * normal character, that escape will be ignored and disappeared.
 * </p>
 * <h2>Result detail</h2>
 * <p>
 * There two kind of methods: one return a string after resolving; the other return a
 * {@linkplain QuickPlaceholderResolver.Result} which includes detail info.
 * </p>
 * <h2>Builder</h2>
 * <p>
 * Default implementation is built from {@linkplain PlaceholderResolverBuilder}. See it for more details.
 * </p>
 * <h2>Is thread-safe?</h2>
 * <p>
 * Default implementation from {@linkplain PlaceholderResolverBuilder} is thread-safe if dependent arguments and objects
 * (array or map) are thread-safe, and vice versa.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-08-03T14:12:37+08:00
 * @since 0.0.0, 2016-08-03T14:12:37+08:00
 * @see PlaceholderResolverBuilder
 */
public interface QuickPlaceholderResolver {

    /**
     * <p>
     * Returns default nesting placeholder resolver, using place prefix "${", place suffix "}", delimiter prefix "<[",
     * delimiter suffix "]>", determine "?", escape "\".
     * </p>
     * 
     * @return default nesting placeholder resolver
     * @since 0.0.0
     */
    public static QuickPlaceholderResolver defaultResolver() {
        return PlaceholderResolverBuilder.DEFAULT_RESOLVER;
    }

    /**
     * <p>
     * Returns default log placeholder resolver, using place prefix "%", place suffix null, delimiter prefix "<[",
     * delimiter suffix "]>", determine "?", escape "\".
     * </p>
     * 
     * @return default nesting placeholder resolver
     * @since 0.0.0
     */
    public static QuickPlaceholderResolver defaultLogResolver() {
        return PlaceholderResolverBuilder.DEFAULT_LOG_RESOLVER;
    }

    /**
     * <p>
     * Returns default SQL placeholder resolver, using place prefix ":", place suffix null, delimiter prefix "<[",
     * delimiter suffix "]>", determine "?", escape "\".
     * </p>
     * 
     * @return default nesting placeholder resolver
     * @since 0.0.0
     */
    public static QuickPlaceholderResolver defaultSQLResolver() {
        return PlaceholderResolverBuilder.DEFAULT_SQL_RESOLVER;
    }

    /**
     * <p>
     * Returnes default nesting placeholder resolver with specified value map, place prefix "${", place suffix "}",
     * delimiter prefix "<[", delimiter suffix "]>", determine "?", escape "\".
     * </p>
     * 
     * @param value
     *            map specified value map
     * @return default nesting placeholder resolver
     * @throws NullPointerException
     *             if specified value map is null
     * @since 0.0.0
     */
    public static QuickPlaceholderResolver defaultNestablewithMap(Map<String, ? extends Object> valueMap)
            throws NullPointerException {
        return PlaceholderResolverBuilder.DEFAULT_RESOLVER_BUILDER.setValueMap(valueMap).build();
    }

    /**
     * <p>
     * Returnes default unnesting placeholder resolver with specified value map, using place prefix ":", place suffix
     * null, delimiter prefix "<[", delimiter suffix "]>", determine "?", escape "\".
     * </p>
     * 
     * @param value
     *            map specified value map
     * @return default nesting placeholder resolver
     * @throws NullPointerException
     *             if specified value map is null
     * @since 0.0.0
     */
    public static QuickPlaceholderResolver defaultUnnestablewithMap(Map<String, ? extends Object> valueMap) {
        return PlaceholderResolverBuilder.DEFAULT_SQL_RESOLVER_BUILDER.setValueMap(valueMap).build();
    }

    /**
     * <p>
     * Resolves input string according to current instance.
     * </p>
     * 
     * @param input
     *            input string
     * @return string after resolving
     * @throws NullPointerException
     *             if input string is null
     * @throws PlaceholderException
     *             if any problem about placeholder occurs
     * @throws UnsupportedOperationException
     *             if current instance doesn't support this method
     * @since 0.0.0
     */
    public String resolve(String input)
            throws NullPointerException, PlaceholderException, UnsupportedOperationException;

    /**
     * <p>
     * Resolves input string according to current instance.
     * </p>
     * 
     * @param input
     *            input string
     * @return string after resolving
     * @throws NullPointerException
     *             if input string is null
     * @throws PlaceholderException
     *             if any problem about placeholder occurs
     * @throws UnsupportedOperationException
     *             if current instance doesn't support this method
     * @since 0.0.0
     */
    public QuickPlaceholderResolver.Result resolveDetail(String input)
            throws NullPointerException, PlaceholderException, UnsupportedOperationException;

    /**
     * <p>
     * Resolves input string with given arguments.
     * </p>
     * 
     * @param input
     *            input string
     * @param args
     *            given arguments
     * @return string after resolving
     * @throws NullPointerException
     *             if input string is null
     * @throws PlaceholderException
     *             if any problem about placeholder occurs
     * @since 0.0.0
     */
    public String resolve(String input, Object... args) throws NullPointerException, PlaceholderException;

    /**
     * <p>
     * Resolves input string with given arguments.
     * </p>
     * 
     * @param input
     *            input string
     * @param args
     *            given arguments
     * @return string after resolving
     * @throws NullPointerException
     *             if input string is null
     * @throws PlaceholderException
     *             if any problem about placeholder occurs
     * @since 0.0.0
     */
    public QuickPlaceholderResolver.Result resolveDetail(String input, Object... args)
            throws NullPointerException, PlaceholderException;

    /**
     * <p>
     * Resolves input string with given value map. In this method, placeholders of input string have corresponding
     * replaced value in given value map.
     * </p>
     * 
     * @param input
     *            input string
     * @param valueMap
     *            given value map
     * @return string after resolving
     * @throws NullPointerExceptionif
     *             input string is null
     * @throws PlaceholderException
     *             if any problem about placeholder occurs
     * @since 0.0.0
     */
    public String resolve(String input, Map<String, ?> valueMap)
            throws NullPointerException, PlaceholderException;

    /**
     * <p>
     * Resolves input string with given value map. In this method, placeholders of input string have corresponding
     * replaced value in given value map.
     * </p>
     * 
     * @param input
     *            input string
     * @param valueMap
     *            given value map
     * @return string after resolving
     * @throws NullPointerExceptionif
     *             input string is null
     * @throws PlaceholderException
     *             if any problem about placeholder occurs
     * @since 0.0.0
     */
    public QuickPlaceholderResolver.Result resolveDetail(String input, Map<String, ?> valueMap)
            throws NullPointerException, PlaceholderException;

    /**
     * <p>
     * Detail result of resolving, including final result string and used arguments on final result string in the order
     * that they appear on final result string. The arguments may be repeated if it is used repeatedly. For example:
     * 
     * <pre>
     * PlaceholderResolver.Result result = resolver.resolveDetail("${0} is ${0}.", "orange",);
     * </pre>
     * 
     * In above example, {@linkplain #getUsedArguments()} will contain two object and both are "orange". Note arguments
     * used in determine token are not considered as used arguments.
     * </p>
     *
     * @author Fred Suvn
     * @version 0.0.0, 2016-08-20T20:18:06+08:00
     * @since 0.0.0, 2016-08-20T20:18:06+08:00
     */
    public static interface Result {

        /**
         * <p>
         * Returns final string after resolving.
         * </p>
         * 
         * @return final string after resolving
         * @since 0.0.0
         */
        public String getFinalString();

        /**
         * <p>
         * Returns used arguments in this resolving in used order. The arguments
         * may be repeated if it is used repeatedly. Returned list is read-only.
         * </p>
         * 
         * @return used arguments in this resolving in used order
         * @since 0.0.0
         */
        public List<Entry<String, ?>> getUsedArguments();
        
    }
}
