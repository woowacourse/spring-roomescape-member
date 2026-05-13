package roomescape.dto;

import java.time.LocalDate;

public record ReservationPatchRequest(LocalDate date, Long timeId) {
}
