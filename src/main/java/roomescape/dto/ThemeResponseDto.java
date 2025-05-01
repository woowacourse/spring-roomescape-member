package roomescape.dto;

import roomescape.domain.Theme;

public record ThemeResponseDto(long themeId, String name, String description, String thumbnail) {

    public static ThemeResponseDto from(Theme theme) {
        return new ThemeResponseDto(
            theme.getId(),
            theme.getName(),
            theme.getDescription(),
            theme.getThumbnail()
        );
    }
}
