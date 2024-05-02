package roomescape.dto;

import java.time.LocalDate;

public record ReservationRequest(
        Long id,
        String name,
        LocalDate date,
        Long timeId,
        Long themeId
) {
}
