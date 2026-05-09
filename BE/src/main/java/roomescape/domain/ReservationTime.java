package roomescape.domain;

import java.time.LocalTime;

public record ReservationTime(
    Long id,
    LocalTime startAt
) {

    public static ReservationTime create(LocalTime startAt) {
        return new ReservationTime(null, startAt);
    }

    public static ReservationTime createRow(Long id, LocalTime startAt) {
        return new ReservationTime(id, startAt);
    }
}
