package roomescape.dto.reservation;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ReservationRequest(
        @NotEmpty(message = "이름을 입력해주세요.") String name,
        @NotNull(message = "날짜를 입력해주세요.") LocalDate date,
        Long timeId,
        Long themeId
) {
}
