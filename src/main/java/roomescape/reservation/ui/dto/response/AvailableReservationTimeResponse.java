package roomescape.reservation.ui.dto.response;

import java.time.LocalTime;

public record AvailableReservationTimeResponse(
        Long timeId,
        LocalTime startAt,
        boolean alreadyBooked
) {

    public AvailableReservationTimeResponse from(
            Long timeId,
            LocalTime startAt,
            boolean alreadyBooked
    ) {
        return new AvailableReservationTimeResponse(timeId, startAt, alreadyBooked);
    }
}
