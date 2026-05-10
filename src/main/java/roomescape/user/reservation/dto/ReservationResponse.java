package roomescape.user.reservation.dto;

import java.time.LocalDate;
import roomescape.user.reservationtime.dto.TimeResponse;
import roomescape.user.reservation.Reservation;

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
