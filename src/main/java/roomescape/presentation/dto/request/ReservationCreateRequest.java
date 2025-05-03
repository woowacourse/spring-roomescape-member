package roomescape.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ReservationCreateRequest(
        @NotBlank(message = "예약자 이름은 필수입니다.") String name,
        @NotNull(message = "예약 일자는 필수입니다.") LocalDate date,
        @NotNull(message = "예약 시간은 필수입니다.") Long timeId,
        @NotNull(message = "예약 테마는 필수입니다.") Long themeId
) {
}
