package roomescape.presentation.dto;

import java.time.LocalDate;
import roomescape.entity.Reservation;

public record ReservationResponse(
        Long id,
        String name,
        LocalDate date,
        ReservationTimeResponse time,
        ThemeResponse theme
) {
    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.id(),
                reservation.name(),
                reservation.date(),
                ReservationTimeResponse.from(reservation.time()),
                ThemeResponse.from(reservation.theme())
        );
    }
}
