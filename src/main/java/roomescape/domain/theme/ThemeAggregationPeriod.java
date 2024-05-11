package roomescape.domain.theme;

import java.time.LocalDate;

public class ThemeAggregationPeriod {

    private static final int POPULARITY_AGGREGATION_PERIOD = 7;

    private final LocalDate period;

    public ThemeAggregationPeriod(final LocalDate period) {
        this.period = period;
    }

    public static LocalDate calculateAggregationPeriod(final LocalDate date) {
        return new ThemeAggregationPeriod(date.minusDays(POPULARITY_AGGREGATION_PERIOD)).period;
    }
}
