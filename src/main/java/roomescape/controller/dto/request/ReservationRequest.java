package roomescape.controller.dto.request;

import java.time.LocalDate;

public record ReservationRequest(String name, LocalDate date, long timeId) {
}
