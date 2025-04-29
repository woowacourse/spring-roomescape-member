package roomescape.controller.dto;

import java.time.LocalDate;
import roomescape.service.reservation.ReservationTime;

public record ReservationRequest(String name, LocalDate date, Long timeId) {

    public Reservation convertToReservation(final ReservationTime reservationTime) {
        return new Reservation(null, name, date, reservationTime);
    }
}
