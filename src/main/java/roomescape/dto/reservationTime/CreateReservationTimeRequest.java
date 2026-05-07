package roomescape.dto.reservationTime;

import java.time.LocalTime;

public record CreateReservationTimeRequest(
        LocalTime startAt
) {
}
