package roomescape.controller.dto.response;

import java.time.LocalTime;
import roomescape.domain.AvailableReservationTime;

public record AvailableReservationTimeResponse(long id, LocalTime startAt, boolean alreadyBooked) {

    public static AvailableReservationTimeResponse from(final AvailableReservationTime availableReservationTime) {
        return new AvailableReservationTimeResponse(
                availableReservationTime.getReservationTime().getId(),
                availableReservationTime.getReservationTime().getStartAt(),
                availableReservationTime.getBookedStatus());
    }
}
