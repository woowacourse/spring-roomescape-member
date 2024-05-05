package roomescape.service.dto.response;

import roomescape.domain.ReservationTime;

import java.time.LocalTime;

public record ReservationTimeIsBookedResponse(LocalTime startAt, Long timeId, boolean alreadyBooked) {

    public ReservationTimeIsBookedResponse(ReservationTime reservationTime, boolean alreadyBooked) {
        this(reservationTime.getStartAt(), reservationTime.getId(), alreadyBooked);
    }
}
