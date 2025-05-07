package roomescape.reservationtime.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

public record ReservationTimeRequest(
        @NotNull LocalTime startAt
) {
}
