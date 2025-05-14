package roomescape.reservation.domain;

import java.time.LocalDateTime;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservation.exception.ReservationInPastException;

public class ReservationDateTime {
    private final ReservationDate date;
    private final ReservationTime time;

    public ReservationDateTime(ReservationDate date, ReservationTime time) {
        validateInPast(date, time);
        this.date = date;
        this.time = time;
    }

    private void validateInPast(ReservationDate date, ReservationTime time) {
        if (LocalDateTime.now().isAfter(LocalDateTime.of(date.getDate(), time.getStartAt()))) {
            throw new ReservationInPastException();
        }
    }

    public ReservationDate getDate() {
        return date;
    }

    public ReservationTime getTime() {
        return time;
    }
}
