package roomescape.time.dto;

import roomescape.time.domain.ReservationTime;

import java.time.LocalTime;

public record ResponseReservationTime(
        Long id,
        LocalTime startAt
) {

    public static ResponseReservationTime from(ReservationTime time) {
        return new ResponseReservationTime(
                time.getId(),
                time.getStartAt()
        );
    }
}
