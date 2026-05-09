package roomescape.service.dto;

import java.time.LocalDate;

public record ReservationCreateCommand(
        String name,
        LocalDate date,
        long timeId,
        long themeId
) {
}
