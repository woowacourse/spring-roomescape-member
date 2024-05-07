package roomescape.reservation.dto;

import java.time.LocalTime;

public record AvailableTimeResponse(
        Long id,
        LocalTime startAt,
        Boolean alreadyBooked
) {
}
