package roomescape.service.dto.query;

import java.time.LocalTime;

import roomescape.domain.ReservationTime;

public record ReservationTimeQuery(
        Long id,
        LocalTime startAt
) {

    public static ReservationTimeQuery from(ReservationTime reservationTime) {
        return new ReservationTimeQuery(reservationTime.getId(), reservationTime.getStartAt());
    }
}
