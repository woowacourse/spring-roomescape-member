package roomescape.dto;

import roomescape.entity.ReservationTime;

import java.time.LocalTime;

public record ReservationTimeResponse(
    Long id,
    LocalTime startAt
) {
    public static ReservationTimeResponse of(ReservationTime reservationTime) {
        return new ReservationTimeResponse(
            reservationTime.getId(),
            reservationTime.getStartAt()
        );
    }
}
