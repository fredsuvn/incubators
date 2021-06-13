package com.cogician.quicker.util;

import com.cogician.quicker.Quicker;

/**
 * <p>
 * Static quick utility class provides static methods for path.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-08-16T12:58:40+08:00
 * @since 0.0.0, 2016-08-16T12:58:40+08:00
 */
public class PathQuicker {

    /**
     * <p>
     * Returns absolute path of given path. If given path start with "/", return itself; else return user dire + "/" +
     * given path.
     * </p>
     * 
     * @param path
     *            given path
     * @return absolute path of given path
     * @since 0.0.0
     */
    public static String toAbsolutePath(String path) {
        if (Quicker.require(path).startsWith("/")) {
            return path;
        }
        return Quicker.getUserDir() + "/" + path;
    }
}
