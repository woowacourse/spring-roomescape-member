package roomescape.controller.dto.theme;

import roomescape.entity.Theme;

import java.util.List;

public record ThemeResponse(
        long id,
        String name,
        String description,
        String thumbnail
) {
    public static ThemeResponse from(Theme theme) {
        return new ThemeResponse(theme.getId(), theme.getName(), theme.getDescription(), theme.getThumbnail());
    }

    public static List<ThemeResponse> from(List<Theme> theme) {
        return theme.stream()
                .map(ThemeResponse::from)
                .toList();
    }
}
