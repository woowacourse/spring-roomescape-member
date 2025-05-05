package roomescape.dto;

import roomescape.entity.Theme;

public record ThemeResponse(
        long id,
        String name,
        String description,
        String thumbnail
) {

    public static ThemeResponse from(final Theme theme) {
        return new ThemeResponse(theme.id(), theme.name(), theme.description(), theme.thumbnail());
    }
}
