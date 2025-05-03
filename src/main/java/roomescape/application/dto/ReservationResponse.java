package roomescape.application.dto;

import java.time.LocalDate;
import roomescape.domain.Reservation;

public record ReservationResponse(
        Long id, String name, LocalDate date, ReservationTimeResponse time, ThemeResponse theme
) {
    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                ReservationTimeResponse.from(reservation.getReservationTime()),
                new ThemeResponse(reservation.getTheme())
        );
    }
}
