package roomescape.dto;

import roomescape.domain.Theme;

public record ThemeResponseDto(Long id, String name, String description, String thumbnail) {

    public static ThemeResponseDto from(Theme theme) {
        return new ThemeResponseDto(theme.id(), theme.name(), theme.description(), theme.thumbnail());
    }
}
