package roomescape.service.dto.request;

import java.time.LocalTime;

import roomescape.domain.ReservationTime;

public record CreateReservationTimeServiceRequest(
        LocalTime startAt
) {

    public ReservationTime toReservationTime() {
        return new ReservationTime(startAt);
    }
}
