package roomescape.reservation.domain.dto;

import java.time.LocalDate;
import roomescape.reservationTime.domain.dto.ReservationTimeResponseDto;
import roomescape.theme.domain.dto.ThemeResponseDto;

public record ReservationResponseDto(
        Long id,
        String name,
        LocalDate date,
        ReservationTimeResponseDto time,
        ThemeResponseDto theme
) {
}
