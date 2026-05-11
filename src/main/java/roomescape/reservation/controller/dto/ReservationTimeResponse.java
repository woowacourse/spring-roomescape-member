package roomescape.reservation.controller.dto;

import roomescape.reservation.domain.ReservationTime;

public record ReservationTimeResponse(
        long id,
        String startAt
) {
    public static ReservationTimeResponse from(ReservationTime time) {
        return new ReservationTimeResponse(time.getId(), time.getStartAt().toString());
    }
}
