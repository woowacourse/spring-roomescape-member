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

    public static ReservationResponse from(Theme theme) { // TODO 필요없는 정보까지 건내주는건 위험하지 않을까?
        return new ReservationResponse(
                null,
                null,
                null,
                null,
                ThemeResponse.from(theme)
        );
    }
}
