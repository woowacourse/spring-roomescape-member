package roomescape.dto;

import roomescape.domain.Theme;

public record PopularThemeResponse(
        Long id,
        String name,
        String description,
        String thumbnail,
        int reservationCount
) {
    public static PopularThemeResponse of(Theme theme, int reservationCount) {
        return new PopularThemeResponse(
                theme.getId(),
                theme.getName(),
                theme.getDescription(),
                theme.getThumbnail(),
                reservationCount
        );
    }
}
