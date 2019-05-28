package com.code.common.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class DateUtils {
    private static final String YEARMONTHDAY_PATTERN = "yyyy-MM-dd";
    private static final String HOURMINUTESECOND_PATTERN = "yyyy-MM-dd";
    private static final String FULLTIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static String formatDateTime(LocalDateTime dateTime) {
        return formatDateTime(dateTime, YEARMONTHDAY_PATTERN);
    }

    public static String formatFullTime(LocalDateTime dateTime) {
        return formatDateTime(dateTime, FULLTIME_PATTERN);
    }

    public static String formatDateTime(LocalDateTime dateTime, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return dateTime.format(formatter);
    }

    public static LocalDateTime parseDateTime(String dateTime, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return LocalDateTime.parse(dateTime, formatter);
    }

    public static LocalDateTime parseDateTime(String dateTime) {
        return parseDateTime(dateTime, FULLTIME_PATTERN);
    }

    public static LocalDateTime parseDate(String dateTime) {
        return parseDateTime(dateTime, YEARMONTHDAY_PATTERN);
    }

    public static LocalDateTime parseTime(String dateTime) {
        return parseDateTime(dateTime, HOURMINUTESECOND_PATTERN);
    }

    public static long timestampByDateTime(LocalDateTime localDateTime) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return instant.toEpochMilli();
    }

    public static LocalDateTime dateTimeByTimeStamp(long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }

    public static long secondByDateTime(LocalDateTime localDateTime) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return instant.getEpochSecond();
    }

    public static LocalDateTime localDateByDate(Date date) {
        ZoneId zoneId = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(date.toInstant(), zoneId);
    }

    public static Date dateByLocalDate(LocalDateTime dateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        return Date.from(dateTime.atZone(zoneId).toInstant());
    }

    public static LocalDateTime dayStart(LocalDateTime time) {
        return time.withHour(0).withMinute(0).withSecond(0).withNano(0);
    }

    //获取一天的结束时间，2017,7,22 23:59:59.999999999
    public static LocalDateTime dayEnd(LocalDateTime time) {
        return time.withHour(23).withMinute(59).withSecond(59).withNano(999999999);
    }

    public static long diffDateTime(LocalDateTime startTime, LocalDateTime endTime, ChronoUnit field) {
        Period period = Period.between(LocalDate.from(startTime), LocalDate.from(endTime));
        if (field == ChronoUnit.YEARS) return period.getYears();
        if (field == ChronoUnit.MONTHS) return period.getYears() * 12 + period.getMonths();
        return field.between(startTime, endTime);
    }

    public static long nowTimeStamp() {
        return DateUtils.timestampByDateTime(LocalDateTime.now());
    }
}
