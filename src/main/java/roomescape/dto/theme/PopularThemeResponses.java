package roomescape.dto.theme;

import java.util.List;

public record PopularThemeResponses(
        List<PopularThemeResponse> themes
) {
    public static PopularThemeResponses from(List<PopularThemeResponse> themes) {
        return new PopularThemeResponses(themes);
    }
}