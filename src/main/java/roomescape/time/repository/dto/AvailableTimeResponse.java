package roomescape.time.repository.dto;

import java.time.LocalTime;

public record AvailableTimeResponse(
        Long id,
        LocalTime startAt,
        boolean alreadyBooked
) {
}
