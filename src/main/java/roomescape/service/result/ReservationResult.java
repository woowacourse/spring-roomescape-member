package roomescape.service.result;

import java.time.LocalDate;
import roomescape.domain.Reservation;

public record ReservationResult(long id, String name, LocalDate date, ReservationTimeResult time) {

    public static ReservationResult from(Reservation reservation) {
        return new ReservationResult(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                ReservationTimeResult.from(reservation.getTime())
        );
    }
}
