package roomescape.service.dto.reservation;

import roomescape.domain.reservation.ReservationTimeAvailability;

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
