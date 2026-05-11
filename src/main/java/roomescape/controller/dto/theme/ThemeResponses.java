package roomescape.controller.dto.theme;

import java.util.List;

public record ThemeResponses(
        List<ThemeResponse> themes
) {
    public ThemeResponses {
        themes = List.copyOf(themes);
    }
}
