package roomescape.dto.reservation;

import java.time.format.DateTimeFormatter;
import roomescape.domain.reservation.Reservation;
import roomescape.dto.reservationTime.ReservationTimeResponse;
import roomescape.dto.theme.ReservationThemeResponse;

public record ReservationResponse(long id, String name, String date, ReservationTimeResponse time, ReservationThemeResponse reservationTheme) {
    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.id(),
                reservation.name(),
                reservation.date().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                ReservationTimeResponse.from(reservation.time()),
                ReservationThemeResponse.from(reservation.reservationTheme())
        );
    }
}
