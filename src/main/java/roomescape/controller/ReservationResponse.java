package roomescape.controller;

import java.time.LocalDate;

public record ReservationResponse(long id, String name, LocalDate date, ReservationTimeResponse time) {
}
