package roomescape.dto.request;

import java.time.LocalDate;

public record ReservationRequest(LocalDate date, Long timeId, Long themeId) {
}
