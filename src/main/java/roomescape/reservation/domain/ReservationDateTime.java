package roomescape.reservation.domain;

import java.time.LocalDateTime;
import roomescape.reservation.domain.exception.PastReservationException;
import roomescape.time.domain.ReservationTime;

public class ReservationDateTime {

    private final ReservationDate reservationDate;
    private final ReservationTime reservationTime;

    public ReservationDateTime(ReservationDate reservationDate, ReservationTime reservationTime) {
        validatePast(reservationDate, reservationTime);
        this.reservationDate = reservationDate;
        this.reservationTime = reservationTime;
    }

    private void validatePast(ReservationDate reservationDate, ReservationTime reservationTime) {
        LocalDateTime reservationDateTime = LocalDateTime.of(reservationDate.getDate(), reservationTime.getStartAt());
        LocalDateTime now = LocalDateTime.now();

        if (reservationDateTime.isBefore(now)) {
            throw new PastReservationException();
        }
    }

    public ReservationDate getReservationDate() {
        return reservationDate;
    }

    public ReservationTime getReservationTime() {
        return reservationTime;
    }
}
