package roomescape.controller.response;

import roomescape.service.result.ReservationResult;

import java.time.LocalDate;

public record ReservationResponse(
        Long id,
        String name,
        LocalDate date,
        ReservationTimeResponse time,
        ThemeResponse theme
) {


    public static ReservationResponse from(ReservationResult reservationResult) {
        return new ReservationResponse(
                reservationResult.id(),
                reservationResult.name(),
                reservationResult.date(),
                ReservationTimeResponse.from(reservationResult.time()),
                ThemeResponse.from(reservationResult.theme())
        );
    }
}
