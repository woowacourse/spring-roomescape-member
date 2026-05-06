package roomescape.controller.dto;

import java.time.LocalTime;
import roomescape.domain.Time;

public record TimeResponse(long id, LocalTime startAt) {

    public static TimeResponse from(Time time) {
        return new TimeResponse(time.id(), time.startAt());
    }
}
