package roomescape.model;

import java.time.LocalDate;

public class ReservationDateTime {
    private final LocalDate date;
    private final ReservationTime time;

    public ReservationDateTime(LocalDate date, ReservationTime time) {
        this.date = date;
        this.time = time;
    }

    public LocalDate getDate() {
        return date;
    }

    public ReservationTime getTime() {
        return time;
    }


}
