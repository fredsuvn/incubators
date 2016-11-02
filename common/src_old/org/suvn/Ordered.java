package com.cogician.quicker;

/**
 * This interface represents a sequential.
 *
 * @author Fred Suvn
 * @version 0.0.0, 2015-08-07 16:53:02
 * @since 0.0.0
 */
public interface Ordered {

    /**
     * Returns whether this sequential object is bidirectional.
     *
     * @return true if is it, false else
     */
    boolean isBidirectional();

    /**
     * Returns whether this object can be random accessed.
     *
     * @return true if is it, false else
     */
    boolean canRandomAccess();

}
