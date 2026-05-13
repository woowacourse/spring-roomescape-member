package roomescape.controller.dto;

import java.time.LocalDate;
import java.util.UUID;

public record ReservationUpdateRequest(
        LocalDate date,
        UUID timeId
) {
}
