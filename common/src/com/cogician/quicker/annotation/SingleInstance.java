package com.cogician.quicker.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * This annotation indicates a type should only have a single instance, and this
 * single instance should be thread-safe unless otherwise specified.
 * </p>
 * 
 * @author Fred Suvn
 * @version 0.0.0, 2015-12-07 15:39:09
 * @since 0.0.0
 */
@Documented
@Target({ElementType.TYPE,})
@Retention(RetentionPolicy.CLASS)
public @interface SingleInstance {

}
