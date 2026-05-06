package roomescape.dto.response;

import java.time.LocalTime;

public record AvailableTimeResponseDto(
        Long id,
        LocalTime startAt,
        boolean alreadyBooked
) {
}
