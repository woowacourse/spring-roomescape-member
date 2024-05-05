package roomescape.domain;

import java.time.LocalTime;

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

    public LocalTime getTime() {
        return time.getStartAt();
    }

    public ReservationTime getReservationTime() {
        return time;
    }
}
