package roomescape.dto.theme;

import roomescape.domain.Theme;

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
            theme.getNameValue(),
            theme.getDescription(),
            theme.getImageUrlValue());
    }
}
