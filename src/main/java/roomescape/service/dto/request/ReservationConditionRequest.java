package roomescape.service.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public record ReservationConditionRequest(
        @NotBlank(message = "테마 ID를 입력해주세요.")
        Long themeId,
        @NotBlank(message = "멤바 ID를 입력해주세요.")
        Long memberId,
        @NotBlank(message = "시작 날짜를 입력해주세요.")
        LocalDate dateFrom,
        @NotBlank(message = "종료 날짜를 입력해주세요.")
        LocalDate dateTo
) {
}
