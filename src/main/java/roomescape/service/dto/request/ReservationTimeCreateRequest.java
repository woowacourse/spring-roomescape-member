package roomescape.service.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

public record ReservationTimeCreateRequest(
        @NotNull
        LocalTime startAt,

        @NotNull
        LocalTime endAt
) {
}
