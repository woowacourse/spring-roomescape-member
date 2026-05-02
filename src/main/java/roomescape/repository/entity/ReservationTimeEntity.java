package roomescape.repository.entity;

import java.time.LocalTime;

public record ReservationTimeEntity(
    Long id,
    LocalTime startAt
) {
}
