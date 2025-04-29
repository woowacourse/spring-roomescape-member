package roomescape.dto;

import java.time.LocalDate;
import roomescape.entity.Reservation;

public record ReservationResponse(
        Long id,
        String name,
        LocalDate date,
        Long timeId
) {

    public static ReservationResponse from(final Reservation reservation) {
        return new ReservationResponse(reservation.id(), reservation.name(),
                reservation.date(),
                reservation.time().id());
    }
}
