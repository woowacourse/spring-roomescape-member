package roomescape.theme.repository.dto;

import java.time.LocalDate;

public record GetThemeRankingsInRecentDaysParams(String startDate, String endDate, int limit) {

    public static GetThemeRankingsInRecentDaysParams of(int days, int limit) {
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(days);
        return new GetThemeRankingsInRecentDaysParams(startDate.toString(), today.toString(), limit);
    }
}
