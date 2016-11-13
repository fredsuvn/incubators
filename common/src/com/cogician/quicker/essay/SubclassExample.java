package com.cogician.quicker.essay;

/**
 * <p>
 * 
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-11-04T08:43:04+08:00
 * @since 0.0.0, 2016-11-04T08:43:04+08:00
 */
public class SubclassExample {

}

class Parent{
    
    Parent get(){
        return this;
    }
}

class Sub extends Parent{
    
    @Override
    Sub get(){
        return this;
    }
}