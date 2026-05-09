package roomescape.web.dto;

import java.time.LocalTime;
import roomescape.repository.dto.TimeSlotProjection;

public record ThemeTimesResponse(
        Long id,
        LocalTime startAt,
        boolean isReservable
) {
    public static ThemeTimesResponse from(TimeSlotProjection timeSlotProjection) {
        return new ThemeTimesResponse(timeSlotProjection.id(), timeSlotProjection.startAt(),
                timeSlotProjection.isReservable());
    }
}
