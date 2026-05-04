package roomescape.dto;

import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

public record ReservationRequest(
        String name,
        String date,
        Long timeId
) {
    public Reservation toReservation(ReservationTime reservationTime) {
        return Reservation.createWithoutId(name, date, reservationTime);
    }
}
