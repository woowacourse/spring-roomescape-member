package roomescape.dto.theme;

import roomescape.domain.Theme;

public record ThemeResponseDto(
    Long id
) {

    public static ThemeResponseDto from(Theme theme) {
        return new ThemeResponseDto(theme.getId());
    }
}
