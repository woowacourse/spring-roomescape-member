package roomescape.service.dto;

import java.time.LocalTime;

public record AvailableReservationTimeResult(
        Long id,
        LocalTime startAt,
        boolean available
) {
}
