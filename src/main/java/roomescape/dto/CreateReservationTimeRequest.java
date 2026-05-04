package roomescape.dto;

import java.time.LocalTime;

public record CreateReservationTimeRequest(
        LocalTime startAt
) {
}
