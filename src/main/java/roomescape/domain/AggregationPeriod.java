package roomescape.domain;

import java.time.LocalDate;

public class AggregationPeriod {

    private static final int POPULARITY_AGGREGATION_PERIOD = 7;

    private final LocalDate period;

    public AggregationPeriod(final LocalDate period) {
        this.period = period;
    }

    public static LocalDate calculateAggregationPeriod(final LocalDate date) {
        return new AggregationPeriod(date.minusDays(POPULARITY_AGGREGATION_PERIOD)).period;
    }
}
