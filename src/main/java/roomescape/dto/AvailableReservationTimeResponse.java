package roomescape.dto;

import java.time.LocalTime;
import roomescape.domain.AvailableReservationTime;

public record AvailableReservationTimeResponse(Long timeId, LocalTime startAt, boolean alreadyBooked) {

    public static AvailableReservationTimeResponse from(AvailableReservationTime availableReservationTime) {
        return new AvailableReservationTimeResponse(
                availableReservationTime.getTimeId(),
                availableReservationTime.getStartAt(),
                availableReservationTime.isAlreadyBooked()
        );
    }
}
