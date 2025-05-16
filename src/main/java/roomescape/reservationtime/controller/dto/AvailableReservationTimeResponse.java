package roomescape.reservationtime.controller.dto;

import java.time.LocalTime;
import roomescape.reservationtime.domain.AvailableReservationTime;

public record AvailableReservationTimeResponse(long id, LocalTime startAt, boolean alreadyBooked) {

    public static AvailableReservationTimeResponse from(final AvailableReservationTime availableReservationTime) {
        return new AvailableReservationTimeResponse(
                availableReservationTime.getReservationTime().getId(),
                availableReservationTime.getReservationTime().getStartAt(),
                availableReservationTime.getBookedStatus());
    }
}
