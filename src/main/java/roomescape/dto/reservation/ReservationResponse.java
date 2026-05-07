package roomescape.dto.reservation;

import java.time.format.DateTimeFormatter;
import roomescape.domain.Reservation;
import roomescape.dto.reservationTime.ReservationTimeResponse;
import roomescape.dto.theme.ThemeResponse;

public record ReservationResponse(
    Long id,
    String name,
    String date,
    ReservationTimeResponse time,
    ThemeResponse theme
) {

    public static ReservationResponse from(Reservation reservation) {
        String name = reservation.getName().value();
        String date = reservation.getDateValue().format(DateTimeFormatter.ISO_LOCAL_DATE);
        ReservationTimeResponse time = ReservationTimeResponse.from(reservation.getTime());

        return new ReservationResponse(
            reservation.getId(),
            name,
            date,
            time,
            ThemeResponse.from(reservation.getTheme())
        );
    }
}
