package roomescape.domain.reservation.dto;

import java.time.LocalTime;

import roomescape.domain.reservationtime.domain.ReservationTime;

public record TimeWithStatusResponse(
        Long id,
        LocalTime startAt,
        boolean reserved
) {
    public static TimeWithStatusResponse from(ReservationTime reservationTime, boolean reserved) {
        return new TimeWithStatusResponse(
                reservationTime.getId(),
                reservationTime.getStartAt(),
                reserved
        );
    }
}
