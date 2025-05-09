package roomescape.reservation.application.dto.request;

import java.time.LocalTime;
import roomescape.reservation.domain.entity.ReservationTime;

public record CreateReservationTimeServiceRequest(
        LocalTime startAt
) {

    public ReservationTime toReservationTime() {
        return new ReservationTime(startAt);
    }
}
