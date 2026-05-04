package roomescape.dto.theme;

import roomescape.domain.Theme;

public record ThemeRequestDto(
    String name,
    String description,
    String imageUrl
) {

    public static ThemeRequestDto from(Theme theme) {
        return new ThemeRequestDto(
            theme.getName(),
            theme.getDescription(),
            theme.getImageUrl()
        );
    }
}
