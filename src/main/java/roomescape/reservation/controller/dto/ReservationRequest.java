package roomescape.reservation.controller.dto;

import java.time.LocalDate;

public record ReservationRequest(
        String name,
        LocalDate date,
        long timeId,
        long themeId
) {
}
