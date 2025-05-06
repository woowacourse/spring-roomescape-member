package roomescape.domain.theme;

import java.time.Clock;
import java.time.LocalDate;

public class LastWeekRange {

    private static final int DAYS_BEFORE_A_WEEK = 7;
    private static final int DAYS_BEFORE_YESTERDAY = 1;

    private final LocalDate start;
    private final LocalDate end;

    public LastWeekRange(final Clock clock) {
        if (clock == null) {
            throw new NullPointerException("Clock은 null일 수 없습니다.");
        }
        LocalDate now = LocalDate.now(clock);
        this.start = now.minusDays(DAYS_BEFORE_A_WEEK);
        this.end = now.minusDays(DAYS_BEFORE_YESTERDAY);
    }

    public LocalDate getStartDate() {
        return start;
    }

    public LocalDate getEndDate() {
        return end;
    }
}
