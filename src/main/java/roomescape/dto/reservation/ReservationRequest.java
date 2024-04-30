package roomescape.dto.reservation;

import java.time.LocalDate;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.time.Time;

public record ReservationRequest(String name, LocalDate date, Long timeId) {

    public Reservation toReservation(Time time) {
        return new Reservation(this.name, this.date, time);
    }
}
