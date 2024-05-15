package roomescape.reservation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ReservationRequest(
        @NotBlank
        @Pattern(regexp = "^(?:(?:19|20)\\d{2})-(?:0[1-9]|1[0-2])-(?:0[1-9]|[1-2][0-9]|3[0-1])$",
        message = "날짜를 입력해 주세요.")
        String date,

        @NotNull(message = "시간을 입력해 주세요.")
        Long timeId,

        @NotNull(message = "테마를 입력해 주세요.")
        Long themeId
) {
}
