package roomescape.controller.dto.reservation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record ReservationRequest(
        @NotBlank @Size(max = 10, message = "이름은 10자를 넘을 수 없습니다.") String name,
        @NotNull LocalDate date,
        long timeId,
        long themeId
) {
}
