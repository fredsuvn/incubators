package com.cogician.quicker.util.calculator;

import java.util.List;

import com.cogician.quicker.util.lexer.ParsingException;

/**
 * <p>
 * Postfix expression parser for Calculator.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-08-02T21:01:55+08:00
 * @since 0.0.0, 2016-08-02T21:01:55+08:00
 */
interface PostfixParser {

    /**
     * <p>
     * Parses input arithmetic expression to postfix expression and returns.
     * </p>
     * 
     * @param input
     *            input arithmetic expression
     * @return postfix expression
     * @throws NullPointerException
     *             if given input string is null
     * @throws ParsingException
     *             if parsing failed
     * @since 0.0.0
     */
    List<Object> parse(String input) throws NullPointerException, ParsingException;
}
