package com.cogician.quicker;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.cogician.quicker.util.TimeQuicker;

/**
 * <p>
 * A class represents a date time point on the time-line in local time zone.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-04-27T09:45:33+08:00
 * @since 0.0.0, 2016-04-27T09:45:33+08:00
 */
@Immutable
public class DateTime {

    private OffsetDateTime time;

    /**
     * <p>
     * Constructs an instance of current date time with local zone offset.
     * </p>
     * 
     * @since 0.0.0
     */
    public DateTime() {
        this.time = OffsetDateTime.now();
    }

    /**
     * <p>
     * Constructs an instance with given offset date time.
     * </p>
     * 
     * @param time
     *            given offset date time
     * @throws NullPointerException
     *             if given offset date time is null
     * @since 0.0.0
     */
    public DateTime(OffsetDateTime time) throws NullPointerException {
        this.time = Quicker.require(time);
    }

    /**
     * <p>
     * Constructs an instance with given year, month, day of month, hour (0 - 23), minute, second, nanosecond and zone
     * offset.
     * </p>
     * 
     * @param year
     *            given year
     * @param month
     *            given month
     * @param day
     *            given day of month
     * @param hour
     *            given hour (0 - 23)
     * @param minute
     *            given minute
     * @param second
     *            given second
     * @param nano
     *            given nanosecond
     * @param offset
     *            given zone offset
     * @throws NullPointerException
     *             if given zone offset is null
     * @throws DateTimeException
     *             if any of given values is out of range
     * @since 0.0.0
     */
    public DateTime(int year, int month, int day, int hour, int minute, int second, int nano, ZoneOffset offset)
            throws NullPointerException, DateTimeException {
        this.time = OffsetDateTime.of(year, month, day, hour, minute, second, nano, Quicker.require(offset));
    }

    /**
     * <p>
     * Constructs an instance with given year, month, day of month, hour (0 - 23), minute, second, nanosecond and local
     * zone offset.
     * </p>
     * 
     * @param year
     *            given year
     * @param month
     *            given month
     * @param day
     *            given day of month
     * @param hour
     *            given hour (0 - 23)
     * @param minute
     *            given minute
     * @param second
     *            given second
     * @param nano
     *            given nanosecond
     * @throws DateTimeException
     *             if any of given values is out of range
     * @since 0.0.0
     */
    public DateTime(int year, int month, int day, int hour, int minute, int second, int nano) throws DateTimeException {
        this(year, month, day, hour, minute, second, nano, TimeQuicker.defaultOffset());
    }

    /**
     * <p>
     * Returns this date time as an instant.
     * </p>
     * 
     * @return this date time as an instant
     * @since 0.0.0
     */
    public Instant asInstant() {
        return time.toInstant();
    }

    /**
     * <p>
     * Returns this date time as a local date time.
     * </p>
     * 
     * @return this date time as a local date time
     * @since 0.0.0
     */
    public LocalDateTime asLocalDateTime() {
        return time.toLocalDateTime();
    }

    /**
     * <p>
     * Returns this date time as a zoned date time.
     * </p>
     * 
     * @return this date time as a zoned date time
     * @since 0.0.0
     */
    public ZonedDateTime asZonedDateTime() {
        return time.toZonedDateTime();
    }

    /**
     * <p>
     * Returns this date time as an offset date time.
     * </p>
     * 
     * @return this date time as an offset date time
     * @since 0.0.0
     */
    public OffsetDateTime asOffsetDateTime() {
        return time;
    }

    /**
     * <p>
     * Gets year value.
     * </p>
     * 
     * @return year value
     * @since 0.0.0
     */
    public int getYear() {
        return time.getYear();
    }

    /**
     * <p>
     * Gets month value.
     * </p>
     * 
     * @return month value
     * @since 0.0.0
     */
    public int getMonth() {
        return time.getMonthValue();
    }

    /**
     * <p>
     * Gets day of month value.
     * </p>
     * 
     * @return day of month value
     * @since 0.0.0
     */
    public int getDayOfMonth() {
        return time.getDayOfMonth();
    }

