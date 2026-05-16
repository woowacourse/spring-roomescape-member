package roomescape.reservation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

public record ReservationUpdateRequest(
        @NotBlank(message = "이름은 비어있을 수 없습니다.")
        String name,

        @NotNull(message = "날짜는 필수 입력 항목입니다.")
        LocalDate date,

        @NotNull(message = "시간 ID는 필수 입력 항목입니다.")
        @Positive(message = "시간 ID는 양수여야 합니다.")
        Long timeId
) {
}
