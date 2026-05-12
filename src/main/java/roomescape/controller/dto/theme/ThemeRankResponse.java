package roomescape.controller.dto.theme;

import roomescape.service.dto.theme.ThemeRankResult;

public record ThemeRankResponse(
        int rank,
        ThemeResponse theme
) {

    public static ThemeRankResponse from(ThemeRankResult result) {
        return new ThemeRankResponse(result.rank(), ThemeResponse.from(result.theme()));
    }
}
