package roomescape.dto;

import java.time.LocalTime;
import roomescape.domain.ReservationTime;

public record AvailableReservationTimeResponse(Long timeId, LocalTime startAt, boolean alreadyBooked) {

    public static AvailableReservationTimeResponse from(ReservationTime reservationTime, boolean alreadyBooked) {
        return new AvailableReservationTimeResponse(reservationTime.getId(), reservationTime.getStartAt(),
                alreadyBooked);
    }
}
