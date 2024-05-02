package roomescape.dto;

import roomescape.domain.Reservation;
import java.time.LocalDate;

public record ReservationResponse(
        Long id,
        String name,
        LocalDate date,
        ReservationTimeResponse time,
        ReservedThemeResponse theme
) {

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                ReservationTimeResponse.from(reservation.getTime()),
                ReservedThemeResponse.from(reservation.getTheme())
        );
    }
}
