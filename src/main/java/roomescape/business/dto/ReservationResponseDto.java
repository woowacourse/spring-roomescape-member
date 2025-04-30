package roomescape.business.dto;

import java.time.LocalDate;

public record ReservationResponseDto(
        long id,
        String name,
        LocalDate date,
        ReservationTimeResponseDto time,
        ReservationThemeResponseDto theme
) {
}
