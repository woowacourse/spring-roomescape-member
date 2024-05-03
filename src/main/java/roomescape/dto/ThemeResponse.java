package roomescape.dto;

import roomescape.domain.theme.Theme;

public record ThemeResponse(
        Long id,
        String name,
        String description,
        String thumbnail
) {

    public static ThemeResponse from(Theme theme) {
        return new ThemeResponse(
                theme.getId(),
                theme.getName().name(),
                theme.getDescription().description(),
                theme.getThumbnail().thumbnail()
        );
    }
}
