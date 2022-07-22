package dev.thanbv1510.eventmonitoringsamplev2.utils;

import lombok.experimental.UtilityClass;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@UtilityClass
public class DateUtils {
    public static LocalDateTime formatToLocalDateTime(String dateStr) {
        try {
            // 2022.02.18 04:14:04 +00:00
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ssXXX");
            return LocalDateTime.parse(dateStr, formatter);
        } catch (Exception e) {
            return LocalDateTime.now();
        }
    }

    public static Date formatToDate(String dateStr) {
        try {
            return new SimpleDateFormat("yyyy.MM.dd HH:mm:ssXXX")
                    .parse(dateStr);
        } catch (Exception e) {
            return new Date();
        }
    }
}
