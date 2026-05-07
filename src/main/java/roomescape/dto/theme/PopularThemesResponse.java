package roomescape.dto.theme;

import java.util.List;
import java.util.stream.IntStream;
import roomescape.domain.Theme;

public record PopularThemesResponse(
    List<PopularThemeResponse> themes
) {

    public static PopularThemesResponse from(List<Theme> themes) {
        List<PopularThemeResponse> ranked = IntStream.range(0, themes.size())
            .mapToObj(i -> PopularThemeResponse.from(themes.get(i), i + 1))
            .toList();
        return new PopularThemesResponse(ranked);
    }
}
