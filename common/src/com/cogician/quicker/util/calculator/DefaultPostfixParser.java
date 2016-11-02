package com.cogician.quicker.util.calculator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import com.cogician.quicker.Quicker;
import com.cogician.quicker.struct.Case;
import com.cogician.quicker.struct.Switch;
import com.cogician.quicker.struct.SwitchBuilder;
import com.cogician.quicker.util.Consts;
import com.cogician.quicker.util.calculator.CalculatorFunction.CalculatorFunctionRef;
import com.cogician.quicker.util.lexer.LexTetrad;
import com.cogician.quicker.util.lexer.ParsingException;
import com.cogician.quicker.util.lexer.RegularLexToken;

/**
 * <p>
 * Default calculator parser.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-08-02T20:06:16+08:00
 * @since 0.0.0, 2016-08-02T20:06:16+08:00
 */
class DefaultPostfixParser implements PostfixParser {

    private static final CalculatorTokens<RegularLexToken> TOKENS = new RegularCalculatorTokens();

    private static final Tokenizer<RegularLexToken> tokenizer = new RegularTokenizer(
            Arrays.asList(TOKENS.identifier(), TOKENS.number(), TOKENS.scientificNotationNumber(), TOKENS.space(),
                    TOKENS.crlf(), TOKENS.operator(), TOKENS.leftBracket(), TOKENS.rightBracket(), TOKENS.comma(),
                    TOKENS.semicolon(), TOKENS.blockComment(), TOKENS.lineComment(), TOKENS.macro()));

    // private static final QuickLogger logger = new QuickLogger(new QuickLoggerConfig());

    private static final Predicate<ParserEnvironment> TEST_UNARY_POS = e -> "+".equals(e.tetrad.getLexeme());

    private static final Predicate<ParserEnvironment> TEST_UNARY_NEG = e -> "-".equals(e.tetrad.getLexeme());

    private static final Predicate<ParserEnvironment> TEST_UNARY_NOT = e -> "~".equals(e.tetrad.getLexeme());

    private static final Predicate<ParserEnvironment> TEST_IDENTIFIER = e -> e.tetrad.getToken() == TOKENS.identifier();

    private static final Predicate<ParserEnvironment> TEST_NUMBER = e -> e.tetrad.getToken().isTokenOf(TOKENS.number());

    private static final Predicate<ParserEnvironment> TEST_OPERATOR = e -> e.tetrad.getToken()
            .isTokenOf(TOKENS.operator());

    private static final Predicate<ParserEnvironment> TEST_LEFT_BRA = e -> e.tetrad.getToken() == TOKENS.leftBracket();

    private static final Predicate<ParserEnvironment> TEST_RIGHT_BRA = e -> e.tetrad.getToken() == TOKENS
            .rightBracket();

    private static final Predicate<ParserEnvironment> TEST_COMMA = e -> e.tetrad.getToken() == TOKENS.comma();

    private static final Consumer<ParserEnvironment> ACTION_UNARY_POS = e -> {
        e.pushStack(BuiltinFunctions.POS);
        e.status[0] = STATUS.UNARY_OPE;
    };

    private static final Consumer<ParserEnvironment> ACTION_UNARY_NEG = e -> {
        e.pushStack(BuiltinFunctions.NEG);
        e.status[0] = STATUS.UNARY_OPE;
    };

    private static final Consumer<ParserEnvironment> ACTION_UNARY_NOT = e -> {
        e.pushStack(BuiltinFunctions.NOT);
        e.status[0] = STATUS.UNARY_OPE;
    };

    private static final Consumer<ParserEnvironment> ACTION_IDENTIFIER = e -> {
        e.functionBagStack.addLast(new FunctionBag(e.tetrad.getLexeme(), 0));
        e.status[0] = STATUS.IDENTIFIER;
    };

    private static final Consumer<ParserEnvironment> ACTION_IDENTIFIER_MUL = e -> {
        e.pushStack(BuiltinFunctions.MUL);
        e.functionBagStack.addLast(new FunctionBag(e.tetrad.getLexeme(), 0));
        e.status[0] = STATUS.IDENTIFIER;
    };

    private static final Consumer<ParserEnvironment> ACTION_NUMBER = e -> {
        e.pushStack(new BigDecimal(e.tetrad.getLexeme()));
        e.status[0] = STATUS.NUMBER;
    };

