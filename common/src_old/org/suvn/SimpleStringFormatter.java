package com.cogician.quicker;

/**
 * A simple and concise string formatter interface. Detail format action and
 * pattern are depended on implementations.
 *
 * @author Fred Suvn
 * @version 0.0.0, 2015-10-08 17:18:02
 * @since 0.0.0
 */
public interface SimpleStringFormatter {

    /**
     * Returns a formatted string using the specified format string, and
     * arguments. Detail format action and pattern are depended on
     * implementations.
     *
     * @param format
     *            format string, not null
     * @param args
     *            Arguments referenced by the format specifiers in the format
     *            string.
     * @return formatted string
     * @throws NullPointerException
     *             if format string is null
     */
    public String format(String format, Object... args);
}
