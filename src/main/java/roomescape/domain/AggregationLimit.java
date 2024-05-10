package roomescape.domain;

public class AggregationLimit {

    private static final int POPULARITY_AGGREGATION_LIMIT = 10;

    private final int limit;

    public AggregationLimit(final int limit) {
        this.limit = limit;
    }

    public static int getAggregationLimit() {
        return new AggregationLimit(POPULARITY_AGGREGATION_LIMIT).limit;
    }
}
