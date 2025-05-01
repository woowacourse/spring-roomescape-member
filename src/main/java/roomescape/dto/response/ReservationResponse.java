package roomescape.dto.response;

import java.time.LocalDate;
import roomescape.entity.Reservation;

public record ReservationResponse(
        Long id,
        String name,
        LocalDate date,
        ReservationTimeResponse time,
        ThemeResponse theme) {

    public static ReservationResponse of(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                ReservationTimeResponse.of(reservation.getTime()),
                ThemeResponse.of(reservation.getTheme())
        );
    }
}
