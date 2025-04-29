package roomescape.business.dto;

import java.time.LocalTime;

public record ReservationTimeResponseDto(
        long id,
        LocalTime startAt
) {
}
