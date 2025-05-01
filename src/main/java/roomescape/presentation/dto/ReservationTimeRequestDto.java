package roomescape.presentation.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

public record ReservationTimeRequestDto(
        @NotNull LocalTime startAt
) {
}