    private static final Consumer<ParserEnvironment> ACTION_NUMBER_MUL = e -> {
        e.pushStack(BuiltinFunctions.MUL);
        e.pushStack(new BigDecimal(e.tetrad.getLexeme()));
        e.status[0] = STATUS.NUMBER;
    };

    private static final Consumer<ParserEnvironment> ACTION_OPERATOR = e -> {
        e.pushOperator();
        e.status[0] = STATUS.BINARY_OPE;
    };

    private static final Consumer<ParserEnvironment> ACTION_LEFT_BRA = e -> {
        e.pushStack(BuiltinFunctions.LEFT_BRA);
        if (e.functionBagStack.peekLast() != null) {
            e.functionBagStack.peekLast().braStack++;
        }
        e.status[0] = STATUS.LEFT_BRA;
    };

    private static final Consumer<ParserEnvironment> ACTION_LEFT_BRA_MUL = e -> {
        e.pushStack(BuiltinFunctions.MUL);
        e.pushStack(BuiltinFunctions.LEFT_BRA);
        if (e.functionBagStack.peekLast() != null) {
            e.functionBagStack.peekLast().braStack++;
        }
        e.status[0] = STATUS.LEFT_BRA;
    };

    private static final Consumer<ParserEnvironment> ACTION_RIGHT_BRA = e -> {
        e.pushStack(BuiltinFunctions.RIGHT_BRA);
        if (e.functionBagStack.peekLast() != null) {
            FunctionBag bag = e.functionBagStack.peekLast();
            bag.braStack--;
            if (bag.braStack == 0) {
                if (!(e.tetradList.get(e.currentIndex - 1).getToken() == TOKENS.leftBracket())) {
                    bag.actualArgs++;
                }
                CalculatorFunction function = e.calculator.getFunction(bag.func, bag.actualArgs);
                function = function == null ? e.calculator.getFunction(bag.func, CalculatorFunction.ARG_NUM_VARIABLE)
                        : function;
                function = function == null ? e.calculator.getFunction("#" + bag.func, bag.actualArgs) : function;
                if (function == null) {
                    throw e.parser.parsingFunctionNameNotFound(bag.func, e.tetrad);
                }
                e.pushStack(function, bag.actualArgs);
                e.functionBagStack.pollLast();
            }
        }
        e.status[0] = STATUS.RIGHT_BRA;
    };

    private static final Consumer<ParserEnvironment> ACTION_COMMA = e -> {
        e.pushStack(BuiltinFunctions.COMMA);
        if (e.functionBagStack.peekLast() != null && e.functionBagStack.peekLast().braStack == 1) {
            e.functionBagStack.peekLast().actualArgs++;
        } else {
            throw e.parser.parsingFailed(e.tetrad);
        }
        e.status[0] = STATUS.COMMA;
    };

    private static final Case<ParserEnvironment> DEFAULT_FAILED_CASE = new Case<ParserEnvironment>(null, null,
            (Predicate<ParserEnvironment>)(e -> {
                throw e.parser.parsingFailed(e.tetrad);
            }));

    private static final Case<ParserEnvironment> CASE_UNARY_POS = new Case<ParserEnvironment>(null, TEST_UNARY_POS,
            ACTION_UNARY_POS);

    private static final Case<ParserEnvironment> CASE_UNARY_NEG = new Case<ParserEnvironment>(null, TEST_UNARY_NEG,
            ACTION_UNARY_NEG);

    private static final Case<ParserEnvironment> CASE_UNARY_NOT = new Case<ParserEnvironment>(null, TEST_UNARY_NOT,
            ACTION_UNARY_NOT);

    private static final Case<ParserEnvironment> CASE_IDENTIFIER = new Case<ParserEnvironment>(null, TEST_IDENTIFIER,
            ACTION_IDENTIFIER);

    private static final Case<ParserEnvironment> CASE_IDENTIFIER_MUL = new Case<ParserEnvironment>(null,
            TEST_IDENTIFIER, ACTION_IDENTIFIER_MUL);

    private static final Case<ParserEnvironment> CASE_NUMBER = new Case<ParserEnvironment>(null, TEST_NUMBER,
            ACTION_NUMBER);

