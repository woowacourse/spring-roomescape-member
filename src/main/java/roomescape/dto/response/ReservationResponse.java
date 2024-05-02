package roomescape.dto.response;

import java.time.format.DateTimeFormatter;

import roomescape.domain.Reservation;
import roomescape.domain.Theme;

public record ReservationResponse(Long id, String name, String date, ReservationTimeResponse timeResponse, ThemeResponse themeResponse) {

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.id(),
                reservation.name().getName(),
                reservation.date(DateTimeFormatter.ISO_DATE),
                ReservationTimeResponse.from(reservation.time()),
                ThemeResponse.from(reservation.getTheme()
                ));
    }

    public static ReservationResponse from(Theme theme) {
        return new ReservationResponse(
                null,
                null,
                null,
                null,
                ThemeResponse.from(theme)
        );
    }
}
