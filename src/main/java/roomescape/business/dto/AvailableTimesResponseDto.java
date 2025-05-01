package roomescape.business.dto;

import java.time.LocalTime;

public record AvailableTimesResponseDto(
        long id,
        LocalTime startAt,
        boolean isBooked
) {
}
