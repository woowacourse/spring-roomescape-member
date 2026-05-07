package roomescape.domain.dto;

import java.time.LocalTime;

public record ReservationTimeCreateData(
        LocalTime startAt,
        LocalTime endAt
) {
}
