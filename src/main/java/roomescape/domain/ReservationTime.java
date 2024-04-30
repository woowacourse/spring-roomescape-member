package roomescape.domain;

import java.sql.Time;
import java.time.LocalTime;
import java.util.Objects;

public class ReservationTime {
    private final Long id;
    private final LocalTime startAt;

    public ReservationTime(Long id, Time startAt) {
        this(id, startAt.toLocalTime());
    }

    public ReservationTime(String startAt) {
        this(null, LocalTime.parse(startAt));
    }

    public ReservationTime(Long id, LocalTime startAt) {
        this.id = id;
        this.startAt = startAt;
    }

    public boolean hasSameId(Long id) {
        return Objects.equals(this.id, id);
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }
}
