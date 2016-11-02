package com.cogician.quicker.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Indicates the class is config-dependent.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-05-20T10:32:24+08:00
 * @since 0.0.0, 2016-05-20T10:32:24+08:00
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface ConfigDependent {

}
