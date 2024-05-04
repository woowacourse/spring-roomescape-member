package roomescape.dto.request;

import java.time.LocalTime;
import roomescape.domain.ReservationTime;

public record ReservationTimeAddRequest(LocalTime startAt) {
    public ReservationTime toEntity() {
        return new ReservationTime(null, startAt);
    }
}
