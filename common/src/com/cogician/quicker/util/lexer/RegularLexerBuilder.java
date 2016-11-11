package com.cogician.quicker.util.lexer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cogician.quicker.Buildable;
import com.cogician.quicker.Checker;
import com.cogician.quicker.Quicker;
import com.cogician.quicker.util.QuickCollections;
import com.cogician.quicker.util.ReferencedCharSequence;
import com.cogician.quicker.util.tostring.QuickToStrings;

/**
 * <p>
 * Regular builder of {@linkplain QuickLexer}. This builder will build a type of lexical analyzer which use regular
 * expression.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-05-06T11:27:37+08:00
 * @since 0.0.0, 2016-05-06T11:27:37+08:00
 * @see QuickLexer
 */
public class RegularLexerBuilder implements Buildable<QuickLexer> {

    /*
     * <p> For this lexer, each token is a {@linkplain RegularLexToken}. Parsers built by this builder have two ways to
     * parse: one is using a whole pattern combines all patterns of given token in adding order, each in a group with a
     * group name, and when it starts to tokenize, it will try to match by the whole pattern and find which token
     * matched by group name; the other is using each pattern of tokens. The prefix of group name should be set to a
     * unique value to avoid existing same group names in given token's pattern. Default the prefix is {@linkplain
     * #GROUP_PREFIX}. If set group prefix to null, or use {@linkplain #buildEach()}, it will build lexer which use each
     * token to match. </p>
     */

    // /**
    // * <p>
    // * Default prefix name of capture group: {@value #GROUP_PREFIX}.
    // * </p>
    // *
    // * @since 0.0.0
    // */
    // public static final String GROUP_PREFIX = "groupPasswordIs94808";

    private final List<RegularLexToken> tokens = new ArrayList<>();

    // private String groupPrefix = GROUP_PREFIX;

    /**
     * <p>
     * Constructs an empty instance.
     * </p>
     * 
     * @since 0.0.0
     */
    public RegularLexerBuilder() {

    }

    /**
     * <p>
     * Adds given tokens in index order and returns this instance.
     * </p>
     * 
     * @param tokens
     *            given tokens
     * @return this
     * @throws NullPointerException
     *             if given token array is null
     * @since 0.0.0
     */
    public RegularLexerBuilder addTokens(RegularLexToken... tokens) throws NullPointerException {
        Quicker.each(tokens, token -> {
            this.tokens.add(token);
        });
        return this;
    }

    /**
     * <p>
     * Adds given tokens in its order and returns this instance. Each token of Given tokens should non-contained in the
     * added tokens, and its pattern should be existing, or it will not be added.
     * </p>
     * 
     * @param tokens
     *            given tokens
     * @return this
     * @throws NullPointerException
     *             if given token list is null
     * @since 0.0.0
     */
    public RegularLexerBuilder addTokens(Iterable<RegularLexToken> tokens) throws NullPointerException {
        Quicker.each(tokens, token -> {
            this.tokens.add(token);
        });
        return this;
    }

    /**
     * <p>
     * Inserts given tokens at specified index in index order and returns this instance.
     * </p>
     * 
     * @param index
     *            specified index in bounds
     * @param tokens
     *            given tokens
     * @return this
     * @throws NullPointerException
     *             if given tokens is null
     * @throws IllegalArgumentException
     *             if index out of bounds
     * @since 0.0.0
     */
    public RegularLexerBuilder insertTokens(int index, RegularLexToken... tokens)
            throws NullPointerException, IllegalArgumentException {
        QuickCollections.addAll(this.tokens, tokens);
        return this;
    }

    /**
     * <p>
     * Inserts given tokens at specified index in index order and returns this instance. Shifts tokens currently at
     * specified index and subsequence after it to right. Each token of Given tokens should non-contained in the added
     * tokens, and its pattern should be existing, or it will not be added.
     * </p>
     * 
     * @param index
     *            specified index in bounds
     * @param tokens
     *            given tokens
     * @return this
     * @throws NullPointerException
     *             if given tokens is null
     * @throws IllegalArgumentException
     *             if index out of bounds
     * @since 0.0.0
     */
    public RegularLexerBuilder insertTokens(int index, Iterable<RegularLexToken> tokens)
            throws NullPointerException, IllegalArgumentException {
        QuickCollections.addAll(this.tokens, tokens);
        return this;
    }

    /**
     * <p>
     * Removes first occurrence of given token. If there is no given token in this builder, do nothing.
     * </p>
     * 
     * @param token
     *            given token
     * @return this
     * @throws NullPointerException
     *             if given token is null
     * @since 0.0.0
     */
    public RegularLexerBuilder removeToken(RegularLexToken token) throws NullPointerException {
        tokens.remove(token);
        return this;
    }

    // /**
    // * <p>
    // * Sets prefix name of each capture group. If set null, the built lexer
    // will
    // * parse by way using each token to match.
    // * </p>
    // *
    // * @param prefix
    // * prefix name of each capture group
    // * @return this
    // * @since 0.0.0
    // */
    // public RegularLexerBuilder setGroupPrefixName(@Nullable String prefix) {
    // this.groupPrefix = prefix;
    // return this;
    // }

    /**
     * <p>
     * Builds a {@linkplain QuickLexer} according to current build status.
     * </p>
     * 
     * @return a {@linkplain QuickLexer} according to current build status
     * @since 0.0.0
     */
    @Override
    public QuickLexer build() {
        // return groupPrefix == null ? new EachPatternRegularLexer(tokens)
        // : new EntirePatternRegularLexer(tokens, groupPrefix);
        return buildEach();
    }

