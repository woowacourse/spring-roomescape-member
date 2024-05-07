package roomescape.service.dto;

import roomescape.domain.ReservationTimeAvailability;

import java.time.LocalTime;

public record ReservationTimeAvailabilityResponse(LocalTime startAt, Long timeId, boolean alreadyBooked) {

    public static ReservationTimeAvailabilityResponse of(ReservationTimeAvailability reservationTimeAvailability) {
        return new ReservationTimeAvailabilityResponse(
                reservationTimeAvailability.getReservationTime().getStartAt(),
                reservationTimeAvailability.getReservationTime().getId(),
                reservationTimeAvailability.isAlreadyBooked()
        );
    }
}
