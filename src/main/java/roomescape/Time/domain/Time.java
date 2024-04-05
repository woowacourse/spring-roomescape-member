package roomescape.Time.domain;

import java.time.LocalTime;
import java.util.Objects;

public class Time {
    private long id;
    private final LocalTime startAt;

    public Time(LocalTime startAt) {
        this(0, startAt);
    }

    public Time(long id, LocalTime startAt) {
        this.id = id;
        this.startAt = startAt;
    }

    public long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Time time = (Time) o;
        return id == time.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
