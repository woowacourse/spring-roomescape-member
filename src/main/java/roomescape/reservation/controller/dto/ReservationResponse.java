package roomescape.reservation.controller.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import roomescape.reservation.service.dto.ReservationResult;

public record ReservationResponse(
        Long id,
        String name,
        LocalDate date,
        Long timeId,
        LocalTime startAt,
        Long themeId,
        String themeName
) {

    public static ReservationResponse from(
            ReservationResult result
    ) {
        return new ReservationResponse(
                result.id(),
                result.name(),
                result.date(),
                result.timeId(),
                result.startAt(),
                result.themeId(),
                result.themeName()
        );
    }
}
