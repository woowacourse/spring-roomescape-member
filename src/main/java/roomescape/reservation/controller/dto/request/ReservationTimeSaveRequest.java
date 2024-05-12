package roomescape.reservation.controller.dto.request;

import java.time.LocalTime;
import roomescape.reservation.domain.ReservationTime;

public record ReservationTimeSaveRequest(
        LocalTime startAt
) {
    public ReservationTime toEntity() {
        return new ReservationTime(startAt);
    }
}
