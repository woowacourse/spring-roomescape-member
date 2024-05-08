package roomescape.application.reservation.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record AdminReservationRequest(
        @NotNull(message = "회원 ID를 입력해주세요.")
        Long memberId,
        @NotNull(message = "날짜를 입력해주세요.")
        LocalDate date,
        @NotNull(message = "시간 ID를 입력해주세요.")
        Long timeId,
        @NotNull(message = "테마 ID를 입력해주세요.")
        Long themeId) {
}
