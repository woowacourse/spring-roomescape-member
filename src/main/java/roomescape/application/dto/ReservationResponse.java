package roomescape.application.dto;

import java.time.LocalDate;
import roomescape.domain.Reservation;

public record ReservationResponse(
        Long id,
        String name,
        LocalDate date,
        ReservationTimeResponse reservationTime,
        ThemeResponse theme
) {

    public ReservationResponse(Reservation reservation) {
        this(
                reservation.getId(),
                reservation.getMember().getName(),
                reservation.getDate(),
                new ReservationTimeResponse(reservation.getReservationTime()),
                new ThemeResponse(reservation.getTheme())
        );
    }
}
