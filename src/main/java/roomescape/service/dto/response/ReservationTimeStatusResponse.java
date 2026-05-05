package roomescape.service.dto.response;

import roomescape.repository.dto.ReservationTimesWithStatus;

import java.time.LocalTime;

public record ReservationTimeStatusResponse(
        Long id,
        LocalTime startAt,
        boolean reserved
) {

    public static ReservationTimeStatusResponse from(ReservationTimesWithStatus reservationTimesWithStatus) {
        return new ReservationTimeStatusResponse(
                reservationTimesWithStatus.id(),
                reservationTimesWithStatus.startAt(),
                reservationTimesWithStatus.reserved()
        );
    }
}
