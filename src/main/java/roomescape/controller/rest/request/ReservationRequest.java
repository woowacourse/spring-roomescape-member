package roomescape.controller.rest.request;

import java.time.LocalDate;

public record ReservationRequest(LocalDate date, long memberId, long timeId, long themeId) {
}
