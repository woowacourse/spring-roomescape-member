package roomescape.presentation.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record AdminReservationRequest(
        @NotNull(message = "날짜를 입력해주세요.") LocalDate date,
        @NotNull(message = "시간을 입력해주세요.") long timeId,
        @NotNull(message = "테마를 입력해주세요.") long themeId,
        @NotNull(message = "사용자를 입력해주세요.") long memberId
) {
}
