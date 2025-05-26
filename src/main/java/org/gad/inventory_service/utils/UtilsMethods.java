package org.gad.inventory_service.utils;

import org.gad.inventory_service.exception.InvalidDateFormatException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

import static org.gad.inventory_service.utils.Constants.MESSAGE_INVALID_DATE_FORMAT;
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
        return Objects.requireNonNullElseGet(localDateTime, () -> LocalDate.now().atStartOfDay()).format(formatter);
    }

    public static BigDecimal formatPrice(BigDecimal price) {
        if (price == null) return null;
        return price.setScale(2, RoundingMode.HALF_UP);
    }

    public static URI createUri(String path, String uuid) {
        return URI.create(String.format(path, uuid));
    }

    public static LocalDateTime parseFlexibleDateStart(String dateStr) {
        try {
            return LocalDateTime.parse(dateStr);
        } catch (DateTimeParseException e) {
            try {
                return LocalDate.parse(dateStr).atStartOfDay();
            } catch (DateTimeParseException ex) {
                throw new InvalidDateFormatException(MESSAGE_INVALID_DATE_FORMAT + dateStr);
            }
        }
    }

    public static LocalDateTime parseFlexibleDateEnd(String dateStr) {
        try {
            return LocalDateTime.parse(dateStr);
        } catch (DateTimeParseException e) {
            try {
                return LocalDate.parse(dateStr).atTime(23, 59, 59);
            } catch (DateTimeParseException ex) {
                throw new InvalidDateFormatException(MESSAGE_INVALID_DATE_FORMAT + dateStr);
            }
        }
    }
}
