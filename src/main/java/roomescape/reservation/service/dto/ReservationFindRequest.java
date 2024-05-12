package roomescape.reservation.service.dto;

import java.time.LocalDate;

public record ReservationFindRequest(
        Long memberId,
        Long themeId,
        LocalDate dateFrom,
        LocalDate dateTo
) {
}
