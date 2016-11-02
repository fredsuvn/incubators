package com.cogician.quicker.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * A method annotated by this annotation indicates its parameters can use directly without routine checking when they
 * are passed in. It is usually used in private methods or constructors.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-03-20T17:35:21+08:00
 * @since 0.0.0, 2016-03-20T17:35:21+08:00
 */
@Documented
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.CLASS)
public @interface ParamsDirectToUse {

}
