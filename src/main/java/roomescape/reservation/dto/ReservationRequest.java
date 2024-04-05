package roomescape.reservation.dto;

import java.time.LocalDate;

public record ReservationRequest(LocalDate date, String name, long timeId) {
}
