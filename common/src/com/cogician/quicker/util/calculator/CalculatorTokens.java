package com.cogician.quicker.util.calculator;

import com.cogician.quicker.util.lexer.LexToken;
import com.cogician.quicker.util.lexer.RegularLexToken;

/**
 * <p>
 * Tokens of calculator.
 * </p>
 *
 * @param <T>
 *            type of token
 * @author Fred Suvn
 * @version 0.0.0, 2016-07-31T21:19:25+08:00
 * @since 0.0.0, 2016-07-31T21:19:25+08:00
 */
interface CalculatorTokens<T extends LexToken> {

    public T macro();

    public T space();

    public T crlf();

    public T identifier();

    public T delimiter();

    public T leftBracket();

    public T rightBracket();

    public T comma();

    public T semicolon();

    public T number();

    public T scientificNotationNumber();

    public T binaryNumber();

    public T octalNumber();

    public T hexNumber();

    public T comment();

    public T blockComment();

    public T lineComment();

    public T operator();

    public T addOperator();

    public T subOperator();

    public T mulOperator();

    public T divOperator();

    public T modOperator();

    public T shiftLeftOperator();

    public T logicalShiftRightOperator();

    public T ArithmeticShiftRightOperator();

    public T andOperator();

    public T orOperator();

    public T notOperator();

    public T xorOperator();

}

class RegularCalculatorTokens implements CalculatorTokens<RegularLexToken> {

    @Override
    public RegularLexToken macro() {
        return RegularLexToken.MACRO;
    }

    @Override
    public RegularLexToken space() {
        return RegularLexToken.SPACE;
    }

    @Override
    public RegularLexToken crlf() {
        return RegularLexToken.CRLF;
    }

    @Override
    public RegularLexToken identifier() {
        return RegularLexToken.IDENTIFIER;
    }

    @Override
    public RegularLexToken delimiter() {
        return RegularLexToken.DELIMITER;
    }

    @Override
    public RegularLexToken leftBracket() {
        return RegularLexToken.LEFT_BRACKET;
    }

    @Override
    public RegularLexToken rightBracket() {
        return RegularLexToken.RIGHT_BRACKET;
    }

    @Override
    public RegularLexToken comma() {
        return RegularLexToken.COMMA;
    }

    @Override
    public RegularLexToken semicolon() {
        return RegularLexToken.SEMICOLON;
    }

    @Override
    public RegularLexToken number() {
        return RegularLexToken.NUMBER;
    }

    @Override
    public RegularLexToken scientificNotationNumber() {
        return RegularLexToken.SCIENTIFIC_NOTATION;
    }

    @Override
    public RegularLexToken binaryNumber() {
        return RegularLexToken.BIN_NUMBER;
    }

    @Override
    public RegularLexToken octalNumber() {
        return RegularLexToken.OCTAL_NUMBER;
    }

    @Override
    public RegularLexToken hexNumber() {
        return RegularLexToken.HEX_NUMBER;
    }

    @Override
    public RegularLexToken comment() {
        return RegularLexToken.COMMENT;
    }

    @Override
    public RegularLexToken blockComment() {
        return RegularLexToken.BLOCK_COMMENT;
    }

    @Override
    public RegularLexToken lineComment() {
        return RegularLexToken.LINE_COMMENT;
    }

    private static final RegularLexToken OPERATOR = new RegularLexToken("OPERATOR",
            "[\\+\\-\\*\\/\\&\\|\\~\\^\\%]|\\<\\<|\\>\\>\\>|\\>\\>");;

    @Override
    public RegularLexToken operator() {
        return OPERATOR;
    }

    @Override
    public RegularLexToken addOperator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public RegularLexToken subOperator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public RegularLexToken mulOperator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public RegularLexToken divOperator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public RegularLexToken modOperator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public RegularLexToken shiftLeftOperator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public RegularLexToken logicalShiftRightOperator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public RegularLexToken ArithmeticShiftRightOperator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public RegularLexToken andOperator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public RegularLexToken orOperator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public RegularLexToken notOperator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public RegularLexToken xorOperator() {
        throw new UnsupportedOperationException();
    }
}
