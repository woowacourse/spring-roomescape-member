package roomescape.theme.service.dto.response;

import roomescape.theme.entity.ThemeEntity;

public record ThemeResponse(
        Long id,
        String name,
        String description,
        String thumbnail
) {

    public static ThemeResponse from(ThemeEntity entity) {
        return new ThemeResponse(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getThumbnail()
        );
    }
}
