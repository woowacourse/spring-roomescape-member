package roomescape.reservationtime;

import java.time.LocalTime;

public class ReservationTime {

    private Long id;
    private final LocalTime startAt;

    public ReservationTime(final LocalTime startAt) {
        this.startAt = startAt;
    }

    public ReservationTime(final Long id, final LocalTime startAt) {
        this.id = id;
        this.startAt = startAt;
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }
}
