package roomescape.dto.response;

import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

import java.time.LocalDate;

public record ReservationResponse(
        long id,
        String name,
        LocalDate date,
        ReservationTime time,
        Theme theme
) {
    public static ReservationResponse from(final Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getName().getValue(),
                reservation.getDate(),
                reservation.getTime(),
                reservation.getTheme()
        );
    }
}
