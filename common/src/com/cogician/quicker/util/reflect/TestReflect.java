package com.cogician.quicker.util.reflect;

import com.cogician.quicker.util.ToStringQuicker;
import com.cogician.quicker.util.ToStringQuicker.ToStringConfig;

/**
 * <p>
 * 
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-08-09T16:16:38+08:00
 * @since 0.0.0, 2016-08-09T16:16:38+08:00
 */
public class TestReflect {

    /**
     * <p>
     * 
     * </p>
     * 
     * @param args
     * @since 0.0.0
     */
    public static void main(String[] args) {
        // Quicker.each(ReflectionQuicker.iteratorOfInterfaceInheritance(Sub2_0.class), i -> {
        // System.out.println(i);
        // });
        // Quicker.each(ReflectionQuicker.iteratorOfClassInheritance(SubCls2.class, SubCls0.class), i -> {
        // System.out.println(i);
        // });
        // Quicker.each(ReflectionQuicker.iteratorOfMembers(SubCls2.class, SubCls0.class), i -> {
        // System.out.println(i);
        // });
        SubCls2 sc2 = new SubCls2();
        // Quicker.each(ReflectionQuicker.iteratorOfFields(sc2.getClass(), SubCls0.class), i -> {
        // try {
        // i.setAccessible(true);
        // System.out.println(i + " = " + i.get(sc2));
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        // });
        ToStringConfig.Builder builder = new ToStringConfig.Builder().copyFrom(ToStringConfig.REFLECTION);
        // builder.setUpTo(SubCls2.class);
        System.out.println(ToStringQuicker.reflectToString(sc2,
                ToStringConfig.Builder.addStyleSuffix(builder.build(), System.lineSeparator())));
    }

}

interface Root {
    static final String rootStr = "rootStr";
}

interface Sub1_0 extends Root {
    static final String rootStr = "Sub1_0";
}

interface Sub1_1 extends Root {
    static final String rootStr = "Sub1_1";
}

interface Sub1_2 extends Root {
    static final String rootStr = "Sub1_2";
}

interface Sub2_0 extends Root, Sub1_0 {
    static final String rootStr = "Sub1_2";

    void method2_0();
}

class SubCls0 {

    SubCls0() {

    }

    void method0() {

    }

    public String str = "123";
}

class SubCls1 extends SubCls0 {
    SubCls1() {

    }

    void method0() {

    }

    void method1() {

    }

    public String str = "456";
}

class SubCls2 extends SubCls1 implements Sub2_0 {
    SubCls2() {

    }

    void method0() {

    }

    void method1() {

    }

    void method2() {

    }

    public String str = "789";

    @Override
    public void method2_0() {
    }
}
