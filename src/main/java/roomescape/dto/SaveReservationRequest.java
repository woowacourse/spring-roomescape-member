package roomescape.dto;

import roomescape.domain.ClientName;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

import java.time.LocalDate;

public record SaveReservationRequest(LocalDate date, String name, Long timeId) {
    public Reservation toReservation(final ReservationTime reservationTime) {
        return new Reservation(
                new ClientName(name),
                date,
                reservationTime
        );
    }
}