    private static final Case<ParserEnvironment> CASE_NUMBER_MUL = new Case<ParserEnvironment>(null, TEST_NUMBER,
            ACTION_NUMBER_MUL);

    private static final Case<ParserEnvironment> CASE_LEFT_BRA = new Case<ParserEnvironment>(null, TEST_LEFT_BRA,
            ACTION_LEFT_BRA);

    private static final Case<ParserEnvironment> CASE_LEFT_BRA_MUL = new Case<ParserEnvironment>(null, TEST_LEFT_BRA,
            ACTION_LEFT_BRA_MUL);

    private static final Case<ParserEnvironment> CASE_RIGHT_BRA = new Case<ParserEnvironment>(null, TEST_RIGHT_BRA,
            ACTION_RIGHT_BRA);

    private static final Case<ParserEnvironment> CASE_OPERATOR = new Case<ParserEnvironment>(null, TEST_OPERATOR,
            ACTION_OPERATOR);

    private static final Case<ParserEnvironment> CASE_COMMA = new Case<ParserEnvironment>(null, TEST_COMMA,
            ACTION_COMMA);

    @SuppressWarnings("unchecked")
    private static final Switch<ParserEnvironment> SWITCH_START_STATUS = new SwitchBuilder<ParserEnvironment>()
            .addCases(CASE_UNARY_POS, CASE_UNARY_NEG, CASE_UNARY_NOT, CASE_IDENTIFIER, CASE_NUMBER, CASE_LEFT_BRA)
            .setDefaultCase(DEFAULT_FAILED_CASE).build();

    private static final Switch<ParserEnvironment> SWITCH_UNARY_OPE_STATUS = SWITCH_START_STATUS;

    private static final Switch<ParserEnvironment> SWITCH_IDENTIFIER_STATUS = new SwitchBuilder<ParserEnvironment>()
            .addCase(CASE_LEFT_BRA).setDefaultCase(DEFAULT_FAILED_CASE).build();

    @SuppressWarnings("unchecked")
    private static final Switch<ParserEnvironment> SWITCH_NUMBER_STATUS = new SwitchBuilder<ParserEnvironment>()
            .addCases(CASE_OPERATOR, CASE_IDENTIFIER_MUL, CASE_COMMA, CASE_LEFT_BRA_MUL, CASE_RIGHT_BRA)
            .setDefaultCase(DEFAULT_FAILED_CASE).build();

    private static final Switch<ParserEnvironment> SWITCH_BINARY_OPE_STATUS = SWITCH_START_STATUS;

    @SuppressWarnings("unchecked")
    private static final Switch<ParserEnvironment> SWITCH_LEFT_BRA_STATUS = new SwitchBuilder<ParserEnvironment>()
            .addCases(CASE_UNARY_POS, CASE_UNARY_NEG, CASE_UNARY_NOT, CASE_IDENTIFIER, CASE_NUMBER, CASE_LEFT_BRA,
                    CASE_RIGHT_BRA)
            .setDefaultCase(DEFAULT_FAILED_CASE).build();

    @SuppressWarnings("unchecked")
    private static final Switch<ParserEnvironment> SWITCH_RIGHT_BRA_STATUS = new SwitchBuilder<ParserEnvironment>()
            .addCases(CASE_UNARY_POS, CASE_UNARY_NEG, CASE_UNARY_NOT, CASE_IDENTIFIER_MUL, CASE_NUMBER_MUL,
                    CASE_OPERATOR, CASE_LEFT_BRA_MUL, CASE_RIGHT_BRA, CASE_COMMA)
            .setDefaultCase(DEFAULT_FAILED_CASE).build();

    private static final Switch<ParserEnvironment> SWITCH_COMMA_STATUS = SWITCH_START_STATUS;

