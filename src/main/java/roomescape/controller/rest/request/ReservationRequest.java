package roomescape.controller.rest.request;

import java.time.LocalDate;

public record ReservationRequest(String name, LocalDate date, long timeId, long themeId) {
}
