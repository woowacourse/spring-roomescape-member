package roomescape.controller.dto;

import java.time.LocalDate;

public record ReservationDetailResponse(
        String id,
        String name,
        LocalDate date,
        ReservationTimeResponse time,
        ThemeResponse theme,
        boolean cancelable
) {
}
