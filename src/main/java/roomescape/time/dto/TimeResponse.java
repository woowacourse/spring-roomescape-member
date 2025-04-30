package roomescape.time.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import roomescape.time.domain.Time;

public record TimeResponse(Long id, @JsonFormat(pattern = "HH:mm") LocalTime startAt) {
    public static TimeResponse from(Time time) {
        return new TimeResponse(time.id(), time.startAt());
    }
}
