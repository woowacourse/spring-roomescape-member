package roomescape.controller.request;

import java.time.LocalDate;

public record ReservationRequest3(LocalDate date, Long themeId, Long timeId) {
}
