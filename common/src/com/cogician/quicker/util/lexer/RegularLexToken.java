package com.cogician.quicker.util.lexer;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.cogician.quicker.util.Patterns;

/**
 * <p>
 * A type of {@linkplain LexToken} which use regular expression.
 * </p>
 * <p>
 * Each regular token uses a pattern of regular expression to indicate a token of which characters matches this pattern.
 * Or, a token's pattern may be null if this token only represents a more general type, which is a parent token of some
 * sub-tokens. Using {@linkplain #hasPattern()} to check whether this token has a valid pattern.
 * </p>
 * <p>
 * Note that a token only has one instance is still effective in this child class.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-04-25T10:41:38+08:00
 * @since 0.0.0, 2016-04-25T10:41:38+08:00
 * @see LexToken
 */
@Immutable
public class RegularLexToken extends LexToken {

    @Nullable
    private final Pattern pattern;

    /**
     * <p>
     * Constructs a new instance (and create a new type at the same time) with given name and null pattern.
     * </p>
     * 
     * @param name
     *            given name
     * @since 0.0.0
     */
    public RegularLexToken(String name) {
        this(name, (String)null, null);
    }

    /**
     * <p>
     * Constructs a new instance (and create a new type at the same time) with given name and given regular expression
     * of pattern.
     * </p>
     * 
     * @param name
     *            given name
     * @param regex
     *            given regular expression of pattern
     * @throws PatternSyntaxException
     *             if given regular expression can not be parsed correctly
     * @since 0.0.0
     */
    public RegularLexToken(String name, @Nullable String regex) throws PatternSyntaxException {
        this(name, regex, null);
    }

    /**
     * <p>
     * Constructs a new instance (and create a new type at the same time) with given name and pattern.
     * </p>
     * 
     * @param name
     *            given name
     * @param pattern
     *            given pattern
     * @since 0.0.0
     */
    public RegularLexToken(String name, @Nullable Pattern pattern) {
        this(name, pattern, null);
    }

    /**
     * <p>
     * Constructs a new instance (and create a new type at the same time) with given name, given regular expression of
     * pattern and super-token.
     * </p>
     * 
     * @param name
     *            given name
     * @param regex
     *            given regular expression of pattern
     * @param superToken
     *            given super-token
     * @throws PatternSyntaxException
     *             if given regular expression can not be parsed correctly
     * @since 0.0.0
     */
    public RegularLexToken(String name, @Nullable String regex, @Nullable RegularLexToken superToken)
            throws PatternSyntaxException {
        this(name, regex == null ? null : Pattern.compile(regex), superToken);
    }

    /**
     * <p>
     * Constructs a new instance (and create a new type at the same time) with given name, pattern and super-token.
     * </p>
     * 
     * @param name
     *            given name
     * @param pattern
     *            given pattern
     * @param superToken
     *            given super-token
     * @since 0.0.0
     */
    public RegularLexToken(String name, Pattern pattern, @Nullable RegularLexToken superToken)
            throws NullPointerException {
        super(name, superToken);
        this.pattern = pattern;
    }

    /**
     * <p>
     * Returns pattern of this token.
     * </p>
     * 
     * @return pattern of this token
     * @since 0.0.0
     */
    public Pattern getPattern() {
        return pattern;
    }

    /**
     * <p>
     * Returns whether this token has a valid pattern.
     * </p>
     * 
     * @return whether this token has a valid pattern
     * @since 0.0.0
     */
    public boolean hasPattern() {
        return getPattern() != null;
    }

    /**
     * <p>
     * Returns a string simply represents this token: name(pattern).
     * </p>
     * 
     * @return a string simply represents this token
     * @since 0.0.0
     */
    @Override
    public String toString() {
        return getName() + "(" + (getPattern() == null ? "" : getPattern()) + ")";
    }

    /**
     * <p>
     * Common identifier token. See {@linkplain Patterns#IDENTIFIER}.
     * </p>
     * 
     * @since 0.0.0
     */
    public static final RegularLexToken IDENTIFIER = new RegularLexToken("IDENTIFIER", Patterns.IDENTIFIER);

    /**
     * <p>
     * Common number token. See {@linkplain Patterns#NUMBER}.
     * </p>
     * 
     * @since 0.0.0
     */
    public static final RegularLexToken NUMBER = new RegularLexToken("NUMBER", Patterns.NUMBER);

    /**
     * <p>
     * Number of scientific notation token, sub-token of {@linkplain #NUMBER}. See
     * {@linkplain Patterns#SCIENTIFIC_NOTATION}.
     * </p>
     * 
     * @since 0.0.0
     */
    public static final RegularLexToken SCIENTIFIC_NOTATION = new RegularLexToken("NUMBER",
            Patterns.SCIENTIFIC_NOTATION, NUMBER);

