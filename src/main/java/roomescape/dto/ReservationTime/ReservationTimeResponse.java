package roomescape.dto.ReservationTime;

import roomescape.domain.ReservationTime.ReservationTime;

public record ReservationTimeResponse(long id, String startAt) {
    public static ReservationTimeResponse from(ReservationTime reservationTime) {
        return new ReservationTimeResponse(reservationTime.id(), reservationTime.startAt());
    }
}
