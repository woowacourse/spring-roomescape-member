package roomescape.user.dto;

import java.time.LocalDate;
import roomescape.user.domain.Reservation;

public record ReservationResponse(
    Long id,
    String name,
    LocalDate date,
    TimeResponse time
) {

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
            reservation.getId(),
            reservation.getName(),
            reservation.getDate(),
            TimeResponse.of(reservation.getTime())
        );
    }
}
