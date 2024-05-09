package roomescape.dto;

import java.time.LocalDate;

public record ReservationRequest(LocalDate date, long memberId, long timeId, long themeId) {
}
