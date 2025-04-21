package roomescape.time.entity;

import java.time.LocalTime;

public class TimeEntity {
    private final long id;
    private final LocalTime startAt;

    public TimeEntity(long id, LocalTime startAt) {
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
