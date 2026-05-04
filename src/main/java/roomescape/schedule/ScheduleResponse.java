package roomescape.schedule;

import java.time.LocalDate;
import java.util.List;

public record ScheduleResponse(long themeId, LocalDate date, List<AvailableTimeDto> schedules) {

}
