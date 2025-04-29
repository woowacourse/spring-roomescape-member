package roomescape.entity;

import java.time.LocalTime;
import java.util.Objects;

public class ReservationTime {

    private final Long id;
    private final LocalTime startAt;

    public ReservationTime(Long id, LocalTime startAt) {
        this.id = id;
        this.startAt = startAt;
    }

    public ReservationTime(LocalTime startAt) {
        this(null, startAt);
    }

    public boolean hasConflict(Theme theme, LocalTime anotherTime) {
        LocalTime max = startAt.plusHours(theme.getDuringTime());
        LocalTime min = startAt.minusHours(theme.getDuringTime());
        return anotherTime.isAfter(min) && anotherTime.isBefore(max);
    }

    public boolean isSameId(Long id) {
        return this.id == id;
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ReservationTime that = (ReservationTime) o;
        return Objects.equals(id, that.id) && Objects.equals(startAt, that.startAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startAt);
    }
}
