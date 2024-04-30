package roomescape.dto;

import java.time.LocalDate;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

public record ReservationRequest(
        String name,
        LocalDate date,
        long timeId
) {
    public Reservation toEntity(final ReservationTime reservationTime) {
        return Reservation.of(name, date, reservationTime);
    }
}
