package roomescape.reservation.entity;

import java.time.LocalTime;

public class ReservationTimeEntity {
    private final long id;
    private final LocalTime startAt;

    public ReservationTimeEntity(long id, LocalTime startAt) {
        this.id = id;
        this.startAt = startAt;
    }

    public long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }
}
