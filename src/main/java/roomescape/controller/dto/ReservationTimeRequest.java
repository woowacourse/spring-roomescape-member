package roomescape.controller.dto;

import java.time.LocalTime;
import roomescape.service.reservation.ReservationTime;

public record ReservationTimeRequest(LocalTime startAt) {

    public ReservationTime converToReservationTime() {
        return new ReservationTime(this.startAt);
    }
}
