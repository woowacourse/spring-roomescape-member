package roomescape.presentation.member.dto;

import java.time.LocalTime;

public record AvailableTimesResponseDto(
        long id,
        LocalTime startAt,
        boolean isBooked
) {
}
