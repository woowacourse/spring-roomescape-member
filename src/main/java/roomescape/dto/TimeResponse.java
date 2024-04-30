package roomescape.dto;

import java.time.LocalTime;

public record TimeResponse(
        Long id,
        LocalTime startAt
) {
}
