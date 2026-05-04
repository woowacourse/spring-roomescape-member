package roomescape.dto;

import java.time.LocalDate;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

public record ReservationRequest(
        String name,
        LocalDate date,
        Long timeId
) {
    public Reservation toReservation(ReservationTime reservationTime) {
        return Reservation.createWithoutId(name, date, reservationTime);
    }
}
