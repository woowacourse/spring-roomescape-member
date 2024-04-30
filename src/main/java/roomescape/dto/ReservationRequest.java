package roomescape.dto;

import java.time.LocalDate;
import roomescape.domain.Name;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

public record ReservationRequest(String name, LocalDate date, long timeId) {

    public Reservation toReservation(ReservationTime reservationTime) {
        return new Reservation(new Name(name), date, reservationTime);
    }
}
