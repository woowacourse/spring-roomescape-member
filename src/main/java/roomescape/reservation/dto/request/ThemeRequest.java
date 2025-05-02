package roomescape.reservation.dto.request;

import roomescape.reservation.entity.Theme;

public record ThemeRequest(
        String name,
        String description,
        String thumbnail
) {

    public ThemeRequest {
        if (name == null || description == null || thumbnail == null) {
            throw new IllegalArgumentException("값이 입력되지 않았습니다.");
        }
    }

    public Theme toEntity() {
        return new Theme(0L, name, description, thumbnail);
    }
}
