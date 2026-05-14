package roomescape.time.controller.dto;

import roomescape.time.domain.ReservationTime;

import java.time.LocalTime;

public record ReservationTimeResponse(Long id, LocalTime startAt) {

    public static ReservationTimeResponse from(ReservationTime time) {
        return new ReservationTimeResponse(time.getId(), time.getStartAt());
    }
}
