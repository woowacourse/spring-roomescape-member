package roomescape.controller.dto.theme;

import java.util.List;

public record ThemeRankResponses(
        List<ThemeRankResponse> themeRankings
) {
    public ThemeRankResponses {
        themeRankings = List.copyOf(themeRankings);
    }
}
