package roomescape.dto.reservationtime;

import roomescape.domain.ReservationTime;

import java.time.LocalTime;

public record ReservationTimeCreateRequest(LocalTime startAt) {

    public ReservationTime toReservationTime() {
        return new ReservationTime(startAt);
    }
}
