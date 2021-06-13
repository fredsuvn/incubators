package com.cogician.quicker.expression;

import java.util.Map;

import com.cogician.quicker.QuickParsingException;

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
 * Escape characters escape tokens of placeholders, region delimiters, determines and escape themselves. If an escape
 * escape a normal character, that escape will be ignored and disappeared.
 * </p>
 * <h2>Result detail</h2>
 * <p>
 * There two kind of methods: one return a string after parsing; the other return a
 * {@linkplain QuickExpressionParser.Result} which includes detail info.
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
/**
 * <p>
 * Quick expression parser such as:
 * 
 * <pre>
 * "This is a ${placeholder}."
 * or
 * "This is a #{object.method()}."
 * </pre>
 * 
 * Performance is up to detail implementation.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-08-03T14:12:37+08:00
 * @since 0.0.0, 2016-08-03T14:12:37+08:00
 */
public interface QuickExpressionParser {

    /**
     * <p>
     * Parses input string.
     * </p>
     * 
     * @param input
     *            input string
     * @return string after parsing
     * @throws NullPointerException
     *             if input string is null
     * @throws QuickExpressionException
     *             if any problem about parsing occurs
     * @since 0.0.0
     */
    public String parse(String input) throws NullPointerException, QuickParsingException;

    /**
     * <p>
     * Parses input string.
     * </p>
     * 
     * @param input
     *            input string
     * @return string after parsing
     * @throws NullPointerException
     *             if input string is null
     * @throws QuickExpressionException
     *             if any problem about parsing occurs
     * @since 0.0.0
     */
    public QuickExpression parseDetail(String input) throws NullPointerException, QuickParsingException;

    /**
     * <p>
     * Parses input string with given arguments.
     * </p>
     * 
     * @param input
     *            input string
     * @param args
     *            given arguments
     * @return string after parsing
     * @throws NullPointerException
     *             if input string is null
     * @throws QuickExpressionException
     *             if any problem about parsing occurs
     * @since 0.0.0
     */
    public String parse(String input, Object... args) throws NullPointerException, QuickParsingException;

    /**
     * <p>
     * Parses input string with given arguments.
     * </p>
     * 
     * @param input
     *            input string
     * @param args
     *            given arguments
     * @return string after parsing
     * @throws NullPointerException
     *             if input string is null
     * @throws QuickExpressionException
     *             if any problem about parsing occurs
     * @since 0.0.0
     */
    public QuickExpression parseDetail(String input, Object... args) throws NullPointerException, QuickParsingException;

    /**
     * <p>
     * Parses input string with given arguments.
     * </p>
     * 
     * @param input
     *            input string
     * @param argumentMap
     *            given arguments
     * @return string after parsing
     * @throws NullPointerExceptionif
     *             input string is null
     * @throws QuickExpressionException
     *             if any problem about parsing occurs
     * @since 0.0.0
     */
    public String parse(String input, Map<String, Object> args) throws NullPointerException, QuickParsingException;

    /**
     * <p>
     * Parses input string with given arguments.
     * </p>
     * 
     * @param input
     *            input string
     * @param args
     *            given arguments
     * @return string after parsing
     * @throws NullPointerExceptionif
     *             input string is null
     * @throws QuickExpressionException
     *             if any problem about parsing occurs
     * @since 0.0.0
     */
    public QuickExpression parseDetail(String input, Map<String, Object> args)
            throws NullPointerException, QuickParsingException;
}
