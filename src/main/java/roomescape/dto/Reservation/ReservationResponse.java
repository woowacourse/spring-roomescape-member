package roomescape.dto.Reservation;

import roomescape.domain.Reservation.Reservation;
import roomescape.dto.ReservationTime.ReservationTimeResponse;
import roomescape.dto.theme.ReservationThemeResponse;

public record ReservationResponse(long id, String name, String date, ReservationTimeResponse time, ReservationThemeResponse reservationTheme) {
    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.id(),
                reservation.name(),
                reservation.date(),
                ReservationTimeResponse.from(reservation.time()),
                ReservationThemeResponse.from(reservation.reservationTheme())
        );
    }
}
