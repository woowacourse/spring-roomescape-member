package roomescape.application.dto;

import java.time.LocalTime;
import roomescape.domain.ReservationTime;

public record ReservationTimeRequest(
        LocalTime startAt
) {

    public ReservationTime toTime() {
        return new ReservationTime(startAt);
    }
}
