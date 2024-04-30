package roomescape.reservation.dto;

import roomescape.reservation.domain.Reservation;
import roomescape.time.domain.ReservationTime;

import java.time.LocalTime;

public record ReservationRequestDto(String name, String date, long timeId) {
    private static final LocalTime UNIDENTIFIED_TIME = LocalTime.now();

    public Reservation toReservation() {
        return new Reservation(null, name, date, new ReservationTime(timeId, UNIDENTIFIED_TIME));
    }
}
