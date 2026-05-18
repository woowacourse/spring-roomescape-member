package roomescape.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ReservationRequest(
        @NotBlank(message = "예약자 이름은 비어 있을 수 없습니다.")
        String name,

        LocalDate date,

        @NotNull(message = "시간 ID는 필수입니다.")
        Long timeId,

        @NotNull(message = "테마 ID는 필수입니다.")
        Long themeId
) {
}
