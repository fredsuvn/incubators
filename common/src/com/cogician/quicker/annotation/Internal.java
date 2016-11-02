package com.cogician.quicker.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Indicates annotated type is used in internal of public framework. Annotated
 * type should not be opened in public API.
 * </p>
 * 
 * @author Fred Suvn
 * @version 0.0.0, 2015-12-22 11:38:43
 * @since 0.0.0
 */
@Documented
@Target({ElementType.TYPE,})
@Retention(RetentionPolicy.CLASS)
public @interface Internal {

}
