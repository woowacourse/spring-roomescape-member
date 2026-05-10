package roomescape.controller.dto;

import java.time.LocalDate;

public record AvailableReservationTimesQuery(
        Long themeId,
        LocalDate date,
        Boolean available
) {
    public static AvailableReservationTimesQuery of(Long themeId, LocalDate date,  Boolean available) {
        return new AvailableReservationTimesQuery(themeId, date, available);
    }
}
