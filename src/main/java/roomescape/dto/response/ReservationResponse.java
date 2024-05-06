package roomescape.dto.response;

import java.time.format.DateTimeFormatter;

import roomescape.domain.Reservation;
import roomescape.domain.Theme;

public record ReservationResponse(Long id, String name, String date, ReservationTimeResponse time, ThemeResponse theme) {

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getName().getName(),
                reservation.getDate(DateTimeFormatter.ISO_DATE),
                ReservationTimeResponse.from(reservation.getTime()),
                ThemeResponse.from(reservation.getTheme()
                ));
    }
}
