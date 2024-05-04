package roomescape.domain;

import static roomescape.exception.ExceptionType.EMPTY_TIME;

import java.time.LocalTime;
import java.util.Objects;
import roomescape.exception.RoomescapeException;

public class ReservationTime {
    private final Long id;
    private final LocalTime startAt;

    public ReservationTime(LocalTime startAt) {
        this(null, startAt);
    }

    public ReservationTime(Long id, LocalTime startAt) {
        if (startAt == null) {
            throw new RoomescapeException(EMPTY_TIME);
        }
        this.id = id;
        this.startAt = startAt;
    }

    public boolean isIdOf(long id) {
        return this.id == id;
    }

    public long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (startAt != null ? startAt.hashCode() : 0);
        return result;
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
    public String toString() {
        return "ReservationTime{" +
                "id=" + id +
                ", startAt=" + startAt +
                '}';
    }
}
