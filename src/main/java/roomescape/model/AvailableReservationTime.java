package roomescape.model;

import java.time.LocalTime;

public record AvailableReservationTime(
        Long id,
        LocalTime statAt,
        boolean alreadyBooked
) {
}
