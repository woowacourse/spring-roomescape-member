package roomescape.dto.theme;

import roomescape.domain.Theme;
import roomescape.domain.vo.ThemeImageUrl;
import roomescape.domain.vo.ThemeName;

public record ThemeResponseDto(
    Long id,
    ThemeName name,
    String description,
    ThemeImageUrl imageUrl
) {

    public static ThemeResponseDto from(Theme theme) {
        return new ThemeResponseDto(
            theme.getId(),
            theme.getName(),
            theme.getDescription(),
            theme.getImageUrl()
        );
    }
}
