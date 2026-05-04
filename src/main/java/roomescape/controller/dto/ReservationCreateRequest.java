package roomescape.controller.dto;

import java.time.LocalDate;

public record ReservationCreateRequest(
        String name,
        LocalDate date,
        long timeId,
        long themeId
) {
}
