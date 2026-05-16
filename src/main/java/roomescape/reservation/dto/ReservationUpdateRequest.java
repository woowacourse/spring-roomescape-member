package roomescape.reservation.dto;

import jakarta.validation.constraints.NotNull;

public record ReservationUpdateRequest(
        @NotNull(message = "변경할 스케줄 ID는 필수입니다.")
        Long scheduleId
) {
}