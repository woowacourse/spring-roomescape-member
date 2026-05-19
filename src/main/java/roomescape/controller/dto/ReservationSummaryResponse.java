package roomescape.controller.dto;

import java.time.LocalDate;

public record ReservationSummaryResponse(
        String id,
        String name,
        LocalDate date,
        boolean canceled,
        String timeId,
        String themeId
) {
}
