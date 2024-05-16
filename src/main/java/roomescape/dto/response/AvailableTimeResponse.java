package roomescape.dto.response;

import roomescape.domain.ReservationTime;

import java.time.LocalTime;

public record AvailableTimeResponse(LocalTime time, Long timeId, boolean alreadyBooked) {

    public static AvailableTimeResponse from(ReservationTime reservationTime, boolean alreadyBooked) {
        return new AvailableTimeResponse(reservationTime.getStartAt(), reservationTime.getId(), alreadyBooked);
    }
}
