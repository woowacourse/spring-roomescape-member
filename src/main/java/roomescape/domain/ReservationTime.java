package roomescape.domain;

import java.time.LocalTime;

public class ReservationTime {
    private final long id;
    private final LocalTime startAt;

    public ReservationTime(LocalTime startAt) {
        this(0L, startAt);
    }

    public ReservationTime(long id, LocalTime startAt) {
        this.id = id;
        this.startAt = startAt;
    }

    public long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }

    public boolean hasId(Long id) {
        return this.id == id;
    }
}
