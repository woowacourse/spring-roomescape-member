package roomescape.presentation.dto.reservation;

import java.time.LocalDate;

public record ReservationFilterDto(
        Long themeId,
        Long userId,
        LocalDate from,
        LocalDate to
) {
}
