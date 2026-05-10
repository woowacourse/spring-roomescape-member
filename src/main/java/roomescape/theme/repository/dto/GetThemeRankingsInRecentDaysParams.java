package roomescape.theme.repository.dto;

import java.time.LocalDate;
import roomescape.global.exception.InvalidRankingConditionException;

public record GetThemeRankingsInRecentDaysParams(LocalDate startDate, LocalDate endDate, int limit) {

    public static GetThemeRankingsInRecentDaysParams of(int days, int limit) {
        if (days < 1) {
            throw new InvalidRankingConditionException("조회 기간은 1일 이상이어야 합니다.");
        }
        if (limit < 1) {
            throw new InvalidRankingConditionException("조회 개수는 1개 이상이어야 합니다.");
        }
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(days);
        return new GetThemeRankingsInRecentDaysParams(startDate, today, limit);
    }
}
