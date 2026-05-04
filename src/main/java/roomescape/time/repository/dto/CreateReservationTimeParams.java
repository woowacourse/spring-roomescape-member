package roomescape.time.repository.dto;

import java.time.LocalTime;

public record CreateReservationTimeParams(
        LocalTime startAt
) {
}
