package roomescape.controller.dto;

import java.time.LocalTime;

public record ReservationTimeResponse(
        long id,
        LocalTime startAt
) {
}
