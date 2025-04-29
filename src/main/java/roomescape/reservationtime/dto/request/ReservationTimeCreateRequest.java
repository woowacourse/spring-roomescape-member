package roomescape.reservationtime.dto.request;

import java.time.LocalTime;
import roomescape.reservationtime.domain.ReservationTime;

public record ReservationTimeCreateRequest(
        LocalTime startAt
) {
    public ReservationTime toReservationTime() {
        return ReservationTime.withUnassignedId(startAt);
    }
}
