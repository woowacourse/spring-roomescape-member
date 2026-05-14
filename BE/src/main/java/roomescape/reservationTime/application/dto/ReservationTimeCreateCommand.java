package roomescape.reservationTime.application.dto;

import java.time.LocalTime;

public record ReservationTimeCreateCommand(
        LocalTime startAt
) {
}
