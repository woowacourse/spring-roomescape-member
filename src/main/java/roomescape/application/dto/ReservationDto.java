package roomescape.application.dto;

import java.time.LocalDate;

public record ReservationDto(
        long id,
        String name,
        ThemeDto theme,
        LocalDate date,
        TimeDto time
) {
}
