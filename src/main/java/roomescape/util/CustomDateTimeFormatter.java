package roomescape.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class CustomDateTimeFormatter {

    private static final String TIME_FORMAT_WITHOUT_SECOND = "HH:mm";

    private CustomDateTimeFormatter() {
    }

    public static String getFormattedDate(final LocalDate date) {
        return date.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    public static String getFormattedTime(final LocalTime time) {
        return time.format(DateTimeFormatter.ofPattern(TIME_FORMAT_WITHOUT_SECOND));
    }
}
