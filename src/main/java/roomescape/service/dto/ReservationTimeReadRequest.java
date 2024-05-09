package roomescape.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ReservationTimeReadRequest(
        @NotBlank(message = "올바르지 않은 날짜입니다.") String date,
        @NotNull long themeId
) {
}
