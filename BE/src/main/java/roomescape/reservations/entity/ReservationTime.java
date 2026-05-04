package roomescape.reservations.entity;

import java.time.LocalTime;

public record ReservationTime(
    Long id,
    LocalTime startAt
) {

    public static ReservationTime of(Long id, LocalTime startAt) {
        return new ReservationTime(id, startAt);
    }
}
