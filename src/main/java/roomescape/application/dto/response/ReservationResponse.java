package roomescape.application.dto.response;

import java.time.LocalDate;
import roomescape.domain.reservation.Reservation;

public record ReservationResponse(
        long id,
        String name,
        ThemeResponse theme,
        LocalDate date,
        ReservationTimeResponse time
) {
    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(reservation.getId(), reservation.getName(),
                ThemeResponse.from(reservation.getTheme()),
                reservation.getDate(),
                new ReservationTimeResponse(reservation.getTimeId(), reservation.getTime()));
    }
}
