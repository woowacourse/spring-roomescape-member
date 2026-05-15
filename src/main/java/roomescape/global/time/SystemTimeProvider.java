package roomescape.global.time;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class SystemTimeProvider implements TimeProvider {

    @Override
    public LocalDateTime nowDateTime() {
        return LocalDateTime.now();
    }

    @Override
    public LocalDate nowDate() {
        return LocalDate.now();
    }

    @Override
    public LocalTime nowTime() {
        return LocalTime.now();
    }
}
