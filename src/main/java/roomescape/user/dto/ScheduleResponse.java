package roomescape.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import roomescape.domain.Schedule;

public record ScheduleResponse(
        @JsonFormat(pattern = "HH:mm")
        LocalTime startAt,
        Boolean isAvailable
) {
    public static ScheduleResponse from(Schedule schedule) {
        return new ScheduleResponse(schedule.getStartAt(), schedule.getAvailable());
    }
}
