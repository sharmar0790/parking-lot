package com.parking.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class DateConverterUtils {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(
            "dd-MMM-yyyy HH:mm:ss");
    private static final Logger LOG = LoggerFactory.getLogger(DateConverterUtils.class);

    private DateConverterUtils() {
    }

    public static LocalDateTime convertStringToDate(final String str) {
        try {
            return LocalDateTime.parse(str, DATE_TIME_FORMATTER);
        } catch (Exception ex) {
            LOG.error("Error found while converting string to date. . .{} and ex. .{}", str, ex);
            return null;
        }
    }
}
