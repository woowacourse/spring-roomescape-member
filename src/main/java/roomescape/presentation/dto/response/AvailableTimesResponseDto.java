package roomescape.presentation.dto.response;

import java.time.LocalTime;

public record AvailableTimesResponseDto(
        long id,
        LocalTime startAt,
        boolean isBooked
) {
}
