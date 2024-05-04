package roomescape.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ReservationRequest(
        @NotBlank(message = "이름은 빈칸(공백)일 수 없습니다.") String name,
        @Pattern(regexp = "^([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))$", message = "올바르지 않은 날짜입니다.") String date,
        @NotNull long timeId,
        @NotNull long themeId) {
}