    /**
     * <p>
     * Builds a {@linkplain QuickLexer} according to current build status, with parsing by way using each token to
     * match.
     * </p>
     * 
     * @return a {@linkplain QuickLexer} according to current build status
     * @since 0.0.0
     */
    private QuickLexer buildEach() {
        return new EachPatternRegularLexer(tokens);
    }

    private static abstract class AbstractRegularLexer implements QuickLexer {

        protected final List<RegularLexToken> regularTokens;

        protected AbstractRegularLexer(List<RegularLexToken> regularTokens) {
            this.regularTokens = new LinkedList<>();
            regularTokens.forEach(token -> {
                if (token.getPattern() != null) {
                    this.regularTokens.add(token);
                }
            });
        }

        protected void nextRowCol(int[] rowCol, String matched) {
            boolean isR = false;
            for (int i = 0; i < matched.length(); i++) {
                char ch = matched.charAt(i);
                if (ch == '\r') {
                    isR = true;
                    rowCol[0]++;
                    rowCol[1] = 1;
                } else if (ch == '\n') {
                    if (!isR) {
                        rowCol[0]++;
                        rowCol[1] = 1;
                    } else {
                        isR = false;
                    }
                } else {
                    rowCol[1]++;
                    isR = false;
                }
            }
        }
    }

    /**
     * <p>
     * Implementation with entire pattern.
     * </p>
     *
     * @author Fred Suvn
     * @version 0.0.0, 2016-11-01T09:22:24+08:00
     * @since 0.0.0, 2016-11-01T09:22:24+08:00
     * @deprecated I don't know why it runs slowly!
     */
    @Deprecated
    @SuppressWarnings("unused")
    private static class EntirePatternRegularLexer extends AbstractRegularLexer {

        private final String groupPrefix;

        private EntirePatternRegularLexer(List<RegularLexToken> regularTokens, String groupPrefix) {
            super(regularTokens);
            this.groupPrefix = groupPrefix;
        }

        private Pattern createPattern() {
            int[] counter = {0};
            String entireRegex = QuickToStrings.join(Quicker.flow(regularTokens).map(token -> {
                StringBuilder sb = new StringBuilder();
                sb.append("(?<");
                sb.append(groupPrefix).append(counter[0]++);
                sb.append(">");
                sb.append(token.getPattern());
                sb.append(")");
                return sb.toString();
            }).toIterable(), "|");

            return Pattern.compile(entireRegex);
        }

        @Override
        public List<LexTetrad> tokenize(String input) throws NullPointerException, ParsingException {
            Checker.checkNull(input);
            Pattern pattern = createPattern();
            List<LexTetrad> list = new ArrayList<>();
            CharSequence inputSequence = new ReferencedCharSequence(input);
            int cur = 0;
            int[] rowCol = {1, 1};
            while (true) {
                CharSequence toBeMatched = inputSequence.subSequence(cur, input.length());
                Matcher m = pattern.matcher(toBeMatched);
                if (m.find() && m.start() == 0) {
                    int e = m.end();
                    String matched = toBeMatched.subSequence(0, e).toString();
                    for (int i = 0; i < regularTokens.size(); i++) {
                        if (m.group(groupPrefix + i) != null) {
                            LexToken token = regularTokens.get(i);
                            list.add(new LexTetrad(matched, token, rowCol[0], rowCol[1]));
                            nextRowCol(rowCol, matched);
                            break;
                        }
                    }
                    cur += e;
                    if (cur == input.length()) {
                        break;
                    }
                } else {
                    throw new ParsingException(
                            "Tokenizing failed, Illegal characters: \"" + Quicker.ellipsis(toBeMatched.toString())
                                    + "\" at line " + rowCol[0] + ", column " + rowCol[1] + ".");
                }
            }
            return list;
        }

    }

    /**
     * <p>
     * Implementation with matching for each pattern.
     * </p>
     *
     * @author Fred Suvn
     * @version 0.0.0, 2016-11-01T10:03:44+08:00
     * @since 0.0.0, 2016-11-01T10:03:44+08:00
     */
    private static class EachPatternRegularLexer extends AbstractRegularLexer {

        private EachPatternRegularLexer(List<RegularLexToken> regularTokens) {
            super(regularTokens);
        }

        @Override
        public List<LexTetrad> tokenize(String input) throws NullPointerException, ParsingException {
            Checker.checkNull(input);
            List<LexTetrad> list = new ArrayList<>();
            CharSequence inputSequence = new ReferencedCharSequence(input);
            int cur = 0;
            int[] rowCol = {1, 1};
            while (true) {
                CharSequence toBeMatched = inputSequence.subSequence(cur, input.length());
                boolean isFound = false;
                for (int i = 0; i < regularTokens.size(); i++) {
                    RegularLexToken token = regularTokens.get(i);
                    Matcher m = token.getPattern().matcher(toBeMatched);
                    if (m.lookingAt() && m.start() == 0 && m.end() > 0) {
                        isFound = true;
                        int e = m.end();
                        String matched = toBeMatched.subSequence(0, e).toString();
                        list.add(new LexTetrad(matched, token, rowCol[0], rowCol[1]));
                        nextRowCol(rowCol, matched);
                        cur += e;
                        break;
                    }
                }
                if (!isFound) {
                    throw new ParsingException(
                            "Tokenizing failed, Illegal characters: \"" + Quicker.ellipsis(toBeMatched.toString())
                                    + "\" at line " + rowCol[0] + ", column " + rowCol[1] + ".");
                }
                if (cur >= input.length()) {
                    break;
                }
                isFound = false;
            }
            return list;
        }

    }
}
