package roomescape.dto;

import roomescape.domain.ReservationTime;

import java.time.LocalTime;

public record ReservationTimeRequest(LocalTime startAt) {

    public ReservationTimeRequest {
        InputValidator.validateNotNull(startAt);
    }

    public ReservationTime toReservationTime() {
        return new ReservationTime(this.startAt());
    }
}
