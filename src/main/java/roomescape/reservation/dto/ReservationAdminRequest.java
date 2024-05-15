package roomescape.reservation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ReservationAdminRequest(
        DateRequest date,

        @NotNull(message = "선택한 테마를 입력해 주세요.")
        Long themeId,

        @NotNull(message = "선택한 시간을 입력해 주세요.")
        Long timeId,

        @NotNull(message = "선택한 사용자를 입력해 주세요.")
        Long memberId
) {
}
