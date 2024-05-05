package roomescape.service.response;

import java.time.LocalTime;
import roomescape.domain.ReservationTime;

public record ReservationTimeAppResponseWithBookable(Long id, LocalTime startAt, boolean alreadyBooked) {

    public static ReservationTimeAppResponseWithBookable of(ReservationTime reservationTime, boolean alreadyBooked) {
        return new ReservationTimeAppResponseWithBookable(
            reservationTime.getId(),
            reservationTime.getStartAt(),
            alreadyBooked);
    }
}
