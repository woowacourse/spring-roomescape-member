package roomescape.controller.dto.response;

import java.time.LocalTime;
import roomescape.service.dto.response.ReservationTimeResult;

public record ReservationTimeResponse(long id, LocalTime startAt) {

    public static ReservationTimeResponse from(ReservationTimeResult result) {
        return new ReservationTimeResponse(result.id(), result.startAt());
    }
}
