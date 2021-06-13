package com.cogician.quicker.util.calculator;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * <p>
 * Built-in functions of calculator.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-08-01T11:58:24+08:00
 * @since 0.0.0, 2016-08-01T11:58:24+08:00
 */
class BuiltinFunctions {

    static final CalculatorFunction ADD = new CalculatorFunction("$ADD", l -> {
        return l.get(0).add(l.get(1));
    }, CalculatorFunction.ARG_NUM_BINARY, CalculatorFunction.PRIORITY_OPERATOR + 90, true);

    static final CalculatorFunction SUB = new CalculatorFunction("$SUB", l -> {
        return l.get(0).subtract(l.get(1));
    }, CalculatorFunction.ARG_NUM_BINARY, CalculatorFunction.PRIORITY_OPERATOR + 90, true);

    static final CalculatorFunction MUL = new CalculatorFunction("$MUL", l -> {
        return l.get(0).multiply(l.get(1));
    }, CalculatorFunction.ARG_NUM_BINARY, CalculatorFunction.PRIORITY_OPERATOR + 100, true);

    static final CalculatorFunction DIV = new CalculatorFunction("$DIV", l -> {
        return l.get(0).divide(l.get(1));
    }, CalculatorFunction.ARG_NUM_BINARY, CalculatorFunction.PRIORITY_OPERATOR + 100, true);

    static final CalculatorFunction REM = new CalculatorFunction("$REM", l -> {
        return l.get(0).remainder(l.get(1));
    }, CalculatorFunction.ARG_NUM_BINARY, CalculatorFunction.PRIORITY_OPERATOR + 100, true);

    static final CalculatorFunction AND = new CalculatorFunction("$AND", l -> {
        return new BigDecimal(l.get(0).toBigInteger().and(l.get(1).toBigInteger()));
    }, CalculatorFunction.ARG_NUM_BINARY, CalculatorFunction.PRIORITY_OPERATOR + 75, true);

    static final CalculatorFunction OR = new CalculatorFunction("$OR", l -> {
        return new BigDecimal(l.get(0).toBigInteger().or(l.get(1).toBigInteger()));
    }, CalculatorFunction.ARG_NUM_BINARY, CalculatorFunction.PRIORITY_OPERATOR + 65, true);

    static final CalculatorFunction NOT = new CalculatorFunction("$NOT", l -> {
        return new BigDecimal(l.get(0).toBigInteger().not());
    }, CalculatorFunction.ARG_NUM_UNARY, CalculatorFunction.PRIORITY_OPERATOR + 110, true);

    static final CalculatorFunction XOR = new CalculatorFunction("$XOR", l -> {
        return new BigDecimal(l.get(0).toBigInteger().xor(l.get(1).toBigInteger()));
    }, CalculatorFunction.ARG_NUM_BINARY, CalculatorFunction.PRIORITY_OPERATOR + 70, true);

    static final CalculatorFunction LFT = new CalculatorFunction("$LFT", l -> {
        return new BigDecimal(l.get(0).toBigInteger().shiftLeft(l.get(1).intValue()));
    }, CalculatorFunction.ARG_NUM_BINARY, CalculatorFunction.PRIORITY_OPERATOR + 80, true);

    static final CalculatorFunction LSR = new CalculatorFunction("$LSR", l -> {
        BigInteger i = l.get(0).toBigInteger();
        i = i.signum() < 0 ? i.negate() : i;
        return new BigDecimal(i.shiftRight(l.get(1).intValue()));
    }, CalculatorFunction.ARG_NUM_BINARY, CalculatorFunction.PRIORITY_OPERATOR + 80, true);

    static final CalculatorFunction ASR = new CalculatorFunction("$ASR", l -> {
        return new BigDecimal(l.get(0).toBigInteger().shiftRight(l.get(1).intValue()));
    }, CalculatorFunction.ARG_NUM_BINARY, CalculatorFunction.PRIORITY_OPERATOR + 80, true);

    static final CalculatorFunction NEG = new CalculatorFunction("$NEG", l -> {
        return l.get(0).negate();
    }, CalculatorFunction.ARG_NUM_UNARY, CalculatorFunction.PRIORITY_OPERATOR + 110, true);

    static final CalculatorFunction POS = new CalculatorFunction("$POS", l -> {
        return l.get(0);
    }, CalculatorFunction.ARG_NUM_UNARY, CalculatorFunction.PRIORITY_OPERATOR + 110, true);

    static final CalculatorFunction LEFT_BRA = new CalculatorFunction("$LEFT_BRA", l -> {
        return l.get(0).negate();
    }, CalculatorFunction.ARG_NUM_DELIMITER, CalculatorFunction.PRIORITY_DELIMITER, true);

    static final CalculatorFunction RIGHT_BRA = new CalculatorFunction("$RIGHT_BRA", l -> {
        return l.get(0).negate();
    }, CalculatorFunction.ARG_NUM_DELIMITER, CalculatorFunction.PRIORITY_DELIMITER, true);

    static final CalculatorFunction COMMA = new CalculatorFunction("$COMMA", l -> {
        return l.get(0).negate();
    }, CalculatorFunction.ARG_NUM_DELIMITER, CalculatorFunction.PRIORITY_DELIMITER, true);
}
