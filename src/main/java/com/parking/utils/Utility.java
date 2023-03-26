package com.parking.utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static com.parking.constants.ApplicationConstants.NUMBER_24;
import static com.parking.constants.ApplicationConstants.NUMBER_60;

public final class Utility {
    private Utility() {
    }

    public static <T> boolean isNullOrEmptyList(final List<T> list) {
        return list == null || list.isEmpty();
    }

    public static int getChargedHours(final LocalDateTime entryDateTime, final LocalDateTime exitDateTime) {
        int chargedHour;
        long minutes = Duration.between(entryDateTime, exitDateTime).toMinutes();
        chargedHour = (int) minutes / NUMBER_60;
        if (minutes % NUMBER_60 != 0) {
            chargedHour++;
        }
        return chargedHour;
    }

    public static int getChargedMinutes(final LocalDateTime entryDateTime, final LocalDateTime exitDateTime) {
        return (int) Duration.between(entryDateTime, exitDateTime).toMinutes();
    }

    public static int getChargedDays(LocalDateTime entryDateTime, LocalDateTime exitDateTime) {
        int chargedDay;
        long minutes = Duration.between(entryDateTime, exitDateTime).toMinutes();
        chargedDay = (int) minutes / (NUMBER_60 * NUMBER_24);
        if (minutes % (NUMBER_60 * NUMBER_24) != 0) {
            chargedDay++;
        }
        return chargedDay;
    }
}
