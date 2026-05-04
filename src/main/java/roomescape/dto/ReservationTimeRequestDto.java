package roomescape.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

public record ReservationTimeRequestDto(
        @NotNull(message = "[ERROR] 시간은 비어 있을 수 없습니다.")
        LocalTime startAt
) {
}
