package roomescape.presentation.dto;

import java.time.LocalTime;

public record ReservationTimeResponseDto(
        long id,
        LocalTime startAt
) {
}
