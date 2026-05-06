package roomescape.presentation.dto;

import roomescape.entity.Theme;

public record ThemeResponse(
        Long id,
        String name,
        String description,
        String thumbnail
) {
    public static ThemeResponse from(Theme theme) {
        return new ThemeResponse(
                theme.id(),
                theme.name(),
                theme.description(),
                theme.thumbnailUrl()
        );
    }
}