    @SuppressWarnings("unchecked")
    private static final Switch<ParserEnvironment> parsingSwitch = new SwitchBuilder<ParserEnvironment>()
            .addCases(new Case<ParserEnvironment>(null, e -> e.status[0] == STATUS.START, e -> {
                SWITCH_START_STATUS.perform(e);
            })).addCases(new Case<ParserEnvironment>(null, e -> e.status[0] == STATUS.UNARY_OPE, e -> {
                SWITCH_UNARY_OPE_STATUS.perform(e);
            })).addCases(new Case<ParserEnvironment>(null, e -> e.status[0] == STATUS.IDENTIFIER, e -> {
                SWITCH_IDENTIFIER_STATUS.perform(e);
            })).addCases(new Case<ParserEnvironment>(null, e -> e.status[0] == STATUS.NUMBER, e -> {
                SWITCH_NUMBER_STATUS.perform(e);
            })).addCases(new Case<ParserEnvironment>(null, e -> e.status[0] == STATUS.BINARY_OPE, e -> {
                SWITCH_BINARY_OPE_STATUS.perform(e);
            })).addCases(new Case<ParserEnvironment>(null, e -> e.status[0] == STATUS.LEFT_BRA, e -> {
                SWITCH_LEFT_BRA_STATUS.perform(e);
            })).addCases(new Case<ParserEnvironment>(null, e -> e.status[0] == STATUS.RIGHT_BRA, e -> {
                SWITCH_RIGHT_BRA_STATUS.perform(e);
            })).addCases(new Case<ParserEnvironment>(null, e -> e.status[0] == STATUS.COMMA, e -> {
                SWITCH_COMMA_STATUS.perform(e);
            })).setDefaultCase(DEFAULT_FAILED_CASE).build();

    private final Calculator calculator;

    public DefaultPostfixParser(Calculator calculator) {
        this.calculator = calculator;
    }

    private void removeUseless(List<LexTetrad> tetrads) {
        List<LexTetrad> tmp = new LinkedList<>(tetrads);
        tetrads.clear();
        tmp.forEach(t -> {
            if (!(t.getToken().isTokenOf(TOKENS.comment()) || t.getToken().isTokenOf(TOKENS.space())
                    || t.getToken().isTokenOf(TOKENS.crlf()))) {
                tetrads.add(t);
            }
        });
    }

    private void expandMacro(List<LexTetrad> tetrads) {
        boolean inMacroDefine = false;
        int status = 0;
        String macFunctionName = null;
        List<String> args = new ArrayList<>();
        List<String> body = new ArrayList<>();
        for (int i = 0; i < tetrads.size(); i++) {
            LexTetrad tetrad = tetrads.get(i);
            if (!inMacroDefine && tetrad.getToken() != TOKENS.macro()) {
                tetrads.subList(0, i).clear();
                return;
            } else if (!inMacroDefine && tetrad.getToken() == TOKENS.macro()) {
                inMacroDefine = true;
            } else if (inMacroDefine) {
                switch (status) {
                    case 0: {
                        macFunctionName = tetrad.getLexeme();
                        status = 1;
                        break;
                    }
                    case 1: {
                        if (tetrad.getToken() == TOKENS.leftBracket()) {
                            status = 2;
                        } else {
                            throw parsingFailed("Wrong macro define.", tetrad);
                        }
                        break;
                    }
                    case 2: {
                        if (tetrad.getToken() == TOKENS.identifier()) {
                            args.add(tetrad.getLexeme());
                            status = 3;
                        } else {
                            throw parsingFailed("Wrong macro define.", tetrad);
                        }
                        break;
                    }
                    case 3: {
                        if (tetrad.getToken() == TOKENS.comma()) {
                            status = 2;
                        } else if (tetrad.getToken() == TOKENS.rightBracket()) {
                            status = 4;
                        } else {
                            throw parsingFailed("Wrong macro define.", tetrad);
                        }
                        break;
                    }
                    case 4: {
                        if (tetrad.getToken() != TOKENS.semicolon()) {
                            body.add(tetrad.getLexeme());
                            status = 5;
                        } else {
                            throw parsingFailed("Wrong macro define.", tetrad);
                        }
                        break;
                    }
                    case 5: {
                        if (tetrad.getToken() == TOKENS.semicolon()) {
                            addMacroFunction(macFunctionName, args, body);
                            macFunctionName = null;
                            args.clear();
                            body.clear();
                            inMacroDefine = false;
                            status = 0;
                        } else {
                            body.add(tetrad.getLexeme());
                        }
                        break;
                    }
                }
            }
        }
        if (status != 0) {
            throw new ParsingException(UNKNOWN_PROBLEM);
        }
    }

