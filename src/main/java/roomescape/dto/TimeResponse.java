package roomescape.dto;

import roomescape.domain.ReservationTime;

import java.sql.Time;

public record TimeResponse(Long id, String startAt) {

    public static TimeResponse from(ReservationTime time) {
        return new TimeResponse(time.getId(), time.getStartAt());
    }
}
