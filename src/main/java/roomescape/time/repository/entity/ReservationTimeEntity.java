package roomescape.time.repository.entity;

import java.time.LocalTime;

public record ReservationTimeEntity(
        Long id,
        LocalTime startAt
) {

    public ReservationTimeEntity(final LocalTime startAt) {
        this(null, startAt);
    }
}
