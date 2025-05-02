package roomescape.time.service.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationTimeWithReservationDto(
        Long timeId,
        LocalTime startAt,
        Long reservationId,
        Long themeId,
        LocalDate date
) {
}
