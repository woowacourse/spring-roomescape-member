package roomescape.reservation.dto.request;

import java.time.LocalDate;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;

public record ReservationRequest(String name, LocalDate date, Long timeId) {

    public Reservation toEntity(ReservationTime reservationTime) {
        return new Reservation(null, name, date, reservationTime);
    }
}
