package com.cogician.quicker.expression;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.IntPredicate;

import javax.annotation.Nullable;
import javax.xml.transform.Result;

import com.cogician.quicker.Buildable;
import com.cogician.quicker.Checker;
import com.cogician.quicker.QuickParsingException;
import com.cogician.quicker.Quicker;
import com.cogician.quicker.struct.QuickTreeNode;
import com.cogician.quicker.util.Consts;
import com.cogician.quicker.util.QuickRefCharSequence;
import com.cogician.quicker.util.lexer.QuickLexTetrad;
import com.cogician.quicker.util.lexer.QuickLexToken;
import com.cogician.quicker.util.lexer.QuickLexer;
import com.cogician.quicker.util.lexer.QuickLexers;

/**
 * <p>
 * Default builder of {@linkplain QuickExpressionParser}. Placeholder resolver built by this builder is default
 * resolver.
 * </p>
 * <p>
 * This builder support two-tokens and single token placeholder. If suffix of placeholder is set to null, placeholder
 * will be considered as single token placeholder. Single placeholder doesn't support nesting resolving.
 * </p>
 * <p>
 * For delimiters, delimiters should be set in pairs. Any one of delimiter is set to null, both will be set to null if
 * {@linkplain #build()} called.
 * </p>
 * <p>
 * There are some policy for if specified placeholder is not found. See {@linkplain NotFoundPolicy}. Default is
 * {@linkplain NotFoundPolicy#IGNORE}.
 * </p>
 * <p>
 * Initial settings of a new builder are place prefix "${", place suffix "}", delimiter prefix "<[", delimiter suffix
 * "]>", determine "?", escape "\". Any two tokens setting of all cannot be equal except null.
 * </p>
 * <p>
 * Note a legal param name only consists of numbers and letters.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-08-03T15:48:56+08:00
 * @since 0.0.0, 2016-08-03T15:48:56+08:00
 * @see QuickExpressionParser
 * @see NotFoundPolicy
 */
public class QuickExpressionParserBuilder implements Buildable<QuickExpressionParser> {

    static final QuickExpressionParserBuilder DEFAULT_RESOLVER_BUILDER = new QuickExpressionParserBuilder()
            .setPlacePrefix("${").setPlaceSuffix("}").setRegionPrefix("<[").setRegionSuffix("]>").setDetermine("?")
            .setEscape("\\");

    static final QuickExpressionParser DEFAULT_RESOLVER = DEFAULT_RESOLVER_BUILDER.build();

    static final QuickExpressionParserBuilder DEFAULT_LOG_RESOLVER_BUILDER = new QuickExpressionParserBuilder()
            .setPlacePrefix("%").setPlaceSuffix(null).setRegionPrefix("<[").setRegionSuffix("]>").setDetermine("?")
            .setEscape("\\");

    static final QuickExpressionParser DEFAULT_LOG_RESOLVER = DEFAULT_LOG_RESOLVER_BUILDER.build();

    static final QuickExpressionParserBuilder DEFAULT_SQL_RESOLVER_BUILDER = new QuickExpressionParserBuilder()
            .setPlacePrefix(":").setPlaceSuffix(null).setRegionPrefix("<[").setRegionSuffix("]>").setDetermine("?")
            .setEscape("\\");

    static final QuickExpressionParser DEFAULT_SQL_RESOLVER = DEFAULT_SQL_RESOLVER_BUILDER.build();

    private String placePrefix = "${";

    private @Nullable String placeSuffix = "}";

    private @Nullable String regionPrefix = "<[";

    private @Nullable String regionSuffix = "]>";

    private @Nullable String determine = "?";

    private @Nullable String escape = "\\";

    private @Nullable Map<String, ?> argumentsMap;

    /**
     * <p>
     * Constructs an empty builder.
     * </p>
     * 
     * @since 0.0.0
     */
    public QuickExpressionParserBuilder() {

    }

