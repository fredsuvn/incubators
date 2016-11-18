package com.cogician.quicker.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * This annotation indicates a type is base and/or underlying.
 * </p>
 * <p>
 * For improving efficiency, the arguments of methods of annotated may not be
 * checked, checking action had better in upper type.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2015-03-05 14:34:02
 * @since 0.0.0
 * @deprecated 
 */
@Documented
@Target({ElementType.TYPE,ElementType.TYPE_PARAMETER,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Deprecated
public @interface Base{

}
