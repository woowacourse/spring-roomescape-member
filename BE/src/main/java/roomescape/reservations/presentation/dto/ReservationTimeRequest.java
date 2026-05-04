package roomescape.reservations.presentation.dto;

import java.time.LocalTime;

public record ReservationTimeRequest(
        LocalTime startAt
) {
}
