package com.cogician.quicker.util;

/**
 * <p>
 * Specification of static utility class.
 * </p>
 * <p>
 * Static utility class is a type of class only provide static method, commonly
 * used to be toolkit class of which methods are stateless. Static utility class
 * {@code XxxUtil} will provide static methods of which names and parameters are
 * same as instance methods come from specification interface
 * {@code XxxUtilSpec} if {@code XxxUtilSpec} exist. {@code XxxUtilSpec} is
 * detail specification of utility class providing default method
 * implementation.
 * </p>
 * <p>
 * Implementation of {@code XxxUtilSpec} must provide a no-argument constructor
 * and better be in single-instance. {@code XxxUtil}'s static methods should
 * directly call same-names and same-parameters methods of instance of the
 * implementation.
 * </p>
 * <p>
 * This {@code XxxUtilSpec} -> {@code XxxUtil} mode called Spec-Util mode. It
 * help maintain {@code XxxUtil}, especially in adapting to different
 * environment. {@code XxxUtil} won't have to according to this mode, if it is
 * very stable for the foreseeable future.
 * </p>
 * 
 * @author Fred Suvn
 * @version 0.0.0, 2015-12-19 14:24:08
 * @since 0.0.0
 * @deprecated Now, a {@code XxxUtil} dosen't have to only one
 *             {@code XxxUtilSpec}. It maybe has some methods that specified by
 *             a interface, and has some other methods specified by other
 *             interface, and also has some methods defined and implemented by
 *             itself.
 */
@Deprecated
public interface UtilSpec {

}
