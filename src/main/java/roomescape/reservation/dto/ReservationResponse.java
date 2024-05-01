package roomescape.reservation.dto;

import java.time.LocalDate;
import roomescape.reservation.domain.Reservation;
import roomescape.theme.dto.ThemeResponse;
import roomescape.time.dto.TimeResponse;

public record ReservationResponse(Long id, String name, LocalDate date, ThemeResponse theme, TimeResponse time) {

    public static ReservationResponse toResponse(Reservation reservation) {
        return new ReservationResponse(reservation.getId(), reservation.getName(), reservation.getDate(),
                ThemeResponse.toResponse(reservation.getTheme()),
                TimeResponse.toResponse(reservation.getTime()));
    }
}
