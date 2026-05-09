package roomescape.reservationtime.payload;

import java.time.LocalTime;
import roomescape.reservationtime.entity.ReservationTime;

public record ReservationTimeResponse(
        Long id,
        LocalTime startAt
) {

    public static ReservationTimeResponse from(ReservationTime reservationTime) {
        return new ReservationTimeResponse(
                reservationTime.getId(),
                reservationTime.getStartAt()
        );
    }

}
