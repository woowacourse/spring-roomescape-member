package roomescape.controller.dto;

import java.time.LocalTime;
import java.util.UUID;

public record ReservationTimeResponse(
        UUID id,
        LocalTime startAt
) {
}
