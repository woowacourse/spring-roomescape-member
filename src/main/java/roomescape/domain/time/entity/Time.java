package roomescape.domain.time.entity;

import java.time.LocalTime;
import java.util.Objects;

public class Time {

    private final Long id;
    private final LocalTime startAt;

    private Time(Long id, LocalTime startAt) {
        this.id = id;
        this.startAt = startAt;
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }

    public static Time create(LocalTime startAt) {
        return new Time(null, startAt);
    }

    public static Time reconstruct(Long id, LocalTime startAt) {
        return new Time(id, startAt);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Time time = (Time) o;
        return Objects.equals(id, time.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
