package roomescape.dto;

import roomescape.entity.Theme;

public record PopularThemeResponse(
        String name,
        String description,
        String thumbnail
) {

    public static PopularThemeResponse from(Theme theme) {
        return new PopularThemeResponse(theme.name(), theme.description(), theme.thumbnail());
    }
}
