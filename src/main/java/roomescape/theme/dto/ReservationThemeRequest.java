package roomescape.theme.dto;

import roomescape.theme.entity.ReservationThemeEntity;

public record ReservationThemeRequest(String name, String description, String thumbnail) {
    public ReservationThemeRequest {
        if (name == null || description == null || thumbnail == null) {
            throw new IllegalArgumentException("값이 입력되지 않았습니다.");
        }
    }

    public ReservationThemeEntity toEntity() {
        return new ReservationThemeEntity(0L, name, description, thumbnail);
    }
}