    /**
     * <p>
     * Sets placeholder prefix.
     * </p>
     * 
     * @param placePrefix
     *            specified placeholder prefix
     * @return this
     * @throws NullPointerException
     *             if specified placeholder prefix is null
     * @since 0.0.0
     */
    public QuickExpressionParserBuilder setPlacePrefix(String placePrefix) throws NullPointerException {
        this.placePrefix = Quicker.require(placePrefix);
        return this;
    }

    /**
     * <p>
     * Sets placeholder suffix.
     * </p>
     * 
     * @param placeSuffix
     *            specified placeholder suffix
     * @return this
     * @since 0.0.0
     */
    public QuickExpressionParserBuilder setPlaceSuffix(@Nullable String placeSuffix) {
        this.placeSuffix = placeSuffix;
        return this;
    }

    /**
     * <p>
     * Sets region prefix delimiter.
     * </p>
     * 
     * @param regionPrefix
     *            specified prefix
     * @return this
     * @since 0.0.0
     */
    public QuickExpressionParserBuilder setRegionPrefix(@Nullable String regionPrefix) {
        this.regionPrefix = regionPrefix;
        return this;
    }

    /**
     * <p>
     * Sets region suffix delimiter.
     * </p>
     * 
     * @param regionSuffix
     *            specified suffix
     * @return this
     * @since 0.0.0
     */
    public QuickExpressionParserBuilder setRegionSuffix(@Nullable String regionSuffix) {
        this.regionSuffix = regionSuffix;
        return this;
    }

    /**
     * <p>
     * Sets determine.
     * </p>
     * 
     * @param determine
     *            specified determine
     * @return this
     * @since 0.0.0
     */
    public QuickExpressionParserBuilder setDetermine(@Nullable String determine) {
        this.determine = determine;
        return this;
    }

    /**
     * <p>
     * Sets escape.
     * </p>
     * 
     * @param escape
     *            specified escape
     * @return this
     * @since 0.0.0
     */
    public QuickExpressionParserBuilder setEscape(@Nullable String escape) {
        this.escape = escape;
        return this;
    }
    
    /**
     * <p>
     * Sets value map.
     * </p>
     * 
     * @param argumentMap
     *            specified value map
     * @return this
     * @since 0.0.0
     */
    public QuickExpressionParserBuilder setArgumentMap(@Nullable Map<String, ? extends Object> argumentMap) {
        this.argumentsMap = argumentMap;
        return this;
    }

    /**
     * <p>
     * Builds a {@linkplain QuickExpressionParser} with current setting.
     * </p>
     * 
     * @return a {@linkplain QuickExpressionParser} with current setting
     * @throws IllegalArgumentException
     *             if any two setting are equal except null
     * @since 0.0.0
     */
    @Override
    public QuickExpressionParser build() throws IllegalArgumentException {
        // return new DefaultPlaceholderResolver(placePrefix, placeSuffix, regionPrefix, regionSuffix, determine,
        // escape,
        // notFoundPolicy, valueMap);
        return new DefaultExpressionParser(this);
    }

    private static class DefaultExpressionParser implements QuickExpressionParser {

        private final String PLC_PRE;
        private final String PLC_SUF;
        private final String RGN_PRE;
        private final String RGN_SUF;
        private final String DET;
        private final String ESC;

        private final Map<String, ? extends Object> arguments;

        private final QuickTreeNode<Object> tree;

        DefaultExpressionParser(QuickExpressionParserBuilder builder) {
            this.PLC_PRE = builder.placePrefix;
            this.PLC_SUF = builder.placeSuffix;
            this.RGN_PRE = builder.regionPrefix;
            this.RGN_SUF = builder.regionSuffix;
            this.DET = builder.determine;
            this.ESC = builder.escape;
            this.arguments = builder.argumentsMap;
            this.tree = createrReservedCharTree();
        }

