package com.cogician.quicker.util.calculator;

import java.util.List;

import com.cogician.quicker.QuickParsingException;
import com.cogician.quicker.util.lexer.QuickLexTetrad;

/**
 * <p>
 * Tokenizer for calculator to tokenize input arithmetic expression.
 * </p>
 * 
 * @param <T>
 *            type of token
 * @author Fred Suvn
 * @version 0.0.0, 2016-07-30T14:28:56+08:00
 * @since 0.0.0, 2016-07-30T14:28:56+08:00
 */
interface Tokenizer<T> {

    /**
     * <p>
     * Tokenizes given input string into a list of lexical tetrad which includes lexeme, token, row and column number.
     * </p>
     * 
     * @param input
     *            given input string
     * @return given input string into a list of lexical tetrad which includes lexeme, token, row and column number
     * @throws NullPointerException
     *             if given input string is null
     * @throws QuickParsingException
     *             if tokenizing failed
     * @since 0.0.0
     */
    List<QuickLexTetrad> tokenize(String input) throws NullPointerException, QuickParsingException;
}
