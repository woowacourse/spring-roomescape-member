package roomescape.dto.theme;

import roomescape.domain.Theme;
import roomescape.domain.vo.ThemeImageUrl;
import roomescape.domain.vo.ThemeName;

public record PopularThemeResponseDto(
    Long id,
    int rank,
    ThemeName name,
    String description,
    ThemeImageUrl imageUrl
) {

    public static PopularThemeResponseDto from(Theme theme, final int rank) {
        return new PopularThemeResponseDto(
            theme.getId(),
            rank,
            theme.getName(),
            theme.getDescription(),
            theme.getImageUrl());
    }
}
