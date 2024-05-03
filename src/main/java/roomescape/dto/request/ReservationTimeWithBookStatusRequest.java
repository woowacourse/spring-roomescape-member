package roomescape.dto.request;

import jakarta.validation.constraints.NotNull;

public record ReservationTimeWithBookStatusRequest(
        @NotNull(message = "예약 날짜 입력이 존재하지 않습니다.") String date,
        @NotNull(message = "테마 입력이 존재하지 않습니다.") Long themeId) {
}
