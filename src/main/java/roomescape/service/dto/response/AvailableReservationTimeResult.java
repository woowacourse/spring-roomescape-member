package roomescape.service.dto.response;

import java.time.LocalTime;
import roomescape.domain.ReservationTime;

public record AvailableReservationTimeResult(long id, LocalTime startAt, boolean alreadyBooked) {

    public static AvailableReservationTimeResult of(ReservationTime reservationTime, boolean alreadyBooked) {
        return new AvailableReservationTimeResult(reservationTime.getId(),
                reservationTime.getStartAt(),
                alreadyBooked);
    }
}
