package roomescape.repository.entity;

import java.sql.Time;

public record ReservationTimeEntity(
        Long id,
        Time startAt,
        Time endAt
) {
}
