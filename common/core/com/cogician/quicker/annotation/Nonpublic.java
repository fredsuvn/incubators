package com.cogician.quicker.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * This annotation indicates annotated type is non-public, only used by this framework. A type annotated by this
 * annotation is unstable and unguaranteed if it is used in public.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-08-05T21:10:59+08:00
 * @since 0.0.0, 2016-08-05T21:10:59+08:00
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Deprecated
public @interface Nonpublic {

}
