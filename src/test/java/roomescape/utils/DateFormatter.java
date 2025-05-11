package roomescape.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public final class DateFormatter {

    private DateFormatter() {
    }

    public static String format(final LocalDate date) {
        final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return dateTimeFormatter.format(date);
    }
}
