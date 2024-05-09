package roomescape.service.util;

import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.stereotype.Component;

@Component
public class NowDateTimeFormatter implements DateTimeFormatter {

    public LocalDate getDate() {
        return LocalDate.now();
    }

    public LocalTime getTime() {
        return LocalTime.now();
    }
}
