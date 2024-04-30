package roomescape.controller.reservation;

import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

public record ReservationRequest(String name, String date, Long timeId) {
    public Reservation toDomain(ReservationTime time) {
        return Reservation.from(
                null,
                name,
                date,
                time
        );
    }
}
