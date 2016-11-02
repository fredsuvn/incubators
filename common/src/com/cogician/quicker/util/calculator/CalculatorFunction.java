package com.cogician.quicker.util.calculator;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;

import com.cogician.quicker.Quicker;
import com.cogician.quicker.Uniforms;
import com.cogician.quicker.util.Patterns;

/**
 * <p>
 * Represents function of {@linkplain Calculator}.
 * </p>
 * <p>
 * In a Calculator, a function's name must consist of digits, letters or underline, and must start with a letter or
 * underline. Calculator function supports overload, but a combination of a name and an argument number must distinct.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-08-01T11:50:29+08:00
 * @since 0.0.0, 2016-08-01T11:50:29+08:00
 */
public class CalculatorFunction {

    /**
     * <p>
     * It means argument number this a function is variable: {@value #ARG_NUM_VARIABLE}.
     * </p>
     * 
     * @since 0.0.0
     */
    public static final int ARG_NUM_VARIABLE = Uniforms.INVALID_CODE;

    /**
     * <p>
     * It means argument number this a function is unary: {@value #ARG_NUM_UNARY}.
     * </p>
     * 
     * @since 0.0.0
     */
    public static final int ARG_NUM_UNARY = 1;

    /**
     * <p>
     * It means argument number this a function is binary: {@value #ARG_NUM_BINARY}.
     * </p>
     * 
     * @since 0.0.0
     */
    public static final int ARG_NUM_BINARY = 2;

    static final int ARG_NUM_DELIMITER = 0;

    static final int PRIORITY_FUNCTION = 1000;

    static final int PRIORITY_OPERATOR = 500;

    static final int PRIORITY_DELIMITER = 0;

    private String name;

    private Function<List<BigDecimal>, BigDecimal> action;

    private final int argumentNumber;

    private final int priority;

    /**
     * <p>
     * Constructs a calculator function with given name, action and number of argument. Function name must consist of
     * digits, letters or underline, and must start with a letter or underline. If this function's parameters is
     * variable, given argument number should be < 0.
     * </p>
     * 
     * @param name
     *            function name
     * @param action
     *            action of function
     * @param argumentNumber
     *            number of argument
     * @throws NullPointerException
     *             given given name or action is null
     * @since 0.0.0
     */
    public CalculatorFunction(String name, Function<List<BigDecimal>, BigDecimal> action, int argumentNumber)
            throws NullPointerException {
        this(name, action, argumentNumber, PRIORITY_FUNCTION, false);
    }

    CalculatorFunction(String name, Function<List<BigDecimal>, BigDecimal> action, int argumentNumber, int priority,
            boolean isBuiltIn) throws NullPointerException, IllegalArgumentException {
        String n = Quicker.require(name);
        if (isBuiltIn) {
            this.name = n;
            this.action = action;
        } else {
            if (Patterns.IDENTIFIER.matcher(n).matches()) {
                this.name = n;
            } else {
                throw new IllegalArgumentException(
                        "Function name must consist of digits, letters or underline, and must start with a letter or underline.");
            }
            this.action = Quicker.require(action);
        }
        this.argumentNumber = argumentNumber;
        this.priority = priority;
        this.ref = argumentNumber == ARG_NUM_VARIABLE ? null : new CalculatorFunctionRef(this, argumentNumber);
    }

    /**
     * <p>
     * Returns name of this function.
     * </p>
     * 
     * @return name of this function
     * @since 0.0.0
     */
    public String getName() {
        return name;
    }

    /**
     * <p>
     * Performs this function with given arguments.
     * </p>
     * 
     * @param args
     *            given arguments
     * @return result of performing
     * @since 0.0.0
     */
    public BigDecimal perform(List<BigDecimal> args) {
        return action.apply(args);
    }

    /**
     * <p>
     * Returns whether this function is built-in.
     * </p>
     * 
     * @return whether this function is built-in
     * @since 0.0.0
     */
    boolean isBuiltIn() {
        return getName().length() > 0 ? getName().startsWith("$") : false;
    }

    /**
     * <p>
     * Returns priority of this function.
     * </p>
     * 
     * @return priority of this function
     * @since 0.0.0
     */
    public int getPriority() {
        return priority;
    }

    /**
     * <p>
     * Returns number of argument of this function. If returned value < 0, that means argument number of this function
     * is variable.
     * </p>
     * 
     * @return number of argument of this function
     * @since 0.0.0
     */
    public int getArgumentNumber() {
        return argumentNumber;
    }

    /**
     * <p>
     * Returns whether this function's argument number is variable.
     * </p>
     * 
     * @return whether this function's argument number is variable
     * @since 0.0.0
     */
    public boolean isVariable() {
        return getArgumentNumber() < 0;
    }

    private final CalculatorFunctionRef ref;

    CalculatorFunctionRef toRef(int actualArgumentNumber) {
        return ref == null ? new CalculatorFunctionRef(this, actualArgumentNumber) : ref;
    }

    static class CalculatorFunctionRef {

        private final CalculatorFunction function;

        private final int actualArgument;

        CalculatorFunctionRef(CalculatorFunction function, int actualArgumentNumber) throws NullPointerException {
            this.function = function;
            this.actualArgument = actualArgumentNumber;
        }

        CalculatorFunction getCalculatorFunction() {
            return function;
        }

        int getActualArgument() {
            return actualArgument;
        }

        @Override
        public String toString() {
            return function.getName() + (function.getArgumentNumber() == CalculatorFunction.ARG_NUM_VARIABLE
                    ? "$v_" + getActualArgument() : "$" + function.getArgumentNumber());
        }
    }
}
