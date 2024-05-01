package roomescape.dto;

import java.time.LocalDate;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

public record ReservationRequest(String name, LocalDate date, Long timeId) {

    public Reservation toReservation(ReservationTime time) {
        return new Reservation(name, date, time);
    }
}
