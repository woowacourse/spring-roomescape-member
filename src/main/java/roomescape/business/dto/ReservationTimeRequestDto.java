package roomescape.business.dto;

import java.time.LocalTime;

public record ReservationTimeRequestDto(
        LocalTime startAt
) {
}
