package com.cogician.quicker.util;

import java.util.regex.Pattern;

import com.cogician.quicker.Checker;

/**
 * <p>
 * Common patterns for regular expression.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-04-12T11:14:34+08:00
 * @since 0.0.0, 2016-04-12T11:14:34+08:00
 */
public class Patterns {

	/**
	 * <p>
	 * Checks whether given regular expression matches full string from start
	 * position ("^") to end position ("$"). If it is, return the original
	 * regular expression string; else it will add start position pattern
	 * character "^" and end position pattern "$" if necessary. For example:
	 * 
	 * <pre>
	 * System.out.println(fullMatch("abc"));
	 * System.out.println(fullMatch("^abc"));
	 * System.out.println(fullMatch("abc$"));
	 * System.out.println(fullMatch("^abc$"));
	 * </pre>
	 * 
	 * All above codes will print:
	 * 
	 * <pre>
	 * ^abc$
	 * </pre>
	 * 
	 * </p>
	 * 
	 * @param regex
	 *            given regular expression, not null
	 * @return regular expression including start and end position, not null
	 * @throws NullPointerException
	 *             if given pattern is null
	 * @since 0.0.0
	 */
	public static String fullMatch(String regex) throws NullPointerException {
		Checker.checkNull(regex);
		String r = regex;
		if (regex.indexOf('^') != 0) {
			r = "^" + r;
		}
		if (regex.indexOf('$') != regex.length() - 1) {
			r = r + "$";
		}
		return r;
	}

	/**
	 * <p>
	 * Regular expression to match any.
	 * </p>
	 * 
	 * @since 0.0.0
	 */
	public static final String EXP_ANY = "[\\s\\S]*";

	/**
	 * <p>
	 * Pattern to match any.
	 * </p>
	 * 
	 * @since 0.0.0
	 */
	public static final Pattern ANY = Pattern.compile(EXP_ANY);

	/**
	 * <p>
	 * Regular expression of identifier consisting of digits, letters or
	 * underline (must start with a letter or underline):
	 * 
	 * <pre>
	 * {@value #EXP_IDENTIFIER}
	 * </pre>
	 * </p>
	 * 
	 * @since 0.0.0
	 */
	public static final String EXP_IDENTIFIER = "[a-zA-Z_][a-zA-Z0-9_]*";

	/**
	 * <p>
	 * Pattern of identifier consisting of digits, letters or underline (must
	 * start with a letter or underline):
	 * 
	 * <pre>
	 * {@value #EXP_IDENTIFIER}
	 * </pre>
	 * </p>
	 * 
	 * @since 0.0.0
	 */
	public static final Pattern IDENTIFIER = Pattern.compile(EXP_IDENTIFIER);

	/**
	 * <p>
	 * Regular expression of common number:
	 * 
	 * <pre>
	 * {@value #EXP_NUMBER}
	 * </pre>
	 * </p>
	 * 
	 * @since 0.0.0
	 */
	public static final String EXP_NUMBER = "\\d*\\.?\\d+";

	/**
	 * <p>
	 * Pattern of common number:
	 * 
	 * <pre>
	 * {@value #EXP_NUMBER}
	 * </pre>
	 * </p>
	 * 
	 * @since 0.0.0
	 */
	public static final Pattern NUMBER = Pattern.compile(EXP_NUMBER);

	/**
	 * <p>
	 * Regular expression of scientific notation:
	 * 
	 * <pre>
	 * {@value #EXP_NUMBER}
	 * </pre>
	 * </p>
	 * 
	 * @since 0.0.0
	 */
	public static final String EXP_SCIENTIFIC_NOTATION = "[1-9](?:\\.\\d+)?[eE][+-]?\\d+";

	/**
	 * <p>
	 * Pattern of scientific notation:
	 * 
	 * <pre>
	 * {@value #EXP_NUMBER}
	 * </pre>
	 * </p>
	 * 
	 * @since 0.0.0
	 */
	public static final Pattern SCIENTIFIC_NOTATION = Pattern.compile(EXP_SCIENTIFIC_NOTATION);

