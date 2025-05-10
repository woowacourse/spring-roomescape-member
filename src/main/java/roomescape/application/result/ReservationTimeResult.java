package roomescape.application.result;

import java.time.LocalTime;
import roomescape.domain.ReservationTime;

public record ReservationTimeResult(
        Long id,
        LocalTime startAt
) {
    public static ReservationTimeResult from(ReservationTime reservationTime) {
        return new ReservationTimeResult(reservationTime.id(), reservationTime.startAt());
    }
}
