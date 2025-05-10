package roomescape.theme.dto;

import roomescape.theme.domain.Theme;

public record ThemeCreateResponse(
        Long id,
        String name,
        String description,
        String thumbnail
) {

    public static ThemeCreateResponse from(Theme theme) {
        return new ThemeCreateResponse(theme.getId(), theme.getName(), theme.getDescription(), theme.getThumbnail());
    }
}
