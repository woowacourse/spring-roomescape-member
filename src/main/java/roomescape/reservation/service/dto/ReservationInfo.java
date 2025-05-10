package roomescape.reservation.service.dto;

import java.time.LocalDate;
import roomescape.reservation.domain.Reservation;

public record ReservationInfo(
        Long id,
        String name,
        LocalDate date,
        ReservationTimeInfo time,
        ThemeInfo theme
) {

    public ReservationInfo(final Reservation reservation) {
        this(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                new ReservationTimeInfo(reservation.getTime()),
                new ThemeInfo(reservation.getTheme())
        );
    }
}
