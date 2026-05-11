package roomescape.reservation.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ReservationRequest(
        @NotNull(message = "시간은 필수입니다.")
        LocalDateTime startAt,

        @NotNull(message = "테마 ID는 필수입니다.")
        Long themeId,

        @NotNull(message = "유저 ID는 필수입니다.")
        Long userId
) {
}
