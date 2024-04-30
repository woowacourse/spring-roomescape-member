package roomescape.controller.reservation;

import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

public record ReservationRequest(String name, String date, Long timeId) {
    public Reservation toDomain() {
        return Reservation.from(
                null,
                name,
                date,
                new ReservationTime(timeId, null)
        );
    }
}
