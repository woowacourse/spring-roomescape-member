package roomescape.service.dto.input;

import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

public record ReservationInput(String name, String date, Long timeId, Long themeId) {

    public Reservation toReservation(ReservationTime time, Theme theme) {
        return Reservation.from(null, name, date, time, theme);
    }
}
