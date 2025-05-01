package roomescape.theme.service.dto.response;

import roomescape.theme.entity.ReservationThemeEntity;

public record ReservationThemeResponse(
        Long id,
        String name,
        String description,
        String thumbnail
) {

    public static ReservationThemeResponse from(ReservationThemeEntity entity) {
        return new ReservationThemeResponse(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getThumbnail()
        );
    }
}
