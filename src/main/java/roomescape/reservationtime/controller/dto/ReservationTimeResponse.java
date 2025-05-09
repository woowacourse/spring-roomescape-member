package roomescape.reservationtime.controller.dto;

import java.time.LocalTime;
import roomescape.reservationtime.domain.ReservationTime;

public record ReservationTimeResponse(long id, LocalTime startAt) {

    public static ReservationTimeResponse from(final ReservationTime reservationTime) {
        return new ReservationTimeResponse(reservationTime.getId(), reservationTime.getStartAt());
    }
}
