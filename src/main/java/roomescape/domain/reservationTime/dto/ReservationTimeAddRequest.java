package roomescape.domain.reservationTime.dto;

import java.time.LocalTime;
import roomescape.domain.reservationTime.domain.ReservationTime;

public record ReservationTimeAddRequest(LocalTime startAt) {
    public ReservationTime toEntity() {
        return new ReservationTime(null, startAt);
    }
}
