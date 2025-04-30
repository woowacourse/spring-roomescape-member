package roomescape.controller.dto.response;

import java.time.LocalTime;
import roomescape.domain.ReservationTime;

public record AvailableReservationTimeResponse(long id, LocalTime startAt, boolean alreadyBooked) {
    public static AvailableReservationTimeResponse from(ReservationTime reservationTime, boolean alreadyBooked) {
        return new AvailableReservationTimeResponse(reservationTime.getId(),
                reservationTime.getStartAt(),
                alreadyBooked);
    }
}
