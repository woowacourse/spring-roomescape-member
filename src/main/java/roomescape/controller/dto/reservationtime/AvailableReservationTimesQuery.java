package roomescape.controller.dto.reservationtime;

import java.time.LocalDate;
import roomescape.service.dto.AvailableReservationTimesCondition;

public record AvailableReservationTimesQuery(
        Long themeId,
        LocalDate date,
        Boolean available
) {

    public static AvailableReservationTimesQuery toQuery(Long themeId, String date, Boolean available) {
        return new AvailableReservationTimesQuery(themeId, LocalDate.parse(date), available);
    }

    public AvailableReservationTimesCondition toCondition() {
        return new AvailableReservationTimesCondition(themeId, date, available);
    }
}
