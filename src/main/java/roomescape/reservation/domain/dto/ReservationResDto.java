package roomescape.reservation.domain.dto;

import java.time.LocalDate;
import roomescape.reservation_time.domain.dto.ReservationTimeResDto;
import roomescape.theme.domain.dto.ThemeResDto;

public record ReservationResDto(
    Long id,
    String name,
    LocalDate date,
    ReservationTimeResDto time,
    ThemeResDto theme
) {
}
