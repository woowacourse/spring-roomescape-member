package roomescape.service.util;

import java.time.LocalDate;
import java.time.LocalTime;

public interface DateTimeFormatter {

    LocalDate getDate();

    LocalTime getTime();
}
