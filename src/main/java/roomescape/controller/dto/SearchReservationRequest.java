package roomescape.controller.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record SearchReservationRequest(
        @NotNull(message = "테마를 선택해주세요.")
        Long themeId,

        @NotNull(message = "예약자를 선택해주세요.")
        Long memberId,

        @NotNull(message = "시작일을 선택해주세요.")
        LocalDate dateFrom,

        @NotNull(message = "종료일을 선택해주세요.")
        LocalDate dateTo
) {
}
