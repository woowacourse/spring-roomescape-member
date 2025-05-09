package roomescape.reservation.controller.dto.response;

import roomescape.reservation.application.dto.response.ReservationThemeServiceResponse;

public record ReservationThemeResponse(
        Long id,
        String name,
        String description,
        String thumbnail
) {

    public static ReservationThemeResponse from(ReservationThemeServiceResponse response) {
        return new ReservationThemeResponse(response.id(), response.name(), response.description(),
                response.thumbnail());
    }
}
