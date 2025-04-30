package roomescape.dto;

import java.util.List;
import roomescape.entity.ThemeEntity;

public record ThemeResponse(Long id, String name, String description, String thumbnail) {

    public static ThemeResponse from(ThemeEntity themeEntity) {
        return new ThemeResponse(themeEntity.id(), themeEntity.name(), themeEntity.description(),
                themeEntity.thumbnail());
    }

    public static List<ThemeResponse> from(List<ThemeEntity> themeEntities) {
        return themeEntities.stream()
                .map(ThemeResponse::from)
                .toList();
    }
}
