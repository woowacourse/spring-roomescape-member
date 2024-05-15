package roomescape.reservation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ReservationRequest(
        DateRequest date,

        @NotNull(message = "시간을 입력해 주세요.")
        Long timeId,

        @NotNull(message = "테마를 입력해 주세요.")
        Long themeId
) {
}
