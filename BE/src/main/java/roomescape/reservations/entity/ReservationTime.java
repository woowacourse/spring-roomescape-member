package roomescape.reservations.entity;

import java.time.LocalTime;

public record ReservationTime(
    Long id,
    LocalTime startAt
) {

    public static ReservationTime createWithNullId(LocalTime startAt) {
        return new ReservationTime(null, startAt);
    }

    public static ReservationTime createWithId(Long id, LocalTime startAt) {
        return new ReservationTime(id, startAt);
    }
}
