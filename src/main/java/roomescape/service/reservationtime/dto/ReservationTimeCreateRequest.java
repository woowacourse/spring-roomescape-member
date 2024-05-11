package roomescape.service.reservationtime.dto;

import roomescape.domain.reservationtime.ReservationTime;

import java.time.LocalTime;

public record ReservationTimeCreateRequest(LocalTime startAt) {

    public ReservationTime toReservationTime() {
        return new ReservationTime(startAt);
    }
}
