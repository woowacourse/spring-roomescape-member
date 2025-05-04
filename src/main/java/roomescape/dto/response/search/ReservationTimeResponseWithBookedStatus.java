package roomescape.dto.response.search;

import java.time.LocalTime;
import roomescape.domain.ReservationTime;

public record ReservationTimeResponseWithBookedStatus(Long id, LocalTime startAt, boolean booked) {

    public static ReservationTimeResponseWithBookedStatus of(ReservationTime time, boolean booked) {
        return new ReservationTimeResponseWithBookedStatus(time.getId(), time.getStartAt(), booked);
    }
}
