package roomescape.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import roomescape.exception.reservation.ReservationInPastException;

public class ReservationDateTime {
    private final LocalDate date;
    private final ReservationTime time;

    public ReservationDateTime(LocalDate date, ReservationTime time) {
        validateInPast(date, time);
        this.date = date;
        this.time = time;
    }

    private void validateInPast(LocalDate date, ReservationTime time) {
        if (LocalDateTime.now().isAfter(LocalDateTime.of(date, time.getStartAt()))) {
            throw new ReservationInPastException();
        }
    }

    public LocalDate getDate() {
        return date;
    }

    public ReservationTime getTime() {
        return time;
    }
}
