package roomescape.reservation.dto.response;

import roomescape.reservation.entity.ReservationTime;

public record ReservationTimeResponse(
        Long id,
        String startAt
) {

    public static ReservationTimeResponse from(ReservationTime entity) {
        return new ReservationTimeResponse(entity.getId(), entity.getFormattedTime());
    }
}
