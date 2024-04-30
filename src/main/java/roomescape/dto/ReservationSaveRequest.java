package roomescape.dto;

import java.time.LocalDate;

public record ReservationSaveRequest(
        Long id,
        String name,
        LocalDate date,
        Long timeId,
        Long themeId
) {
}
