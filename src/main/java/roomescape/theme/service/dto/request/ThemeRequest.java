package roomescape.theme.service.dto.request;

import roomescape.theme.entity.ThemeEntity;

public record ThemeRequest(String name, String description, String thumbnail) {
    public ThemeRequest {
        if (name == null || description == null || thumbnail == null) {
            throw new IllegalArgumentException("값이 입력되지 않았습니다.");
        }
    }

    public ThemeEntity toEntity() {
        return new ThemeEntity(0L, name, description, thumbnail);
    }
}
