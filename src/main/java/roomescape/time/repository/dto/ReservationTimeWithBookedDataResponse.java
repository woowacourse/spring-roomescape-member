package roomescape.time.repository.dto;

import java.time.LocalTime;

public record ReservationTimeWithBookedDataResponse(
        Long id,
        LocalTime startAt,
        boolean alreadyBooked
) {
}
