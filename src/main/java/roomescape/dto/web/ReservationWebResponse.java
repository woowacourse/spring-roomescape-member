package roomescape.dto.web;

import java.time.LocalDate;

public record ReservationWebResponse(Long id, String name, LocalDate date, ReservationTimeWebResponse time) {
}
