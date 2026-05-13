package roomescape.service.dto.response;

import java.time.LocalTime;

public record ReservationTimeResponse(
        Long id,
        LocalTime startAt,
        LocalTime endAt
) {
}
