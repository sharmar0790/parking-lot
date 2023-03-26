package com.parking.constants;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public final class ApplicationConstants {
    public static final String SLASH = "/";
    public static final String V1 = "/v1";
    public static final String PARKING_EP = "/parking";
    public static final String AVAILABLE_SLOTS_EP = "/availableSlots";
    public static final String PARK_EP = "/park";
    public static final String UN_PARK_EP = "/un_park";
    public static final String CARS = "CARS";
    public static final String BUSES = "BUSES";
    public static final String MOTORCYCLES = "MOTORCYCLES";
    public static final String R_STR = "R-";
    public static final String SLOT_STR = "SLOT-";
    public static final String UNDERSCORE_STR = "_";
    public static final int NUMBER_3600 = 3600;
    public static final int NUMBER_60 = 60;
    public static final int NUMBER_24 = 24;
    public static final String DD_MMM_YYYY_KK_MM_SS = "dd-MMM-yyyy kk:mm:ss";//pattern to format the date in 24 hour format
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(
            DD_MMM_YYYY_KK_MM_SS);//29-May-2022 14:44:07
    public static final List<String> ACCEPTABLE_VEHICLE_TYPE = new ArrayList<>() {
        {
            add(CARS);
            add(BUSES);
            add(MOTORCYCLES);
        }
    };

    private ApplicationConstants() {
    }
}
