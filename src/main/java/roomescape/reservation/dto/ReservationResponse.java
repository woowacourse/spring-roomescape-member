package roomescape.reservation.dto;

import java.time.LocalDate;
import roomescape.reservation.domain.Reservation;

public record ReservationResponse(
        long id,
        LocalDate date,
        ReservationTimeResponse time,
        ThemeResponse theme) {
    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getDate(),
                ReservationTimeResponse.from(reservation.getTime()),
                ThemeResponse.from(reservation.getTheme())
        );
    }
}
