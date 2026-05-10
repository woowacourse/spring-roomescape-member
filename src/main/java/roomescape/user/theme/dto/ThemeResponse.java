package roomescape.user.theme.dto;

import roomescape.user.theme.Theme;

public record ThemeResponse(
    Long id,
    String name,
    String description,
    String imageUrl
) {

    public static ThemeResponse of(Theme theme) {
        return new ThemeResponse(
            theme.getId(),
            theme.getName(),
            theme.getDescription(),
            theme.getImageUrl()
        );
    }
}
