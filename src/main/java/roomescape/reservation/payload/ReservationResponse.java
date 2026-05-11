package roomescape.reservation.payload;

import java.time.LocalDate;
import roomescape.reservation.entity.Reservation;
import roomescape.reservationtime.payload.ReservationTimeResponse;
import roomescape.theme.payload.ThemeResponse;

public record ReservationResponse(
        Long id,
        String name,
        LocalDate date,
        ReservationTimeResponse time,
        ThemeResponse theme
) {

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