        private QuickTreeNode<Object> createrReservedCharTree() {
            QuickTreeNode<Object> root = new QuickTreeNode<>();
            List<String> list = new ArrayList<>();
            if (Checker.isNotEmpty(PLC_PRE)) {
                list.add(PLC_PRE);
            }
            if (Checker.isNotEmpty(PLC_SUF)) {
                list.add(PLC_SUF);
            }
            if (Checker.isNotEmpty(RGN_PRE)) {
                list.add(RGN_PRE);
            }
            if (Checker.isNotEmpty(RGN_SUF)) {
                list.add(RGN_SUF);
            }
            if (Checker.isNotEmpty(DET)) {
                list.add(DET);
            }
            if (Checker.isNotEmpty(ESC)) {
                list.add(ESC);
            }
            for (int i = 0; i < list.size(); i++) {
                String cur = list.get(i);
                createCharTree(root, cur);
            }
            return root;
        }

        private void createCharTree(QuickTreeNode<Object> root, String str) {
            char[] cs = str.toCharArray();
            QuickTreeNode<Object> cur = root;
            for (int i = 0; i < cs.length; i++) {
                char c = cs[i];
                List<Object> values = cur.childrenValues();
                if (values.contains(c)) {
                    cur = cur.children().get(values.indexOf(c));
                } else {
                    values.add(c);
                    cur = cur.children().get(values.size() - 1);
                }
            }
            cur.childrenValues().add(str);
        }

        private static final String ERROR_UNKNOWN = "Unknown error";
        private static final String ERROR_UNMATCHED = "Unmatched placeholder or region symbol";
        private static final String ERROR_UNEXPECTED = "Unexpected symbol";
        private static final String ERROR_NOARGS = "Specified arguent was not found";

        private QuickExpression resolve0(Bag args) {
            while (args.offset < args.input.length()) {
                String token = nextToken(args, args.finalString);
                if (token == PLC_PRE) {
                    int offset = args.offset;
                    args.offset++;
                    Entry<String, Object> kv = buildPlaceholder(args);
                    //Object value = checkPolicy(kv.getKey(), kv.getValue(), args.input, offset);
                    args.finalString.append(value);
                    if (args.usedValues != null) {
                        args.usedValues.add(kv);
                    }
                } else if (token == PLC_SUF) {
                    throw new QuickParsingException(
                            parseErrorInfo(args.input, args.offset - token.length(), ERROR_UNMATCHED));
                } else if (token == RGN_PRE) {
                    args.offset++;
                    buildRegion(args);
                } else if (token == RGN_SUF) {
                    throw new QuickParsingException(
                            parseErrorInfo(args.input, args.offset - token.length(), ERROR_UNMATCHED));
                } else if (token == DET) {
                    throw new QuickParsingException(
                            parseErrorInfo(args.input, args.offset - token.length(), ERROR_UNEXPECTED));
                } else if (token == ESC) {
                    args.offset++;
                    if (args.offset < args.input.length()) {
                        args.finalString.append(args.input.charAt(args.offset));
                        args.offset++;
                    } else {
                        throw new QuickParsingException(
                                parseErrorInfo(args.input, args.offset - token.length(), ERROR_UNEXPECTED));
                    }
                } else {
                    throw new QuickParsingException(
                            parseErrorInfo(args.input, args.offset - token.length(), ERROR_UNKNOWN));
                }
            }
            return null;
        }

        private final @Nullable String nextToken(Bag args, StringBuilder output) {
            QuickTreeNode<Object> cur = this.tree;
            StringBuilder token = new StringBuilder();
            while (args.offset < args.input.length()) {
                char c = args.input.charAt(args.offset);
                List<Object> values = cur.childrenValues();
                if (cur.childrenValues().contains(c)) {
                    token.append(c);
                    QuickTreeNode<Object> node = cur.children().get(values.indexOf(c));
                    List<Object> childValues = node.childrenValues();
                    if (childValues.size() == 1 && childValues.get(0) instanceof String) {
                        args.offset++;
                        return (String)childValues.get(0);
                    } else {
                        cur = node;
                    }
                } else {
                    output.append(token);
                    token.setLength(0);
                    output.append(c);
                }
                args.offset++;
            }
            return null;
        }

