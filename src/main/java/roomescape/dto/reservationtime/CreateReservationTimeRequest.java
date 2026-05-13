package roomescape.dto.reservationtime;

import java.time.LocalTime;

public record CreateReservationTimeRequest(
        LocalTime startAt
) {
}
