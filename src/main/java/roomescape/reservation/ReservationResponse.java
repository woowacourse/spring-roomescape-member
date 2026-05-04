package roomescape.reservation;

import java.time.LocalDate;

import roomescape.reservationtime.ReservationTimeResponse;
import roomescape.theme.ThemeResponse;


public record ReservationResponse(long id, ThemeResponse themeResponse, String name, LocalDate date, ReservationTimeResponse time) {
    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                ThemeResponse.from(reservation.getTheme()),
                reservation.getName(),
                reservation.getDate(),
                ReservationTimeResponse.from(reservation.getTime())
        );
    }
}