    /**
     * <p>
     * Binary number token, sub-token of {@linkplain #NUMBER}. See {@linkplain Patterns#BINARY_NUMBER}.
     * </p>
     * 
     * @since 0.0.0
     */
    public static final RegularLexToken BIN_NUMBER = new RegularLexToken("BIN_NUMBER", Patterns.BIN_NUMBER, NUMBER);

    /**
     * <p>
     * Hex number token, sub-token of {@linkplain #NUMBER}. See {@linkplain Patterns#HEX_NUMBER}.
     * </p>
     * 
     * @since 0.0.0
     */
    public static final RegularLexToken HEX_NUMBER = new RegularLexToken("HEX_NUMBER", Patterns.HEX_NUMBER, NUMBER);

    /**
     * <p>
     * Octal number token, sub-token of {@linkplain #NUMBER}. See {@linkplain Patterns#OCTAL_NUMBER}.
     * </p>
     * 
     * @since 0.0.0
     */
    public static final RegularLexToken OCTAL_NUMBER = new RegularLexToken("OCTAL_NUMBER", Patterns.OCTAL_NUMBER,
            NUMBER);

    /**
     * <p>
     * Operator token. See {@linkplain Patterns#OPERATOR}.
     * </p>
     * 
     * @since 0.0.0
     */
    public static final RegularLexToken OPERATOR = new RegularLexToken("OPERATOR", Patterns.OPERATOR);

    /**
     * <p>
     * Super parenthesis token. This token doesn't represent any string, only being the super token of left and right
     * parenthesis.
     * </p>
     * 
     * @since 0.0.0
     */
    public static final RegularLexToken DELIMITER = new RegularLexToken("DELIMITER");

    /**
     * <p>
     * Left bracket token: "(".
     * </p>
     * 
     * @since 0.0.0
     */
    public static final RegularLexToken LEFT_BRACKET = new RegularLexToken("LEFT_BRACKET", "\\(", DELIMITER);

    /**
     * <p>
     * Right bracket token: ")".
     * </p>
     * 
     * @since 0.0.0
     */
    public static final RegularLexToken RIGHT_BRACKET = new RegularLexToken("RIGHT_BRACKET", "\\)", DELIMITER);

    /**
     * <p>
     * Comma token: ",".
     * </p>
     * 
     * @since 0.0.0
     */
    public static final RegularLexToken COMMA = new RegularLexToken("COMMA", Pattern.compile(","), DELIMITER);

    /**
     * <p>
     * Semicolon token: ";".
     * </p>
     * 
     * @since 0.0.0
     */
    public static final RegularLexToken SEMICOLON = new RegularLexToken("SEMICOLON", Pattern.compile(";"), DELIMITER);

    /**
     * <p>
     * Macro token: "#DEFINE", case insenstive.
     * </p>
     * 
     * @since 0.0.0
     */
    public static final RegularLexToken MACRO = new RegularLexToken("MACRO",
            Pattern.compile("#[dD][eE][fF][iI][nN][eE]"));

    /**
     * <p>
     * Super comment token. This token doesn't represent any string, only being the super token of other comments.
     * </p>
     * 
     * @since 0.0.0
     */
    public static final RegularLexToken COMMENT = new RegularLexToken("COMMENT");

    /**
     * <p>
     * Block comment token: "{@code /* [comment] *}{@code /}"
     * </p>
     * 
     * @since 0.0.0
     */
    public static final RegularLexToken BLOCK_COMMENT = new RegularLexToken("BLOCK_COMMENT",
            Pattern.compile("/\\*[^/]*\\*/"), COMMENT);

    /**
     * <p>
     * Line comment token: "{@code // [comment] }"
     * </p>
     * 
     * @since 0.0.0
     */
    public static final RegularLexToken LINE_COMMENT = new RegularLexToken("LINE_COMMENT",
            Pattern.compile("//.*(?=" + Patterns.EXP_CRLF + ")"), COMMENT);

    /**
     * <p>
     * Space token. See {@linkplain Patterns#SPACE}.
     * </p>
     * 
     * @since 0.0.0
     */
    public static final RegularLexToken SPACE = new RegularLexToken("SPACE", Patterns.SPACE);

    /**
     * <p>
     * Linefeed token. See {@linkplain Patterns#CRLF}.
     * </p>
     * 
     * @since 0.0.0
     */
    public static final RegularLexToken CRLF = new RegularLexToken("CRLF", Patterns.CRLF);
}
