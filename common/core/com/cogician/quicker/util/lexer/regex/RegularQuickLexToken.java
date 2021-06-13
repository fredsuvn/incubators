package com.cogician.quicker.util.lexer.regex;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.cogician.quicker.util.Patterns;
import com.cogician.quicker.util.lexer.QuickLexToken;

/**
 * <p>
 * A type of {@linkplain QuickLexToken} which use regular expression.
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
 * @see QuickLexToken
 */
@Immutable
public class RegularQuickLexToken extends QuickLexToken {

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
    public RegularQuickLexToken(String name) {
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
    public RegularQuickLexToken(String name, @Nullable String regex) throws PatternSyntaxException {
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
    public RegularQuickLexToken(String name, @Nullable Pattern pattern) {
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
    public RegularQuickLexToken(String name, @Nullable String regex, @Nullable RegularQuickLexToken superToken)
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
    public RegularQuickLexToken(String name, Pattern pattern, @Nullable RegularQuickLexToken superToken)
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
    public static final RegularQuickLexToken IDENTIFIER = new RegularQuickLexToken("IDENTIFIER", Patterns.IDENTIFIER);

    /**
     * <p>
     * Common number token. See {@linkplain Patterns#NUMBER}.
     * </p>
     * 
     * @since 0.0.0
     */
    public static final RegularQuickLexToken NUMBER = new RegularQuickLexToken("NUMBER", Patterns.NUMBER);

    /**
     * <p>
     * Number of scientific notation token, sub-token of {@linkplain #NUMBER}. See
     * {@linkplain Patterns#SCIENTIFIC_NOTATION}.
     * </p>
     * 
     * @since 0.0.0
     */
    public static final RegularQuickLexToken SCIENTIFIC_NOTATION = new RegularQuickLexToken("NUMBER",
            Patterns.SCIENTIFIC_NOTATION, NUMBER);

    /**
     * <p>
     * Binary number token, sub-token of {@linkplain #NUMBER}. See {@linkplain Patterns#BINARY_NUMBER}.
     * </p>
     * 
     * @since 0.0.0
     */
    public static final RegularQuickLexToken BIN_NUMBER = new RegularQuickLexToken("BIN_NUMBER", Patterns.BIN_NUMBER, NUMBER);

    /**
     * <p>
     * Hex number token, sub-token of {@linkplain #NUMBER}. See {@linkplain Patterns#HEX_NUMBER}.
     * </p>
     * 
     * @since 0.0.0
     */
    public static final RegularQuickLexToken HEX_NUMBER = new RegularQuickLexToken("HEX_NUMBER", Patterns.HEX_NUMBER, NUMBER);

    /**
     * <p>
     * Octal number token, sub-token of {@linkplain #NUMBER}. See {@linkplain Patterns#OCTAL_NUMBER}.
     * </p>
     * 
     * @since 0.0.0
     */
    public static final RegularQuickLexToken OCTAL_NUMBER = new RegularQuickLexToken("OCTAL_NUMBER", Patterns.OCTAL_NUMBER,
            NUMBER);

    /**
     * <p>
     * Operator token. See {@linkplain Patterns#OPERATOR}.
     * </p>
     * 
     * @since 0.0.0
     */
    public static final RegularQuickLexToken OPERATOR = new RegularQuickLexToken("OPERATOR", Patterns.OPERATOR);

    /**
     * <p>
     * Super parenthesis token. This token doesn't represent any string, only being the super token of left and right
     * parenthesis.
     * </p>
     * 
     * @since 0.0.0
     */
    public static final RegularQuickLexToken DELIMITER = new RegularQuickLexToken("DELIMITER");

    /**
     * <p>
     * Left bracket token: "(".
     * </p>
     * 
     * @since 0.0.0
     */
    public static final RegularQuickLexToken LEFT_BRACKET = new RegularQuickLexToken("LEFT_BRACKET", "\\(", DELIMITER);

    /**
     * <p>
     * Right bracket token: ")".
     * </p>
     * 
     * @since 0.0.0
     */
    public static final RegularQuickLexToken RIGHT_BRACKET = new RegularQuickLexToken("RIGHT_BRACKET", "\\)", DELIMITER);

    /**
     * <p>
     * Comma token: ",".
     * </p>
     * 
     * @since 0.0.0
     */
    public static final RegularQuickLexToken COMMA = new RegularQuickLexToken("COMMA", Pattern.compile(","), DELIMITER);

    /**
     * <p>
     * Semicolon token: ";".
     * </p>
     * 
     * @since 0.0.0
     */
    public static final RegularQuickLexToken SEMICOLON = new RegularQuickLexToken("SEMICOLON", Pattern.compile(";"), DELIMITER);

    /**
     * <p>
     * Macro token: "#DEFINE", case insenstive.
     * </p>
     * 
     * @since 0.0.0
     */
    public static final RegularQuickLexToken MACRO = new RegularQuickLexToken("MACRO",
            Pattern.compile("#[dD][eE][fF][iI][nN][eE]"));

    /**
     * <p>
     * Super comment token. This token doesn't represent any string, only being the super token of other comments.
     * </p>
     * 
     * @since 0.0.0
     */
    public static final RegularQuickLexToken COMMENT = new RegularQuickLexToken("COMMENT");

    /**
     * <p>
     * Block comment token: "{@code /* [comment] *}{@code /}"
     * </p>
     * 
     * @since 0.0.0
     */
    public static final RegularQuickLexToken BLOCK_COMMENT = new RegularQuickLexToken("BLOCK_COMMENT",
            Pattern.compile("/\\*[^/]*\\*/"), COMMENT);

    /**
     * <p>
     * Line comment token: "{@code // [comment] }"
     * </p>
     * 
     * @since 0.0.0
     */
    public static final RegularQuickLexToken LINE_COMMENT = new RegularQuickLexToken("LINE_COMMENT",
            Pattern.compile("//.*(?=" + Patterns.EXP_CRLF + ")"), COMMENT);

    /**
     * <p>
     * Space token. See {@linkplain Patterns#SPACE}.
     * </p>
     * 
     * @since 0.0.0
     */
    public static final RegularQuickLexToken SPACE = new RegularQuickLexToken("SPACE", Patterns.SPACE);

    /**
     * <p>
     * Linefeed token. See {@linkplain Patterns#CRLF}.
     * </p>
     * 
     * @since 0.0.0
     */
    public static final RegularQuickLexToken CRLF = new RegularQuickLexToken("CRLF", Patterns.CRLF);
}
