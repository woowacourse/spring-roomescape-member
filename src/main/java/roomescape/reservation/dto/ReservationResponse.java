package roomescape.reservation.dto;

import java.time.LocalDate;
import roomescape.theme.dto.ThemeResponse;
import roomescape.time.dto.TimeResponse;

public record ReservationResponse(Long id, String name, LocalDate date, ThemeResponse theme, TimeResponse time) {
}
