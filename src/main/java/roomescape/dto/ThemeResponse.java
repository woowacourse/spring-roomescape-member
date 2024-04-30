package roomescape.dto;

import roomescape.domain.Theme;

public record ThemeResponse(
        Long id,
        String name,
        String Description,
        String thumbnail
) {

    public static ThemeResponse from(Theme theme) {
        return new ThemeResponse(
                theme.getId(),
                theme.getName().getName(),
                theme.getDescription().getDescription(),
                theme.getThumbnail().getThumbnail()
        );
    }
}
