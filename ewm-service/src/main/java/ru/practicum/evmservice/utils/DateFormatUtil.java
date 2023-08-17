package ru.practicum.evmservice.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateFormatUtil {

    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static LocalDateTime formatStringToDate(String date) {
        if (date != null) {
            return LocalDateTime.parse(date, formatter);
        } else {
            return null;
        }
    }

    public static String formatDateToString(LocalDateTime dateTime) {
        return dateTime.format(formatter);
    }
}
