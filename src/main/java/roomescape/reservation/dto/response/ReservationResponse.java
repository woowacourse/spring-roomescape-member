package roomescape.reservation.dto.response;

import java.time.LocalDate;
import roomescape.reservation.domain.Reservation;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.dto.response.ReservationTimeResponse;

public record ReservationResponse(
        long id,
        String name,
        LocalDate date,
        ReservationTimeResponse time
) {
    public static ReservationResponse from(Reservation reservation, ReservationTime reservationTime) {
        return new ReservationResponse(reservation.getId(), reservation.getName(), reservation.getDate(),
                ReservationTimeResponse.from(reservationTime)
        );
    }
}