    private void addMacroFunction(String name, List<String> args, List<String> body) {
        String macroFunctionName = "#" + name;
        Function<List<BigDecimal>, BigDecimal> action = new Function<List<BigDecimal>, BigDecimal>() {

            private Calculator calculator = DefaultPostfixParser.this.calculator;

            private String[] funcArgs = args.toArray(Consts.emptyStringArray());

            private String funcBody = toBody();

            private String toBody() {
                StringBuilder sb = new StringBuilder();
                body.forEach(str -> {
                    sb.append(str).append(" ");
                });
                return sb.toString();
            }

            @Override
            public BigDecimal apply(List<BigDecimal> t) {
                // logger.debug(macroFunctionName + " macro body: " + funcBody);
                String actual = funcBody;
                for (int i = 0; i < funcArgs.length; i++) {
                    actual = actual.replaceAll("\\b" + funcArgs[i] + "\\b", t.get(i).toString());
                }
                // logger.debug(macroFunctionName + " macro replacement body: " + actual);
                BigDecimal result = calculator.calculate(actual);
                // logger.debug(macroFunctionName + " macro result: " + result);
                return result;
            }
        };
        CalculatorFunction macroFunction = new CalculatorFunction(macroFunctionName, action, args.size(),
                CalculatorFunction.PRIORITY_FUNCTION, true);
        calculator.addFunction(macroFunction);
    }

    private static class ParserEnvironment {
        Calculator calculator;
        DefaultPostfixParser parser;
        List<Object> stack;
        Deque<CalculatorFunctionRef> functionStack;
        LexTetrad tetrad;
        int[] status;
        Deque<FunctionBag> functionBagStack;
        List<LexTetrad> tetradList;
        int currentIndex;

        void pushStack(BigDecimal num) {
            parser.pushStack(stack, num);
        }

        void pushStack(CalculatorFunction function) {
            parser.pushStack(stack, functionStack, function);
        }

        void pushStack(CalculatorFunction function, int actualArgumentNumber) {
            parser.pushStack(stack, functionStack, function, actualArgumentNumber);
        }

        void pushOperator() {
            parser.pushOperator(stack, functionStack, tetrad);
        }
    }

    private static class STATUS {
        static final int START = 0;
        static final int UNARY_OPE = 1;
        static final int IDENTIFIER = 2;
        static final int NUMBER = 3;
        static final int BINARY_OPE = 4;
        static final int LEFT_BRA = 5;
        static final int RIGHT_BRA = 6;
        static final int COMMA = 7;
    }

    private static class FunctionBag {
        String func;
        int braStack;
        int actualArgs = 0;

        FunctionBag(String func, int braStack) {
            this.func = func;
            this.braStack = braStack;
        }
    }

    private void pushStack(List<Object> stack, BigDecimal num) {
        stack.add(num);
    }

    private void pushStack(List<Object> stack, Deque<CalculatorFunctionRef> functionStack, CalculatorFunction function)
            throws ParsingException {
        if (function.getArgumentNumber() == CalculatorFunction.ARG_NUM_UNARY) {
            pushStack(stack, functionStack, BuiltinFunctions.ADD, 2);
        }
        pushStack(stack, functionStack, function, function.getArgumentNumber());
    }

    private void pushStack(List<Object> stack, Deque<CalculatorFunctionRef> functionStack, CalculatorFunction function,
            int actualArgumentNumber) throws ParsingException {
        if (!function.isVariable() && function.getArgumentNumber() != actualArgumentNumber) {
            throw new ParsingException("Actual passed argument number of function " + function.getName()
                    + " doesn't match the required number of this function.");
        }
        CalculatorFunction last = functionStack.peekLast() == null ? null
                : functionStack.peekLast().getCalculatorFunction();
        if (last == null || function == BuiltinFunctions.LEFT_BRA || function.getPriority() > last.getPriority()) {
            functionStack.addLast(function.toRef(actualArgumentNumber));
        } else if (function == BuiltinFunctions.RIGHT_BRA) {
            while (!(functionStack.peekLast().getCalculatorFunction() == BuiltinFunctions.LEFT_BRA)) {
                stack.add(functionStack.removeLast());
            }
            functionStack.removeLast();
        } else if (function == BuiltinFunctions.COMMA) {
            while (!(functionStack.peekLast().getCalculatorFunction() == BuiltinFunctions.LEFT_BRA)) {
                stack.add(functionStack.removeLast());
            }
        } else if (function.getPriority() <= last.getPriority()) {
            while (last != null && last.getPriority() >= function.getPriority()) {
                stack.add(functionStack.removeLast());
                last = functionStack.peekLast() == null ? null : functionStack.peekLast().getCalculatorFunction();
            }
            functionStack.addLast(function.toRef(actualArgumentNumber));
        } else {
            throw new ParsingException(UNKNOWN_PROBLEM);
        }
    }

