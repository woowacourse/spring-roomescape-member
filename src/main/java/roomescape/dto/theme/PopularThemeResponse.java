package roomescape.dto.theme;

import roomescape.domain.Theme;

public record PopularThemeResponse(
    Long id,
    int rank,
    String name,
    String description,
    String imageUrl
) {

    public static PopularThemeResponse from(Theme theme, final int rank) {
        return new PopularThemeResponse(
            theme.getId(),
            rank,
            theme.getNameValue(),
            theme.getDescription(),
            theme.getImageUrlValue());
    }
}
