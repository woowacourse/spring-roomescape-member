package roomescape.theme.payload;

import roomescape.theme.entity.Theme;

public record ThemeResponse(
        Long id,
        String name,
        String description,
        String thumbnailUrl,
        Long runtime
) {

    public static ThemeResponse from(Theme theme) {
        return new ThemeResponse(
                theme.getId(),
                theme.getName(),
                theme.getDescription(),
                theme.getThumbnailUrl(),
                theme.getRuntime().toMinutes()
        );
    }

}
