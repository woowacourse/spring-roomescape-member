package roomescape.dto.response;

import java.time.format.DateTimeFormatter;

import roomescape.domain.Name;
import roomescape.domain.Reservation;

public record ReservationResponse(Long id, String name, String date, ReservationTimeResponse time, ThemeResponse theme) {

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.id(),
                reservation.name().getName(),
                reservation.date(DateTimeFormatter.ISO_DATE),
                ReservationTimeResponse.from(reservation.time()),
                ThemeResponse.from(reservation.getTheme()
                ));
    }
}
