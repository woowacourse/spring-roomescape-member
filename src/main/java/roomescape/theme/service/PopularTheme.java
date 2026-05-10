package roomescape.theme.service;

import roomescape.theme.domain.Theme;

public record PopularTheme(
        Long id,
        String name,
        String description,
        String thumbnail,
        int rank
) {
    public static PopularTheme of(Theme theme, int rank) {
        return new PopularTheme(
                theme.getId(),
                theme.getName(),
                theme.getDescription(),
                theme.getThumbnail(),
                rank);
    }
}
