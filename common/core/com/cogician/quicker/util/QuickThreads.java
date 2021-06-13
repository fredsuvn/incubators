package com.cogician.quicker.util;

import javax.annotation.Nullable;

/**
 * <p>
 * Static quick utility class provides static methods for thread.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-05-23T22:45:19+08:00
 * @since 0.0.0, 2016-05-23T22:45:19+08:00
 */
public class QuickThreads {

    /**
     * <p>
     * Starts a new thread with given action. If given action is null, do nothing.
     * </p>
     * 
     * @param action
     *            given action
     * @param isDaemon
     *            whether new thread is a daemon thread
     * @since 0.0.0
     */
    public static void start(@Nullable Runnable action, boolean isDaemon) {
        if (action != null) {
            Thread t = new Thread(action);
            t.setDaemon(isDaemon);
            t.start();
        }
    }

    /**
     * <p>
     * Starts a new non-daemon thread with given action. If given action is null, do nothing.
     * </p>
     * 
     * @param action
     *            given action
     * @since 0.0.0
     */
    public static void start(@Nullable Runnable action) {
        start(action, false);
    }

    /**
     * <p>
     * Starts a new daemon thread with given action. If given action is null, do nothing.
     * </p>
     * 
     * @param action
     *            given action
     * @since 0.0.0
     */
    public static void startDaemon(@Nullable Runnable action) {
        start(action, true);
    }
}
