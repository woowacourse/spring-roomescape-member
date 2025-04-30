package roomescape.dto.response;

import java.time.LocalDate;
import roomescape.domain.Reservation;

public record ReservationResponse(
    Long id,
    String name,
    LocalDate date,
    ReservationTimeResponse time,
    //TODO: dto 간소화?
    ThemeResponse theme
) {

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
            reservation.getId(),
            reservation.getName(),
            reservation.getDate(),
            ReservationTimeResponse.from(reservation.getTime()),
            ThemeResponse.from(reservation.getTheme())
        );
    }
}
