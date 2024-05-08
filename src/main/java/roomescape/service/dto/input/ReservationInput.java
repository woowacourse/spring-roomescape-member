package roomescape.service.dto.input;

import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationTime;
import roomescape.domain.reservation.Theme;

public record ReservationInput(String name, String date, Long timeId, Long themeId) {

    public Reservation toReservation(ReservationTime time, Theme theme) {
        return Reservation.from(null, name, date, time, theme);
    }
}
