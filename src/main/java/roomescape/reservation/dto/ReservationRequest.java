package roomescape.reservation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReservationRequest(
        @NotBlank(message = "날짜는 필수입니다.")
        String date,

        @NotBlank(message = "시간은 필수입니다.")
        String time,

        @NotNull(message = "테마 ID는 필수입니다.")
        Long themeId,

        @NotNull(message = "유저 ID는 필수입니다.")
        Long userId
) {
}
