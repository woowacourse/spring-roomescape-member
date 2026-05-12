package roomescape.controller.dto.theme;

import roomescape.service.dto.theme.ThemeRankingCondition;

public record ThemeRankingQuery(
        int days,
        int limit
) {

    public ThemeRankingCondition toCondition() {
        return new ThemeRankingCondition(days, limit);
    }
}
