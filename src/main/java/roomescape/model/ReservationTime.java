package roomescape.model;

import java.time.LocalTime;
import roomescape.dto.TimeResponse;

public record ReservationTime(Long id, LocalTime startAt) {

    public static ReservationTime from(TimeResponse timeResponse) {
        return new ReservationTime(timeResponse.id(), timeResponse.startAt());
    }

    public LocalTime startAt() {
        return startAt;
    }
}
