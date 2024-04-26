package roomescape.dto.web;

import java.time.LocalDate;

public record ReservationWebRequest(LocalDate date, String name, Long timeId) {
}
