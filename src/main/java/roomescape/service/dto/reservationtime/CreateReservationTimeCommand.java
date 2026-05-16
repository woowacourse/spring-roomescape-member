package roomescape.service.dto.reservationtime;

import java.time.LocalTime;

public record CreateReservationTimeCommand(
        LocalTime startAt
) {
}
