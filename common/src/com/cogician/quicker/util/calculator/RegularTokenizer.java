package com.cogician.quicker.util.calculator;

import java.util.List;

import javax.annotation.concurrent.ThreadSafe;

import com.cogician.quicker.util.lexer.LexTetrad;
import com.cogician.quicker.util.lexer.QuickLexer;
import com.cogician.quicker.util.lexer.ParsingException;
import com.cogician.quicker.util.lexer.RegularLexToken;
import com.cogician.quicker.util.lexer.RegularLexerBuilder;

/**
 * <p>
 * A {@linkplain Tokenizer} implemented by regular expression.
 * </p>
 * <p>
 * The element type of token list argument of {@linkplain #tokenize(String, List)} in this implementation shuold be
 * {@linkplain RegularLexToken}.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-07-30T14:36:26+08:00
 * @since 0.0.0, 2016-07-30T14:36:26+08:00
 */
@ThreadSafe
class RegularTokenizer implements Tokenizer<RegularLexToken> {

    private final QuickLexer lexer;

    RegularTokenizer(List<RegularLexToken> tokens) {
        lexer = new RegularLexerBuilder().addTokens(tokens).build();
    }

    @Override
    public List<LexTetrad> tokenize(String input) throws NullPointerException, ParsingException {
        return lexer.tokenize(input);
    }
}
