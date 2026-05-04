package roomescape.domain.dto;

import java.time.LocalTime;

public record ReservationTimeCreateCommand(
        LocalTime startAt,
        LocalTime endAt
) {
}
