package roomescape.reservation;

import java.time.LocalDate;
import roomescape.reservationtime.ReservationTimeResponse;
import roomescape.theme.ThemeResponse;


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

