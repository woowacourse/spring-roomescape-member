package roomescape.domain;

import java.time.LocalDate;

public class PopularThemeSelectionCriteria {

    private static final int DAY_BEFORE_END = 1;

    private final LocalDate startDay;
    private final LocalDate endDay;

    public PopularThemeSelectionCriteria(final LocalDate startBaseDate, final int durationInDays) {
        this.startDay = calculateDate(startBaseDate, durationInDays + DAY_BEFORE_END);
        this.endDay = calculateDate(startBaseDate, DAY_BEFORE_END);
    }

    private LocalDate calculateDate(final LocalDate baseDate, final int daysBefore) {
        return baseDate.minusDays(daysBefore);
    }

    public LocalDate getStartDay() {
        return startDay;
    }

    public LocalDate getEndDay() {
        return endDay;
    }
}
