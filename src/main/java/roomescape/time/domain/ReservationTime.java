package roomescape.time.domain;

import java.time.LocalTime;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonFormat;

public record ReservationTime(long id, @JsonFormat(pattern = "HH:mm") LocalTime startAt) {
    private static final long UNDEFINED = 0;
    public ReservationTime{
        validateStartAt(startAt);
    }
    public ReservationTime(@JsonFormat(pattern = "HH:mm") LocalTime startAt) {
        this(UNDEFINED, startAt);
    }

    public void validateStartAt(LocalTime startAt) {
        if (startAt == null) {
            throw new IllegalArgumentException("Start time cannot be null");
        }
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        ReservationTime target = (ReservationTime) object;
        return id == target.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
