package roomescape.domain.reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Schedule {
    private final ReservationDate date;
    private final ReservationTime time;

    public Schedule(ReservationDate date, ReservationTime time) {
        this.date = date;
        this.time = time;
    }

    public boolean isBeforeNow() {
        return LocalDateTime.of(date.getDate(), time.getStartAt()).isBefore(LocalDateTime.now());
    }

    public LocalDate getDate() {
        return date.getDate();
    }

    public LocalTime getTime() {
        return time.getStartAt();
    }

    public ReservationTime getReservationTime() {
        return time;
    }
}
