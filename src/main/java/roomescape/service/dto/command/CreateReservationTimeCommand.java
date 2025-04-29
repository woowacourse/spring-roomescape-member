package roomescape.service.dto.command;

import java.time.LocalTime;

import roomescape.domain.ReservationTime;

public record CreateReservationTimeCommand(
        LocalTime startAt
) {

    public ReservationTime toReservationTime() {
        return new ReservationTime(startAt);
    }
}
