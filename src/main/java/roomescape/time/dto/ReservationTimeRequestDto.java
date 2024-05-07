package roomescape.time.dto;

import roomescape.time.domain.ReservationTime;

import java.time.LocalTime;

public record ReservationTimeRequestDto(LocalTime startAt) {

    public ReservationTime toReservationTime() {
        return new ReservationTime(null, startAt);
    }
}
