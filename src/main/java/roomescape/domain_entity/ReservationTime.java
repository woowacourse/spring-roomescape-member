package roomescape.domain_entity;

import java.time.LocalTime;

public class ReservationTime {
    private Id id;
    private LocalTime startAt;

    public ReservationTime() {
    }

    public ReservationTime(LocalTime startAt) {
        this(Id.empty(), startAt);
    }

    public ReservationTime(Id id) {
        this(id, null);
    }

    public ReservationTime(Id id, LocalTime startAt) {
        this.id = id;
        this.startAt = startAt;
    }

    public ReservationTime copyWithId(Id id) {
        return new ReservationTime(id, startAt);
    }

    public long getId() {
        return id.value();
    }

    public LocalTime getStartAt() {
        return startAt;
    }
}
