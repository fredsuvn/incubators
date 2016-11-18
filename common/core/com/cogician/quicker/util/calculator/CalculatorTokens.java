package com.cogician.quicker.util.calculator;

import com.cogician.quicker.util.lexer.QuickLexToken;
import com.cogician.quicker.util.lexer.regex.RegularQuickLexToken;

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
interface CalculatorTokens<T extends QuickLexToken> {

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

class RegularCalculatorTokens implements CalculatorTokens<RegularQuickLexToken> {

    @Override
    public RegularQuickLexToken macro() {
        return RegularQuickLexToken.MACRO;
    }

    @Override
    public RegularQuickLexToken space() {
        return RegularQuickLexToken.SPACE;
    }

    @Override
    public RegularQuickLexToken crlf() {
        return RegularQuickLexToken.CRLF;
    }

    @Override
    public RegularQuickLexToken identifier() {
        return RegularQuickLexToken.IDENTIFIER;
    }

    @Override
    public RegularQuickLexToken delimiter() {
        return RegularQuickLexToken.DELIMITER;
    }

    @Override
    public RegularQuickLexToken leftBracket() {
        return RegularQuickLexToken.LEFT_BRACKET;
    }

    @Override
    public RegularQuickLexToken rightBracket() {
        return RegularQuickLexToken.RIGHT_BRACKET;
    }

    @Override
    public RegularQuickLexToken comma() {
        return RegularQuickLexToken.COMMA;
    }

    @Override
    public RegularQuickLexToken semicolon() {
        return RegularQuickLexToken.SEMICOLON;
    }

    @Override
    public RegularQuickLexToken number() {
        return RegularQuickLexToken.NUMBER;
    }

    @Override
    public RegularQuickLexToken scientificNotationNumber() {
        return RegularQuickLexToken.SCIENTIFIC_NOTATION;
    }

    @Override
    public RegularQuickLexToken binaryNumber() {
        return RegularQuickLexToken.BIN_NUMBER;
    }

    @Override
    public RegularQuickLexToken octalNumber() {
        return RegularQuickLexToken.OCTAL_NUMBER;
    }

    @Override
    public RegularQuickLexToken hexNumber() {
        return RegularQuickLexToken.HEX_NUMBER;
    }

    @Override
    public RegularQuickLexToken comment() {
        return RegularQuickLexToken.COMMENT;
    }

    @Override
    public RegularQuickLexToken blockComment() {
        return RegularQuickLexToken.BLOCK_COMMENT;
    }

    @Override
    public RegularQuickLexToken lineComment() {
        return RegularQuickLexToken.LINE_COMMENT;
    }

    private static final RegularQuickLexToken OPERATOR = new RegularQuickLexToken("OPERATOR",
            "[\\+\\-\\*\\/\\&\\|\\~\\^\\%]|\\<\\<|\\>\\>\\>|\\>\\>");;

    @Override
    public RegularQuickLexToken operator() {
        return OPERATOR;
    }

    @Override
    public RegularQuickLexToken addOperator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public RegularQuickLexToken subOperator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public RegularQuickLexToken mulOperator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public RegularQuickLexToken divOperator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public RegularQuickLexToken modOperator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public RegularQuickLexToken shiftLeftOperator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public RegularQuickLexToken logicalShiftRightOperator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public RegularQuickLexToken ArithmeticShiftRightOperator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public RegularQuickLexToken andOperator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public RegularQuickLexToken orOperator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public RegularQuickLexToken notOperator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public RegularQuickLexToken xorOperator() {
        throw new UnsupportedOperationException();
    }
}
