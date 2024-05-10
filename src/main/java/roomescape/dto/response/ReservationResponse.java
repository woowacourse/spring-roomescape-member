package roomescape.dto.response;

import java.time.format.DateTimeFormatter;
import roomescape.domain.Reservation;

public record ReservationResponse(
        Long id,
        String name,
        String date,
        ReservationTimeResponse time,
        ThemeResponse theme
) {

    public ReservationResponse(Reservation reservation) {
        this(
                reservation.getId(),
                reservation.getLoginMember().getName().name(),
                reservation.getDate(DateTimeFormatter.ISO_DATE),
                new ReservationTimeResponse(reservation.getTime()),
                new ThemeResponse(reservation.getTheme())
        );
    }
}
