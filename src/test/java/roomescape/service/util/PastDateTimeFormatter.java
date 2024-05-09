package roomescape.service.util;

import java.time.LocalDate;
import java.time.LocalTime;

public class PastDateTimeFormatter implements DateTimeFormatter {

    @Override
    public LocalDate getDate() {
        return LocalDate.parse("2024-01-02");
    }

    @Override
    public LocalTime getTime() {
        return LocalTime.parse("10:00");
    }
}
