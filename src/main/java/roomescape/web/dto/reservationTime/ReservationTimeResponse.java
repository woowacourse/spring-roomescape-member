package roomescape.web.dto.reservationTime;

import java.time.LocalTime;
import roomescape.domain.ReservationTime;

public record ReservationTimeResponse(Long id, LocalTime startAt) {
    public static ReservationTimeResponse from(ReservationTime time) {
        return new ReservationTimeResponse(time.getId(), time.getStartAt());
    }
}
