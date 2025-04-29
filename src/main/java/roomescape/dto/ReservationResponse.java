package roomescape.dto;

import java.time.LocalDate;
import roomescape.domain.Reservation;

public record ReservationResponse(long id, String name, LocalDate date, ReservationTimeResponse time) {

    public static ReservationResponse of(Reservation reservation) {
        return new ReservationResponse(reservation.getId(), reservation.getName(),
                reservation.getDate(), ReservationTimeResponse.of(reservation.getTime()));
    }
}
