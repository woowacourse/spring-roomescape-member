package roomescape.dto;

import java.time.LocalDate;
import roomescape.entity.Reservation;

public record ReservationResponse(
        long id,
        String name,
        LocalDate date,
        ReservationTimeResponse time
) {

    public static ReservationResponse from(final Reservation reservation) {
        return new ReservationResponse(reservation.id(), reservation.name(),
                reservation.date(),
                ReservationTimeResponse.from(reservation.time())
        );
    }
}
