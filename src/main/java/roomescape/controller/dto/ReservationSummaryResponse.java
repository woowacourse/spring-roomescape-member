package roomescape.controller.dto;

import java.time.LocalDate;
import java.util.UUID;

public record ReservationSummaryResponse(
        UUID id,
        String name,
        LocalDate date,
        UUID timeId,
        UUID themeId
) {
}
