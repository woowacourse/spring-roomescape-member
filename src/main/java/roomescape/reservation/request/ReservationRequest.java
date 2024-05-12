package roomescape.reservation.request;

import java.time.LocalDate;

public record ReservationRequest(LocalDate date, long timeId, long themeId, long memberId) {
}
