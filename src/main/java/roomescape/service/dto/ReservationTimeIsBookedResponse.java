package roomescape.service.dto;

import roomescape.domain.ReservationTime;

import java.time.LocalTime;

public record ReservationTimeIsBookedResponse(LocalTime startAt, Long timeId, boolean alreadyBooked) {

    public static ReservationTimeIsBookedResponse of(ReservationTime reservationTime, boolean alreadyBooked) {
        return new ReservationTimeIsBookedResponse(
                reservationTime.getStartAt(),
                reservationTime.getId(),
                alreadyBooked
        );
    }
}
