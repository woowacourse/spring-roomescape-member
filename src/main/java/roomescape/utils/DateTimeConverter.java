package roomescape.utils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

public class DateTimeConverter {

    private static final String DATE_FORMAT = "uuuu-MM-dd";
    private static final String TIME_FORMAT = "HH:mm";

    public static LocalDate dateConverter(String date) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(DATE_FORMAT)
                .withResolverStyle(ResolverStyle.STRICT));
    }

    public static LocalTime timeConverter(String time) {
        return LocalTime.parse(time, DateTimeFormatter.ofPattern(TIME_FORMAT));
    }
}
