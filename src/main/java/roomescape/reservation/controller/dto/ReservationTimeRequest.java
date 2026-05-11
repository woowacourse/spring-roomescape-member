package roomescape.reservation.controller.dto;

import java.time.LocalTime;
import jakarta.validation.constraints.NotNull;

public record ReservationTimeRequest(
        @NotNull(message = "예약 시간은 필수입니다.")
        LocalTime startAt
) {
}
