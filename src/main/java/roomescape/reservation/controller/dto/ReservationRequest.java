package roomescape.reservation.controller.dto;

import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ReservationRequest(
        @NotBlank(message = "예약자 이름은 필수입니다.")
        String name,
        @NotNull(message = "예약 날짜는 필수입니다.")
        LocalDate date,
        @Positive(message = "예약 시간 ID는 양수여야 합니다.")
        long timeId,
        @Positive(message = "테마 ID는 양수여야 합니다.")
        long themeId
) {
}
