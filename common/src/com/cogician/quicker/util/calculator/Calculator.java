package com.cogician.quicker.util.calculator;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.cogician.quicker.Checker;
import com.cogician.quicker.Quicker;
import com.cogician.quicker.util.QuiCollections;
import com.cogician.quicker.util.calculator.CalculatorFunction.CalculatorFunctionRef;
import com.cogician.quicker.util.lexer.ParsingException;

/**
 * <p>
 * A simple calculator to calculate arithmetic expression and return a big decimal.
 * </p>
 * <p>
 * For example:
 * 
 * <pre>
 * BigDecimal result = new Calculator().calculate("2 * (3 + 5)");
 * (result will be 16)
 * </pre>
 * 
 * Just like the above example, the arithmetic expression includes integer and decimal, base 10, unary operators (+, -,
 * ~), binary operators (+, -, *, /, %, &, |, ^, &#60;&#60;, &#62;&#62;, &#62;&#62;&#62;).
 * </p>
 * <h2>Macro</h2>
 * <p>
 * Calculator supports macro:
 * 
 * <pre>
 * String exp = "#define add3(x, y, z) x + y + z;\r\n"
 * + "#define add6(x, y, z, a, b, c) add3(x,y,z) + add3(a,b,c);\r\n"
 * + "5max(2, 10%3, add6(1,2,3,4,5,6))-  1";
 * BigDecimal result = new Calculator().calculate(exp);
 * (result will be 104)
 * </pre>
 * 
 * A macro start with "#define", case insensitive, and end with a ";". Name of macro must consist of numbers, letters or
 * underline, and must start with a letter or underline.
 * </p>
 * <h2>Function</h2>
 * <p>
 * Calculator can be added custom function with {@linkplain #addFunction(CalculatorFunction)}. It supports fixed and
 * variable parameters. See {@linkplain #addFunction(CalculatorFunction)}.
 * <h2>Not Thread-safe and Cloneable</h2>
 * <p>
 * Calculator is not thread-safe, but it is cloneable so using {@linkplain #clone()} can copy a new instance contains
 * all custom function.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-01-28T10:48:51+08:00
 * @since 0.0.0, 2016-01-28T10:48:51+08:00
 */
public class Calculator implements Cloneable {

    @Nullable
    private Map<String, CalculatorFunction> functionMap = null;

    private DefaultPostfixParser parser = new DefaultPostfixParser(this);

    /**
     * <p>
     * Constructs an instance of calculator.
     * </p>
     * 
     * @since 0.0.0
     */
    public Calculator() {

    }

    /**
     * <p>
     * Calculates given arithmetic expression.
     * </p>
     * 
     * @param expr
     *            given arithmetic expression
     * @return result of expression
     * @throws NullPointerException
     *             if given expression is null
     * @throws ParsingException
     *             if any problem occurs when parsing or calculating
     * @since 0.0.0
     */
    public BigDecimal calculate(String expr) throws NullPointerException, ParsingException {
        Checker.checkNull(expr);
        List<Object> stack = parser.parse(expr);
        List<BigDecimal> numberStack = new LinkedList<>();
        numberStack.clear();
        for (int i = 0; i < stack.size();) {
            Object obj = stack.get(i);
            if (obj instanceof BigDecimal) {
                i++;
            } else if (obj == null) {
                stack.set(i, new BigDecimal(0));
                i++;
            } else if (obj instanceof CalculatorFunctionRef) {
                CalculatorFunctionRef op = (CalculatorFunctionRef)obj;
                int indexAfterOperating = i - op.getActualArgument();
                for (int j = indexAfterOperating; j < i; j++) {
                    numberStack.add((BigDecimal)stack.get(j));
                }
                BigDecimal opResult = Quicker.require(op.getCalculatorFunction().perform(numberStack),
                        () -> new BigDecimal(0));
                numberStack.clear();
                int removeCount = 0;
                for (; removeCount < op.getActualArgument(); removeCount++) {
                    stack.remove(indexAfterOperating);
                }
                stack.set(indexAfterOperating, opResult);
                i = indexAfterOperating;
            } else {
                throw new ParsingException("Unknown parsing exception: " + expr);
            }
        }
        if (stack.size() != 1) {
            throw new ParsingException("Unknown parsing exception: " + expr);
        }
        BigDecimal result = (BigDecimal)Quicker.require(stack.get(0), () -> new BigDecimal(0));
        stack.clear();
        return result;
    }

    /**
     * <p>
     * Add given function in this calculator. Combination of function name and argument number must be distinct in this
     * calculator.
     * </p>
     * 
     * @param function
     *            given function
     * @throws NullPointerException
     *             if given function is null
     * @throws IllegalArgumentException
     *             if combination of function name and argument number is duplicated
     * @since 0.0.0
     */
    public void addFunction(CalculatorFunction function) throws NullPointerException, IllegalArgumentException {
        Checker.checkNull(function);
        String actualName = createActualName(function.getName(), function.getArgumentNumber());
        if (functionMap == null) {
            functionMap = new HashMap<>();
        } else if (functionMap.get(actualName) != null) {
            throw new IllegalArgumentException("There exists a function with same name in this calculator.");
        }
        functionMap.put(actualName, function);
    }

    /**
     * <p>
     * Returns function with given function's name and argument number in this calculator. If searched function's
     * argument number is variable, given argument number should be < 0 . Return null if not found.
     * </p>
     * 
     * @param name
     *            given function's name
     * @param argumentNumber
     *            given argument number
     * @return function with given function's name and argument number in this calculator or null if not found
     * @throws NullPointerException
     *             if given function's name is null
     * @since 0.0.0
     */
    public @Nullable CalculatorFunction getFunction(String name, int argumentNumber) throws NullPointerException {
        Checker.checkNull(name);
        if (functionMap == null) {
            return null;
        }
        return functionMap.get(createActualName(name, argumentNumber));
    }

    private String createActualName(String name, int argumentNumber) {
        return name + "$" + (argumentNumber < 0 ? CalculatorFunction.ARG_NUM_VARIABLE : argumentNumber);
    }

    /**
     * <p>
     * Deep clones this instance and returns.
     * </p>
     * 
     * @return a new copy
     * @since 0.0.0
     */
    public Calculator clone() {
        Calculator clone = new Calculator();
        clone.functionMap = new HashMap<>(functionMap);
        return clone;
    }

}
