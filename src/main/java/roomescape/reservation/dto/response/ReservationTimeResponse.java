package roomescape.reservation.dto.response;

import roomescape.reservation.entity.ReservationTime;

public record ReservationTimeResponse(
        Long id,
        String startAt
) {

    public static ReservationTimeResponse from(ReservationTime reservationTime) {
        return new ReservationTimeResponse(reservationTime.getId(), reservationTime.getFormattedTime());
    }
}
