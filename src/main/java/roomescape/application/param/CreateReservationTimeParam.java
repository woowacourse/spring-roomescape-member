package roomescape.application.param;

import java.time.LocalTime;

public record CreateReservationTimeParam(
        LocalTime startAt
) {
}
