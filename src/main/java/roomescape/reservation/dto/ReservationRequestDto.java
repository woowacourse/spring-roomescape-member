package roomescape.reservation.dto;

import roomescape.reservation.domain.Reservation;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

import java.time.LocalTime;

public record ReservationRequestDto(String name, String date, long timeId, long themeId) {

    public Reservation toReservation() {
        return new Reservation(null, name, date,
                new ReservationTime(timeId, LocalTime.MIN.toString()),
                new Theme(themeId, null, null, null));
    }
}
