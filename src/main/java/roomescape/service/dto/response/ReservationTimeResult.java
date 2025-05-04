package roomescape.service.dto.response;

import java.time.LocalTime;
import roomescape.domain.ReservationTime;

public record ReservationTimeResult(long id, LocalTime startAt) {

    public static ReservationTimeResult from(ReservationTime reservationTime) {
        return new ReservationTimeResult(reservationTime.getId(), reservationTime.getStartAt());
    }
}
