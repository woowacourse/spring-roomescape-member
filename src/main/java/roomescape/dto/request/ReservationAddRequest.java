package roomescape.dto.request;

import java.time.LocalDate;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

public record ReservationAddRequest(String name, LocalDate date, Long timeId) {

    public Reservation toReservation() {
        return new Reservation(name, date, new ReservationTime(timeId));
    }
}
