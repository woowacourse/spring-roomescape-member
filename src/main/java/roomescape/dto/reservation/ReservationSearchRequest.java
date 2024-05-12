package roomescape.dto.reservation;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ReservationSearchRequest(
        @NotNull Long themeId,
        @NotNull Long memberId,
        @NotNull(message = "시작 날짜를 입력해주세요.") LocalDate dateFrom,
        @NotNull(message = "종료 날짜를 입력해주세요.") LocalDate dateTo
) {
}
