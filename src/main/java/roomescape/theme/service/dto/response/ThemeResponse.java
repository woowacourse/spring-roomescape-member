package roomescape.theme.service.dto.response;

import roomescape.theme.entity.Theme;

public record ThemeResponse(
        Long id,
        String name,
        String description,
        String thumbnail
) {

    public static ThemeResponse of(Theme entity) {
        return new ThemeResponse(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getThumbnail()
        );
    }
}
