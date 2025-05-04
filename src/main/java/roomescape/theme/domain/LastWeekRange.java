package roomescape.theme.domain;

import java.time.LocalDate;
import java.util.Objects;

public class LastWeekRange {
    private final LocalDate start;
    private final LocalDate end;

    public LastWeekRange(final LocalDate now) {
        LocalDate validatedNow = Objects.requireNonNull(now, "now는 null일 수 없습니다.");
        this.start = validatedNow.minusDays(7);
        this.end = validatedNow.minusDays(1);
    }

    public LocalDate getStart() {
        return start;
    }

    public LocalDate getEnd() {
        return end;
    }
}
