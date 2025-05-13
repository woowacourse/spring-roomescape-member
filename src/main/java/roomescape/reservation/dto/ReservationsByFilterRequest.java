package roomescape.reservation.dto;

import java.time.LocalDate;

public record ReservationsByFilterRequest(Long themeId, Long memberId, LocalDate dateFrom, LocalDate dateTo) {
}
