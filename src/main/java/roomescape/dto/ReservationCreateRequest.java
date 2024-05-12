package roomescape.dto;

import java.time.LocalDate;

public record ReservationCreateRequest(LocalDate date, Long timeId, Long themeId) {
}
