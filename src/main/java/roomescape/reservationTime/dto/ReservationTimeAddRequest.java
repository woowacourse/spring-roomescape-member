package roomescape.reservationTime.dto;

import java.time.LocalTime;
import roomescape.reservationTime.domain.ReservationTime;

public record ReservationTimeAddRequest(LocalTime startAt) {
    public ReservationTime toEntity() {
        return new ReservationTime(null, startAt);
    }
}
