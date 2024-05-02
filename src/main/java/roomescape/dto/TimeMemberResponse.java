package roomescape.dto;

import java.time.LocalTime;

public record TimeMemberResponse(
        Long id,
        LocalTime startAt,
        Boolean alreadyBooked
) {
}
