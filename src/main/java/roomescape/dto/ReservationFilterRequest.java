package roomescape.dto;

import java.time.LocalDate;

public record ReservationFilterRequest(Long themeId, Long memberId, LocalDate dateFrom, LocalDate dateTo) {
}
