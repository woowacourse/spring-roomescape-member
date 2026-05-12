package roomescape.service.dto.reservationtime;

import java.time.LocalTime;

public record AvailableReservationTimeResult(
        Long id,
        LocalTime startAt,
        boolean available
) {
}
