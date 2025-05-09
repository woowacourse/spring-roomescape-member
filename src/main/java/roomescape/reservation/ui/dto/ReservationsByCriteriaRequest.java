package roomescape.reservation.ui.dto;

import java.time.LocalDate;

public record ReservationsByCriteriaRequest(
        Long themeId,
        Long memberId,
        LocalDate dateFrom,
        LocalDate dateTo
) {

}
