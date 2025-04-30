package roomescape.reservation.dto;

import java.time.LocalDate;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.theme.domain.Theme;

public record ReservationResponse(Long id, String name, Theme theme, LocalDate date, ReservationTime time) {
}
