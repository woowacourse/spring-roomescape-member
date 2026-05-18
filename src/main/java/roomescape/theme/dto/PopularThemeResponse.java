package roomescape.theme.dto;

import roomescape.theme.domain.Theme;

public record PopularThemeResponse(
        Long id,
        String name,
        String description,
        String thumbnail,
        int rank
) {
    public static PopularThemeResponse of(Theme theme, int rank) {
        return new PopularThemeResponse(
                theme.getId(),
                theme.getName(),
                theme.getDescription(),
                theme.getThumbnail(),
                rank
        );
    }
}
