package roomescape.presentation.dto.response;

import java.time.LocalTime;

public record ReservationTimeResponseDto(
        long id,
        LocalTime startAt
) {
}
