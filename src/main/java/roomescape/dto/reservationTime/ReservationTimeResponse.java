package roomescape.dto.reservationTime;

import roomescape.domain.reservationTime.ReservationTime;

public record ReservationTimeResponse(long id, String startAt) {
    public static ReservationTimeResponse from(ReservationTime reservationTime) {
        return new ReservationTimeResponse(reservationTime.id(), reservationTime.startAt());
    }
}
