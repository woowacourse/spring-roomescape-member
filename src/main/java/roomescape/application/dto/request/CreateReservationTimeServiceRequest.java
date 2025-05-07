package roomescape.application.dto.request;

import java.time.LocalTime;
import roomescape.domain.entity.ReservationTime;

public record CreateReservationTimeServiceRequest(
        LocalTime startAt
) {

    public ReservationTime toReservationTime() {
        return new ReservationTime(startAt);
    }
}
