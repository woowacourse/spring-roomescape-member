package roomescape.reservation.dto.response;

import java.time.LocalDate;
import roomescape.reservation.domain.Reservation;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.dto.response.ReservationTimeResponse;
import roomescape.theme.domain.Theme;
import roomescape.theme.dto.response.ThemeResponse;

public record ReservationResponse(
        long id,
        String name,
        LocalDate date,
        ReservationTimeResponse time,
        ThemeResponse theme
) {
    public static ReservationResponse from(Reservation reservation, ReservationTime reservationTime, Theme theme) {
        return new ReservationResponse(reservation.getId(), reservation.getName(), reservation.getDate(),
                ReservationTimeResponse.from(reservationTime), ThemeResponse.from(theme)
        );
    }
}
