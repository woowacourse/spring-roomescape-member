package roomescape.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import roomescape.exception.InvalidReservationException;

public class Schedule {
    private final ReservationDate date;
    private final ReservationTime time;

    public Schedule(final ReservationDate date, final ReservationTime time) {
        this.date = date;
        this.time = time;
    }

    public String getDate() {
        return date.getValue();
    }

    public String getTime() {
        return time.getStartAt();
    }

    public ReservationTime getReservationTime() {
        return time;
    }
}
