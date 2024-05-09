package roomescape.dto.time;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import roomescape.domain.time.Time;

public record TimeResponse(
        Long id,
        @JsonFormat(pattern = "HH:mm") LocalTime startAt
) {

    public static TimeResponse from(Time time) {
        return new TimeResponse(time.getId(), time.getStartAt());
    }
}
