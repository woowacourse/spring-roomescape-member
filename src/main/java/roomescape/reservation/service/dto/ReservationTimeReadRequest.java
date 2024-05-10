package roomescape.reservation.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReservationTimeReadRequest(
        @NotBlank(message = "올바르지 않은 날짜입니다.") String date,
        @NotNull long themeId
) {
}
