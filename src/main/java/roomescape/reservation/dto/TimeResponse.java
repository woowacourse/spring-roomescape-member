package roomescape.reservation.dto;

import java.time.LocalTime;

public record TimeResponse(
        Long id,
        LocalTime startAt
) {
}
