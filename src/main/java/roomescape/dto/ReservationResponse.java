package roomescape.dto;

import java.time.LocalDate;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

public record ReservationResponse(
        long id,
        String name,
        LocalDate date,
        ReservationTime time
) {
    public static ReservationResponse from(final Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getName().getValue(),
                reservation.getDate(),
                reservation.getTime()
        );
    }
}
