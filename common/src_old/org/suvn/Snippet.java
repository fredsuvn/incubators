package com.cogician.quicker;

/**
 * A functional interface represents a piece of code, can be run.
 *
 * @author Fred Suvn
 * @version 0.0.0, 2015-07-26 18:55:11
 * @since 0.0.0
 */
@FunctionalInterface
public interface Snippet {
    /**
     * Runs the piece of code. This method return a status code when running
     * finished, commonly, a nonzero status code indicates a abnormal
     * termination.
     *
     * @return status code
     */
    public int run();
}
