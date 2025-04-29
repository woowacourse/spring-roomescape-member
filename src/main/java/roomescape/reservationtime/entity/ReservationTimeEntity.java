package roomescape.reservationtime.entity;

import java.time.LocalTime;
import roomescape.reservationtime.domain.ReservationTime;

public record ReservationTimeEntity(
        Long id,
        LocalTime startAt
) {
    public ReservationTimeEntity(final long id, final String startAt) {
        this(id, LocalTime.parse(startAt));
    }

    public ReservationTime toReservationTime() {
        return ReservationTime.of(id, startAt);
    }
}
