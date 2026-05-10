package roomescape.schedule.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import roomescape.schedule.model.Schedule;

import java.util.List;

public class SchedulesResponse {
    private final List<ScheduleResponse> scheduleResponses;

    private SchedulesResponse(List<ScheduleResponse> scheduleResponses) {
        this.scheduleResponses = scheduleResponses;
    }

    public static SchedulesResponse from(List<Schedule> schedules) {
        List<ScheduleResponse> responses = schedules.stream()
                .map(ScheduleResponse::from)
                .toList();

        return new SchedulesResponse(responses);
    }

    @JsonValue
    public List<ScheduleResponse> getScheduleResponses() {
        return scheduleResponses;
    }
}
