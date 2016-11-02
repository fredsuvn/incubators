package com.cogician.quicker.util.calculator;

import java.math.BigDecimal;
import java.util.Collections;

/**
 * <p>
 * 
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-08-01T23:05:22+08:00
 * @since 0.0.0, 2016-08-01T23:05:22+08:00
 */
public class TestCalculator {

    public static void main(String[] args) {
        Calculator c = new Calculator();
        c.addFunction(new CalculatorFunction("max", l -> {
            return l.get(0).compareTo(l.get(1)) > 0 ? l.get(0) : l.get(1);
        }, 2));
        c.addFunction(new CalculatorFunction("max", l -> {
            Collections.sort(l, (l1, l2) -> l1.compareTo(l2));
            return l.get(l.size() - 1);
        }, -1));
        c.addFunction(new CalculatorFunction("show", l -> {
            System.out.println("I'm function show!");
            return null;
        }, 0));
        String exp = "" + "#define add3(x, y, z) x + y + z;\r\n"
                + "#define add6(x, y, z, a, b, c) add3(x,y,z) + add3(a,b,c);\r\n"
                + "5max(2, 10%3, add6(1,2,3,4,5,6))-  1";
        BigDecimal result = c.clone().calculate(exp);
        System.out.println(result);
        System.out.println(c.calculate("10%3"));
        System.out.println(c.calculate("3%10"));
        System.out.println(c.calculate("2 * (3 + 5)"));
    }
}
