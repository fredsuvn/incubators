package com.cogician.quicker.util.calculator;

import java.util.List;

import javax.annotation.concurrent.ThreadSafe;

import com.cogician.quicker.QuickParsingException;
import com.cogician.quicker.util.lexer.QuickLexTetrad;
import com.cogician.quicker.util.lexer.QuickLexer;
import com.cogician.quicker.util.lexer.regex.RegularQuickLexToken;
import com.cogician.quicker.util.lexer.regex.RegularQuickLexerBuilder;

/**
 * <p>
 * A {@linkplain Tokenizer} implemented by regular expression.
 * </p>
 * <p>
 * The element type of token list argument of {@linkplain #tokenize(String, List)} in this implementation shuold be
 * {@linkplain RegularQuickLexToken}.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-07-30T14:36:26+08:00
 * @since 0.0.0, 2016-07-30T14:36:26+08:00
 */
@ThreadSafe
class RegularTokenizer implements Tokenizer<RegularQuickLexToken> {

    private final QuickLexer lexer;

    RegularTokenizer(List<RegularQuickLexToken> tokens) {
        lexer = new RegularQuickLexerBuilder().addTokens(tokens).build();
    }

    @Override
    public List<QuickLexTetrad> tokenize(String input) throws NullPointerException, QuickParsingException {
        return lexer.tokenize(input);
    }
}
