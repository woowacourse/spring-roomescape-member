package roomescape.domain.theme;

public class ThemeAggregationLimit {

    private static final int POPULARITY_AGGREGATION_LIMIT = 10;

    private final int limit;

    public ThemeAggregationLimit(final int limit) {
        this.limit = limit;
    }

    public static int getAggregationLimit() {
        return new ThemeAggregationLimit(POPULARITY_AGGREGATION_LIMIT).limit;
    }
}
