package roomescape.theme.dto;

import roomescape.theme.entity.Theme;

public record ThemeResponse(
        long id,
        String name,
        String description,
        String thumbnail
) {

    public static ThemeResponse from(final Theme theme) {
        return new ThemeResponse(theme.getId(), theme.getName(), theme.getDescription(), theme.getThumbnail());
    }
}
