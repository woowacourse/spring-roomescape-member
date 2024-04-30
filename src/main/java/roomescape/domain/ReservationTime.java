package roomescape.domain;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class ReservationTime {

    private final Long id;
    private final LocalTime startAt;

    public ReservationTime(Long id, LocalTime startAt) {
        this.id = id;
        this.startAt = startAt;
    }

    public ReservationTime(Long id) {
        this(id, null);
    }

    public ReservationTime(LocalTime startAt) {
        this(null, startAt);
    }

    public Long id() {
        return id;
    }

    public LocalTime startAt() {
        return startAt;
    }

    public String startAt(DateTimeFormatter formatter) {
        return startAt.format(formatter);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReservationTime that = (ReservationTime) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
