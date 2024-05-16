package roomescape.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record MemberReservationRequest(
        @NotNull(message = "예약 날짜 입력이 존재하지 않습니다.") LocalDate date,
        @NotNull(message = "예약 시간 입력이 존재하지 않습니다.") Long timeId,
        @NotNull(message = "테마 입력이 존재하지 않습니다.") Long themeId) {
}
