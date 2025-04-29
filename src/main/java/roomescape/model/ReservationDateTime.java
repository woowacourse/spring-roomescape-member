package roomescape.model;

public class ReservationDateTime {
    private final ReservationDate date;
    private final ReservationTime time;

    public ReservationDateTime(ReservationDate date, ReservationTime time) {
        this.date = date;
        this.time = time;
    }

    public ReservationDate getDate() {
        return date;
    }

    public ReservationTime getTime() {
        return time;
    }


}
