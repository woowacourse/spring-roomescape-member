package roomescape.time.service.dto;

import roomescape.time.domain.ReservationTime;

import java.time.LocalTime;

public record ReservationTimeResult(Long id, LocalTime startAt) {

    public static ReservationTimeResult from(ReservationTime reservationTime) {
        return new ReservationTimeResult(
                reservationTime.getId(),
                reservationTime.getStartAt()
        );
    }
}
