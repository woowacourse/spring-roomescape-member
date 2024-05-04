package roomescape.reservation.dto;

import roomescape.reservation.domain.Reservation;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

public record ReservationRequestDto(String name, String date, long timeId, long themeId) {

    public Reservation toReservation(final ReservationTime reservationTime, final Theme theme) {
        return Reservation.of(null, name, date, reservationTime, theme);
    }
}
