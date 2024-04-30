package roomescape.dto;

import java.time.LocalTime;
import roomescape.domain.ReservationTime;

public record ReservationTimeRequest(String startAt) {
    public ReservationTime toDomain() {
        LocalTime time = LocalTime.parse(startAt);
        return new ReservationTime(time);
    }
}
