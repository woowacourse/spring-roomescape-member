package roomescape.reservation.dto.request;

import java.time.LocalDate;
import roomescape.reservation.model.Reservation;
import roomescape.reservationtime.model.ReservationTime;

public record CreateReservationRequest(LocalDate date, String name, Long timeId) {

    public Reservation toReservation(final ReservationTime reservationTime) {
        return new Reservation(
                null,
                this.name,
                this.date,
                reservationTime.getId(),
                reservationTime.getTime()
        );
    }
}
