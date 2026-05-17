package roomescape.reservationtime.controller.dto;

import java.time.LocalTime;
import roomescape.reservationtime.service.dto.ReservationTimeResult;

public record ReservationTimeResponse(
        Long id,
        LocalTime startAt
) {

    public static ReservationTimeResponse from(ReservationTimeResult reservationTimeResult) {
        return new ReservationTimeResponse(
                reservationTimeResult.id(),
                reservationTimeResult.startAt()
        );
    }
}
