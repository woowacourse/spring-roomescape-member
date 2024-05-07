package roomescape.controller.api.dto.response;

import roomescape.service.dto.output.ReservationOutput;

public record ReservationResponse(long id, String name, ThemeResponse theme, String date,
                                  ReservationTimeResponse time) {

    public static ReservationResponse toResponse(final ReservationOutput output) {
        return new ReservationResponse(
                output.id(),
                output.name(),
                ThemeResponse.toResponse(output.theme()),
                output.date(),
                ReservationTimeResponse.toResponse(output.time())
        );
    }
}
