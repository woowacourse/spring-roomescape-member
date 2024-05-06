package roomescape.time.dto;

import roomescape.time.domain.ReservationTime;

public record ReservationTimeRequestDto(String startAt) {

    public ReservationTime toReservationTime() {
        return new ReservationTime(null, startAt);
    }
}
