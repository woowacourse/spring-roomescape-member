package roomescape.dto;

import java.util.List;
import roomescape.domain.Theme;

public record ThemeResponse(
        Long id,
        String name,
        String description,
        String thumbnail
) {

    public static List<ThemeResponse> fromThemes(final List<Theme> themes) {
        return themes.stream()
                .map(ThemeResponse::fromTheme)
                .toList();
    }

    public static ThemeResponse fromTheme(Theme theme) {
        return new ThemeResponse(theme.getId(), theme.getName(), theme.getDescription(), theme.getThumbnail());
    }
}
