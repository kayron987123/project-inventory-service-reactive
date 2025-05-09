package org.gad.inventory_service.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UtilsMethods {
    private static final String PATTERN = "yyyy-MM-dd HH:mm:ss";
    private UtilsMethods() {
    }

    public static String datetimeNowFormatted() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN);
        return LocalDateTime.now().format(formatter);
    }
}
