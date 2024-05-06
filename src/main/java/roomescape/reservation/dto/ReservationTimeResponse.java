package roomescape.reservation.dto;

import java.time.LocalTime;
import roomescape.reservation.domain.ReservationTime;

public record ReservationTimeResponse(long id, LocalTime startAt) {
    public static ReservationTimeResponse from(ReservationTime reservationTime) {
        return new ReservationTimeResponse(reservationTime.getId(), reservationTime.getStartAt());
    }
}
