package roomescape.reservationtime.dto;

import java.time.LocalDate;
import java.util.List;

public record ScheduleResponse(
        long themeId,
        LocalDate date,
        List<AvailableTime> schedules
) {
}
