package roomescape.schedule.dto;

import roomescape.schedule.model.Schedule;

import java.util.List;

public class SchedulesResponse {
    private final List<ScheduleResponse> scheduleResponses;

    private SchedulesResponse(List<ScheduleResponse> scheduleResponses) {
        this.scheduleResponses = scheduleResponses;
    }

    public static SchedulesResponse from(List<Schedule> schedules) {
        List<ScheduleResponse> responses = schedules.stream()
                .map(ScheduleResponse::of)
                .toList();

        return new SchedulesResponse(responses);
    }

    public List<ScheduleResponse> getScheduleResponses() {
        return scheduleResponses;
    }
}
