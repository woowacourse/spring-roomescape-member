package roomescape.domain.policy;

import java.time.LocalDate;

public class PopularThemeCriteria {

    private final int periodDays;
    private final int limit;

    public PopularThemeCriteria(int periodDays, int limit) {
        if (periodDays <= 0) {
            throw new IllegalArgumentException("집계 기간은 1일 이상이어야 합니다: " + periodDays);
        }
        if (limit <= 0) {
            throw new IllegalArgumentException("조회 개수는 1개 이상이어야 합니다: " + limit);
        }
        this.periodDays = periodDays;
        this.limit = limit;
    }

    public static PopularThemeCriteria recentWeekTop10() {
        return new PopularThemeCriteria(7, 10);
    }

    public LocalDate from(LocalDate today) {
        return today.minusDays(periodDays);
    }

    public LocalDate to(LocalDate today) {
        return today;
    }

    public int getLimit() {
        return limit;
    }
}
