package roomescape.business;

import java.time.LocalTime;

public final class ReservationTime {

    private final Long id;
    private final LocalTime startAt;

    public ReservationTime(Long id, LocalTime startAt) {
        this.id = id;
        this.startAt = startAt;
    }

    public ReservationTime(LocalTime startAt) {
        this(null, startAt);
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }
}
