package roomescape.util;

import roomescape.service.util.DateTimeFormatter;

import java.time.LocalDate;
import java.time.LocalTime;

public class OldDateTimeFormatter implements DateTimeFormatter {
    public LocalDate getDate() {
        return LocalDate.parse("2000-01-01");
    }

    public LocalTime getTime() {
        return LocalTime.parse("10:00");
    }
}
