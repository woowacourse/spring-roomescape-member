package roomescape.presentation.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ReservationRequestDto(
        @NotEmpty(message = "예약자명은 필수입니다.") String name,
        @NotNull(message = "예약할 날짜는 필수입니다.") LocalDate date,
        @NotNull(message = "예약할 시간 ID는 필수입니다.") Long timeId,
        @NotNull(message = "예약할 테마 ID는 필수입니다.") Long themeId
) {
}
