package roomescape.reservation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ReservationRequest(
        @NotNull(message = "스케줄 ID는 필수입니다.")
        @Positive(message = "스케줄 ID는 양수여야 합니다.")
        Long scheduleId,

        @NotBlank(message = "이름은 필수입니다.")
        String name
) {
}
