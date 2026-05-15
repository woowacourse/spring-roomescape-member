package roomescape.dto.reservation;

import java.time.format.DateTimeFormatter;
import roomescape.domain.reservation.ReservationWithTimeAndTheme;
import roomescape.dto.reservationTime.ReservationTimeResponse;
import roomescape.dto.theme.ThemeResponse;

public record ReservationResponse(long id, String name, String date, ReservationTimeResponse time, ThemeResponse reservationTheme) {
    public static ReservationResponse from(ReservationWithTimeAndTheme reservation) {
        return new ReservationResponse(
                reservation.id(),
                reservation.name(),
                reservation.date().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                ReservationTimeResponse.from(reservation.time()),
                ThemeResponse.from(reservation.reservationTheme())
        );
    }
}
