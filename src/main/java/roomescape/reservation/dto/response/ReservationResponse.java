package roomescape.reservation.dto.response;

import roomescape.reservation.entity.Theme;
import roomescape.reservation.entity.Reservation;

import java.time.LocalDate;

public record ReservationResponse(
        Long id,
        String name,
        LocalDate date,
        TimeResponse time,
        ThemeResponse theme
) {

    public static ReservationResponse from(Reservation reservation, Theme theme) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                TimeResponse.from(reservation.getTime()),
                ThemeResponse.from(theme)
        );
    }
}
