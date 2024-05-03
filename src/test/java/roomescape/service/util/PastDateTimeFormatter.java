package roomescape.service.util;

import java.time.LocalDate;
import java.time.LocalTime;

public class PastDateTimeFormatter implements DateTimeFormatter {

    @Override
    public LocalDate getDate() {
        return LocalDate.parse("1999-12-31");
    }

    @Override
    public LocalTime getTime() {
        return LocalTime.parse("11:11");
    }

}