    /**
     * <p>
     * Gets hour value.
     * </p>
     * 
     * @return hour value
     * @since 0.0.0
     */
    public int getHour() {
        return time.getHour();
    }

    /**
     * <p>
     * Gets minute value.
     * </p>
     * 
     * @return minute value
     * @since 0.0.0
     */
    public int getMinute() {
        return time.getMinute();
    }

    /**
     * <p>
     * Gets second value.
     * </p>
     * 
     * @return second value
     * @since 0.0.0
     */
    public int getSecond() {
        return time.getSecond();
    }

    /**
     * <p>
     * Gets nanosecond value.
     * </p>
     * 
     * @return nanosecond value
     * @since 0.0.0
     */
    public int getNano() {
        return time.getNano();
    }

    /**
     * <p>
     * Gets day of week value.
     * </p>
     * 
     * @return day of week value
     * @since 0.0.0
     */
    public int getDayOfWeek() {
        return time.getDayOfWeek().getValue();
    }

    /**
     * <p>
     * Gets day of year value.
     * </p>
     * 
     * @return day of year value
     * @since 0.0.0
     */
    public int getDayOfYear() {
        return time.getDayOfYear();
    }

    /**
     * <p>
     * Converts this date time to the number of seconds from the epoch of 1970-01-01T00:00:00Z.
     * </p>
     * 
     * @return number of seconds from the epoch of 1970-01-01T00:00:00Z
     * @since 0.0.0
     */
    public long toEpochSecond() {
        return time.toEpochSecond();
    }

    /**
     * <p>
     * Converts this date time to the number of milliseconds from the epoch of 1970-01-01T00:00:00Z.
     * </p>
     * 
     * @return number of milliseconds from the epoch of 1970-01-01T00:00:00Z
     * @since 0.0.0
     */
    public long toEpochMillis() {
        return toEpochSecond() * 1000 + getNano() / 1000000;
    }

    /**
     * <p>
     * Converts this date time with UTC/GMT zone offset.
     * </p>
     * 
     * @return this date time with UTC/GMT zone offset
     * @since 0.0.0
     */
    public DateTime toUTC() {
        return new DateTime(time.withOffsetSameInstant(ZoneOffset.UTC));
    }

    /**
     * <p>
     * Converts this date time as string by format {@linkplain TimeQuicker#SIMPLE_FORMATTER}.
     * </p>
     * 
     * @return this date time as string by format {@linkplain TimeQuicker#SIMPLE_FORMATTER}
     * @since 0.0.0
     */
    public String toString() {
        return time.format(TimeQuicker.SIMPLE_FORMATTER);
    }

    /**
     * <p>
     * Converts this date time as string by given date time formatter.
     * </p>
     * 
     * @param formatter
     *            given date time formatter
     * @return this date time as string by given date time formatter
     * @throws NullPointerException
     *             if given date time formatter is null
     * @since 0.0.0
     */
    public String toString(DateTimeFormatter formatter) throws NullPointerException {
        return time.format(Quicker.require(formatter));
    }

    /**
     * <p>
     * Converts this date time as string by given date time pattern.
     * </p>
     * 
     * @param pattern
     *            given date time pattern
     * @return this date time as string by given date time pattern
     * @throws NullPointerException
     *             if given date time pattern is null
     * @since 0.0.0
     */
    public String toString(String pattern) throws NullPointerException {
        return time.format(DateTimeFormatter.ofPattern(Quicker.nonnull(pattern), Locale.US));
    }

    /**
     * <p>
     * Returns whether this date time is equals to given date time.
     * </p>
     * 
     * @param obj
     *            given date time
     * @return whether this date time is equals to given date time
     * @since 0.0.0
     */
    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof DateTime) {
            DateTime given = (DateTime)obj;
            return time.equals(given.time);
        }
        return false;
    }

    /**
     * <p>
     * Returns hash code of this date time.
     * </p>
     * 
     * @return hash code of this date time
     * @since 0.0.0
     */
    @Override
    public int hashCode() {
        return time.hashCode();
    }
}
