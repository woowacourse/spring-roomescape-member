package roomescape.service.dto.response;

import java.time.LocalTime;

public record ReservationTimeStatusResponse(
        Long id,
        LocalTime startAt,
        boolean reserved
) {
}
