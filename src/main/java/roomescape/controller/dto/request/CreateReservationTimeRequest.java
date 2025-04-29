package roomescape.controller.dto.request;

import java.time.LocalTime;
import roomescape.domain.ReservationTime;

public record CreateReservationTimeRequest(LocalTime startAt) {

    public ReservationTime toReservationTime() {
        return new ReservationTime(startAt);
    }
}
