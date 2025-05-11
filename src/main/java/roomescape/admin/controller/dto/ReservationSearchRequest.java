package roomescape.admin.controller.dto;

import java.time.LocalDate;

public record ReservationSearchRequest(
        Long themeId,
        Long memberId,
        LocalDate dateFrom,
        LocalDate dateTo
) {
}
