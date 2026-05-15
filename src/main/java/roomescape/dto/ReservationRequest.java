package roomescape.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ReservationRequest(
        @NotBlank(message = "이름은 빈 값일 수 없습니다.") String name,
        @NotNull(message = "날짜는 빈 값일 수 없습니다.") LocalDate date,
        @NotNull(message = "timeId는 빈 값일 수 없습니다.") Long timeId,
        @NotNull(message = "themeId는 빈 값일 수 없습니다.") Long themeId) {
}
