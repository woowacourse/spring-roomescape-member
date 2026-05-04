package roomescape.dto.Reservation;

import roomescape.domain.Reservation.Reservation;
import roomescape.dto.ReservationTime.ReservationTimeResponse;
import roomescape.dto.theme.ThemeResponse;

public record ReservationResponse(long id, String name, String date, ReservationTimeResponse time, ThemeResponse themeResponse) {
    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(reservation.id(), reservation.name(), reservation.date(), ReservationTimeResponse.from(reservation.time()), ThemeResponse.from(reservation.reservationTheme()));
    }
}
