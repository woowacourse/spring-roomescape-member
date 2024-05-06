package roomescape.dto;

import java.time.LocalTime;
import roomescape.domain.ReservationTime;

public record ReservationTimeRequest(LocalTime startAt) {

    public ReservationTimeRequest {
        InputValidator.validateNotNull(startAt);
    }

    public ReservationTime toReservationTime() {
        return new ReservationTime(this.startAt());
    }
}
