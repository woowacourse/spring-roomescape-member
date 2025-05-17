package roomescape.presentation.admin.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record AdminReservationRequestDto(
        @NotNull(message = "사용자 ID는 필수입니다.") Long memberId,
        @NotNull(message = "예약 날짜는 필수입니다.") LocalDate date,
        @NotNull(message = "테마 ID는 필수입니다.") Long themeId,
        @NotNull(message = "예약 가능 시간 ID는 필수입니다.") Long timeId
) {
}
