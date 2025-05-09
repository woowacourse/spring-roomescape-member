package roomescape.presentation.response;

import java.time.LocalDate;
import roomescape.service.result.ReservationResult;

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
