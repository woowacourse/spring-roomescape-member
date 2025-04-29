package roomescape.model;

import java.time.LocalTime;
import java.util.Objects;

public class ReservationTime {
    private final Long id;
    private final LocalTime startAt;

    public ReservationTime(Long id, LocalTime startAt) {
        this.id = Objects.requireNonNull(id);
        this.startAt = Objects.requireNonNull(startAt);
    }

    public ReservationTime(LocalTime startAt) {
        this.id = null;
        this.startAt = Objects.requireNonNull(startAt);
    }

    public ReservationTime(Long id) {
        this.id = id;
        this.startAt = null;
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }
}
