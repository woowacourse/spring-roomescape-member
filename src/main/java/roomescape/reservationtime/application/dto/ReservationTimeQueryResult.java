package roomescape.reservationtime.application.dto;

import java.time.LocalTime;
import roomescape.reservationtime.domain.ReservationTime;

public record ReservationTimeQueryResult(Long id, LocalTime startAt) {

    public static ReservationTimeQueryResult from(ReservationTime time) {
        return new ReservationTimeQueryResult(
                time.getId(),
                time.getStartAt()
        );
    }

    public static ReservationTimeQueryResult from(Long id, LocalTime startAt) {
        return new ReservationTimeQueryResult(
                id,
                startAt
        );
    }
}
