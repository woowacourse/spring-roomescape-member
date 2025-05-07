package roomescape.presentation.dto.response;

import roomescape.business.model.entity.Theme;

import java.util.List;

public record ThemeResponse(
        String id,
        String name,
        String description,
        String thumbnail
) {
    public static List<ThemeResponse> from(List<Theme> theme) {
        return theme.stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public static ThemeResponse from(Theme theme) {
        return new ThemeResponse(theme.id(), theme.name(), theme.description(), theme.thumbnail());
    }
}
