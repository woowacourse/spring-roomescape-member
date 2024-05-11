package roomescape.application.dto.request;

import java.time.LocalDate;

public record ReservationFilteringRequest(long themeId, long memberId, LocalDate dateFrom, LocalDate dateTo) {
}
