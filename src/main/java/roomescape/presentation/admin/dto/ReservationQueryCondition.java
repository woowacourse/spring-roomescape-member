package roomescape.presentation.admin.dto;

import java.time.LocalDate;

public record ReservationQueryCondition(
        Long themeId,
        Long memberId,
        LocalDate dateFrom,
        LocalDate dateTo
) {
}
