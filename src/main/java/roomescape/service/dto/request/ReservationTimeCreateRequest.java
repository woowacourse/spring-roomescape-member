package roomescape.service.dto.request;

import java.time.LocalTime;

public record ReservationTimeCreateRequest(
        LocalTime startAt
) {
}
