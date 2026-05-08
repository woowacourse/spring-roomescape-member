package roomescape.reservationtime.controller;

import java.time.LocalDate;
import java.util.List;
import roomescape.reservationtime.domain.AvailableTime;

public record ScheduleResponse(long themeId, LocalDate date, List<AvailableTime> schedules) {
    public static ScheduleResponse from(long themeId, LocalDate date, List<AvailableTime> schedules) {
        return new ScheduleResponse(themeId, date, schedules);
    }
}
