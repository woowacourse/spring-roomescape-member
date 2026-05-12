package roomescape.controller.dto;

import java.time.LocalTime;

public record ReservationTimeResponse(
        String id,
        LocalTime startAt
) {
}