    private void pushOperator(List<Object> stack, Deque<CalculatorFunctionRef> functionStack, LexTetrad tetrad) {
        switch (tetrad.getLexeme()) {
            case "+": {
                pushStack(stack, functionStack, BuiltinFunctions.ADD);
                break;
            }
            case "-": {
                pushStack(stack, functionStack, BuiltinFunctions.SUB);
                break;
            }
            case "*": {
                pushStack(stack, functionStack, BuiltinFunctions.MUL);
                break;
            }
            case "/": {
                pushStack(stack, functionStack, BuiltinFunctions.DIV);
                break;
            }
            case "%": {
                pushStack(stack, functionStack, BuiltinFunctions.REM);
                break;
            }
            case "&": {
                pushStack(stack, functionStack, BuiltinFunctions.AND);
                break;
            }
            case "|": {
                pushStack(stack, functionStack, BuiltinFunctions.OR);
                break;
            }
            case "^": {
                pushStack(stack, functionStack, BuiltinFunctions.XOR);
                break;
            }
            case "<<": {
                pushStack(stack, functionStack, BuiltinFunctions.LFT);
                break;
            }
            case ">>": {
                pushStack(stack, functionStack, BuiltinFunctions.ASR);
                break;
            }
            case ">>>": {
                pushStack(stack, functionStack, BuiltinFunctions.LSR);
                break;
            }
            default: {
                throw parsingFailed(tetrad);
            }
        }
    }

    private ParsingException parsingFailed(LexTetrad tetrad) {
        return new ParsingException("Expression wrong at row " + tetrad.getRow() + ", column " + tetrad.getColumn()
                + ": " + Quicker.ellipsis(tetrad.getLexeme(), RESERVED_CHAR_NUM));
    }

    private ParsingException parsingFailed(String msg, LexTetrad tetrad) {
        return new ParsingException(msg + " Thrown at row " + tetrad.getRow() + ", column " + tetrad.getColumn() + ": "
                + Quicker.ellipsis(tetrad.getLexeme(), RESERVED_CHAR_NUM));
    }

    private ParsingException parsingFunctionNameNotFound(String functionName, LexTetrad tetrad) {
        return new ParsingException("Function not found at row " + tetrad.getRow() + ", column " + tetrad.getColumn()
                + ": " + Quicker.ellipsis(functionName, RESERVED_CHAR_NUM));
    }

    private static final int RESERVED_CHAR_NUM = 666;

    private static final String UNKNOWN_PROBLEM = "Unknown exception occurs.";

    public List<Object> parsePostfix(List<LexTetrad> tetrads) {
        removeUseless(tetrads);
        expandMacro(tetrads);
        List<Object> stack = new ArrayList<>();
        Deque<CalculatorFunctionRef> functionStack = new LinkedList<>();
        Deque<FunctionBag> functionBagStack = new LinkedList<>();
        int[] status = {STATUS.START};
        ParserEnvironment en = new ParserEnvironment();
        en.parser = this;
        en.calculator = calculator;
        en.status = status;
        en.stack = stack;
        en.functionStack = functionStack;
        en.functionBagStack = functionBagStack;
        en.tetradList = tetrads;
        for (int i = 0; i < tetrads.size(); i++) {
            LexTetrad tetrad = tetrads.get(i);
            en.tetrad = tetrad;
            en.currentIndex = i;
            parsingSwitch.perform(en);
        }
        while (functionStack.peekLast() != null) {
            stack.add(functionStack.pollLast());
        }
        // logger.debug(stack);
        return stack;
    }

    @Override
    public List<Object> parse(String input) throws NullPointerException, ParsingException {
        return parsePostfix(tokenizer.tokenize(input));
    }
}
