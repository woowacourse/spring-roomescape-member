package roomescape.controller.dto.theme;

import roomescape.domain.Theme;
import roomescape.domain.vo.ThemeImageUrl;
import roomescape.domain.vo.ThemeName;

public record PopularThemeResponseDto(
    Long id,
    int rank,
    String name,
    String description,
    String imageUrl
) {

    public static PopularThemeResponseDto from(Theme theme, final int rank) {
        return new PopularThemeResponseDto(
            theme.getId(),
            rank,
            theme.getName().value(),
            theme.getDescription(),
            theme.getImageUrl().value());
    }
}
