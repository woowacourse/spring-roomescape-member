package roomescape.controller.api.dto.response;

import roomescape.service.dto.response.ReservationThemeServiceResponse;

public record ReservationThemeResponse(
        Long id,
        String name,
        String description,
        String thumbnail
) {

    public static ReservationThemeResponse from(ReservationThemeServiceResponse response) {
        return new ReservationThemeResponse(response.id(), response.name(), response.description(),
                response.description());
    }
}
