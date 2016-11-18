package com.cogician.quicker;

/**
 * <p>
 * A builder interface to build target object in the Builder design pattern like:
 * 
 * <pre>
 * Target t = new Builder().appandSomething(something).build();
 * </pre>
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-05-25T11:21:22+08:00
 * @since 0.0.0, 2016-05-25T11:21:22+08:00
 */
@FunctionalInterface
public interface Buildable<T> {

    /**
     * <p>
     * Builds target object.
     * </p>
     * 
     * @return builded object
     * @since 0.0.0
     */
    public T build();
}
