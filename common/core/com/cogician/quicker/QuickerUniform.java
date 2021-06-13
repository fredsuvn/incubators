package com.cogician.quicker;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.IllegalFormatException;
import java.util.Locale;

import com.cogician.quicker.configuration.QConfiguration;
import com.cogician.quicker.util.QuickClocks;
import com.cogician.quicker.util.reflect.QuickReflections;

/**
 * <p>
 * Static uniform utility, used to uniform default arguments in current application. These argument configured in
 * <a href="config.properties">config.properties</a>
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-05-16T17:17:52+08:00
 * @since 0.0.0, 2016-05-16T17:17:52+08:00
 */
public class QuickerUniform {

    private static final QConfiguration config = QuickerProperties.getProperties();

    private static boolean isDefault(String str) {
        return "default".equalsIgnoreCase(str);
    }

    /**
     * <p>
     * Uniformed locale.
     * </p>
     * 
     * @since 0.0.0
     */
    public static final Locale LOCALE;

    /**
     * <p>
     * Uniformed charset.
     * </p>
     * 
     * @since 0.0.0
     */
    public static final Charset CHARSET;

    /**
     * <p>
     * Formats given string with given arguments in standard.
     * </p>
     * 
     * @param str
     *            given string
     * @param args
     *            given arguments
     * @return a formatted string
     * @throws NullPointerException
     *             if given string is null
     * @throws IllegalFormatException
     *             if any problem occurs when formatting
     * @since 0.0.0
     */
    public static String format(String str, Object... args) throws NullPointerException, IllegalFormatException {
        if (str == null) {
            throw new NullPointerException("Passed uniform-formatted string is null.");
        }
        return String.format(LOCALE, str, args);
    }

    /**
     * <p>
     * Uniformed datetime format.
     * </p>
     * 
     * @since 0.0.0
     */
    public static final String DATETIME_FORMAT;

    /**
     * <p>
     * Uniformed charset.
     * </p>
     * 
     * @since 0.0.0
     */
    public static final DateTimeFormatter DATETIME_FORMATTER;

    /**
     * <p>
     * Uniformed zone offset.
     * </p>
     * 
     * @since 0.0.0
     */
    public static final ZoneOffset ZONE_OFFSET;

    /**
     * <p>
     * -1, a common number represents a invalid info, flag or returned value.
     * </p>
     * 
     * @since 0.0.0
     */
    public static final int INVALID_CODE = -1;

    /**
     * <p>
     * -1, {@linkplain #INVALID_CODE} in big integer.
     * </p>
     * 
     * @since 0.0.0
     */
    public static final BigInteger INVALID_CODE_BIG_INT = new BigInteger("-1");

    /**
     * <p>
     * -1, {@linkplain #INVALID_CODE} in big decimal.
     * </p>
     * 
     * @since 0.0.0
     */
    public static final BigDecimal INVALID_CODE_BIG_DEC = new BigDecimal("-1");

    /**
     * <p>
     * Returns whether given code is equal to {@linkplain #INVALID_CODE}.
     * </p>
     * 
     * @param code
     *            given code
     * @return whether given code is equal to {@linkplain #INVALID_CODE}
     * @since 0.0.0
     */
    public static boolean isInvalid(int code) {
        return INVALID_CODE == code;
    }

    /**
     * <p>
     * Uniformed path separator, ":" on UNIX.
     * </p>
     * 
     * @since 0.0.0
     */
    public static final String PATH_SEPARATOR;

    /**
     * <p>
     * Uniformed file separator, "/" on UNIX.
     * </p>
     * 
     * @since 0.0.0
     */
    public static final String FILE_SEPARATOR;

    /**
     * <p>
     * Uniformed line separator, "\r\n" on Windows, "\n" on linux.
     * </p>
     * 
     * @since 0.0.0
     */
    public static final String LINE_SEPARATOR;

    static {
        String locale = config.getString("locale");
        if (isDefault(locale)) {
            LOCALE = Locale.getDefault();
        } else {
            LOCALE = (Locale)QuickReflections.getValue(Locale.class, locale);
        }

        String charset = config.getString("charset");
        if (isDefault(charset)) {
            CHARSET = Charset.defaultCharset();
        } else {
            CHARSET = Charset.forName(charset);
        }

        String datetimeFormat = config.getString("datetime.format");
        DATETIME_FORMAT = datetimeFormat;
        DATETIME_FORMATTER = DateTimeFormatter.ofPattern(datetimeFormat);

        String zoneOffset = config.getString("zone.offset");
        if (isDefault(zoneOffset)) {
            ZONE_OFFSET = QuickClocks.defaultOffset();
        } else {
            ZONE_OFFSET = ZoneOffset.ofTotalSeconds(Quicker.calculate(zoneOffset).intValue());
        }

        String pathSeparator = config.getString("separator.path");
        if (isDefault(pathSeparator)) {
            PATH_SEPARATOR = File.pathSeparator;
        } else {
            PATH_SEPARATOR = pathSeparator;
        }
        String fileSeparator = config.getString("separator.file");
        if (isDefault(fileSeparator)) {
            FILE_SEPARATOR = File.separator;
        } else {
            FILE_SEPARATOR = fileSeparator;
        }
        String lineSeparator = config.getString("separator.line");
        if (isDefault(lineSeparator)) {
            LINE_SEPARATOR = System.lineSeparator();
        } else {
            LINE_SEPARATOR = lineSeparator;
        }
    }
}
