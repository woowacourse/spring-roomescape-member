package roomescape.utils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

public class DateTimeConverter {

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("uuuu-MM-dd")
            .withResolverStyle(ResolverStyle.STRICT);
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private DateTimeConverter() {
    }

    public static LocalDate dateConverter(String date) {
        return LocalDate.parse(date, DATE_FORMATTER);
    }

    public static LocalTime timeConverter(String time) {
        return LocalTime.parse(time, TIME_FORMATTER);
    }
}
