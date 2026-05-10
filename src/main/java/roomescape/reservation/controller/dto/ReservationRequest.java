package roomescape.reservation.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record ReservationRequest(
        @NotBlank(message = "이름은 비어있을 수 없습니다.")
        @Size(max = 10, message = "이름은 10글자 이하여야 합니다.")
        String name,

        @NotNull(message = "예약 날짜는 필수입니다.")
        LocalDate date,

        @NotNull(message = "예약 시간 ID는 필수입니다.")
        @Positive(message = "예약 시간 ID는 양수여야 합니다.")
        Long timeId,

        @NotNull(message = "테마 ID는 필수입니다.")
        @Positive(message = "테마 ID는 양수여야 합니다.")
        Long themeId
) {
}
