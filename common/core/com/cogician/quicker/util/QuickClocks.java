package com.cogician.quicker.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

import com.cogician.quicker.Quicker;

/**
 * <p>
 * Static quick utility class provides simple methods for date time.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-04-29T14:02:38+08:00
 * @since 0.0.0, 2016-04-29T14:02:38+08:00
 */
public class QuickClocks {

    /**
     * <p>
     * Simple and humanism date time pattern, uuuu-MM-dd HH:mm:ss Z.
     * </p>
     * 
     * @since 0.0.0
     */
    public static final String HUMANISM_PATTERN = "uuuu-MM-dd HH:mm:ss Z";

    /**
     * <p>
     * Simple and humanism date time formatter, uuuu-MM-dd HH:mm:ss Z.
     * </p>
     * 
     * @since 0.0.0
     */
    public static final DateTimeFormatter HUMANISM_FORMATTER = DateTimeFormatter.ofPattern(HUMANISM_PATTERN);

    /**
     * <p>
     * Returns system default local zone offset.
     * </p>
     * 
     * @return system default local zone offset
     * @since 0.0.0
     */
    public static ZoneOffset defaultOffset() {
//        return ZoneOffset.systemDefault().getRules().getOffset(instant)
//        TimeZone.getDefault().toZoneId();
//        ZoneOffset.systemDefault();
        return ZoneOffset.systemDefault().getRules().getOffset(Instant.now());
    }

    /**
     * <p>
     * Returns local zone offset of specified instant.
     * </p>
     * 
     * @param instant
     *            specified instant
     * @return local zone offset of specified instant
     * @throws NullPointerException
     *             if specified instant is null
     * @since 0.0.0
     */
    public static ZoneOffset getOffset(Instant instant) throws NullPointerException {
        return ZoneId.systemDefault().getRules().getOffset(Quicker.require(instant));
    }

    /**
     * <p>
     * Returns local zone offset of specified local date time.
     * </p>
     * 
     * @param local
     *            specified local date time
     * @return local zone offset of specified local date time
     * @throws NullPointerException
     *             if specified local date time is null
     * @since 0.0.0
     */
    public static ZoneOffset getOffset(LocalDateTime local) throws NullPointerException {
        return ZoneId.systemDefault().getRules().getOffset(Quicker.require(local));
    }

    /**
     * <p>
     * Returns a string of now date time of simple format as {@linkplain #HUMANISM_PATTERN}.
     * </p>
     * 
     * @return a string of now date time of simple format as {@linkplain #HUMANISM_PATTERN}
     * @since 0.0.0
     */
    public static String now() {
        return OffsetDateTime.now().format(HUMANISM_FORMATTER);
    }
}
