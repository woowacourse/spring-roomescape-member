package roomescape.domain.reservation.dto;

import java.time.LocalDate;

public record ReservationFindRequest(Long themeId, Long memberId, LocalDate dateFrom, LocalDate dateTo) {
}
