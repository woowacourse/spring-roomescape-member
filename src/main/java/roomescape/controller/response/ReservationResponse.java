package roomescape.controller.response;

import java.time.LocalDate;
import roomescape.service.result.ReservationResult;

public record ReservationResponse(
        Long id,
        String name,
        LocalDate date,
        ReservationTimeResponse time
) {


    public static ReservationResponse from(ReservationResult reservationResult) {
        return new ReservationResponse(
                reservationResult.id(),
                reservationResult.name(),
                reservationResult.date(),
                new ReservationTimeResponse(
                        reservationResult.time().id(),
                        reservationResult.time().startAt()
                )
        );
    }
}
