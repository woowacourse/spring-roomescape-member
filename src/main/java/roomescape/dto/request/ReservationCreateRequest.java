package roomescape.dto.request;

import java.time.LocalDate;

public record ReservationCreateRequest(String name, LocalDate date, Long timeId, Long themeId) {
}
