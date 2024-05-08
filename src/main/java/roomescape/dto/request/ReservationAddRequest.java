package roomescape.dto.request;

import java.time.LocalDate;

public record ReservationAddRequest(String name, LocalDate date, Long timeId, Long themeId) {
}
