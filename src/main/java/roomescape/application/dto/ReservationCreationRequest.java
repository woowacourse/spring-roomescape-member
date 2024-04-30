package roomescape.application.dto;

import java.time.LocalDate;

public record ReservationCreationRequest(String name, LocalDate date, long timeId) {
}
