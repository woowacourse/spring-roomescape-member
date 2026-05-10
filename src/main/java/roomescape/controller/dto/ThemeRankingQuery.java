package roomescape.controller.dto;

import roomescape.global.exception.InvalidRankingConditionException;

public record ThemeRankingQuery(
        int days,
        int limit
) {

    private static final int MIN_RANKING_DAYS = 1;
    private static final int MAX_RANKING_DAYS = 180;
    private static final int MIN_RANKING_LIMIT = 1;
    private static final int MAX_RANKING_LIMIT = 50;

    public ThemeRankingQuery {
        validateDays(days);
        validateLimit(limit);
    }

    private static void validateDays(int days) {
        if (days < MIN_RANKING_DAYS) {
            throw new InvalidRankingConditionException("조회 기간은 " + MIN_RANKING_DAYS + "일 이상이어야 합니다.");
        }
        if (days > MAX_RANKING_DAYS) {
            throw new InvalidRankingConditionException("조회 기간은 " + MAX_RANKING_DAYS + "일을 넘을 수 없습니다.");
        }
    }

    private static void validateLimit(int limit) {
        if (limit < MIN_RANKING_LIMIT) {
            throw new InvalidRankingConditionException("조회 개수는 " + MIN_RANKING_LIMIT + "개 이상이어야 합니다.");
        }
        if (limit > MAX_RANKING_LIMIT) {
            throw new InvalidRankingConditionException("조회 개수는 " + MAX_RANKING_LIMIT + "개를 넘을 수 없습니다.");
        }
    }
}
