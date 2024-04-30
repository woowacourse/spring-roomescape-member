package roomescape.controller.request;

import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

import java.time.LocalDate;
import java.util.Objects;

public record ReservationRequest(
        String name,
        LocalDate date,
        Long timeId
) {
    public ReservationRequest {
        Objects.requireNonNull(name);
        Objects.requireNonNull(date);
        Objects.requireNonNull(timeId);
    }

    public Reservation toEntity(ReservationTime reservationTime) {
        return new Reservation(name, date, reservationTime);
    }
}
