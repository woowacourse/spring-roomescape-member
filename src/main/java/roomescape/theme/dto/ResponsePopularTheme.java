package roomescape.theme.dto;

import roomescape.theme.domain.Theme;

public record ResponsePopularTheme(
        Long id,
        String name,
        String description,
        String thumbnail,
        int rank
) {
    public static ResponsePopularTheme of(Theme theme, int rank) {
        return new ResponsePopularTheme(
                theme.getId(),
                theme.getName(),
                theme.getDescription(),
                theme.getThumbnail(),
                rank
        );
    }
}
