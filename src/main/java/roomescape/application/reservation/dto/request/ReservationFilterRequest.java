package roomescape.application.reservation.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ReservationFilterRequest(
        @NotNull(message = "사용자 ID를 입력해주세요.")
        Long memberId,
        @NotNull(message = "테마 ID를 입력해주세요.")
        Long themeId,
        @NotNull(message = "구간 시작 날짜를 입력해주세요.")
        LocalDate startDate,
        @NotNull(message = "구간 종료 날짜를 입력해주세요.")
        LocalDate endDate) {
}
