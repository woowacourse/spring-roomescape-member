package roomescape.dto.theme;

import roomescape.entity.ThemeEntity;

public record PopularThemeResponse(String name, String description, String thumbnail) {
    public static PopularThemeResponse of(ThemeEntity entity) {
        return new PopularThemeResponse(entity.name(), entity.description(), entity.thumbnail());
    }
}
