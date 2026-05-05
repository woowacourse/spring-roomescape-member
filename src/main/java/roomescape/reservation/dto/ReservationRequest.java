package roomescape.reservation.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ReservationRequest(
        @NotNull(message = "스케줄 ID는 필수입니다.")
        @Positive(message = "올바른 스케줄 ID 형식이 아닙니다.")
        Long scheduleId,

        @NotNull(message = "유저 ID는 필수입니다.")
        @Positive(message = "올바른 유저 ID 형식이 아닙니다.")
        Long userId
) {
}
