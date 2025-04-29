package roomescape.domain;

import java.time.LocalTime;

public class ReservationTime {

    private final long id;
    private final LocalTime startAt;

    public ReservationTime(final long id, final LocalTime startAt) {
        this.id = id;
        this.startAt = startAt;
    }

    public ReservationTime(final LocalTime startAt) {
        this(0, startAt);
    }

    public ReservationTime withId(final long id) {
        return new ReservationTime(id, this.startAt);
    }

    public long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }
}
