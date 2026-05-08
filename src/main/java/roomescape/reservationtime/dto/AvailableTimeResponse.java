package roomescape.reservationtime.dto;

import roomescape.reservationtime.domain.ReservationTimeAvailability;

import java.time.LocalTime;

public record AvailableTimeResponse(
        Long id,
        LocalTime startAt,
        boolean isAvailable
) {

    public static AvailableTimeResponse from(ReservationTimeAvailability timeAvailability) {
        return new AvailableTimeResponse(
                timeAvailability.getReservationTime().getId(),
                timeAvailability.getReservationTime().getStartAt(),
                timeAvailability.isAvailable());
    }
}
