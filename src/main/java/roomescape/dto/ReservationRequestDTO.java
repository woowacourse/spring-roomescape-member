package roomescape.dto;

import java.time.LocalDate;

public record ReservationRequestDTO(
        String name,
        LocalDate date,
        Long timeId,
        Long themeId
) {
}
