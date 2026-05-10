package roomescape.domain.reservationtime.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

public record ReservationTimeCreateRequest(
        @NotNull(message = "startAt은 null일 수 없습니다.")
        LocalTime startAt
) {
}
