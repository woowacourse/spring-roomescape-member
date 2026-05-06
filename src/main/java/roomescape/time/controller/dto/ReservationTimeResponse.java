package roomescape.time.controller.dto;

import roomescape.theme.repository.AvailableTimeQueryResult;
import roomescape.time.service.dto.ReservationTimeResult;

import java.time.LocalTime;

public record ReservationTimeResponse(Long id, LocalTime startAt) {

    public static ReservationTimeResponse from(ReservationTimeResult time) {
        return new ReservationTimeResponse(time.id(), time.startAt());
    }

    public static ReservationTimeResponse from(AvailableTimeQueryResult time) {
        return new ReservationTimeResponse(time.id(), time.startAt());
    }
}
