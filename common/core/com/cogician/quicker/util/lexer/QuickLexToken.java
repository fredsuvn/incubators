package com.cogician.quicker.util.lexer;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.cogician.quicker.Checker;

/**
 * <p>
 * Token of lexical analysis.
 * </p>
 * <p>
 * Token is a type symbol. Each token represents a regular combination of characters to separate those characters from
 * input text. <b>A token only has one instance.</b> Creating a new instance will create a new type symbol at same time.
 * That is, using
 * 
 * <pre>
 * token0 == token1
 * </pre>
 * 
 * to check whether two tokens indicate one type is correct.
 * </p>
 * <p>
 * A token may has some sub-tokens. That means, some tokens maybe belong to a more general type.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-07-29T22:02:16+08:00
 * @since 0.0.0, 2016-07-29T22:02:16+08:00
 */
@Immutable
public class QuickLexToken {

    private final String name;

    @Nullable
    private final QuickLexToken superToken;

    /**
     * <p>
     * Constructs a new token with given name.
     * </p>
     * 
     * @param name
     *            given name
     * @throws NullPointerException
     *             if given name is null
     * @since 0.0.0
     */
    public QuickLexToken(String name) throws NullPointerException {
        this(name, null);
    }

    /**
     * <p>
     * Constructs a new token with given name and super-token.
     * </p>
     * 
     * @param name
     *            given name
     * @param superToken
     *            given super-token
     * @throws NullPointerException
     *             if given name is null
     * @since 0.0.0
     */
    public QuickLexToken(String name, @Nullable QuickLexToken superToken) throws NullPointerException {
        this.name = name;
        this.superToken = superToken;
    }

    /**
     * <p>
     * Returns name of this token.
     * </p>
     * 
     * @return name of this token
     * @since 0.0.0
     */
    public String getName() {
        return name;
    }

    /**
     * <p>
     * Returns super-token of this token.
     * </p>
     * 
     * @return super-token of this token
     * @since 0.0.0
     */
    public @Nullable QuickLexToken getSuperToken() {
        return superToken;
    }

    /**
     * <p>
     * Returns whether this token is sub-token of given super-token. If this token == given token, return true.
     * </p>
     * 
     * @param token
     *            given super-token
     * @return whether this type is sub-token of given super-token
     * @throws NullPointerException
     *             if given super-token is null
     * @since 0.0.0
     */
    public boolean isTokenOf(QuickLexToken token) throws NullPointerException {
        Checker.checkNull(token);
        QuickLexToken thisSuperTree = this;
        do {
            if (thisSuperTree == token) {
                return true;
            }
            thisSuperTree = thisSuperTree.getSuperToken();
        } while (thisSuperTree != null);
        return false;
    }

    /**
     * <p>
     * Returns a string simply represents this token (just name).
     * </p>
     * 
     * @return a string simply represents this token
     * @since 0.0.0
     */
    @Override
    public String toString() {
        return getName();
    }
}
