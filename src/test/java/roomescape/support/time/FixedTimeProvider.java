package roomescape.support.time;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import roomescape.global.time.TimeProvider;

public class FixedTimeProvider implements TimeProvider {

    private LocalDateTime current;

    public FixedTimeProvider(final LocalDateTime current) {
        this.current = current;
    }

    @Override
    public LocalDateTime nowDateTime() {
        return current;
    }

    @Override
    public LocalDate nowDate() {
        return current.toLocalDate();
    }

    @Override
    public LocalTime nowTime() {
        return current.toLocalTime();
    }
}
