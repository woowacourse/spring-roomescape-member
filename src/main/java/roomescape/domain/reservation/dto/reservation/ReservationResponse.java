package roomescape.domain.reservation.dto.reservation;

import java.time.LocalDate;
import roomescape.domain.reservation.dto.reservationtime.ReservationTimeResponse;
import roomescape.domain.reservation.dto.theme.ThemeResponse;
import roomescape.domain.reservation.entity.Reservation;

public record ReservationResponse(Long id, String name, LocalDate date, ReservationTimeResponse time,
                                  ThemeResponse theme) {

    public static ReservationResponse from(final Reservation reservation) {
        final ReservationTimeResponse reservationTimeResponse = ReservationTimeResponse.from(
                reservation.getReservationTime());

        final ThemeResponse themeResponse = ThemeResponse.from(reservation.getTheme());

        return new ReservationResponse(reservation.getId(), reservation.getName(), reservation.getReservationDate(),
                reservationTimeResponse, themeResponse);
    }
}
