package roomescape.theme.domain;

import java.time.LocalDate;

public class LastWeekRange {
    private final LocalDate start;
    private final LocalDate end;

    public LastWeekRange(final LocalDate now) {
        this.start = now.minusDays(7);
        this.end = now.minusDays(1);
    }

    public LocalDate getStart() {
        return start;
    }

    public LocalDate getEnd() {
        return end;
    }
}