        private Entry<String, Object> buildPlaceholder(Bag args) {
            StringBuilder key = new StringBuilder();
            while (args.offset < args.input.length()) {
                String token = nextToken(args, key);
                if (token == PLC_PRE) {
                    int offset = args.offset;
                    args.offset++;
                    args.usedValuesCount++;
                    Entry<String, Object> kv = buildPlaceholder(args);
                    Object value = checkPolicy(kv.getKey(), kv.getValue(), args.input, offset);
                    key.append(value);
                    if (args.usedValues != null) {
                        args.usedValues.add(kv);
                    }
                } else if (token == PLC_SUF) {
                    String k = key.toString();
                    args.offset++;
                    return new PlaceholderEntr(k, args.valueGetter.apply(k));
                } else if (token == RGN_PRE) {
                    args.offset++;
                    buildRegion(args);
                } else if (token == RGN_SUF) {
                    throw new QuickParsingException(parseErrorInfo(args.input, args.offset, ERROR_UNMATCHED));
                } else if (token == DET) {
                    throw new QuickParsingException(parseErrorInfo(args.input, args.offset, ERROR_UNEXPECTED));
                } else if (token == ESC) {
                    args.offset++;
                    if (args.offset < args.input.length()) {
                        key.append(args.input.charAt(args.offset));
                        args.offset++;
                    } else {
                        throw new QuickParsingException(parseErrorInfo(args.input, args.offset, ERROR_UNEXPECTED));
                    }
                } else {
                    throw new QuickParsingException(parseErrorInfo(args.input, args.offset, ERROR_UNKNOWN));
                }
            }

            throw new QuickParsingException(parseErrorInfo(args.input, args.offset, ERROR_UNMATCHED));
        }

        private void buildRegion(Bag args) {

            StringBuilder sb = new StringBuilder();
            StringBuilder token = new StringBuilder();
            QuickTreeNode<Object> cur = this.tree;
            Object lastValue = null;
            boolean ddd = false;

            while (args.offset < args.input.length()) {
                char c = args.input.charAt(args.offset);
                List<Object> values = cur.childrenValues();
                if (values.contains(c)) {
                    token.append(c);
                    cur = cur.children().get(values.indexOf(c));
                    args.offset++;
                } else {
                    if (values.size() == 1 && values.get(0) instanceof String) {
                        String reserved = (String)values.get(0);
                        if (reserved == PLC_PRE) {
                            args.offset++;
                            Entry<String, Object> kv = buildPlaceholder(args);
                            Object value = kv.getValue();
                            if (value == NO_FOUND) {
                                skipRegion(args);
                            } else {
                                sb.append(value);
                                if (args.usedValues != null) {
                                    args.usedValues.add(kv);
                                }
                            }
                            lastValue = value;
                        } else if (reserved == PLC_SUF) {
                            throw new QuickParsingException(parseErrorInfo(args.input, args.offset, ERROR_UNMATCHED));
                        } else if (reserved == RGN_PRE) {
                            args.offset++;
                            buildRegion(args);
                            lastValue = null;
                        } else if (reserved == RGN_SUF) {
                            args.finalString.append(sb);
                            args.offset++;
                            return;
                        } else if (reserved == DET) {
                            if (lastValue == NO_FOUND) {
                                args.offset++;
                                skipRegion(args);
                            } else if (lastValue != null) {
                                args.offset++;

                            } else {
                                throw new QuickParsingException(
                                        parseErrorInfo(args.input, args.offset, ERROR_UNEXPECTED));
                            }
                        } else if (reserved == ESC) {
                            args.offset++;
                            if (args.offset < args.input.length()) {
                                sb.append(args.input.charAt(args.offset));
                                args.offset++;
                            } else {
                                throw new QuickParsingException(
                                        parseErrorInfo(args.input, args.offset, ERROR_UNEXPECTED));
                            }
                            lastValue = null;
                        } else {
                            throw new QuickParsingException(parseErrorInfo(args.input, args.offset, ERROR_UNKNOWN));
                        }
                        token.setLength(0);
                    } else {
                        sb.append(token);
                        token.setLength(0);
                        sb.append(c);
                        cur = this.tree;
                        args.offset++;
                        lastValue = null;
                    }
                }
            }
            throw new QuickParsingException(parseErrorInfo(args.input, args.offset, ERROR_UNMATCHED));
        }

