package roomescape.controller.dto;

import java.time.LocalDate;
import java.util.UUID;

public record ReservationDetailResponse(
        UUID id,
        String name,
        LocalDate date,
        ReservationTimeResponse time,
        ThemeResponse theme
) {
}
