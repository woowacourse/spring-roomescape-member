package roomescape.controller.reservation;

import roomescape.controller.time.TimeResponse;
import roomescape.domain.Reservation;

import java.time.format.DateTimeFormatter;

public record ReservationResponse(Long id, String name, String date, TimeResponse time) {

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE),
                TimeResponse.from(reservation.getTime())
        );
    }
}
