package roomescape.presentation.admin.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

public record ReservationTimeRequestDto(
        @NotNull(message = "추가할 시간은 필수입니다.") LocalTime startAt
) {
}
