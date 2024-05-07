package roomescape.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import roomescape.controller.exception.valid.DateValid;

public record AdminReservationRequest(
        @NotBlank(message = "날짜는 필수 값입니다.")
        @DateValid
        String date,

        @NotNull(message = "테마 id는 필수 값입니다.")
        @Positive
        Long themeId,

        @NotNull(message = "시간 id는 필수 값입니다.")
        @Positive
        Long timeId,

        @NotNull(message = "회원 id는 필수 값입니다.")
        @Positive
        Long memberId
) {
}
