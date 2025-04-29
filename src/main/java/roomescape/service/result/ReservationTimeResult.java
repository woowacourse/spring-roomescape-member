package roomescape.service.result;

import java.time.LocalTime;

public record ReservationTimeResult(
        Long id,
        LocalTime startAt
) {
}
