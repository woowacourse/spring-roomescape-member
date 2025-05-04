package roomescape.controller.dto.response;

import java.time.LocalTime;
import roomescape.service.dto.response.AvailableReservationTimeResult;

public record AvailableReservationTimeResponse(long id, LocalTime startAt, boolean alreadyBooked) {

    public static AvailableReservationTimeResponse from(AvailableReservationTimeResult result) {
        return new AvailableReservationTimeResponse(result.id(), result.startAt(), result.alreadyBooked());
    }
}
