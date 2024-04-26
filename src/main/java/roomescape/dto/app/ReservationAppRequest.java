package roomescape.dto.app;

import java.time.LocalDate;

public record ReservationAppRequest(Long timeId, LocalDate date, String name) {
}
