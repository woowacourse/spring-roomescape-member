package roomescape.domain.reservationtime.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

public record ReservationTimeUpdateRequest(
        @NotNull(message = "시작 시간은 필수입니다.")
        LocalTime startAt
) {
}
