package roomescape.dto;

import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

import java.time.LocalDate;

public record ReservationSaveRequest(
        String name,
        LocalDate date,
        Long timeId) {

    public Reservation toModel() {
        return new Reservation(name, date, new ReservationTime(timeId));
    }
}
