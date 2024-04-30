package roomescape.dto;

import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

import java.time.LocalDate;

public record ReservationRequest(String name, LocalDate date, Long timeId) {

    public Reservation toEntity(final ReservationTime reservationTime) {
        return new Reservation(name, date, reservationTime);
    }
}
