package roomescape.controller.admin.api.dto;

import java.time.LocalTime;
import roomescape.service.result.ReservationTimeResult;

public record AdminReservationTimeResponse(long id, LocalTime startAt) {

    public static AdminReservationTimeResponse from(ReservationTimeResult result) {
        return new AdminReservationTimeResponse(result.id(), result.startAt());
    }
}
