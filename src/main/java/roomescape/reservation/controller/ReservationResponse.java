package roomescape.reservation.controller;

import roomescape.reservation.domain.Reservation;
import roomescape.reservationtime.controller.ReservationTimeResponse;
import roomescape.theme.controller.ThemeResponse;

import java.time.LocalDate;


public record ReservationResponse(long id, String name, LocalDate date, ReservationTimeResponse time,
                                  ThemeResponse theme) {
    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                ReservationTimeResponse.from(reservation.getTime()),
                ThemeResponse.from(reservation.getTheme())
        );
    }
}

