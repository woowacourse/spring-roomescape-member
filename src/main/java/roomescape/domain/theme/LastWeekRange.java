package roomescape.domain.theme;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class LastWeekRange {
    private final LocalDateTime start;
    private final LocalDateTime end;

    public LastWeekRange(final Clock clock) {
        LocalDateTime now = LocalDateTime.now(clock);
        this.start = now.minusDays(7);
        this.end = now.minusDays(1);
    }

    public LocalDate getStartDate() {
        return start.toLocalDate();
    }

    public LocalDate getEndDate() {
        return end.toLocalDate();
    }
}
