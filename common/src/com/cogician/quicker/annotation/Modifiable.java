package com.cogician.quicker.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Annotates the parameter is modifiable. By default a parameter without this annotation is read-only.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-03-21T08:41:45+08:00
 * @since 0.0.0, 2016-03-21T08:41:45+08:00
 */
@Documented
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Modifiable {

}