	/**
	 * <p>
	 * Regular expression of binary number:
	 * 
	 * <pre>
	 * {@value #EXP_BINARY_NUMBER}
	 * </pre>
	 * </p>
	 * 
	 * @since 0.0.0
	 */
	public static final String EXP_BINARY_NUMBER = "0[bB][01]+";

	/**
	 * <p>
	 * Pattern of binary number:
	 * 
	 * <pre>
	 * {@value #EXP_BINARY_NUMBER}
	 * </pre>
	 * </p>
	 * 
	 * @since 0.0.0
	 */
	public static final Pattern BIN_NUMBER = Pattern.compile(EXP_BINARY_NUMBER);

	/**
	 * <p>
	 * Regular expression of octal number:
	 * 
	 * <pre>
	 * {@value #EXP_OCTAL_NUMBER}
	 * </pre>
	 * </p>
	 * 
	 * @since 0.0.0
	 */
	public static final String EXP_OCTAL_NUMBER = "0[07]+";

	/**
	 * <p>
	 * Pattern of octal number:
	 * 
	 * <pre>
	 * {@value #EXP_OCTAL_NUMBER}
	 * </pre>
	 * </p>
	 * 
	 * @since 0.0.0
	 */
	public static final Pattern OCTAL_NUMBER = Pattern.compile(EXP_OCTAL_NUMBER);

	/**
	 * <p>
	 * Regular expression of hex number:
	 * 
	 * <pre>
	 * {@value #EXP_HEX_NUMBER}
	 * </pre>
	 * </p>
	 * 
	 * @since 0.0.0
	 */
	public static final String EXP_HEX_NUMBER = "0[xX][0-9a-fA-F]+";

	/**
	 * <p>
	 * Pattern of hex number:
	 * 
	 * <pre>
	 * {@value #EXP_HEX_NUMBER}
	 * </pre>
	 * </p>
	 * 
	 * @since 0.0.0
	 */
	public static final Pattern HEX_NUMBER = Pattern.compile(EXP_HEX_NUMBER);

	/**
	 * <p>
	 * Regular expression of common operator:
	 * 
	 * <pre>
	 * {@value #EXP_OPERATOR}
	 * </pre>
	 * </p>
	 * 
	 * @since 0.0.0
	 */
	public static final String EXP_OPERATOR = "\\+\\+|--|<<|>>>|>>|[\\+\\-\\*/%&\\|~\\^]";

	/**
	 * <p>
	 * Pattern of common operator:
	 * 
	 * <pre>
	 * {@value #EXP_OPERATOR}
	 * </pre>
	 * </p>
	 * 
	 * @since 0.0.0
	 */
	public static final Pattern OPERATOR = Pattern.compile(EXP_OPERATOR);

	/**
	 * <p>
	 * Regular expression of one or more spaces or tables:
	 * 
	 * <pre>
	 * {@value #EXP_SPACE}
	 * </pre>
	 * </p>
	 * 
	 * @since 0.0.0
	 */
	public static final String EXP_SPACE = "[ \\t]+";

	/**
	 * <p>
	 * Pattern of one or more spaces or tables:
	 * 
	 * <pre>
	 * {@value #EXP_SPACE}
	 * </pre>
	 * </p>
	 * 
	 * @since 0.0.0
	 */
	public static final Pattern SPACE = Pattern.compile(EXP_SPACE);

	/**
	 * <p>
	 * Regular expression of linefeed:
	 * 
	 * <pre>
	 * {@value #EXP_CRLF}
	 * </pre>
	 * </p>
	 * 
	 * @since 0.0.0
	 */
	public static final String EXP_CRLF = "(?:\\r?\\n)|\\r";

	/**
	 * <p>
	 * Pattern of linefeed:
	 * 
	 * <pre>
	 * {@value #EXP_CRLF}
	 * </pre>
	 * </p>
	 * 
	 * @since 0.0.0
	 */
	public static final Pattern CRLF = Pattern.compile(EXP_CRLF);

}