        private void skipRegion(Bag args) {
            // StringBuilder key = new StringBuilder();
            // StringBuilder token = new StringBuilder();
            // QuickTreeNode<Object> cur = this.tree;
            // while (args.offset < args.input.length()) {
            // char c = args.input.charAt(args.offset);
            // List<Object> values = cur.childrenValues();
            // if (values.contains(c)) {
            // token.append(c);
            // cur = cur.children().get(values.indexOf(c));
            // args.offset++;
            // } else {
            // if (values.size() == 1 && values.get(0) instanceof String) {
            // String reserved = (String)values.get(0);
            // if (reserved == PLC_PRE){
            // args.usedValuesCount++;
            // Entry<String, Object> kv = buildPlaceholder(args);
            // int offset = args.offset;
            // Object value = checkPolicy(kv.getKey(), kv.getValue(), args.input, offset);
            // key.append(value);
            // if (args.usedValues != null){
            // args.usedValues.add(kv);
            // }
            // } else if(reserved == PLC_SUF){
            // String k = key.toString();
            // args.offset++;
            // return new PlaceholderEntr(k, args.valueGetter.apply(k));
            // }else if(reserved == RGN_PRE){
            // buildRegion(args);
            // }else if(reserved == RGN_SUF){
            // throw new QuickParsingException(parseErrorInfo(args.input, args.offset, ERROR_UNMATCHED));
            // }else if(reserved == DET){
            // throw new QuickParsingException(parseErrorInfo(args.input, args.offset, ERROR_UNEXPECTED));
            // }else if(reserved == ESC){
            // args.offset++;
            // if (args.offset < args.input.length()){
            // key.append(args.input.charAt(args.offset));
            // args.offset++;
            // } else {
            // throw new QuickParsingException(parseErrorInfo(args.input, args.offset, ERROR_UNEXPECTED));
            // }
            // }else {
            // throw new QuickParsingException(parseErrorInfo(args.input, args.offset, ERROR_UNKNOWN));
            // }
            // token.setLength(0);
            // } else {
            // key.append(token);
            // token.setLength(0);
            // key.append(c);
            // cur = this.tree;
            // args.offset++;
            // }
            // }
            // }
            throw new QuickParsingException(parseErrorInfo(args.input, args.offset, ERROR_UNMATCHED));
        }

        private String parseErrorInfo(String input, int offset, String msg) {
            QuickRefCharSequence chars = new QuickRefCharSequence(input, 0, offset + 1);
            int[] rowCol = QuickLexers.getLastRowCol(chars);
            return "Parse " + Quicker.ellipsis(chars) + " error: " + msg + ". " + "Position at: " + offset + "(row: "
                    + rowCol[0] + ", column: " + rowCol[1] + ").";
        }

        private final Object NO_FOUND = new Object();

        class Bag {
            String input;
            int offset;
            StringBuilder finalString;
            List<Entry<String, Object>> usedValues;
            int usedValuesCount;
            Function<String, Object> valueGetter;

            Bag(String input, Function<String, Object> valueGetter, boolean needDetail) {
                this.input = input;
                this.offset = 0;
                this.finalString = new StringBuilder();
                this.usedValues = needDetail ? new ArrayList<>() : null;
                this.usedValuesCount = 0;
                this.valueGetter = valueGetter;
            }
        }

        class PlaceholderEntr implements Entry<String, Object> {

            private String key;
            private Object value;

            PlaceholderEntr(String key, Object value) {
                this.key = key;
                this.value = value;
            }

            @Override
            public String getKey() {
                return key;
            }

            @Override
            public Object getValue() {
                return value;
            }

            @Override
            public Object setValue(Object value) {
                throw new UnsupportedOperationException();
            }

        }
    }
}
