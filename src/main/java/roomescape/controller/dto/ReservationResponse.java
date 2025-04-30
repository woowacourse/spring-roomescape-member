package roomescape.controller.dto;

import java.time.LocalDate;
import roomescape.service.reservation.Reservation;

public record ReservationResponse(
        Long id,
        String name,
        LocalDate date,
        ReservationTimeResponse time,
        ThemeResponse theme
) {

    public ReservationResponse(final Reservation reservation) {
        this(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                new ReservationTimeResponse(reservation.getTime()),
                new ThemeResponse(reservation.getTheme())
        );
    }
}
