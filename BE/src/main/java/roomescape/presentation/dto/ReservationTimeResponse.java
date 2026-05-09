package roomescape.presentation.dto;

import roomescape.domain.ReservationTime;

public record ReservationTimeResponse(
        Long id,
        String startAt,
        boolean alreadyBooked
) {
    public static ReservationTimeResponse from(ReservationTime reservationTime) {
        return from(reservationTime, false);
    }

    public static ReservationTimeResponse from(ReservationTime reservationTime, boolean alreadyBooked) {
        return new ReservationTimeResponse(
                reservationTime.id(),
                reservationTime.startAt().toString(),
                alreadyBooked
        );
    }
}
