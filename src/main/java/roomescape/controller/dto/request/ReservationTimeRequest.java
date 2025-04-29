package roomescape.controller.dto.request;

import java.time.LocalTime;
import roomescape.domain.ReservationTime;

public record ReservationTimeRequest(LocalTime startAt) {

    public ReservationTime toReservationTime() {
        return new ReservationTime(startAt);
    }
}
