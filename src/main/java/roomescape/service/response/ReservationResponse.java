package roomescape.service.response;

import java.time.format.DateTimeFormatter;
import roomescape.domain.Reservation;

public record ReservationResponse(
        Long id,
        String name,
        String date,
        ReservationTimeResponse time,
        ThemeResponse theme) {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static ReservationResponse from(Reservation reservation) {
        String date = reservation.getDate().format(DATE_FORMATTER);
        ReservationTimeResponse timeResponse = ReservationTimeResponse.from(reservation.getTime());
        ThemeResponse themeResponse = ThemeResponse.from(reservation.getTheme());
        return new ReservationResponse(
                reservation.getId(), reservation.getName().getValue(), date, timeResponse, themeResponse
        );
    }
}
