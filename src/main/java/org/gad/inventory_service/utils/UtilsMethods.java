package org.gad.inventory_service.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static org.gad.inventory_service.utils.Constants.PATTERN;


public class UtilsMethods {
    private UtilsMethods() {
    }

    public static String datetimeNowFormatted() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN);
        return LocalDateTime.now().format(formatter);
    }

    public static String localDateTimeFormatted(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN);
        return localDateTime.format(formatter);
    }

    public static UUID generateUUID() {
        return UUID.randomUUID();
    }

    public static UUID convertStringToUUID(String uuid) {
        return UUID.fromString(uuid);
    }

    public static String convertUUIDToString(UUID uuid) {
        return uuid.toString();
    }

    public static BigDecimal formatPrice(BigDecimal price) {
        return price.setScale(2, RoundingMode.HALF_UP);
    }

    public static URI createUri(String path, String uuid) {
        return URI.create(String.format(path, uuid));
    }
}
